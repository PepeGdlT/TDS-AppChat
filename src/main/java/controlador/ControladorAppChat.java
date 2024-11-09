package controlador;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.toedter.calendar.JCalendar;

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


 // CAMBIAR ESTO A ENUM, Y TRATAR PATRON EXPERTO

// CAMBIAR DE TELEFONO A CODIGO

public class ControladorAppChat {
	
	private static ControladorAppChat unicaInstancia;
	
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	private IAdaptadorChatIndividualDAO adaptadorChatIndividual;
	private IAdaptadorMensajeDAO adaptadorMensaje;
	private IAdaptadorGrupoDAO adaptadorGrupo;
	
	private  CatalogoUsuarios catalogoUsuarios;
	
	private Usuario usuarioActual;
	private Contacto contactoActual;
	
	private ControladorAppChat() {
		inicializarAdaptadores();
		inicializarCatalogos();
	}
	
	
	public static ControladorAppChat getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new ControladorAppChat();
		}
		return unicaInstancia;
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
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
	}
	
	
	
	//-----------------------------------------------------
	// Funciones de control de usuario actual
	//-----------------------------------------------------
 	
	
	public boolean registrarUsuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
		
		if (existeUsuario(numeroTelefono)) return false;
		
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);

		adaptadorUsuario.registrarUsuario(usuario);
		catalogoUsuarios.addUsuario(usuario);
		return true;
	}


	public boolean existeUsuario(String numeroTelefono) {
		return CatalogoUsuarios.getUnicaInstancia().encontrarUsuario(numeroTelefono) != null;
	}
	


	public boolean iniciarSesion(String phone, String contrasena) {

		Usuario usuario = catalogoUsuarios.encontrarUsuario(phone);
		if (usuario != null && usuario.getContrasena().equals(contrasena)) {
			this.usuarioActual = usuario;
			return true;
		}
		return false;
	}
	
	public boolean borrarUsuario(Usuario usuario) {
		if(existeUsuario(usuario.getNumeroTelefono())) return false;
		
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
			return new LinkedList<ChatIndividual>();
		}
		return usuarioActual.getChatIndividuales();
	}
	
	public List<Grupo> getGrupos() {
		if (usuarioActual == null) {
			return new LinkedList<Grupo>();
		}
		return usuarioActual.getGrupos();
	}
	
	
	

	
	//-----------------------------------------------------
	// Funciones de control de chats
	//-----------------------------------------------------
	
	
	public List<Mensaje> getMensajes(Contacto contacto) {
		if (contacto instanceof ChatIndividual && !((ChatIndividual) contacto).isUser(usuarioActual)) {
			return Stream
					.concat(contacto.getMensajesEnviados().stream(),
							contacto.getMensajesRecibidos(Optional.of(usuarioActual)).stream())
					.sorted().collect(Collectors.toList());
		} else {
			return contacto.getMensajesEnviados().stream().sorted().collect(Collectors.toList());
		}
	}
	
	public Mensaje getUltimoMensaje(Contacto contacto) {
		List<Mensaje> mensajes = getMensajes(contacto);
		if (mensajes.isEmpty()) {
			return null;
		}
		return mensajes.get(mensajes.size() - 1);
	}
	
	public void enviarMensaje(Contacto contacto, String mensaje) {
		//TODO: enviar mensaje texto
	}

	public void enviarMensaje(Contacto contacto, int emoji) {
		//TODO: enviar mensaje emoji
	}

	public void setChatActual(Contacto contacto) {
		//TODO: setear chat actual
	}
	
	public boolean isAdmin(Grupo grupo) {
		return false;
		//TODO: comprobar si el usuario actual es admin del grupo
	}
	
	
	//-----------------------------------------------------
	// Funciones de creacion de objetos
	//-----------------------------------------------------
	
		
	public ChatIndividual crearChatIndividual(Usuario usuario) {
		return null;
		//TODO: crear chat individual
	}
	
	public Grupo crearGrupo(String nombre, List<ChatIndividual> participantes) {
		return null;
		//TODO: crear grupo
	}

	
	//-----------------------------------------------------
	// Funciones de modificacion de objetos
	//-----------------------------------------------------
	
	
	
	public Grupo modificarGrupo(Grupo grupo, String nombre, List<ChatIndividual> participantes) {
	    return null;
	    //TODO: modificar grupo
	}
	

	
	
	//-----------------------------------------------------
	// Funciones de get de objetos
	//-----------------------------------------------------

	
	public String getNombreContacto(Usuario usuario) {
		return null;
		//TODO: obtener nombre de contacto
	}

	public Optional<Contacto> getContacto(String nombre) {
	    return Optional.empty();
	    //TODO: obtener contacto
	}

	
	private Optional<Usuario> getUser(String name) {
        //TODO: obtener usuario
		return null;
	}
	
	
	public List<Mensaje> buscarMensajes(String emisor, LocalDateTime fechaInicio, LocalDateTime fechaFin, String text) {
	    //TODO: buscar mensajes
		return null;
	}
	
	
	//-----------------------------------------------------
	// Funciones de eliminacion de objetos
	//-----------------------------------------------------
	
	public void deleteContacto(Contacto contacto) {
		//TODO: eliminar contacto
	}
	
	
	public void deleteChatIndividual(ChatIndividual chat) {
		//TODO: eliminar contacto
	}



	
}
