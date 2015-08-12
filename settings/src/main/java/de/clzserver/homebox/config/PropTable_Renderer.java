package de.clzserver.homebox.config;

import de.clzserver.homebox.config.properties.BooleanProperty;
import de.clzserver.homebox.config.properties.EnumerationProperty;
import de.clzserver.homebox.config.properties.StringProperty;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.BadLocationException;
import java.awt.*;

/**
 * Created by Marc Janßen on 12.08.2015.
 */
public class PropTable_Renderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable parent, Object value, boolean isSelected, boolean isFocused,
                                                   int row, int column) {
        Component temp = super.getTableCellRendererComponent(parent,value,isSelected,isFocused,row,column);

        if (column == 1) {
            if (value instanceof StringProperty) {
                StringProperty prop = (StringProperty)value;
                JTextField tf = new JTextField(prop.getValue());
                tf.setBorder(null);
                tf.setBackground(temp.getBackground());
                tf.getDocument().addDocumentListener(new StringPropertyChanger(prop));
                temp = tf;
            } else if (value instanceof BooleanProperty) {
                // TODO: noch kein Plan ob ich BooleanProps überhaupt brauche
            } else if (value instanceof EnumerationProperty) {
                // TODO: Hab ehrlich gesagt überhaupt keine Ahnung wie die Property überhaupt ins Repo gekommen ist,
                // aber wenn die schonmal da ist kann sie ja bleiben ;-)
            }
        }
        return temp;
    }

    public class StringPropertyChanger implements DocumentListener {

        private StringProperty m_content = null;

        public StringPropertyChanger(StringProperty content) {
            m_content = content;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateContent(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateContent(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateContent(e);
        }

        private void updateContent(DocumentEvent e) {
            try {
                String erg = e.getDocument().getText(0, e.getDocument().getLength());
                m_content.setValue(erg);
            } catch (BadLocationException e1) {
                HBPrinter.getInstance().printError(this.getClass(),
                        "Konnte den geänderten String nicht aus dem Textfield lesen",
                        e1);
            }
        }
    }
}
