package de.clzserver.homebox.tray.parts;

import java.awt.MenuItem;

import javax.swing.table.DefaultTableModel;

public class T_Icon_Item extends MenuItem{

	private T_Icon_Menu content = null;
	
	public T_Icon_Item(T_Icon_Menu p_enum) {
		content = p_enum;
		this.setLabel(content.get_fkt_Name());
		this.setName(content.toString());

		addActionListener(T_Icon_Listener.getInstance());
	}
	
	public void do_action() {
		content.do_action();
	}
}
