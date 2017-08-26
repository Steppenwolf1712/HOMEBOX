package de.clzserver.homebox.budgetcalc.gui;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import de.clzserver.homebox.budgetcalc.interfaces.API_Factory;
import de.clzserver.homebox.budgetcalc.interfaces.Budget_API;

public class Gui extends JFrame {

	private JTabbedPane tabbedPane;
	private Budget_API Budget_api;

	private Component panel1;
	private Component panel2;

	public Gui() {
	

		super("Abrechnung");	
		setSize(440, 400);
		setLocation(200, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		API_Factory api = API_Factory.getInstance();
		Budget_api = api.create_ODS_API();
		if (!Budget_api.islocked()) {

			panel1 = new JPanelBudgetWriter();
			panel1.setBounds(0, 0, 300, 300);

			panel2 = new JPanelBudgetReader();
			panel2.setBounds(0, 0, 300, 300);

			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Eintrag hinzuf�gen", panel1);
			tabbedPane.addTab("Eintr�ge ausgeben", panel2);
			tabbedPane.setBounds(10, 10, 400, 350);
			tabbedPane.setFocusable(false);

			this.setLayout(null);
			this.add(tabbedPane);
			this.setResizable(false);
			setVisible(true);

		} else {
			JOptionPane.showMessageDialog(null, "Die Datei ist gelockt. "+ Budget_api.getCurrentUser(),
					"Fehler", JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
	}

}
