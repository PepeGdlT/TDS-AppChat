package vista; 

import java.awt.Color;

public class ElegantPalette {
    
    // Fondo general (Negro elegante)
    public static final Color BACKGROUND = new Color(16,16,16); // #101010

    // Panel o cuadros secundarios (Gris oscuro)
    public static final Color PANEL_BACKGROUND = new Color(43, 43, 43); // #2B2B2B

    // Texto primario (Blanco)
    public static final Color PRIMARY_TEXT = new Color(255, 255, 255); // #FFFFFF

    // Texto secundario (Gris claro)
    public static final Color SECONDARY_TEXT = new Color(180, 180, 180); // #B4B4B4

    // Botón de acción (Verde brillante)
    public static final Color ACTION_BUTTON = new Color(0, 255, 0); // #00FF00

    // Fondo del botón en hover (Verde más oscuro)
    public static final Color ACTION_BUTTON_HOVER = new Color(0, 200, 0); // #00C800

    // Texto del botón de acción (Negro para buen contraste)
    public static final Color BUTTON_TEXT = new Color(0, 0, 0); // #000000

    // Bordes (Un verde suave que no sea intrusivo)
    public static final Color BORDER_COLOR = new Color(0, 100, 0); // #006400

    // Campos de texto (gris oscuro)
    public static final Color TEXT_FIELD_BACKGROUND = new Color(30, 30, 30); // #1E1E1E

    // Texto dentro de los campos de texto (blanco suave)
    public static final Color TEXT_FIELD_TEXT_PREV = new Color(200, 200, 200); // #DCDCDC
    public static final Color TEXT_FIELD_TEXT = new Color(250, 250, 250);

    // Texto de enlaces o acciones secundarias (Verde claro para destacarse)
    public static final Color LINK_TEXT = new Color(0, 255, 128); // #00FF80

 // Fondo en hover (Gris más claro)
    public static final Color HOVER_BACKGROUND = new Color(60, 60, 60); // #3C3C3C


    // Constructor privado para prevenir instanciación
    private ElegantPalette() {
        // Esta clase solo contiene colores estáticos, no es necesario instanciarla.
    }
}