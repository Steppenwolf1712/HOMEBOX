package de.clzserver.homebox.tray.parts;

import de.clzserver.homebox.filemanager.onlinecheck.OnlineChecker;
import de.clzserver.homebox.tray.func.*;

public class T_Icon_Menu {
	
	public static T_Icon_Menu clp_submenu = new T_Icon_Menu(new Clipboard());
	public static T_Icon_Menu budgetcalc = new T_Icon_Menu(new BudgetCalc_Starter());
	public static T_Icon_Menu processmngr;
	public static T_Icon_Menu linkTray = new T_Icon_Menu(new Link_Collection());
	public static T_Icon_Menu placeholder1 = new T_Icon_Menu();
	public static T_Icon_Menu servermanager;
	public static T_Icon_Menu placeholder2 = new T_Icon_Menu();
	public static T_Icon_Menu infoTray = new T_Icon_Menu(new Info());
	public static T_Icon_Menu properties = new T_Icon_Menu(new Property_Manager());
	public static T_Icon_Menu closeTray = new T_Icon_Menu(new Closer());
	
	private static int counter = 7;
	
	private static void update_Values() {
		if (OnlineChecker.isServer()) {
			counter = 9;

			servermanager = new T_Icon_Menu(new ServerManager());

			values = new T_Icon_Menu[counter];
			values[0] = clp_submenu;
			values[1] = budgetcalc;
			values[2] = linkTray;
			values[3] = placeholder1;
			values[4] = servermanager;
			values[5] = placeholder2;
			values[6] = infoTray;
			values[7] = properties;
			values[8] = closeTray;
		} else {
			counter = 8;

			processmngr = new T_Icon_Menu(new ProcessMngr_Starter());

			values = new T_Icon_Menu[counter];
			values[0] = clp_submenu;
			values[1] = budgetcalc;
			values[2] = processmngr;
			values[3] = linkTray;
			values[4] = placeholder1;
			values[5] = infoTray;
			values[6] = properties;
			values[7] = closeTray;
		}
		
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

