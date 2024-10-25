package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser; // Importar JCalendar para la fecha de nacimiento
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class VentanaRegister extends JPanel {

    private JTextField fullNameField;
    private JDateChooser dateChooser;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JTextArea greetingMessageField;
    private JButton showPasswordButton;
    private JButton selectImageButton;
    private JLabel imagePreview;
    private boolean isPasswordVisible = false;
    private VentanaInicio mainFrame; // Referencia a VentanaPrincipal

    public VentanaRegister() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{380};
        setLayout(gridBagLayout);
        setPreferredSize(new Dimension(402, 450));
        setBackground(ElegantPalette.PANEL_BACKGROUND);

        // Título de registro
        JLabel registerTitle = new JLabel("Register", SwingConstants.CENTER);
        registerTitle.setVerticalAlignment(SwingConstants.BOTTOM);
        registerTitle.setForeground(ElegantPalette.PRIMARY_TEXT);
        registerTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(10, 0, 10, 0);
        gbcTitle.anchor = GridBagConstraints.CENTER;
        add(registerTitle, gbcTitle);

        // Campo de nombre completo
        fullNameField = new JTextField("Enter your full name");
        fullNameField.setColumns(40);
        setupTextField(fullNameField);

        GridBagConstraints gbcFullName = new GridBagConstraints();
        gbcFullName.fill = GridBagConstraints.HORIZONTAL;
        gbcFullName.gridx = 0;
        gbcFullName.gridy = 1;
        gbcFullName.insets = new Insets(10, 0, 10, 0);
        add(fullNameField, gbcFullName);

        // Selector de fecha de nacimiento
        dateChooser = new JDateChooser();
        dateChooser.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        dateChooser.setForeground(ElegantPalette.TEXT_FIELD_TEXT);
        dateChooser.setDateFormatString("dd-MM-yyyy");

        GridBagConstraints gbcDateChooser = new GridBagConstraints();
        gbcDateChooser.fill = GridBagConstraints.HORIZONTAL;
        gbcDateChooser.gridx = 0;
        gbcDateChooser.gridy = 2;
        gbcDateChooser.insets = new Insets(10, 0, 10, 0);
        add(dateChooser, gbcDateChooser);

        // Campo de correo electrónico
        emailField = new JTextField("Enter your email");
        emailField.setColumns(40);
        setupTextField(emailField);

        GridBagConstraints gbcEmail = new GridBagConstraints();
        gbcEmail.fill = GridBagConstraints.HORIZONTAL;
        gbcEmail.gridx = 0;
        gbcEmail.gridy = 3;
        gbcEmail.insets = new Insets(10, 0, 10, 0);
        add(emailField, gbcEmail);

        // Campo de número de teléfono
        phoneField = new JTextField("Enter your phone number");
        phoneField.setColumns(40);
        setupTextField(phoneField);

        GridBagConstraints gbcPhone = new GridBagConstraints();
        gbcPhone.fill = GridBagConstraints.HORIZONTAL;
        gbcPhone.gridx = 0;
        gbcPhone.gridy = 4;
        gbcPhone.insets = new Insets(10, 0, 10, 0);
        add(phoneField, gbcPhone);

        // Campo de contraseña
        passwordField = new JPasswordField("Enter your password");
        passwordField.setColumns(40);
        setupPasswordField(passwordField);

        JButton showPasswordButton = new JButton(IconsResource.getIconHide());
        showPasswordButton.setPreferredSize(new Dimension(32, 32));
        showPasswordButton.setBackground(Color.WHITE);
        showPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility(IconsResource.getIconHide(), IconsResource.getIconShow());
            }
        });

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 5;
        gbcPassword.insets = new Insets(10, 0, 10, 0);
        add(passwordPanel, gbcPassword);

        // Botón de selección de imagen de perfil
        selectImageButton = new JButton("Select Profile Image");
        selectImageButton.setBackground(ElegantPalette.ACTION_BUTTON);
        selectImageButton.setForeground(ElegantPalette.BUTTON_TEXT);
        selectImageButton.addActionListener(e -> selectProfileImage());

        GridBagConstraints gbcSelectImage = new GridBagConstraints();
        gbcSelectImage.gridx = 0;
        gbcSelectImage.gridy = 6;
        gbcSelectImage.insets = new Insets(10, 0, 10, 0);
        gbcSelectImage.anchor = GridBagConstraints.CENTER;
        add(selectImageButton, gbcSelectImage);

        // Vista previa de la imagen seleccionada
        imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(100, 100));
        imagePreview.setOpaque(true);
        imagePreview.setBackground(ElegantPalette.PANEL_BACKGROUND);

        GridBagConstraints gbcImagePreview = new GridBagConstraints();
        gbcImagePreview.gridx = 0;
        gbcImagePreview.gridy = 7;
        gbcImagePreview.insets = new Insets(10, 0, 10, 0);
        gbcImagePreview.anchor = GridBagConstraints.CENTER;
        add(imagePreview, gbcImagePreview);

        // Campo de mensaje de saludo
        greetingMessageField = new JTextArea("Enter a greeting message (optional)");
        greetingMessageField.setRows(4);
        greetingMessageField.setLineWrap(true);
        greetingMessageField.setWrapStyleWord(true);
        greetingMessageField.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        greetingMessageField.setForeground(ElegantPalette.TEXT_FIELD_TEXT_PREV);
        greetingMessageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ElegantPalette.BORDER_COLOR),
                new EmptyBorder(5, 5, 5, 5)
        ));
        greetingMessageField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (greetingMessageField.getForeground() == ElegantPalette.TEXT_FIELD_TEXT_PREV) {
                    greetingMessageField.setText("");
                    greetingMessageField.setForeground(ElegantPalette.TEXT_FIELD_TEXT);
                }
            }
        });

        GridBagConstraints gbcGreeting = new GridBagConstraints();
        gbcGreeting.fill = GridBagConstraints.HORIZONTAL;
        gbcGreeting.gridx = 0;
        gbcGreeting.gridy = 8;
        gbcGreeting.insets = new Insets(10, 0, 10, 0);
        add(greetingMessageField, gbcGreeting);

        // Botón de registro
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        registerButton.setBackground(ElegantPalette.ACTION_BUTTON);
        registerButton.setForeground(ElegantPalette.BUTTON_TEXT);

        GridBagConstraints gbcRegisterButton = new GridBagConstraints();
        gbcRegisterButton.gridx = 0;
        gbcRegisterButton.gridy = 9;
        gbcRegisterButton.insets = new Insets(10, 0, 10, 0);
        gbcRegisterButton.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbcRegisterButton);

        // Enlace para volver a Login
        JLabel loginLabel = new JLabel("Back to Login", SwingConstants.CENTER);
        loginLabel.setForeground(ElegantPalette.LINK_TEXT);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.showLoginPanel(); // Cambia a la ventana de login
                }
            }
        });

        GridBagConstraints gbcLoginLabel = new GridBagConstraints();
        gbcLoginLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginLabel.gridx = 0;
        gbcLoginLabel.gridy = 10;
        gbcLoginLabel.insets = new Insets(10, 0, 10, 0);
        add(loginLabel, gbcLoginLabel);
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

    private void selectProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            ImageIcon profileImage = new ImageIcon(selectedFile.getAbsolutePath());
            imagePreview.setIcon(profileImage);
        }
    }

    public void setMainFrame(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
    }
}
