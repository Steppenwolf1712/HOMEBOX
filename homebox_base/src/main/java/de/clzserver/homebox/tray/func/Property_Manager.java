package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.config.PropMngr;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class Property_Manager implements IMenuItem{

	@Override
	public void start() {
		PropMngr mngr = new PropMngr();
		mngr.pack();
		mngr.setLocationRelativeTo(null);
		mngr.setVisible(true);
	}

	@Override
	public String getName() {
		return "Eigenschaften";
	}

	@Override
	public String getRef() {
		return "PROPERTIES";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
