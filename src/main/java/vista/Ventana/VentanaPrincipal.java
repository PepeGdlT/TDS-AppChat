package vista.Ventana;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.ExportPDF;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import modelo.Descuento.Descuento;
import modelo.Descuento.DescuentoPorFecha;
import modelo.Descuento.DescuentoPorMensaje;
import modelo.Descuento.FactoriaDescuento;
import tds.BubbleText;
import vista.utils.ContactoListRenderer;
import vista.utils.ContactoVisor;
import vista.utils.ElegantPalette;
import vista.utils.GrupoListRenderer;
import vista.utils.GrupoVisor;
import vista.utils.IconsResource;
import vista.utils.Visor;
import vista.utils.utils;

public class VentanaPrincipal extends JPanel {



	/*
	 * POSIBLE LISTENER PARA CAMBIOS DE CONTACTOS Y QUE SE HAGA UN CARGARCONTACTOS
	 * 
	 * 
	 */

	/*
	 * FALTA
	 * 
	 * -LOGICA MENSAJES POR MES
	 * -s
	 * 
	 * 
	 * 
	 */


	// Componentes principales
	private DefaultListModel<Visor> modeloLista;
	private JList<Visor> listaContactos;
	private JPanel mensajesPanel; // Panel donde se colocarán las burbujas
	private JPanel panelChat;
	private JTextArea areaTexto;
	private JTextField campoBusqueda;
	private ControladorAppChat controlador;
	private VentanaInicio mainFrame;
	private boolean esVistaChatIndividual = true;

	// Mapa para almacenar mensajes por contacto


	public VentanaPrincipal(VentanaInicio mainFrame, ControladorAppChat controlador) {
		this.mainFrame = mainFrame;
		this.controlador = controlador;
		setLayout(new BorderLayout());

		// Crear los paneles principales
		add(createTopPanel(), BorderLayout.NORTH);
		add(createContactListPanel(), BorderLayout.WEST);
		add(createChatPanel(), BorderLayout.CENTER);

	}

