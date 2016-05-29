package de.clzserver.homebox.remoteprocess.remote.base;

import de.clzserver.homebox.remoteprocess.remote.IRemoteCommand;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class RemoteCommand implements IRemoteCommand {

    private final String type;
    private final String process;

    public RemoteCommand(String type, String process) {
        this.type = type;
        this.process = process;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getProcessName() {
        return process;
    }
}
