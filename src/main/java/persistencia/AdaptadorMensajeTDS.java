package persistencia;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {

	private static final String MENSAJE = "mensaje";
	private static final String TEXTO = "texto";
	private static final String EMISOR = "emisor";
	private static final String RECEPTOR = "receptor";
	private static final String HORA = "hora";
	private static final String EMOTICONO = "emoticono";
	
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorMensajeTDS unicaInstancia = null;
	
	public static AdaptadorMensajeTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			return new AdaptadorMensajeTDS();
		} else {
			return unicaInstancia;
		}
	}
	
	private AdaptadorMensajeTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	@Override
	public void registrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = null;
		boolean existe = true;
		try {
			eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		noUser(mensaje.getEmisor());
		noReceptores(mensaje.getReceptor());
		
		
		
		eMensaje = new Entidad();
		eMensaje.setNombre(MENSAJE);
		eMensaje.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad(TEXTO, mensaje.getTexto()),
						new Propiedad(EMISOR, String.valueOf(mensaje.getEmisor().getCodigo())),
						new Propiedad(RECEPTOR, String.valueOf(mensaje.getReceptor().getCodigo())),
						new Propiedad(HORA, mensaje.getHora().toString()),
						new Propiedad(EMOTICONO, String.valueOf(mensaje.getEmoticono()
				)))));
		
		PoolDAO.getUnicaInstancia().addObjeto(mensaje.getCodigo(), eMensaje);
		
		eMensaje = servPersistencia.registrarEntidad(eMensaje);
		mensaje.setCodigo(eMensaje.getId());
		
	}

	@Override
	public void borrarMensaje(Mensaje mensaje) {
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		
		servPersistencia.borrarEntidad(eMensaje);
		
		if (PoolDAO.getUnicaInstancia().contiene(mensaje.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObjeto(mensaje.getCodigo());
		}
	}

	@Override
	public void modificarMensaje(Mensaje mensaje) {
		
		Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eMensaje, TEXTO);
		servPersistencia.anadirPropiedadEntidad(eMensaje, TEXTO,
				mensaje.getTexto());
		servPersistencia.eliminarPropiedadEntidad(eMensaje, EMISOR);
		servPersistencia.anadirPropiedadEntidad(eMensaje, EMISOR,
				String.valueOf(mensaje.getEmisor().getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eMensaje, RECEPTOR);
		servPersistencia.anadirPropiedadEntidad(eMensaje, RECEPTOR,
				String.valueOf(mensaje.getReceptor().getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eMensaje, HORA);
		servPersistencia.anadirPropiedadEntidad(eMensaje, HORA,
				mensaje.getHora().toString());
		servPersistencia.eliminarPropiedadEntidad(eMensaje, EMOTICONO);
		servPersistencia.anadirPropiedadEntidad(eMensaje, EMOTICONO, 
				String.valueOf(mensaje.getEmoticono()));
	}

	@Override
	public Mensaje recuperarMensaje(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Mensaje) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eMensaje;
		String texto;
		Usuario emisor;
		ChatIndividual receptor;
		LocalDateTime hora;
		int emoticono;
		
		eMensaje = servPersistencia.recuperarEntidad(codigo);
		
		texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
		hora = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, HORA));
		emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
		
		Mensaje mensaje = new Mensaje(texto, emoticono, hora);
		mensaje.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, mensaje);
		
		AdaptadorUsuarioTDS adaptadorUsuario = AdaptadorUsuarioTDS.getUnicaInstancia();
		emisor = adaptadorUsuario.recuperarUsuario(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMISOR)));
		mensaje.setEmisor(emisor);
		
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		receptor = adaptadorChatIndividual.recuperarChatIndividual(Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, RECEPTOR)));
		mensaje.setReceptor(receptor);
		
		return mensaje;
	}

	@Override
	public List<Mensaje> recuperarTodosMensajes() {
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		List<Entidad> entidades = servPersistencia.recuperarEntidades(MENSAJE);

		for (Entidad eMensaje : entidades) {
			mensajes.add(recuperarMensaje(eMensaje.getId()));
		}

		return mensajes;
	}
	
	
	
	// --------------------------------------------------------------------------------
	// METODOS AUXILIARES
	// --------------------------------------------------------------------------------
	
	

	private void noReceptores(List<Contacto> contactos) {
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		AdaptadorGrupoTDS adaptadorGrupos = AdaptadorGrupoTDS.getUnicaInstancia();
		contactos.stream().forEach(c -> {
			if (c instanceof ChatIndividual) {
				adaptadorChatIndividual.registrarChatIndividual((ChatIndividual) c);
			} else {
				adaptadorGrupos.registrarGrupo((Grupo) c);
			}
		});
	}
	
	private void noReceptores(Contacto contacto) {
		LinkedList<Contacto> contactos = new LinkedList<>();
        contactos.add(contacto);
        noReceptores(contactos);
	}
	


	private void noUser(Usuario emisor) {
		AdaptadorUsuarioTDS.getUnicaInstancia().registrarUsuario(emisor);
	}

	

}
