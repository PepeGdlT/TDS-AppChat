package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controlador.ControladorAppChat;
import modelo.Usuario;

public class VentanaEditarPerfil extends JPanel {

    private JTextField fullNameField, emailField, phoneField, greetingMessageField, profilePictureField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton saveButton, cancelButton, changePasswordButton;
    private JLabel profilePictureLabel;
    private ControladorAppChat controlador;
    private Usuario usuario;
    private VentanaInicio mainFrame;

    public VentanaEditarPerfil(VentanaInicio mainFrame, ControladorAppChat controlador) {
        this.mainFrame = mainFrame;
        this.controlador = controlador;
        this.usuario = controlador.getUsuarioActual();
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre Completo
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nombre Completo:"), gbc);

        gbc.gridx = 1;
        fullNameField = new JTextField(usuario.getNombreCompleto(), 20);
        add(fullNameField, gbc);

        // Correo Electrónico (No editable)
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Correo Electrónico:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(usuario.getEmail());
        emailField.setEditable(false);
        add(emailField, gbc);

        // Teléfono (No editable)
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Número de Teléfono:"), gbc);

        gbc.gridx = 1;
        phoneField = new JTextField(usuario.getNumeroTelefono());
        phoneField.setEditable(false);
        add(phoneField, gbc);

        // Saludo
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Mensaje de Saludo:"), gbc);

        gbc.gridx = 1;
        greetingMessageField = new JTextField(usuario.getSaludo(), 20);
        add(greetingMessageField, gbc);

        // Foto de Perfil (URL)
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Foto de Perfil (URL):"), gbc);

        gbc.gridx = 1;
        profilePictureField = new JTextField(usuario.getFotoPerfil(), 20);
        profilePictureField.addActionListener(e -> updateProfilePicture());
        add(profilePictureField, gbc);

        // Vista previa de imagen
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        profilePictureLabel = new JLabel();
        profilePictureLabel.setHorizontalAlignment(JLabel.CENTER);
        updateProfilePicture();
        add(profilePictureLabel, gbc);

        // Botón de cambiar contraseña
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        changePasswordButton = new JButton("Cambiar Contraseña");
        changePasswordButton.addActionListener(e -> openChangePasswordDialog());
        add(changePasswordButton, gbc);

        // Botones de Guardar y Cancelar
        gbc.gridy = 7;
        gbc.gridwidth = 1;

        saveButton = new JButton("Guardar");
        saveButton.addActionListener(e -> saveChanges());
        add(saveButton, gbc);

        gbc.gridx = 1;
        cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> mainFrame.showMainWindow());
        add(cancelButton, gbc);
    }

    private void updateProfilePicture() {
        String url = profilePictureField.getText().trim();
        if (!url.isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(new ImageIcon(new java.net.URL(url))
                        .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
                profilePictureLabel.setIcon(icon);
                profilePictureLabel.setText("");
            } catch (Exception e) {
                profilePictureLabel.setIcon(null);
                profilePictureLabel.setText("URL no válida");
            }
        } else {
            profilePictureLabel.setIcon(null);
            profilePictureLabel.setText("Sin imagen");
        }
    }

    private void openChangePasswordDialog() {
        JDialog passwordDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cambiar Contraseña", true);
        passwordDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos de contraseña
        gbc.gridx = 0;
        gbc.gridy = 0;
        passwordDialog.add(new JLabel("Contraseña Actual:"), gbc);

        gbc.gridx = 1;
        JPasswordField currentPasswordField = new JPasswordField(15);
        passwordDialog.add(currentPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        passwordDialog.add(new JLabel("Nueva Contraseña:"), gbc);

        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(15);
        passwordDialog.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        passwordDialog.add(new JLabel("Confirmar Nueva:"), gbc);

        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(15);
        passwordDialog.add(confirmPasswordField, gbc);

        // Botón de cambiar
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton confirmButton = new JButton("Confirmar Cambio");
        confirmButton.addActionListener(e -> {
            String currentPass = new String(currentPasswordField.getPassword());
            String newPass = new String(newPasswordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            if (!currentPass.equals(usuario.getContrasena())) {
                JOptionPane.showMessageDialog(passwordDialog, "Contraseña actual incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(passwordDialog, "Los campos no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(passwordDialog, "Las nuevas contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            usuario.setContrasena(confirmPass);
            controlador.modificarUsuario(usuario);
            JOptionPane.showMessageDialog(passwordDialog, "Contraseña cambiada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            passwordDialog.dispose();
        });
        passwordDialog.add(confirmButton, gbc);

        passwordDialog.pack();
        passwordDialog.setLocationRelativeTo(this);
        passwordDialog.setVisible(true);
    }

    private void saveChanges() {
        usuario.setNombreCompleto(fullNameField.getText().trim());
        usuario.setSaludo(greetingMessageField.getText().trim());
        usuario.setFotoPerfil(profilePictureField.getText().trim());

        controlador.modificarUsuario(usuario);
        JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.showMainWindow();
    }
}
