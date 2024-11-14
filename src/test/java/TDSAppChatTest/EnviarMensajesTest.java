package TDSAppChatTest;

import modelo.ChatIndividual;
import modelo.Mensaje;
import modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controlador.ControladorAppChat;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EnviarMensajesTest {

    private ControladorAppChat controlador;
    private Usuario usuario1;
    private Usuario usuario2;
    private ChatIndividual chat;

    @BeforeEach
    void setUp() {
        controlador = ControladorAppChat.INSTANCE;
        controlador.cerrarSesion();  // Aseguramos que no haya sesión activa para cada test.

        // Registrar usuarios
        controlador.registrarUsuario("Usuario 1", "111111111", "user1@correo.com", "password1", "Hola, soy Usuario 1", "\\resources\\iconos\\avatar.png", "01/01/1990");
        controlador.registrarUsuario("Usuario 2", "222222222", "user2@correo.com", "password2", "Hola, soy Usuario 2", "\\resources\\iconos\\avatar.png", "02/02/1990");

        // Iniciar sesión con el primer usuario
        controlador.iniciarSesion("111111111", "password1");
        usuario1 = controlador.getUsuarioActual();
        assertNotNull(usuario1, "El usuario 1 debería estar en sesión.");

        // Buscar el segundo usuario y crear un chat individual entre ellos
        usuario2 = controlador.existeUsuario("222222222");
        assertNotNull(usuario2, "El usuario 2 debería existir.");
        
        chat = new ChatIndividual(usuario2);
        controlador.registrarChatIndividual(chat);
        usuario1.addChat(chat);
    }

    @Test
    void enviarYRecuperarMensajes() {
        // Enviar algunos mensajes desde usuario1 a usuario2
        controlador.enviarMensaje(chat, "Hola, Usuario 2!");
        controlador.enviarMensaje(chat, "¿Cómo estás?");
        controlador.enviarMensaje(chat, "¡Nos vemos mañana!");

        // Recuperar los mensajes del chat y verificar el contenido y orden
        List<Mensaje> mensajes = controlador.getMensajes(chat);
        assertEquals(3, mensajes.size(), "El chat debería contener 3 mensajes.");

        assertEquals("Hola, Usuario 2!", mensajes.get(0).getTexto(), "El primer mensaje debería coincidir.");
        assertEquals("¿Cómo estás?", mensajes.get(1).getTexto(), "El segundo mensaje debería coincidir.");
        assertEquals("¡Nos vemos mañana!", mensajes.get(2).getTexto(), "El tercer mensaje debería coincidir.");
    }

    @Test
    void verificarPersistenciaDeMensajes() {
        // Enviar un mensaje desde usuario1 a usuario2
        controlador.enviarMensaje(chat, "Mensaje persistente!");

        // Recuperar el chat y mensajes desde la base de datos para verificar persistencia
        controlador.cerrarSesion();
        controlador.iniciarSesion("111111111", "password1");  // Reiniciar la sesión y obtener de nuevo el usuario
        ChatIndividual chatRecuperado = controlador.getChatIndividuals().stream()
                .filter(c -> c.getNumeroTelefono().equals("222222222"))
                .findFirst()
                .orElse(null);
        
        assertNotNull(chatRecuperado, "El chat con Usuario 2 debería existir después de cerrar sesión.");

        List<Mensaje> mensajesRecuperados = controlador.getMensajes(chatRecuperado);
        assertEquals(1, mensajesRecuperados.size(), "El chat recuperado debería contener 1 mensaje.");

        assertEquals("Mensaje persistente!", mensajesRecuperados.get(0).getTexto(), "El contenido del mensaje debería coincidir.");
    }
}
