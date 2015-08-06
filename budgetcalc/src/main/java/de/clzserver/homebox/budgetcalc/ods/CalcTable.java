package de.clzserver.homebox.budgetcalc.ods;

import java.io.File;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.pkg.OdfFileDom;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.clzserver.homebox.budgetcalc.gui.UserEnum;
import de.clzserver.homebox.budgetcalc.ods.interfaces.IBudget;

class CalcTable {

	private static CalcTable single = null;
	private static File table_file = null;
	private OdfDocument table = null;
	private OdfFileDom odfContent = null;
	
	private CalcTable() {
		
	}
	
	public static CalcTable getInstance(String file) {
		if (single == null)
			if (file != null && !file.isEmpty()) {
				try {
					table_file = new File(file);
					single = new CalcTable();
					single.table = OdfDocument.loadDocument(file);
				} catch (Exception e) {
					System.out.println("CalcTable: Konnte die Datei "+file+" nicht laden!");
					e.printStackTrace();
				}
			}
		return single;
	}
	
	public void write_row(String table_name, String verwendung, float betrag, String datum, String user) {
		
		try {
			odfContent = table.getContentDom();
			
			NodeList list = odfContent.getElementsByTagName("table:table");
			Node table_node = null;
			for (int i = 0; i<list.getLength(); i++) {
				NamedNodeMap map = list.item(i).getAttributes();
				Node temp = map.getNamedItem("table:name");
				if (temp.getNodeValue().equals(table_name)) {
					table_node = list.item(i);
					break;
				}
			}
			
			if (table_node == null) {
				table_node = create_Table(table_name);
				System.out.println("CalcTable: Erstelle Tabelle "+table_name);
			}
				
			if (table_node instanceof Element) {
				Element table_ref = (Element)table_node;

				Node new_line = createRow(verwendung, betrag, datum, user);
				
				System.out.println("CalcTable: Füge neue Zeile hinzu");
				table_ref.appendChild(new_line);
				
				odfContent.saveXML(table_ref);

				table.save(table_file);
			} else {
				System.out.println("CalcTable: Die Tabelleninformationen lassen sich nicht weiterverarbeiten");
			}
			
		} catch (Exception e) {
			System.out.println("CalcTable: ODFToolkit konnte keine Dom-Instanz aus der ods erstellen");
			e.printStackTrace();
		}
		
	}
	

	private Element create_Table(String table_name) {
		NodeList list = odfContent.getElementsByTagName("office:spreadsheet");
		Element spreadsheet = (Element)list.item(0);
		
		Element tab = odfContent.createElementNS("table", "table:table");
		tab.setAttributeNS("table", "table:name", table_name);
		tab.setAttributeNS("table", "table:print", "false");
		tab.setAttributeNS("table", "table:style-name", "ta1");

		Element col1 = odfContent.createElementNS("table", "table:table-column");
		col1.setAttributeNS("table", "table:default-cell-style-name", "Default");
		col1.setAttributeNS("table", "table:style-name", "co1");
		tab.appendChild(col1);

		Element col2 = odfContent.createElementNS("table", "table:table-column");
		col2.setAttributeNS("table", "table:default-cell-style-name", "Default");
		col2.setAttributeNS("table", "table:number-columns-repeated", "3");
		col2.setAttributeNS("table", "table:style-name", "co2");
		tab.appendChild(col2);
		
		// KopfZeile hinzufügen
		Element header = odfContent.createElementNS("table", "table:table-row");
		header.setAttributeNS("table", "table:style-name", "ro1");

		Element cell1 = createCell("string", "ce1", "Verwendung");
		header.appendChild(cell1);

		Element cell2 = createCell("string", "ce4", "Betrag");
		header.appendChild(cell2);

		Element cell3 = createCell("string", "ce6", "Datum");
		header.appendChild(cell3);

		Element cell4 = createCell("string", "ce1", "Wer");
		header.appendChild(cell4);
		
		tab.appendChild(header);
		
		Node pointer = odfContent.getElementsByTagName("table:database-ranges").item(0);
		spreadsheet.insertBefore(tab, pointer);
		odfContent.saveXML(tab);
		
		return tab;
	}

