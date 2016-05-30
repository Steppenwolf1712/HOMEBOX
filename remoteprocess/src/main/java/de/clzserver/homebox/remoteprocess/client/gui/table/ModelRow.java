package de.clzserver.homebox.remoteprocess.client.gui.table;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;
import de.clzserver.homebox.remoteprocess.remote.base.RemoteCommand;

import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Marc Janßen on 29.05.2016.
 */
public class ModelRow {

    private static final String STATE_OFFLINE = "Offline";
    private static final String STATE_ONLINE = "Online";

    private final String processName;
    private boolean processState = false;

    private IProcessServer server;

    public ModelRow(String processName, IProcessServer server) {
        this.processName = processName;
        this.server = server;

        init();
    }

    private void init() {
        updateState();
    }

    public void updateState() {
        try {
            Map<String, Boolean> states = server.getRunningApps();

            processState = states.get(processName);
        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Die ModelRow-Instanz zu dem Prozess "+processName+" konnte den ProzessZustand nicht erfolgreich abgfragen!", e);
            processState = false;
        }
    }

    public String getProcessName() {
        return processName;
    }

    public String ProcessState() {
        if (processState)
            return STATE_ONLINE;
        else
            return STATE_OFFLINE;
    }

    public void turnState() {
        RemoteCommand command = null;
        if (processState)
            command = new RemoteCommand(RemoteCommand.CTYPE_STOP, processName);
        else
            command = new RemoteCommand(RemoteCommand.CTYPE_START, processName);

        try {
            server.executeCommand(command);
        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Fehler beim Übertragen/ausführen des RemoteCommands "+command+"!", e);
        }

    }

    public String getBtnText() {
        if (processState)
            return "Fahre "+processName+"runter";
        else
            return "Starte "+processName;
    }
}
