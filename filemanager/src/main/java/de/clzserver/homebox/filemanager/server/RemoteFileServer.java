package de.clzserver.homebox.filemanager.server;

import java.io.*;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.remote.IRemoteFile;
import de.clzserver.homebox.filemanager.remote.base.RemoteFile;

public class RemoteFileServer implements IFileServer{

	private static Map<String, String> m_lock_table = null;
	
	public RemoteFileServer() {
		init();
	}
	
	private void init() {
		if (m_lock_table == null)
			m_lock_table = new TreeMap<String, String>();
	}
	
//	private File getData(String file, boolean exclusiv) throws IOException{
//		File erg = null;
//
//		if (m_lock_table.containsKey(file)) {
//			if (m_lock_table.get(file)) {
//				return null;
//			}
//			m_lock_table.put(file, exclusiv);
//			return m_files.get(file);
//		}
//
//		erg = new File(file);
//
//		if (erg.exists()) {
//			m_files.put(file, erg);
//			m_lock_table.put(file, exclusiv);
//			return erg;
//		}
//		throw new IOException("File konnte nicht gefunden werden!");
//	}
	
	@Override
	public IRemoteFile getFile(String filePath, String user, boolean exclusive)
			throws RemoteException {
		if (exclusive) {
			String user_lock = m_lock_table.get(filePath);
			if (user_lock == null || user_lock.equals(user)) {
				m_lock_table.put(filePath, user);
				File content = new File(filePath);

				HBPrinter.getInstance().printMSG(this.getClass(), "Anfrage auf File: \n\t"+ filePath+"Gebe File Zurück:="+content.getAbsolutePath());
				return new RemoteFile(content, user, filePath, true);
			} else {
				HBPrinter.getInstance().printMSG(this.getClass(), "Anfrage auf File: \n\t"+ filePath+"Es kann kein File zurückgegeben werden!");
				throw new RemoteException(this.getClass()+": Anfrage auf File: \n\t"+ filePath+"Es kann kein File zurückgegeben werden!");
			}
		} else {
			String user_lock = m_lock_table.get(filePath);
			if (user_lock == null || m_lock_table.get(filePath).equals(user)) {
				m_lock_table.remove(filePath);
				File content = new File(filePath);
				HBPrinter.getInstance().printMSG(this.getClass(), "Anfrage auf File: \n\t"+ filePath+"Gebe File Zurück:="+content.getAbsolutePath());
				return new RemoteFile(content, user, filePath);
			} else {
				HBPrinter.getInstance().printMSG(this.getClass(), "Anfrage auf File: \n\t"+ filePath+"Es kann kein File zurückgegeben werden!");
				throw new RemoteException(this.getClass()+": Anfrage auf File: \n\t"+ filePath+"Es kann kein File zurückgegeben werden!");
			}
		}
	}

	@Override
	public IRemoteFile getFile(String file, String user)
			throws RemoteException {
		String filePath = Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY)+user+"\\"+file;
		File content = new File(filePath);
		HBPrinter.getInstance().printMSG(this.getClass(), "Anfrage auf File: \n\t"+ file+"Gebe File Zurück:="+content.getAbsolutePath());

