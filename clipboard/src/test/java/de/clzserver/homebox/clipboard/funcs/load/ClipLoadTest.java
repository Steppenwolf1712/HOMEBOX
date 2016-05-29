package de.clzserver.homebox.clipboard.funcs.load;

import de.clzserver.homebox.clipboard.Clipboard_Fkts;
import de.clzserver.homebox.clipboard.funcs.RMI_Connect;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class ClipLoadTest {

    public static void main(String[] args) {
        RMI_Connect.getInstance().setOffset("build\\resources\\main\\");
        Clipboard_Fkts.getInstance().loadClipboard();
    }
}
