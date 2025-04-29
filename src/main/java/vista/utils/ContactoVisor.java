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
			lblNombre.setText(nombre);  // Mostrar el número de teléfono
			mostrarCirculoAzul();  // Mostrar el círculo azul
		} else {
			// Si no es solo dígitos, mostrar el nombre del contacto
			lblNombre.setText(nombre);  // Mostrar el nombre
		}

		// Establecer el último mensaje
		lblUltimoMensaje.setText(ultimoMensaje);
	}

	// Implementación del método abstracto
	@Override
	protected void setNombreYUltimoMensaje(String ultimoMensaje) {
		lblUltimoMensaje.setText(ultimoMensaje);  // Establecer el último mensaje
	}

	// Método para verificar si el nombre es solo dígitos (número de teléfono)
	private boolean esSoloDigitos(String nombre) {
		return nombre.matches("\\d+");  // Verificar si el nombre contiene solo números (es un número de teléfono)
	}

	// Método para mostrar el círculo azul
	private void mostrarCirculoAzul() {
		lblCirculo.setBounds(5, 5, 15, 15); // Posición en la esquina superior izquierda
		try {
			// Usamos la clase utils para cargar la imagen
			ImageIcon icono = IconsResource.BLUE_CIRCLE;
			Image imgEscalada = icono.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH);
			lblCirculo.setIcon(new ImageIcon(imgEscalada));
		} catch (Exception e) {
			lblCirculo.setText("X"); // Mostrar algo en caso de error
		}
		add(lblCirculo);  // Agregar el círculo solo si el nombre es numérico
	}
	


}

