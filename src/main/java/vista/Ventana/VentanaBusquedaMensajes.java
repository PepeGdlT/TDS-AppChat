package vista.Ventana;

import javax.swing.*;
import controlador.ControladorAppChat;
import modelo.filtro.*;
import tds.BubbleText;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaBusquedaMensajes extends JPanel {
    private JTextField campoTexto, campoTelefono, campoContacto;
    private JList<String> listaResultados;
    private DefaultListModel<String> modeloLista;

    public VentanaBusquedaMensajes(VentanaInicio ventanaInicio) {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new GridLayout(3, 2, 5, 5));
        panelBusqueda.add(new JLabel("Texto del mensaje:"));
        panelBusqueda.add(campoTexto = new JTextField());
        panelBusqueda.add(new JLabel("Teléfono del contacto:"));
        panelBusqueda.add(campoTelefono = new JTextField());
        panelBusqueda.add(new JLabel("Nombre del contacto:"));
        panelBusqueda.add(campoContacto = new JTextField());

        // Botones
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnVolver = new JButton("Volver");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnVolver);

        // Lista de resultados
        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaResultados);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        // Organización de componentes
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnBuscar.addActionListener(e -> buscarMensajes());
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        btnVolver.addActionListener(e -> ventanaInicio.showMainWindow());
    }

    private void buscarMensajes() {
        String textoBusqueda = campoTexto.getText().trim().toLowerCase();
        String telefonoBusqueda = campoTelefono.getText().trim();
        String contactoBusqueda = campoContacto.getText().trim().toLowerCase();

        modeloLista.clear();

        if (textoBusqueda.isEmpty() && telefonoBusqueda.isEmpty() && contactoBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Introduce al menos un criterio de búsqueda", 
                    "Búsqueda vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Delegar la búsqueda de mensajes al controlador
        List<Mensaje> mensajesFiltrados = ControladorAppChat.INSTANCE.buscarMensajes(textoBusqueda, telefonoBusqueda, contactoBusqueda);

        if (mensajesFiltrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron mensajes con los criterios especificados", 
                                        "Búsqueda sin resultados", JOptionPane.INFORMATION_MESSAGE);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            mensajesFiltrados.forEach(m -> {
                // Determinar si es emoticono
                boolean esEmoticono = m.getEmoticono() != null && m.getEmoticono() != -1;

                // Determinar el nombre del emisor (siempre es Usuario)
                String nombreEmisor;
                if (m.getEmisor().equals(ControladorAppChat.INSTANCE.getUsuarioActual())) {
                    nombreEmisor = "Tú";
                } else {
                    nombreEmisor = obtenerNombreContacto(m.getEmisor());
                }

                // Determinar el nombre del receptor (siempre es Contacto)
                String nombreReceptor;
                if (esReceptorElUsuarioActual(m.getReceptor())) {
                    nombreReceptor = "Tú";
                } else {
                    nombreReceptor = m.getReceptor().getNombreContacto();
                }

                // Mostrar contenido del mensaje
                String contenido = esEmoticono ? 
                    "[Emoticono #" + m.getEmoticono() + "]" : 
                    m.getTexto();

                String resultado = String.format("%s → %s (%s): %s",
                    nombreEmisor,
                    nombreReceptor,
                    m.getHora().format(formatter),
                    contenido);

                modeloLista.addElement(resultado);
            });
        }
    }

    private boolean esReceptorElUsuarioActual(Contacto receptor) {
        if (receptor instanceof ChatIndividual) {
            return ((ChatIndividual)receptor).getContacto().equals(ControladorAppChat.INSTANCE.getUsuarioActual());
        } else if (receptor instanceof Grupo) {
            return ((Grupo)receptor).getMiembros().stream()
                .anyMatch(chat -> chat.getContacto().equals(ControladorAppChat.INSTANCE.getUsuarioActual()));
        }
        return false;
    }

    private String obtenerNombreContacto(Usuario usuario) {
        for (Contacto contacto : ControladorAppChat.INSTANCE.getUsuarioActual().getContactos()) {
            if (contacto instanceof ChatIndividual) {
                ChatIndividual chat = (ChatIndividual) contacto;
                if (chat.getContacto().equals(usuario)) {
                    return chat.getNombreContacto();
                }
            }
        }
        return usuario.getNombreCompleto();
    }

    private void limpiarBusqueda() {
        campoTexto.setText("");
        campoTelefono.setText("");
        campoContacto.setText("");
        modeloLista.clear();
    }
}
