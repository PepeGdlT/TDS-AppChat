package persistencia;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.persistence.Entity;

import org.h2.engine.User;

import com.formdev.flatlaf.json.ParseException;
import com.toedter.calendar.JCalendar;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;




public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {
	
	
	private static final String USUARIO = "usuario";
	private static final String NOMBRECOMPLETO = "nombreCompleto";
	private static final String NUMEROTELEFONO = "numeroTelefono";
	private static final String EMAIL = "email";
	private static final String CONTRASENA = "contrasena";
	private static final String SALUDO = "saludo";
	private static final String FOTOPERFILURL = "fotoPerfilURL";
	private static final String FECHANACIMIENTO = "fechaNacimiento";
	private static final String CHATS_INDIVIDUALES = "chatsIndividuales";
	private static final String PREMIUM = "Premium";
	private static final String GRUPOS = "grupos";
	
	
	

	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;
	private SimpleDateFormat dateFormat;

	public static AdaptadorUsuarioTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuarioTDS();
		}
		return unicaInstancia;
	}
	

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}
	
	
	

	public void registrarUsuario(Usuario usuario) {

		if (existeUsuario(usuario.getCodigo())) return;
		
		Entidad eUsuario = null;	
		
		noChats(usuario.getChatIndividuales());
		noGrupos(usuario.getGrupos());
		
		
		eUsuario = new Entidad();
		eUsuario.setNombre(USUARIO);
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad(NOMBRECOMPLETO, usuario.getNombreCompleto()),
                        new Propiedad(NUMEROTELEFONO, usuario.getNumeroTelefono()),
                        new Propiedad(EMAIL, usuario.getEmail()),
                        new Propiedad(CONTRASENA, usuario.getContrasena()),
                        new Propiedad(SALUDO, usuario.getSaludo()),
                        new Propiedad(FOTOPERFILURL, usuario.getFotoPerfilURL()),
                        new Propiedad(FECHANACIMIENTO, usuario.getFechaNacimiento()),
                        new Propiedad(CHATS_INDIVIDUALES, obtenerCodigosChatIndividual(usuario.getChatIndividuales())),
                        new Propiedad(PREMIUM, String.valueOf(usuario.isPremium())),
                        new Propiedad(GRUPOS, obtenerCodigosGrupo(usuario.getGrupos()))
					    )));
		

		usuario.setCodigo(eUsuario.getId());
		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);
		
	}

	public void borrarUsuario(Usuario usuario) {
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		AdaptadorGrupoTDS adaptadorGrupo = AdaptadorGrupoTDS.getUnicaInstancia();
	
		for (ChatIndividual chat : usuario.getChatIndividuales()) {
			adaptadorChatIndividual.borrarChatIndividual(chat);
		}
		
		for (Grupo grupo : usuario.getGrupos()) {
			adaptadorGrupo.borrarGrupo(grupo);
		}
		
		
		servPersistencia.borrarEntidad(eUsuario);
		
		// Si esta en el Pool tambien se borra del pool
		if (PoolDAO.getUnicaInstancia().contiene(usuario.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObjeto(usuario.getCodigo());
		}
		
	}

	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eUsuario, NOMBRECOMPLETO);
		servPersistencia.anadirPropiedadEntidad(eUsuario, NOMBRECOMPLETO,
				usuario.getNombreCompleto());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, NUMEROTELEFONO);
		servPersistencia.anadirPropiedadEntidad(eUsuario, NUMEROTELEFONO,
				usuario.getNumeroTelefono());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, EMAIL);
		servPersistencia.anadirPropiedadEntidad(eUsuario, EMAIL,
				usuario.getEmail());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, CONTRASENA);
		servPersistencia.anadirPropiedadEntidad(eUsuario, CONTRASENA, 
				usuario.getContrasena());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, SALUDO);
		servPersistencia.anadirPropiedadEntidad(eUsuario, SALUDO,
				usuario.getSaludo());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, FOTOPERFILURL);
		servPersistencia.anadirPropiedadEntidad(eUsuario, FOTOPERFILURL,
				usuario.getFotoPerfilURL());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, FECHANACIMIENTO);
		servPersistencia.anadirPropiedadEntidad(eUsuario, FECHANACIMIENTO,
				dateFormat.format(usuario.getFechaNacimiento()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES);
		servPersistencia.anadirPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES, 
				obtenerCodigosChatIndividual(usuario.getChatIndividuales()));

		servPersistencia.eliminarPropiedadEntidad(eUsuario,PREMIUM);
		servPersistencia.anadirPropiedadEntidad(eUsuario, PREMIUM,
				String.valueOf(usuario.isPremium()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, GRUPOS);
		servPersistencia.anadirPropiedadEntidad(eUsuario, GRUPOS,
				obtenerCodigosGrupo(usuario.getGrupos()));
		
		
		
	}

	public Usuario recuperarUsuario(int codigo) {
		
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		

		
		Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
	    String nombreCompleto = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRECOMPLETO);
	    String numeroTelefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, NUMEROTELEFONO);
	    String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
	    String contrasena = servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTRASENA);
	    String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, SALUDO);
	    String fotoPerfilURL = servPersistencia.recuperarPropiedadEntidad(eUsuario, FOTOPERFILURL);
	    boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
	    String fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHANACIMIENTO);
	    
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL,fechaNacimiento);
		usuario.setPremium(premium);
		usuario.setCodigo(codigo);
		
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		List<ChatIndividual> chats = obtenerChatsDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES));
		for (ChatIndividual c : chats) usuario.addChat(c);
		
		List<Grupo> grupos = obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS));
		for (Grupo g : grupos) usuario.addGrupo(g);

		usuario.setChatIndividuales(obtenerChatsDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES)));
		
		
		return usuario;
		
	}

	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<Usuario>();
		System.out.println("Recuperando todos los usuarios");
        List<Entidad> eUsuarios = servPersistencia.recuperarEntidades(USUARIO);
        for (Entidad eUsuario : eUsuarios) {
        	System.out.println("Recuperando usuario -> " + eUsuario.getId());
            usuarios.add(recuperarUsuario(eUsuario.getId()));
        }
        return usuarios;
	}
	
	//-----------------------------------------------------------------------------------------
	// FUNCIONES AUXILIARES
	//-----------------------------------------------------------------------------------------
	

	private void noGrupos(List<Grupo> grupos) {
		AdaptadorGrupoTDS adaptadorGA = AdaptadorGrupoTDS.getUnicaInstancia();
		grupos.stream().forEach(g -> adaptadorGA.registrarGrupo(g));
	}

	private void noChats(List<ChatIndividual> chats) {
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		chats.stream().forEach(c -> {
				adaptadorChatIndividual.registrarChatIndividual((ChatIndividual) c);
		});
	}
	
	private boolean existeUsuario(int codigo) {
	    try {
	        return servPersistencia.recuperarEntidad(codigo) != null;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CONTACTOS Y GRUPOS -> CODIGOS
	// -----------------------------------------------------------------------------------------
	
	
	
	private List<Grupo> obtenerGruposDesdeCodigos(String codigos) {
		List<Grupo> grupos = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorGrupoTDS adaptadorG = AdaptadorGrupoTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			grupos.add(adaptadorG.recuperarGrupo(Integer.valueOf((String) strTok.nextElement())));
		}
		return grupos;
	}

	private List<ChatIndividual> obtenerChatsDesdeCodigos(String codigos) {
		List<ChatIndividual> chats = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorChatIndividualTDS adaptadorC = AdaptadorChatIndividualTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			chats.add(adaptadorC.recuperarChatIndividual((Integer.valueOf((String) strTok.nextElement()))));
		}
		return chats;
	}
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CODIGOS -> CONTACTOS Y GRUPOS
	// -----------------------------------------------------------------------------------------
	
	
	private String obtenerCodigosGrupo(List<Grupo> list) {
		return list.stream().filter(c -> c instanceof Grupo) 											
				.map(c -> String.valueOf(c.getCodigo()))
				.reduce("", (l, c) -> l + c + " ")																		
				.trim();
	}
	
	
	
	private String obtenerCodigosChatIndividual(List<ChatIndividual> contactos) {
		return contactos.stream().filter(c -> c instanceof ChatIndividual) 
				.map(c -> String.valueOf(c.getCodigo())).reduce("", (l, c) -> l + c + " ") 																		
				.trim();
	}
	
	

	
}
