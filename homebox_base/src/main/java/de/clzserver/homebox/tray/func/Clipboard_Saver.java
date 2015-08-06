package de.clzserver.homebox.tray.func;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;

import de.clzserver.homebox.clipboard.Clipboard_Fkts;
import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class Clipboard_Saver implements IMenuItem {

	@Override
	public void start() {
		Clipboard_Fkts.getInstance().saveClipboard();
	}

	@Override
	public String getName() {

		Clipboard system_clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable data = system_clip.getContents(null);
		
		if (data == null)
			return "Trasnferieren";
		
		String teil = Type_Factory.getTransferableType(data);
		if (teil != null) {
			teil = " ("+teil+")";
			return "Transferieren"+teil;
		}
		return "Transferieren";
	}

	@Override
	public String getRef() {
		return "CLP_SAVE";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
