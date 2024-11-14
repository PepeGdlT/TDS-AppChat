package persistencia;

import java.util.List;

import modelo.Mensaje;

public interface IAdaptadorMensajeDAO {
	
	// Metodos CRUD
	
	 void registrarMensaje(Mensaje mensaje);
	 void borrarMensaje(Mensaje mensaje);
	 void modificarMensaje(Mensaje mensaje);
	 Mensaje recuperarMensaje(int codigo);
	 List<Mensaje> recuperarTodosMensajes();

}
