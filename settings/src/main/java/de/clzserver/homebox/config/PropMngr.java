package de.clzserver.homebox.config;

import de.clzserver.homebox.config.properties.PropertyGroup;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.*;

public class PropMngr extends JFrame implements ActionListener{

	private Config cfg = null;

	private JButton btn_save = null;
	private JButton btn_cancel = null;

	private JXTaskPaneContainer m_taskContainer;

	public PropMngr() {
		super("Eigenschaften");

		cfg = Config.getInstance();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		init();
		setResizable(false);
	}

	private void init() {
		setLayout(new BorderLayout());

		m_taskContainer = new JXTaskPaneContainer();
		m_taskContainer.setLayout(new VerticalLayout());
		m_taskContainer.setBackground(this.getBackground());

		JScrollPane content = new JScrollPane(m_taskContainer);
		content.setPreferredSize(new Dimension(470, 300));
		content.setBorder(null);
		content.setMaximumSize(new Dimension(470, 310));
//		content.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		Collection<PropertyGroup> groups = cfg.getProps();

		JScrollPane sc_pane = null;
		PropTable table = null;
		JXTaskPane pane = null;
		for (PropertyGroup group: groups) {
			table = new PropTable(group);

			sc_pane = new JScrollPane(table);
			sc_pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			sc_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

			pane = new JXTaskPane();
			pane.setTitle(group.getGroupID());
			table.setSize(pane.getSize());
			sc_pane.setPreferredSize(new Dimension(450, (int)(table.getRowHeight()*(table.getRowCount()+1.44))));
			pane.add(sc_pane);
			m_taskContainer.add(pane);
		}

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
		cfg.save();;
	}
}
