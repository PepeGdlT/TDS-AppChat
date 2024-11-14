package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;

public class VentanaPrincipal extends JPanel {

    private VentanaInicio mainFrame; // Referencia a VentanaInicio
    private JList<ContactoVisor> listaContactos; // Lista de contactos
    private DefaultListModel<ContactoVisor> modeloContactos; // Modelo de la lista
    private ControladorAppChat controlador;

    public VentanaPrincipal(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
        this.controlador = ControladorAppChat.INSTANCE;

        setLayout(new BorderLayout(0, 0));

        // Inicializar paneles
        JPanel panelOpciones = createTopPanel();
        JPanel panelListaContactos = createLeftPanel();
        JScrollPane chatScrollPane = createChatPanel();

        // Añadir paneles al panel principal
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
        searchButton.addActionListener(e -> performSearch());
        panel.add(searchButton);

        JButton contactsButton = new JButton("Contactos");
        contactsButton.addActionListener(e -> mainFrame.showContactPanel());
        panel.add(contactsButton);

        JButton premiumButton = new JButton("Premium");
        premiumButton.addActionListener(e -> handlePremiumAction());
        panel.add(premiumButton);

        JLabel lblUser = new JLabel(controlador.getUsuarioActual().getNombreCompleto());
        panel.add(lblUser);

        return panel;
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        modeloContactos = new DefaultListModel<>();

        // Cargar los contactos del usuario actual en el modelo de la lista
        controlador.getChatIndividuals().forEach(chat -> {
            String nombreContacto = chat.getNombre();
            String fotoPerfil = chat.getFoto(); 
            String ultimoMensaje = controlador.getUltimoMensaje(chat) != null
                    ? controlador.getUltimoMensaje(chat).toString() : "Sin mensajes";
            modeloContactos.addElement(new ContactoVisor(nombreContacto, fotoPerfil, ultimoMensaje));
        });

        listaContactos = new JList<>(modeloContactos);
        listaContactos.setCellRenderer(new ContactoListRenderer());

        // Añadir MouseListener para abrir el chat al hacer clic en un contacto
        listaContactos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listaContactos.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        ContactoVisor contactoSeleccionado = modeloContactos.getElementAt(index);
                        abrirChat(contactoSeleccionado);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(listaContactos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createChatPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.LIGHT_GRAY);

        chatPanel.setPreferredSize(new Dimension(400, 700));

        JScrollPane chatScrollPane = new JScrollPane(chatPanel);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        return chatScrollPane;
    }

    private void performSearch() {
        String searchQuery = JOptionPane.showInputDialog(this, "Introduce el nombre o número de teléfono del contacto:");
        if (searchQuery != null && !searchQuery.isEmpty()) {
        	// Verificar que `getChatIndividuals()` devuelva una lista
        	List<ChatIndividual> resultados = controlador.getChatIndividuals().stream()
        	        .filter(chat -> chat.getNombre().equalsIgnoreCase(searchQuery)
        	                || chat.getNumeroTelefono().contains(searchQuery))
        	        .collect(Collectors.toList());

        	// Verificar que `ChatIndividual` tenga `getFoto()`
        	modeloContactos.clear();
        	resultados.forEach(chat -> {
        	    String nombreContacto = chat.getNombre();
        	    String fotoPerfil = chat.getFoto();  // Asegúrate de que este método existe en ChatIndividual
        	    String ultimoMensaje = controlador.getUltimoMensaje(chat) != null
        	            ? controlador.getUltimoMensaje(chat).toString() : "Sin mensajes";
        	    modeloContactos.addElement(new ContactoVisor(nombreContacto, fotoPerfil, ultimoMensaje));
        	});

        }
    }

    private void handlePremiumAction() {
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Deseas activar la cuenta Premium?", "Premium", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            controlador.hacerPremium(true);
            JOptionPane.showMessageDialog(this, "¡Felicidades! Ahora tienes una cuenta Premium.", "Premium Activado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void abrirChat(ContactoVisor contacto) {
        // Lógica para abrir y mostrar el chat con el contacto seleccionado
        System.out.println("Abriendo chat con: " + contacto.getName());
        // Aquí podrías actualizar el chatPanel para mostrar los mensajes del contacto seleccionado
    }
}
