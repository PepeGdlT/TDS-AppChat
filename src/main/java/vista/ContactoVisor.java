package vista;

import java.awt.*;
import java.net.URL;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

public class ContactoVisor extends JPanel {

    private static final Border SELECCIONADO = BorderFactory.createLineBorder(ElegantPalette.HOVER_BACKGROUND, 2);
    private static final Border NO_SELECCIONADO = BorderFactory.createEmptyBorder(2, 2, 2, 2);

    private JLabel lblImagen;
    private JLabel lblNombre;
    private JLabel lblUltimoMensaje;
    private JLabel lblCirculo;

    public ContactoVisor(String nombre, String fotoUrl, String ultimoMensaje) {
        setLayout(null); // Usar posicionamiento manual para flexibilidad
        setPreferredSize(new Dimension(300, 70)); // Tamaño del componente

        // Configurar el fondo predeterminado
        setBackground(Color.WHITE); // Color predeterminado para el fondo
        setBorder(NO_SELECCIONADO); // Borde predeterminado

        // Crear componentes
        lblImagen = new JLabel();
        lblNombre = new JLabel();
        lblUltimoMensaje = new JLabel();
        lblCirculo = new JLabel();

        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblUltimoMensaje.setFont(new Font("Arial", Font.PLAIN, 12));

        // Configuración de posiciones
        lblImagen.setBounds(10, 10, 50, 50); // Imagen a la izquierda
        lblNombre.setBounds(70, 10, 200, 20); // Nombre al lado derecho de la imagen
        lblUltimoMensaje.setBounds(70, 35, 200, 20); // Último mensaje debajo del nombre

        // Configuración de la imagen del contacto
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

        // Configuración del texto
        lblNombre.setText(nombre);
        lblUltimoMensaje.setText(ultimoMensaje);

        // Agregar el círculo azul si el nombre contiene solo dígitos
        if (esSoloDigitos(nombre)) {
            lblCirculo.setBounds(5, 5, 15, 15); // Posición en la esquina superior izquierda
            try {
                ImageIcon originalIcon = new ImageIcon(ContactoVisor.class.getResource("/iconos/blue_circle.png"));
                Image scaledImage = originalIcon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH); // Tamaño reducido
                lblCirculo.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                lblCirculo.setText("X"); // Mostrar algo en caso de error
            }
            add(lblCirculo); // Agregar el círculo solo si el nombre es numérico
        }

        // Agregar los componentes al panel
        add(lblImagen);
        add(lblNombre);
        add(lblUltimoMensaje);
    }

    public void setSeleccionado(boolean isSelected) {
        if (isSelected) {
            setBackground(ElegantPalette.BACKGROUND); // Color de fondo para selección
            setBorder(SELECCIONADO); // Borde azul para mostrar selección
        } else {
            setBackground(ElegantPalette.HOVER_BACKGROUND); // Fondo blanco cuando no está seleccionado
            setBorder(NO_SELECCIONADO); // Sin borde cuando no está seleccionado
        }
    }

    // Validar si el nombre contiene solo dígitos
    private boolean esSoloDigitos(String texto) {
        return texto.matches("\\d+"); // Verificar si el nombre contiene solo números
    }

    public String getNombre() {
        return lblNombre.getText();
    }
}
