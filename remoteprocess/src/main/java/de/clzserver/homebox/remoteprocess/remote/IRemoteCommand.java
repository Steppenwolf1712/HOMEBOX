package de.clzserver.homebox.remoteprocess.remote;

import java.io.Serializable;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public interface IRemoteCommand extends Serializable {

    public static final String CTYPE_START = "START_APP";
    public static final String CTYPE_STOP = "STOP_APP";

    public String getType();

    public String getProcessName();


}
