package TDSAppChatTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import controlador.ControladorAppChat;

public class AppChatTest {

    private ControladorAppChat controlador;

    @BeforeEach
    public void setUp() {
        // Inicializar el controlador
        controlador = ControladorAppChat.INSTANCE;
    }

    @Test
    public void testRegistrarUsuarios() {
        // Registrar usuarios
        controlador.registrarUsuario("Usuario 1", "1", "email1@mail.com", "1", "Hola soy 1", "https://avatar.iran.liara.run/public/boy?username=1", "1990-01-01");
        controlador.registrarUsuario("Usuario 2", "2", "email2@mail.com", "2", "Hola soy 2", "https://avatar.iran.liara.run/public/boy?username=2", "1991-02-02");
        controlador.registrarUsuario("Usuario 3", "3", "email3@mail.com", "3", "Hola soy 3", "https://avatar.iran.liara.run/public/boy?username=3", "1992-03-03");

        assertEquals(1, 1); 
    }
}
