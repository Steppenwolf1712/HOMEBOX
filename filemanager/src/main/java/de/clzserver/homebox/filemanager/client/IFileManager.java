package de.clzserver.homebox.filemanager.client;

import java.io.File;

public interface IFileManager {


	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * 
	 * @param file
	 * @param exclusiv
	 * @return
	 */
	public File getFile(String file, boolean exclusiv);
	
	/**
	 * Methode zur Anfrage von Files.
	 * Es wird ein RemoteFile als Antwort versendet mit dem Inhalt der angeforderten Datei!
	 * Das File ist ein User-Abhängiges File.
	 * 
	 * @param file
	 * @param exclusiv
	 * @return
	 */
	public File getFile(String file, String user, boolean exclusiv);

	/**
	 * Die Methode erlaubt das raus schreiben von Files die dann im Server gespeichert werden.
	 * Ob es sich hier um User-File handelt oder nicht wird im IRemoteFile festgehalten.
	 * 
	 * @param rfile
	 * @param done
	 * @return
	 */
	public boolean commitFile(File rfile, boolean done);
	
}
