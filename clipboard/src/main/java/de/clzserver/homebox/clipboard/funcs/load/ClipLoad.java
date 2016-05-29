package de.clzserver.homebox.clipboard.funcs.load;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.RMI_Connect;
import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;

public class ClipLoad {

	public void load() {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		String type = Type_Factory.getSaveType();
		RMI_Connect connect = RMI_Connect.getInstance();
		
		if (type.equals(Type_Factory.APP_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es wird eine Menge von Dateien geladen!");
			
			String direc = Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY);

			String[] list = new String[0];
			try {
				list = connect.getContentList(direc);
			} catch (RemoteException e) {
				HBPrinter.getInstance().printError(this.getClass(), "Konnte den Inhalt des Ordners "+direc+" nicht vom Server erfragen!\n" +
						"Das Ablageverzeichnis für die Files ist kein Pfad, sondern eine Datei oder existiert nicht!", e);
				return;
			}

			List<File> temp_files = new ArrayList<File>();

			for (String s: list) {
				try {
						temp_files.add(connect.getFile(s));
				} catch (RemoteException e) {
					HBPrinter.getInstance().printError(this.getClass(), "Konnte das File "+s+" nicht vom Server übertragen!", e);
					return;
				} catch (IOException e) {
					HBPrinter.getInstance().printError(this.getClass(), "Konnte das File " + s + " nicht auf im lokalen Dateisystem abspeichern!", e);
					return;
				}
			}

			TransferableFiles temp = new TransferableFiles(temp_files);
			clip.setContents(temp, null);
		} else if (type.equals(Type_Factory.IMG_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es wird ein Bildausschnitt geladen!");
			
			BufferedImage buff = loadImage();
			
			TransferableImage i = new TransferableImage(buff);
			clip.setContents(i, null);
			
		} else if (type.equals(Type_Factory.TEXT_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es wird ein Text geladen!");
			
			String erg = loadString();
			
			StringSelection erg_selection = new StringSelection(erg);
			
			clip.setContents(erg_selection, null);
		} else {
			HBPrinter.getInstance().printMSG(this.getClass(), "Der Typ der Ablage wurde nicht erkannt!");
		}
	}

	private BufferedImage loadImage() {
		Config cfg = Config.getInstance();
		
		String location = cfg.getValue(Config.SAVEIMG_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);

		RMI_Connect connect = RMI_Connect.getInstance();

		File input = null;
		try {
			input = connect.getFile(location);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte das File "+location+" nicht vom Server übertragen!", e);
			return null;
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte das File " + location + " nicht auf im lokalen Dateisystem abspeichern!", e);
			return null;
		}
		BufferedImage buff = null;
		try {
			HBPrinter.getInstance().printMSG(this.getClass(), "Die Ablage wird von hier geladen: "+location);
			buff = ImageIO.read(input);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Ein/Ausgabe Fehler beim File mit dem gespeicherten Bild Inhalt", e);
		}
		
		return buff;
	}

	private String loadString() {
		String erg = "";

		Config cfg = Config.getInstance();

		RMI_Connect connect = RMI_Connect.getInstance();
		String location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);

		try {
			HBPrinter.getInstance().printMSG(this.getClass(), "ClipLoad: Die Ablage wird von hier geladen: "+location);

			File temp = connect.getFile(location);

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(temp)));
		
			String teil = reader.readLine();
			while (teil != null) {
				erg += teil;
				erg += System.getProperty("line.separator");
				teil = reader.readLine();
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte beim lesen der verteilten Zwischenablage die Datei "+location+" lokal nicht finden!", e);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte das File "+location+" nicht vom Server übertragen!", e);
			return null;
		}catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "I/O Fehler beim lesen des Files!", e);
		}

		return erg;
	}
}
