package persistencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorMensajeTDS implements IAdaptadorMensajeDAO {

    private static final String MENSAJE = "mensaje";
    private static final String TEXTO = "texto";
    private static final String EMISOR = "emisor";
    private static final String RECEPTOR = "receptor";
    private static final String HORA = "hora";
    private static final String EMOTICONO = "emoticono";

    private static ServicioPersistencia servPersistencia;
    private static FactoriaDAO factoria;

    AdaptadorMensajeTDS() throws DAOException {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        factoria = FactoriaDAO.getUnicaInstancia();
    }

    @Override
    public void registrarMensaje(Mensaje mensaje) {
        if (mensaje == null || existeMensaje(mensaje.getCodigo())) return;

        Entidad eMensaje = new Entidad();
        eMensaje.setNombre(MENSAJE);
        eMensaje.setPropiedades(new ArrayList<>(List.of(
                new Propiedad(TEXTO, mensaje.getTexto()),
                new Propiedad(EMISOR, String.valueOf(mensaje.getEmisor().getCodigo())),
                new Propiedad(RECEPTOR, getReceptorCodigo(mensaje.getReceptor())),
                new Propiedad(HORA, mensaje.getHora().toString()),
                new Propiedad(EMOTICONO, String.valueOf(mensaje.getEmoticono()))
        )));

        eMensaje = servPersistencia.registrarEntidad(eMensaje);
        mensaje.setCodigo(eMensaje.getId());

        PoolDAO.INSTANCE.addObjeto(mensaje.getCodigo(), mensaje);
    }

    @Override
    public void borrarMensaje(Mensaje mensaje) {
        if (mensaje == null) return;

        Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());
        servPersistencia.borrarEntidad(eMensaje);

        PoolDAO.INSTANCE.removeObjeto(mensaje.getCodigo());
    }

    @Override
    public void modificarMensaje(Mensaje mensaje) {
        if (mensaje == null) return;

        Entidad eMensaje = servPersistencia.recuperarEntidad(mensaje.getCodigo());

        
		for (Propiedad prop : eMensaje.getPropiedades()) {
			if (prop.getNombre().equals(TEXTO)) {
				prop.setValor(mensaje.getTexto());
			} else if (prop.getNombre().equals(EMISOR)) {
				prop.setValor(String.valueOf(mensaje.getEmisor().getCodigo()));
			} else if (prop.getNombre().equals(RECEPTOR)) {
				prop.setValor(getReceptorCodigo(mensaje.getReceptor()));
			} else if (prop.getNombre().equals(HORA)) {
				prop.setValor(mensaje.getHora().toString());
			} else if (prop.getNombre().equals(EMOTICONO)) {
				prop.setValor(String.valueOf(mensaje.getEmoticono()));
			}
			servPersistencia.modificarPropiedad(prop);
		}
    }

    @Override
    public Mensaje recuperarMensaje(int codigo) {
        if (PoolDAO.INSTANCE.contiene(codigo)) {
            return (Mensaje) PoolDAO.INSTANCE.getObjeto(codigo);
        }

        Entidad eMensaje = servPersistencia.recuperarEntidad(codigo);
        String texto = servPersistencia.recuperarPropiedadEntidad(eMensaje, TEXTO);
        LocalDateTime hora = LocalDateTime.parse(servPersistencia.recuperarPropiedadEntidad(eMensaje, HORA));
        int emoticono = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMOTICONO));
        Usuario emisor = factoria.getUsuarioDAO().recuperarUsuario(
                Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eMensaje, EMISOR)));
        Object receptor = getReceptor(servPersistencia.recuperarPropiedadEntidad(eMensaje, RECEPTOR));

        Mensaje mensaje = new Mensaje(texto, emoticono, hora);
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setCodigo(codigo);

        PoolDAO.INSTANCE.addObjeto(codigo, mensaje);
        return mensaje;
    }

    @Override
    public List<Mensaje> recuperarTodosMensajes() {
        List<Mensaje> mensajes = new ArrayList<>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades(MENSAJE);

        for (Entidad eMensaje : entidades) {
            mensajes.add(recuperarMensaje(eMensaje.getId()));
        }

        return mensajes;
    }

    // --------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // --------------------------------------------------------------------------------

    private boolean existeMensaje(int codigo) {
        return servPersistencia.recuperarEntidad(codigo) != null;
    }

    private String getReceptorCodigo(Object receptor) {
        if (receptor instanceof ChatIndividual) {
            return String.valueOf(((ChatIndividual) receptor).getCodigo());
        } else if (receptor instanceof Grupo) {
            return String.valueOf(((Grupo) receptor).getCodigo());
        }
        return null;
    }

    private Object getReceptor(String receptorCodigo) {
        int codigo = Integer.parseInt(receptorCodigo);
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        AdaptadorGrupoTDS adaptadorGrupo = (AdaptadorGrupoTDS) factoria.getGrupoDAO();

        ChatIndividual chat = adaptadorChatIndividual.recuperarChatIndividual(codigo);
        if (chat != null) return chat;

        return adaptadorGrupo.recuperarGrupo(codigo);
    }
}