	private Node createRow(String verwendung, float betrag, String datum, String user) {
		Element new_line = odfContent.createElementNS("table", "table:table-row");
		Attr attribut = odfContent.createAttributeNS("table", "table:style-name");
		attribut.setValue("ro1");
		new_line.setAttributeNode(attribut);
		
		Budget data = new Budget(verwendung, betrag, datum, user);
		
		Element cell1 = createCell("string", "ce2", data.getVerwendung());
		new_line.appendChild(cell1);
		
		Element cell2 = createCell("currency", "ce5", data.getBetragString());
		cell2.setAttributeNS("table", "table:content-validation-name", "val1");
		cell2.setAttributeNS("office", "office:currency", "EUR");
		cell2.setAttributeNS("office", "office:value", ""+data.getBetrag());
		new_line.appendChild(cell2);
		
		Element cell3 = createCell("date", "ce7", data.getDatumString());
		cell3.setAttributeNS("office", "office:date-value", data.getStandardDatumString());
		new_line.appendChild(cell3);
		
		UserEnum[] enums = UserEnum.values();
		String style = "";
		for (int i = 0; i<enums.length; i++)
			if (enums[i].getName().equals(data.getUser())) {
				style = enums[i].getStyle();
				break;
			}
		Element cell4 = createCell("string", style, data.getUser());
		cell4.setAttributeNS("table", "table:content-validation-name", "val5");
		new_line.appendChild(cell4);
		
		return new_line;
	}

	private Element createCell(String value_type, String style, String text) {
		Element cell = odfContent.createElementNS("table", "table:table-cell");
		cell.setAttributeNS("office", "office:value-type", value_type);
		cell.setAttributeNS("table", "table:style-name", style);
		Element val = odfContent.createElementNS("text", "text:p");
		val.setTextContent(text);
		cell.appendChild(val);
		
		return cell;
	}

	public ArrayList<IBudget> readout_Table(String table_name) {
		ArrayList<IBudget> erg = new ArrayList<IBudget>();
		
		try {
			OdfFileDom odfContent = table.getContentDom();

			XPath xpath = odfContent.getXPath();
			NodeList nodes_Verwendung = (NodeList) xpath.evaluate("//table:table[@table:name='"+table_name+"']/table:table-row/table:table-cell[1]", odfContent, XPathConstants.NODESET);
			NodeList nodes_Betrag = (NodeList) xpath.evaluate("//table:table[@table:name='"+table_name+"']/table:table-row/table:table-cell[2]", odfContent, XPathConstants.NODESET);
			NodeList nodes_Datum = (NodeList) xpath.evaluate("//table:table[@table:name='"+table_name+"']/table:table-row/table:table-cell[3]", odfContent, XPathConstants.NODESET);
			NodeList nodes_User = (NodeList) xpath.evaluate("//table:table[@table:name='"+table_name+"']/table:table-row/table:table-cell[4]", odfContent, XPathConstants.NODESET);

			for (int i = 1; i< nodes_Verwendung.getLength(); i++) {
				Node cell_v = nodes_Verwendung.item(i);
				Node cell_b = nodes_Betrag.item(i);
				Node cell_d = nodes_Datum.item(i);
				Node cell_u = nodes_User.item(i);
				
				//Es sollten entweder alle Zeilen beschrieben sein oder keine, dementsprechend reicht ein Test aus, Schade das ich in der Schleife testen muss :-/
				if (cell_d == null)
					return erg;
				
				if (!cell_b.getTextContent().isEmpty()) {
					Budget temp = new Budget(cell_v.getTextContent(),
							parse_float(cell_b.getTextContent()),
							cell_d.getTextContent(), 
							cell_u.getTextContent());
					erg.add(temp);
				}
			}
			
		} catch (Exception e) {
			System.out.println("CalcTable: Konnte den Inhalt der Datei nicht auslesen!");
			e.printStackTrace();
		}
		
		return erg;
	}
	
	/**
	 * Gibt die Float-Repäsentation des Strings zurück
	 * 
	 * @param temp
	 * @return
	 */
	private float parse_float(String temp) {
		if (temp == null)
			return 0.00f;
		return Float.parseFloat(temp.split(" ")[0].replace(",","."));
	}
}
