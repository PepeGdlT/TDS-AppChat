package vista.Ventana;

import javax.swing.*;
import com.formdev.flatlaf.intellijthemes.*;
import controlador.ControladorAppChat;
import modelo.ChatIndividual;

import java.awt.*;

public class VentanaInicio {

    protected JFrame frame;
    private JPanel mainPanel;

    public VentanaInicio() {
        initialize();
    }
    private void initialize() {
        try {
            UIManager.setLookAndFeel(new FlatXcodeDarkIJTheme());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame();
        frame.setTitle("AppChat");
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
        VentanaLogin loginPanel = new VentanaLogin(this);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showRegisterPanel() {
        mainPanel.removeAll();
        VentanaRegister registerPanel = new VentanaRegister(this);
        mainPanel.add(registerPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showMainWindow() {
        mainPanel.removeAll();
        VentanaPrincipal mainWindow = new VentanaPrincipal(this);
        mainPanel.add(mainWindow, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void showContactPanel() {
        mainPanel.removeAll();
        VentanaContactos contactPanel = new VentanaContactos(this);
        mainPanel.add(contactPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
	public void showGroupPanel() {
        mainPanel.removeAll();
        VentanaGrupos gruposPanel = new VentanaGrupos(this);
        mainPanel.add(gruposPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
		
	}
	public void showBusquedaMensajesPanel() {
	    mainPanel.removeAll();
	    VentanaBusquedaMensajes busquedaMensajesPanel = new VentanaBusquedaMensajes(this);
	    mainPanel.add(busquedaMensajesPanel, BorderLayout.CENTER);
	    mainPanel.revalidate();
	    mainPanel.repaint();
	}
	public void showEditarPerfil() {
	    mainPanel.removeAll();
	    VentanaEditarPerfil editarPerfil = new VentanaEditarPerfil(this);
	    mainPanel.add(editarPerfil, BorderLayout.CENTER);
	    mainPanel.revalidate();
	    mainPanel.repaint();
		
	}

}
