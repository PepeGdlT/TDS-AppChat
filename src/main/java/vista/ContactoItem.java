package vista;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class ContactoItem extends JPanel {
	//TODO: Controlador
	
	public ContactoItem(String nombre, String fotorurl, String ultimoMensaje) {
		setSize(new Dimension(300, 100));
		
		// Configurar layout horizontal para icono + info
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		fixSize(this, 300, 110);
		this.setBackground(Color.WHITE);
		this.setBorder(new TitledBorder(nombre));
		
		// Imagen del contacto (cargar con ruta directa o revisada)
		JLabel lblimagen = new JLabel();
		lblimagen.setIcon(new ImageIcon(IconsResource.class.getResource("/" + fotorurl)));  // Ajusta ruta si es necesario
		fixSize(lblimagen, 48, 48);
		this.add(lblimagen);  // Añadir imagen al panel principal
		
		// Panel info (nombre y último mensaje) con GridBagLayout
		JPanel info = new JPanel();
		fixSize(info, 200, 100);
		info.setOpaque(false);
		
		GridBagLayout gbl_info = new GridBagLayout();
		gbl_info.columnWidths = new int[]{0, 0, 100, 0}; // Ajustar según contenido
		gbl_info.rowHeights = new int[]{14, 14, 0, 0}; // Ajustar según contenido
		gbl_info.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_info.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		info.setLayout(gbl_info);
		
		// Nombre del contacto
		JLabel nombreLabel = new JLabel(nombre);
		GridBagConstraints gbc_nombreLabel = new GridBagConstraints();
		gbc_nombreLabel.anchor = GridBagConstraints.WEST;
		gbc_nombreLabel.insets = new Insets(0, 10, 5, 0);  // Separación entre ícono y texto
		gbc_nombreLabel.gridx = 2;
		gbc_nombreLabel.gridy = 0;
		info.add(nombreLabel, gbc_nombreLabel);
		
		// Añadir panel de información al panel principal
		this.add(info);
		
		// Último mensaje (corregir color para que sea visible)
		JLabel ultmensaje = new JLabel(ultimoMensaje);
		ultmensaje.setVerticalAlignment(SwingConstants.BOTTOM);
		ultmensaje.setForeground(Color.GRAY); // Cambiar color para que sea visible
		ultmensaje.setOpaque(false);  // Fondo transparente
		GridBagConstraints gbc_ultmensaje = new GridBagConstraints();
		gbc_ultmensaje.anchor = GridBagConstraints.EAST;
		gbc_ultmensaje.insets = new Insets(0, 10, 0, 0);  // Separación
		gbc_ultmensaje.gridx = 2;
		gbc_ultmensaje.gridy = 2;
		info.add(ultmensaje, gbc_ultmensaje);
	}
	
	// Método para fijar el tamaño de los componentes
	private void fixSize(JComponent c, int x, int y) {
		c.setMinimumSize(new Dimension(x, y));
		c.setMaximumSize(new Dimension(x, y));
		c.setPreferredSize(new Dimension(x, y));
	}
}
