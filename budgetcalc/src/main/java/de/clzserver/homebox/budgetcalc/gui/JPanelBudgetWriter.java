package de.clzserver.homebox.budgetcalc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.clzserver.homebox.budgetcalc.ods.API_Factory;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IODS_API;
import de.clzserver.homebox.budgetcalc.ods.interfaces.MonthEnum;
import de.clzserver.homebox.config.Config;

public class JPanelBudgetWriter extends JPanel implements ActionListener, KeyListener {

	private JTextArea tuse;
	private JTextField tabs;
	private JTextField tdate;
	private JTextField tperson;
	private JLabel use;
	private JLabel abs;
	private JLabel person;
	private JLabel date;
	private JComboBox<UserEnum> c;
	private JButton enter_btn;
	private IODS_API api;
	private JComboBox c_day;
	private JComboBox c_month;
	private Config conf;

	public JPanelBudgetWriter() {
		super();
		init();
	}

	public void init() {
		this.setLayout(null);

		tuse = new JTextArea();
		tuse.setLineWrap(true);
		tuse.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane(tuse);
		scrollpane.setBounds(60, 40, 170, 40);
		tuse.addKeyListener(this);
		tuse.setFocusable(true);
	
		tabs = new JTextField();
		tdate = new JTextField();
		tperson = new JTextField();

		use = new JLabel("Verwendungszweck:");
		abs = new JLabel("Betrag:");
		date = new JLabel("Datum:");
		person = new JLabel("Person:");

		use.setBounds(60, 0, 300, 50);
		abs.setBounds(60, 65, 300, 50);
		date.setBounds(60, 125, 300, 50);
		person.setBounds(60, 185, 300, 50);

		tuse.setBounds(60, 30, 170, 40);
		tabs.setBounds(60, 110, 170, 20);

		c = new JComboBox<UserEnum>();
		UserEnum[] enums = UserEnum.values();
		for (int i = 0; i < enums.length; i++)
			c.addItem(enums[i]);
		c.setBounds(60, 230, 150, 20);
		conf = Config.getInstance();
		String user = conf.getValue(Config.USER_STRING_KEY);
		c.setSelectedItem(UserEnum.get(user));
		
		c_month = new JComboBox();
	    String comboBoxListDay[] = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	    MonthEnum[] menums = MonthEnum.values();
		for (int i = 0; i < menums.length; i++) {
			c_month.addItem(menums[i]);
		}
	    
	   c_day= new JComboBox(comboBoxListDay);

	   
	   c_day.setBounds(60, 170, 50, 20);
	   c_month.setBounds(120, 170, 120, 20);
	
	   
	   Date now = new Date(); 
	   SimpleDateFormat sdf = new SimpleDateFormat("dd");
	   c_day.setSelectedItem(sdf.format(now));
	   SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
	   String s = sdf2.format(now);
	   c_month.setSelectedIndex(Integer.parseInt(s)-1);
	   
	   this.add(c_day);
	   this.add(c_month);
	   

		enter_btn = new JButton("Hinzufügen");
		enter_btn.setBounds(60, 270, 120, 30);
		enter_btn.addActionListener(this);

		this.add(use);
		this.add(abs);
		this.add(date);
		this.add(person);
		this.add(enter_btn);
		this.add(c);
		this.add(scrollpane);
		this.add(tabs);
	}
	
	

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == enter_btn) {
			API_Factory api_fac = API_Factory.getInstance();
			api = api_fac.createAPI();
			if (!api.islocked()) {
				Budget buddi = null;
				
				String verwendung = this.tuse.getText();
				
				float betrag = Float.parseFloat(this.tabs.getText());
				
				Calendar c = Calendar.getInstance();
				String day = (String) this.c_day.getSelectedItem();
				MonthEnum me = (MonthEnum) this.c_month.getSelectedItem();
				c.set(2014, me.getIndex(), Integer.parseInt(day));
				
				UserEnum ce = (UserEnum) this.c.getSelectedItem();
				
				buddi = new Budget(verwendung, betrag, c.getTime().getTime(), ce.getName());
				
				api.writeBudget(buddi);
			} else {
			
			}
		} 
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_TAB){
			System.out.println("tab");
			tuse.setFocusable(false);
		tabs.setFocusable(true);
		}		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	
		
	}
}
