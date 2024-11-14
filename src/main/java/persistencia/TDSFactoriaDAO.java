package persistencia;



public class TDSFactoriaDAO extends FactoriaDAO {
	
	public TDSFactoriaDAO() {	}
	
	@Override
	public AdaptadorUsuarioTDS getUsuarioDAO() {	
		try {
			return new AdaptadorUsuarioTDS();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null; 
	}


	@Override
	public AdaptadorMensajeTDS getMensajeDAO() {
		try {
			return new AdaptadorMensajeTDS();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AdaptadorChatIndividualTDS getChatIndividualDAO() {
		try {
			return new AdaptadorChatIndividualTDS();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AdaptadorGrupoTDS getGrupoDAO() {
		try {
			return new AdaptadorGrupoTDS();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
