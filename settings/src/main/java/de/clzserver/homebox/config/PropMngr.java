package de.clzserver.homebox.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;

public class PropMngr extends JFrame implements ActionListener{

	private Config cfg;
	private JPanel content;

	private JButton btn_save;
	private JButton btn_cancel;

	public PropMngr() {
		super("Eigenschaften");

		cfg = Config.getInstance();

		init();
		setVisible(true);
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 420, 390);
		setLayout(null);
		setResizable(false);

		content = new JPanel(true);
		content.setBounds(10, 10, 392, 300);
		content.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

		//JScrollPane sc_pane = new JScrollPane(content);
		

		add(content);
		
		btn_save = new JButton("Einstellungen speichern");
		btn_save.setBounds(15, 320, 180, 30);
		btn_save.addActionListener(this);
		
		add(btn_save);

		btn_cancel = new JButton("Abbrechen");
		btn_cancel.setBounds(210, 320, 180, 30);
		btn_cancel.addActionListener(this);
		
		add(btn_cancel);
		
		setVisible(true);
	}

	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == btn_save) {
			
		} else if (arg0.getSource() == btn_cancel) {
			this.setVisible(false);
			this.save();
			this.dispose();
		}
	}

	private void save() {
		// TODO Auto-generated method stub
		
	}
}
