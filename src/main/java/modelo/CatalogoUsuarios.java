package modelo;

import java.util.*;

import persistencia.DAOException;
import persistencia.FactoriaDAO;

public class CatalogoUsuarios {
	
	private Map<String, Usuario> usuariosTelefono;
	private Map<Integer, Usuario> usuariosCodigo;
	
	private static CatalogoUsuarios unicaInstancia = new CatalogoUsuarios();
	private FactoriaDAO dao;
	
	private CatalogoUsuarios() {
		usuariosTelefono = new HashMap<String, Usuario>();
		usuariosCodigo = new HashMap<Integer, Usuario>();
		try {
			dao = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
			
			List<Usuario> listaUsuarios = dao.getUsuarioDAO().recuperarTodosUsuarios();
			for (Usuario usuario : listaUsuarios) {
				usuariosTelefono.put(usuario.getNumeroTelefono(), usuario);
				usuariosCodigo.put(usuario.getCodigo(), usuario);
			}
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	public static CatalogoUsuarios getUnicaInstancia() {
		return unicaInstancia;
	}
	public List<Usuario> getUsuarios() {
		return new LinkedList<Usuario>(usuariosTelefono.values());
	}
	
	public Usuario encontrarUsuario(String login) {
		return usuariosTelefono.get(login);
	}

	public Usuario encontrarUsuario(int id) {
		return usuariosCodigo.get(id);
	}
	
	

	public void addUsuario(Usuario usuario) {
		usuariosCodigo.put(usuario.getCodigo(), usuario);
		usuariosTelefono.put(usuario.getNumeroTelefono(), usuario);

	}
	
	public void removeUsuario(Usuario usuario) {
		usuariosTelefono.remove(usuario.getNumeroTelefono());
		usuariosCodigo.remove(usuario.getCodigo());
    }
	
	
	
	public boolean contains(Usuario usuario) {
		return usuariosTelefono.containsValue(usuario);
	}
	
	
	
	
}
	
	
	

