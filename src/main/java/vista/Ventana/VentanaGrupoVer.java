package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import modelo.Grupo;
import vista.utils.utils;

public class VentanaGrupoVer extends JDialog {
    public VentanaGrupoVer(JFrame owner, Grupo grupo) {
        super(owner, "Perfil del Grupo", true); // Ventana modal

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

        // Foto del grupo
        JLabel lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        utils.cargarImagenDesdeURL(grupo.getFoto(), lblFoto, 100, 100);  // Usamos la clase utils
        lblFoto.setPreferredSize(new Dimension(100, 100));

        // Nombre del grupo
        JLabel lblNombre = new JLabel(grupo.getNombreContacto());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
        lblNombre.setForeground(Color.WHITE);

        // Administrador del grupo
        JLabel lblAdministrador = new JLabel("Administrador: " + grupo.getAdministrador().getNombreCompleto());
        lblAdministrador.setFont(new Font("Arial", Font.PLAIN, 12));
        lblAdministrador.setForeground(new Color(200, 200, 200));
        lblAdministrador.setHorizontalAlignment(SwingConstants.CENTER);

        // Miembros del grupo
        JLabel lblMiembros = new JLabel("Miembros: " + grupo.getMiembros().size());
        lblMiembros.setFont(new Font("Arial", Font.PLAIN, 12));
        lblMiembros.setForeground(new Color(200, 200, 200));
        lblMiembros.setHorizontalAlignment(SwingConstants.CENTER);

        // Añadir componentes al panel central
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblFoto);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblNombre);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblAdministrador);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(lblMiembros);
        panelCentral.add(Box.createVerticalStrut(10));

        // Añadir panel a la ventana
        add(panelCentral, BorderLayout.CENTER);
    }
}
