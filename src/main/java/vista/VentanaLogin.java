package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controlador.ControladorAppChat;
import java.awt.*;
import java.awt.event.*;

public class VentanaLogin extends JPanel {

    private JTextField phoneField;
    private JPasswordField passwordField;
    private JButton showPasswordButton;
    private boolean isPasswordVisible = false;
    private VentanaInicio mainFrame; 

    public VentanaLogin(VentanaInicio mainFrame) {
    	this.mainFrame = mainFrame;

        setupLayout();
        setupComponents();
    }
    private void setupLayout() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{380};
        setLayout(gridBagLayout);
        setPreferredSize(new Dimension(402, 266));
        setBackground(ElegantPalette.PANEL_BACKGROUND);
    }

    private void setupComponents() {
        addLoginTitle();
        addPhoneField();
        addPasswordField();
        addLoginButton();
        addRegisterButton();
    }

    private void addLoginTitle() {
        JLabel loginTitle = new JLabel("Login", SwingConstants.CENTER);
        loginTitle.setForeground(ElegantPalette.PRIMARY_TEXT);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 20));
        GridBagConstraints gbcTitle = createGbc(0, 0, 10);
        add(loginTitle, gbcTitle);
    }

    private void addPhoneField() {
        phoneField = new JTextField("Telefono", 40);
        setupTextField(phoneField);
        GridBagConstraints gbcPhone = createGbc(0, 1, 10);
        add(phoneField, gbcPhone);
    }

    private void addPasswordField() {
        passwordField = new JPasswordField("Enter your password", 40);
        setupPasswordField(passwordField);

        showPasswordButton = new JButton(IconsResource.EYE_SHOW);
        showPasswordButton.setBorderPainted(false);
        showPasswordButton.setPreferredSize(new Dimension(32, 32));
        showPasswordButton.setBackground(new Color(43, 43, 43));
        showPasswordButton.addActionListener(e -> togglePasswordVisibility(IconsResource.EYE_HIDE, IconsResource.EYE_SHOW));

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        GridBagConstraints gbcPassword = createGbc(0, 2, 10);
        add(passwordPanel, gbcPassword);
    }

    private void addLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> iniciarSesion());
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        loginButton.setBackground(ElegantPalette.ACTION_BUTTON);
        loginButton.setForeground(ElegantPalette.BUTTON_TEXT);

        GridBagConstraints gbcLoginButton = createGbc(0, 3, 10);
        add(loginButton, gbcLoginButton);
    }

    private void addRegisterButton() {
    	JButton registerButton = new JButton("Register");
    	registerButton.addActionListener(e -> handleRegister());
    	registerButton.setFont(new Font("Tahoma", Font.BOLD, 11));
    	registerButton.setForeground(ElegantPalette.LINK_TEXT);
    	registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    	
        GridBagConstraints gbcRegisterLabel = createGbc(0, 4, 10);
        add(registerButton, gbcRegisterLabel);
    }

    private GridBagConstraints createGbc(int x, int y, int padding) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(padding, 0, padding, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        return gbc;
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
                }
            }
        });
    }

    private void iniciarSesion() {
        String phone = phoneField.getText();
        String password = new String(passwordField.getPassword());

        if (phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete ambos campos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean login = ControladorAppChat.INSTANCE.iniciarSesion(phone, password);
            
            if (login && mainFrame != null) {
                System.out.println(ControladorAppChat.INSTANCE.getUsuarioActual().toString());
                mainFrame.showMainWindow();
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de usuario o contraseña no válido", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingrese un número de teléfono válido", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void togglePasswordVisibility(ImageIcon iconShow, ImageIcon iconHide) {
        isPasswordVisible = !isPasswordVisible;
        passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '•');
        showPasswordButton.setIcon(isPasswordVisible ? iconShow : iconHide);
    }
    
    private void handleRegister() {
        mainFrame.showRegisterPanel();
    }
    
}
