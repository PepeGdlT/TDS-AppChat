package persistencia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

import javax.persistence.Entity;

import org.h2.engine.User;

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
	
	private static ServicioPersistencia servPersistencia;
	private static AdaptadorUsuarioTDS unicaInstancia = null;


	public static AdaptadorUsuarioTDS getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new AdaptadorUsuarioTDS();
		}
		return unicaInstancia;
	}
	

	private AdaptadorUsuarioTDS() {
		servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
	}
	
	
	

	public void registrarUsuario(Usuario usuario) {
		Entidad eUsuario = null;
		boolean existe = true;
		try {
			eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		} catch (NullPointerException e) {
			existe = false;
		}
		if (existe) return;
		
		
		noContactos(usuario.getContactos());
		noGrupos(usuario.getGruposAdmin());
		
		
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("nombreCompleto", usuario.getNombreCompleto()),
                        new Propiedad("numeroTelefono", String.valueOf(usuario.getNumeroTelefono())),
                        new Propiedad("email", usuario.getEmail()),
                        new Propiedad("contrasena", usuario.getContrasena()),
                        new Propiedad("saludo", usuario.getSaludo()),
                        //new Propiedad("fotoPerfilURL", usuario.getFotoPerfilURL()),
                        new Propiedad("fechaNacimiento", usuario.getFechaNacimiento().toString()),
                        new Propiedad("contactos", obtenerCodigosChatIndividual(usuario.getContactos())),
                        new Propiedad("gruposAdmin", obtenerCodigosGruposAdmin(usuario.getGruposAdmin())),
                        new Propiedad("Premium", String.valueOf(usuario.isPremium())),
                        new Propiedad("grupos", obtenerCodigosGrupo(usuario.getContactos()))
					    )));
		
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);

		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		
		usuario.setCodigo(eUsuario.getId());
		
		
	}

	public void borrarUsuario(Usuario usuario) {
		
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		
		AdaptadorChatIndividualTDS adaptadorChatIndividual = AdaptadorChatIndividualTDS.getUnicaInstancia();
		AdaptadorGrupoTDS adaptadorGrupo = AdaptadorGrupoTDS.getUnicaInstancia();
	
		for (Contacto contacto : usuario.getContactos()) {
			if (contacto instanceof ChatIndividual) {
				adaptadorChatIndividual.borrarChatIndividual((ChatIndividual) contacto);
			}
			else {
				adaptadorGrupo.borrarGrupo((Grupo) contacto);
			}
		}
		
		for (Grupo grupoAdmin : usuario.getGruposAdmin()) {
			adaptadorGrupo.borrarGrupo(grupoAdmin);
		}
		
		
		servPersistencia.borrarEntidad(eUsuario);
		
		// Si esta en el Pool tambien se borra del pool
		if (PoolDAO.getUnicaInstancia().contiene(usuario.getCodigo())) {
			PoolDAO.getUnicaInstancia().removeObjeto(usuario.getCodigo());
		}
		
	}

	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "nombreCompleto");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "nombreCompleto",
				usuario.getNombreCompleto());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "numeroTelefono");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "numeroTelefono",
				String.valueOf(usuario.getNumeroTelefono()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "email");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "email",
				usuario.getEmail());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "contrasena");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "contrasena", 
				usuario.getContrasena());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "saludo");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "saludo",
				usuario.getSaludo());
		//servPersistencia.eliminarPropiedadEntidad(eUsuario, "fotoPerfilURL");
		//servPersistencia.anadirPropiedadEntidad(eUsuario, "fotoPerfilURL",
		//		usuario.getFotoPerfilURL());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "fechaNacimiento");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "fechaNacimiento",
				usuario.getFechaNacimiento().toString());
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "contactos");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "contactos", 
				obtenerCodigosChatIndividual(usuario.getContactos()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "gruposAdmin");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "gruposAdmin",
				obtenerCodigosGruposAdmin(usuario.getGruposAdmin()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "Premium");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "Premium",
				String.valueOf(usuario.isPremium()));
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "grupos");
		servPersistencia.anadirPropiedadEntidad(eUsuario, "grupos",
				obtenerCodigosGrupo(usuario.getContactos()));
		
		
		
	}

	public Usuario recuperarUsuario(int codigo) {
		
		if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
			return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
		}
		

		
		Entidad eUsuario;
		String nombreCompleto;
		int numeroTelefono;
		String email;
		String contrasena;
		String saludo;
		String fotoPerfilURL;
		LocalDate fechaNacimiento;
		List<ChatIndividual> contactos;
		List<Grupo> gruposAdmin;
		boolean Premium;
		
		eUsuario = servPersistencia.recuperarEntidad(codigo);
		
		
		nombreCompleto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombreCompleto");
		numeroTelefono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eUsuario, "numeroTelefono"));
		email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		contrasena = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contrasena");
		saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		//fotoPerfilURL = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fotoPerfilURL");
		fechaNacimiento = LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento"));
		Premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "Premium"));
		
		
		
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fechaNacimiento);
		usuario.setPremium(Premium);
		usuario.setCodigo(codigo);
		
		
		PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);

		gruposAdmin = obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "gruposadmin"));
		for (Grupo g : gruposAdmin) usuario.addGrupoAdmin(g);
		
		contactos = obtenerContactosDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos"));
		for (ChatIndividual c : contactos) usuario.addContacto(c);
		
		return usuario;
		
	}

	public List<Usuario> recuperarTodosUsuarios() {
		List<Usuario> usuarios = new LinkedList<Usuario>();
        List<Entidad> eUsuarios = servPersistencia.recuperarEntidades("usuario");
        for (Entidad eUsuario : eUsuarios) {
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

	private void noContactos(List<Contacto> contactos) {
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
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CONTACTOS Y GRUPOS DESDE CODIGOS
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

	private List<ChatIndividual> obtenerContactosDesdeCodigos(String codigos) {
		List<ChatIndividual> contactos = new LinkedList<>();
		StringTokenizer strTok = new StringTokenizer(codigos, " ");
		AdaptadorChatIndividualTDS adaptadorC = AdaptadorChatIndividualTDS.getUnicaInstancia();
		while (strTok.hasMoreTokens()) {
			contactos.add(adaptadorC.recuperarChatIndividual((Integer.valueOf((String) strTok.nextElement()))));
		}
		return contactos;
	}
	
	// -----------------------------------------------------------------------------------------
	// OBTENCION DE CODIGOS DE CONTACTOS Y GRUPOS
	// -----------------------------------------------------------------------------------------
	
	
	private String obtenerCodigosGrupo(List<Contacto> grupos) {
		return grupos.stream().filter(c -> c instanceof Grupo) 											
				.map(c -> String.valueOf(c.getCodigo()))
				.reduce("", (l, c) -> l + c + " ")																		
				.trim();
	}
	
	
	private String obtenerCodigosGruposAdmin(List<Grupo> gruposAdmin) {
		String grupos = "";
		for (Grupo grupo : gruposAdmin) {
			grupos += grupo.getCodigo() + " ";
		}
		return grupos.trim();
	}
	
	private String obtenerCodigosChatIndividual(List<Contacto> contactos) {
		return contactos.stream().filter(c -> c instanceof ChatIndividual) 
				.map(c -> String.valueOf(c.getCodigo())).reduce("", (l, c) -> l + c + " ") 																		
				.trim();
	}
	
	
}
