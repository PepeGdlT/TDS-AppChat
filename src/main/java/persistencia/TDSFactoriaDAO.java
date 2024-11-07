package persistencia;

public class TDSFactoriaDAO extends FactoriaDAO {

	@Override
	public IAdaptadorUsuarioDAO getUsuarioDAO() {
		return AdaptadorUsuarioTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorMensajeDAO getMensajeDAO() {
		return AdaptadorMensajeTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorChatIndividualDAO getChatIndividualDAO() {
		return AdaptadorChatIndividualTDS.getUnicaInstancia();
	}

	@Override
	public IAdaptadorGrupoDAO getGrupoDAO() {
		return AdaptadorGrupoTDS.getUnicaInstancia();
	}
}
