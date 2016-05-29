package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.tray.parts.IMenuItem;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class ServerManager implements IMenuItem {

    public static IMenuItem filemgr_status = new FileMgr_Status();

    @Override
    public void start() {
        HBPrinter.getInstance().printMSG(this.getClass(), "Das Root-Element des Server-Submenu bietet von sich aus keine Funktion an!");
    }

    @Override
    public String getName() {
        return "Server-Status";
    }

    @Override
    public String getRef() {
        return "S_Menu";
    }

    @Override
    public IMenuItem[] getSubmenu() {
        IMenuItem[] items = new IMenuItem[1];
        items[0] = filemgr_status;

        return items;
    }
}
