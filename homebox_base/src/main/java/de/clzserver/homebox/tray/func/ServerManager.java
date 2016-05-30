package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.tray.parts.IMenuItem;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class ServerManager implements IMenuItem {

    public static IMenuItem fileMngr_status = new FileMgr_Status();
    public static IMenuItem processMngr_status  = new ProcessMngr_Status();

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
        IMenuItem[] items = new IMenuItem[2];
        items[0] = fileMngr_status;
        items[1] = processMngr_status;

        return items;
    }
}
