package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import controlador.ControladorAppChat;
import modelo.Grupo;
import vista.utils.utils;

public class VentanaGrupoEdit extends JDialog {
    private final Grupo grupo;

    private JTextField txtNombre;
    private JLabel lblFoto;

    public VentanaGrupoEdit(JFrame owner, Grupo grupo) {
        super(owner, "Editar Grupo", true); 
        this.grupo = grupo;

        // Configuración de la ventana
        setLayout(new BorderLayout());
        setSize(300, 300); // Tamaño compacto
        setResizable(false);
        setLocationRelativeTo(owner); // Centrada sobre la ventana principal
        getContentPane().setBackground(new Color(34, 34, 34)); // Fondo oscuro

        // Panel principal
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentral.setBackground(new Color(44, 44, 44)); // Fondo oscuro más claro

        // Foto del grupo
        lblFoto = new JLabel();
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        utils.cargarImagenDesdeURL(grupo.getFoto(), lblFoto, 100, 100); // Usamos la clase utils
        lblFoto.setPreferredSize(new Dimension(100, 100));

        // Nombre del grupo
        txtNombre = new JTextField(grupo.getNombreContacto());
        txtNombre.setFont(new Font("Arial", Font.BOLD, 14));
        txtNombre.setHorizontalAlignment(SwingConstants.CENTER);
        txtNombre.setMaximumSize(new Dimension(250, 30)); // Tamaño reducido
        txtNombre.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1, true));
        txtNombre.setBackground(new Color(60, 60, 60));
        txtNombre.setForeground(Color.WHITE);

        // Añadir componentes al panel central
        panelCentral.add(Box.createVerticalStrut(10)); // Espaciado superior
        panelCentral.add(lblFoto);
        panelCentral.add(Box.createVerticalStrut(10)); // Espaciado
        panelCentral.add(txtNombre);
        panelCentral.add(Box.createVerticalStrut(10)); // Espaciado inferior

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.setBackground(new Color(34, 34, 34));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.setFocusPainted(false);
        btnGuardar.setBackground(new Color(46, 204, 113)); // Verde oscuro moderno
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));

        btnCancelar.setFocusPainted(false);
        btnCancelar.setBackground(new Color(231, 76, 60)); // Rojo oscuro moderno
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));

        // Acciones de los botones
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose()); // Cerrar la ventana

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // Añadir paneles a la ventana
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarFoto(String fotoUrl) {
        utils.cargarImagenDesdeURL(fotoUrl, lblFoto, 100, 100);  // Usamos la clase utils
    }

    private void guardarCambios() {
        String nuevoNombre = txtNombre.getText().trim();

        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Aquí solo delegamos la acción al controlador
        ControladorAppChat.INSTANCE.modificarGrupo(grupo, nuevoNombre, grupo.getMiembros()); 

        JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose(); // Cerrar la ventana
    }
}
