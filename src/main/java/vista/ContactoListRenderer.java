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
                cont.setBackground(Color.PINK);
            } else {
                cont.setBackground(list.getBackground());
            }
            return cont;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
