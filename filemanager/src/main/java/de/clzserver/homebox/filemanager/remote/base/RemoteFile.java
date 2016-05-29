package de.clzserver.homebox.filemanager.remote.base;

import java.io.*;

import de.clzserver.homebox.config.Config;
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
	public RemoteFile(File para_content, String para_remotePath) {
		this(para_content, null, para_remotePath, false);
	}

	/**
	 * Nimmt ein String einer File location und bereitet das File zum übertragen
	 * über das Netzwerk vor.
	 *
	 * @param para_content
	 */
	public RemoteFile(File para_content, String user, String para_remotePath) {
		this(para_content, user, para_remotePath, false);
	}

	/**
	 * Erzeugt in RemoteFile ohne Content. Es wird gebraucht um dem Client mitzuteilen,
	 * dass die Ressource zur Zeit einen exklusiven Zugrif eines anderen Users hat.
	 *
	 */
	public RemoteFile(boolean rechte) {
		m_b_rechte = rechte;
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
	public RemoteFile(File para_content, String para_user, String para_remotePath, boolean rechte) {
//		String path = RemoteFile.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//		String decodedPath = "";
//		try {
//			 decodedPath = URLDecoder.decode(path, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			HBPrinter.getInstance().printError(this.getClass(), "Konnte den Pfad zum Programm nicht decoden.\n" +
//					"Somit ist es nicht möglich das lokale Dateisystem zu ereichen", e);
//		}
		m_s_user = para_user;

//		System.out.println("lokaler Pfad: "+decodedPath);

		m_s_local_path = para_remotePath;//decodedPath+para_content;

		m_b_rechte = rechte;

		if (m_s_local_path != null && m_s_local_path.length() > 0 && para_content!=null) {
			readout(para_content);
		} else
			HBPrinter
					.getInstance()
					.printError(
							this.getClass(),
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
					this.getClass(),
					"Konnte das gewünschte File "+to_read +" nicht finden!", e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(
					this.getClass(),
					"Es trat ein Ein/Ausgabe-Fehler beim auslesen der Datei "
							+ to_read  + " auf!", e);
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

//	@Override
//	public boolean saveFile() {
//		File temp = new File((getUser()==null?m_s_local_path: getUserFilePath()));
//
//		try {
//			if (temp.exists())
//				HBPrinter.getInstance().printMSG(this.getClass(),
//						"Das File " + m_s_local_path + " wird überschrieben!");
//			else
//				temp.createNewFile();
//
//			FileOutputStream writer = new FileOutputStream(temp);
//			writer.write(m_byte_a_content);
//			writer.close();
//
//			setSynchronised(true);
//			return true;
//		} catch (FileNotFoundException e) {
//			HBPrinter
//					.getInstance()
//					.printError(
//							this.getClass(),
//							"Das File "
//									+ m_s_local_path
//									+ " konnte nicht gefunden werden (Schreibzugriff)!",
//							e);
//		} catch (IOException e) {
//			HBPrinter.getInstance().printError(this.getClass(), "Fehler beim Schreiben oder Erstellen des Files "+m_s_local_path, e);
//		}
//		return false;
//	}

	private String getUserFilePath() {
		return Config.getInstance().getValue(Config.USER_CONTENTS_PATH_KEY.toString())+getUser()+"\\"+m_s_local_path;
	}

	@Override
	public File getFile() {

		int index = m_s_local_path.lastIndexOf(".");
		String filePathName = m_s_local_path.substring(0, index);
		String end = m_s_local_path.substring(index);
		try {
			String name = filePathName.substring(filePathName.lastIndexOf("\\"));
			File erg = File.createTempFile(name, end);

			HBPrinter.getInstance().printMSG(this.getClass(), "Erzeuge temporäres File :=\n\t" + erg.getAbsolutePath());

			FileOutputStream writer = new FileOutputStream(erg);
			writer.write(m_byte_a_content);
			writer.flush();
			writer.close();

			setSynchronised(true);

			erg.deleteOnExit();

			return erg;
		} catch (FileNotFoundException e) {
			HBPrinter
					.getInstance()
					.printError(
							this.getClass(),
							"Das File "
									+ m_s_local_path
									+ " konnte nicht gefunden werden (Schreibzugriff)!",
							e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Fehler beim Schreiben oder Erstellen des Files "+m_s_local_path, e);
		}
		return null;
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

	public void safeContentAt(File target) throws IOException {
		FileOutputStream writer = new FileOutputStream(target);
		writer.write(m_byte_a_content);
		writer.flush();
		writer.close();
	}

	public boolean isSynchronised() {
		return m_b_synchronised;
	}

	public void setSynchronised(boolean m_b_synchronised) {
		this.m_b_synchronised = m_b_synchronised;
	}

}
