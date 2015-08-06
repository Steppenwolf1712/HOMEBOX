package de.clzserver.homebox.tray;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import de.clzserver.homebox.config.HBPrinter;
import de.clzserver.homebox.tray.parts.T_Icon_Menu;
import de.clzserver.homebox.tray.parts.T_Icon_Item;
import de.clzserver.homebox.tray.parts.T_Icon_SubMenu;
import de.clzserver.homebox.tray.parts.T_Icon_PPMenu;

public class T_Icon implements MouseListener{

	private TrayIcon tray = null;
	private T_Icon_PPMenu ppmenu = null;
	
	private static SystemTray sysTray;

	private static T_Icon single = null;

	private T_Icon(Image img) {
		create_PPMenu();

		// Hole dir die SystemTray Instanz
		sysTray = SystemTray.getSystemTray();

		tray = new TrayIcon(img, "HomeBox", ppmenu);
		tray.setImageAutoSize(true);
		tray.addMouseListener(this);
	}

	public static T_Icon getInstance() {
		if (single == null) {
			// Lade ein bild für den infobereich
			Image img = Toolkit.getDefaultToolkit()
					.getImage("ressources/Herzchen.gif");//"C:\\Users\\Marc Janßen\\Workspace\\Archiv\\favicon.gif");
			single = new T_Icon(img);
		}
		return single;
	}
	

	private void create_PPMenu() {
		// Popmenu
		ppmenu = new T_Icon_PPMenu();

		T_Icon_Menu[] temps = T_Icon_Menu.values();
		for (int i = 0; i < temps.length; i++) {
			if (temps[i].is_placeholder()) {
				ppmenu.addSeparator();
			} else if (temps[i].has_submenu()) {
				T_Icon_SubMenu m = new T_Icon_SubMenu(temps[i]);
				ppmenu.add(m);
				T_Icon_Menu[] sub = temps[i].get_submenu();
				for (int j = 0; j <sub.length; j++) 
					m.add(new T_Icon_Item(sub[j]));
				
			} else {
				ppmenu.add(new T_Icon_Item(temps[i]));
			}
		}
	}

	public void remove() {
		if (tray != null) {
			SystemTray sysTray = SystemTray.getSystemTray();
			sysTray.remove(tray);
			HBPrinter.getInstance().printMSG(this, "TrayIcon wurde entfernt!");
		}
	}

	public void show() {		
		// Initialisiere SystemTray
		try {
			sysTray.add(tray);

		} catch (AWTException e) {
			HBPrinter.getInstance().printError(this, "Konnte Das Tray-Icon nicht zum laufenden SystemTray hinzufügen!", e);
		}
	}

//	@Override
//	public void mouseDragged(MouseEvent arg0) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void mouseMoved(MouseEvent arg0) {
//	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			ppmenu.update_Labels();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
