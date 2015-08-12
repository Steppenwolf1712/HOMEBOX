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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;

public class ClipLoad {

	public void load() {
		System.out.println();
		
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		String type = Type_Factory.getSaveType();
		
		if (type.equals(Type_Factory.APP_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es wird eine Menge von Dateien geladen!");
			
			File dir = new File(Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY));
			
			if (!dir.isDirectory()) {
				HBPrinter.getInstance().printError(this.getClass(), "Das Ablageverzeichnis für die Files ist kein Pfad sondern ein File oder existiert nicht!", new IOException("Ungültiger Pfad"));
				return;
			}
			
			String[] temp_list = dir.list();
			List<File> temp_files = new ArrayList<File>();
			for (String s: temp_list)
				temp_files.add(new File(s));
			
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
		
		File input = new File(location);
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
		
		try {
			String location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
			HBPrinter.getInstance().printMSG(this.getClass(), "ClipLoad: Die Ablage wird von hier geladen: "+location);
			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(location)));
		
			String teil = reader.readLine();
			while (teil != null) {
				erg += teil;
				erg += System.getProperty("line.separator");
				teil = reader.readLine();
			}
			
			reader.close();
		
		} catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte beim String lesen aus dem Save das File nicht finden!", e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "I/O Fehler beim lesen des Files!", e);
		}

		return erg;
	}
}
