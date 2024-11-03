package persistencia;


import java.util.List;

import modelo.Usuario;

public interface IAdaptadorUsuarioDAO {
	
	// Metodos CRUD - Create, Read, Update, Delete
	public void registrarUsuario(Usuario usuario);
	public void borrarUsuario(Usuario usuario);
	public void modificarUsuario(Usuario usuario);
	public Usuario recuperarUsuario(int codigo);
	public List<Usuario> recuperarTodosUsuarios();
}
