package vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal {

    private JFrame frame;
    private JPanel mainPanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                // Configuración de look and feel de FlatLaf
                UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());

                VentanaPrincipal window = new VentanaPrincipal();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public VentanaPrincipal() {
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
        loginPanel.setMainFrame(this);  // Pasar referencia de VentanaPrincipal a VentanaLogin
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
}
