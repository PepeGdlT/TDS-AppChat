package TDSAppChatTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import controlador.ControladorAppChat;
import modelo.Mensaje;
import modelo.Usuario;
import modelo.ChatIndividual;
import modelo.Grupo;
import java.time.LocalDateTime;
import java.util.List;

public class AppChatTest {

    private ControladorAppChat controlador;

    @BeforeEach
    public void setUp() {
        controlador = ControladorAppChat.INSTANCE;
    }

    @Test
    public void testCompletoChatIndividualYListaDifusion() {
        // Registrar usuarios
        controlador.registrarUsuario("José", "1", "jose@mail.com", "1", 
                                   "Hola soy José", "https://avatar.iran.liara.run/public/boy?username=1", "1990-01-01");
        controlador.registrarUsuario("Pablo", "2", "pablo@mail.com", "2", 
                                    "Hola soy Pablo", "https://avatar.iran.liara.run/public/boy?username=2", "1991-02-02");
        controlador.registrarUsuario("Edu", "3", "edu@mail.com", "3", 
                                   "Hola soy Edu", "https://avatar.iran.liara.run/public/boy?username=3", "1992-03-03");

        // Login de los usuarios
        controlador.iniciarSesion("1", "1");
        Usuario jose = controlador.getUsuarioActual();
        
        controlador.iniciarSesion("2", "2");
        Usuario pablo = controlador.getUsuarioActual();
        
        controlador.iniciarSesion("3", "3");
        Usuario edu = controlador.getUsuarioActual();

        // José agrega a Pablo como contacto
        controlador.iniciarSesion("1", "1");
        controlador.agregarContacto("Pablo", "2");

        // Pablo agrega a José como contacto
        controlador.iniciarSesion("2", "2");
        controlador.agregarContacto("José", "1");

        // José agrega a Edu como contacto
        controlador.iniciarSesion("1", "1");
        controlador.agregarContacto("Edu", "3");

        // Envío de mensajes individuales
        controlador.iniciarSesion("1", "1");
        ChatIndividual chatJosePablo = controlador.getChatIndividual("Pablo");
        controlador.enviarMensaje(chatJosePablo, "Hola Pablo, ¿qué tal?");
        controlador.enviarMensaje(chatJosePablo, 1); // Emoticono con código 1

        controlador.iniciarSesion("2", "2");
        ChatIndividual chatPabloJose = controlador.getChatIndividual("José");
        controlador.enviarMensaje(chatPabloJose, "Hola José, todo bien!");
        controlador.enviarMensaje(chatPabloJose, 2); // Emoticono con código 2

        // Verificar mensajes individuales
        List<Mensaje> mensajesJosePablo = controlador.getMensajes(chatJosePablo);
        assertEquals(4, mensajesJosePablo.size()); 
        assertEquals("Hola Pablo, ¿qué tal?", mensajesJosePablo.get(0).getTexto());
        assertEquals(1, mensajesJosePablo.get(1).getEmoticono());

        // José crea una lista de difusión con Pablo y Edu
        controlador.iniciarSesion("1", "1");
        ChatIndividual chatJoseEdu = controlador.getChatIndividual("Edu");
        controlador.crearGrupo("Amigos", List.of(chatJosePablo, chatJoseEdu));
        Grupo listaAmigos = controlador.getGrupos().get(0);

        // Verificar que solo José ve la lista de difusión
        assertEquals(1, controlador.getGrupos().size());
        
        controlador.iniciarSesion("2", "2");
        assertEquals(0, controlador.getGrupos().size()); // Pablo no ve la lista
        
        controlador.iniciarSesion("3", "3");
        assertEquals(0, controlador.getGrupos().size()); // Edu no ve la lista

        // José envía mensaje a la lista de difusión
        controlador.iniciarSesion("1", "1");
        controlador.enviarMensaje(listaAmigos, "Hola a todos en la lista!");

        // Verificar que los miembros reciben el mensaje individualmente
        controlador.iniciarSesion("2", "2");
        ChatIndividual chatPabloLista = controlador.getChatIndividual("José");
        List<Mensaje> mensajesPablo = controlador.getMensajes(chatPabloLista);
        assertEquals(3, mensajesPablo.size()); // 2 anteriores + 1 de la lista
        assertEquals("Hola a todos en la lista!", mensajesPablo.get(2).getTexto());

        controlador.iniciarSesion("3", "3");
        ChatIndividual chatEduLista = controlador.getChatIndividual("José");
        List<Mensaje> mensajesEdu = controlador.getMensajes(chatEduLista);
        assertEquals(1, mensajesEdu.size()); // Solo el mensaje de la lista
        assertEquals("Hola a todos en la lista!", mensajesEdu.get(0).getTexto());
    }
}