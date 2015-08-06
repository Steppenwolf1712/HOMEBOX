package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.clipboard.Clipboard_Fkts;
import de.clzserver.homebox.clipboard.funcs.Type_Factory;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class Clipboard_Loader implements IMenuItem{

	@Override
	public void start() {
		Clipboard_Fkts.getInstance().loadClipboard();
	}

	@Override
	public String getName() {
		return "Laden ("+Type_Factory.getSaveType()+")";
	}

	@Override
	public String getRef() {
		return"CLP_LOAD";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
