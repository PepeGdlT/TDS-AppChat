package persistencia;


import java.util.List;

import modelo.Usuario;

public interface IAdaptadorUsuarioDAO {
	
	// Metodos CRUD - Create, Read, Update, Delete
	 void registrarUsuario(Usuario usuario);
	 void borrarUsuario(Usuario usuario);
	 void modificarUsuario(Usuario usuario);
	 Usuario recuperarUsuario(int codigo);
	 List<Usuario> recuperarTodosUsuarios();
}
