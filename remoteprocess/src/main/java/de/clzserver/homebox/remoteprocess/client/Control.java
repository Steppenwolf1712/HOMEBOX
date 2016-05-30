package de.clzserver.homebox.remoteprocess.client;

import de.clzserver.homebox.remoteprocess.client.gui.ProcessManager;

import javax.swing.*;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class Control {

    public void showProcessManager() {
        ProcessManager pmngr = new ProcessManager();

        pmngr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pmngr.pack();
        pmngr.setVisible(true);
    }
}
