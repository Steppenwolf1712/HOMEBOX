package de.clzserver.homebox.remoteprocess.client.rmi;

import de.clzserver.homebox.remoteprocess.remote.IProcessServer;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class ProcessServerFactory {

    private static ProcessServerFactory single = null;


    private String m_host;

    private ProcessServerFactory() {
        m_host = "CLZ-Server";
    }

    public static ProcessServerFactory getInstance() {
        if (single == null)
            single = new ProcessServerFactory();
        return single;
    }

    public void setHost(String host) {
        m_host = host;
    }

    public IProcessServer getProcessServer() {
        return new ProcessServer(m_host);
    }
}
