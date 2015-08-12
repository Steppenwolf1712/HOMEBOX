package de.clzserver.homebox.config.properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PropertyGroup extends AbstractTableModel implements Collection<Property> {

    private final String m_groupID;

    private static final String PROPERTY_ID = "PropertyGroup";

    private static final String[] HEADER = {"Name", "Wert"};
    private static final int HEADER_NAME = 0;
    private static final int HEADER_VALUE = 1;

    private ArrayList<Property> props = null;

    PropertyGroup(String groupID) {
        m_groupID = groupID;
        props = new ArrayList<Property>();
    }

    @Override
    public int size() {
        return props.size();
    }

    @Override
    public boolean isEmpty() {
        return props.isEmpty();
    }

    @Override
    public boolean contains(Object key) {
        for (Property prop: props) {
            if (prop.equals(key))
                return true;
        }
        return false;
    }

    @Override
    public Iterator<Property> iterator() {
        return props.iterator();
    }

    @Override
    public Object[] toArray() {
        return props.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return props.toArray(a);
    }

    public boolean add(Property property) {
        return props.add(property);
    }

    @Override
    public boolean remove(Object o) {
        return props.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return props.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Property> c) {
        return props.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return props.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return props.retainAll(c);
    }

    @Override
    public void clear() {
        props.clear();
    }

    public Property get(int index) {
        return props.get(index);
    }

    public int indexOf(String key) {
        for (Property prop: props) {
            if (prop.equals(key))
                return props.indexOf(prop);
        }
        return -1;
    }

    public String getGroupID() {
        return m_groupID;
    }

    public Node saveNode(Document doc) {
        Element erg = doc.createElement(PROPERTY_ID);
        erg.setAttribute("name", getGroupID());

        for (Property prop: props)
            erg.appendChild(prop.saveNode(doc));

        return erg;
    }

    @Override
    public int getRowCount() {
        return size();
    }

    @Override
    public int getColumnCount() {
        return HEADER.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case HEADER_NAME:
                return props.get(rowIndex).getKey();
            case HEADER_VALUE:
                return props.get(rowIndex);
            default:
                return null;
        }
    }

    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case HEADER_NAME:
                return int.class;
            case HEADER_VALUE:
                return Property.class;
            default:
                return null;
        }
    }

    public String getColumnName(int columnIndex) {
        return HEADER[columnIndex];
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex==HEADER_VALUE)
            return true;
        return false;
    }

    private String writeXML(int tabs) {
        String temp = "";
        for (int i = 0; i<tabs; i++)
            temp += "\n";

        String erg = temp+"<PropertyGroup name=\""+getGroupID()+"\">\n";
        for (Property prop: props)
            erg += prop.save(tabs+1);
        erg += temp+"</PropertyGroup>";
        return erg;
    }

    public String toString() {
        return writeXML(0);
    }
}
