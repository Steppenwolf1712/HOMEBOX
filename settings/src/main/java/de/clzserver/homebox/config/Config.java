package de.clzserver.homebox.config;

import de.clzserver.homebox.config.properties.Property;
import de.clzserver.homebox.config.properties.PropertyGroup;
import de.clzserver.homebox.config.properties.StringProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class Config {
	
	private static final String INIT_CONFIG = "build\\resources\\main\\init.xml";

	private static final String HOMEBOX_SETTINGS = "Homebox-Settings";

	public static final String HOMEBOX_PATH_KEY = "HOMEBOX_PATH";
	public static final String SAVE_PATH_KEY = "SAVE_PATH";
	public static final String SAVETYPE_NAME_KEY = "SAVETYPE_NAME";
	public static final String SAVETEXT_PATH_KEY = "SAVETEXT_PATH";
	public static final String SAVEIMG_PATH_KEY = "SAVEIMG_PATH";
	public static final String SAVEAPP_PATH_KEY = "SAVEAPP_PATH";
	public static final String SAVE_NAME_KEY = "SAVE_NAME";
	public static final String CALCODS_PATH_KEY = "CALCODS_PATH";
	public static final String CALCODS_NAME_KEY = "CALCODS_NAME";
	public static final String CALCODS_LOCK_NAME_KEY = "CALCODS_LOCK_NAME";
	public static final String USER_STRING_KEY = "USER_STRING";
		
	private ArrayList<PropertyGroup> props;
	private static Config single = null;
	
	private Config() {
		props = new ArrayList<PropertyGroup>();
		init();
	}

	Collection<PropertyGroup> getProps() {
		return props;
	}
	
	private void init() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document dom = db.parse(INIT_CONFIG);

			Element elem = dom.getDocumentElement();

			NodeList nl = elem.getChildNodes();
			for (int i = 0; i<= nl.getLength(); i++) {
				Node temp = nl.item(i);
				if (temp != null && temp.getNodeType() != Node.TEXT_NODE)
					props.add(Property.parsePropertyGroup(nl.item(i)));
			}
		} catch (ParserConfigurationException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Konnte keinen DocumentBuilder erstellen!",
					e);
		} catch (SAXException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Es gab einen SAX-Parser Fehler beim einlesen der Config datei",
					e);
		} catch (IOException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Es gabe einen Ein-/Ausgabe-Fehler beim lesen der Config Datei",
					e);
		}
	}

	public static Config getInstance() {
		if (single == null)
			single = new Config();
		return single;
	}
	
	public void setProperty(String key, String value) {
		for (PropertyGroup group: props) {
			if (group.contains(key)) {
				group.get(group.indexOf(key)).setValue(value);
				return;
			}
		}
		HBPrinter.getInstance().printMSG(this.getClass(),
				"Neue Property in der Config-CLASS:\nKEY:=" + key + "\nVALUE:=" + value);
		int maxOrder = 0;
		PropertyGroup hs = null;
		for (PropertyGroup group: props)
			if (group.getGroupID().equals(HOMEBOX_SETTINGS))
				hs = group;
		for (Property p: hs)
			if (maxOrder <= p.getOrder())
				maxOrder = p.getOrder()+1;
		hs.add(new StringProperty(key, value, maxOrder, HOMEBOX_SETTINGS));
	}
	
	/**
	 * Bei der GetValue Methode werden die Verweise innerhalb der values aufgelöst. Auf diese Weise ist es zum Beispiel möglich,
	 * in der Init-Datei Pfade wie $Parent_Path$Ordnername\ zu definieren. Zusätzlich sind die Values schon geparst worden und 
	 * die Reihenfolge in der die Eigenschaften in der Init-Datei stehen ist irrelevant.
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		String temp = getPlainValue(key);

		String erg = get_rek_Value(temp);
				
		return erg;
	}

	public String getPlainValue(String key) {
		for (PropertyGroup group: props)
			if (group.contains(key))
				return group.get(group.indexOf(key)).getValue();
		return null;
	}
	
	private String get_rek_Value(String temp) {
		String erg = "";
		
		if (temp.contains("$")) {
			String[] tempi = temp.split(Pattern.quote("$"));
			
			for (int i = 0; i<tempi.length; i++) {
				for (PropertyGroup group: props) {
					if (group.contains(tempi[i])) {
						erg += get_rek_Value(getPlainValue(tempi[i]));
//					erg += get_rek_Value(props.get(props.indexOf(tempi[i])).getValue());
					} else {
						erg += tempi[i];
					}
				}
			}
		} else {
			erg += temp;
		}
		
		return erg;
	}

	public void save() {
		File temp = new File(INIT_CONFIG);
		File temp_save = new File(INIT_CONFIG+"temp");
		temp.renameTo(temp_save);

		temp.delete();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.newDocument();

			Element rootElement = doc.createElement(Property.ID_PARENT);
			doc.appendChild(rootElement);

			for (PropertyGroup p: props) {
				rootElement.appendChild(p.saveNode(doc));
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(INIT_CONFIG));

			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Konnte keine Transformation konfigurieren!",
					e);
		} catch (TransformerException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Konnte nicht erfolgreich transformieren!",
					e);
		} catch (ParserConfigurationException e) {
			HBPrinter.getInstance().printError(this.getClass(),
					"Konnte den Parser nciht erfolgreich konfigurieren!",
					e);
		}

	}
}
