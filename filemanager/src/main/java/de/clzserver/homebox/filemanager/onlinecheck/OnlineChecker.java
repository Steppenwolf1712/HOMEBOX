package de.clzserver.homebox.filemanager.onlinecheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Observable;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;

import javax.swing.*;


public class OnlineChecker extends Observable {
	public static final String google = "http://google.de";

	private Checker checker;

	private OnlineStatus currentState;

	private static OnlineChecker single = null;

	//Der Server muss nciht online sein und bekommt einen OfflineStatus, Es reicht wenn er für andere erreichbar ist.
	//Das muss ihn selbst aber nicht kümmern
	private static boolean isServer = false;

	private OnlineChecker() {
		checker = new Checker(this);
		currentState = null;
	}

	public static OnlineChecker getInstance() {
		if (single == null)
			single = new OnlineChecker();
		return single;
	}

	private void compareCurrentState(OnlineStatus state) {
		if (currentState != null && !state.equals(currentState))
			notifyObservers(state);

		System.out.println("Der derzeitige OnlineStatus beträgt: "+state);

		currentState = state;
	}

	public static void setServerMode(boolean p_isServer) {
		isServer = p_isServer;
	}
	public static boolean isServer() {
		return isServer;
	}

	public OnlineStatus get_CurrentOnlineStatus() {
		if (OnlineChecker.isServer)
			return OnlineStatus.Offline;
		currentState = OnlineChecker.checkStatus();
		return currentState;
	}

	public static OnlineStatus checkStatus() {
		if (OnlineChecker.isServer)
			return OnlineStatus.Offline;
		try {
			if (isServerReachable()) {
				return OnlineStatus.Lan;
			}
			URL urlGoogle = new URL(google);
			if (isReachable(urlGoogle)) {
				HBPrinter.getInstance().printMSG(OnlineChecker.class, "Du bist Online");
				return OnlineStatus.Online;
			}
		} catch (MalformedURLException e) {
			HBPrinter.getInstance().printError(OnlineChecker.class, "Falsch geformte URL zum checken des online Status entdeckt!", e);
		}
	
		HBPrinter.getInstance().printMSG(OnlineChecker.class, "Du bist Offline");
		return OnlineStatus.Offline;
	}

	private static boolean isServerReachable() {
		Config cfg = Config.getInstance();

		try {
			BufferedReader buff = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(cfg.getValue(Config.HOMEBOX_PATH_KEY)+
									cfg.getValue(Config.SAVE_PATH_KEY)+cfg.getValue(Config.SAVETYPE_NAME_KEY)) ));

			buff.readLine();

			buff.close();
			return true;
		} catch (FileNotFoundException e) {
			HBPrinter.getInstance().printError(OnlineChecker.class, "Konnte den Server im lokalen Netzwerk nicht finden!", e);
			return false;
		} catch (IOException e) {
			HBPrinter.getInstance().printError(OnlineChecker.class, "Konnte den Server im lokalen Netzwerk nicht finden!", e);
			return false;
		}
	}
	
	//checks for connection to the internet through dummy request
    private static boolean isReachable(URL url)
    {
        try {
            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

			return true;
        } catch (UnknownHostException e) {
            HBPrinter.getInstance().printError(OnlineChecker.class, "Konnte den Server im lokalen Netzwerk nicht finden!", e);
        }
        catch (IOException e) {
			HBPrinter.getInstance().printError(OnlineChecker.class, "Ein-/Ausgabefehler beim Versuch auf ein File zuzugreifen während des Online-Checks!", e);
        }
		return false;
    }

	public void startOnlineChecker() {
		if (!checker.isRunning())
			checker.start();
	}

	public void stopOnlineChecker() {
		checker.stopThread();
	}

	private class Checker extends Thread {
		private boolean runs = false;
		private OnlineChecker m_parent;

		public Checker(OnlineChecker parent) {
			m_parent = parent;
		}

		public boolean isRunning() {
			return runs;
		}

		public void stopThread() {
			runs = false;
			this.interrupt();
		}

		public void run() {
			runs = true;

			while (runs) {
				m_parent.compareCurrentState(OnlineChecker.checkStatus());

				try {
					Thread.sleep(180000);
				} catch (InterruptedException e) {
					HBPrinter.getInstance().printError(this.getClass(), "Die OnlineStatus-Abfrage wurde unterbrochen!", e);
				}
			}
			HBPrinter.getInstance().printMSG(this.getClass(), "Die OnlineStatus-Abfrage wurde beendet!");
		}

	}
}
