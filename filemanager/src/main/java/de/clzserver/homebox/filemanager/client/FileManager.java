package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.remote.base.RemoteFile;
import de.clzserver.homebox.registry.RMI_Starter;
import de.clzserver.homebox.registry.RegistryHandle;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

abstract class FileManager extends RMI_Starter implements IFileManager {

    public FileManager(String host) {
        super(host, RemoteFile.class);
    }

    IFileServer server;

    public void doCustomRmiHandling() {
        try {
            Registry registry;
            if (m_host == null) {
                registry = LocateRegistry.getRegistry(RegistryHandle.REGISTRY_BINDING);
            } else
                registry = LocateRegistry.getRegistry(m_host, RegistryHandle.REGISTRY_BINDING);

            server = (IFileServer) registry.lookup(IFileServer.SERVICE_NAME);

        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Es gab einen Fehler bei der Komunkation mit dem Remoteserver!", e);
        } catch (NotBoundException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Der Dienst ist nicht in der Registry des Servers unter einem Namen registriert!", e);
        }
    }
}
