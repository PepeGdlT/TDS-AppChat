package persistencia;

public abstract class FactoriaDAO {
	private static FactoriaDAO unicaInstancia;
	
	public static final String DAO_TDS = "persistencia.TDSFactoriaDAO";
	
	public static FactoriaDAO getUnicaInstancia(String tipo) throws DAOException {
		if (unicaInstancia == null) {
			try {
				unicaInstancia = (FactoriaDAO) Class.forName(tipo).newInstance();
			} catch (Exception e) {
				throw new DAOException("FactoriaDAO.getUnicaInstancia: " + e.getMessage());
			}
		}
		return unicaInstancia;
	}
	
	public static FactoriaDAO getUnicaInstancia() throws DAOException {
		return (unicaInstancia == null) ? getUnicaInstancia(DAO_TDS) : unicaInstancia;
	}
	
	protected FactoriaDAO() {}
	
	public abstract IAdaptadorUsuarioDAO getUsuarioDAO();
	public abstract IAdaptadorMensajeDAO getMensajeDAO();
	public abstract IAdaptadorChatIndividualDAO getChatIndividualDAO();
	public abstract IAdaptadorGrupoDAO getGrupoDAO();
	
	
}
