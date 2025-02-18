package vista.Ventana;

import javax.swing.*;
import controlador.ControladorAppChat;
import modelo.filtro.*;
import modelo.Mensaje;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaBusquedaMensajes extends JPanel {
    private JTextField campoTexto, campoTelefono, campoContacto;
    private JList<String> listaResultados;
    private DefaultListModel<String> modeloLista;
    private ControladorAppChat controlador;

    public VentanaBusquedaMensajes(VentanaInicio ventanaInicio, ControladorAppChat controlador) {
        this.controlador = controlador;
        
        setLayout(new BorderLayout());
        
        // Campos de búsqueda
        campoTexto = new JTextField(15);
        campoTelefono = new JTextField(10);
        campoContacto = new JTextField(15);
        JButton btnBuscarMensajes = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        
        // Panel de entrada
        JPanel panelSuperior = new JPanel(new GridLayout(2, 3, 5, 5));
        panelSuperior.add(new JLabel("Texto:"));
        panelSuperior.add(new JLabel("Teléfono:"));
        panelSuperior.add(new JLabel("Contacto:"));
        
        panelSuperior.add(campoTexto);
        panelSuperior.add(campoTelefono);
        panelSuperior.add(campoContacto);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnBuscarMensajes);
        panelBotones.add(btnLimpiar);
        
        // Lista de resultados
        modeloLista = new DefaultListModel<>();
        listaResultados = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaResultados);
        
        // Botón para volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> ventanaInicio.showMainWindow());
        JPanel panelInferior = new JPanel();
        panelInferior.add(btnVolver);
        
        // Eventos
        btnBuscarMensajes.addActionListener(e -> buscarMensajes());
        btnLimpiar.addActionListener(e -> limpiarBusqueda());
        
        // Añadir componentes al panel
        add(panelSuperior, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private void buscarMensajes() {
        String textoBusqueda = campoTexto.getText().trim().toLowerCase();
        String telefonoBusqueda = campoTelefono.getText().trim();
        String contactoBusqueda = campoContacto.getText().trim().toLowerCase();
        modeloLista.clear();

        if (textoBusqueda.isEmpty() && telefonoBusqueda.isEmpty() && contactoBusqueda.isEmpty()) {
            return;
        }

        List<Mensaje> mensajes = obtenerMensajesDesdeControlador();
        FiltroComposite filtroCompuesto = new FiltroComposite();

        if (!textoBusqueda.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroTexto(textoBusqueda));
        }
        if (!telefonoBusqueda.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroNumero(telefonoBusqueda));
        }
        if (!contactoBusqueda.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroNombre(contactoBusqueda));
        }

        List<Mensaje> mensajesFiltrados = filtroCompuesto.filtrar(mensajes);
        for (Mensaje mensaje : mensajesFiltrados) {
            modeloLista.addElement(mensaje.getReceptor().getNombreContacto() + ": " + mensaje.getTexto());
        }
    }

    private void limpiarBusqueda() {
        campoTexto.setText("");
        campoTelefono.setText("");
        campoContacto.setText("");
        modeloLista.clear();
    }

    private List<Mensaje> obtenerMensajesDesdeControlador() {
        if (controlador.getUsuarioActual() == null) {
            return List.of();
        }
        return controlador.getUsuarioActual().getContactos().stream()
                .flatMap(contacto -> controlador.getMensajes(contacto).stream())
                .collect(Collectors.toList());
    }
}
