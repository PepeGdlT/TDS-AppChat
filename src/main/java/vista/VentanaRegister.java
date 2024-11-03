package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.toedter.calendar.JDateChooser;
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
    private JPasswordField confirmPasswordField;
    private JTextArea greetingMessageField;
    private JButton showPasswordButton;
    private JButton selectImageButton;
    private JLabel imagePreview;
    private boolean isPasswordVisible = false;
    private VentanaInicio mainFrame;

    public VentanaRegister() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWeights = new double[]{1.0, 1.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        gridBagLayout.columnWidths = new int[]{190, 190}; // Two columns with equal width
        setLayout(gridBagLayout);
        setPreferredSize(new Dimension(800, 600));
        setBackground(ElegantPalette.PANEL_BACKGROUND);

        // Title
        JLabel registerTitle = new JLabel("Register", SwingConstants.CENTER);
        registerTitle.setVerticalAlignment(SwingConstants.BOTTOM);
        registerTitle.setForeground(ElegantPalette.PRIMARY_TEXT);
        registerTitle.setFont(new Font("Arial", Font.BOLD, 20));
        
        GridBagConstraints gbcTitle = new GridBagConstraints();
        gbcTitle.gridwidth = 2; // Span across both columns
        gbcTitle.fill = GridBagConstraints.HORIZONTAL;
        gbcTitle.gridx = 0;
        gbcTitle.gridy = 0;
        gbcTitle.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        gbcTitle.anchor = GridBagConstraints.CENTER;
        add(registerTitle, gbcTitle);

        // Full Name Field
        fullNameField = new JTextField("Enter your full name");
        setupTextField(fullNameField);

        GridBagConstraints gbcFullName = new GridBagConstraints();
        gbcFullName.fill = GridBagConstraints.HORIZONTAL;
        gbcFullName.gridx = 0;
        gbcFullName.gridy = 1;
        gbcFullName.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        add(fullNameField, gbcFullName);

        // Email Field
        emailField = new JTextField("Enter your email");
        setupTextField(emailField);

        GridBagConstraints gbcEmail = new GridBagConstraints();
        gbcEmail.fill = GridBagConstraints.HORIZONTAL;
        gbcEmail.gridx = 1; // Second column
        gbcEmail.gridy = 1;
        gbcEmail.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        add(emailField, gbcEmail);

        // Phone Number Field
        phoneField = new JTextField("Enter your phone number");
        setupTextField(phoneField);

        GridBagConstraints gbcPhone = new GridBagConstraints();
        gbcPhone.fill = GridBagConstraints.HORIZONTAL;
        gbcPhone.gridx = 0;
        gbcPhone.gridy = 2;
        gbcPhone.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        add(phoneField, gbcPhone);

        // Date of Birth Selector
        dateChooser = new JDateChooser();
        dateChooser.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        dateChooser.setForeground(ElegantPalette.TEXT_FIELD_TEXT);
        dateChooser.setDateFormatString("dd-MM-yyyy");

        GridBagConstraints gbcDateChooser = new GridBagConstraints();
        gbcDateChooser.fill = GridBagConstraints.HORIZONTAL;
        gbcDateChooser.gridx = 1; // Second column
        gbcDateChooser.gridy = 2;
        gbcDateChooser.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        add(dateChooser, gbcDateChooser);

        // Password Field
        passwordField = new JPasswordField("Enter your password");
        setupPasswordField(passwordField);

        // Confirm Password Field
        confirmPasswordField = new JPasswordField("Confirm your password");
        setupPasswordField(confirmPasswordField);

        // Show Password Button
        showPasswordButton = new JButton(IconsResource.getIconHide());
        showPasswordButton.setPreferredSize(new Dimension(32, 32));
        showPasswordButton.setBackground(Color.WHITE);
        showPasswordButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                togglePasswordVisibility(IconsResource.getIconHide(), IconsResource.getIconShow());
            }
        });

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        // Adding Password Panel to layout
        GridBagConstraints gbcPassword = new GridBagConstraints();
        gbcPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcPassword.gridx = 0;
        gbcPassword.gridy = 3;
        gbcPassword.insets = new Insets(10, 20, 0, 20); // Left and right padding added
        add(passwordPanel, gbcPassword);

        // Confirm Password Panel
        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);
        confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);

        // Adding Confirm Password Panel to layout
        GridBagConstraints gbcConfirmPassword = new GridBagConstraints();
        gbcConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
        gbcConfirmPassword.gridx = 1; // Second column
        gbcConfirmPassword.gridy = 3;
        gbcConfirmPassword.insets = new Insets(10, 20, 0, 20); // Left and right padding added
        add(confirmPasswordPanel, gbcConfirmPassword);

        // Profile Image Selection Button
        selectImageButton = new JButton("Select Profile Image");
        selectImageButton.setBackground(ElegantPalette.ACTION_BUTTON);
        selectImageButton.setForeground(ElegantPalette.BUTTON_TEXT);
        selectImageButton.addActionListener(e -> selectProfileImage());

        GridBagConstraints gbcSelectImage = new GridBagConstraints();
        gbcSelectImage.gridwidth = 2; // Span across both columns
        gbcSelectImage.gridy = 4;
        gbcSelectImage.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        gbcSelectImage.anchor = GridBagConstraints.CENTER;
        add(selectImageButton, gbcSelectImage);

        // Image Preview
        imagePreview = new JLabel();
        imagePreview.setPreferredSize(new Dimension(100, 100));
        imagePreview.setOpaque(true);
        imagePreview.setBackground(ElegantPalette.PANEL_BACKGROUND);
        imagePreview.setBorder(BorderFactory.createLineBorder(ElegantPalette.BORDER_COLOR));

        GridBagConstraints gbcImagePreview = new GridBagConstraints();
        gbcImagePreview.gridwidth = 2; // Span across both columns
        gbcImagePreview.gridy = 5;
        gbcImagePreview.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        gbcImagePreview.anchor = GridBagConstraints.CENTER;
        add(imagePreview, gbcImagePreview);

        // Greeting Message Field
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
        gbcGreeting.gridwidth = 2; // Span across both columns
        gbcGreeting.fill = GridBagConstraints.HORIZONTAL;
        gbcGreeting.gridy = 6;
        gbcGreeting.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        add(greetingMessageField, gbcGreeting);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Tahoma", Font.BOLD, 11));
        registerButton.setBackground(ElegantPalette.ACTION_BUTTON);
        registerButton.setForeground(ElegantPalette.BUTTON_TEXT);

        GridBagConstraints gbcRegisterButton = new GridBagConstraints();
        gbcRegisterButton.gridwidth = 2; // Span across both columns
        gbcRegisterButton.gridy = 7;
        gbcRegisterButton.insets = new Insets(10, 20, 10, 20); // Left and right padding added
        gbcRegisterButton.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbcRegisterButton);

        // Back to Login Link
        JLabel loginLabel = new JLabel("Back to Login", SwingConstants.CENTER);
        loginLabel.setForeground(ElegantPalette.LINK_TEXT);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.showLoginPanel();
                }
            }
        });

        GridBagConstraints gbcLoginLabel = new GridBagConstraints();
        gbcLoginLabel.gridwidth = 2; // Span across both columns
        gbcLoginLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcLoginLabel.gridy = 8;
        gbcLoginLabel.insets = new Insets(10, 20, 10, 20); // Left and right padding added
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
            confirmPasswordField.setEchoChar('•');
            showPasswordButton.setIcon(iconHide);
            isPasswordVisible = false;
        } else {
            passwordField.setEchoChar((char) 0);
            confirmPasswordField.setEchoChar((char) 0);
            showPasswordButton.setIcon(iconShow);
            isPasswordVisible = true;
        }
    }

    private void selectProfileImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            // Load the image
            ImageIcon profileImage = new ImageIcon(selectedFile.getAbsolutePath());
            
            // Check if the image was loaded correctly
            if (profileImage.getIconWidth() > 0 && profileImage.getIconHeight() > 0) {
                // Resize image
                Image img = profileImage.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                imagePreview.setIcon(new ImageIcon(img));
            } else {
                JOptionPane.showMessageDialog(this, "Selected file is not a valid image.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public void setMainFrame(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
    }
}
