package de.clzserver.homebox.config;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class PropTable extends JTable{

	private TableProperties properties;
	

	public PropTable(TableProperties properties) {
		this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
		this.properties = properties;
	}
	/**
	 * Returns a cell editor depending on the specific property type associated
	 * with this cell.
	 */
	public TableCellEditor getCellEditor(int row, int column) {

		if (column == 1) {
			JTextField textField = new JTextField(property.getValue());
			return new DefaultCellEditor(textField);
		}
		return null;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		int modelRow = convertRowIndexToModel(row);
		if (isRowSelected(modelRow))
			return c;

		c.setBackground(colorForRow(row));
		return c;
	}

	protected Color colorForRow(int row) {
		return (row % 2 == 0) ? new Color(245, 245, 245) : new Color(220, 220, 220);
	}
	
	
	
}
