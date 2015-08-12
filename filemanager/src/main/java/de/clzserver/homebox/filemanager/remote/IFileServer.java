package de.clzserver.homebox.filemanager.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileServer extends Remote {

	public static final String SERVICE_NAME = "FileManager";

	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * 
	 * @param file
	 * @param exclusiv
	 * @return
	 * @throws RemoteException
	 */
	public IRemoteFile getFile(String file, boolean exclusiv) throws RemoteException;
	
	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * Das File ist ein User-Abhängiges File.
	 * 
	 * @param file
	 * @param exclusiv
	 * @return
	 * @throws RemoteException
	 */
	public IRemoteFile getFile(String file, String user, boolean exclusiv) throws RemoteException;

	/**
	 * Die Methode erlaubt das raus schreiben von Files die dann im Server gespeichert werden.
	 * Ob es sich hier um User-File handelt oder nicht wird im IRemoteFile festgehalten.
	 * 
	 * @param rfile
	 * @param done
	 * @return
	 * @throws RemoteException
	 */
	public boolean commitFile(IRemoteFile rfile, boolean done) throws RemoteException;
}
