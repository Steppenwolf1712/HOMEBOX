package de.clzserver.homebox.remoteprocess.client.rmi;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.registry.RMI_Starter;
import de.clzserver.homebox.registry.RegistryHandle;
import de.clzserver.homebox.remoteprocess.remote.IProcessServer;
import de.clzserver.homebox.remoteprocess.remote.IRemoteCommand;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class ProcessServer extends RMI_Starter implements IProcessServer {

    public ProcessServer(String m_host) {
        super(m_host, IRemoteCommand.class);
    }

    private IProcessServer server;

    @Override
    public String[] getAvailableApps() throws RemoteException {
        return server.getAvailableApps();
    }

    @Override
    public Map<String, Boolean> getRunningApps() throws RemoteException {
        return server.getRunningApps();
    }

    @Override
    public boolean executeCommand(IRemoteCommand command) throws RemoteException {
        return server.executeCommand(command);
    }

    @Override
    public void doCustomRmiHandling() {
        try {
            Registry registry;
            if (m_host == null) {
                registry = LocateRegistry.getRegistry(RegistryHandle.REGISTRY_BINDING);
            } else
                registry = LocateRegistry.getRegistry(m_host, RegistryHandle.REGISTRY_BINDING);

            server = (IProcessServer) registry.lookup(IProcessServer.SERVICE_NAME);

        } catch (RemoteException e) {
            HBPrinter.getInstance().printError(this.getClass(), "Es gab einen Fehler bei der Komunkation mit dem Remoteserver \""+m_host+"\"!", e);
        } catch (NotBoundException e) {
            HBPrinter.getInstance().showError(this.getClass(),  "Der Dienst "+IProcessServer.SERVICE_NAME+" ist nicht in der Registry des Servers unter einem Namen registriert!", e);
            HBPrinter.getInstance().printError(this.getClass(), "Der Dienst " + IProcessServer.SERVICE_NAME + " ist nicht in der Registry des Servers unter einem Namen registriert!", e);
        }
    }
}
