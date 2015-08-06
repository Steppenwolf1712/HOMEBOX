package de.clzserver.homebox.tray.func;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import de.clzserver.homebox.tray.parts.IMenuItem;

public class Link_Collection implements IMenuItem{

	private ArrayList<IMenuItem> submenu = null;
	
	@Override
	public void start() {
		JOptionPane.showMessageDialog(null, "Ich kann noch nichts, aber ich bin da ");
	
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Link-Sammlung";
	}

	@Override
	public String getRef() {
		return "LINK_SAMMLUNG";
	}

	@Override
	public IMenuItem[] getSubmenu() {
		//Irrelevant wie groß das Array ist, es wird eh gleich überschrieben ^^
		IMenuItem[] items = new IMenuItem[0];
		
		//Wenn die ArrayList noch inicht initialisiert wurde, wird sie mit Standardkrams gefüllt.
		if (submenu == null)
			init_Submenu();
		items = submenu.toArray(items);
		
		return items;
	}

	private void init_Submenu() {

		//Hier könnte man ansetzen die bereitsgespeicherten Links einzusetzen.
		submenu = new ArrayList<IMenuItem>();
		submenu.add(new Link_add());
	}

}


