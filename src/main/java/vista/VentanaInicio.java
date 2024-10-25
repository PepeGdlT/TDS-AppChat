package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaInicio {

    private JFrame frame;
    private JPanel mainPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Configuración de look and feel de FlatLaf
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());

                VentanaInicio window = new VentanaInicio();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VentanaInicio() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());  // Layout principal centrado
        mainPanel.setBackground(Color.BLACK);
        frame.getContentPane().add(mainPanel);

        // Añadir VentanaLogin al iniciar
        
        VentanaLogin loginPanel = new VentanaLogin();
        loginPanel.setMainFrame(this);  // Pasar referencia de VentanaInicio a VentanaLogin
        mainPanel.add(loginPanel, BorderLayout.CENTER);
		        		
        frame.setVisible(true);
    }

    public void showLoginPanel() {
        mainPanel.removeAll();
        VentanaLogin loginPanel = new VentanaLogin();
        loginPanel.setMainFrame(this);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showRegisterPanel() {
        mainPanel.removeAll();
        VentanaRegister registerPanel = new VentanaRegister();
        registerPanel.setMainFrame(this);
        mainPanel.add(registerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    
	public void showMainWindow() {
		mainPanel.removeAll();
		VentanaPrincipal mainWindow = new VentanaPrincipal();
		
		
        ContactoItem contacto1 = new ContactoItem("Contacto 1", "avatar.png", "Último mensaje de contacto 1");
        ContactoItem contacto2 = new ContactoItem("Contacto 2", "avatar.png", "Último mensaje de contacto 2");
        ContactoItem contacto3 = new ContactoItem("Contacto 3", "avatar.png", "Último mensaje de contacto 3");
        
        // Agregar los contactos a la lista
        mainWindow.agregarContacto(contacto1);
        mainWindow.agregarContacto(contacto2);
        mainWindow.agregarContacto(contacto3);
		
		mainWindow.setMainFrame(this);
		mainPanel.add(mainWindow, BorderLayout.CENTER);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
    
    
}
