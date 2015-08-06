package de.clzserver.homebox.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;


public class Config {
	
	private static final String INIT_CONFIG = "init.txt";

	public static final String HOMEBOX_PATH_KEY = "HOMEBOX_PATH";
	public static final String SAVE_PATH_KEY = "SAVE_PATH";
	public static final String SAVETYPE_NAME_KEY = "SAVETYPE_NAME";
	public static final String SAVETEXT_PATH_KEY = "SAVETEXT_PATH";
	public static final String SAVEIMG_PATH_KEY = "SAVEIMG_PATH";
	public static final String SAVEAPP_PATH_KEY = "SAVEAPP_PATH";
	public static final String SAVE_NAME_KEY = "SAVE_NAME";
	public static final String CALCODS_PATH_KEY = "CALCODS_PATH";
	public static final String CALCODS_NAME_KEY = "CALCODS_NAME";
	public static final String CALCODS_LOCK_NAME_KEY = "CALCODS_LOCK_NAME";
	public static final String USER_STRING_KEY = "USER_STRING";
		
	private Properties props = null;
	private static Config single = null;
	
	private Config() {
		props = new Properties();
		init();
	}
	
	Properties getProps() {
		return props;
	}
	
	private void init() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(INIT_CONFIG)));
			
			String line = reader.readLine();
			while (line != null) {
				String[] kv = line.split("=");
				props.put(kv[0], kv[1]);
				line = reader.readLine();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fehler!!!\nKonnte init.txt Datei nicht finden.");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler!!!\nKonnte InitFile nicht auslesen.");
			e.printStackTrace();
		}
	}

	public static Config getInstance() {
		if (single == null)
			single = new Config();
		return single;
	}
	
	public void setProperty(String key, String value) {
		if (props.containsKey(key)) {
			props.setProperty(key, value);
		} else {
			System.out.println("Neue Property in der Config-CLASS:\nKEY:="+key+"\nVALUE:="+value);
			props.put(key, value);
		}
	}
	
	/**
	 * Bei der GetValue Methode werden die Verweise innerhalb der values aufgelöst. Auf diese Weise ist es zum Beispiel möglich,
	 * in der Init-Datei Pfade wie $Parent_Path$Ordnername\ zu definieren. Zusätzlich sind die Values schon geparst worden und 
	 * die Reihenfolge in der die Eigenschaften in der Init-Datei stehen ist irrelevant.
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		String temp = props.getProperty(key);
		
		String erg = get_rek_Value(temp);
				
		return erg;
	}
	
	private String get_rek_Value(String temp) {
		String erg = "";
		
		if (temp.contains("$")) {
			String[] tempi = temp.split(Pattern.quote("$"));
			
			for (int i = 0; i<tempi.length; i++) {
				if (props.containsKey(tempi[i])) {
					erg += get_rek_Value(props.getProperty(tempi[i]));
				} else {
					erg += tempi[i];
				}
			}
		} else {
			erg += temp;
		}
		
		return erg;
	}

	public void save() {
		File temp = new File(INIT_CONFIG);
		File temp_save = new File(INIT_CONFIG+"temp");
		temp.renameTo(temp_save);

		temp.delete();
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(INIT_CONFIG)));
			
			Enumeration<Object> keys = props.keys();
			while (keys.hasMoreElements()) {
				String temp_key = (String)keys.nextElement();
				String erg = temp_key+"=";
				erg+=props.getProperty(temp_key);
				erg+="\n";
				
				writer.write(erg);
			}
			
			writer.flush();
			writer.close();
			
			System.out.println("Init-File wurde erfoglreich gespeichert");
			temp_save.delete();
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fehler!!!\nBeim erstellen eines neuen init.txt Files.");
			e.printStackTrace();
			temp_save.renameTo(new File(INIT_CONFIG));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim schreiben einer Zeile ins init.txt Files");
			e.printStackTrace();
			temp_save.renameTo(new File(INIT_CONFIG));
		}
		
	}
}
