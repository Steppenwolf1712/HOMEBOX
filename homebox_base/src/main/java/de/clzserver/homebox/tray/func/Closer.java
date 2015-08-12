package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.tray.T_Icon;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class Closer implements IMenuItem {

	@Override
	public void start() {
		HBPrinter.getInstance().printMSG(this.getClass(), "Vielen Dank das sie sich für HomeBox entschieden haben ;-)");
		T_Icon.getInstance().remove();
		System.exit(0);
	}

	@Override
	public String getName() {
		return "Beenden";
	}

	@Override
	public String getRef() {
		return "CLOSE";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
