package de.clzserver.homebox.remoteprocess.server;

import de.clzserver.homebox.registry.RegistryHandle;
import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;
import de.clzserver.homebox.registry.RMI_Starter;
import de.clzserver.homebox.remoteprocess.remote.IRemoteCommand;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Marc Janßen on 29.05.2016.
 */
public class RemoteProcessServer_Starter extends RMI_Starter {

    private RemoteProcessServer pmngr;

    public RemoteProcessServer_Starter() {
        super(null, IRemoteCommand.class);
    }

    public boolean shutDown() {
        try {
            Registry registry = RegistryHandle.getInstance().getRegistry();

            HBPrinter.getInstance().printMSG(
                    this.getClass(),
                    "Entferne Bibliothek-Verweis "
                            + IProcessServer.SERVICE_NAME + " aus der Registry!");
            registry.unbind(IProcessServer.SERVICE_NAME);

            HBPrinter.getInstance().printMSG(this.getClass(), "Stoppe Dienst RemoteProcessServer!");
            return UnicastRemoteObject.unexportObject(pmngr, false);
        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(RemoteProcessServer_Starter.class,
                    "Konnte den Service des ProcessManagers nicht stoppen!", e);
        } catch (NotBoundException e) {
            HBPrinter.getInstance().printError(RemoteProcessServer_Starter.class,
                    "Der Service des ProcessManagers ist zurzeit nicht gebunden und kann deshalb nicht von der Registryy entfernt werden!", e);
        }
        return false;
    }

    @Override
    public void doCustomRmiHandling() {
        start();
    }

    public void start() {
        try {
            HBPrinter.getInstance().printMSG(this.getClass(), "Eröffne Bibliothek!");
            pmngr = new RemoteProcessServer();

            HBPrinter.getInstance().printMSG(this.getClass(), "Starte Dienst RemoteProcessServer!");
            IProcessServer stub = (IProcessServer) UnicastRemoteObject
                    .exportObject(pmngr, IProcessServer.RMI_LAN_PORT);

            Registry registry = RegistryHandle.getInstance().getRegistry();

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
