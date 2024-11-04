package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.apps.AppChat.Message;
import umu.tds.apps.AppChat.User;

public class AdaptadorChatIndividualTDS implements IAdaptadorChatIndividualDAO {

	
	
	private static AdaptadorChatIndividualTDS unicaInstancia = null;
	private static ServicioPersistencia servPersistencia;
	
	
	public static AdaptadorChatIndividualTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorChatIndividualTDS();
		}
		return unicaInstancia;
	}
	
	private AdaptadorChatIndividualTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
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
		
		eChat = new Entidad();
		eChat.setNombre("chat");
		eChat.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("nombre", chat.getNombre()),
						new Propiedad("numeroTelefono", String.valueOf(chat.getNumeroTelefono())),
                        new Propiedad("usuario", String.valueOf(chat.getUsuario().getCodigo())),
                        new Propiedad("mensajesEnviados", obtenerCodigosMensajesEnviados(chat.getMensajesEnviados()))
                )));
		
		PoolDAO.getUnicaInstancia().addObjeto(chat.getCodigo(), chat);
		
		eChat = servPersistencia.registrarEntidad(eChat);
		
		chat.setCodigo(eChat.getId());
		
	}



	@Override
	public void borrarChatIndividual(ChatIndividual chat) {
		
		Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
		
		AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getInstancia();
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
		
		servPersistencia.eliminarPropiedadEntidad(eChat, "nombre");
		servPersistencia.anadirPropiedadEntidad(eChat, "nombre",
				chat.getNombre());
		servPersistencia.eliminarPropiedadEntidad(eChat, "numeroTelefono");
		servPersistencia.anadirPropiedadEntidad(eChat, "numeroTelefono",
				String.valueOf(chat.getNumeroTelefono()));
		servPersistencia.eliminarPropiedadEntidad(eChat, "usuario");
		servPersistencia.anadirPropiedadEntidad(eChat, "usuario", 
				String.valueOf(chat.getUsuario().getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eChat, "mensajesEnviados");
		servPersistencia.anadirPropiedadEntidad(eChat, "mensajesEnviados", 
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
		nombre = servPersistencia.recuperarPropiedadEntidad(eChat, "nombre");
		numeroTelefono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, "numeroTelefono"));
		
		ChatIndividual chat = new ChatIndividual(nombre, numeroTelefono, new LinkedList<Mensaje>(), null);
		chat.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);
		
		LinkedList<Mensaje> mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eChat, "mensajesEnviados"));
		for (Mensaje m : mensajes) chat.enviarMensaje(m);
		
		chat.setUsuario(obtenerUsuarioDesdeCodigo(servPersistencia.recuperarPropiedadEntidad(eChat, "usuario")));
	
		return chat;
	}
	



	@Override
	public List<ChatIndividual> recuperarTodosChatsIndividuales() {
		List<ChatIndividual> chats = new LinkedList<ChatIndividual>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades("chat");
		for (Entidad eChat : entidades) {
			chats.add(recuperarChatIndividual(eChat.getId()));
		}
		return chats;
	}
	
	private void registrarSiNoExisteUser(Usuario usuario) {
		//TODO
	}

	private void registrarSiNoExistenMensajes(List<Mensaje> mensaje) {
		// TODO
	}
	
	
	
	private Usuario obtenerUsuarioDesdeCodigo(String recuperarPropiedadEntidad) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private LinkedList<Mensaje> obtenerMensajesDesdeCodigos(String recuperarPropiedadEntidad) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private String obtenerCodigosMensajesEnviados(List<Mensaje> mensajesEnviados) {
        
		return "";
		
	}
	
	
}
