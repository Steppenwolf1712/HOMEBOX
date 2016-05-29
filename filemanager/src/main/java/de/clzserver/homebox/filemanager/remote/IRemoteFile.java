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
	 * Methode gibt den lokalen String zur�ck, damit eine lokale Version des Files
	 * gespeichert werden kann.
	 * 
	 * @return
	 */
	public String getFileLocation();
	
	/**
	 * Gibt zur�ck ob das File speziell einem User zugeordnet ist oder nicht.
	 * 
	 * @return
	 */
	public boolean isUserFile();
	
	/**
	 * Gibt den User-String zur�ck, damit die Datei auf dem Server auch dem richtigen User zugeordnet werden kann.
	 * @return
	 */
	public String getUser();
	
	/**
	 * GIbt das zugeh�rige File zur�ck, wenn es schon als lokales tempor�res File gespeichert wurde.
	 * Ansonsten wird versucht den Inhalt in einem tempor�ren File abzulegen.
	 * 
	 * Sollte das nicht funktionieren wird null zur�ckgegeben.
	 * @return
	 */
	public File getFile();

	/**
	 * Die Methode schreibt den �bergebenen Content in das �bergebene File. Exceptions werden direkt zur�ckgegeben.
	 *
	 * @param target
	 * @throws IOException
	 */
	public void safeContentAt(File target) throws IOException;

	/**
	 * Erlaubt den Inhalt des RemoteFiles zu �berschreiben, um danach wieder an den Server zur�ckgesand zu werden.
	 * Wird null �bergeben, wird einfach das inherente File erneut eingelesen. (zu bevorzugen)
	 *
	 * @param para_new_content
	 */
	public void takeOverChangesOfFile(File para_new_content);
	
	/**
	 * St��t das erneute Einlesen des Files an, um �nderungen am lokalen File zu �bernehmen.
	 */
	public void takeOverChangesOfFile();
	
	/**
	 * Gibt zur�ck, ob das angeforderte File tats�chlich �bergeben wurde, oder ob das File
	 * zur Zeit einen exklusiven zugriff eines anderen Users hat!
	 * @return
	 */
	public boolean hasRights();
	
	public boolean isSynchronised();
	
	public void setSynchronised(boolean m_b_synchronised);
}
