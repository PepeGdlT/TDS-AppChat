package vista;

import javax.swing.*;

public class VentanaContactos extends JPanel {
	private VentanaInicio mainFrame; // Referencia a VentanaInicio

	public VentanaContactos() {
		// Configurar el título de la ventana
		JLabel title = new JLabel("Contactos");
		add(title);
	}

	public void setMainFrame(VentanaInicio mainFrame) {
		this.mainFrame = mainFrame;
	}
}