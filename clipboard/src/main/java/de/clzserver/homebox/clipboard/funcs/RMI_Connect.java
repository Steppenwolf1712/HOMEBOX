package de.clzserver.homebox.clipboard.funcs;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.filemanager.client.FileManagerFactory;
import de.clzserver.homebox.filemanager.client.IFileManager;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.filemanager.onlinecheck.OnlineStatus;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by Marc Janﬂen on 27.05.2016.
 */
public class RMI_Connect {

    private static RMI_Connect single;

    private IFileManager mgr;
    private String m_offset;

    public static RMI_Connect getInstance() {
        if (single == null)
            single = new RMI_Connect();
        return single;
    }

    private RMI_Connect() {
        OnlineStatus status = OnlineChecker.getInstance().get_CurrentOnlineStatus();

        HBPrinter.getInstance().printMSG(this.getClass(), "Der derzeitige Online-Status lautet:=\n\t"+status);

        mgr = FileManagerFactory.getInstance().getFileManager(status);

        m_offset = "";
    }

    /**
     * This methode is only for debugging and shall add the necessary offset of the file locations at the debuggin server:= "build/resources"
     * @param offset
     */
    public void setOffset(String offset) {
        m_offset = offset;
    }

    public File getFile(String filePath) throws IOException {
        File temp = mgr.getFile(m_offset+filePath);
        temp.deleteOnExit();
        return temp;
    }

    public boolean commitFile(File content, String filePath) throws RemoteException {
        return mgr.commitFile(content, m_offset+filePath, null, false, true);
    }

    public String[] getContentList(String filePath) throws RemoteException {
        return mgr.getDirectoryContent(m_offset + filePath);
    }

    public boolean deleteFile(String filePath) throws RemoteException {
        return mgr.deleteFile(m_offset+filePath);
    }
}
