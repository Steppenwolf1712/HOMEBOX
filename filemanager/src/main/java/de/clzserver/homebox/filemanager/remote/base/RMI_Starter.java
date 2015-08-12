package de.clzserver.homebox.filemanager.remote.base;

import de.clzserver.homebox.filemanager.remote.PolicyFileLocator;

public abstract class RMI_Starter {

    /**
     *
     * @param clazzToAddToServerCodebase a class that should be in the java.rmi.server.codebase property.
     */
    public RMI_Starter(Class clazzToAddToServerCodebase) {

        System.setProperty("java.rmi.server.codebase", clazzToAddToServerCodebase
            .getProtectionDomain().getCodeSource().getLocation().toString());

        System.setProperty("java.security.policy", PolicyFileLocator.getLocationOfPolicyFile());

        if(System.getSecurityManager() == null) {
        	System.out.println("Starte Securitymanager!");
            System.setSecurityManager(new SecurityManager());
        }

        doCustomRmiHandling();
    }

    /**
     * extend this class and do RMI handling here
     */
    public abstract void doCustomRmiHandling();
}
