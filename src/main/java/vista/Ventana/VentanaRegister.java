package vista.Ventana;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;

import controlador.ControladorAppChat;
import vista.utils.Palette;
import vista.utils.IconsResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

public class VentanaRegister extends JPanel {

	private JTextField fullNameField, emailField, phoneField, greetingMessageField;
	private JPasswordField passwordField, confirmPasswordField;
	private JLabel fullNameErrorLabel, emailErrorLabel, phoneErrorLabel, dateErrorLabel, passwordErrorLabel, imagePreview;
	private JButton showPasswordButton, selectImageButton, backToLoginButton, registerButton;
	private JDateChooser dateChooser;
	private String imagenURL;
	private boolean isPasswordVisible = false;
	private VentanaInicio mainFrame; 

	public VentanaRegister(VentanaInicio mainFrame) {
		this.mainFrame = mainFrame;

		// Layout Configuration
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWidths = new int[]{190, 190};
		setLayout(gridBagLayout);
		setPreferredSize(new Dimension(800, 600));
		setBackground(Palette.PANEL_BACKGROUND);

		// Título
		JLabel registerTitle = new JLabel("Register", SwingConstants.CENTER);
		registerTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		registerTitle.setForeground(Palette.PRIMARY_TEXT);
		registerTitle.setFont(new Font("Arial", Font.BOLD, 20));

		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.gridwidth = 2;
		gbcTitle.fill = GridBagConstraints.HORIZONTAL;
		gbcTitle.gridx = 0;
		gbcTitle.gridy = 0;
		gbcTitle.insets = new Insets(10, 20, 10, 20);
		gbcTitle.anchor = GridBagConstraints.CENTER;
		add(registerTitle, gbcTitle);

		// Nombre completo
		fullNameField = new JTextField("Enter your full name");
		setupTextField(fullNameField);
		GridBagConstraints gbcFullName = new GridBagConstraints();
		gbcFullName.fill = GridBagConstraints.HORIZONTAL;
		gbcFullName.gridx = 0;
		gbcFullName.gridy = 1;
		gbcFullName.insets = new Insets(10, 20, 10, 20);
		add(fullNameField, gbcFullName);

		// Error de nombre completo
		fullNameErrorLabel = new JLabel("");
		fullNameErrorLabel.setForeground(Color.RED);
		GridBagConstraints gbcFullNameError = new GridBagConstraints();
		gbcFullNameError.fill = GridBagConstraints.HORIZONTAL;
		gbcFullNameError.gridx = 0;
		gbcFullNameError.gridy = 2;
		gbcFullNameError.insets = new Insets(0, 20, 10, 20);
		add(fullNameErrorLabel, gbcFullNameError);

		// Email
		emailField = new JTextField("Enter your email");
		setupTextField(emailField);
		GridBagConstraints gbcEmail = new GridBagConstraints();
		gbcEmail.fill = GridBagConstraints.HORIZONTAL;
		gbcEmail.gridx = 1;
		gbcEmail.gridy = 1;
		gbcEmail.insets = new Insets(10, 20, 10, 20);
		add(emailField, gbcEmail);

		// Error de email
		emailErrorLabel = new JLabel("");
		emailErrorLabel.setForeground(Color.RED);
		GridBagConstraints gbcEmailError = new GridBagConstraints();
		gbcEmailError.fill = GridBagConstraints.HORIZONTAL;
		gbcEmailError.gridx = 1;
		gbcEmailError.gridy = 2;
		gbcEmailError.insets = new Insets(0, 20, 10, 20);
		add(emailErrorLabel, gbcEmailError);

		// Teléfono
		phoneField = new JTextField("Enter your phone number");
		setupTextField(phoneField);
		GridBagConstraints gbcPhone = new GridBagConstraints();
		gbcPhone.fill = GridBagConstraints.HORIZONTAL;
		gbcPhone.gridx = 0;
		gbcPhone.gridy = 3;
		gbcPhone.insets = new Insets(10, 20, 10, 20);
		add(phoneField, gbcPhone);

		// Error de teléfono
		phoneErrorLabel = new JLabel("");
		phoneErrorLabel.setForeground(Color.RED);
		GridBagConstraints gbcPhoneError = new GridBagConstraints();
		gbcPhoneError.fill = GridBagConstraints.HORIZONTAL;
		gbcPhoneError.gridx = 0;
		gbcPhoneError.gridy = 4;
		gbcPhoneError.insets = new Insets(0, 20, 10, 20);
		add(phoneErrorLabel, gbcPhoneError);

		// Fecha de nacimiento
		dateChooser = new JDateChooser();
		dateChooser.setOpaque(false);
		dateChooser.setBackground(new Color(255, 255, 255));
		dateChooser.setForeground(Palette.TEXT_FIELD_TEXT);
		dateChooser.setDateFormatString("dd-MM-yyyy");
		GridBagConstraints gbcDateChooser = new GridBagConstraints();
		gbcDateChooser.fill = GridBagConstraints.HORIZONTAL;
		gbcDateChooser.gridx = 1;
		gbcDateChooser.gridy = 3;
		gbcDateChooser.insets = new Insets(10, 20, 10, 20);
		add(dateChooser, gbcDateChooser);

		// Error de fecha
		dateErrorLabel = new JLabel("");
		dateErrorLabel.setForeground(Color.RED);
		GridBagConstraints gbcDateError = new GridBagConstraints();
		gbcDateError.fill = GridBagConstraints.HORIZONTAL;
		gbcDateError.gridx = 1;
		gbcDateError.gridy = 4;
		gbcDateError.insets = new Insets(0, 20, 10, 20);
		add(dateErrorLabel, gbcDateError);

		// Contraseña
		passwordField = new JPasswordField("Enter your password");
		setupPasswordField(passwordField);
		// Confirmar contraseña
		confirmPasswordField = new JPasswordField("Confirm your password");
		setupPasswordField(confirmPasswordField);

		// Botón para mostrar/ocultar la contraseña
		showPasswordButton = new JButton(IconsResource.EYE_SHOW);
		showPasswordButton.setBorderPainted(false);
		showPasswordButton.setPreferredSize(new Dimension(32, 32));
		showPasswordButton.setBackground(new Color(43, 43, 43));
		showPasswordButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				togglePasswordVisibility(IconsResource.EYE_HIDE, IconsResource.EYE_SHOW);
			}
		});

		GridBagConstraints gbcShowPassword = new GridBagConstraints();
		gbcShowPassword.gridx = 2;
		gbcShowPassword.gridy = 5;
		gbcShowPassword.insets = new Insets(0, -5, 0, 15);
		add(showPasswordButton, gbcShowPassword);

		GridBagConstraints gbcPassword = new GridBagConstraints();
		gbcPassword.fill = GridBagConstraints.HORIZONTAL;
		gbcPassword.gridx = 0;
		gbcPassword.gridy = 5;
		gbcPassword.insets = new Insets(10, 20, 0, 20);
		add(passwordField, gbcPassword);

		GridBagConstraints gbcConfirmPassword = new GridBagConstraints();
		gbcConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbcConfirmPassword.gridx = 1;
		gbcConfirmPassword.gridy = 5;
		gbcConfirmPassword.insets = new Insets(10, 20, 0, 20);
		add(confirmPasswordField, gbcConfirmPassword);

		// Password Error Label
		passwordErrorLabel = new JLabel("");
		passwordErrorLabel.setForeground(Color.RED);
		GridBagConstraints gbcPasswordError = new GridBagConstraints();
		gbcPasswordError.gridwidth = 2;
		gbcPasswordError.fill = GridBagConstraints.HORIZONTAL;
		gbcPasswordError.gridy = 6;
		gbcPasswordError.insets = new Insets(0, 20, 10, 20);
		add(passwordErrorLabel, gbcPasswordError);

		// Botón para seleccionar imagen
		selectImageButton = new JButton("Select Image");
		selectImageButton.setPreferredSize(new Dimension(150, 40));
		selectImageButton.setBackground(Palette.ACTION_BUTTON);
		selectImageButton.setForeground(Palette.BUTTON_TEXT);
		selectImageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectImage();
			}
		});

		GridBagConstraints gbcSelectImageButton = new GridBagConstraints();
		gbcSelectImageButton.gridwidth = 2;
		gbcSelectImageButton.gridx = 0;
		gbcSelectImageButton.gridy = 6;
		gbcSelectImageButton.insets = new Insets(10, 20, 10, 20);
		add(selectImageButton, gbcSelectImageButton);

		// Vista previa de la imagen
		imagePreview = new JLabel("No image selected", SwingConstants.CENTER);
		imagePreview.setPreferredSize(new Dimension(100, 100));
		imagePreview.setOpaque(true);
		imagePreview.setBackground(Palette.PANEL_BACKGROUND);
		imagePreview.setBorder(BorderFactory.createLineBorder(Palette.BORDER_COLOR));
		GridBagConstraints gbcImagePreview = new GridBagConstraints();
		gbcImagePreview.gridwidth = 2;
		gbcImagePreview.gridx = 0;
		gbcImagePreview.gridy = 7;
		gbcImagePreview.insets = new Insets(10, 20, 10, 20);
		add(imagePreview, gbcImagePreview);

		// Campo de saludo opcional
		greetingMessageField = new JTextField("Write a greeting message (optional)");
		setupTextField(greetingMessageField);
		GridBagConstraints gbcGreetingMessage = new GridBagConstraints();
		gbcGreetingMessage.gridwidth = 2;
		gbcGreetingMessage.fill = GridBagConstraints.HORIZONTAL;
		gbcGreetingMessage.gridx = 0;
		gbcGreetingMessage.gridy = 8;
		gbcGreetingMessage.insets = new Insets(10, 20, 20, 20);
		add(greetingMessageField, gbcGreetingMessage);

		// Back to Login Button
		backToLoginButton = new JButton("Back to Login");
		backToLoginButton.setPreferredSize(new Dimension(150, 40));
		backToLoginButton.setForeground(Palette.LINK_TEXT);
		backToLoginButton.addActionListener(e -> handleBackToLogin());

		GridBagConstraints gbcBackToLoginButton = new GridBagConstraints();
		gbcBackToLoginButton.gridwidth = 2;
		gbcBackToLoginButton.gridx = 0;
		gbcBackToLoginButton.gridy = 10;
		gbcBackToLoginButton.insets = new Insets(10, 20, 10, 20);
		add(backToLoginButton, gbcBackToLoginButton);

		// Register Button
		registerButton = new JButton("Registrar nuevo usuario");
		registerButton.setPreferredSize(new Dimension(150, 40));
		registerButton.setBackground(Palette.ACTION_BUTTON);
		registerButton.setForeground(Palette.BUTTON_TEXT);
		registerButton.addActionListener(e -> handleRegister());

		GridBagConstraints gbcRegisterButton = new GridBagConstraints();
		gbcRegisterButton.gridwidth = 2;
		gbcRegisterButton.gridx = 0;
		gbcRegisterButton.gridy = 9;
		gbcRegisterButton.insets = new Insets(10, 20, 20, 20);
		add(registerButton, gbcRegisterButton);

		// Setup placeholders
		setupTextFieldWithPlaceholder(fullNameField, "Enter your full name");
		setupTextFieldWithPlaceholder(emailField, "Enter your email");
		setupTextFieldWithPlaceholder(phoneField, "Enter your phone number");
		setupTextFieldWithPlaceholder(passwordField, "Enter your password");
		setupTextFieldWithPlaceholder(confirmPasswordField, "Confirm your password");
		setupTextFieldWithPlaceholder(greetingMessageField, "Write a greeting message (optional)");
	}

	private void setupTextField(JTextField textField) {
		textField.setBackground(Palette.TEXT_FIELD_BACKGROUND);
		textField.setForeground(Palette.TEXT_FIELD_TEXT);
		textField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Palette.BORDER_COLOR),
				new EmptyBorder(5, 5, 5, 5)
				));
	}

	private void setupPasswordField(JPasswordField passwordField) {
		passwordField.setBackground(Palette.TEXT_FIELD_BACKGROUND);
		passwordField.setForeground(Palette.TEXT_FIELD_TEXT);
		passwordField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Palette.BORDER_COLOR),
				new EmptyBorder(5, 5, 5, 5)
				));
	}

	private void setupTextFieldWithPlaceholder(JTextField textField, String placeholder) {
		textField.setText(placeholder);
		textField.setForeground(Color.GRAY);

		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Palette.TEXT_FIELD_TEXT);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText(placeholder);
					textField.setForeground(Color.GRAY);
				}
			}
		});
	}

	private void togglePasswordVisibility(ImageIcon iconShow, ImageIcon iconHide) {
		isPasswordVisible = !isPasswordVisible;
		passwordField.setEchoChar(isPasswordVisible ? (char) 0 : '•');
		showPasswordButton.setIcon(isPasswordVisible ? iconShow : iconHide);
		confirmPasswordField.setEchoChar(isPasswordVisible ? (char) 0 : '•');
		showPasswordButton.setIcon(isPasswordVisible ? iconShow : iconHide);
	}

	private void selectImage() {
		imagenURL = JOptionPane.showInputDialog("Inserta la URL de la imagen:");
		if (imagenURL != null && !imagenURL.isEmpty()) { 
			try { 
				URL url = new URL(imagenURL);
				Image originalImage = ImageIO.read(url);
				int maxWidth = 100;
				int maxHeight = 100;
				Image scaledImage = originalImage.getScaledInstance(maxWidth, maxHeight, Image.SCALE_SMOOTH);
				imagePreview.setIcon(new ImageIcon(scaledImage));
				imagePreview.setText("");
			} catch (IOException e) { 
				e.printStackTrace(); 
				JOptionPane.showMessageDialog(this, "Failed to load image from URL.", "Error", JOptionPane.ERROR_MESSAGE); 
				} 
		}
	}

	private void handleBackToLogin() {
		mainFrame.showLoginPanel();
	}

	private void handleRegister() {
		boolean OK = false;
		OK = validateForm();
		if (OK) {
			boolean registrado = false;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String formattedDate = dateFormat.format(dateChooser.getDate());

			registrado = ControladorAppChat.INSTANCE.registrarUsuario(
					fullNameField.getText(),
					phoneField.getText(),
					emailField.getText(),
					new String(passwordField.getPassword()),
					greetingMessageField.getText(),
					imagenURL,
					formattedDate
					);



			if (registrado) {
				JOptionPane.showMessageDialog(VentanaRegister.this, "Usuario registrado correctamente.", "Registro",
						JOptionPane.INFORMATION_MESSAGE);
				mainFrame.showLoginPanel();
			} else {
				JOptionPane.showMessageDialog(VentanaRegister.this, "No se ha podido llevar a cabo el registro.\n",
						"Registro", JOptionPane.ERROR_MESSAGE);

			}
		}



	}



	private boolean validateForm() {
		boolean isValid = true;

		fullNameErrorLabel.setText("");
		emailErrorLabel.setText("");
		phoneErrorLabel.setText("");
		dateErrorLabel.setText("");
		passwordErrorLabel.setText("");

		// Validar nombre completo
		if (fullNameField.getText().trim().isEmpty() || fullNameField.getText().equals("Enter your full name")) {
			fullNameErrorLabel.setText("Full name is required.");
			isValid = false;
		}

		// Validar email
		String emailPattern = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
		if (emailField.getText().trim().isEmpty() || emailField.getText().equals("Enter your email") || !emailField.getText().matches(emailPattern)) {
			emailErrorLabel.setText("Please enter a valid email.");
			isValid = false;
		}

		// Validar número de teléfono
		if (phoneField.getText().trim().isEmpty() || phoneField.getText().equals("Enter your phone number")) {
			phoneErrorLabel.setText("Phone number is required.");
			isValid = false;
		}

		// Validar fecha de nacimiento
		if (dateChooser.getDate() == null || dateChooser.getDate().after(java.sql.Date.valueOf(LocalDate.now()))) {
			dateErrorLabel.setText("Please select a valid birth date.");
			isValid = false;
		}

		// Validar contraseñas
		String password = new String(passwordField.getPassword());
		String confirmPassword = new String(confirmPasswordField.getPassword());

		if (password.trim().isEmpty() || password.equals("Enter your password")) {
			passwordErrorLabel.setText("Password is required.");
			isValid = false;
		} else if (!password.equals(confirmPassword)) {
			passwordErrorLabel.setText("Passwords do not match.");
			isValid = false;
		}

		// Si el formulario es válido, mostrar un mensaje de éxito (o realizar una acción adicional como enviar los datos)
		if (!isValid) {
			JOptionPane.showMessageDialog(this, "Por favor, arregla los errores y vuelva a intentar.", "Registro fallido", JOptionPane.ERROR_MESSAGE);
		}
		return isValid;
	}
}
