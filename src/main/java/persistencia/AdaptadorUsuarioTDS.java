package persistencia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;

public class AdaptadorUsuarioTDS implements IAdaptadorUsuarioDAO {

    private static final String USUARIO = "usuario";
    private static final String NOMBRECOMPLETO = "nombreCompleto";
    private static final String NUMEROTELEFONO = "numeroTelefono";
    private static final String EMAIL = "email";
    private static final String CONTRASENA = "contrasena";
    private static final String SALUDO = "saludo";
    private static final String FOTOPERFILURL = "fotoPerfilURL";
    private static final String FECHANACIMIENTO = "fechaNacimiento";
    private static final String CHATS_INDIVIDUALES = "chatsIndividuales";
    private static final String PREMIUM = "Premium";
    private static final String GRUPOS = "grupos";

    private ServicioPersistencia servPersistencia;
    private FactoriaDAO factoria;
    
    // Constructor
    public AdaptadorUsuarioTDS() throws DAOException {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        factoria = FactoriaDAO.getUnicaInstancia();
    }

    @Override
    public void registrarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getCodigo())) return;

        Entidad eUsuario = new Entidad();
        eUsuario.setNombre(USUARIO);
        eUsuario.setPropiedades(new ArrayList<>(List.of(
                new Propiedad(NOMBRECOMPLETO, usuario.getNombreCompleto()),
                new Propiedad(NUMEROTELEFONO, usuario.getNumeroTelefono()),
                new Propiedad(EMAIL, usuario.getEmail()),
                new Propiedad(CONTRASENA, usuario.getContrasena()),
                new Propiedad(SALUDO, usuario.getSaludo()),
                new Propiedad(FOTOPERFILURL, usuario.getFotoPerfilURL()),
                new Propiedad(FECHANACIMIENTO, usuario.getFechaNacimiento()),
                new Propiedad(CHATS_INDIVIDUALES, obtenerCodigosChatIndividual(usuario.getChatIndividuales())),
                new Propiedad(PREMIUM, String.valueOf(usuario.isPremium())),
                new Propiedad(GRUPOS, obtenerCodigosGrupo(usuario.getGrupos()))
        )));

        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        usuario.setCodigo(eUsuario.getId());

        PoolDAO.getUnicaInstancia().addObjeto(usuario.getCodigo(), usuario);

        // Registrar todos los chats y grupos asociados
        registrarChats(usuario.getChatIndividuales());
        registrarGrupos(usuario.getGrupos());
    }

    @Override
    public void borrarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

        borrarChats(usuario.getChatIndividuales());
        borrarGrupos(usuario.getGrupos());

        servPersistencia.borrarEntidad(eUsuario);
        PoolDAO.getUnicaInstancia().removeObjeto(usuario.getCodigo());
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

        actualizarPropiedadesUsuario(eUsuario, usuario);
        registrarChats(usuario.getChatIndividuales());
        registrarGrupos(usuario.getGrupos());
    }

    @Override
    public Usuario recuperarUsuario(int codigo) {
        if (PoolDAO.getUnicaInstancia().contiene(codigo)) {
            return (Usuario) PoolDAO.getUnicaInstancia().getObjeto(codigo);
        }

        Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
        Usuario usuario = construirUsuarioDesdeEntidad(eUsuario);

        PoolDAO.getUnicaInstancia().addObjeto(codigo, usuario);
        return usuario;
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
        List<Usuario> usuarios = new LinkedList<>();
        List<Entidad> eUsuarios = servPersistencia.recuperarEntidades(USUARIO);
        for (Entidad eUsuario : eUsuarios) {
            usuarios.add(recuperarUsuario(eUsuario.getId()));
        }
        return usuarios;
    }

    // -----------------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------------------------------------------------------------------

    private void registrarChats(List<ChatIndividual> chats) {
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        chats.forEach(adaptadorChatIndividual::registrarChatIndividual);
    }

    private void borrarChats(List<ChatIndividual> chats) {
        AdaptadorChatIndividualTDS adaptadorChatIndividual = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        chats.forEach(adaptadorChatIndividual::borrarChatIndividual);
    }

    private void registrarGrupos(List<Grupo> grupos) {
        AdaptadorGrupoTDS adaptadorGrupo = (AdaptadorGrupoTDS) factoria.getGrupoDAO();
        grupos.forEach(adaptadorGrupo::registrarGrupo);
    }

    private void borrarGrupos(List<Grupo> grupos) {
        AdaptadorGrupoTDS adaptadorGrupo = (AdaptadorGrupoTDS) factoria.getGrupoDAO();
        grupos.forEach(adaptadorGrupo::borrarGrupo);
    }

    private void actualizarPropiedadesUsuario(Entidad eUsuario, Usuario usuario) {
        servPersistencia.eliminarPropiedadEntidad(eUsuario, NOMBRECOMPLETO);
        servPersistencia.anadirPropiedadEntidad(eUsuario, NOMBRECOMPLETO, usuario.getNombreCompleto());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, NUMEROTELEFONO);
        servPersistencia.anadirPropiedadEntidad(eUsuario, NUMEROTELEFONO, usuario.getNumeroTelefono());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, EMAIL);
        servPersistencia.anadirPropiedadEntidad(eUsuario, EMAIL, usuario.getEmail());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, CONTRASENA);
        servPersistencia.anadirPropiedadEntidad(eUsuario, CONTRASENA, usuario.getContrasena());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, SALUDO);
        servPersistencia.anadirPropiedadEntidad(eUsuario, SALUDO, usuario.getSaludo());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, FOTOPERFILURL);
        servPersistencia.anadirPropiedadEntidad(eUsuario, FOTOPERFILURL, usuario.getFotoPerfilURL());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, FECHANACIMIENTO);
        servPersistencia.anadirPropiedadEntidad(eUsuario, FECHANACIMIENTO, usuario.getFechaNacimiento());
        servPersistencia.eliminarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES);
        servPersistencia.anadirPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES, obtenerCodigosChatIndividual(usuario.getChatIndividuales()));
        servPersistencia.eliminarPropiedadEntidad(eUsuario, PREMIUM);
        servPersistencia.anadirPropiedadEntidad(eUsuario, PREMIUM, String.valueOf(usuario.isPremium()));
        servPersistencia.eliminarPropiedadEntidad(eUsuario, GRUPOS);
        servPersistencia.anadirPropiedadEntidad(eUsuario, GRUPOS, obtenerCodigosGrupo(usuario.getGrupos()));
    }

    private Usuario construirUsuarioDesdeEntidad(Entidad eUsuario) {
        String nombreCompleto = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRECOMPLETO);
        String numeroTelefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, NUMEROTELEFONO);
        String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
        String contrasena = servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTRASENA);
        String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, SALUDO);
        String fotoPerfilURL = servPersistencia.recuperarPropiedadEntidad(eUsuario, FOTOPERFILURL);
        boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
        String fechaNacimiento = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHANACIMIENTO);

        Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);
        usuario.setPremium(premium);
        usuario.setCodigo(eUsuario.getId());

        usuario.setChatIndividuales(obtenerChatsDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES)));
        usuario.setGrupos(obtenerGruposDesdeCodigos(servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS)));

        return usuario;
    }

    private boolean existeUsuario(int codigo) {
        return servPersistencia.recuperarEntidad(codigo) != null;
    }

    private List<Grupo> obtenerGruposDesdeCodigos(String codigos) {
        List<Grupo> grupos = new LinkedList<>();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorGrupoTDS adaptadorGrupo = (AdaptadorGrupoTDS) factoria.getGrupoDAO();
        while (strTok.hasMoreTokens()) {
            grupos.add(adaptadorGrupo.recuperarGrupo(Integer.parseInt(strTok.nextToken())));
        }
        return grupos;
    }

    private List<ChatIndividual> obtenerChatsDesdeCodigos(String codigos) {
        List<ChatIndividual> chats = new LinkedList<>();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        AdaptadorChatIndividualTDS adaptadorChat = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        while (strTok.hasMoreTokens()) {
            chats.add(adaptadorChat.recuperarChatIndividual(Integer.parseInt(strTok.nextToken())));
        }
        return chats;
    }

    private String obtenerCodigosGrupo(List<Grupo> list) {
        return list.stream()
                   .map(g -> String.valueOf(g.getCodigo()))
                   .collect(Collectors.joining(" "));
    }

    private String obtenerCodigosChatIndividual(List<ChatIndividual> chats) {
        return chats.stream()
                    .map(c -> String.valueOf(c.getCodigo()))
                    .collect(Collectors.joining(" "));
    }
}
