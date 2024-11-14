package persistencia;

import java.util.List;

import modelo.Grupo;

public interface IAdaptadorGrupoDAO {

		// Metodos CRUD
	
	     void registrarGrupo(Grupo grupo);
	     void borrarGrupo(Grupo grupo);
	     void modificarGrupo(Grupo grupo);
	     Grupo recuperarGrupo(int codigo);
	     List<Grupo> recuperarTodosGrupos();
	   
}
