package de.clzserver.homebox.filemanager.server;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.TreeMap;

import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.remote.IRemoteFile;

public class RemoteFileServer implements IFileServer{

	private static Map<String, File> m_files = null;
	private static Map<String, Boolean> m_lock_table = null;
	
	public RemoteFileServer() {
		init();
	}
	
	private void init() {
		if (m_files == null)
			m_files = new TreeMap<String, File>();
		if (m_lock_table == null)
			m_lock_table = new TreeMap<String, Boolean>();
	}
	
	private File getData(String file, boolean exclusiv) throws IOException{
		File erg = null;
		
		if (m_lock_table.containsKey(file)) {
			if (m_lock_table.get(file)) {
				return null;
			}
			m_lock_table.put(file, exclusiv);
			return m_files.get(file);
		}

		erg = new File(file);
		
		if (erg.exists()) {
			m_files.put(file, erg);
			m_lock_table.put(file, exclusiv);
			return erg;
		}
		throw new IOException("File konnte nicht gefunden werden!");
	}
	
	@Override
	public IRemoteFile getFile(String file, boolean exclusiv)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRemoteFile getFile(String file, String user, boolean exclusiv)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean commitFile(IRemoteFile rfile, boolean done)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

}
