package modelo;

import java.util.*;

import persistencia.DAOException;
import persistencia.FactoriaDAO;

public enum CatalogoUsuarios {
	INSTANCE;
	
	private Map<String, Usuario> usuariosTelefono;
	private Map<Integer, Usuario> usuariosCodigo;
	
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
	

	public List<Usuario> getUsuarios() {
		return new LinkedList<Usuario>(usuariosTelefono.values());
	}
	
	public Optional<Usuario>  encontrarUsuario(String numeroTelefono) {
		return usuariosTelefono.values().stream().filter(u -> u.getNumeroTelefono().equals(numeroTelefono)).findAny();
	}

	public  Usuario encontrarUsuario(int id) {
		return usuariosCodigo.values().stream().filter(u -> u.getCodigo() == id).findAny().orElse(null);
	}
	
	

	public void addUsuario(Usuario usuario) {
		usuariosCodigo.put(usuario.getCodigo(), usuario);
		usuariosTelefono.put(usuario.getNumeroTelefono(), usuario);

	}
	
	public void removeUsuario(Usuario usuario) {
		usuariosTelefono.remove(usuario.getNumeroTelefono());
		usuariosCodigo.remove(usuario.getCodigo());
    }
	
	public Usuario getUsuario(String numeroTelefono) {
		return usuariosTelefono.get(numeroTelefono);
	}
	
	public boolean contains(Usuario usuario) {
		return usuariosTelefono.containsValue(usuario);
	}
	
	
	
	
}