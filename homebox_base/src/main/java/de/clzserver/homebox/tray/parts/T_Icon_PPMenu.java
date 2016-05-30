package de.clzserver.homebox.tray.parts;

import java.awt.Component;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;

public class T_Icon_PPMenu extends PopupMenu{

	public void show(Component comp, int a, int b) {
		update_Labels();
		super.show(comp, a, a);
	}

	public void update_Labels() {
		T_Icon_Menu[] vals = T_Icon_Menu.values(), subvals;
		for (int i = 0; i<vals.length; i++) {
			updateLabel_of_Item(vals[i].toString(), vals[i].get_fkt_Name());
			if (vals[i].has_submenu()) {
				subvals = vals[i].get_submenu();
				for (int j = 0; j<subvals.length; j++)
					updateLabel_of_Item(subvals[j].toString(), subvals[j].get_fkt_Name());
			}
		}
	}

	private void updateLabel_of_Item(String item_key, String item_Label) {
		int max = this.getItemCount();
		MenuItem temp = this.getItem(0);
		for (int i = 0; i<max-1; temp=this.getItem(++i)) {
			if (temp instanceof Menu) {
				Menu m = (Menu) temp;
				int m_max =m.getItemCount();

				for(int j = 0; j<m_max; j++) {
					temp = m.getItem(j);

					if (temp.getName().equals(item_key)) {
						temp.setLabel(item_Label);
						return;
					}
				}
				continue;
			}

			if (temp.getName().equals(item_key)) {
				temp.setLabel(item_Label);
				return;
			}
		}
	}
}
