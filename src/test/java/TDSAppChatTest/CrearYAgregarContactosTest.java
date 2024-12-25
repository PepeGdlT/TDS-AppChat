package TDSAppChatTest;

import modelo.ChatIndividual;
import modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controlador.ControladorAppChat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class CrearYAgregarContactosTest {

    private ControladorAppChat controlador;
    private Usuario usuario123;

    @BeforeEach
    void setUp() {
        controlador = ControladorAppChat.INSTANCE;
        controlador.cerrarSesion();  // Aseguramos que no haya sesión activa para cada test.
        
        // Crear y registrar al usuario con número 123
        controlador.registrarUsuario("Prueba", "123", "usuario123@correo.com", "123", 
                                     "Soy el usuario 123", "\\resources\\iconos\\avatar.png", "01/01/1990");
        controlador.iniciarSesion("123", "123");
        usuario123 = controlador.getUsuarioActual();
        
        // Inicializar lista de chats para asegurar que no sea nula
        usuario123.setChatIndividuales(new ArrayList<>());
        assertNotNull(usuario123.getChatIndividuales(), "La lista de chats individuales debería estar inicializada y no ser nula.");
        assertNotNull(usuario123, "El usuario con número 123 debería estar en sesión.");
    }
    
    @Test
    void crearYAgregarContactosAlUsuario123() {
        // Lista de nombres y números para los contactos
        String[] numeros = {"123456789", "987654321", "555555555", "444444444", "333333333"};
        String[] nombres = {"Juan Perez", "Ana Lopez", "Carlos Ruiz", "Elena Gomez", "Marcos Jimenez"};
        List<Usuario> usuariosCreados = new ArrayList<>();

        // Crear y registrar los contactos
        for (int i = 0; i < numeros.length; i++) {
            controlador.registrarUsuario(nombres[i], numeros[i], nombres[i].toLowerCase() + "@correo.com", "password", 
                                         "Hola, soy " + nombres[i], "\\resources\\iconos\\avatar.png", "01/01/1980");
            Usuario usuarioContacto = controlador.existeUsuario(numeros[i]);
            assertNotNull(usuarioContacto, "El usuario de contacto debería existir después del registro.");
            usuariosCreados.add(usuarioContacto);
        }

        // Agregar cada usuario como contacto del usuario123
        for (Usuario usuarioContacto : usuariosCreados) {
            ChatIndividual contactoChat = new ChatIndividual(usuarioContacto);
            controlador.registrarChatIndividual(contactoChat);
            usuario123.addChat(contactoChat);
        }

        // Guardar cambios en persistencia
        controlador.modificarUsuario(usuario123);
        
        // Debugging: intentar cargar de nuevo el usuario directamente desde la persistencia
        Usuario usuarioRecargado = controlador.existeUsuario("123");
        assertNotNull(usuarioRecargado, "El usuario 123 debería existir en la persistencia.");
        System.out.println("Número de contactos guardados en persistencia: " + usuarioRecargado.getChatIndividuales().size());

        // Verificar que se han agregado los contactos
        assertEquals(usuariosCreados.size(), usuarioRecargado.getChatIndividuales().size(), "El usuario 123 debería tener todos los contactos agregados.");
    }

    
    @Test
    void verificarContactosAlIniciarSesion() {
        controlador.cerrarSesion(); // Cerrar sesión si hay alguna activa
        controlador.iniciarSesion("123", "123"); // Iniciar sesión con el usuario 123

        Usuario usuarioEnSesion = controlador.getUsuarioActual();
        assertNotNull(usuarioEnSesion, "El usuario 123 debería estar en sesión.");
        
        // Verificar que la lista de contactos no sea nula
        assertNotNull(usuarioEnSesion.getChatIndividuales(), "La lista de chats individuales debería estar inicializada y no ser nula.");
        
        // Obtener los contactos del usuario en sesión
        System.out.println("Número de contactos tras iniciar sesión: " + usuarioEnSesion.getChatIndividuales().size());
        assertEquals(5, usuarioEnSesion.getChatIndividuales().size(), "El usuario 123 debería tener 5 contactos al iniciar sesión.");

        usuarioEnSesion.getChatIndividuales().forEach(contacto -> 
            assertNotEquals(0, contacto.getCodigo(), "El código del contacto no debería ser 0.")
        );
    }
}
