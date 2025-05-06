package controlador;

import java.time.LocalDate;
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
import modelo.Descuento.Descuento;
import modelo.Descuento.FactoriaDescuento;
import modelo.filtro.FiltroComposite;
import modelo.filtro.FiltroNombre;
import modelo.filtro.FiltroNumero;
import modelo.filtro.FiltroTexto;
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
    
    private String estadoDescuentoFecha;
    private String estadoDescuentoMensajes;

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
        
        LocalDate hoy = LocalDate.now();
        List<Usuario> usuarios = adaptadorUsuario.recuperarTodosUsuarios();
        for (Usuario usuario : usuarios) {
            if (usuario.getUltimaFechaMensaje() != null && 
                !hoy.getMonth().equals(usuario.getUltimaFechaMensaje().getMonth())) {
                usuario.setMensajesEnviadosUltimoMes(0);
                usuario.setUltimaFechaMensaje(hoy);
                adaptadorUsuario.modificarUsuario(usuario);
            }
        }
    }

    //-----------------------------------------------------
    // Funciones de control de usuario actual
    //-----------------------------------------------------
    
    public boolean registrarUsuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
        
    	if(catalogoUsuarios.encontrarUsuario(numeroTelefono).isPresent()){
    		return false;
    	}
    	
        Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento, LocalDate.now());

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
            return Collections.emptyList(); 
        }

        return contacto.getMensajesEnviados().stream()
                .sorted(Comparator.comparing(Mensaje::getHora))
                .collect(Collectors.toList());
    }
    
   
    public List<Mensaje> obtenerTodosLosMensajes() {
        if (usuarioActual == null) {
            return Collections.emptyList();
        }

        List<Mensaje> todosLosMensajes = usuarioActual.getContactos().stream()
            .flatMap(contacto -> contacto.getMensajesEnviados().stream())  
            .sorted(Comparator.comparing(Mensaje::getHora))  
            .collect(Collectors.toList()); 

        return todosLosMensajes;
    }

    
    
    public Mensaje getUltimoMensaje(ChatIndividual chat) {
        List<Mensaje> mensajes = getMensajes(chat);
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.get(mensajes.size() - 1);
    }
    
    public void enviarMensaje(Contacto contacto, String mensajeTexto) {
        if (mensajeTexto == null || mensajeTexto.trim().isEmpty() || usuarioActual == null) return;

        LocalDate hoy = LocalDate.now();
        if (usuarioActual.getUltimaFechaMensaje() == null || 
            !hoy.getMonth().equals(usuarioActual.getUltimaFechaMensaje().getMonth())) {
            usuarioActual.setMensajesEnviadosUltimoMes(0);
        }

        Mensaje mensaje = new Mensaje(mensajeTexto, LocalDateTime.now(), usuarioActual, contacto);
        adaptadorMensaje.registrarMensaje(mensaje);

        usuarioActual.setMensajesEnviadosUltimoMes(usuarioActual.getMensajesEnviadosUltimoMes() + 1);
        usuarioActual.setUltimaFechaMensaje(hoy);
        adaptadorUsuario.modificarUsuario(usuarioActual);

        if (contacto instanceof Grupo) {
            Grupo grupo = (Grupo) contacto;
            for (ChatIndividual miembro : grupo.getMiembros()) {
                enviarMensajePrivadoAReceptor(miembro.getContacto(), mensaje);
            }
            grupo.enviarMensaje(mensaje);
            adaptadorGrupo.modificarGrupo(grupo);
        } else if (contacto instanceof ChatIndividual) {
            ChatIndividual chatEmisor = (ChatIndividual) contacto;
            Usuario receptor = chatEmisor.getContacto();
            chatEmisor.enviarMensaje(mensaje);
            enviarMensajePrivadoAReceptor(receptor, mensaje);
            adaptadorChatIndividual.modificarChatIndividual(chatEmisor);
        }
    }





    private void enviarMensajePrivadoAReceptor(Usuario receptor, Mensaje mensaje) {
    	ChatIndividual chatReceptor = receptor.getChatsIndividuales().stream()
    		    .filter(c -> c.getContacto().equals(usuarioActual))
    		    .findFirst()
    		    .orElse(null);


        if (chatReceptor == null) {

        	chatReceptor = new ChatIndividual(usuarioActual.getNumeroTelefono(),
                                              usuarioActual.getNumeroTelefono(),
                                              usuarioActual);
            receptor.addContacto(chatReceptor);
            adaptadorChatIndividual.registrarChatIndividual(chatReceptor);
        }

        chatReceptor.enviarMensaje(mensaje);
        adaptadorChatIndividual.modificarChatIndividual(chatReceptor); 
        adaptadorUsuario.modificarUsuario(receptor);  
    }





    public void enviarMensaje(Contacto contacto, int emoji) {
        if (contacto == null) return;

        Mensaje mensaje = new Mensaje(emoji, LocalDateTime.now(), usuarioActual, contacto);

        contacto.enviarMensaje(mensaje);

        adaptadorMensaje.registrarMensaje(mensaje);

        if (contacto instanceof ChatIndividual) {
            ChatIndividual chatIndividual = (ChatIndividual) contacto;

            Usuario receptor = chatIndividual.getContacto();

            ChatIndividual chatReceptor = receptor.getChatsIndividuales().stream()
                    .filter(c -> c.getnumeroTelefono().equals(usuarioActual.getNumeroTelefono()))
                    .findFirst()
                    .orElse(null);

            if (chatReceptor == null) {
                chatReceptor = new ChatIndividual(usuarioActual.getNombreCompleto(),
                                                  usuarioActual.getNumeroTelefono(), usuarioActual);
                receptor.addContacto(chatReceptor);
                adaptadorChatIndividual.registrarChatIndividual(chatReceptor);
            }

            chatReceptor.enviarMensaje(mensaje);

            adaptadorChatIndividual.modificarChatIndividual(chatReceptor);
            adaptadorUsuario.modificarUsuario(receptor);
        } else if (contacto instanceof Grupo) {
            adaptadorGrupo.modificarGrupo((Grupo) contacto);
        }

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
        grupo.setMiembros(miembros);

        adaptadorGrupo.modificarGrupo(grupo);

        for (ChatIndividual chat : miembros) {
            adaptadorUsuario.modificarUsuario(chat.getContacto()); // si es necesario
        }

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
    // Funciones de eliminación de objetos
    //-----------------------------------------------------
    
	public void eliminarContacto(Contacto c) {
	    usuarioActual.removeContacto(c);

	    if (c instanceof ChatIndividual) {
	        adaptadorChatIndividual.borrarChatIndividual((ChatIndividual) c);
	    } else if (c instanceof Grupo) {
	        Grupo grupo = (Grupo) c;
	        adaptadorGrupo.borrarGrupo(grupo);
	    }

	    adaptadorUsuario.modificarUsuario(usuarioActual);
	}



    public ChatIndividual getChatIndividual(String nombre) {
        if (usuarioActual == null || nombre == null || nombre.trim().isEmpty()) {
            return null; 
        }
        for (ChatIndividual chat : usuarioActual.getChatsIndividuales()) {
            if (chat.getNombreContacto().equalsIgnoreCase(nombre.trim())) {
                return chat; 
            }
        }

        return null;
    }

    
    
    public List<String[]> getContactosParaTabla() {
        if (usuarioActual == null) {
            return new ArrayList<>();
        }
        return usuarioActual.getChatsIndividuales().stream()
                .map(chat -> new String[]{chat.getNombreContacto(), chat.getnumeroTelefono()})
                .collect(Collectors.toList());
    }



    public Contacto getContactoPorNombre(String nombre) {
        ChatIndividual chat = getChatIndividual(nombre);
        if (chat != null) {
            return chat;  
        }

        Grupo grupo = getGrupoPorNombre(nombre);
        if (grupo != null) {
            return grupo;  
        }

        return null; 
    }

    



	public Grupo getGrupoPorNombre(String nombre) {
        return usuarioActual.getGrupos().stream()
                .filter(grupo -> grupo.getNombreContacto().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
	}

	//Mensajes enviados este mes
	
	public int obtenerMensajesEnviadosEsteMes() {
	    if (usuarioActual == null) return 0;
	    return usuarioActual.getMensajesEnviadosUltimoMes();
	}

	public void resetearContadoresMensuales() {
	    LocalDate hoy = LocalDate.now();
	    List<Usuario> todosUsuarios = CatalogoUsuarios.INSTANCE.getUsuarios();
	    for (Usuario usuario : todosUsuarios) {
	        if (usuario.getUltimaFechaMensaje() == null || 
	            !hoy.isEqual(usuario.getUltimaFechaMensaje())) {
	            usuario.setMensajesEnviadosUltimoMes(0);
	            usuario.setUltimaFechaMensaje(hoy);
	            adaptadorUsuario.modificarUsuario(usuario);
	        }
	    }
	}
	
	
	
	
	// Descuentos
	
	
	public double calcularDescuento(Usuario usuario) {
        double totalDescuento = 0;

        Descuento descuentoFecha = FactoriaDescuento.crearDescuento(
            "modelo.Descuento.DescuentoPorFecha", 10.0, "2025-01-01", "2025-07-31"
        );
        if (descuentoFecha.esAplicable(usuario)) {
            totalDescuento += descuentoFecha.getDescuento(usuario);
            estadoDescuentoFecha = "Descuento por Fecha: ✔ Aplicado (10%)";
        } else {
            estadoDescuentoFecha = "Descuento por Fecha: ✖ No válido en esta fecha";
        }

        Descuento descuentoMensajes = FactoriaDescuento.crearDescuento(
            "modelo.Descuento.DescuentoPorMensaje", 15.0, 20
        );
        if (descuentoMensajes.esAplicable(usuario)) {
            totalDescuento += descuentoMensajes.getDescuento(usuario);
            estadoDescuentoMensajes = "Descuento por Mensajes: ✔ Aplicado (15%)";
        } else {
            estadoDescuentoMensajes = "Descuento por Mensajes: ✖ No cumple con los mensajes mínimos";
        }

        double precioOriginal = 24.99;
        return precioOriginal * (1 - totalDescuento / 100);
    }

    public String getEstadoDescuentoFecha() {
        return estadoDescuentoFecha;
    }

    public String getEstadoDescuentoMensajes() {
        return estadoDescuentoMensajes;
    }

    public void confirmarPago(Usuario usuario) {
        usuario.setPremium(true);
        adaptadorUsuario.modificarUsuario(usuario);
     }


    // Funciones de búsqueda de mensajes
    
    
    public List<Mensaje> buscarMensajes(String texto, String telefono, String nombreContacto) {
        List<Mensaje> mensajes = obtenerTodosLosMensajes(); 

        FiltroComposite filtroCompuesto = new FiltroComposite();

        if (!texto.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroTexto(texto));
        }
        if (!telefono.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroNumero(telefono));
        }
        if (!nombreContacto.isEmpty()) {
            filtroCompuesto.agregarFiltro(new FiltroNombre(nombreContacto));
        }

        return filtroCompuesto.filtrar(mensajes); 
    }

    
}