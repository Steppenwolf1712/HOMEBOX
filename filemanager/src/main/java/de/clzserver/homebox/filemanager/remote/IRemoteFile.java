package de.clzserver.homebox.filemanager.remote;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface IRemoteFile extends Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	/**
	 * Methode gibt den lokalen String zurück, damit eine lokale Version des Files
	 * gespeichert werden kann.
	 * 
	 * @return
	 */
	public String getFileLocation();
	
	/**
	 * Gibt zurück ob das File speziell einem User zugeordnet ist oder nicht.
	 * 
	 * @return
	 */
	public boolean isUserFile();
	
	/**
	 * Gibt den User-String zurück, damit die Datei auf dem Server auch dem richtigen User zugeordnet werden kann.
	 * @return
	 */
	public String getUser();
	
	/**
	 * GIbt das zugehörige File zurück, wenn es schon als lokales temporäres File gespeichert wurde.
	 * Ansonsten wird versucht den Inhalt in einem temporären File abzulegen.
	 * 
	 * Sollte das nicht funktionieren wird null zurückgegeben.
	 * @return
	 */
	public File getFile();

	/**
	 * Die Methode schreibt den übergebenen Content in das übergebene File. Exceptions werden direkt zurückgegeben.
	 *
	 * @param target
	 * @throws IOException
	 */
	public void safeContentAt(File target) throws IOException;

	/**
	 * Erlaubt den Inhalt des RemoteFiles zu überschreiben, um danach wieder an den Server zurückgesand zu werden.
	 * Wird null übergeben, wird einfach das inherente File erneut eingelesen. (zu bevorzugen)
	 *
	 * @param para_new_content
	 */
	public void takeOverChangesOfFile(File para_new_content);
	
	/**
	 * Stößt das erneute Einlesen des Files an, um Änderungen am lokalen File zu übernehmen.
	 */
	public void takeOverChangesOfFile();
	
	/**
	 * Gibt zurück, ob das angeforderte File tatsächlich übergeben wurde, oder ob das File
	 * zur Zeit einen exklusiven zugriff eines anderen Users hat!
	 * @return
	 */
	public boolean hasRights();
	
	public boolean isSynchronised();
	
	public void setSynchronised(boolean m_b_synchronised);
}
