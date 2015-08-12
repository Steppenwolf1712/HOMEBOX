package de.clzserver.homebox.filemanager;

import java.util.Observable;

import de.clzserver.homebox.filemanager.client.FileManagerFactory;
import de.clzserver.homebox.filemanager.client.IFileManager;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineStatus;
import de.clzserver.homebox.filemanager.server.RemoteFileServer_Starter;

/**
 * Diese Klasse, hält den online Status und die FileManager-Instanz. Sie ist als Singleton geplant, damit von überall auf den momentan aktuellen FileManager zugegriffen werden kann.
 * Geplant sind drei möglich unterschiedliche Instanzen des FileManagers:
 * 	- Eine Online Instanz, zu nutzen wenn man unterwegs ist und nur über das Internet an die Serverseitigen informationen kommen kann.
 * 	- Eine LAN Instanz, welche die Files und den Server über Lan erreichen könnte, es aber nicht natürlich nicht tut, damit der Server die Kontrolle behählt (Es ändert sich nur die URL zum Erreichen und vllt der Port). Also von zu Hause aus zu nutzen sein soll.
 * 	- Eine Offline Instanz, die lediglich die Arbeit mit lokalen Files erlaubt und alle Plugins die den Server braucht abschaltet.
 * 
 * @author Marc Janßen
 *
 */
public class FileManagerHandle extends Observable{

	private FileManagerHandle single = null;
	private OnlineStatus m_onlinestatus = null;
	
	private FileManagerHandle() {
		init();
	}
	
	public FileManagerHandle getInstance() {
		if (single == null)
			single = new FileManagerHandle();
		return single;
	}
	
	private void init() {
		status_update();
	}
	
	private void status_update() {
		OnlineStatus temp_onlinestatus = OnlineChecker.checkStatus();
		
		if (m_onlinestatus != temp_onlinestatus && temp_onlinestatus != null)
			notifyObservers(temp_onlinestatus);
		
		m_onlinestatus = temp_onlinestatus;
	}
	
	public OnlineStatus getOnlineStatus() {
		return this.m_onlinestatus;
	}
	
	public IFileManager getFileManager() {
		status_update();
		return FileManagerFactory.getInstance().getFileManager(m_onlinestatus);
	}
	
	/**
	 * Die Methode startet den RemoteFileServer.
	 * Von daher soll die Methode nur von der ServerInstanz der HomeBox aufgerufen werden.
	 */
	public void startServer() {
		new RemoteFileServer_Starter();
	}
}
