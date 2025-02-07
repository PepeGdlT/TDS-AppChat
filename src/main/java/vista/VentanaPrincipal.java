package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Descuento;
import modelo.DescuentoPorFecha;
import modelo.DescuentoPorMensaje;
import modelo.ExportPDF;
import modelo.FactoriaDescuento;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.BubbleText;

public class VentanaPrincipal extends JPanel {
	private final ControladorAppChat controlador;


	/*
	 * POSIBLE LISTENER PARA CAMBIOS DE CONTACTOS Y QUE SE HAGA UN CARGARCONTACTOS
	 * 
	 * 
	 */


	// Componentes principales
	private DefaultListModel<ContactoVisor> modeloLista;
	private JList<ContactoVisor> listaContactos;
	private JPanel mensajesPanel; // Panel donde se colocarán las burbujas
	private JPanel panelChat;
	private JTextArea areaTexto;
	private JTextField campoBusqueda;
	private VentanaInicio mainFrame;

	// Mapa para almacenar mensajes por contacto


	public VentanaPrincipal(VentanaInicio mainFrame) {
		this.mainFrame = mainFrame;
		controlador = ControladorAppChat.INSTANCE;

		setLayout(new BorderLayout());

		// Crear los paneles principales
		add(createTopPanel(), BorderLayout.NORTH);
		add(createContactListPanel(), BorderLayout.WEST);
		add(createChatPanel(), BorderLayout.CENTER);

		// Cargar contactos iniciales
		//cargarContactosEjemplo();
	}

