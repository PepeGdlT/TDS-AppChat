package persistencia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Mensaje;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorChatIndividualTDS implements IAdaptadorChatIndividualDAO {

    private static final String CHAT = "chat";
    private static final String CONTACTO = "contacto";
    private static final String MENSAJES_ENVIADOS = "mensajesEnviados";

    private static ServicioPersistencia servPersistencia;
    private static FactoriaDAO factoria;


    AdaptadorChatIndividualTDS() throws DAOException {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        factoria = FactoriaDAO.getUnicaInstancia();
    }

    // -----------------------------------------------------------------------------------------
    // MÉTODOS DE PERSISTENCIA
    // -----------------------------------------------------------------------------------------

    @Override
    public void registrarChatIndividual(ChatIndividual chat) {
        if (chat == null || existeChat(chat.getCodigo())) return;

        Entidad eChat = new Entidad();
        eChat.setNombre(CHAT);
        eChat.setPropiedades(new ArrayList<>(List.of(
                new Propiedad(CONTACTO, String.valueOf(chat.getContacto().getCodigo())),
                new Propiedad(MENSAJES_ENVIADOS, obtenerCodigosMensajes(chat.getMensajesEnviados()))
        )));

        eChat = servPersistencia.registrarEntidad(eChat);
        chat.setCodigo(eChat.getId());

        PoolDAO.getUnicaInstancia().addObjeto(chat.getCodigo(), chat);

        // Registrar mensajes asociados
        registrarMensajes(chat.getMensajesEnviados());
    }

    @Override
    public void borrarChatIndividual(ChatIndividual chat) {
        if (chat == null) return;

        Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
        if (eChat == null) return;

        borrarMensajes(chat.getMensajesEnviados());
        servPersistencia.borrarEntidad(eChat);

        PoolDAO.getUnicaInstancia().removeObjeto(chat.getCodigo());
    }

    @Override
    public void modificarChatIndividual(ChatIndividual chat) {
        if (chat == null) return;

        Entidad eChat = servPersistencia.recuperarEntidad(chat.getCodigo());
        if (eChat == null) return;

        servPersistencia.eliminarPropiedadEntidad(eChat, CONTACTO);
        servPersistencia.anadirPropiedadEntidad(eChat, CONTACTO, String.valueOf(chat.getContacto().getCodigo()));
        
        servPersistencia.eliminarPropiedadEntidad(eChat, MENSAJES_ENVIADOS);
        servPersistencia.anadirPropiedadEntidad(eChat, MENSAJES_ENVIADOS, obtenerCodigosMensajes(chat.getMensajesEnviados()));

        // Registrar cualquier mensaje nuevo
        registrarMensajes(chat.getMensajesEnviados());
    }

    @Override
    public ChatIndividual recuperarChatIndividual(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (ChatIndividual) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        Entidad eChat = servPersistencia.recuperarEntidad(codigo);
        if (eChat == null) return null;

        // Recuperar el código del contacto y obtener el Usuario
        int codigoContacto = Integer.parseInt(servPersistencia.recuperarPropiedadEntidad(eChat, CONTACTO));
        
        Usuario contacto = factoria.getUsuarioDAO().recuperarUsuario(codigoContacto);
        if (contacto == null) return null;

        // Crear ChatIndividual usando el contacto
        ChatIndividual chat = new ChatIndividual(contacto);
        chat.setCodigo(codigo);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, chat);

        // Recuperar y asignar mensajes enviados
        List<Mensaje> mensajesEnviados = obtenerMensajesDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eChat, MENSAJES_ENVIADOS));
        mensajesEnviados.forEach(chat::enviarMensaje);

        return chat;
    }

    @Override
    public List<ChatIndividual> recuperarTodosChatsIndividuales() {
        List<ChatIndividual> chats = new LinkedList<>();
        List<Entidad> entidades = servPersistencia.recuperarEntidades(CHAT);
        for (Entidad eChat : entidades) {
            ChatIndividual chat = recuperarChatIndividual(eChat.getId());
            if (chat != null) {
                chats.add(chat);
            }
        }
        return chats;
    }

    // -----------------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------------------------------------------------------------------

    private void registrarMensajes(List<Mensaje> mensajes) {
        AdaptadorMensajeTDS adaptadorMensajes = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        mensajes.forEach(adaptadorMensajes::registrarMensaje);
    }

    private void borrarMensajes(List<Mensaje> mensajes) {
        AdaptadorMensajeTDS adaptadorMensajes = (AdaptadorMensajeTDS) factoria.getMensajeDAO();
        mensajes.forEach(adaptadorMensajes::borrarMensaje);
    }

    private boolean existeChat(int codigo) {
        return servPersistencia.recuperarEntidad(codigo) != null;
    }

    // -----------------------------------------------------------------------------------------
    // OBTENCIÓN DE MENSAJES DESDE CÓDIGOS
    // -----------------------------------------------------------------------------------------

    private List<Mensaje> obtenerMensajesDesdeCodigos(String codigos) {
        List<Mensaje> mensajes = new LinkedList<>();
        if (codigos == null || codigos.isEmpty()) return mensajes;

        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorMensajeTDS adaptadorMensaje =(AdaptadorMensajeTDS) factoria.getMensajeDAO();
        while (strTok.hasMoreTokens()) {
            mensajes.add(adaptadorMensaje.recuperarMensaje(Integer.parseInt(strTok.nextToken())));
        }
        return mensajes;
    }

    // -----------------------------------------------------------------------------------------
    // OBTENCIÓN DE CÓDIGOS DESDE MENSAJES
    // -----------------------------------------------------------------------------------------

    private String obtenerCodigosMensajes(List<Mensaje> mensajesEnviados) {
        if (mensajesEnviados == null || mensajesEnviados.isEmpty()) return "";

        return mensajesEnviados.stream()
                .map(m -> String.valueOf(m.getCodigo()))
                .collect(Collectors.joining(" "));
    }
}
