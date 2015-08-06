package de.clzserver.homebox.clipboard.funcs.save;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.imageio.ImageIO;

import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.config.Config;

public class ClipSave {


	public void save(Transferable data) throws IOException, UnsupportedFlavorException{

//		printInfo(data);
		System.out.println();
		
		DataFlavor[] flavs = data.getTransferDataFlavors();
		
		if (flavs == null ||  flavs.length == 0) {
			System.out.println("ClipSave: Nichts in der Zwischenablage");
			return;
		}
		
		String test = Type_Factory.getTransferableType(data);
		
		if (test.equals(Type_Factory.TEXT_TYPE)) {
			System.out.println("ClipSave: Es ist ein Text zu transferieren");
			
			String erg = null;
			for (int i = 0; i<flavs.length; i++) {
				Object temp_data = data.getTransferData(flavs[i]);
				
				if (temp_data instanceof String) {
					erg = (String) temp_data;
					break;
				}
			}
			if (erg == null)
				System.out.println("ClipSave: Da ist beim String suchen aus der Zwischenablage was schief gegangen.");
			
			saveString(erg);
			
		} else if (test.equals(Type_Factory.APP_TYPE)) {
			System.out.println("ClipSave: Es ist eine Menge von Application(Dateien) zu transferieren");
			saveFiles((List)data.getTransferData(flavs[0]));
			
		} else if (test.equals(Type_Factory.IMG_TYPE)) {
			System.out.println("ClipSave: Es ist ein Bildausschnitt zu transferieren");
			saveImage((BufferedImage)data.getTransferData(flavs[0]));
			
		} else {
			System.out.println("ClipSave-Fehler: der zu Transferierende Typ konnte nicht erkannt werden.");
		}
		
		
//		
//		if (content instanceof String) {
//			saveString((String)content);
//		} else if (content instanceof List) {
//			saveFiles((List)content);
//		} else if (content instanceof )
	}

	private void saveImage(BufferedImage transferData) {
		Type_Factory.writeImage();
		
		Config cfg = Config.getInstance();
		String location = cfg.getValue(Config.SAVEIMG_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
		
		File target = new File(location);
		
		try {
			ImageIO.write(transferData, "jpg", target);
			System.out.println("ClipSave: Die Ablage wird gespeichert=> "+location);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveFiles(List content) {
		// TODO Auto-generated method stub
		Type_Factory.writeApllication();
		
	}

	private void saveString(String content) {
		Type_Factory.writeText();
		
		Config cfg = Config.getInstance();
		String location = cfg.getValue(Config.SAVETEXT_PATH_KEY)+cfg.getValue(Config.SAVE_NAME_KEY);
		
		try {
			BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(location)));
			
			System.out.println("ClipSave: Die Ablage wird gespeichert=> "+location);
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

	
	private void printInfo(Transferable data) throws UnsupportedFlavorException, IOException {
		DataFlavor[] flavors = data.getTransferDataFlavors();
		int i = 0;
		for (DataFlavor flav: flavors) {
			System.out.println("Inhalt NUmmer "+ i++
					+"\nHumandesc: "+flav.getHumanPresentableName()
					+"\nMyMEtype: "+flav.getMimeType()+"\nJavatype: "
					+flav.getDefaultRepresentationClassAsString()
					+"\nPrimaryType: "+flav.getPrimaryType());
			
			System.out.println(data.getTransferData(flav));
			System.out.println();
			
		}
	}
}
