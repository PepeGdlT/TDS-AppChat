package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class VentanaPrincipal extends JPanel {
	
    private VentanaInicio mainFrame; // Reference to VentanaInicio
    private JList<ContactoVisor> listaContactos; // List of contacts
    private DefaultListModel<ContactoVisor> modeloContactos; // List model

    public VentanaPrincipal() {
        setLayout(new BorderLayout(0, 0));
        
        // Initialize panels
        JPanel panelOpciones = createTopPanel();
        JPanel panelListaContactos = createLeftPanel();
        JScrollPane chatScrollPane = createChatPanel();
        
        // Add panels to the main panel
        add(panelOpciones, BorderLayout.NORTH);
        add(panelListaContactos, BorderLayout.WEST);
        add(chatScrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel lblSearch = new JLabel("Contacto o Teléfono:");
        panel.add(lblSearch);

        JButton searchButton = new JButton("Buscar");
        // Add action listener for search button (functionality to be implemented)
        searchButton.addActionListener(e -> performSearch());
        panel.add(searchButton);

        JButton contactsButton = new JButton("Contactos");
        contactsButton.addActionListener(e -> mainFrame.showContactPanel());
        panel.add(contactsButton);

        JButton premiumButton = new JButton("Premium");
        // Add action listener for premium button (functionality to be implemented)
        premiumButton.addActionListener(e -> handlePremiumAction());
        panel.add(premiumButton);

        JLabel lblUser = new JLabel("Usuario: $usuario_actual");
        panel.add(lblUser);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        modeloContactos = new DefaultListModel<>();
        listaContactos = new JList<>(modeloContactos);
        listaContactos.setCellRenderer(new ContactoListRenderer());

        // Add the list of contacts to a JScrollPane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(listaContactos);
        panel.add(scrollPane, BorderLayout.CENTER);
        
 
        
        return panel;
    }

    private JScrollPane createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(ElegantPalette.BACKGROUND);
        
        // Configure chat panel sizes
        chatPanel.setPreferredSize(new Dimension(400, 700));

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        return chatScrollPane;
    }

    private void performSearch() {
        // Implement search logic here
        System.out.println("Search button clicked!");
    }

    private void handlePremiumAction() {
        // Implement premium feature logic here
        System.out.println("Premium button clicked!");
    }

    public void setMainFrame(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
    }
}
