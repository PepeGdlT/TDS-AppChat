package vista;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.SwingConstants;
import java.awt.Font;

public class ContactoItem extends JPanel {
    
    public ContactoItem(String nombre, String fotorurl, String ultimoMensaje) {

    	
        // Establecer tamaño y color de fondo del panel
        setSize(new Dimension(300, 60));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        fixSize(this, 300, 60);
        this.setBorder(new EmptyBorder(5, 10, 5, 10)); // Margen interno alrededor del panel


        
        // Imagen del contacto
        JLabel lblimagen = new JLabel();
        lblimagen.setIcon(new ImageIcon(IconsResource.class.getResource("/" + fotorurl))); // Cargar imagen
        fixSize(lblimagen, 48, 48);
        this.add(lblimagen);

        // Panel de información (nombre y último mensaje) con GridBagLayout
        JPanel infoPanel = new JPanel();
        fixSize(infoPanel, 200, 50);
        infoPanel.setOpaque(false); // Hacer que el panel sea transparente para mostrar el fondo principal
        
        GridBagLayout gbl_infoPanel = new GridBagLayout();
        infoPanel.setLayout(gbl_infoPanel);

        // Nombre del contacto (alineado a la izquierda y en negrita)
        JLabel nombreLabel = new JLabel(nombre);
        nombreLabel.setFont(nombreLabel.getFont().deriveFont(Font.BOLD, 14f)); // Negrita y tamaño
        nombreLabel.setForeground(ElegantPalette.PRIMARY_TEXT); // Color de texto primario
        
        // Configuración específica para el nombre
        GridBagConstraints gbcNombre = new GridBagConstraints();
        gbcNombre.anchor = GridBagConstraints.WEST;
        gbcNombre.gridx = 0;
        gbcNombre.gridy = 0;
        gbcNombre.weightx = 1.0;
        gbcNombre.insets = new Insets(0, 10, 2, 0); // Espaciado debajo del nombre
        infoPanel.add(nombreLabel, gbcNombre);

        // Último mensaje (alineado a la izquierda y debajo del nombre)
        JLabel ultmensaje = new JLabel(ultimoMensaje);
        ultmensaje.setForeground(ElegantPalette.SECONDARY_TEXT); // Color de texto secundario
        ultmensaje.setFont(ultmensaje.getFont().deriveFont(12f)); // Tamaño de fuente más pequeño
        
        // Configuración específica para el último mensaje
        GridBagConstraints gbcUltmensaje = new GridBagConstraints();
        gbcUltmensaje.anchor = GridBagConstraints.WEST;
        gbcUltmensaje.gridx = 0;
        gbcUltmensaje.gridy = 1; // Colocar en la segunda fila
        gbcUltmensaje.insets = new Insets(0, 10, 0, 0); // Margen izquierdo para alineación
        infoPanel.add(ultmensaje, gbcUltmensaje);

        // Agregar el panel de información al panel principal
        this.add(infoPanel);
    }
    
    // Método para fijar el tamaño de los componentes
    private void fixSize(JComponent c, int x, int y) {
        c.setMinimumSize(new Dimension(x, y));
        c.setMaximumSize(new Dimension(x, y));
        c.setPreferredSize(new Dimension(x, y));
    }
}
