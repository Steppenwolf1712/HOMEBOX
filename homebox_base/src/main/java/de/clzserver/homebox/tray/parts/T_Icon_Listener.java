package de.clzserver.homebox.tray.parts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class T_Icon_Listener implements ActionListener{

	private static T_Icon_Listener single = null;

	private T_Icon_Listener() {
		
	}
	
	public static T_Icon_Listener getInstance() {
		if (single == null)
			single = new T_Icon_Listener();
		return single;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		if (arg0.getSource() instanceof T_Icon_Item) {
			T_Icon_Item item = (T_Icon_Item) arg0.getSource();
			item.do_action();
		}
	}
}
