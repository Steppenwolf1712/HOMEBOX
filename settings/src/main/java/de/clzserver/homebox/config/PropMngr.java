package de.clzserver.homebox.config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PropMngr extends JFrame implements ActionListener{

	private Config cfg = null;
	private JPanel content = null;

	private JButton btn_save = null;
	private JButton btn_cancel = null;

	public PropMngr() {
		super("Eigenschaften");

		cfg = Config.getInstance();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		init();
		setResizable(false);
	}

	private void init() {
		setLayout(new BorderLayout());

		content = new JPanel(true);
		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JScrollPane sc_pane = new JScrollPane(content);

		add(content, BorderLayout.CENTER);

		JPanel p_btn = new JPanel();

		btn_save = new JButton("Einstellungen speichern");
		btn_save.setBounds(15, 320, 180, 30);
		btn_save.addActionListener(this);
		
		p_btn.add(btn_save);

		btn_cancel = new JButton("Abbrechen");
		btn_cancel.setBounds(210, 320, 180, 30);
		btn_cancel.addActionListener(this);
		
		p_btn.add(btn_cancel);

		add(p_btn, BorderLayout.SOUTH);
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
