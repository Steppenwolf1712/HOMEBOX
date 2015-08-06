package de.clzserver.homebox.tray.parts;

import java.awt.Menu;

public class T_Icon_SubMenu extends Menu{

	public T_Icon_SubMenu(T_Icon_Menu p_enum) {
		this.setLabel(p_enum.get_fkt_Name());
		this.setName(p_enum.toString());
	}
}
