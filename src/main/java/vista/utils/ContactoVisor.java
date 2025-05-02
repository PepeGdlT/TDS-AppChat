package vista.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;


import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;


import modelo.Contacto;
//En ContactoVisor.java
public class ContactoVisor extends Visor {


	public ContactoVisor(String nombre, String fotoUrl, String ultimoMensaje) {


		super(nombre, fotoUrl, ultimoMensaje);

		if (esSoloDigitos(nombre)) {
			lblNombre.setText(nombre);  
			mostrarCirculoAzul();  
		} else {
			lblNombre.setText(nombre); 
		}

		// Establecer el último mensaje
		lblUltimoMensaje.setText(ultimoMensaje);
	}

	@Override
	protected void setNombreYUltimoMensaje(String ultimoMensaje) {
		lblUltimoMensaje.setText(ultimoMensaje);  
	}

	// Método para verificar si el nombre es solo dígitos (número de teléfono)
	private boolean esSoloDigitos(String nombre) {
		return nombre.matches("\\d+");  
	}

	// Método para mostrar el círculo azul
	private void mostrarCirculoAzul() {
		lblCirculo.setBounds(5, 5, 15, 15); 
		try {
			ImageIcon icono = IconsResource.BLUE_CIRCLE;
			Image imgEscalada = icono.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
			lblCirculo.setIcon(new ImageIcon(imgEscalada));
		} catch (Exception e) {
			lblCirculo.setText("X"); // Mostrar algo en caso de error
		}
		add(lblCirculo);  
	}
	


}

