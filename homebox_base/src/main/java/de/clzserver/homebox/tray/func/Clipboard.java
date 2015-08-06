package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.tray.parts.IMenuItem;

public class Clipboard implements IMenuItem{

	public static IMenuItem clp_saver = new Clipboard_Saver();
	public static IMenuItem clp_loader = new Clipboard_Loader();
	
	@Override
	public void start() {
		System.out.println("Clipboard: Das Root-Element des Clipboad-Submenu bietet von sich aus keine Funktion an!");
	}

	@Override
	public String getName() {
		return "Zwischenablage";
	}

	@Override
	public String getRef() {
		return "CLP_Menu";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		IMenuItem[] items = new IMenuItem[2];
		items[0] = clp_saver;
		items[1] = clp_loader;
		
		return items;
	}

}
