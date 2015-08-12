package de.clzserver.homebox.config;

import de.clzserver.homebox.config.properties.*;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class PropTable extends JTable{

	private PropertyGroup properties;
	

	public PropTable(PropertyGroup properties) {
		this.properties = properties;
		setModel(properties);

		TableCellRenderer renderer =  new PropTable_Renderer();
		setDefaultRenderer(Property.class, renderer);
	}

	
}
