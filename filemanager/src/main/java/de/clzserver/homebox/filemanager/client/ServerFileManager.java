package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.filemanager.FileManagerHandle;
import de.clzserver.homebox.filemanager.remote.IRemoteFile;
import de.clzserver.homebox.filemanager.remote.base.RemoteFile;

import java.io.*;
import java.rmi.RemoteException;

class ServerFileManager implements IFileManager{

	@Override
	public File getFile(String file) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {//Benutze die RMI-Methoden, damit das locking mitbenutzt werden kann...
			IRemoteFile rFile = instance.getService().getFile(file, null, false);

			return new File(rFile.getFileLocation());

			//rFile.safeContentAt(temp);//Ziemlich unnötig da er selbst der server ist, wird sich die Datei schon nicht sofort geändert haben nach dem lesen
		} else {
			return new File(file);
		}
	}

	@Override
	public File getFile(String file, String user, boolean exclusive) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {
			IRemoteFile rfile = instance.getService().getFile(file, user, exclusive);

			return new File(rfile.getFileLocation());
//			if (!temp.exists())
//				temp.createNewFile();
//
//			rfile.safeContentAt(temp);
//
//			return temp;
		} else {
			return new File(file);
		}
	}

	@Override
	public File getFile(String file, String user) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {
			IRemoteFile rfile = instance.getService().getFile(file, user);

			return new File(rfile.getFileLocation());
//			if (!temp.exists())
//				temp.createNewFile();
//
//			rfile.safeContentAt(temp);
//
//			return temp;
		} else {
			return new File((user==null?"": Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY)+
					user+"\\")+file);
		}
	}

	@Override
	public boolean commitFile(File content, String remoteFilePath, String user, boolean exclusive, boolean done) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {
			IRemoteFile rfile = new RemoteFile(content, user, remoteFilePath, exclusive);

			return instance.getService().commitFile(rfile, user, remoteFilePath, done);
//			if (!temp.exists())
//				temp.createNewFile();
//
//			rfile.safeContentAt(temp);
//
//			return temp;
		} else {
			byte[] m_byte_a_content = new byte[(int) content.length()];
			try {
				FileInputStream reader = new FileInputStream(content);
				reader.read(m_byte_a_content);

				reader.close();
			} catch (FileNotFoundException e) {
				HBPrinter.getInstance().printError(
						this.getClass(),
						"Konnte das gewünschte File "+content +" nicht finden!", e);
			} catch (IOException e) {
				HBPrinter.getInstance().printError(
						this.getClass(),
						"Es trat ein Ein/Ausgabe-Fehler beim auslesen der Datei "
								+ content  + " auf!", e);
			}
			File target = new File(remoteFilePath);
			try {

				FileOutputStream writer = new FileOutputStream(target);
				writer.write(m_byte_a_content);
				writer.flush();
				writer.close();

				return true;
			} catch (FileNotFoundException e) {
				HBPrinter
						.getInstance()
						.printError(
								this.getClass(),
								"Das File "
										+ remoteFilePath
										+ " konnte nicht gefunden werden (Schreibzugriff)!",
								e);
			} catch (IOException e) {
				HBPrinter.getInstance().printError(this.getClass(), "Fehler beim Schreiben oder Erstellen des Files "+remoteFilePath, e);
			}
			return false;
		}
	}

	@Override
	public String[] getDirectoryContent(String directoryPath) throws RemoteException {
		return getDirectoryContent(directoryPath, null);
	}

	@Override
	public String[] getDirectoryContent(String filePath, String user) throws RemoteException {
		String directoryPath = (user==null?"": Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY)+
			user+"\\")+filePath;

		File[] list = new File(directoryPath).listFiles();
		String[] erg = new String[list.length];

		for(int i = 0; i<list.length; i++) {
			erg[i] = list[i].getPath();
		}

		return erg;
	}

	@Override
	public boolean deleteFile(String filePath, String user) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {
			return instance.getService().deleteFile((user==null?"": Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY)+
					user+"\\")+filePath, user);
		} else {
			return new File(filePath).delete();
		}
	}

	@Override
	public boolean deleteFile(String filePath) throws RemoteException {
		FileManagerHandle instance = FileManagerHandle.getInstance();
		if (instance.isRunning()) {
			return instance.getService().deleteFile(filePath);
		} else {
			return new File(filePath).delete();
		}
	}
}
