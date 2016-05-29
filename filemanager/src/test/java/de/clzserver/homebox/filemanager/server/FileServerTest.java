package de.clzserver.homebox.filemanager.server;

import de.clzserver.homebox.filemanager.remote.base.RMI_Starter;

/**
 * Created by Marc Janﬂen on 25.05.2016.
 */
public class FileServerTest {

    public static void main(String[] args) {
        //Diese Zeile Reicht um den Filemanager zu starten.
        RMI_Starter starter = new RemoteFileServer_Starter();
    }
}
