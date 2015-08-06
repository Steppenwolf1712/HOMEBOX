package de.clzserver.homebox.main;

import java.awt.SystemTray;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.tray.T_Icon;

public class MainTest {

	static class ShutdownHook extends Thread {
		public void run() {
			if (cfg != null)
				cfg.save();
		}
	}

	private static Config cfg = null;
	private static T_Icon trayIcon = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//Lese schonmal die Config Informationen ein
		cfg = Config.getInstance();
		
		// Fügt shutdown hook zum Speichern der Init und zumentfernen des TrayIcons hinzu
		ShutdownHook shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		if (SystemTray.isSupported()) {
			System.out.println("MainTest: Das Systemtray wird supported!");
			

			trayIcon = T_Icon.getInstance();
			trayIcon.show();
		} else {
			System.out.println("MainTest: Das SystemTray wird leider nicht supported :-(");
		}
	}

}
