package de.clzserver.homebox.filemanager;

import java.util.Observable;

import de.clzserver.homebox.filemanager.client.FileManagerFactory;
import de.clzserver.homebox.filemanager.client.IFileManager;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineStatus;
import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.server.RemoteFileServer;
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

	private static FileManagerHandle single = null;

	private RemoteFileServer currentServer;
	private boolean isRunning = false;

	private FileManagerHandle() {
		init();
	}
	
	public static FileManagerHandle getInstance() {
		if (single == null)
			single = new FileManagerHandle();
		return single;
	}
	
	private void init() {
		startServer();
	}

	
	/**
	 * Die Methode startet den RemoteFileServer.
	 * Von daher soll die Methode nur von der ServerInstanz der HomeBox aufgerufen werden.
	 */
	public void startServer() {
		if (isRunning)
			return;
		RemoteFileServer_Starter starter = new RemoteFileServer_Starter();
		currentServer = starter.getServer();
		isRunning = true;
	}

	public void stopServer() {
		isRunning = !RemoteFileServer_Starter.shutDown(currentServer);
	}

	public void turnOnOffServer() {
		if (isRunning)
			stopServer();
		else
			startServer();
	}

	public String getMenuItemMsg() {
		if (isRunning)
			return "Remote-FileManager beenden(Server läuft)";
		else
			return "Remote-FileManager starten(Server ist aus)";
	}

	public boolean isRunning() {
		return isRunning;
	}

	public IFileServer getService() {
		return currentServer;
	}
}
