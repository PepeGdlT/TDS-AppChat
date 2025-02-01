package controlador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import modelo.CatalogoUsuarios;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import persistencia.AdaptadorUsuarioTDS;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorChatIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;


public enum ControladorAppChat {
    INSTANCE;
   
    
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private IAdaptadorChatIndividualDAO adaptadorChatIndividual;
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorGrupoDAO adaptadorGrupo;
    
    private CatalogoUsuarios catalogoUsuarios;
    
    private Usuario usuarioActual;
    private ChatIndividual chatActual;

    private ControladorAppChat() {
        inicializarAdaptadores();
        inicializarCatalogos();
    }


    
    private void inicializarAdaptadores() {
        FactoriaDAO factoria = null;
        try {
            factoria = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adaptadorUsuario = factoria.getUsuarioDAO();
        adaptadorChatIndividual = factoria.getChatIndividualDAO();
        adaptadorMensaje = factoria.getMensajeDAO();
        adaptadorGrupo = factoria.getGrupoDAO();
    }
    
    private void inicializarCatalogos() {
        catalogoUsuarios = CatalogoUsuarios.INSTANCE;
    }

    //-----------------------------------------------------
    // Funciones de control de usuario actual
    //-----------------------------------------------------
    
    public boolean registrarUsuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
        
    	if(catalogoUsuarios.encontrarUsuario(numeroTelefono).isPresent()){
    		return false;
    	}
    	
        Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);

    	if (catalogoUsuarios.contains(usuario)) {
    	      return false;
    	}
    	
        catalogoUsuarios.addUsuario(usuario);
        adaptadorUsuario.registrarUsuario(usuario);
        
