package de.clzserver.homebox.tray.func;

import javax.swing.JOptionPane;

import de.clzserver.homebox.tray.parts.IMenuItem;

public class Info implements IMenuItem{

	@Override
	public void start() {
		JOptionPane.showMessageDialog(null, "Das ist das �BELST COOLE Programm von\nJanine und Marc ^^");
		System.out.println("Das ist das �BELST COOLE Programm von\nJanine und Marc ^^");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Info";
	}

	@Override
	public String getRef() {
		return "INFO";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		return null;
	}

}
