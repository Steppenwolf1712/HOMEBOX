package de.clzserver.homebox.remoteprocess.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * Created by Marc Jan�en on 29.05.2016.
 */
public interface IProcessServer extends Remote {

    String SERVICE_NAME = "ProcessServer";

    int RMI_LAN_PORT = 54124;


    public String[] getAvailableApps() throws RemoteException;
    public Map<String,Boolean> getRunningApps() throws RemoteException;

    public boolean executeCommand(IRemoteCommand command) throws RemoteException;
}
