package de.clzserver.homebox.clipboard.funcs;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.*;

import de.clzserver.homebox.config.Config;

public class Type_Factory {


	public static final String TEXT_TYPE = "Text";
	public static final String APP_TYPE = "Dateien";
	public static final String IMG_TYPE = "Bild";
	
	public static final DataFlavor APP_FLAV = DataFlavor.javaFileListFlavor;
	
	private Type_Factory() {
		
	}
	
	public static boolean writeText() {
		return writeType(TEXT_TYPE);
	}
	

	public static boolean writeImage() {
		return writeType(IMG_TYPE);
	}
	
	public static boolean writeApllication() {
		return writeType(APP_TYPE);
	}
	

	private static boolean writeType(String string) {
		Config cfg = Config.getInstance();
		
		try {
			RMI_Connect connection = RMI_Connect.getInstance();

			String filePath = cfg.getValue(Config.SAVE_PATH_KEY)+cfg.getValue(Config.SAVETYPE_NAME_KEY);

			File temp = File.createTempFile(cfg.getValue(Config.SAVETYPE_NAME_KEY), "");
			temp.deleteOnExit();

			BufferedWriter buff = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(temp)));
			
			buff.write(string);
			
			buff.flush();
			buff.close();

			connection.commitFile(temp, filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		

		return true;
	}
	
	public static String getSaveType() {
		Config cfg = Config.getInstance();
		
		try {
			RMI_Connect connection = RMI_Connect.getInstance();

			String filePath = cfg.getValue(Config.SAVE_PATH_KEY)+cfg.getValue(Config.SAVETYPE_NAME_KEY);

			File temp = connection.getFile(filePath);

			BufferedReader buff = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(temp)));
			
			String erg = buff.readLine();
			
			buff.close();
			
			if (erg.equals(APP_TYPE))
				return APP_TYPE;
			if (erg.equals(IMG_TYPE))
				return IMG_TYPE;
			if (erg.equals(TEXT_TYPE))
				return TEXT_TYPE;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String getDataFlavorTypes(DataFlavor... flavs) {
		return getType(flavs);
	}
	
	private static String getType(DataFlavor[] flavs) {
		if (flavs.length == 0)
			return null;
		
		if (flavs.length >1 || flavs[0].getPrimaryType().equals("text")) {
			return TEXT_TYPE;
		} else if (flavs[0].getPrimaryType().equals("application")) {
			return APP_TYPE;
		} else if (flavs[0].getPrimaryType().equals("image")) {
			return IMG_TYPE;
		}
		return null;
	}
	
	public static String getTransferableType(Transferable trans) {
		DataFlavor[] flavs = trans.getTransferDataFlavors();
		return getType(flavs);
	}
}

