package modelo;

import java.util.*;

import persistencia.DAOException;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorUsuarioDAO;


public class CatalogoUsuarios {
	private Map<Integer, Usuario> usuarios;
	private static CatalogoUsuarios unicaInstancia = new CatalogoUsuarios();
	
	private FactoriaDAO dao;
	private IAdaptadorUsuarioDAO adaptadorUsuario;
	
	private CatalogoUsuarios() {
		try {
			dao = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
			adaptadorUsuario = dao.getUsuarioDAO();
			usuarios = new HashMap<Integer, Usuario>();
			this.cargarUsuarios();
		} catch (DAOException eDAO) {
			eDAO.printStackTrace();
		}
	}
	
	
	public static CatalogoUsuarios getUnicaInstancia() {
		return unicaInstancia;
	}
	
	public List<Usuario> getUsuarios() {
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		for (Usuario usuario : usuarios.values()) {
			lista.add(usuario);
		}
		return lista;
	}
	
	
	public Usuario getUsuarioCodigo(int codigo) {
		for (Usuario usuario : usuarios.values()) {
			if (usuario.getCodigo() == codigo) {
				return usuario;
			}
		}
		return null;
	}
	
	public Usuario getUsuarioTelefono(int telefono) {
		return usuarios.get(telefono);
	}
	
	public void addUsuario(Usuario usuario) {
		usuarios.put(usuario.getNumeroTelefono(), usuario);
	}
	
	public void removeUsuario(Usuario usuario) {
		usuarios.remove(usuario.getNumeroTelefono());
    }
	
	
	private void cargarUsuarios() throws DAOException{
        List<Usuario> listaUsuariosBD = adaptadorUsuario.recuperarTodosUsuarios();
        for (Usuario usuario : listaUsuariosBD) {
            usuarios.put(usuario.getNumeroTelefono(), usuario);
        }
    }
	
	
	
	
	
	
}
	
	
	

