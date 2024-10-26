package vista;

// ContactoListRenderer.java

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class ContactoListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, 
                    Object value, int index, boolean isSelected, 
                    boolean cellHasFocus) {
        if (value != null && value instanceof ContactoItem) {
            ContactoItem cont = (ContactoItem) value;
            if (isSelected) {
                cont.setBackground(ElegantPalette.PANEL_BACKGROUND);
            } else {
                cont.setBackground(ElegantPalette.BACKGROUND);
            }
          
            return cont;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