	// Crear el panel superior
	private JPanel createTopPanel() {
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// Crear un botón con la foto y el nombre del usuario
		JButton btnPerfil = new JButton();
		btnPerfil.setLayout(new BorderLayout());

		// Cargar la foto de perfil
		String urlFotoPerfil = controlador.getUsuarioActual().getFotoPerfil();
		JLabel lblFotoPerfil = new JLabel();
		utils.cargarImagenDesdeURL(urlFotoPerfil, lblFotoPerfil, 40, 40);

		// Cargar el nombre del usuario
		JLabel lblUsuario = new JLabel(controlador.getUsuarioActual().getNombreCompleto());
		lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));

		// Añadir la foto y el nombre al botón
		btnPerfil.add(lblFotoPerfil, BorderLayout.WEST);
		btnPerfil.add(lblUsuario, BorderLayout.CENTER);

		// Quitar el borde y el fondo del botón para que parezca un JLabel
		btnPerfil.setBorderPainted(false);
		btnPerfil.setContentAreaFilled(false);
		btnPerfil.setFocusPainted(false);



		// Añadir acción al botón para abrir la ventana de edición de perfil
		btnPerfil.addActionListener(e -> {
			mainFrame.showEditarPerfil();
		});

		// Añadir el botón al panel superior
		topPanel.add(btnPerfil);

		// Resto del código del TopPanel (botones de búsqueda, premium, logout, etc.)
		JButton btnPremium = new JButton(esUsuarioPremium() ? IconsResource.PDF : IconsResource.PREMIUM);
		btnPremium.addActionListener(e -> {
			if (esUsuarioPremium()) {
				Visor seleccionado = listaContactos.getSelectedValue();
				btnPremium.setIcon(esUsuarioPremium() ? IconsResource.PDF : IconsResource.PREMIUM);
				if (seleccionado != null) {
					ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombreContacto());
					if (chat != null) {
						ExportPDF.crearPDF(controlador.getUsuarioActual(), chat);
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
		campoBusqueda.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				buscarContacto();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				buscarContacto();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				buscarContacto();
			}
		});

		JButton btnBuscarMensajes = new JButton(IconsResource.LUPA);
		btnBuscarMensajes.addActionListener(e -> buscaMensajes());

		JButton btnCambioLista = new JButton(esVistaChatIndividual ? IconsResource.GROUP : IconsResource.CHAT);
		btnCambioLista.addActionListener(e-> {
			esVistaChatIndividual = !esVistaChatIndividual;
			btnCambioLista.setIcon(esVistaChatIndividual ? IconsResource.GROUP : IconsResource.CHAT);
			cargarContactos();
		});

		JButton btnLogout = new JButton(IconsResource.LOGOUT);
		btnLogout.addActionListener(e -> {
			controlador.cerrarSesion();
			mainFrame.showLoginPanel();
		});

		btnPremium.setBorderPainted(false);
		btnBuscarMensajes.setBorderPainted(false);
		btnLogout.setBorderPainted(false);

		topPanel.add(campoBusqueda);
		topPanel.add(btnBuscarMensajes);
		topPanel.add(btnPremium);
		topPanel.add(btnCambioLista);
		topPanel.add(btnLogout);

		return topPanel;
	}




	private void cargarContactos() {
		modeloLista.clear();  

		if (esVistaChatIndividual) {
			// Cargar contactos individuales
			List<ChatIndividual> contactos = controlador.getChatIndividuals();
			for (ChatIndividual chat : contactos) {
				Visor contactoVisor = new ContactoVisor(chat.getNombreContacto() , chat.getFoto(), chat.getUltimoMensaje());
				modeloLista.addElement(contactoVisor);
			}
			listaContactos.setCellRenderer(new ContactoListRenderer());  
		} else {
			// Cargar grupos
			List<Grupo> grupos = controlador.getGrupos();
			for (Grupo grupo : grupos) {
				Visor grupoVisor = new GrupoVisor(grupo.getNombreContacto(), grupo.getFoto(), "Lista de Difusión");
				modeloLista.addElement(grupoVisor);
			}
			listaContactos.setCellRenderer(new GrupoListRenderer());  
		}
	}




	// Crear el panel de lista de contactos
	private JPanel createContactListPanel() {
		JPanel contactPanel = new JPanel(new BorderLayout());
		contactPanel.setPreferredSize(new Dimension(250, getHeight()));

		modeloLista = new DefaultListModel<>();
		listaContactos = new JList<>(modeloLista);
		//listaContactos.setCellRenderer(new VisorListRenderer<Visor>());
		listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Evento de doble clic o clic derecho
		listaContactos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// Doble clic para abrir chat
					Visor seleccionado = listaContactos.getSelectedValue();
					if (seleccionado != null) {
						abrirChat(seleccionado);  
					} else {
						JOptionPane.showMessageDialog(VentanaPrincipal.this, 
								"No se ha seleccionado ningún grupo o contacto.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// Mostrar menú contextual con botón derecho
					JPopupMenu menu = new JPopupMenu();

					JMenuItem itemVisualizar = new JMenuItem("Visualizar perfil");
					itemVisualizar.addActionListener(e2 -> {
						Visor seleccionado = listaContactos.getSelectedValue();
						if (seleccionado != null) {
							if (seleccionado instanceof ContactoVisor) {
								ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombreContacto());
								if (chat != null) {
									new VentanaContactoVer(mainFrame.frame, chat).setVisible(true);
								}
							} else if (seleccionado instanceof GrupoVisor) {
								Grupo grupo = controlador.getGrupoPorNombre(seleccionado.getNombreContacto());
								if (grupo != null) {
									new VentanaGrupoVer(mainFrame.frame, grupo).setVisible(true);  // Nueva ventana para grupo
								}
							}
						} else {
							JOptionPane.showMessageDialog(VentanaPrincipal.this, 
									"No se ha seleccionado ningún grupo o contacto.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					});

					JMenuItem itemEditar = new JMenuItem("Editar");
					itemEditar.addActionListener(e2 -> {
						Visor seleccionado = listaContactos.getSelectedValue();
						if (seleccionado != null) {
							if (seleccionado instanceof ContactoVisor) {
								ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombreContacto());
								if (chat != null) {
									new VentanaContactoEdit(mainFrame.frame, chat, controlador).setVisible(true);
									cargarContactos();
								}
							} else if (seleccionado instanceof GrupoVisor) {
								Grupo grupo = controlador.getGrupoPorNombre(seleccionado.getNombreContacto());
								if (grupo != null) {
									new VentanaGrupoEdit(mainFrame.frame, grupo, controlador).setVisible(true);  // Nueva ventana para editar grupo
									cargarContactos();  // Cambiar a cargarGrupos si es necesario
								}
							}
						} else {
							JOptionPane.showMessageDialog(VentanaPrincipal.this, 
									"No se ha seleccionado ningún grupo o contacto.", "Error", JOptionPane.ERROR_MESSAGE);
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
		int emojisPorPagina = 8; 
		List<JLabel> emojiLabels = new ArrayList<>();
		JPanel emojiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		for (int i = 0; i < 24; i++) {
			JLabel emojiLabel = new JLabel();
			ImageIcon originalIcon = (ImageIcon) BubbleText.getEmoji(i);
			Image image = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			emojiLabel.setIcon(new ImageIcon(image));
			emojiLabel.setName(String.valueOf(i));
			emojiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

			emojiLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					Visor seleccionado = listaContactos.getSelectedValue();
					if (seleccionado != null) {
						ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombreContacto());
						if (chat != null) {
							controlador.enviarMensaje(chat, Integer.parseInt(emojiLabel.getName()));
							abrirChat(seleccionado);
							listaContactos.setSelectedValue(seleccionado, true);
						}
					} else {
						JOptionPane.showMessageDialog(VentanaPrincipal.this, "Selecciona un contacto.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			emojiLabels.add(emojiLabel);
		}

		JPanel navigationPanel = new JPanel(new BorderLayout());
		JButton leftButton = new JButton("◀");
		JButton rightButton = new JButton("▶");

		int[] paginaActual = {0}; // Array para modificar dentro de los eventos

		ActionListener actualizarEmojis = e -> {
			emojiPanel.removeAll();
			int start = paginaActual[0] * emojisPorPagina;
			int end = Math.min(start + emojisPorPagina, emojiLabels.size());
			for (int i = start; i < end; i++) {
				emojiPanel.add(emojiLabels.get(i));
			}
			emojiPanel.revalidate();
			emojiPanel.repaint();
		};

		leftButton.addActionListener(e -> {
			if (paginaActual[0] > 0) {
				paginaActual[0]--;
				actualizarEmojis.actionPerformed(null);
			}
		});

		rightButton.addActionListener(e -> {
			if ((paginaActual[0] + 1) * emojisPorPagina < emojiLabels.size()) {
				paginaActual[0]++;
				actualizarEmojis.actionPerformed(null);
			}
		});

		navigationPanel.add(leftButton, BorderLayout.WEST);
		navigationPanel.add(emojiPanel, BorderLayout.CENTER);
		navigationPanel.add(rightButton, BorderLayout.EAST);

		actualizarEmojis.actionPerformed(null); // Mostrar primera página

		return navigationPanel;
	}





	// Buscar un contacto
	private void buscarContacto() {
		String textoBusqueda = campoBusqueda.getText().trim().toLowerCase();
		modeloLista.clear();

		List<ChatIndividual> contactos = controlador.getChatIndividuals();

		// Si el campo de búsqueda está vacío, restauramos la lista completa
		if (textoBusqueda.isEmpty()) {
			for (ChatIndividual chat : contactos) {
				ContactoVisor contactoVisor = new ContactoVisor(chat.getNombreContacto(), chat.getFoto(), chat.getUltimoMensaje());
				modeloLista.addElement(contactoVisor);
			}
			return; // Terminamos la ejecución aquí si no hay filtro
		}

		// Si hay búsqueda, filtramos los contactos
		for (ChatIndividual chat : contactos) {
			if (chat.getNombreContacto().toLowerCase().contains(textoBusqueda)) {
				ContactoVisor contactoVisor = new ContactoVisor(chat.getNombreContacto(), chat.getFoto(), chat.getUltimoMensaje());
				modeloLista.addElement(contactoVisor);
			}
		}
	}


	private void abrirChat(Visor contactoVisor) {
	    // Obtener el contacto real desde ContactoVisor (puede ser ChatIndividual o Grupo)
	    Contacto contacto = (Contacto) controlador.getContactoPorNombre(contactoVisor.getNombreContacto());  // Obtener el contacto real

	    // Ocultar el título inicial del panel de chat
	    JPanel topPanel = (JPanel) panelChat.getComponent(0);
	    JLabel tituloChat = (JLabel) topPanel.getComponent(0);
	    tituloChat.setText(contacto.getNombreContacto());

	    mensajesPanel.removeAll(); // Limpiar mensajes existentes

	    List<Mensaje> mensajes = obtenerMensajes(contactoVisor);

	    // Filtramos solo los mensajes del grupo si el contacto es un grupo
	    if (contacto instanceof Grupo) {
	        Grupo grupo = (Grupo) contacto; // Convertimos contacto a Grupo
	        // Solo mostramos los mensajes enviados al grupo, no los mensajes privados
	        mensajes = grupo.getMensajesEnviados(); // Obtener solo los mensajes enviados al grupo
	    }

	    // Formateador de hora y fecha
	    DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");
	    DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    // Variables para controlar la fecha actual
	    LocalDate fechaAnterior = null;

	    // Forzar el scroll hacia abajo si estaba previamente abajo
	    JScrollBar verticalScrollBar = ((JScrollPane) mensajesPanel.getParent().getParent()).getVerticalScrollBar();
	    boolean scrollAbajo = (verticalScrollBar.getValue() + verticalScrollBar.getVisibleAmount() == verticalScrollBar.getMaximum());

	    for (Mensaje mensaje : mensajes) {
	        LocalDate fechaMensaje = mensaje.getHora().toLocalDate();

	        if (fechaAnterior == null || !fechaMensaje.isEqual(fechaAnterior)) {
	            String etiquetaFecha = obtenerEtiquetaFecha(fechaMensaje, fechaFormatter);
	            JLabel etiqueta = new JLabel(etiquetaFecha, SwingConstants.CENTER);
	            etiqueta.setFont(new Font("Arial", Font.BOLD, 12));
	            etiqueta.setForeground(Color.GRAY);
	            etiqueta.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Espaciado
	            mensajesPanel.add(etiqueta);
	            fechaAnterior = fechaMensaje;
	        }

	        boolean enviado = mensaje.getEmisor().equals(controlador.getUsuarioActual());
	        Color fondoColor = enviado ? ElegantPalette.SENT_MESSAGE_BACKGROUND : ElegantPalette.RECEIVED_MESSAGE_BACKGROUND;

	        String hora = mensaje.getHora().format(horaFormatter);
	        String remitente = (enviado ? "Tú" : contacto.getNombreContacto()) + " - " + hora;

	        if (mensaje.getTexto() != null && !mensaje.getTexto().trim().isEmpty()) {
	            // Mensaje de texto
	            BubbleText burbuja = new BubbleText(mensajesPanel, mensaje.getTexto(), fondoColor,
	                    remitente, enviado ? BubbleText.SENT : BubbleText.RECEIVED, 12);
	            mensajesPanel.add(burbuja);
	        } else if (mensaje.getEmoticono() != null) {
	            // Emoji
	            BubbleText burbuja = new BubbleText(mensajesPanel, mensaje.getEmoticono(), fondoColor,
	                    remitente, enviado ? BubbleText.SENT : BubbleText.RECEIVED, 12);
	            mensajesPanel.add(burbuja);
	        }
	    }


	    // Hacer visibles el panel de emojis y el input panel al abrir el chat
	    JPanel bottomPanel = (JPanel) panelChat.getComponent(2); // Contenedor inferior
	    JPanel emojiPanel = (JPanel) bottomPanel.getComponent(0); // Panel de emojis
	    emojiPanel.setVisible(true);

	    JPanel inputPanel = (JPanel) bottomPanel.getComponent(1); // Panel de entrada de texto
	    inputPanel.setVisible(true);

	    // Revalidar y repintar el panel de mensajes
	    mensajesPanel.revalidate();
	    mensajesPanel.repaint();

	    // Forzar el scroll hacia abajo si estaba previamente abajo
	    if (scrollAbajo) {
	        SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
	    }
	}


	// Método que obtiene la etiqueta de fecha para mostrar en los mensajes
	private String obtenerEtiquetaFecha(LocalDate fechaMensaje, DateTimeFormatter fechaFormatter) {
		if (fechaMensaje.isEqual(LocalDate.now())) {
			return "Hoy";
		} else if (fechaMensaje.isEqual(LocalDate.now().minusDays(1))) {
			return "Ayer";
		} else {
			return fechaMensaje.format(fechaFormatter);
		}
	}
	// Método que obtiene los mensajes dependiendo si es grupo o chat individual
	private List<Mensaje> obtenerMensajes(Visor contactoVisor) {
	    Contacto contacto = (Contacto) controlador.getContactoPorNombre(contactoVisor.getNombreContacto());  // Obtener el contacto real
	    List<Mensaje> mensajesFiltrados = new ArrayList<>();

	    if (contacto instanceof Grupo) {
	        Grupo grupo = (Grupo) contacto;
	        mensajesFiltrados = grupo.getMensajesEnviados();
	    } else if (contacto instanceof ChatIndividual) {
	        ChatIndividual chat = (ChatIndividual) contacto;
	        mensajesFiltrados = controlador.getMensajes(chat); // Obtener mensajes del chat
	    }

	    return mensajesFiltrados;
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

	private void buscaMensajes() {
		mainFrame.showBusquedaMensajesPanel();
	}

	// Enviar mensaje
	private void enviarMensaje() {

		String mensaje = areaTexto.getText().trim();
		if (!mensaje.isEmpty()) {
			Visor seleccionado = listaContactos.getSelectedValue();

			if (seleccionado != null) {
				System.out.println("Enviando mensaje a " + seleccionado.getNombreContacto());
				Contacto contacto = controlador.getContactoPorNombre(seleccionado.getNombreContacto());

				if (contacto != null) {
					controlador.enviarMensaje(contacto, mensaje);
					cargarContactos();

					// Mantener la selección y refrescar la conversación
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
		VentanaDescuentos ventanaDes = new VentanaDescuentos(mainFrame.frame, controlador.getUsuarioActual(), controlador);


	}


	private boolean esUsuarioPremium() {
		return controlador.getUsuarioActual().isPremium(); 
	}



}