		return iniciarSesion(numeroTelefono, contrasena);

    }

    public void modificarUsuario(Usuario usuario) {
        if (usuario != null) {
            adaptadorUsuario.modificarUsuario(usuario);
        }
    }



    public boolean iniciarSesion(String numeroTelefono, String contrasena) {
        Usuario usuario = catalogoUsuarios.getUsuario(numeroTelefono);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public boolean borrarUsuario(Usuario usuario) {
    	if (!catalogoUsuarios.contains(usuario)) {
  	      return false;
  	}
        adaptadorUsuario.borrarUsuario(usuario);
        catalogoUsuarios.removeUsuario(usuario);
        return true;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void hacerPremium(Boolean premium) {
        usuarioActual.setPremium(premium);
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    //-----------------------------------------------------
    // Funciones de control de contactos
    //-----------------------------------------------------
    
    public List<ChatIndividual> getChatIndividuals() {
        if (usuarioActual == null) {
            return new LinkedList<>();
        }
        return usuarioActual.getChatsIndividuales();
    }
    
    public List<Grupo> getGrupos() {
        if (usuarioActual == null) {
            return new LinkedList<>();
        }
        return usuarioActual.getGrupos();
    }
    
    public boolean agregarContacto(String nombre, String telefono) {
        if (usuarioActual == null) return false;

        Optional<ChatIndividual> chatExistente = usuarioActual.getChatsIndividuales().stream()
                .filter(c -> c.getnumeroTelefono().equals(telefono))
                .findFirst();

        if (chatExistente.isPresent()) return false;

        Usuario contacto = catalogoUsuarios.getUsuario(telefono);
        if (contacto == null) return false;

        ChatIndividual nuevoContacto = new ChatIndividual(nombre, telefono, contacto);
        usuarioActual.addContacto(nuevoContacto);
        
        adaptadorChatIndividual.registrarChatIndividual(nuevoContacto);
        adaptadorUsuario.modificarUsuario(usuarioActual);

        return true;
    }


    //-----------------------------------------------------
    // Funciones de control de chats
    //-----------------------------------------------------
    
    public List<Mensaje> getMensajes(Contacto contacto) {
        if (contacto == null || contacto.getMensajesEnviados() == null) {
            return Collections.emptyList(); // Devuelve una lista vacía segura
        }

        return contacto.getMensajesEnviados().stream()
                .sorted(Comparator.comparing(Mensaje::getHora))
                .collect(Collectors.toList());
    }

    
    
    public Mensaje getUltimoMensaje(ChatIndividual chat) {
        List<Mensaje> mensajes = getMensajes(chat);
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.get(mensajes.size() - 1);
    }
    
    public void enviarMensaje(Contacto contacto, String mensajeTexto) {
        if (mensajeTexto == null || mensajeTexto.trim().isEmpty()) return;

        // Crear el mensaje
        Mensaje mensaje = new Mensaje(mensajeTexto, LocalDateTime.now(), usuarioActual, contacto);

        // Añadir el mensaje al chat del emisor
        contacto.enviarMensaje(mensaje);
        adaptadorMensaje.registrarMensaje(mensaje);

        if (contacto instanceof ChatIndividual) {
            ChatIndividual chatIndividual = (ChatIndividual) contacto;

            // Obtener el usuario receptor
            Usuario receptor = chatIndividual.getContacto();

            // Encontrar el chat correspondiente en el receptor
            ChatIndividual chatReceptor = receptor.getChatsIndividuales().stream()
                    .filter(c -> c.getnumeroTelefono().equals(usuarioActual.getNumeroTelefono()))
                    .findFirst()
                    .orElse(null);

            // Si el chat no existe en el receptor, se crea
            if (chatReceptor == null) {
                chatReceptor = new ChatIndividual(usuarioActual.getNumeroTelefono(),
                                                  usuarioActual.getNumeroTelefono(), usuarioActual);
                receptor.addContacto(chatReceptor);
                adaptadorChatIndividual.registrarChatIndividual(chatReceptor);
            }

            // Añadir el mensaje al chat del receptor
            chatReceptor.enviarMensaje(mensaje);
            adaptadorChatIndividual.modificarChatIndividual(chatReceptor);

            // Actualizar al receptor en la base de datos
            adaptadorUsuario.modificarUsuario(receptor);
        } else if (contacto instanceof Grupo) {
            adaptadorGrupo.modificarGrupo((Grupo) contacto);
        }

        // Actualizar el chat del emisor
        adaptadorChatIndividual.modificarChatIndividual((ChatIndividual) contacto);
    }


    public void enviarMensaje(Contacto contacto, int emoji) {
        if (contacto == null) return;

        // Crear el mensaje
        Mensaje mensaje = new Mensaje(emoji, LocalDateTime.now(), usuarioActual, contacto);

        // Añadir el mensaje al chat del emisor
        contacto.enviarMensaje(mensaje);

        // Registrar el mensaje en la base de datos
        adaptadorMensaje.registrarMensaje(mensaje);

        if (contacto instanceof ChatIndividual) {
            ChatIndividual chatIndividual = (ChatIndividual) contacto;

            // Obtener el receptor
            Usuario receptor = chatIndividual.getContacto();

            // Buscar el chat correspondiente al emisor desde el lado del receptor
            ChatIndividual chatReceptor = receptor.getChatsIndividuales().stream()
                    .filter(c -> c.getnumeroTelefono().equals(usuarioActual.getNumeroTelefono()))
                    .findFirst()
                    .orElse(null);

            // Si no existe, crear un nuevo chat
            if (chatReceptor == null) {
                chatReceptor = new ChatIndividual(usuarioActual.getNombreCompleto(),
                                                  usuarioActual.getNumeroTelefono(), usuarioActual);
                receptor.addContacto(chatReceptor);
                adaptadorChatIndividual.registrarChatIndividual(chatReceptor);
            }

            // Añadir el mensaje al chat del receptor
            chatReceptor.enviarMensaje(mensaje);

            // Persistir cambios del receptor
            adaptadorChatIndividual.modificarChatIndividual(chatReceptor);
            adaptadorUsuario.modificarUsuario(receptor);
        } else if (contacto instanceof Grupo) {
            adaptadorGrupo.modificarGrupo((Grupo) contacto);
        }

        // Persistir cambios del emisor
        adaptadorChatIndividual.modificarChatIndividual((ChatIndividual) contacto);
    }


    public void setChatActual(ChatIndividual chat) {
        this.chatActual = chat;
    }

    public boolean isAdmin(Grupo grupo) {
        return grupo.getAdministrador().equals(usuarioActual);
    }

    //-----------------------------------------------------
    // Funciones de creación de objetos
    //-----------------------------------------------------
    
    public ChatIndividual crearChatIndividual(String nombre, String numeroTelefono) {
		if (!usuarioActual.existeChatIndividual(nombre)) {
			Optional<Usuario> usuarioOpt = catalogoUsuarios.encontrarUsuario(numeroTelefono);

			if (usuarioOpt.isPresent()) {
				ChatIndividual nuevoContacto = new ChatIndividual(nombre, numeroTelefono, usuarioOpt.get());
				usuarioActual.addContacto(nuevoContacto);

				adaptadorChatIndividual.registrarChatIndividual(nuevoContacto);

				adaptadorUsuario.modificarUsuario(usuarioActual);
				return nuevoContacto;
			}
		}
		return null;
    }
    
    public Grupo crearGrupo(String nombre, List<ChatIndividual> miembros) {
		if (usuarioActual.existeGrupo(nombre)) {
			return null;
		}

		Grupo nuevoGrupo = new Grupo(nombre, new LinkedList<Mensaje>(), miembros, usuarioActual);

		usuarioActual.addContacto(nuevoGrupo);
		miembros.stream().forEach(p -> p.addGrupo(nuevoGrupo));

		adaptadorGrupo.registrarGrupo(nuevoGrupo);

		adaptadorUsuario.modificarUsuario(usuarioActual);

		miembros.stream().forEach(p -> {
			Usuario usuario = p.getContacto();
			adaptadorUsuario.modificarUsuario(usuario);
		});

		return nuevoGrupo;
	}
    //-----------------------------------------------------
    // Funciones de modificación de objetos
    //-----------------------------------------------------
    
	public Grupo modificarGrupo(Grupo grupo, String nombre, List<ChatIndividual> miembros) {
		grupo.setNombreAgregado(nombre);

		// Creo listas para las altas y las bajas
		List<ChatIndividual> nuevos = new LinkedList<>();
		List<ChatIndividual> mantenidos = new LinkedList<>();

		for (ChatIndividual contacto : miembros) {
			if (grupo.existeContacto(contacto.getContacto())) {
				mantenidos.add(contacto);
			} else {
				nuevos.add(contacto);
			}
		}

		List<ChatIndividual> eliminados = new LinkedList<>(grupo.getMiembros());
		eliminados.removeAll(miembros);

		// Le modifico el grupo si el usuario ya existia. Si es nuevo, se lo añado
		mantenidos.stream().forEach(p -> p.modificarGrupo(grupo));
		nuevos.stream().forEach(p -> p.addGrupo(grupo));

		// Elimino el grupo de los participantes que ya no lo tienen
		eliminados.stream().forEach(p -> {
			p.eliminarGrupo(grupo);
			adaptadorUsuario.modificarUsuario(p.getContacto());
		});

		// Se le cambia al grupo la lista de participantes
		grupo.setMiembros(miembros);

		// Conexion con persistencia
		adaptadorGrupo.modificarGrupo(grupo);

		// Actualiza los usuarios que no estaban antes en el grupo
		nuevos.stream().map(ChatIndividual::getContacto).forEach(u -> adaptadorUsuario.modificarUsuario(u));

		return grupo;
	}
    
	public ChatIndividual modificarChatIndividual(ChatIndividual chat, String nombre) {
		chat.setNombreAgregado(nombre);

		adaptadorChatIndividual.modificarChatIndividual(chat);

		return chat;
	}

	public void modificarMensaje(Mensaje mensaje) {
		adaptadorMensaje.modificarMensaje(mensaje);
	}

    //-----------------------------------------------------
    // Funciones de búsqueda de mensajes
    //-----------------------------------------------------
    
    public List<Mensaje> buscarMensajes(String emisor, LocalDateTime fechaInicio, LocalDateTime fechaFin, String text) {
    	return null;
    }

    //-----------------------------------------------------
    // Funciones de eliminación de objetos
    //-----------------------------------------------------
    
    public void eliminarContacto(Contacto c) {
		usuarioActual.removeContacto(c);
		if (c instanceof ChatIndividual) {
			adaptadorChatIndividual.borrarChatIndividual((ChatIndividual) c);
		} else {
			Grupo grupo = (Grupo) c;
			grupo.getMiembros().stream().forEach(p -> {
				p.eliminarGrupo(grupo);
				adaptadorUsuario.modificarUsuario(p.getContacto());
			});
			adaptadorGrupo.borrarGrupo((Grupo) c);
		}

		adaptadorUsuario.modificarUsuario(usuarioActual);
	}



    public ChatIndividual getChatIndividual(String nombre) {
        if (usuarioActual == null || nombre == null || nombre.trim().isEmpty()) {
            return null; // Si no hay usuario actual o el nombre es inválido
        }

        // Buscar el chat individual con el nombre especificado
        for (ChatIndividual chat : usuarioActual.getChatsIndividuales()) {
            if (chat.getNombreContacto().equalsIgnoreCase(nombre.trim())) {
                return chat; // Devolver el chat encontrado
            }
        }

        return null; // Si no se encuentra ningún chat
    }

    
    
    public List<String[]> getContactosParaTabla() {
        if (usuarioActual == null) {
            return new ArrayList<>();
        }
        return usuarioActual.getChatsIndividuales().stream()
                .map(chat -> new String[]{chat.getNombreContacto(), chat.getnumeroTelefono()})
                .collect(Collectors.toList());
    }



    // Obtener un contacto por su nombre (ejemplo para edición o visualización)
    public ChatIndividual getContactoPorNombre(String nombre) {
        return getChatIndividual(nombre);
    }
    



	public Grupo getGrupoPorNombre(String nombre) {
        return usuarioActual.getGrupos().stream()
                .filter(grupo -> grupo.getNombreContacto().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
	}





    
}