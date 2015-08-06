package de.clzserver.homebox.budgetcalc.ods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;

public class Locker {
	
	private final int USER = 1;
	private final int MASCHINE = 2;
	private final int DATE = 3;

	private String path;
	
	public Locker() {
		aktualisieren();
	}
	
	public boolean islocked() {
		File test = new File(path);
		return test.exists();
	}
	
	public String getCurrentUser() {
		if (islocked()) {
			String erg = "";
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
				
				String msg = reader.readLine();
				String[] msg_parts = msg.split(",");
				
				erg = "Der User "+msg_parts[USER]+" hat die Abrechnungsdatei seit dem "+msg_parts[DATE]+" von der Maschine "+msg_parts[MASCHINE]+" aus geöffnet!";
				
				reader.close();
			} catch (FileNotFoundException e) {
				HBPrinter.getInstance().printError(this, "Konnte Lock-File ("+path+") nicht öffnen und lesen!", e);
			} catch (IOException e) {
				HBPrinter.getInstance().printError(this, "Der Inhalt der Datie konnte nicht gelesen werden!", e);
			}
			
			return erg;
		} else {
			return "Niemand!";
		}
	}
	
	private void aktualisieren() {
		Config cfg = Config.getInstance();
		
		path = cfg.getValue(Config.CALCODS_PATH_KEY)+cfg.getValue(Config.CALCODS_LOCK_NAME_KEY);
	}
}
