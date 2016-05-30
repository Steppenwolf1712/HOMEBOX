package de.clzserver.homebox.remoteprocess.client.gui.table;

import de.clzserver.homebox.remoteprocess.client.rmi.ProcessServer;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by Marc Janﬂen on 29.05.2016.
 */
public class ProcessModel extends AbstractTableModel{

    private static final String HEADER_LEFT = "Prozess-Name";
    private static final String HEADER_RIGHT = "Prozess-Zustand";

    private ArrayList<ModelRow> rows;

    private ProcessServer server;

    public ProcessModel() {
        init();
    }

    private void init() {
        rows = new ArrayList<ModelRow>();
    }

    public void addModelRow(ModelRow row) {
        if (row == null)
            return;
        rows.add(row);
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0)
            return HEADER_LEFT;
        else if (column == 1)
            return HEADER_RIGHT;
        else
            return null;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ModelRow row = rows.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return row.getProcessName();
            case 1:
                return row.ProcessState();
        }
        return null;
    }

    public ModelRow getModelRow(int i) {
        return rows.get(i);
    }

    public void updateRows() {
        for (ModelRow row: rows)
            row.updateState();
        for (int i = 0; i<rows.size(); i++)
            fireTableCellUpdated(i, 1);
    }
}