		if (content.exists())
			return new RemoteFile(content, filePath);
		else
			throw new RemoteException(this.getClass().toString()+": Das File existiert nicht auf dem Server!");//TODO: Evtl sollte ich eine Remote methode implementieren die die existenz von Dateien auf dem Server zurückgibt!
	}

	@Override
	public boolean commitFile(IRemoteFile rfile, String user, String identifier, boolean done)
			throws RemoteException {
		boolean rechte = rfile.hasRights();

		String currentLock = m_lock_table.get(rfile.getFileLocation());
		try {
			if (rechte) {
				if (currentLock != null && currentLock.equals(user)) {
					File temp = rfile.getFile();

					FileChannel in = new FileInputStream(temp).getChannel();

					File target = new File(rfile.getFileLocation());

					if (!target.exists())
						target.createNewFile();

					FileChannel out =  new FileOutputStream(target).getChannel();

					in.transferTo(0, in.size(), out);

					in.close();
					out.close();

					if (done)
						m_lock_table.remove(rfile.getFileLocation());

					return true;
				} else {
					HBPrinter.getInstance().printMSG(this.getClass(), "Konnte das RemoteFile " + rfile.getFileLocation() + " nicht commiten!\n" +
							"Das File wurde bereits von " + currentLock + " gelockt und der user " + user + " hat nicht die benötigten Rechte!");
					throw new RemoteException(this.getClass().toString()+": Konnte das RemoteFile " + rfile.getFileLocation() + " nicht commiten!\n" +
							"Das File wurde bereits von " + currentLock + " gelockt und der user " + user + " hat nicht die benötigten Rechte!");
				}
			}

			if (rfile.getUser() == null) {
				if (currentLock != null) {
					HBPrinter.getInstance().printMSG(this.getClass(), "Konnte das RemoteFile " + rfile.getFileLocation() + "nicht commiten!\n" +
							"Das File wurde bereits von " + currentLock + " gelockt und kann ohne die benötigten Rechte nicht beschrieben werden!");
					throw new RemoteException(this.getClass().toString()+": Konnte das RemoteFile " + rfile.getFileLocation() + "nicht commiten!\n" +
							"Das File wurde bereits von " + currentLock + " gelockt und kann ohne die benötigten Rechte nicht beschrieben werden!");
				}

				File target = new File(rfile.getFileLocation());

				if (!target.exists())
					target.createNewFile();

				HBPrinter.getInstance().printMSG(this.getClass(), "Schreibe eingehende Änderungen in das file: " + rfile.getFileLocation());
				rfile.safeContentAt(target);//TODO: Diese speicher Methode muss evtl die anderen ersetzen. Das sollte aber erst evaluiert werden, ob das nötig ist

				return true;
			}


			File target = new File(Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY.toString())+
					rfile.getUser()+"\\"+rfile.getFileLocation());

			if (!target.exists())
				target.createNewFile();

			HBPrinter.getInstance().printMSG(this.getClass(), "Schreibe eingehende Änderungen in das file: " + rfile.getFileLocation());
			rfile.safeContentAt(target);

			return true;
		} catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte ein File beim commit auf den Server nicht finden!", e);
			throw new RemoteException(""+this.getClass().toString()+": Konnte ein File beim commit auf den Server nicht finden!\n" +
					e.getMessage());
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Es gab einen Ein-/Ausgabefehler beim commit einer Dateiänderung!", e);
			throw new RemoteException(""+this.getClass().toString()+": Es gab einen Ein-/Ausgabefehler beim commit einer Dateiänderung!\n" +
					e.getMessage());
		}
	}

	@Override
	public String[] getDirectoryContent(String filePath) throws RemoteException {
		return getDirectoryContent(filePath, null);
	}

	@Override
	public String[] getDirectoryContent(String filePath, String user) throws RemoteException {
		ArrayList<String> erg = new ArrayList<String>();

		if (user != null && user.length()>0)
			filePath = Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY)+Config.getInstance().getValue(Config.USER_STRING_KEY)+
					"/"+filePath;

		File direc = new File(filePath);

		if (direc.exists() && direc.isDirectory()) {

			File[] contents = direc.listFiles();

			if (contents.length == 0)
				return new String[0];

			for (File f: contents) {
				erg.add(f.getName());
			}

			String[] ret = new String[erg.size()];

			ret = erg.toArray(ret);
			return ret;
		} else {
			throw new RemoteException(this.getClass().toString()+": Der übergebene String enthält keinen Pfad zu einer gültigen Directory");
		}
	}

	@Override
	public boolean deleteFile(String filePath, String user) throws RemoteException {
		String currentUser = m_lock_table.get(filePath);

		if (user != null && user.length()>0) {
			filePath = Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY) + Config.getInstance().getValue(Config.USER_STRING_KEY) +
					"/" + filePath;
		} else {
			if (currentUser != null && !currentUser.equals(user)) {
				throw new RemoteException(this.getClass().toString()+": Das referenzierte File steht zurzeit unter exclusivem Zugriff von "+currentUser+"!");
			}
		}

		File toDelete = new File(filePath);

		if (!toDelete.exists() || toDelete.isDirectory())
			return false;

		return toDelete.delete();
	}

	@Override
	public boolean deleteFile(String filePath) throws RemoteException {
		return deleteFile(filePath, null);
	}

}
