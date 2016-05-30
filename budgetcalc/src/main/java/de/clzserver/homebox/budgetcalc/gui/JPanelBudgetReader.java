package de.clzserver.homebox.budgetcalc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import de.clzserver.homebox.budgetcalc.ods.API_Factory;
import de.clzserver.homebox.budgetcalc.ods.IBudget;
import de.clzserver.homebox.budgetcalc.ods.IODS_API;
import de.clzserver.homebox.budgetcalc.ods.MonthEnum;
import de.clzserver.homebox.config.HBPrinter;

public class JPanelBudgetReader extends JPanel implements ActionListener {

	private final String[] head = { "Verwendungszweck", "Betrag", "Datum",
			"Person" };

	private JButton update_btn;
	private JComboBox c2;
	private IODS_API IODS_api;
	private JTable table;
	private DefaultTableModel t_model;
	private IBudget[] mEnu;
	private String[] erg = new String[4];

	public JPanelBudgetReader() {
		super();
		init();
	}

	public void init() {

		this.setLayout(null);

		update_btn = new JButton("aktualisieren");
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
		String month = sdf2.format(now);
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy");
		String year = sdf3.format(now);
		c2.setSelectedIndex(Integer.parseInt(month) - 1);

		this.add(c2);

		API_Factory api = API_Factory.getInstance();
		IODS_api = api.createAPI();
		if (!IODS_api.islocked()) {
			table = new JTable();// erg, head);

			aktualisiere((MonthEnum.get((Integer.parseInt(month) - 1))), year);

			TableColumn col = table.getColumnModel().getColumn(0);
			col.setPreferredWidth(150);
			TableColumn col3 = table.getColumnModel().getColumn(3);
			col3.setPreferredWidth(100);

			JScrollPane scroll = new JScrollPane(table);
			scroll.setBounds(10, 60, 370, 260);
			this.add(scroll);

			table.setAutoCreateRowSorter(true);
		} else {
			HBPrinter.getInstance().printMSG(this.getClass(), "Das Budget-File ist gelockt ");
		}

	}

	private void aktualisiere(MonthEnum month, String year) {
		if (t_model != null)
			for (int index = t_model.getRowCount() - 1; index >= 0; index--)
				t_model.removeRow(index);
		else {
			t_model = new DefaultTableModel();

			for (String header : head)
				t_model.addColumn(header);
		}
		mEnu = IODS_api.getMonth(month, Integer.parseInt(year));

		if (mEnu != null) {
			for (int i = 0; i < mEnu.length; i++) {

				erg[0] = mEnu[i].getVerwendung();
				erg[1] = mEnu[i].getBetragString();
				erg[2] = mEnu[i].getDatumString();
				erg[3] = mEnu[i].getUser();

				t_model.addRow(erg);

			}

			table.setModel(t_model);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == update_btn) {
			if (!IODS_api.islocked()) {

				for (int j = t_model.getRowCount() - 1; j >= 0; j--) {

					t_model.removeRow(j);
				}
				table.repaint();
				this.repaint();
				aktualisiere((MonthEnum) c2.getSelectedItem(), getYear());

				t_model.fireTableDataChanged();

				table.repaint();
				this.repaint();
			}
		}
		calculate();
	}

	private String getYear() {
		// TODO Was Sinnvolles
		return "2014";
	}

	public float[] calculate() {
		UserEnum[] enums = UserEnum.values();
		String name = enums[0].toString();
		float[] abs = new float[enums.length];
		 System.out.println(enums[0]);
		 System.out.println(enums[1]);
		 System.out.println(name);
		for (int i = 0; i <= t_model.getRowCount()-1 ; i++) {
			 String vec =  (String) ((Vector) t_model.getDataVector().elementAt(i))
					.elementAt(1);
			 vec = vec.substring(0, vec.length()-2); 
			vec = vec.replace(",", ".");
			 
			 float y = Float.parseFloat(vec);
			 System.out.println("Bezahlt hat : "+(String)((Vector) t_model.getDataVector().elementAt(i))
					.elementAt(3));
	
			 boolean d = ( name.equals((String)((Vector) t_model.getDataVector().elementAt(i))
					.elementAt(3)));
			 System.out.println("Gleich Marc ?  "+d);
			
			if (name.equals((String)((Vector) t_model.getDataVector().elementAt(i))
					.elementAt(3))) {
				System.out.println("(Marc)"+ y);
				abs[0] = abs[0] + y;
			} else
				abs[1] = abs[1] + y;
			System.out.println("(Janine)"+y);
		}
		System.out.println("Anteil Marc "+abs[0]);
		System.out.println("Anteil Janine" +abs[1]);
		return abs;

	}

}
