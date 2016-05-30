package de.clzserver.homebox.remoteprocess;

import de.clzserver.homebox.remoteprocess.server.RemoteProcessServer_Starter;

import java.util.Observable;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class RemoteProcessHandle extends Observable {

    private static RemoteProcessHandle single = null;

    private boolean isrunning = false;
    private RemoteProcessServer_Starter server;

    private RemoteProcessHandle() {
        init();
    }

    private void init() {
        server = new RemoteProcessServer_Starter();
        isrunning = true;
    }

    public static RemoteProcessHandle getInstance() {
        if (single == null)
            single = new RemoteProcessHandle();
        return single;
    }

    public void startServer() {
        if (isrunning)
            return;
        isrunning = true;
        server.start();
    }

    public void stopServer() {
        isrunning = !server.shutDown();
    }

    public void turnOnOffServer() {
        if (isrunning)
            stopServer();
        else
            startServer();
    }

    public String getMenuItemMsg() {
        if (isrunning)
            return "Remote-ProcessServer beenden(Server l‰uft)";
        else
            return "Remote-ProcessServer starten(Server ist aus)";
    }

    public boolean isRunning() {
        return isrunning;
    }
}
