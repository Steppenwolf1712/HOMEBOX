package de.clzserver.homebox.tray.parts;

import de.clzserver.homebox.tray.func.BudgetCalc_Starter;
import de.clzserver.homebox.tray.func.Clipboard;
import de.clzserver.homebox.tray.func.Closer;
import de.clzserver.homebox.tray.func.Info;
import de.clzserver.homebox.tray.func.Link_Collection;
import de.clzserver.homebox.tray.func.Placeholder;
import de.clzserver.homebox.tray.func.Property_Manager;

public class T_Icon_Menu {
	
	public static T_Icon_Menu clp_submenu = new T_Icon_Menu(new Clipboard());
	public static T_Icon_Menu budgetcalc = new T_Icon_Menu(new BudgetCalc_Starter());
	public static T_Icon_Menu placeholder1 = new T_Icon_Menu();
	public static T_Icon_Menu infoTray = new T_Icon_Menu(new Info());
	public static T_Icon_Menu properties = new T_Icon_Menu(new Property_Manager());
	public static T_Icon_Menu closeTray = new T_Icon_Menu(new Closer());
	public static T_Icon_Menu LinkTray = new T_Icon_Menu(new Link_Collection());
	
	private static int counter = 7;
	
	private static void update_Values() {
		values = new T_Icon_Menu[counter];
		values[0] = clp_submenu;
		values[1] = budgetcalc;
		values[2] = LinkTray;
		values[3] = placeholder1;
		values[4] = infoTray;
		values[5] = properties;
		values[6] = closeTray;
		
	}
	
	private static T_Icon_Menu[] values;
	
	public static T_Icon_Menu[] values() {
		update_Values();
		return values;
	}
	
	private FKT_DEF fkt_def;
	
	private T_Icon_Menu() {
		setDef(new Placeholder("", ""), true);
	}
	
	private T_Icon_Menu(IMenuItem def) {
		setDef(def, false);
	}
	
	@Deprecated
	private T_Icon_Menu(String name, String ref) {
		setDef(new Placeholder(name, ref), false);
	}
	
	private void setDef(IMenuItem def, boolean placeholder) {
		fkt_def = new FKT_DEF(def, placeholder);
	}

	public String get_fkt_Name() {
		return this.fkt_def.getName();
	}
	
	public String toString() {
		return this.fkt_def.getRef();
	}
	
	public void do_action() {
		this.fkt_def.do_funktion();
	}
	
	public boolean is_placeholder() {
		return this.fkt_def.is_placeholder();
	}
	
	public boolean has_submenu() {
		return this.fkt_def.has_Submenu();
	}
	
	public T_Icon_Menu[] get_submenu() {
		IMenuItem[] items = this.fkt_def.get_Submenu();
		
		T_Icon_Menu[] erg = new T_Icon_Menu[items.length];
		for (int i = 0; i<erg.length; i++)
			erg[i] = new T_Icon_Menu(items[i]);
		
		return erg;
	}
}

