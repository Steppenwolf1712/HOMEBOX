package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.remoteprocess.client.Control;
import de.clzserver.homebox.tray.parts.IMenuItem;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class ProcessMngr_Starter implements IMenuItem {

    private static final String REF = "PMNGR";
    private static final String NAME = "Prozess-Manager";

    @Override
    public void start() {
        new Control().showProcessManager();
    }

    @Override
    public String getName() {
        return NAME ;
    }

    @Override
    public String getRef() {
        return REF ;
    }

    @Override
    public IMenuItem[] getSubmenu() {
        return null;
    }

}
