package persistencia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
                new Propiedad(FOTOPERFILURL, usuario.getFotoPerfil()),
                new Propiedad(FECHANACIMIENTO, usuario.getFechaNacimiento()),
                new Propiedad(CHATS_INDIVIDUALES, obtenerCodigosChatIndividual(usuario.getChatsIndividuales())),
                new Propiedad(PREMIUM, String.valueOf(usuario.isPremium())),
                new Propiedad(GRUPOS, obtenerCodigosGrupo(usuario.getGrupos()))
        )));

        eUsuario = servPersistencia.registrarEntidad(eUsuario);
        usuario.setCodigo(eUsuario.getId());

        PoolDAO.INSTANCE.addObjeto(usuario.getCodigo(), usuario);

        // Registrar todos los chats y grupos asociados
        registrarChats(usuario.getChatsIndividuales());
        registrarGrupos(usuario.getGrupos());
    }

    @Override
    public void borrarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

        borrarChats(usuario.getChatsIndividuales());
        borrarGrupos(usuario.getGrupos());

        servPersistencia.borrarEntidad(eUsuario);
        PoolDAO.INSTANCE.removeObjeto(usuario.getCodigo());
    }

    @Override
    public void modificarUsuario(Usuario usuario) {
        Entidad eUsuario = servPersistencia.recuperarEntidad(usuario.getCodigo());

        actualizarPropiedadesUsuario(eUsuario, usuario);
        registrarChats(usuario.getChatsIndividuales());
        registrarGrupos(usuario.getGrupos());
        
    }

    @Override
    public Usuario recuperarUsuario(int codigo) {
        if (PoolDAO.INSTANCE.contiene(codigo)) {
            return (Usuario) PoolDAO.INSTANCE.getObjeto(codigo);
        }

        Entidad eUsuario = servPersistencia.recuperarEntidad(codigo);
        Usuario usuario = construirUsuarioDesdeEntidad(eUsuario);

        PoolDAO.INSTANCE.addObjeto(codigo, usuario);
        return usuario;
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() {
    	return servPersistencia.recuperarEntidades(USUARIO).stream()
    			  .map(entidad -> recuperarUsuario(entidad.getId()))
    			  .collect(Collectors.toList());
    }

    // -----------------------------------------------------------------------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------------------------------------------------------------------

    private void registrarChats(List<ChatIndividual> chats) {
        AdaptadorChatIndividualTDS adaptadorChat = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        for (ChatIndividual chat : chats) {
            if (!PoolDAO.INSTANCE.contiene(chat.getCodigo())) {
                adaptadorChat.registrarChatIndividual(chat);
            }
        }
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
        
    	for(Propiedad prop : eUsuario.getPropiedades()) {
    		if(prop.getNombre().equals(NOMBRECOMPLETO)) {
    			prop.setValor(usuario.getNombreCompleto());
    		} else if (prop.getNombre().equals(NUMEROTELEFONO)) {
				prop.setValor(usuario.getNumeroTelefono());
			} else if (prop.getNombre().equals(EMAIL)) {
				prop.setValor(usuario.getEmail());
			} else if (prop.getNombre().equals(CONTRASENA)) {
				prop.setValor(usuario.getContrasena());
			} else if (prop.getNombre().equals(SALUDO)) {
				prop.setValor(usuario.getSaludo());
			} else if (prop.getNombre().equals(FOTOPERFILURL)) {
				prop.setValor(usuario.getFotoPerfil());
			} else if (prop.getNombre().equals(FECHANACIMIENTO)) {
				prop.setValor(usuario.getFechaNacimiento());
			} else if (prop.getNombre().equals(CHATS_INDIVIDUALES)) {
				prop.setValor(obtenerCodigosChatIndividual(usuario.getChatsIndividuales()));
			} else if (prop.getNombre().equals(PREMIUM)) {
				prop.setValor(String.valueOf(usuario.isPremium()));
			} else if (prop.getNombre().equals(GRUPOS)) {
				prop.setValor(obtenerCodigosGrupo(usuario.getGrupos()));
			}
    		servPersistencia.modificarPropiedad(prop);
    	}
    }

    private Usuario construirUsuarioDesdeEntidad(Entidad eUsuario) {

        String nombreCompleto = servPersistencia.recuperarPropiedadEntidad(eUsuario, NOMBRECOMPLETO);
        String numeroTelefono = servPersistencia.recuperarPropiedadEntidad(eUsuario, NUMEROTELEFONO);
        String email = servPersistencia.recuperarPropiedadEntidad(eUsuario, EMAIL);
        String contrasena = servPersistencia.recuperarPropiedadEntidad(eUsuario, CONTRASENA);
        String saludo = servPersistencia.recuperarPropiedadEntidad(eUsuario, SALUDO);
        String fotoPerfilURL = servPersistencia.recuperarPropiedadEntidad(eUsuario, FOTOPERFILURL);
        String fechaNacimientoStr = servPersistencia.recuperarPropiedadEntidad(eUsuario, FECHANACIMIENTO);
        boolean premium = Boolean.parseBoolean(servPersistencia.recuperarPropiedadEntidad(eUsuario, PREMIUM));
        
        Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimientoStr);
        usuario.setPremium(premium);
        usuario.setCodigo(eUsuario.getId());

        PoolDAO.INSTANCE.addObjeto(usuario.getCodigo(), usuario);

        // Recuperar y asignar contactos y grupos
        String chatsCodigos = servPersistencia.recuperarPropiedadEntidad(eUsuario, CHATS_INDIVIDUALES);
        usuario.setChatIndividuales(obtenerChatsDesdeCodigos(chatsCodigos));

        String gruposCodigos = servPersistencia.recuperarPropiedadEntidad(eUsuario, GRUPOS);
        usuario.setGrupos(obtenerGruposDesdeCodigos(gruposCodigos));

        
        System.out.println("Códigos de chats individuales: " + chatsCodigos);
        System.out.println("Códigos de grupos: " + gruposCodigos);
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
        if (codigos == null || codigos.isEmpty()) {
            return new LinkedList<>();
        }

        AdaptadorChatIndividualTDS adaptadorChat = (AdaptadorChatIndividualTDS) factoria.getChatIndividualDAO();
        StringTokenizer strTok = new StringTokenizer(codigos, " ");
        List<ChatIndividual> chats = new LinkedList<>();
        Set<Integer> chatsEnRecuperacion = new HashSet<>(); // Para evitar referencias circulares

        while (strTok.hasMoreTokens()) {
            int codigo = Integer.parseInt(strTok.nextToken());

            // Evitar procesar el mismo chat repetidamente
            if (chatsEnRecuperacion.contains(codigo)) {
                continue;
            }
            chatsEnRecuperacion.add(codigo);

            // Recuperar el chat
            ChatIndividual chat = adaptadorChat.recuperarChatIndividual(codigo);
            if (chat != null) {
                chats.add(chat);
            }

            chatsEnRecuperacion.remove(codigo);
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
