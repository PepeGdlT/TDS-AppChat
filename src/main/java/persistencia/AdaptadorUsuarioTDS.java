package persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import com.toedter.calendar.JCalendar;

import beans.Entidad;
import beans.Propiedad;
import modelo.Contacto;
import modelo.Grupo;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;


	// METER POOLDAO	

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
		
		eUsuario = new Entidad();
		eUsuario.setNombre("usuario");
		eUsuario.setPropiedades(new ArrayList<Propiedad>(
				Arrays.asList(
						new Propiedad("nombreCompleto", usuario.getNombreCompleto()),
                        new Propiedad("numeroTelefono", String.valueOf(usuario.getNumeroTelefono())),
                        new Propiedad("email", usuario.getEmail()),
                        new Propiedad("contrasena", usuario.getContrasena()),
                        new Propiedad("saludo", usuario.getSaludo()),
                        new Propiedad("fotoPerfilURL", usuario.getFotoPerfilURL()),
                        new Propiedad("fechaNacimiento", usuario.getFechaNacimiento().toString()),
                        new Propiedad("contactos", usuario.getContactos(),toString()),
                        new Propiedad("gruposAdmin", usuario.getGruposAdmin().toString()),
                        new Propiedad("Premium", usuario.isPremium().toString()))		
				));
		
		PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);

		
		eUsuario = servPersistencia.registrarEntidad(eUsuario);
		
		usuario.setCodigo(eUsuario.getId());
		
		
	}

	public void borrarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		servPersistencia.borrarEntidad(eUsuario);
	}

	public void modificarUsuario(Usuario usuario) {
		Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());
		
		servPersistencia.eliminarPropiedadEntidad(eUsuario, "saludo");
        servPersistencia.anadirPropiedadEntidad(eUsuario, "saludo", usuario.getSaludo());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, "premium");
        servPersistencia.anadirPropiedadEntidad(eUsuario, "premium", String.valueOf(usuario.isPremium()));
        servPersistencia.eliminarPropiedadEntidad(eUsuario, "fotoPerfilURL");
        servPersistencia.anadirPropiedadEntidad(eUsuario, "fotoPerfilURL", usuario.getFotoPerfilURL());
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
		JCalendar fechaNacimiento;
		LinkedList<Contacto> contactos;
		LinkedList<Grupo> gruposAdmin;
		boolean Premium;
		
		eUsuario = servPersistencia.recuperarEntidad(codigo);
		nombreCompleto = servPersistencia.recuperarPropiedadEntidad(eUsuario, "nombreCompleto");
		numeroTelefono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eUsuario, "numeroTelefono"));
		email = servPersistencia.recuperarPropiedadEntidad(eUsuario, "email");
		contrasena = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contrasena");
		saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, "saludo");
		fotoPerfilURL = servPersistencia.recuperarPropiedadEntidad(eUsuario, "fotoPerfilURL");
		fechaNacimiento = JCalendar.parse(servPersistencia.recuperarPropiedadEntidad(eUsuario, "fechaNacimiento"));
		contactos = servPersistencia.recuperarPropiedadEntidad(eUsuario, "contactos");
		gruposAdmin = servPersistencia.recuperarPropiedadEntidad(eUsuario, "gruposAdmin");
		Premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, "Premium"));
		
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);
		usuario.setContactos(contactos);
		usuario.setGruposAdmin(gruposAdmin);
		usuario.setPremium(Premium);
		
		usuario.setCodigo(codigo);
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
	
}
