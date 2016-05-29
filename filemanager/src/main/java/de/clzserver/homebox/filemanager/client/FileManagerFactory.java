package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.filemanager.onlinecheck.OnlineStatus;


public class FileManagerFactory {
	


	private static FileManagerFactory single = null;
	private OnlineStatus status_old = null;
	private IFileManager fileManagerOld;

	private FileManagerFactory(){
		//ich bin nur da um da zu sein
	}
	
	// use getShape method to get object of type shape
	public IFileManager getFileManager(OnlineStatus status) {
		if (status_old == null) {
			status_old = status;
		}

		boolean change = !status.equals(status_old);
		status_old = status;
		if (change)
			return fileManagerOld;
		switch (status) {

		case Online:
			fileManagerOld = new ClientFileManager("pfanne.dyndns.org");
			break;
		case Offline:
			fileManagerOld = new ServerFileManager();
			break;
		case Lan:
			fileManagerOld = new ClientFileManager("192.168.2.114");
			break;
		}
		return fileManagerOld;
	}

	public static FileManagerFactory getInstance() {
		if (single == null)
			single = new FileManagerFactory();
		return single;
	}

}
