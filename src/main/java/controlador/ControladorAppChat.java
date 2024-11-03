package controlador;

import com.toedter.calendar.JCalendar;

import modelo.CatalogoUsuarios;
import modelo.Usuario;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorUsuarioDAO;

public class ControladorAppChat {
	
	private static ControladorAppChat unicaInstancia;
	
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	private  CatalogoUsuarios catalogoUsuarios;
	
	private Usuario usuarioActual;
	
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
	
	
	
	public void registrarUsuario(String nombreCompleto, int numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, JCalendar fechaNacimiento) {
		Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);
		adaptadorUsuario.registrarUsuario(usuario);
		
		catalogoUsuarios.addUsuario(usuario);
	}
	
	
	private void inicializarAdaptadores() {
		FactoriaDAO factoria = null;
		try {
			factoria = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		adaptadorUsuario = factoria.getUsuarioDAO();
	}
	
	
	private void inicializarCatalogos() {
		catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
	}
	
	
	public boolean existeUsuario(int numeroTelefono) {
		return CatalogoUsuarios.getUnicaInstancia().getUsuario(numeroTelefono) != null;
	}
	
}
