package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorChatIndividualTDS implements IAdaptadorChatIndividualDAO {

	
	private static final String CHAT = "chat";
	private static final String NOMBRE = "nombre";
	private static final String NUMERO_TELEFONO = "numeroTelefono";
	private static final String USUARIO = "usuario";
	private static final String MENSAJES_ENVIADOS = "mensajesEnviados";
	
	
	private static AdaptadorChatIndividualTDS unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	// Singleton
	public static AdaptadorChatIndividualTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorChatIndividualTDS();
		}
		return unicaInstancia;
	}
	
	private AdaptadorChatIndividualTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	// -----------------------------------------------------------------------------------------
	// METODOS DE PERSISTENCIA
	// -----------------------------------------------------------------------------------------
	
	
	@Override
	public void registrarChatIndividual(ChatIndividual chat) {
		Entidad eChat = null;
		boolean existe = true;
		try {
			eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		
		noMensajes(chat.getMensajesEnviados());
		noUser(chat.getUsuario());
		
		
		eChat = new Entidad();
		eChat.setNombre(CHAT);
		eChat.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad(NOMBRE, chat.getNombre()),
						new Propiedad(NUMERO_TELEFONO, String.valueOf(chat.getNumeroTelefono())),
                        new Propiedad(USUARIO, String.valueOf(chat.getUsuario().getCodigo())),
                        new Propiedad(MENSAJES_ENVIADOS, obtenerCodigosMensajesEnviados(chat.getMensajesEnviados()))
                )));
		
		PoolDAO.getUnicaInstancia().addObjeto(chat.getCodigo(), chat);
		
		eChat = servPersistencia.registrarEntidad(eChat);
		
		chat.setCodigo(eChat.getId());
		
	}



	@Override
	public void borrarChatIndividual(ChatIndividual chat) {
		
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		
		AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
		for (Mensaje mensaje : chat.getMensajesEnviados()) {
			adaptadorMensaje.borrarMensaje(mensaje);
		}
		
		servPersistencia.borrarEntidad(eChat);
		
		if (PoolDAO.getUnicaInstancia().contiene(chat.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObjeto(chat.getCodigo());
		}
	}

	@Override
	public void modificarChatIndividual(ChatIndividual chat) {
		
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eChat, NOMBRE);
		servPersistencia.anadirPropiedadEntidad(eChat, NOMBRE,
				chat.getNombre());
		servPersistencia.eliminarPropiedadEntidad(eChat, NUMERO_TELEFONO);
		servPersistencia.anadirPropiedadEntidad(eChat, NUMERO_TELEFONO,
				String.valueOf(chat.getNumeroTelefono()));
		servPersistencia.eliminarPropiedadEntidad(eChat, USUARIO);
		servPersistencia.anadirPropiedadEntidad(eChat, USUARIO, 
				String.valueOf(chat.getUsuario().getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eChat, MENSAJES_ENVIADOS);
		servPersistencia.anadirPropiedadEntidad(eChat, MENSAJES_ENVIADOS, 
				obtenerCodigosMensajesEnviados(chat.getMensajesEnviados()));
		
		
	}

	@Override
	public ChatIndividual recuperarChatIndividual(int codigo) {
		
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (ChatIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eChat;
		String nombre;
		int numeroTelefono;
		int codigoUsuario;
		List<Mensaje> mensajesEnviados;
		
		eChat = servPersistencia.recuperarEntidad(codigo);
		nombre = servPersistencia.recuperarPropiedadEntidad(eChat, NOMBRE);
		numeroTelefono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, NUMERO_TELEFONO));
		
		ChatIndividual chat = new ChatIndividual(nombre, numeroTelefono, new LinkedList<Mensaje>(), null);
		chat.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);
		
		List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eChat, MENSAJES_ENVIADOS));
		for (Mensaje m : mensajes) chat.enviarMensaje(m);
		
		chat.setUsuario(obtenerUsuarioDesdeCodigo(servPersistencia.recuperarPropiedadEntidad(eChat, USUARIO)));
	
		return chat;
	}
	



	@Override
	public List<ChatIndividual> recuperarTodosChatsIndividuales() {
		List<ChatIndividual> chats = new LinkedList<ChatIndividual>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades(CHAT);
		for (Entidad eChat : entidades) {
			chats.add(recuperarChatIndividual(eChat.getId()));
		}
		return chats;
	}
	
	//-----------------------------------------------------------------------------------------
	// METODOS AUXILIARES
	//-----------------------------------------------------------------------------------------
	
	private void noMensajes(List<Mensaje> messages) {
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		messages.stream().forEach(m -> adaptadorMensajes.registrarMensaje(m));
	}

	private void noUser(Usuario admin) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		adaptadorUsuarios.registrarUsuario(admin);
	}
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CONTACTOS Y GRUPOS -> CODIGOS
	// -----------------------------------------------------------------------------------------
	
	private Usuario obtenerUsuarioDesdeCodigo(String codigo) {
		AdaptadorUsuarioTDS adaptadorUsuario = AdaptadorUsuarioTDS.getUnicaInstancia();
		return adaptadorUsuario.recuperarUsuario(Integer.parseInt(codigo));
	}

	
	private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
		List<Mensaje> mensajes = new LinkedList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			mensajes.add(adaptadorMensaje.recuperarMensaje((Integer.valueOf((String) strTok.nextElement()))));

		}
		return mensajes;
	}
	
	
	
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CODIGOS -> CONTACTOS Y GRUPOS
	// -----------------------------------------------------------------------------------------
	
	private String obtenerCodigosMensajesEnviados(List<Mensaje> mensajesEnviados) {
		return mensajesEnviados.stream()
				.map(m -> String.valueOf(m.getCodigo()))
				.reduce("", (l, m) -> l + m + " ")
				.trim();
	}


	
}
