package de.clzserver.homebox.config.properties;

import de.clzserver.homebox.config.HBPrinter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class StringProperty extends Property{

	public static final String PROPERTY_ID = "String";

	public StringProperty(Node xml, String groupID) {
		NodeList childs = xml.getChildNodes();

		ArrayList<Node> rest = init(childs);
		if (rest.size()>=0) {
			for (Node n: rest) {
				HBPrinter.getInstance().printError(this.getClass(),
						"Konnte das Attribut mit dem Namen:\""+n.getNodeName()+"\" nicht identifizieren!",
						new IllegalArgumentException(n.getNodeName()));
			}
		}
		setGroupID(groupID);
	}

	public StringProperty(String key, String value, int order, String groupID) {
		this.setGroupID(groupID);
		this.setKey(key);
		this.setValue(value);
		this.setOrder(order);
	}

	@Override
	public String save(int tabs) {
		String erg = createLine(tabs, "<"+PROPERTY_ID+">");
		erg += createLine(tabs+1, "<"+Property.ID_KEY +">"+getKey()+"</"+Property.ID_KEY +">");
		erg += createLine(tabs+1, "<"+Property.ID_VALUE+">"+getValue()+"</"+Property.ID_VALUE+">");
//		erg += createLine(tabs+1, "<"+Property.ID_GROUPID+">"+getGroupID()+"</"+Property.ID_GROUPID+">");
		erg += createLine(tabs+1, "<"+Property.ID_ORDER+">"+getOrder()+"</"+Property.ID_ORDER+">");
		erg += createLine(tabs, "</"+PROPERTY_ID+">");
		return erg;
	}

	@Override
	public Node saveNode(Document doc) {
		Element erg = doc.createElement(PROPERTY_ID);

		Element key = doc.createElement(Property.ID_KEY);
		key.setNodeValue(getKey());
		erg.appendChild(key);

		Element value = doc.createElement(Property.ID_VALUE);
		value.setNodeValue(getValue());
		erg.appendChild(value);

		Element order = doc.createElement(Property.ID_ORDER);
		order.setNodeValue(""+getOrder());
		erg.appendChild(order);

//		Element groupID = doc.createElement(Property.ID_GROUPID);
//		groupID.setNodeValue(getGroupID());
//		erg.appendChild(groupID);

		return erg;
	}

	private String createLine(int tabs, String tail) {
		String erg = "";
		for (int i = 0; i<tabs; i++)
			erg += "\t";
		erg += tail+"\n";
		return erg;
	}

	public String toString() {
		return save(0);
	}
}
