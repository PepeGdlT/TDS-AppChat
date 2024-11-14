package TDSAppChatTest;

import modelo.CatalogoUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controlador.ControladorAppChat;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ControladorAppChatTest {
    
    private ControladorAppChat controlador;

    @BeforeEach
    void setUp() {
        controlador = ControladorAppChat.INSTANCE;
        controlador.cerrarSesion();  // Aseguramos que no haya sesión activa para cada test.
    }

    @Test
    void registrarUsuarioNuevo() {
        boolean registrado = controlador.registrarUsuario(
                "Juan Perez", "123456789", "juan@correo.com", "password123", 
                "Hola, soy Juan!", "fotoUrl", "01/01/2000"
        );
        assertTrue(registrado, "El usuario debería registrarse correctamente.");
        assertNotNull(CatalogoUsuarios.INSTANCE.encontrarUsuario("123456789"));
    }

    @Test
    void registrarUsuarioExistente() {
        controlador.registrarUsuario("Ana Lopez", "987654321", "ana@correo.com", "password456", 
                                      "Soy Ana", "fotoUrl2", "01/01/2000");
        boolean registrado = controlador.registrarUsuario(
                "Ana Lopez", "987654321", "ana@correo.com", "password456", 
                "Soy Ana", "fotoUrl2", "01/01/2000"
        );
        assertFalse(registrado, "El usuario no debería registrarse si el número ya existe.");
    }

    @Test
    void iniciarSesionExitoso() {
        controlador.registrarUsuario("Carlos Ruiz", "555555555", "carlos@correo.com", "password789", 
                                      "Hola, soy Carlos", "fotoUrl3", "01/01/2000");
        boolean inicioSesion = controlador.iniciarSesion("555555555", "password789");
        assertTrue(inicioSesion, "El inicio de sesión debería ser exitoso.");
        assertEquals("555555555", controlador.getUsuarioActual().getNumeroTelefono());
    }

    @Test
    void iniciarSesionFallido() {
        controlador.registrarUsuario("Elena Gomez", "444444444", "elena@correo.com", "password000", 
                                      "Hola", "fotoUrl4", "01/01/2000");
        boolean inicioSesion = controlador.iniciarSesion("444444444", "passwordIncorrecto");
        assertFalse(inicioSesion, "El inicio de sesión debería fallar con contraseña incorrecta.");
        assertNull(controlador.getUsuarioActual());
    }

    @Test
    void cerrarSesionTest() {
        controlador.registrarUsuario("Marcos Jimenez", "333333333", "marcos@correo.com", "password111", 
                                      "Saludos", "fotoUrl5", "01/01/2000");
        controlador.iniciarSesion("333333333", "password111");
        controlador.cerrarSesion();
        assertNull(controlador.getUsuarioActual(), "El usuario debería cerrar sesión correctamente.");
    }
}
