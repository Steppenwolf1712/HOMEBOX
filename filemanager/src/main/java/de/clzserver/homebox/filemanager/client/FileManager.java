package de.clzserver.homebox.filemanager.client;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.filemanager.remote.IFileServer;
import de.clzserver.homebox.filemanager.remote.base.RMI_Starter;
import de.clzserver.homebox.filemanager.remote.base.RemoteFile;

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
                System.out.println("Bin hier und hole mir die lokale registry... glaub ich....");
                registry = LocateRegistry.getRegistry(IFileServer.REGISTRY_BINDING);
            } else
                registry = LocateRegistry.getRegistry(m_host, IFileServer.REGISTRY_BINDING);

            server = (IFileServer) registry.lookup(IFileServer.SERVICE_NAME);

        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "", e);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
