package vista.utils;

import javax.swing.*;
import java.awt.*;

public class GrupoListRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, 
                    Object value, int index, boolean isSelected, 
                    boolean cellHasFocus) {
        if (value instanceof GrupoVisor) {
            GrupoVisor grupo = (GrupoVisor) value;
            grupo.setSeleccionado(isSelected);
            return grupo;
        } else {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}
