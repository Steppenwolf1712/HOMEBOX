package de.clzserver.homebox.config.properties;

import de.clzserver.homebox.config.HBPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Comparator;

public abstract class Property implements Comparator<Property> {

	public static final String ID_PARENT = "Properties";

	static final String ID_VALUE = "Value";
	static final String ID_KEY = "Name";
	static final String ID_ORDER = "Order";
	static final String ID_GROUPID = "GroupID";

	private String m_value = null;
	private String m_key = null;
	private int m_order;
	private String m_groupID = null;

	ArrayList<Node> init(NodeList childs) {
		ArrayList<Node> erg = new ArrayList<Node>();
		for (int i = 0; i<childs.getLength(); i++) {
			Node child = childs.item(i);
			if (child.getNodeType() == Node.TEXT_NODE)
				continue;

			switch (child.getNodeName()) {
				case Property.ID_GROUPID:
					setGroupID(child.getChildNodes().item(0).getNodeValue());
					break;
				case Property.ID_KEY:
					setKey(child.getChildNodes().item(0).getNodeValue());
					break;
				case Property.ID_ORDER:
					setOrder(Integer.parseInt(child.getChildNodes().item(0).getNodeValue()));
					break;
				case Property.ID_VALUE:
					setValue(child.getChildNodes().item(0).getNodeValue());
					break;
				default:
					erg.add(child);
			}
		}
		return erg;
	}

	public String getKey() {
		return m_key;
	}

	void setKey(String key) {
		m_key = key;
	}

	public void setValue(String selectedValue) {
		m_value = selectedValue;
	}

	public String getValue() {
		return m_value;
	}

	void setOrder(int order) {
		m_order = order;
	}

	public int getOrder() {
		return m_order;
	}

	void setGroupID(String id) {
		m_groupID = id;
	}

	public String getGroupID() {
		return m_groupID;
	}

	public static PropertyGroup parsePropertyGroup(Node xml) {
		String groupID = xml.getAttributes().item(0).getNodeValue();
		PropertyGroup erg = new PropertyGroup(groupID);

		NodeList nodes = xml.getChildNodes();
		for (int i =0; i<nodes.getLength(); i++) {
			if (nodes.item(i).getNodeType() == Node.TEXT_NODE)
				continue;
			erg.add(parseProperty(nodes.item(i), groupID));
		}

		HBPrinter.getInstance().printMSG(Property.class, erg.toString());

		return erg;
	}

	private static Property parseProperty(Node xml, String groupID) {
		Property erg = null;
		switch (xml.getNodeName()) {
			case StringProperty.PROPERTY_ID:
				erg = new StringProperty(xml, groupID);
				break;
			case BooleanProperty.PROPERTY_ID:
				erg = new BooleanProperty(xml, groupID);
				break;
			default:
				HBPrinter.getInstance().printError(Property.class,
						"Konnte die Property nicht einlesen, da der Property-Name: \""+xml.getNodeName()+"\" nicht bekannt ist!",
						new IllegalArgumentException(xml.getNodeName()));
		}
		return erg;
	}

	public int compare(Property o1, Property o2) {
		return Integer.compare(o1.getOrder(), o2.getOrder());
	}

	public abstract String save(int tabs);

	public boolean equals(Object o) {
		if (o instanceof Property) {
			Property p = (Property)o;
			return (p.getKey().equals(this.getKey()))&&(p.getGroupID().equals(this.getGroupID()));
		}
		if (o instanceof String) {
			String s = (String)o;
			return s.equals(this.getKey());
		}
		return false;
	}

	public abstract Node saveNode(Document doc);

	private class Abstract_Property {}
}
