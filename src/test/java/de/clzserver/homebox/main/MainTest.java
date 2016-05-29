package de.clzserver.homebox.main;

import java.awt.SystemTray;

import de.clzserver.homebox.clipboard.funcs.RMI_Connect;
import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.tray.T_Icon;

public class MainTest {

	public static final String SERVERMODE = "ServerMode";
	public static final String CLIENTMODE = "ClientMode";

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
		String offset = "";
		if (args.length>1) {
			offset = args[1];
		}
		System.out.println("Starte HOMEBOX mit Option :=\n\tHOMEBOX-State= " + args[0] + "\n\tOffset= " + offset);

		//Lese schonmal die Config Informationen ein
		if (args[0].equals(SERVERMODE)) {
			OnlineChecker.setServerMode(true);
		}
		cfg = Config.getInstance();
		RMI_Connect.getInstance().setOffset(offset);//"build\\resources\\main\\");

		// F�gt shutdown hook zum Speichern der Init und zum Entfernen des TrayIcons hinzu
		ShutdownHook shutdownHook = new ShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		if (SystemTray.isSupported()) {
			System.out.println("MainTest: Das Systemtray wird supported!");
			

			trayIcon = T_Icon.getInstance(offset);
			trayIcon.show();
		} else {
			System.out.println("MainTest: Das SystemTray wird leider nicht supported :-(");
		}
	}

}
