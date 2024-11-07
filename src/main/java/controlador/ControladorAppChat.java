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
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorChatIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;




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
 	
	
	public boolean registrarUsuario(String nombreCompleto, int numeroTelefono, String email, String contrasena, String saludo,  LocalDate fechaNacimiento) {
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fechaNacimiento);
		
		//TODO : Comprobar si el usuario ya existe
		
		adaptadorUsuario.registrarUsuario(usuario);
		catalogoUsuarios.addUsuario(usuario);
		return true;
	}

	public boolean iniciarSesion(int numeroTelefono, String contrasena) {
		Usuario usuario = catalogoUsuarios.getUsuarioTelefono(numeroTelefono);
		if (usuario != null && usuario.getContrasena().equals(contrasena)) {
			usuarioActual = usuario;
			return true;
		}
		return false;
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
	
	public List<Contacto> getContactos() {
		if (usuarioActual == null) {
			return new LinkedList<Contacto>();
		}
		Usuario usuario = adaptadorUsuario.recuperarUsuario(usuarioActual.getCodigo());
		return usuarioActual.getContactos();
	}
	

	public boolean existeUsuario(int numeroTelefono) {
		return CatalogoUsuarios.getUnicaInstancia().getUsuarioTelefono(numeroTelefono) != null;
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
	
	public List<Grupo> getGruposAdminUsuarioActual() {
		return usuarioActual.getGruposAdmin();
	}
	
	
	public String getNombreContacto(Usuario usuario) {
		return null;
		//TODO: obtener nombre de contacto
	}

	public Optional<Contacto> getContacto(String nombre) {
	    return Optional.empty();
	    //TODO: obtener contacto
	}

	
	private Optional<Usuario> getUser(String name) {
        return Optional.empty();
	}
	
	
	public List<Mensaje> buscarMensajes(String emisor, LocalDateTime fechaInicio, LocalDateTime fechaFin, String text) {
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
