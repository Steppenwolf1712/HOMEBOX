package de.clzserver.homebox.tray.func;


import de.clzserver.homebox.linkcollection.LinkAdd;
import de.clzserver.homebox.tray.parts.IMenuItem;

public class Link_add implements IMenuItem {
	

	@Override
	public void start() {
		new LinkAdd();
	}

	@Override
	public String getName() {

		return "Hinzufügen";
	}

	@Override
	public String getRef() {

		return "ADD";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
