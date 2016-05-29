package de.clzserver.homebox.filemanager.client;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public interface IFileManager {

	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 *
	 * @param file
	 * @param user
	 * @param exclusive
	 * @return
	 */
	public File getFile(String file, String user, boolean exclusive) throws IOException;

	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 *
	 * @param file
	 * @return
	 */
	public File getFile(String file) throws IOException;
	
	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * Das File ist ein User-Abhängiges File.
	 * 
	 * @param file
	 * @return
	 */
	public File getFile(String file, String user) throws IOException;

	/**
	 * Die Methode erlaubt das raus schreiben von Files die dann im Server gespeichert werden.
	 * Ob es sich hier um User-File handelt oder nicht wird im IRemoteFile festgehalten.
	 *
	 * @param content
	 * @param remoteFilePath
	 * @param user
	 * @param exclusive
	 * @param done
	 * @return
	 */
	public boolean commitFile(File content, String remoteFilePath, String user, boolean exclusive, boolean done) throws RemoteException;

	/**
	 * Diese Methode gibt ein Array der relativen Pfadangaben aller in dem Ordner enthaltenen Dateien zurück.
	 *
	 * @param directoryPath
	 * @return
	 * @throws RemoteException
	 */
	public String[] getDirectoryContent(String directoryPath) throws RemoteException;

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
