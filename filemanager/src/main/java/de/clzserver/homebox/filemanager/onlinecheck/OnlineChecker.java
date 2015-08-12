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

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;


public class OnlineChecker {
	public static final String google = "http://google.de"; 
	
	
	public static OnlineStatus checkStatus() {
		
		
		try {
//			if(isServerReachable()){
//		
//				HBPrinter.getInstance().printMSG(OnlineChecker.class, "Du bist im LAN");
//				return OnlineStatus.Lan;
//			}
				URL urlGoogle = new URL(google);
				if(isReachable(urlGoogle)){ 
					HBPrinter.getInstance().printMSG(OnlineChecker.class, "Du bist Online");
					return OnlineStatus.Online; 
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		HBPrinter.getInstance().printMSG(OnlineChecker.class, "Du bist Offline");
		return OnlineStatus.Offline;
	}
	public static boolean isServerReachable(){
	Config cfg = Config.getInstance();
	
	try {
		BufferedReader buff = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(cfg.getValue(Config.SAVE_PATH_KEY)+cfg.getValue(Config.SAVETYPE_NAME_KEY)) ));
		
		buff.readLine();
		
		buff.close();
		return true; 
	} catch (FileNotFoundException e) {
		e.printStackTrace();
		return false;
	} catch (IOException e) {
		e.printStackTrace();
		return false;
	}
	}
	
	//checks for connection to the internet through dummy request
    public static boolean isReachable(URL url)
    {
        try {
            //make a URL to a known source
            

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
