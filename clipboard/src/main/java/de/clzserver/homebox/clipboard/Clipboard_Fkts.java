package de.clzserver.homebox.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JOptionPane;

import de.clzserver.homebox.clipboard.funcs.load.ClipLoad;
import de.clzserver.homebox.clipboard.funcs.save.ClipSave;
import de.clzserver.homebox.config.HBPrinter;

public class Clipboard_Fkts {
	
	private ClipSave save = null;
	private ClipLoad load; 
	private static Clipboard_Fkts single = null;
	
	private Clipboard_Fkts() {
		save = new ClipSave();
		load = new ClipLoad();
	}
	
	public static Clipboard_Fkts getInstance() {
		if (single==null)
			single = new Clipboard_Fkts();
		return single;
	}
	
	public void loadClipboard() {
		load.load();
	}
	
	public void saveClipboard() {
		Clipboard system_clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable data = system_clip.getContents(null);
		
			try {				
				save.save(data, system_clip);
			}
			catch (UnsupportedFlavorException e) {
				HBPrinter.getInstance().printError(this.getClass(), "Der Inhalt der jetzigen Zwischenablage wird von Java nicht unterstützt",e);
			} catch (IOException e) {
				HBPrinter.getInstance().printError(this.getClass(), "Ein/Ausgabefehler", e);
			}
	}
	
}
