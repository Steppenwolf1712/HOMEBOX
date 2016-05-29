package de.clzserver.homebox.remoteprocess.server;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;
import de.clzserver.homebox.remoteprocess.remote.base.RMI_Starter;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Marc Janßen on 29.05.2016.
 */
public class RemoteProcessServer_Starter extends RMI_Starter{

    private RemoteProcessServer pmngr;

    public RemoteProcessServer_Starter() {
        super(null, RemoteProcessServer.class);
    }

    public RemoteProcessServer getServer() {
        return pmngr;
    }



    @Override
    public void doCustomRmiHandling() {
        try {
            // System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            HBPrinter.getInstance().printMSG(this.getClass(), "Eröffne Bibliothek!");
            pmngr = new RemoteProcessServer();

            HBPrinter.getInstance().printMSG(this.getClass(), "Starte Dienst RemoteProcessServer!");
            IProcessServer stub = (IProcessServer) UnicastRemoteObject
                    .exportObject(pmngr, IProcessServer.RMI_LAN_PORT);

            Registry registry = LocateRegistry.createRegistry(IProcessServer.REGISTRY_BINDING);

            HBPrinter.getInstance().printMSG(
                    this.getClass(),
                    "Veröffentliche Bibliothek-Dienst unter "
                            + IProcessServer.SERVICE_NAME + "!");
            registry.rebind(IProcessServer.SERVICE_NAME, stub);

            HBPrinter.getInstance().printMSG(this.getClass(), "Process-Server hochgefahren!");
        } catch (Exception e) {
            HBPrinter.getInstance().printError(this.getClass(),
                    "Konnte Server nicht starten!", e);
        }
    }


}
