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

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.config.Config;

public class ClipLoad {

	public void load() {
		System.out.println();
		
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		String type = Type_Factory.getSaveType();
		
		if (type.equals(Type_Factory.APP_TYPE)) {
			System.out.println("ClipLoad: Es wird eine Menge von Dateien geladen!");
			
		} else if (type.equals(Type_Factory.IMG_TYPE)) {
			System.out.println("Clipload: Es wird ein Bildausschnitt geladen!");
			
			BufferedImage buff = loadImage();
			
			TransferableImage i = new TransferableImage(buff);
			clip.setContents(i, null);
			
		} else if (type.equals(Type_Factory.TEXT_TYPE)) {
			System.out.println("ClipLoad: Es wird ein Text geladen!");
			
			String erg = loadString();
			
			StringSelection erg_selection = new StringSelection(erg);
			
			clip.setContents(erg_selection, null);
		} else {
			System.out.println("ClipLoad-Fehler: Der Typ der Ablage wurde nicht erkannt!");
		}
	}

	private BufferedImage loadImage() {
		Config cfg = Config.getInstance();
		
		String location = cfg.getValue(Config.SAVEIMG_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
		
		File input = new File(location);
		BufferedImage buff = null;
		try {
			System.out.println("ClipLoad: Die Ablage wird von hier geladen: "+location);
			buff = ImageIO.read(input);
		} catch (IOException e) {
			System.out.println("ClipLoad: Ein/Ausgabe Fehler beim File mit dem gespeicherten Bild Inhalt");
			e.printStackTrace();
		}
		
		return buff;
	}

	private String loadString() {
		String erg = "";

		Config cfg = Config.getInstance();
		
		try {
			String location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
			System.out.println("ClipLoad: Die Ablage wird von hier geladen: "+location);
			
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
			System.out.println("ClipLoad-Fehler: Konnte beim String lesen aus dem Save das File nicht finden!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Clipload-Fehler: I/O Fehler beim lesen des Files!");
			e.printStackTrace();
		}

		return erg;
	}
}
