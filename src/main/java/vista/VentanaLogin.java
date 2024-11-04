package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaLogin extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton showPasswordButton;
    private boolean isPasswordVisible = false;

    private VentanaInicio mainFrame; // Referencia a VentanaPrincipal

    public VentanaLogin() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{380};
        setLayout(gridBagLayout);
        setPreferredSize(new Dimension(402, 266));
        setBackground(ElegantPalette.PANEL_BACKGROUND);

        // Configurar el título de login
        JLabel loginTitle = new JLabel("Login", SwingConstants.CENTER);

        loginTitle.setBackground(Color.RED);
        loginTitle.setVerticalAlignment(SwingConstants.BOTTOM);
        loginTitle.setForeground(ElegantPalette.PRIMARY_TEXT);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(10, 0, 10, 0);
        gbcTitle.anchor = GridBagConstraints.CENTER;
        add(loginTitle, gbcTitle);

        // Configurar el campo de usuario
        usernameField = new JTextField("Telefono");
        usernameField.setColumns(40);
        setupTextField(usernameField);

        GridBagConstraints gbcUsername = new GridBagConstraints();
        gbcUsername.fill = GridBagConstraints.HORIZONTAL;
        gbcUsername.gridx = 0;
        gbcUsername.gridy = 1;
        gbcUsername.insets = new Insets(10, 0, 10, 0);
        add(usernameField, gbcUsername);

        // Configurar el campo de contraseña
        passwordField = new JPasswordField("Enter your password");
        passwordField.setColumns(40);
        setupPasswordField(passwordField);

        // Botón de visibilidad de contraseña

        showPasswordButton = new JButton(IconsResource.EYE_SHOW);
        showPasswordButton.setBorderPainted(false);
        showPasswordButton.setForeground(new Color(255, 255, 255));
        showPasswordButton.setPreferredSize(new Dimension(32, 32));
        showPasswordButton.setBackground(new Color(43, 43, 43));
        showPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility(IconsResource.EYE_HIDE, IconsResource.EYE_SHOW);
            }
        });

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 2;
        gbcPassword.insets = new Insets(10, 0, 10, 0);
        gbcPassword.anchor = GridBagConstraints.CENTER;
        add(passwordPanel, gbcPassword);

        // Configurar el botón de login
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarSesion();
            }
        });
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        loginButton.setBackground(ElegantPalette.ACTION_BUTTON);
        loginButton.setForeground(ElegantPalette.BUTTON_TEXT);

        GridBagConstraints gbcLoginButton = new GridBagConstraints();
        gbcLoginButton.gridx = 0;
        gbcLoginButton.gridy = 3;
        gbcLoginButton.insets = new Insets(10, 0, 10, 0);
        gbcLoginButton.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbcLoginButton);

        // Configurar el label de registro
        JLabel registerLabel = new JLabel("Register", SwingConstants.CENTER);
        registerLabel.setForeground(ElegantPalette.LINK_TEXT);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.showRegisterPanel();  // Cambia a la ventana de registro
                }
            }
        });

        GridBagConstraints gbcRegisterLabel = new GridBagConstraints();
        gbcRegisterLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcRegisterLabel.gridx = 0;
        gbcRegisterLabel.gridy = 4;
        gbcRegisterLabel.insets = new Insets(10, 0, 10, 0);
        gbcRegisterLabel.anchor = GridBagConstraints.CENTER;
        add(registerLabel, gbcRegisterLabel);
    }

    private void setupTextField(JTextField textField) {
        textField.setForeground(ElegantPalette.TEXT_FIELD_TEXT_PREV);
        textField.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ElegantPalette.BORDER_COLOR),
            new EmptyBorder(5, 5, 5, 5)
        ));
        textField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textField.getForeground() == ElegantPalette.TEXT_FIELD_TEXT_PREV) {
                    textField.setText("");
                    textField.setForeground(ElegantPalette.TEXT_FIELD_TEXT);
                    textField.setFont(new Font("Tahoma", Font.BOLD, 11));
                }
            }
        });
    }

    private void setupPasswordField(JPasswordField passwordField) {
        passwordField.setForeground(ElegantPalette.TEXT_FIELD_TEXT_PREV);
        passwordField.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ElegantPalette.BORDER_COLOR),
            new EmptyBorder(5, 5, 5, 5)
        ));
        passwordField.setEchoChar('•');
        passwordField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (passwordField.getForeground() == ElegantPalette.TEXT_FIELD_TEXT_PREV) {
                    passwordField.setText("");
                    passwordField.setForeground(ElegantPalette.TEXT_FIELD_TEXT);
                    passwordField.setFont(new Font("Tahoma", Font.BOLD, 11));
                }
            }
        });
    }

    public void setMainFrame(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
    }

    private void togglePasswordVisibility(ImageIcon iconShow, ImageIcon iconHide) {
        if (isPasswordVisible) {
            passwordField.setEchoChar('•');
            showPasswordButton.setIcon(iconHide);
            isPasswordVisible = false;
        } else {
            passwordField.setEchoChar((char) 0);
            showPasswordButton.setIcon(iconShow);
            isPasswordVisible = true;
        }
    }
    
    private void iniciarSesion() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete ambos campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if ((username.equals("admin") && password.equals("admin")) || 
            (username.equals("Telefono") && password.equals("Enter your password"))) {
            mainFrame.showMainWindow();
        } else {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
