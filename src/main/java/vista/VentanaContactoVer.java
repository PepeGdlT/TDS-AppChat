package vista;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import modelo.ChatIndividual;

public class VentanaContactoVer extends JDialog {
    public VentanaContactoVer(JFrame owner, ChatIndividual chat) {
        super(owner, "Perfil del Contacto", true); // Ventana modal

        // Configuración de la ventana
        setLayout(new BorderLayout());
        setSize(300, 300);
        setResizable(false);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(34, 34, 34));

        // Panel principal
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentral.setBackground(new Color(44, 44, 44));

        // Foto del contacto
        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        cargarFoto(chat.getFoto(), lblFoto);
        lblFoto.setPreferredSize(new Dimension(100, 100));

        // Nombre del contacto
        JLabel lblNombre = new JLabel(chat.getNombreContacto());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setForeground(Color.WHITE);

        // Estado del contacto
        JLabel lblSaludo = new JLabel("\"" + chat.getSaludo() + "\"");
        lblSaludo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblSaludo.setForeground(new Color(150, 150, 150));
        lblSaludo.setHorizontalAlignment(SwingConstants.CENTER);

        // Número del contacto
        JLabel lblNumero = new JLabel("Número de teléfono: " + chat.getnumeroTelefono());
        lblNumero.setFont(new Font("Arial", Font.PLAIN, 12));
        lblNumero.setForeground(new Color(200, 200, 200));
        lblNumero.setHorizontalAlignment(SwingConstants.CENTER);

        // Añadir componentes al panel central
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblFoto);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblNombre);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblSaludo);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblNumero);
        panelCentral.add(Box.createVerticalStrut(10));

        // Añadir panel a la ventana
        add(panelCentral, BorderLayout.CENTER);
    }

    private void cargarFoto(String fotoUrl, JLabel lblFoto) {
        try {
            ImageIcon icon = new ImageIcon(new URL(fotoUrl));
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblFoto.setText("Foto no disponible");
            lblFoto.setFont(new Font("Arial", Font.BOLD, 14));
            lblFoto.setForeground(Color.LIGHT_GRAY);
        }
    }
}
