package de.clzserver.homebox.remoteprocess.remote.base;

import de.clzserver.homebox.remoteprocess.remote.PolicyFileLocator;

public abstract class RMI_Starter {

    public String m_host;

    /**
     *
     * @param classToAddToServerCodebase a class that should be in the java.rmi.server.codebase property.
     */
    public RMI_Starter(String host, Class classToAddToServerCodebase) {
        m_host = host;

        System.setProperty("java.rmi.server.codebase", classToAddToServerCodebase
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
