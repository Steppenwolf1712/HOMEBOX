package de.clzserver.homebox.clipboard.funcs;

import de.clzserver.homebox.clipboard.Clipboard_Fkts;

import javax.swing.*;

/**
 * Created by Marc Janﬂen on 28.05.2016.
 */
public class CompleteClipBoardTest {

    public static void main(String[] args) {
        RMI_Connect.getInstance().setOffset("build\\resources\\main\\");
        Clipboard_Fkts.getInstance().saveClipboard();

        JOptionPane.showInputDialog("Sach Bescheid wenn ich den Server nach der Zwischenablage fragen soll!");

        Clipboard_Fkts.getInstance().loadClipboard();
    }
}
