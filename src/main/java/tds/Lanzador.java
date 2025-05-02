package tds;

import java.awt.EventQueue;
import java.io.File;

import controlador.ControladorAppChat;
import vista.Ventana.VentanaInicio;

public class Lanzador {
    public static void main(final String[] args) {
    	
    	
    
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaInicio ventana = new VentanaInicio();
                  
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}