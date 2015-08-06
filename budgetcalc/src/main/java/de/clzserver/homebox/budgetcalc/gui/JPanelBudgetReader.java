package de.clzserver.homebox.budgetcalc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.clzserver.homebox.budgetcalc.ods.API_Factory;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IBudget;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IODS_API;
import de.clzserver.homebox.budgetcalc.ods.interfaces.MonthEnum;

public class JPanelBudgetReader extends JPanel implements ActionListener {

	private JButton update_btn;
	private JComboBox c2;
	private IODS_API IODS_api;
	private JTable table;
	private DefaultTableModel t_model;
	private IBudget[] mEnu;
	private String sperson;
	private String sdatum;
	private String sbetrag;
	private String sZweck;
	private String[] erg = new String[4];

	public JPanelBudgetReader() {
		super();
		init();
	}

	public void init() {

		this.setLayout(null);


		update_btn = new JButton("aktualisiere");
		update_btn.setBounds(140, 10, 100, 20);
		update_btn.addActionListener(this);

		this.add(update_btn);

		c2 = new JComboBox();
		MonthEnum[] menums = MonthEnum.values();
		for (int i = 0; i < menums.length; i++) {
			c2.addItem(menums[i]);
		}
		c2.setBounds(10, 10, 100, 20);
		   Date now = new Date(); 
		   SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
		   String s = sdf2.format(now);
		   SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		   String year = sdf3.format(now);
		   c2.setSelectedIndex(Integer.parseInt(s)-1);

		this.add(c2);

		API_Factory api = API_Factory.getInstance();
		IODS_api = api.createAPI();
		if (!IODS_api.islocked()) {
			table = new JTable();// erg, head);
			String[] head = { "Verwendungszweck", "Betrag", "Datum", "Person" };

			t_model = new DefaultTableModel();
			t_model.addColumn("Verwendungszweck");
			t_model.addColumn("Betrag");
			t_model.addColumn("Datum");
			t_model.addColumn("Person");

			mEnu = IODS_api.getMonth((MonthEnum.get((Integer.parseInt(s)-1))), Integer.parseInt(year));

			if (mEnu != null) {
				for (int i = 0; i < mEnu.length; i++) {

					sperson = mEnu[i].getUser();
					sdatum = mEnu[i].getDatumString();
					sbetrag = mEnu[i].getBetragString();
					sZweck = mEnu[i].getVerwendung();

					erg[0] = sZweck;
					erg[1] = sbetrag;
					erg[2] = sdatum;
					erg[3] = sperson;

					t_model.addRow(erg);

				}

				table.setModel(t_model);
				TableColumn col = table.getColumnModel().getColumn(0);
				col.setPreferredWidth(150);
				TableColumn col3 = table.getColumnModel().getColumn(3);
				col3.setPreferredWidth(100);

				JScrollPane scroll = new JScrollPane(table);
				scroll.setBounds(10, 60, 370, 260);
				this.add(scroll);

			}
		} else {
			System.out.println("ist gelockt ");
		}

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == update_btn) {
			if (!IODS_api.islocked()) {
				mEnu = IODS_api
						.getMonth((MonthEnum) c2.getSelectedItem(), 2014);

				for (int j = t_model.getRowCount() - 1; j >= 0; j--) {

					t_model.removeRow(j);
				}
				table.repaint();
				this.repaint();
				if (mEnu != null) {

					for (int r = 0; r < mEnu.length; r++) {
			
						sperson = mEnu[r].getUser();
						sdatum = mEnu[r].getDatumString();
						sbetrag = mEnu[r].getBetragString();
						sZweck = mEnu[r].getVerwendung();

						erg[0] = sZweck;
						erg[1] = sbetrag;
						erg[2] = sdatum;
						erg[3] = sperson;

						t_model.addRow(erg);
						table.setModel(t_model);
						t_model.fireTableDataChanged();
					}

					table.repaint();
					this.repaint();
				}
			}
		}

	}
}
