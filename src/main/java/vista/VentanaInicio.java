package vista;

import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.*;
import java.awt.*;

public class VentanaInicio {

    protected JFrame frame;
    private JPanel mainPanel;

    public VentanaInicio() {
        initialize();
    }

    private void initialize() {
        try {
            // Configuración de look and feel de FlatLaf
            UIManager.setLookAndFeel(new FlatXcodeDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);
        frame.getContentPane().add(mainPanel);

        showLoginPanel();
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
        mainWindow.setMainFrame(this);
        mainPanel.add(mainWindow, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showContactPanel() {
        mainPanel.removeAll();
        VentanaContactos contactPanel = new VentanaContactos();
        contactPanel.setMainFrame(this);
        mainPanel.add(contactPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
