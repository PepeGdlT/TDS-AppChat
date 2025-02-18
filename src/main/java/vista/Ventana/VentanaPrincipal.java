package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
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
import vista.utils.IconsResource;
import vista.utils.utils;

public class VentanaPrincipal extends JPanel {



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
	private ControladorAppChat controlador;
	private VentanaInicio mainFrame;

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

		JLabel lblUsuario = new JLabel(controlador.getUsuarioActual().getNombreCompleto());
	    String urlFotoPerfil = controlador.getUsuarioActual().getFotoPerfil();
	    
	    JLabel lblFotoPerfil = new JLabel();
	    utils.cargarImagenDesdeURL(urlFotoPerfil, lblFotoPerfil, 40, 40); 
		JButton btnPremium = new JButton(esUsuarioPremium() ? IconsResource.PDF : IconsResource.PREMIUM);
		btnPremium.addActionListener(e -> {
			if (esUsuarioPremium()) {
				ContactoVisor seleccionado = listaContactos.getSelectedValue();
				if (seleccionado != null) {
					ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
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
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> buscarContacto());
		
	    JButton btnBuscarMensajes = new JButton(IconsResource.LUPA);
	    btnBuscarMensajes.addActionListener(e -> buscaMensajes());

		topPanel.add(lblFotoPerfil);
		topPanel.add(lblUsuario);
		topPanel.add(btnPremium);
		topPanel.add(campoBusqueda);
		topPanel.add(btnBuscar);
		topPanel.add(btnBuscarMensajes);

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
								new VentanaContactoEdit(mainFrame.frame, chat,controlador).setVisible(true);
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
	    int emojisPorPagina = 9; 
	    List<JLabel> emojiLabels = new ArrayList<>();
	    JPanel emojiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    
	    for (int i = 0; i <= 25; i++) {
	        JLabel emojiLabel = new JLabel();
	        ImageIcon originalIcon = (ImageIcon) BubbleText.getEmoji(i);
	        Image image = originalIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
	        emojiLabel.setIcon(new ImageIcon(image));
	        emojiLabel.setName(String.valueOf(i));
	        emojiLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        
	        emojiLabel.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                ContactoVisor seleccionado = listaContactos.getSelectedValue();
	                if (seleccionado != null) {
	                    ChatIndividual chat = controlador.getChatIndividual(seleccionado.getNombre());
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
	
	private void buscaMensajes() {
	    mainFrame.showBusquedaMensajesPanel();
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
		VentanaDescuentos ventanaDes = new VentanaDescuentos(mainFrame.frame, controlador.getUsuarioActual(), controlador);

		
	}


	private boolean esUsuarioPremium() {
		return controlador.getUsuarioActual().isPremium(); 
	}



}