	// Crear el panel superior
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel lblUsuario = new JLabel(controlador.getUsuarioActual().getNombreCompleto());
		JButton btnPremium = new JButton(esUsuarioPremium() ? "Exportar PDF" : "Hacer Premium");
		btnPremium.addActionListener(e -> {
		    if (esUsuarioPremium()) {
		        ContactoVisor seleccionado = listaContactos.getSelectedValue();
		        if (seleccionado != null) {
		            ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
		            if (chat != null) {
		                ExportPDF exportPDF = new ExportPDF();
		                exportPDF.crearPDF(controlador.getUsuarioActual(), chat);
		            } else {
		                JOptionPane.showMessageDialog(this, "No se ha podido encontrar el chat del contacto.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(this, "Seleccione un contacto para exportar el chat.", "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    } else {
		        activarPremium();
		    }
		});
		campoBusqueda = new JTextField(15);
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> buscarContacto());

		topPanel.add(lblUsuario);
		topPanel.add(btnPremium);
		topPanel.add(campoBusqueda);
		topPanel.add(btnBuscar);

		return topPanel;
	}

	private void cargarContactos() {
		modeloLista.clear(); 

		List<ChatIndividual> contactos = controlador.getChatIndividuals(); // Obtiene todos los contactos del usuario actual
		for (ChatIndividual chat : contactos) {
			ContactoVisor contactoVisor = new ContactoVisor(chat.getNombreContacto(), chat.getFoto(), chat.getUltimoMensaje());
			modeloLista.addElement(contactoVisor); 
		}
	}


	// Crear el panel de lista de contactos
	// Crear el panel de lista de contactos
	private JPanel createContactListPanel() {
	    JPanel contactPanel = new JPanel(new BorderLayout());
	    contactPanel.setPreferredSize(new Dimension(250, getHeight()));

	    modeloLista = new DefaultListModel<>();
	    listaContactos = new JList<>(modeloLista);
	    listaContactos.setCellRenderer(new ContactoListRenderer());
	    listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	    // Evento de doble clic o clic derecho
	    listaContactos.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	                // Doble clic para abrir chat
	                ContactoVisor seleccionado = listaContactos.getSelectedValue();
	                if (seleccionado != null) {
	                    abrirChat(seleccionado);
	                } else {
	                    JOptionPane.showMessageDialog(VentanaPrincipal.this, 
	                        "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            } else if (e.getButton() == MouseEvent.BUTTON3) {
	                // Mostrar menú contextual con botón derecho
	                JPopupMenu menu = new JPopupMenu();

	                JMenuItem itemVisualizar = new JMenuItem("Visualizar perfil contacto");
	                itemVisualizar.addActionListener(e2 -> {
	                    ContactoVisor seleccionado = listaContactos.getSelectedValue();
	                    if (seleccionado != null) {
	                        ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
	                        if (chat != null) {
	                            new VentanaContactoVer(mainFrame.frame, chat).setVisible(true);
	                            cargarContactos();
	                        } else {
	                            JOptionPane.showMessageDialog(VentanaPrincipal.this, 
	                                "No se ha podido encontrar el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
	                        }
	                    } else {
	                        JOptionPane.showMessageDialog(VentanaPrincipal.this, 
	                            "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                });

	                JMenuItem itemEditar = new JMenuItem("Editar contacto");
	                itemEditar.addActionListener(e2 -> {
	                    ContactoVisor seleccionado = listaContactos.getSelectedValue();
	                    if (seleccionado != null) {
	                        ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
	                        if (chat != null) {
	                            new VentanaContactoEdit(mainFrame.frame, chat).setVisible(true);
	                            cargarContactos();
	                        } else {
	                            JOptionPane.showMessageDialog(VentanaPrincipal.this, 
	                                "No se ha podido encontrar el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
	                        }
	                    } else {
	                        JOptionPane.showMessageDialog(VentanaPrincipal.this, 
	                            "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                });

	                menu.add(itemVisualizar);
	                menu.add(itemEditar);
	                menu.show(listaContactos, e.getX(), e.getY());
	            }
	        }
	    });

	    // Configuración de JScrollPane
	    JScrollPane scrollPane = new JScrollPane(listaContactos);
	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	    // Botón para agregar contacto
	    JButton btnAgregarContacto = new JButton("Agregar Contacto");
	    btnAgregarContacto.addActionListener(e -> agregarContacto());

	    // Botón para crear grupo
	    JButton btnCrearGrupo = new JButton("Crear Grupo");
	    btnCrearGrupo.addActionListener(e -> crearGrupo());

	    // Panel para los botones en la parte inferior
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Alineación centrada
	    buttonPanel.add(btnAgregarContacto);
	    buttonPanel.add(btnCrearGrupo);

	    // Agregar componentes al panel principal
	    contactPanel.add(scrollPane, BorderLayout.CENTER); // Lista en el centro
	    contactPanel.add(buttonPanel, BorderLayout.SOUTH); // Botones abajo

	    // Cargar contactos en la lista
	    cargarContactos();

	    return contactPanel;
	}




	// Crear el panel de chat
	private JPanel createChatPanel() {
	    panelChat = new JPanel(new BorderLayout());

	    // Crear un panel superior con el título "Proyecto AppChat" cuando no se ha seleccionado un chat
	    JPanel topPanel = new JPanel();
	    topPanel.setBackground(new Color(37, 211, 102)); // Color verde WhatsApp (opcional)

	    // Título que aparece cuando no se selecciona un chat
	    JLabel tituloChat = new JLabel("Proyecto AppChat");
	    tituloChat.setFont(new Font("Arial", Font.BOLD, 18));
	    tituloChat.setForeground(Color.WHITE);
	    topPanel.add(tituloChat);

	    // Añadimos el panel con el título en la parte superior (este panel solo aparecerá cuando no haya chat seleccionado)
	    panelChat.add(topPanel, BorderLayout.NORTH);

	    // Crear y configurar el panel de mensajes
	    mensajesPanel = new JPanel();
	    mensajesPanel.setLayout(new BoxLayout(mensajesPanel, BoxLayout.Y_AXIS));
	    JScrollPane scrollMensajes = new JScrollPane(mensajesPanel);
	    scrollMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    panelChat.add(scrollMensajes, BorderLayout.CENTER);

	    // Crear un panel contenedor para emojis y área de texto
	    JPanel bottomPanel = new JPanel(new BorderLayout());

	    // Crear y agregar el panel de emojis
	    JPanel emojiPanel = createEmojiPanel();
	    emojiPanel.setVisible(false); // Ocultamos el panel de emojis inicialmente
	    bottomPanel.add(emojiPanel, BorderLayout.NORTH);

	    // Crear y agregar el panel de entrada de texto
	    JPanel inputPanel = createInputPanel();
	    inputPanel.setVisible(false); // Ocultamos el input panel inicialmente
	    bottomPanel.add(inputPanel, BorderLayout.SOUTH);

	    // Agregar el contenedor inferior al panel principal del chat
	    panelChat.add(bottomPanel, BorderLayout.SOUTH);

	    return panelChat;
	}

	// Crear el panel de emojis
	private JPanel createEmojiPanel() {
		JPanel emojiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		emojiPanel.setBackground(ElegantPalette.BACKGROUND); // Fondo claro

		// Añadir emojis al panel
		for (int i = 0; i <= BubbleText.MAXICONO; i++) {
			JLabel emojiLabel = new JLabel();
			emojiLabel.setIcon(BubbleText.getEmoji(i)); // Obtener emoji
			emojiLabel.setName(String.valueOf(i));
			emojiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			// Añadir evento de clic al emoji
			emojiLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					ContactoVisor seleccionado = listaContactos.getSelectedValue();
					if (seleccionado != null) {
						ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
						if (chat != null) {
							controlador.enviarMensaje(chat, Integer.parseInt(emojiLabel.getName())); // Enviar emoji
							abrirChat(seleccionado); // Actualizar el chat en la interfaz
							// Mantener la selección actual
							listaContactos.setSelectedValue(seleccionado, true);
						}
					} else {
						JOptionPane.showMessageDialog(VentanaPrincipal.this, "Por favor selecciona un contacto para enviar el emoji.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			emojiPanel.add(emojiLabel); // Añadir el emoji al panel
		}

		return emojiPanel;
	}




	// Buscar un contacto
	private void buscarContacto() {
		String textoBusqueda = campoBusqueda.getText().trim().toLowerCase();
		List<ContactoVisor> contactosFiltrados = new ArrayList<>();
		for (int i = 0; i < modeloLista.size(); i++) {
			ContactoVisor contacto = modeloLista.getElementAt(i);
			if (contacto.getNombre().toLowerCase().contains(textoBusqueda)) {
				contactosFiltrados.add(contacto);
			}
		}

		modeloLista.clear();
		for (ContactoVisor c : contactosFiltrados) {
			modeloLista.addElement(c);
		}
	}


	// Abrir un chat
	private void abrirChat(ContactoVisor contacto) {
	    // Ocultar el título inicial del panel de chat
	    JPanel topPanel = (JPanel) panelChat.getComponent(0);
	    JLabel tituloChat = (JLabel) topPanel.getComponent(0);
	    tituloChat.setText(contacto.getNombre());

	    mensajesPanel.removeAll(); // Limpiar mensajes existentes

	    // Recuperar mensajes del contacto desde la base de datos
	    ChatIndividual chat = controlador.getChatIndividual(contacto.getNombre());
	    List<Mensaje> mensajes = controlador.getMensajes(chat);

	    for (Mensaje mensaje : mensajes) {
	        // Determinar si el mensaje fue enviado por el usuario
	        boolean enviado = mensaje.getEmisor().equals(controlador.getUsuarioActual());
	        Color fondoColor = enviado ? ElegantPalette.SENT_MESSAGE_BACKGROUND : ElegantPalette.RECEIVED_MESSAGE_BACKGROUND;

	        // Validar si el mensaje es un emoji o texto
	        if (mensaje.getTexto() != null && !mensaje.getTexto().trim().isEmpty()) {
	            // Crear burbuja para texto
	            BubbleText burbuja = new BubbleText(mensajesPanel, mensaje.getTexto(), fondoColor,
	                    enviado ? "Tú" : contacto.getNombre(),
	                    enviado ? BubbleText.SENT : BubbleText.RECEIVED,
	                    12);

	            mensajesPanel.add(burbuja);
	        } else if (mensaje.getEmoticono() != null) { // Cambiar según el valor por defecto si no hay emoji
	            // Crear burbuja para emoji
	            BubbleText burbuja = new BubbleText(mensajesPanel, mensaje.getEmoticono(), fondoColor,
	                    enviado ? "Tú" : contacto.getNombre(),
	                    enviado ? BubbleText.SENT : BubbleText.RECEIVED,
	                    18);

	            mensajesPanel.add(burbuja);
	        }
	    }

	    // Hacer visibles el panel de emojis y el input panel al abrir el chat
	    JPanel bottomPanel = (JPanel) panelChat.getComponent(2); // Contenedor inferior
	    JPanel emojiPanel = (JPanel) bottomPanel.getComponent(0); // Panel de emojis
	    emojiPanel.setVisible(true);

	    JPanel inputPanel = (JPanel) bottomPanel.getComponent(1); // Panel de entrada de texto
	    inputPanel.setVisible(true);

	    // Verificar si el scroll está abajo antes de añadir mensajes
	    JScrollBar verticalScrollBar = ((JScrollPane) mensajesPanel.getParent().getParent()).getVerticalScrollBar();
	    boolean scrollAbajo = (verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() == verticalScrollBar.getMaximum());

	    mensajesPanel.revalidate();
	    mensajesPanel.repaint();

	    // Forzar el scroll hacia abajo si estaba previamente abajo
	    if (scrollAbajo) {
	        SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
	    }
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new BorderLayout());
		areaTexto = new JTextArea(3, 30);
		areaTexto.setLineWrap(true);
		inputPanel.add(new JScrollPane(areaTexto), BorderLayout.CENTER);

		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(e -> enviarMensaje());
		inputPanel.add(btnEnviar, BorderLayout.EAST);
		return inputPanel;
	}

	// Agregar un nuevo contacto
	private void agregarContacto() {	
		mainFrame.showContactPanel();
	}
	
	private void crearGrupo() {
		// TODO Auto-generated method stub
		mainFrame.showGroupPanel();
	}

	// Enviar mensaje
	private void enviarMensaje() {
	    String mensaje = areaTexto.getText().trim();
	    if (!mensaje.isEmpty()) {
	        ContactoVisor seleccionado = listaContactos.getSelectedValue();
	        if (seleccionado != null) {
	            ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
	            if (chat != null) {
	                controlador.enviarMensaje(chat, mensaje);
	                cargarContactos();

	                // Mantener el contacto seleccionado y actualizar el chat
	                listaContactos.setSelectedValue(seleccionado, true);
	                abrirChat(seleccionado);

	                // Limpiar el área de texto
	                areaTexto.setText("");
	            }
	        }
	    }
	}


	// Método para activar Premium con pantalla de descuentos y validación de la factoría
	private void activarPremium() {
	    // Mostrar ventana de descuentos
	    JDialog ventanaDescuentos = crearVentanaDescuentos();

	    // Panel para mostrar el coste y los descuentos
	    JPanel panelDescuentos = new JPanel();
	    panelDescuentos.setLayout(new BoxLayout(panelDescuentos, BoxLayout.Y_AXIS));

	    // Mostrar coste inicial
	    JLabel lblCostoInicial = new JLabel("Coste inicial: $24.99");
	    panelDescuentos.add(lblCostoInicial);

	    // Crear los descuentos disponibles (simulamos descuentos)
	    String[] descuentos = {"Descuento por Fecha (10%)", "Descuento por Mensajes (15%)"};
	    JComboBox<String> comboDescuentos = new JComboBox<>(descuentos);
	    panelDescuentos.add(comboDescuentos);

	    // Label para mostrar el precio actualizado
	    JLabel lblPrecioActualizado = new JLabel("Precio actualizado: $24.99");
	    panelDescuentos.add(lblPrecioActualizado);

	    // Crear botón para aplicar descuento
	    JButton btnAplicarDescuento = crearBotonAplicarDescuento(comboDescuentos, lblPrecioActualizado);
	    panelDescuentos.add(btnAplicarDescuento);

	    // Crear botón de confirmar y pagar
	    JButton btnConfirmarPago = crearBotonConfirmarPago(ventanaDescuentos);
	    panelDescuentos.add(btnConfirmarPago);

	    // Añadir el panel de descuentos al dialog
	    ventanaDescuentos.add(panelDescuentos, BorderLayout.CENTER);

	    // Configuración de la ventana de descuentos
	    configurarVentanaDescuentos(ventanaDescuentos);
	}

	// Método que crea la ventana de descuentos
	private JDialog crearVentanaDescuentos() {
	    return new JDialog(mainFrame.frame, "Descuentos Premium", true);
	}

	// Método que crea el botón de aplicar descuento
	private JButton crearBotonAplicarDescuento(JComboBox<String> comboDescuentos, JLabel lblPrecioActualizado) {
	    JButton btnAplicarDescuento = new JButton("Aplicar Descuento");
	    btnAplicarDescuento.addActionListener(e -> aplicarDescuento(comboDescuentos, lblPrecioActualizado));
	    return btnAplicarDescuento;
	}

	// Método que aplica el descuento seleccionado
	private void aplicarDescuento(JComboBox<String> comboDescuentos, JLabel lblPrecioActualizado) {
	    String selectedDescuento = (String) comboDescuentos.getSelectedItem();
	    double precioOriginal = 24.99;
	    double precioFinal = precioOriginal;

	    try {
	        // Obtener el descuento adecuado según la selección
	        Descuento descuento = obtenerDescuento(selectedDescuento);

	        // Si existe un descuento, lo aplicamos
	        if (descuento != null && esDescuentoValido(descuento)) {
	            precioFinal -= precioOriginal * (descuento.getDescuento(controlador.getUsuarioActual()) / 100);
	            lblPrecioActualizado.setText("Precio actualizado: $" + String.format("%.2f", precioFinal));
	        } else {
	            JOptionPane.showMessageDialog(null, "Descuento no válido.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (IllegalArgumentException ex) {
	        JOptionPane.showMessageDialog(null, "Error al aplicar descuento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	// Método que verifica si el descuento es válido
	private boolean esDescuentoValido(Descuento descuento) {
	    if (descuento instanceof DescuentoPorFecha) {
	        return esFechaValida();
	    } else if (descuento instanceof DescuentoPorMensaje) {
	        return esMensajeValido();
	    }
	    return false;
	}

	// Método que obtiene el descuento según la selección
	private Descuento obtenerDescuento(String descuentoSeleccionado) {
	    if (descuentoSeleccionado.equals("Descuento por Fecha (10%)")) {
	        return new DescuentoPorFecha(10.0, "2025-01-01", "2025-07-31"); // Ejemplo de fechas
	    } else if (descuentoSeleccionado.equals("Descuento por Mensajes (15%)")) {
	        return new DescuentoPorMensaje(15.0, 10); // Ejemplo: requiere 10 mensajes
	    }
	    return null; // Si no es un descuento válido
	}

	// Método que crea el botón de confirmar y pagar
	private JButton crearBotonConfirmarPago(JDialog ventanaDescuentos) {
	    JButton btnConfirmarPago = new JButton("Confirmar y Pagar");
	    btnConfirmarPago.addActionListener(e -> {
	        if (realizarPago()) {
	            controlador.getUsuarioActual().setPremium(true); // Actualizar estado a premium
	            JOptionPane.showMessageDialog(ventanaDescuentos, "¡Pago realizado exitosamente! Ahora eres usuario Premium.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
	            ventanaDescuentos.dispose(); // Cierra la ventana de descuentos
	            mainFrame.showMainWindow(); // Actualiza la ventana
	            controlador.modificarUsuario(controlador.getUsuarioActual()); // Actualiza el usuario en la base de datos)
	            
	        } else {
	            JOptionPane.showMessageDialog(ventanaDescuentos, "Error en el pago.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    });
	    return btnConfirmarPago;
	}
	private boolean realizarPago() {
        // Simulación de pago correcto
	    return true; 
	}


	// Método que configura la ventana de descuentos
	private void configurarVentanaDescuentos(JDialog ventanaDescuentos) {
	    ventanaDescuentos.setSize(300, 250);
	    ventanaDescuentos.setLocationRelativeTo(this);
	    ventanaDescuentos.setVisible(true);
	}

	// Método que simula la comprobación de fechas del usuario
	private boolean esFechaValida() {
	    // Verificar si la fecha de registro del usuario está dentro del rango permitido
	    LocalDate fechaRegistro = controlador.getUsuarioActual().getFechaRegistro();
	    LocalDate fechaInicio = LocalDate.of(2025, 1, 1);
	    LocalDate fechaFin = LocalDate.of(2025, 07, 31);

	    // Verificar si la fecha de registro está dentro del rango
	    return !fechaRegistro.isBefore(fechaInicio) && !fechaRegistro.isAfter(fechaFin);
	}

	// Método que simula la comprobación de los mensajes enviados por el usuario
	private boolean esMensajeValido() {
	    int mensajesEnviados = controlador.getUsuarioActual().getMensajesEnviadosUltimoMes();
	    return mensajesEnviados >= 10;  // Por ejemplo, se requiere al menos 10 mensajes
	}
	
	private boolean esUsuarioPremium() {
	    return controlador.getUsuarioActual().isPremium(); 
	}
	
	

}
