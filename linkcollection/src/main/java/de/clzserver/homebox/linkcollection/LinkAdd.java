package de.clzserver.homebox.linkcollection;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import de.clzserver.homebox.config.HBPrinter;

public class LinkAdd {

	private JList links = new JList();
	private JTextField name; 
	
	public LinkAdd(){
		 name = new JTextField();
		JTextField url = new JTextField();
		Object[] message = { "Welchen Titel soll der Link haben?", name, "Url:", url };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE,
				JOptionPane.OK_CANCEL_OPTION);
		pane.createDialog(null, "Link hinzufügen").setVisible(true);

		HBPrinter.getInstance().printMSG(this.getClass(), "Eingabe= " + name.getText() + ", "
				+ url.getText());
		System.exit(0);
		
		
	}

}
