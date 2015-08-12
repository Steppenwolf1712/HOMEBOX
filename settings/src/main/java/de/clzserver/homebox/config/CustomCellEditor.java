package de.clzserver.homebox.config;

import de.clzserver.homebox.config.properties.BooleanProperty;
import de.clzserver.homebox.config.properties.EnumerationProperty;
import de.clzserver.homebox.config.properties.Property;
import de.clzserver.homebox.config.properties.StringProperty;

import java.awt.Component;
import java.awt.Insets;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class CustomCellEditor implements TableCellEditor {
	private Vector<String> _values;
	private Property _property;
	private ButtonGroup _radioGroup;

	protected transient Vector<CellEditorListener> listeners;

	public CustomCellEditor(StringProperty property) {
		_values = new Vector<String>();
		_property = property;
		_values.add(property.getValue());
		
		listeners = new Vector<CellEditorListener>();
	}
	
	public CustomCellEditor(BooleanProperty property) {
		_values = new Vector<String>();
		_values.add("true");
		_values.add("false");
		_property = property;

		listeners = new Vector<CellEditorListener>();
	}

//	public CustomCellEditor(EnumerationProperty property) {
//		_values = property.getLiterals();
//		_property = property;
//		listeners = new Vector<CellEditorListener>();
//	}

	@Override
	public void addCellEditorListener(CellEditorListener cel) {
		listeners.addElement(cel);

	}

	@Override
	public void cancelCellEditing() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getCellEditorValue() {
		return getSelectedValue();
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void removeCellEditorListener(CellEditorListener cel) {
		listeners.removeElement(cel);
	}

	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		_property.setValue(getSelectedValue());

		ChangeEvent ce = new ChangeEvent(this);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			((CellEditorListener) listeners.elementAt(i)).editingStopped(ce);
		}

		return true;
	}

	public String getSelectedValue() {
		for (Enumeration<AbstractButton> buttons = _radioGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		_radioGroup = new ButtonGroup();

		for (int i = 0; i < _values.size(); i++) {
			String v = _values.get(i);
			JRadioButton r1 = new JRadioButton(v);
			_radioGroup.add(r1);
			r1.setBorder(BorderFactory.createEmptyBorder());
			r1.setMargin(new Insets(-1, -1, -1, -1));
			r1.setSelected(v.equalsIgnoreCase(_property.getValue()));
			panel.add(r1);
		}

		return panel;
	}
}