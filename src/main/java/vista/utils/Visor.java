package vista.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import modelo.Contacto;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public abstract class Visor extends JPanel {

    private static final Border SELECCIONADO = BorderFactory.createLineBorder(Palette.HOVER_BACKGROUND, 2);
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    protected JLabel lblImagen;
    protected JLabel lblNombre;
    protected JLabel lblUltimoMensaje;
    protected JLabel lblCirculo;
    
    


    public Visor(String nombre, String fotoUrl, String ultimoMensaje) {

        setLayout(null);
        setPreferredSize(new Dimension(300, 70));
        setBackground(Color.WHITE);
        setBorder(NO_SELECCIONADO);

        lblImagen = new JLabel();
        lblNombre = new JLabel();
        lblUltimoMensaje = new JLabel();
        lblCirculo = new JLabel();

        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblUltimoMensaje.setFont(new Font("Arial", Font.PLAIN, 12));

        lblImagen.setBounds(10, 10, 50, 50);
        lblNombre.setBounds(70, 10, 200, 20);
        lblUltimoMensaje.setBounds(70, 35, 200, 20);

        // Configuración de la imagen
        try {
            URL url = new URL(fotoUrl);
            Image imagenOriginal = ImageIO.read(url);
            int anchoDeseado = 50;
            int altoDeseado = 50;
            Image imagenEscalada = imagenOriginal.getScaledInstance(anchoDeseado, altoDeseado, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(imagenEscalada));
        } catch (IOException e) {
            lblImagen.setText("Imagen no disponible");
        }

        // Configuración de nombre y último mensaje
        setNombreYUltimoMensaje(ultimoMensaje);

        // Agregar los componentes al panel
        add(lblImagen);
        add(lblNombre);
        add(lblUltimoMensaje);
    }

    // Método abstracto para establecer nombre y último mensaje
    protected abstract void setNombreYUltimoMensaje(String ultimoMensaje);



    public void setSeleccionado(boolean isSelected) {
        if (isSelected) {
            setBackground(Palette.BACKGROUND);
            setBorder(SELECCIONADO);
        } else {
            setBackground(Palette.HOVER_BACKGROUND);
            setBorder(NO_SELECCIONADO);
        }
    }
    
	public String getNombreContacto() {
		return lblNombre.getText();
	}
    

}
