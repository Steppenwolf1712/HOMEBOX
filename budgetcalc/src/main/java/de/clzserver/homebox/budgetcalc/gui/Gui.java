package de.clzserver.homebox.budgetcalc.gui;

import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.clzserver.homebox.budgetcalc.ods.API_Factory;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IBudget;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IODS_API;

public class Gui extends JFrame {

	private JTabbedPane tabbedPane;
	private IODS_API IODS_api;

	private Component panel1;
	private Component panel2;

	public Gui() {
	

		super("Abrechnung");	
		setSize(440, 400);
		setLocation(200, 200);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		API_Factory api = API_Factory.getInstance();
		IODS_api = api.createAPI();
		if (!IODS_api.islocked()) {

			panel1 = new JPanelBudgetWriter();
			panel1.setBounds(0, 0, 300, 300);

			panel2 = new JPanelBudgetReader();
			panel2.setBounds(0, 0, 300, 300);

			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Eintrag hinzufügen", panel1);
			tabbedPane.addTab("Einträge ausgeben", panel2);
			tabbedPane.setBounds(10, 10, 400, 350);
			tabbedPane.setFocusable(false);

			this.setLayout(null);
			this.add(tabbedPane);
			this.setResizable(false);
			setVisible(true);

		} else {
			JOptionPane.showMessageDialog(null, "Die Datei ist gelockt. "+IODS_api.getCurrentUser(),
					"Fehler", JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}
	}

}
