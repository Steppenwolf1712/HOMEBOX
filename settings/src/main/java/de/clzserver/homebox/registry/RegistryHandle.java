package de.clzserver.homebox.registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class RegistryHandle {

    private static RegistryHandle single = null;

    public static int REGISTRY_BINDING = 2001;

    private RegistryHandle() {
        init();
    }

    private void init() {

    }

    public static RegistryHandle getInstance() {
        if (single == null)
            single = new RegistryHandle();
        return single;
    }

    private Registry reg = null;

    public Registry getRegistry() {
        if (reg == null)
            try {
                reg = LocateRegistry.createRegistry(REGISTRY_BINDING);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        return reg;
    }
}
