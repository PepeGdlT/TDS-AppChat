package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Usuario;

public class VentanaContactos extends JPanel {

    private VentanaInicio mainFrame;
    private JList<String> contactList;
    private DefaultListModel<String> contactListModel;

    public VentanaContactos(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Contactos");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Crear la lista para mostrar los contactos
        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        JScrollPane scrollPane = new JScrollPane(contactList);
        add(scrollPane, BorderLayout.CENTER);

        cargarContactos();

        // Panel inferior con botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JButton backButton = new JButton("Volver a Inicio");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showMainWindow();
            }
        });
        buttonPanel.add(backButton);
        
        JButton addButton = new JButton("Agregar Contacto");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarContacto();
            }
        });
        buttonPanel.add(addButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void cargarContactos() {
        ControladorAppChat controlador = ControladorAppChat.INSTANCE;
        Usuario usuarioActual = controlador.getUsuarioActual();

        if (usuarioActual == null) {
            System.out.println("No hay un usuario en sesión.");
            return;
        }

        List<ChatIndividual> chats = usuarioActual.getChatsIndividuales();
        
        if (chats == null || chats.isEmpty()) {
            System.out.println("No hay chats individuales para el usuario.");
            return;
        }

        List<String> nombresContactos = chats.stream()
                .map(ChatIndividual::getNombreContacto)
                .collect(Collectors.toList());

        contactListModel.clear();
        for (String nombre : nombresContactos) {
            contactListModel.addElement(nombre);
        }
    }

    private void agregarContacto() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del contacto:");
        String telefono = JOptionPane.showInputDialog(this, "Ingrese el número de teléfono del contacto:");
        
        if (nombre != null && telefono != null && !nombre.isEmpty() && !telefono.isEmpty()) {
            ControladorAppChat controlador = ControladorAppChat.INSTANCE;
            boolean agregado = controlador.agregarContacto(nombre, telefono);
            if (agregado) {
                JOptionPane.showMessageDialog(this, "Contacto agregado correctamente.");
                cargarContactos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el contacto. Verifique la información.");
            }
        }
    }
}
