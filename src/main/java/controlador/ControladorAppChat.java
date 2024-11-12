package controlador;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import modelo.CatalogoUsuarios;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Mensaje;
import modelo.Usuario;
import persistencia.AdaptadorUsuarioTDS;
import persistencia.FactoriaDAO;
import persistencia.IAdaptadorChatIndividualDAO;
import persistencia.IAdaptadorGrupoDAO;
import persistencia.IAdaptadorMensajeDAO;
import persistencia.IAdaptadorUsuarioDAO;

public class ControladorAppChat {
    
    private static ControladorAppChat unicaInstancia;
    
    private IAdaptadorUsuarioDAO adaptadorUsuario;
    private IAdaptadorChatIndividualDAO adaptadorChatIndividual;
    private IAdaptadorMensajeDAO adaptadorMensaje;
    private IAdaptadorGrupoDAO adaptadorGrupo;
    
    private CatalogoUsuarios catalogoUsuarios;
    
    private Usuario usuarioActual;
    private ChatIndividual chatActual;

    public ControladorAppChat() {
        inicializarAdaptadores();
        inicializarCatalogos();
    }

    public static ControladorAppChat getUnicaInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new ControladorAppChat();
        }
        return unicaInstancia;
    }
    
    private void inicializarAdaptadores() {
        FactoriaDAO factoria = null;
        try {
            factoria = FactoriaDAO.getUnicaInstancia(FactoriaDAO.DAO_TDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adaptadorUsuario = factoria.getUsuarioDAO();
        adaptadorChatIndividual = factoria.getChatIndividualDAO();
        adaptadorMensaje = factoria.getMensajeDAO();
        adaptadorGrupo = factoria.getGrupoDAO();
    }
    
    private void inicializarCatalogos() {
        catalogoUsuarios = CatalogoUsuarios.getUnicaInstancia();
    }

    //-----------------------------------------------------
    // Funciones de control de usuario actual
    //-----------------------------------------------------
    
    public boolean registrarUsuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
        if (existeUsuario(numeroTelefono) != null) return false;

        Usuario usuario = new Usuario(nombreCompleto, numeroTelefono, email, contrasena, saludo, fotoPerfilURL, fechaNacimiento);
        adaptadorUsuario.registrarUsuario(usuario);
        catalogoUsuarios.addUsuario(usuario);
        return true;
    }

    public void modificarUsuario(Usuario usuario) {
        if (usuario != null) {
            adaptadorUsuario.modificarUsuario(usuario);
        }
    }

    public Usuario existeUsuario(String numeroTelefono) {
        return CatalogoUsuarios.getUnicaInstancia().encontrarUsuario(numeroTelefono);
    }

    public boolean iniciarSesion(String phone, String contrasena) {
        Usuario usuario = catalogoUsuarios.encontrarUsuario(phone);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            this.usuarioActual = usuario;
            return true;
        }
        return false;
    }

    public boolean borrarUsuario(Usuario usuario) {
        if (existeUsuario(usuario.getNumeroTelefono()) != null) return false;

        adaptadorUsuario.borrarUsuario(usuario);
        catalogoUsuarios.removeUsuario(usuario);
        return true;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void hacerPremium(Boolean premium) {
        usuarioActual.setPremium(premium);
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    //-----------------------------------------------------
    // Funciones de control de contactos
    //-----------------------------------------------------
    
    public List<ChatIndividual> getChatIndividuals() {
        if (usuarioActual == null) {
            return new LinkedList<>();
        }
        return usuarioActual.getChatIndividuales();
    }
    
    public List<Grupo> getGrupos() {
        if (usuarioActual == null) {
            return new LinkedList<>();
        }
        return usuarioActual.getGrupos();
    }
    
    public boolean agregarContacto(String nombre, String telefono) {
        if (usuarioActual == null) return false;

        // Verifica si el contacto ya existe en los chats del usuario actual
        Optional<ChatIndividual> chatExistente = usuarioActual.getChatIndividuales().stream()
                .filter(c -> c.getNumeroTelefono().equals(telefono))
                .findFirst();

        if (chatExistente.isPresent()) {
            return false; // Si ya existe, no lo agrega de nuevo
        }

        // Buscar el usuario para el nuevo contacto
        Usuario contacto = catalogoUsuarios.encontrarUsuario(telefono);
        if (contacto == null) {
            // Si el contacto no existe en el catálogo, no lo agrega
            return false;
        }

        // Crear el nuevo chat individual y asociarlo al usuario actual
        ChatIndividual nuevoContacto = new ChatIndividual(contacto);
        usuarioActual.addChat(nuevoContacto);
        
        // Guarda el usuario actualizado con el nuevo contacto
        adaptadorUsuario.modificarUsuario(usuarioActual);

        return true;
    }


    //-----------------------------------------------------
    // Funciones de control de chats
    //-----------------------------------------------------
    
    public List<Mensaje> getMensajes(ChatIndividual chat) {
        return chat.getMensajesEnviados().stream().sorted().collect(Collectors.toList());
    }
    
    public Mensaje getUltimoMensaje(ChatIndividual chat) {
        List<Mensaje> mensajes = getMensajes(chat);
        if (mensajes.isEmpty()) {
            return null;
        }
        return mensajes.get(mensajes.size() - 1);
    }
    
    public void enviarMensaje(ChatIndividual chat, String mensajeTexto) {
        Mensaje mensaje = new Mensaje(mensajeTexto, LocalDateTime.now(), usuarioActual, chat);
        chat.enviarMensaje(mensaje);
        adaptadorMensaje.registrarMensaje(mensaje);
    }

    public void enviarMensaje(ChatIndividual chat, int emoji) {
        Mensaje mensaje = new Mensaje(emoji, LocalDateTime.now(), usuarioActual, chat);
        chat.enviarMensaje(mensaje);
        adaptadorMensaje.registrarMensaje(mensaje);
    }

    public void setChatActual(ChatIndividual chat) {
        this.chatActual = chat;
    }

    public boolean isAdmin(Grupo grupo) {
        return grupo.getAdministrador().equals(usuarioActual);
    }

    //-----------------------------------------------------
    // Funciones de creación de objetos
    //-----------------------------------------------------
    
    public ChatIndividual crearChatIndividual(Usuario usuario) {
        ChatIndividual chat = new ChatIndividual(usuario);
        adaptadorChatIndividual.registrarChatIndividual(chat);
        usuarioActual.addChat(chat);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return chat;
    }
    
    public Grupo crearGrupo(String nombre, List<ChatIndividual> participantes) {
        Grupo grupo = new Grupo(nombre, usuarioActual);
        participantes.forEach(grupo::addMiembro);
        adaptadorGrupo.registrarGrupo(grupo);
        usuarioActual.addGrupo(grupo);
        adaptadorUsuario.modificarUsuario(usuarioActual);
        return grupo;
    }

    //-----------------------------------------------------
    // Funciones de modificación de objetos
    //-----------------------------------------------------
    
    public Grupo modificarGrupo(Grupo grupo, String nombre, List<ChatIndividual> participantes) {
        grupo.setNombre(nombre);
        grupo.setListaMiembros(participantes);
        adaptadorGrupo.modificarGrupo(grupo);
        return grupo;
    }

    //-----------------------------------------------------
    // Funciones de búsqueda de mensajes
    //-----------------------------------------------------
    
    public List<Mensaje> buscarMensajes(String emisor, LocalDateTime fechaInicio, LocalDateTime fechaFin, String text) {
        return usuarioActual.getChatIndividuales().stream()
                .flatMap(chat -> chat.getMensajesEnviados().stream())
                .filter(mensaje -> mensaje.getEmisor().getNombreCompleto().equalsIgnoreCase(emisor) &&
                                   !mensaje.getHora().isBefore(fechaInicio) &&
                                   !mensaje.getHora().isAfter(fechaFin) &&
                                   mensaje.getTexto().contains(text))
                .collect(Collectors.toList());
    }

    //-----------------------------------------------------
    // Funciones de eliminación de objetos
    //-----------------------------------------------------
    
    public void deleteChatIndividual(ChatIndividual chat) {
        adaptadorChatIndividual.borrarChatIndividual(chat);
        usuarioActual.getChatIndividuales().remove(chat);
        adaptadorUsuario.modificarUsuario(usuarioActual);
    }

    //-----------------------------------------------------
    // Funciones de registro de entidades
    //-----------------------------------------------------
    
    public void registrarChatIndividual(ChatIndividual chat) {
        adaptadorChatIndividual.registrarChatIndividual(chat);
    }
}
