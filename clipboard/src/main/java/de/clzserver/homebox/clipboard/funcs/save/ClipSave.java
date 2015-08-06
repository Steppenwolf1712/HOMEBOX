package de.clzserver.homebox.clipboard.funcs.save;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.shared.Config;
import de.clzserver.homebox.shared.HBPrinter;

public class ClipSave {


	public void save(Transferable data, Clipboard clip) throws IOException, UnsupportedFlavorException{

//		printInfo(data);
		
		DataFlavor[] flavs = data.getTransferDataFlavors();
		
		if (flavs == null ||  flavs.length == 0) {
			HBPrinter.getInstance().printMSG(this, "Nichts in der Zwischenablage");
			return;
		}
		
		String test = Type_Factory.getTransferableType(data);
		
		if (test.equals(Type_Factory.TEXT_TYPE)) {
			HBPrinter.getInstance().printMSG(this, "Es ist ein Text zu transferieren");
			
//			printInfo(data);
			
			//Die Ursprüngliche selbstständige Suche nach sinnvolem Text der zu kopieren sei ist zu aufwendig
//			String erg = null;
//			for (int i = 0; i<flavs.length; i++) {
//				Object temp_data = data.getTransferData(flavs[i]);
//				
//				if (temp_data instanceof String) {
//					erg = (String) temp_data;
//					break;
//				}
//			}
			//Die Notlösung die scheinbar nur den Textinhalt von einer Text-ähnlichen Kopie ausgibt
			String erg = (String)clip.getData(DataFlavor.stringFlavor); 
			//Eine schönere Lösung wäre alle Flavors abzuholen und zu serialisieren, ich weiß blox nicht ob ich die wieder in das Clipboard reinbekomme
			
			if (erg == null)
				HBPrinter.getInstance().printMSG(this, "Da ist beim String suchen aus der Zwischenablage was schief gegangen.");
			
			saveString(erg);
			
		} else if (test.equals(Type_Factory.APP_TYPE)) {
			HBPrinter.getInstance().printMSG(this, "Es ist eine Menge von Application(Dateien) zu transferieren");
			saveFiles((List<File>)data.getTransferData(flavs[0]));
			
		} else if (test.equals(Type_Factory.IMG_TYPE)) {
			HBPrinter.getInstance().printMSG(this, "Es ist ein Bildausschnitt zu transferieren");
			saveImage((BufferedImage)data.getTransferData(flavs[0]));
			
		} else {
			HBPrinter.getInstance().printMSG(this, "Fehler: Der zu Transferierende Typ konnte nicht erkannt werden.");
		}
	}

	private void saveImage(BufferedImage transferData) {
		Type_Factory.writeImage();
		
		Config cfg = Config.getInstance();
		String location = cfg.getValue(Config.SAVEIMG_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
		
		File target = new File(location);
		
		try {
			ImageIO.write(transferData, "jpg", target);
			HBPrinter.getInstance().printMSG(this, "Die Ablage wird gespeichert=> "+location);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this, "Ein/Ausgabe-Fehler beim speichern eines Bildes!", e);
		}
	}

	private void saveFiles(List<File> content) {
		Type_Factory.writeApllication();
		
		File save_dir = new File(Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY));
		if (save_dir.exists() && save_dir.isDirectory()) {
			for (File f: save_dir.listFiles()) {
				f.delete();
			}
		}
		
		File copy = null;
		for (File f: content) {
			copy = new File(Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY)+f.getName());
			copyFileUsingStream(f, copy);
		}
	}

	private void saveString(String content) {
		Type_Factory.writeText();
		
		Config cfg = Config.getInstance();
		String location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
		
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(location)));
			
			HBPrinter.getInstance().printMSG(this, "Die Ablage wird gespeichert=> "+location);
			writer.write(content);
			
			writer.flush();
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void copyFileUsingStream(File source, File dest) {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(this, "Die Datei konnte nicht gefunden werden!", e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this, "Es gab einen Ein/Ausgabefehler!", e);
		} finally {
	        try {
				is.close();
		        os.close();
			} catch (IOException e) {
				HBPrinter.getInstance().printError(this, "Die Streams konnten nicht geschlossen werden!", e);
			}
	    }
	}

	
	private void printInfo(Transferable data) throws UnsupportedFlavorException, IOException {
		DataFlavor[] flavors = data.getTransferDataFlavors();
		int i = 0;
		for (DataFlavor flav: flavors) {
			HBPrinter.getInstance().printMSG(this, "Inhalt Nummer "+ i++
					+"\nHumandesc: "+flav.getHumanPresentableName()
					+"\nMyMEtype: "+flav.getMimeType()+"\nJavatype: "
					+flav.getDefaultRepresentationClassAsString()
					+"\nPrimaryType: "+flav.getPrimaryType()+"\n"+
					data.getTransferData(flav)+"\n");
			
		}
	}
}
