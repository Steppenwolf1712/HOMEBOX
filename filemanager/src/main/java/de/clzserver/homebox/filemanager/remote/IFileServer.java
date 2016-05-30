package de.clzserver.homebox.filemanager.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileServer extends Remote {

	String SERVICE_NAME = "FileManager";

	int RMI_LAN_PORT = 54123;

	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * Wenn das File Exklusiv-Rechte geltend gemacht werden sollen, dann muss die Information über den User mit übergeben werden!
	 * 
	 * @param file
	 * @param user
	 * @param exclusive
	 * @return
	 * @throws RemoteException
	 */
	public IRemoteFile getFile(String file, String user, boolean exclusive) throws RemoteException;
	
	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * Das File ist ein User-Abhängiges File.
	 * 
	 * @param file
	 * @return
	 * @throws RemoteException
	 */
	public IRemoteFile getFile(String file, String user) throws RemoteException;

	/**
	 * Die Methode erlaubt das raus schreiben von Files die dann im Server gespeichert werden.
	 * Ob es sich hier um User-File handelt oder nicht wird im IRemoteFile festgehalten.
	 * 
	 * @param rfile
	 * @param user
	 * @param filePath
	 * @param done
	 * @return
	 * @throws RemoteException
	 */
	public boolean commitFile(IRemoteFile rfile, String user, String filePath,  boolean done) throws RemoteException;


	/**
	 * Die Methode ruft auf der angegebenen Directory die Methode "listFiles" auf. Es wird ein String-Array zurückgegeben,
	 * welches die relativen Pfade der einzelnen Dateien in dem Ordner wiedergeben.
	 *
	 * Sollte der übergebene String keinen existierenden Ordner auf dem Server darstellen, wird eine RemoteException geworden.
	 *
	 * @param filePath
	 * @return
	 * @throws RemoteException
	 */
	public String[] getDirectoryContent(String filePath) throws RemoteException;

	/**
	 * Die Methode ruft auf der angegebenen Directory aus dem privaten User Verzeichnis die Methode "listFiles" auf. Es wird ein String-Array zurückgegeben,
	 * welches die relativen Pfade der einzelnen Dateien in dem Ordner wiedergeben.
	 *
	 * Sollte der übergebene String keinen existierenden Ordner auf dem Server darstellen, wird eine RemoteException geworden.
	 *
	 * @param filePath
	 * @return
	 * @throws RemoteException
	 */
	public String[] getDirectoryContent(String filePath, String user) throws RemoteException;

	/**
	 * Die Methode löscht die entfernte Datei auf dem Server. Diese Methode kümmerst sich hierbei um die User Verzeichnisse.
	 *
	 * Die Methode gibt mit einem Boolean zurück, ob das löschen geklappt hat.
	 *
	 * @param filePath
	 * @param user
	 * @return
	 * @throws RemoteException
	 */
	public boolean deleteFile(String filePath, String user) throws RemoteException;

	/**
	 * Die Methode löscht die entfernte Datei auf dem Server.
	 *
	 * Die Methode gibt mit einem Boolean zurück, ob das löschen geklappt hat.
	 *
	 * @param filePath
	 * @return
	 * @throws RemoteException
	 */
	public boolean deleteFile(String filePath) throws RemoteException;
}
