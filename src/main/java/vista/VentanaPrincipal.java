package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Mensaje;
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
		JButton btnPremium = new JButton("Hacer Premium");
		btnPremium.addActionListener(e -> activarPremium());

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
	private JPanel createContactListPanel() {
		JPanel contactPanel = new JPanel(new BorderLayout());
		contactPanel.setPreferredSize(new Dimension(250, getHeight()));

		modeloLista = new DefaultListModel<>();
		listaContactos = new JList<>(modeloLista);
		listaContactos.setCellRenderer(new ContactoListRenderer());
		listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		listaContactos.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		            ContactoVisor seleccionado = listaContactos.getSelectedValue();
		            if (seleccionado != null) {
		                abrirChat(seleccionado);
		            } else {
		                JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } else if (e.getButton() == MouseEvent.BUTTON3) {
		            // Mostrar menú contextual
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
			                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha podido encontrar el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
			                    }
			                } else {
			                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
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
		                        JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha podido encontrar el contacto.", "Error", JOptionPane.ERROR_MESSAGE);
		                    }
		                } else {
		                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "No se ha seleccionado ningún contacto.", "Error", JOptionPane.ERROR_MESSAGE);
		                }
		            });




		            menu.add(itemEditar);
		            menu.add(itemVisualizar);
		            menu.show(listaContactos, e.getX(), e.getY());
		        }
		    }
		});


		// Configuración de JScrollPane
		JScrollPane scrollPane = new JScrollPane(listaContactos);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Solo barra vertical cuando es necesario
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Desactivar la barra horizontal

		// Botón para agregar contacto
		JButton btnAgregarContacto = new JButton("Agregar Contacto");
		btnAgregarContacto.addActionListener(e -> agregarContacto());

		contactPanel.add(scrollPane, BorderLayout.CENTER);
		contactPanel.add(btnAgregarContacto, BorderLayout.SOUTH);


		cargarContactos();

		return contactPanel;
		}


		// Crear el panel de chat
		private JPanel createChatPanel() {
			panelChat = new JPanel(new BorderLayout());

			// Crear un panel superior con el título "Proyecto AppChat" cuando no se ha seleccionado un chat
			JPanel topPanel = new JPanel();
			topPanel.setBackground(new Color(37, 211, 102));  // Color verde WhatsApp (opcional)

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

			// Crear y agregar el panel de entrada de texto
			JPanel inputPanel = createInputPanel();
			panelChat.add(inputPanel, BorderLayout.SOUTH);

			return panelChat;
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
				String texto = mensaje.getTexto();

				// Usar colores oscuros definidos en ElegantPalette
				Color fondoColor = enviado ? ElegantPalette.SENT_MESSAGE_BACKGROUND : ElegantPalette.RECEIVED_MESSAGE_BACKGROUND;

				// Crear la burbuja de mensaje
				BubbleText burbuja = new BubbleText(mensajesPanel, texto, fondoColor,
						enviado ? "Tú" : contacto.getNombre(),
								enviado ? BubbleText.SENT : BubbleText.RECEIVED,
										12);

				mensajesPanel.add(burbuja);
			}

			mensajesPanel.revalidate();
			mensajesPanel.repaint();
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
						// Actualizar el chat en la interfaz
						abrirChat(seleccionado);

						// Limpiar el área de texto
						areaTexto.setText("");
					}
				}
			}
		}

		// Activar cuenta premium
		private void activarPremium() {
			controlador.hacerPremium(true);
			JOptionPane.showMessageDialog(this, "¡Tu cuenta ahora es Premium!");
		}
	}
