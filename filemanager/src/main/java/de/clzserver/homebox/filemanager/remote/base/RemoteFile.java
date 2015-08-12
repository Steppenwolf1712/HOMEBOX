package de.clzserver.homebox.filemanager.remote.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.clzserver.homebox.filemanager.remote.IRemoteFile;
import de.clzserver.homebox.config.HBPrinter;

public class RemoteFile implements IRemoteFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2492893896231830140L;

	private String m_s_user;
	private String m_s_local_path;

	private byte[] m_byte_a_content;
	private boolean m_b_synchronised = false;
	private boolean m_b_rechte = false;

	/**
	 * Nimmt ein String einer File location und bereitet das File zum übertragen
	 * über das Netzwerk vor.
	 * 
	 * @param para_content
	 */
	public RemoteFile(String para_content) {
		this(null, para_content);
	}
	
	/**
	 * Erzeugt in RemoteFile ohne Content. Es wird gebraucht um dem Client mitzuteilen,
	 * dass die Ressource zur Zeit einen exklusiven Zugrif eines anderen Users hat.
	 *
	 */
	public RemoteFile() {
		m_b_rechte = false;
	}

	/**
	 * Nimmt eine String Repräsentation eines File entgegen und bereitet das
	 * File zum versenden über das Netzwerk vor. Das File kann dazu auch ein
	 * File eines Users sein, welches in einem sub Verzeichnis auf dem Server
	 * gespeichert wird. Sollte es sich um ein User-unabhängiges File halten,
	 * kann einfach null übergeben werden.
	 * 
	 * @param para_user
	 * @param para_content
	 */
	public RemoteFile(String para_user, String para_content) {
		m_s_user = para_user;

		m_s_local_path = para_content;

		if (m_s_local_path != null && m_s_local_path.length() > 0) {
			File content_safe = new File(m_s_local_path);
			readout(content_safe);
		} else
			HBPrinter
					.getInstance()
					.printError(
							this,
							"Ungültiger Dateipfad!",
							new IOException(
									"Der übergebene String, der ein pfad zu einem File beschreiben sollte, ist leer!"));
	}

	private void readout(File to_read) {

		m_byte_a_content = new byte[(int) to_read.length()];
		try {
			FileInputStream reader = new FileInputStream(to_read);
			reader.read(m_byte_a_content);

			reader.close();
		} catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(
					this,
					"Konnte das gewünschte File nicht mit dem Pfad: "
							+ m_s_local_path + "\nfinden!", e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(
					this,
					"Es trat ein Ein/Ausgabe-Fehler beim auslesen der Datei "
							+ m_s_local_path + " auf!", e);
		}
	}

	@Override
	public String getFileLocation() {
		return m_s_local_path;
	}

	@Override
	public boolean isUserFile() {
		return (m_s_user == null ? false : true);
	}

	@Override
	public String getUser() {
		return m_s_user;
	}

	@Override
	public boolean saveFile() {
		File temp = new File(m_s_local_path);

		try {
			if (temp.exists())
				HBPrinter.getInstance().printMSG(this,
						"Das File " + m_s_local_path + " wird überschrieben!");
			else
				temp.createNewFile();

			FileOutputStream writer = new FileOutputStream(temp);
			writer.write(m_byte_a_content);
			writer.close();
			
			setSynchronised(true);
			return true;
		} catch (FileNotFoundException e) {
			HBPrinter
					.getInstance()
					.printError(
							this,
							"Das File "
									+ m_s_local_path
									+ " konnte nicht gefunden werden (Schreibzugriff)!",
							e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this, "Fehler beim Schreiben oder Erstellen des Files "+m_s_local_path, e);
		}
		return false;
	}

	@Override
	public File getFile() {
		File erg = null;
		
		if (this.saveFile())
			erg = new File(m_s_local_path);
		
		return erg;
	}

	@Override
	public void takeOverChangesOfFile(File para_new_content) {
		if (para_new_content == null || para_new_content.length() <= 0) {
			File content_safe = new File(m_s_local_path);
			readout(content_safe);
		} else {
			readout(para_new_content);
		}
	}

	@Override
	public void takeOverChangesOfFile() {
		this.takeOverChangesOfFile(null);
	}

	@Override
	public boolean hasRights() {
		return m_b_rechte;
	}

	public boolean isSynchronised() {
		return m_b_synchronised;
	}

	public void setSynchronised(boolean m_b_synchronised) {
		this.m_b_synchronised = m_b_synchronised;
	}

}
