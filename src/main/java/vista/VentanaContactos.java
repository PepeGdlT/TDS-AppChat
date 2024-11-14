package vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Usuario;

public class VentanaContactos extends JPanel {

    private VentanaInicio mainFrame;
    private JList<String> contactList;

    public VentanaContactos(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new BorderLayout());
        setBackground(ElegantPalette.PANEL_BACKGROUND);

        JLabel title = new JLabel("Contactos");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(ElegantPalette.PRIMARY_TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Crear la lista para mostrar los contactos
        contactList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(contactList);
        add(scrollPane, BorderLayout.CENTER);

        cargarContactos();
    }

    private void cargarContactos() {
        ControladorAppChat controlador = ControladorAppChat.INSTANCE;
        Usuario usuarioActual = controlador.getUsuarioActual();

        if (usuarioActual == null) {
            System.out.println("No hay un usuario en sesión.");
            return;
        }

        List<ChatIndividual> chats = usuarioActual.getChatIndividuales();
        
        if (chats == null || chats.isEmpty()) {
            System.out.println("No hay chats individuales para el usuario.");
            return;
        }

        for (ChatIndividual chat : chats) {
            System.out.println("Chat ID: " + chat.getCodigo());
            System.out.println("Nombre del contacto: " + chat.getContacto().getNombreCompleto());
        }

        List<String> nombresContactos = chats.stream()
                .map(ChatIndividual::getNombre)  // Usa `getNombre` en vez de `getContacto().getNombreCompleto()`
                .collect(Collectors.toList());

        contactList.setListData(nombresContactos.toArray(new String[0]));
    }






}
