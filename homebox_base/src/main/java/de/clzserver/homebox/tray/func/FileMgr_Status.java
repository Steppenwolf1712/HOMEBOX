package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.filemanager.FileManagerHandle;
import de.clzserver.homebox.tray.parts.IMenuItem;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class FileMgr_Status implements IMenuItem{

    private FileManagerHandle handle;

    public FileMgr_Status() {
        handle = FileManagerHandle.getInstance();
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
        return"FMNGR_SERV";
    }

    @Override
    public IMenuItem[] getSubmenu() {
        return null;
    }

}
