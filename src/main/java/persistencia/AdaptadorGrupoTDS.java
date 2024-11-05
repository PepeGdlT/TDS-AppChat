package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


public class AdaptadorGrupoTDS implements IAdaptadorGrupoDAO {

	private ServicioPersistencia servPersistencia;
	private static AdaptadorGrupoTDS unicaInstancia = null;
	
	public static AdaptadorGrupoTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			return new AdaptadorGrupoTDS();
		} else {
			return unicaInstancia;
		}
	}
	
	private AdaptadorGrupoTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	@Override
	public void registrarGrupo(Grupo grupo) {
		Entidad eGrupo = null;
		boolean existe = true;
		try {
			eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		noMensajes(grupo.getMensajesEnviados());
		noContactos(grupo.getListaMiembros());
		noAdmin(grupo.getAdministrador());
		
		eGrupo = new Entidad();
		eGrupo.setNombre("grupo");
		eGrupo.setPropiedades(new ArrayList<Propiedad>(
			Arrays.asList(	
				new Propiedad("nombre", grupo.getNombre()),
				new Propiedad("administrador", String.valueOf(grupo.getAdministrador().getCodigo())),
				new Propiedad("codigo", String.valueOf(grupo.getCodigo())),
				new Propiedad("miembros", obtenerCodigosMiembros(grupo.getListaMiembros())),
				new Propiedad("mensajes", obtenerCodigosMensajes(grupo.getMensajesEnviados()))
			)));
		
		PoolDAO.getUnicaInstancia().addObjeto(grupo.getCodigo(), eGrupo);
		
		eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		grupo.setCodigo(eGrupo.getId());
		
	}

	@Override
	public void borrarGrupo(Grupo grupo) {
        Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
        
        AdaptadorMensajeTDS adaptadorMensaje = AdaptadorMensajeTDS.getUnicaInstancia();
		for (Mensaje mensaje : grupo.getMensajesEnviados()) {
			adaptadorMensaje.borrarMensaje(mensaje);
		}
		
		eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		servPersistencia.borrarEntidad(eGrupo);
		
		if (PoolDAO.getUnicaInstancia().contiene(grupo.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObjeto(grupo.getCodigo());
		}
		
		
	}


	@Override
	public void modificarGrupo(Grupo grupo) {
		Entidad eGrupo = servPersistencia.recuperarEntidad(grupo.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "nombre");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "nombre",
				grupo.getNombre());
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "administrador");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "administrador",
				String.valueOf(grupo.getAdministrador().getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "codigo");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "codigo",
				String.valueOf(grupo.getCodigo()));
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "miembros");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "miembros", 
				obtenerCodigosMiembros(grupo.getListaMiembros()));
		servPersistencia.eliminarPropiedadEntidad(eGrupo, "mensajes");
		servPersistencia.anadirPropiedadEntidad(eGrupo, "mensajes", 
				obtenerCodigosMensajes(grupo.getMensajesEnviados()));
	}

	@Override
	public Grupo recuperarGrupo(int codigo) {
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Grupo) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		
		Entidad eGrupo;
		String nombre;
		int codigoGrupo;
        
		eGrupo = servPersistencia.recuperarEntidad(codigo);
		
		nombre = servPersistencia.recuperarPropiedadEntidad(eGrupo, "nombre");
		
		Grupo grupo = new Grupo(nombre, new ArrayList<Mensaje>(), new ArrayList<ChatIndividual>(), null);	
		grupo.setCodigo(codigo);
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, grupo);
		
		List<Mensaje> mensajes = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, "mensajes"));
		for (Mensaje m : mensajes) {
			grupo.enviarMensaje(m);
		}
		
		List<ChatIndividual> miembros = obtenerMiembrosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eGrupo, "miembros"));
		for (ChatIndividual c : miembros) {
			grupo.addMiembro(c);
		}
		
		grupo.setAdministrador(obtenerAdministradorDesdeCodigo(servPersistencia.recuperarPropiedadEntidad(eGrupo, "administrador")));
		
		return grupo;
		
	}



	@Override
	public List<Grupo> recuperarTodosGrupos() {
		List<Grupo> grupos = new ArrayList<Grupo>();
        
        List<Entidad> entidades = servPersistencia.recuperarEntidades("grupo");
        for (Entidad eGrupo : entidades) {
            grupos.add(recuperarGrupo(eGrupo.getId()));
        }
        
        return grupos;
	}

	
	
	//----------------------------------------------------------------
	// Funciones auxiliares
	//----------------------------------------------------------------
	
	private void noMensajes(List<Mensaje> messages) {
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		messages.stream()
		.forEach(m -> adaptadorMensajes.registrarMensaje(m));
	}

	private void noContactos(List<ChatIndividual> contacts) {
		AdaptadorChatIndividualTDS adaptadorContactos = AdaptadorChatIndividualTDS.getUnicaInstancia();
		contacts.stream()
			.forEach(c -> adaptadorContactos.registrarChatIndividual(c));
	}

	private void noAdmin(Usuario admin) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		adaptadorUsuarios.registrarUsuario(admin);
	}
	
	
	//----------------------------------------------------------------
	// Obtener codigos -> miembros y mensajes
	//----------------------------------------------------------------
	
	
	
	private String obtenerCodigosMiembros(List<ChatIndividual> list) {
		return list.stream()
				.map(c -> String.valueOf(c.getCodigo()))
				.reduce("", (l, c) -> l + c + " ")
				.trim();
	}


	
	private String obtenerCodigosMensajes(List<Mensaje> list) {
		return list.stream()
                .map(m -> String.valueOf(m.getCodigo()))
				.reduce("", (l, m) -> l + m + " ")
                .trim();
	}


	//----------------------------------------------------------------
	// Obtener mensajes y miembros -> codigos
	//----------------------------------------------------------------
	
	
	private List<Mensaje> obtenerMensajesDesdeCodigos(String recuperarPropiedadEntidad) {
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		StringTokenizer strTok = new StringTokenizer(recuperarPropiedadEntidad, " ");
		AdaptadorMensajeTDS adaptadorMensajes = AdaptadorMensajeTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			mensajes.add(adaptadorMensajes.recuperarMensaje(Integer.valueOf((String) strTok.nextElement())));
		}
		return mensajes;
	}
	

	private List<ChatIndividual> obtenerMiembrosDesdeCodigos(String recuperarPropiedadEntidad) {
		List<ChatIndividual> miembros = new ArrayList<ChatIndividual>();
		StringTokenizer strTok = new StringTokenizer(recuperarPropiedadEntidad, " ");
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			miembros.add(
					adaptadorChatIndividual.recuperarChatIndividual(Integer.valueOf((String) strTok.nextElement())));
		}
		return miembros;
	}
	

	private Usuario obtenerAdministradorDesdeCodigo(String recuperarPropiedadEntidad) {
		AdaptadorUsuarioTDS adaptadorUsuarios = AdaptadorUsuarioTDS.getUnicaInstancia();
		return adaptadorUsuarios.recuperarUsuario(Integer.valueOf(recuperarPropiedadEntidad));
	}

	
}
