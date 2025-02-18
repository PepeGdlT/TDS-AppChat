package vista.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class utils {
	
	public static void cargarImagenDesdeURL(String url, JLabel label, int width, int height) {
	    try {
	        URL imageURL = new URL(url);
	        BufferedImage originalImage = ImageIO.read(imageURL);
	        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	        label.setIcon(new ImageIcon(resizedImage));
	    } catch (IOException e) {
	        label.setText("No img"); // Muestra texto si la imagen no se carga
	        System.err.println("Error al cargar la imagen: " + e.getMessage());
	    }
	}

}
