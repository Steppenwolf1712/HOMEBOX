package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.filemanager.remote.IRemoteFile;
import de.clzserver.homebox.filemanager.remote.base.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

class ClientFileManager extends FileManager{

	public ClientFileManager(String host) {
		super(host);
	}

	@Override
	public File getFile(String file) throws IOException {
		IRemoteFile rfile = server.getFile(file, null, false);

		File temp = new File(rfile.getFileLocation());
		if (!temp.exists()) {
			temp.mkdirs();
			temp.createNewFile();
		}

		rfile.safeContentAt(temp);

		return temp;
	}

	@Override
	public File getFile(String file, String user, boolean exclusive) throws IOException {
		IRemoteFile rfile = server.getFile(file, user, exclusive);

		File temp = new File(rfile.getFileLocation());
		if (!temp.exists()) {
			temp.mkdirs();
			temp.createNewFile();
		}

		rfile.safeContentAt(temp);

		return temp;
	}

	@Override
	public File getFile(String file, String user) throws IOException {
		IRemoteFile rfile = server.getFile(file, user);

		File temp = new File(rfile.getFileLocation());
		if (!temp.exists())
			temp.createNewFile();

		rfile.safeContentAt(temp);

		return temp;
	}

	@Override
	public boolean commitFile(File content, String remoteFilePath, String user, boolean exclusive, boolean done) throws RemoteException {
			IRemoteFile rfile = new RemoteFile(content, user, remoteFilePath, exclusive);

			return server.commitFile(rfile, user, remoteFilePath, done);
	}

	@Override
	public String[] getDirectoryContent(String directoryPath) throws RemoteException {
		return server.getDirectoryContent(directoryPath);
	}

	@Override
	public String[] getDirectoryContent(String filePath, String user) throws RemoteException {
		return server.getDirectoryContent(filePath, user);
	}

	@Override
	public boolean deleteFile(String filePath, String user) throws RemoteException {
		return server.deleteFile(filePath, user);
	}

	@Override
	public boolean deleteFile(String filePath) throws RemoteException {
		return server.deleteFile(filePath);
	}
}
