package de.clzserver.homebox.clipboard.funcs.save;

import de.clzserver.homebox.clipboard.Clipboard_Fkts;
import de.clzserver.homebox.clipboard.funcs.RMI_Connect;

import javax.swing.*;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class ClipSaveTest {

    public static void main(String[] args) {
        RMI_Connect.getInstance().setOffset("build\\resources\\main\\");
        Clipboard_Fkts.getInstance().saveClipboard();
    }
}
