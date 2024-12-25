package vista;

import javax.swing.*;
import java.awt.*;

public class ContactoListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, 
                    Object value, int index, boolean isSelected, 
                    boolean cellHasFocus) {
        if (value instanceof ContactoVisor) {
            ContactoVisor contact = (ContactoVisor) value;
            contact.setSeleccionado(isSelected);
            return contact;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
