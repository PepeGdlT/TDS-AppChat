package TDSAppChatTest;

import modelo.ChatIndividual;
import modelo.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import controlador.ControladorAppChat;
import static org.junit.jupiter.api.Assertions.*;

class AgregarContactosA123Test {
    
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
        assertNotNull(usuario123, "El usuario con número 123 debería estar en sesión.");
    }

    @Test
    void pruebaRegistroChatIndividual() {
        // Crear un usuario de prueba para el contacto
        controlador.registrarUsuario("Contacto Prueba", "000000000", "contacto@correo.com", "pass123", 
                                     "Soy un contacto de prueba", "\\resources\\iconos\\avatar_contacto.png", "02/02/1992");
        
        Usuario contactoPrueba = controlador.existeUsuario("000000000");
        assertNotNull(contactoPrueba, "El usuario del contacto debería existir.");

        ChatIndividual chatPrueba = new ChatIndividual(contactoPrueba);
        controlador.registrarChatIndividual(chatPrueba);

        assertNotEquals(0, chatPrueba.getCodigo(), "El código del chat no debería ser 0.");
        System.out.println("Código asignado a chatPrueba: " + chatPrueba.getCodigo());
    }

    @Test
    void agregarContactosAlUsuario123() {
        // Listado de contactos a agregar
        String[] numeros = {"123456789", "987654321", "555555555", "444444444", "333333333"};
        String[] nombres = {"Juan Perez", "Ana Lopez", "Carlos Ruiz", "Elena Gomez", "Marcos Jimenez"};

        for (int i = 0; i < numeros.length; i++) {
            Usuario usuarioContacto = controlador.existeUsuario(numeros[i]);
            if (usuarioContacto == null) {
                // Registrar el usuario de contacto si no existe
                controlador.registrarUsuario(nombres[i], numeros[i], nombres[i].toLowerCase() + "@correo.com", "password", 
                                             "Hola, soy " + nombres[i], "\\resources\\iconos\\avatar.png", "01/01/1980");
                usuarioContacto = controlador.existeUsuario(numeros[i]);
            }

            assertNotNull(usuarioContacto, "El usuario de contacto debería existir después del registro.");
            
            // Crear y agregar el ChatIndividual
            ChatIndividual contactoChat = new ChatIndividual(usuarioContacto);
            controlador.registrarChatIndividual(contactoChat);
            usuario123.addChat(contactoChat);
        }

        // Verificar que se han agregado los contactos
        assertEquals(5, usuario123.getChatIndividuales().size(), "El usuario 123 debería tener 5 contactos.");
        usuario123.getChatIndividuales().forEach(contacto -> 
            assertNotEquals(0, contacto.getCodigo(), "El código del contacto no debería ser 0.")
        );

        controlador.modificarUsuario(usuario123);  // Guardar cambios en la persistencia
    }
    @Test
    void verificarContactosTrasIniciarSesion() {
        controlador.cerrarSesion(); // Cerrar sesión si hay alguna activa
        controlador.iniciarSesion("123", "123"); // Iniciar sesión con el usuario 123

        Usuario usuarioEnSesion = controlador.getUsuarioActual();
        assertNotNull(usuarioEnSesion, "El usuario 123 debería estar en sesión.");
        
        // Obtener los contactos del usuario en sesión
        assertEquals(5, usuarioEnSesion.getChatIndividuales().size(), "El usuario 123 debería tener 5 contactos al iniciar sesión.");

        usuarioEnSesion.getChatIndividuales().forEach(contacto -> 
            assertNotEquals(0, contacto.getCodigo(), "El código del contacto no debería ser 0.")
        );
    }

    
}
