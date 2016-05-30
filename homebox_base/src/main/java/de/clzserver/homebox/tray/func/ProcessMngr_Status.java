package de.clzserver.homebox.tray.func;


import de.clzserver.homebox.remoteprocess.RemoteProcessHandle;
import de.clzserver.homebox.tray.parts.IMenuItem;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class ProcessMngr_Status implements IMenuItem {

    private RemoteProcessHandle handle;

    public ProcessMngr_Status() {
        handle = RemoteProcessHandle.getInstance();
    }

    @Override
    public void start() {
        handle.turnOnOffServer();
    }

    @Override
    public String getName() {
        return handle.getMenuItemMsg();
    }

    @Override
    public String getRef() {
        return"PMNGR_SERV";
    }

    @Override
    public IMenuItem[] getSubmenu() {
        return null;
    }

}
