package tds;

import java.awt.EventQueue;
import vista.VentanaInicio;

public class Lanzador {
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Crear instancia de VentanaInicio
                    VentanaInicio ventana = new VentanaInicio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
