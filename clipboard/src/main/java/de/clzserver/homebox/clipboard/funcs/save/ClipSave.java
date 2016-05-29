package de.clzserver.homebox.clipboard.funcs.save;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.rmi.RemoteException;
import java.util.List;

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.RMI_Connect;
import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;

public class ClipSave {


	public void save(Transferable data, Clipboard clip) throws IOException, UnsupportedFlavorException{

//		printInfo(data);
		
		DataFlavor[] flavs = data.getTransferDataFlavors();
		
		if (flavs == null ||  flavs.length == 0) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Nichts in der Zwischenablage");
			return;
		}
		
		String test = Type_Factory.getTransferableType(data);
		
		if (test.equals(Type_Factory.TEXT_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es ist ein Text zu transferieren");
			
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
				HBPrinter.getInstance().printMSG(this.getClass(), "Da ist beim String suchen aus der Zwischenablage was schief gegangen.");
			
			saveString(erg);
			
		} else if (test.equals(Type_Factory.APP_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es ist eine Menge von Application(Dateien) zu transferieren");
			saveFiles((List<File>)data.getTransferData(flavs[0]));
			
		} else if (test.equals(Type_Factory.IMG_TYPE)) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Es ist ein Bildausschnitt zu transferieren");
			saveImage((BufferedImage)data.getTransferData(flavs[0]));
			
		} else {
			HBPrinter.getInstance().printMSG(this.getClass(), "Fehler: Der zu Transferierende Typ konnte nicht erkannt werden.");
		}
		HBPrinter.getInstance().printMSG(this.getClass(), "Die Zwischenablage wurde erfolgreich übertragen!");
	}

	private void saveImage(BufferedImage transferData) {
		Type_Factory.writeImage();
		
		Config cfg = Config.getInstance();
		RMI_Connect connect = RMI_Connect.getInstance();

		String endloc = cfg.getValue(Config.SAVE_NAME_KEY), location = cfg.getValue(Config.SAVEIMG_PATH_KEY)+endloc;

		File target;
		try {
			target = File.createTempFile(endloc, "");
			target.deleteOnExit();

			ImageIO.write(transferData, "jpg", target);
			HBPrinter.getInstance().printMSG(this.getClass(), "Die Ablage wird gespeichert=> "+location);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Ein/Ausgabe-Fehler beim speichern eines Bildes!", e);
			return;
		}

		try {
			connect.commitFile(target, location);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Das Bild konnte nicht auf den Server übertragen werden!", e);
		}
	}

	private void saveFiles(List<File> content) {
		Type_Factory.writeApllication();

		RMI_Connect connection = RMI_Connect.getInstance();
		String subLocation = Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY);

		String[] remotelist = new String[0];
		try {
			remotelist = connection.getContentList(subLocation);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Konnte das Remote-Verzeichnis "+subLocation+" nicht auslesen und übertragen!", e);
		}

		if (remotelist.length == 0) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Das Remote-Verzeichnis " + subLocation + " ist leer laut Server-Antwort!");
			return;
		}

		for (String f: remotelist) {
			try {
				connection.deleteFile(f);
			} catch (RemoteException e) {
			}
		}
		String part = Config.getInstance().getValue(Config.SAVEAPP_PATH_KEY);
		for (File f: content) {
			try {
				if (!connection.commitFile(f, part+f.getName())) {
					HBPrinter.getInstance().printError(this.getClass(), "Konnte die Datei " + f + " nicht zum Server übertragen!", new IOException());
                }
			} catch (RemoteException e) {
				HBPrinter.getInstance().printError(this.getClass(), "Konnte die Datei " + f + " nicht zum Server übertragen!", e);
			}
		}
	}

	private void saveString(String content) {
		Type_Factory.writeText();

		RMI_Connect connection = RMI_Connect.getInstance();
		Config cfg = Config.getInstance();
		String endloc=cfg.getValue(Config.SAVE_NAME_KEY), location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+endloc;

		File target;
		try {
			target = File.createTempFile(endloc, "");
			target.deleteOnExit();

			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(target)));
			
			HBPrinter.getInstance().printMSG(this.getClass(), "Die Ablage wird gespeichert=> "+location);
			writer.write(content);
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Ein/Ausgabe-Fehler beim Speichern eines Textes!", e);
			return;
		}

		try {
			connection.commitFile(target, location);
		} catch (RemoteException e) {
			HBPrinter.getInstance().printError(this.getClass(), "Der Text konnte nicht auf den Server übertragen werden!", e);
		}
	}
	
	private void printInfo(Transferable data) throws UnsupportedFlavorException, IOException {
		DataFlavor[] flavors = data.getTransferDataFlavors();
		int i = 0;
		for (DataFlavor flav: flavors) {
			HBPrinter.getInstance().printMSG(this.getClass(), "Inhalt Nummer "+ i++
					+"\nHumandesc: "+flav.getHumanPresentableName()
					+"\nMyMEtype: "+flav.getMimeType()+"\nJavatype: "
					+flav.getDefaultRepresentationClassAsString()
					+"\nPrimaryType: "+flav.getPrimaryType()+"\n"+
					data.getTransferData(flav)+"\n");
			
		}
	}
}
