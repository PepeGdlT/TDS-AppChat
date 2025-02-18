package tds;

import java.awt.EventQueue;
import java.io.File;

import controlador.ControladorAppChat;
import vista.Ventana.VentanaInicio;

public class Lanzador {
    public static void main(final String[] args) {
    	
    	
    	
    	/*
    	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/k",
    			"java -jar ServidorPersistenciaH2.jar");
    	pb.directory(new File("."));
    	
    	try {
			pb.start();
		} catch (Exception e) {
			e.printStackTrace();
    	}
	    */
    	
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