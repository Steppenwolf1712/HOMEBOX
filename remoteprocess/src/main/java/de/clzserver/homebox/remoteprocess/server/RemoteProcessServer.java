package de.clzserver.homebox.remoteprocess.server;

import de.clzserver.homebox.config.Config;
import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;
import de.clzserver.homebox.remoteprocess.remote.IRemoteCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marc Janßen on 29.05.2016.
 */
public class RemoteProcessServer implements IProcessServer {

    private static final String[] APPS = {"Itunes","VisualSVN"};
//    private static final String APP_VSVN = "VisualSVN";
    public static final String S_PROCESS = "_PROCESS_NAME";
    public static final String S_LOCATION = "_LOCATION_PATH";

    private Map<String, String> processMap;
    private Map<String, String> locationMap;
    private Map<String, Boolean> runningMap;

    public RemoteProcessServer() {
        init();
    }

    private void init() {
        initMaps();
        for (String s: APPS) {
            prepare(s);
        }
        try {
            updateRunning(APPS);
        } catch (IOException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Konstruktor wirft Fehler!!!\nKonnte den Befehl \"taslklist\" nicht erfolgreich ausführen!", e);
        }
    }

    private void initMaps() {
        processMap = new HashMap<String, String>();
        locationMap = new HashMap<String, String>();
        runningMap = new HashMap<String, Boolean>();
    }

    private void updateRunning(String[] apps) throws IOException {
        Runtime runner = Runtime.getRuntime();

        Process p = runner.exec("cmd /c tasklist");

        Reader r = new InputStreamReader(p.getInputStream());
        BufferedReader reader = new BufferedReader(r);

        String line = reader.readLine();
        ArrayList<String> list = new ArrayList<String>();
        for (String s: apps)
            list.add(s);

        while (line != null) {
            for (String s: list) {
                if (line.startsWith(processMap.get(s))) {
                    runningMap.put(s, true);
                    list.remove(s);
                }
            }
            if (list.isEmpty())
                break;
            line = reader.readLine();
        }
        if (!list.isEmpty()) {
            for (String s: list)
                runningMap.put(s, false);
        }

    }

    private void prepare(String s) {
        Config cfg = Config.getInstance();

        processMap.put(s, cfg.getValue(s + S_PROCESS));
        locationMap.put(s,cfg.getValue(s + S_LOCATION));
    }


    @Override
    public String[] getAvailableApps() throws RemoteException {
        return APPS;
    }

    @Override
    public Map<String, Boolean> getRunningApps() throws RemoteException {
        try {
            updateRunning(APPS);
        } catch (IOException e) {
            throw new RemoteException(this.getClass()+ ": Konnte den Befehl \"taslklist\" nicht erfolgreich ausführen!\n"+e.getMessage());
        }

        return runningMap;
    }

    @Override
    public boolean executeCommand(IRemoteCommand command) throws RemoteException {
        String toExec = "cmd /c ", app;

        switch (command.getType()) {
            case IRemoteCommand.CTYPE_START:
                app = command.getProcessName();
                if (!hasApp(app))
                    throw new RemoteException(this.getClass()+": Konnt das Programm "+app+"nicht in der List der verfügbaren Programme finden!");
                toExec += locationMap.get(app);

                try {
                    Runtime.getRuntime().exec(toExec);
                } catch (IOException e) {
                    throw new RemoteException(this.getClass()+": Beim Starten des Programms "+app+"ist ein Fehler aufgetreten!");
                }
                return true;
            case IRemoteCommand.CTYPE_STOP:
                app = command.getProcessName();
                if (!hasApp(app))
                    throw new RemoteException(this.getClass()+": Konnt das Programm "+app+"nicht in der List der verfügbaren Programme finden!");
                toExec += "taskkill "+processMap.get(app);

                if (!runningMap.get(app))
                    return false;

                try {
                    Runtime.getRuntime().exec(toExec);
                } catch (IOException e) {
                    throw new RemoteException(this.getClass()+": Beim Schliessen des Programms "+app+"ist ein Fehler aufgetreten!");
                }
                return true;
            default:
                throw new RemoteException(this.getClass()+": Konnte den Befehl "+command.getType()+" nicht ausführen, a er dem ProcessServer unbekannt ist!");
        }
    }

    private boolean hasApp(String app) {
        for (String name: APPS) {
            if (name.equals(app))
                return true;
        }
        return false;
    }
}
