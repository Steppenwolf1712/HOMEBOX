package de.clzserver.homebox.tray.func;

import de.clzserver.homebox.tray.parts.IMenuItem;

public class Placeholder implements IMenuItem{

	private String ref;
	private String name;

	public Placeholder(String n, String r) {
		name = n;
		ref = r;
	}

	@Override
	public void start() {
		System.out.println("Placeholder machen nichts!");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getRef() {
		return ref;
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
