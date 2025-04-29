package vista.utils;

import javax.swing.*;
import modelo.Grupo;

public class GrupoVisor extends Visor {

    public GrupoVisor(String nombre, String fotoUrl, String ultimoMensaje) {
        // Llamamos al constructor de la clase base Visor
        super(nombre, fotoUrl, ultimoMensaje);

        // Establecer el nombre del grupo directamente
        lblNombre.setText(nombre);

        // Establecer el último mensaje del grupo
        lblUltimoMensaje.setText(ultimoMensaje);
    }

    // Implementación del método abstracto
    @Override
    protected void setNombreYUltimoMensaje(String ultimoMensaje) {
        // Aquí ya no es necesario verificar el tipo de contacto, ya que no tenemos el objeto completo
        lblUltimoMensaje.setText(ultimoMensaje != null && !ultimoMensaje.trim().isEmpty() ? ultimoMensaje : "No hay mensajes");
    }

    @Override
    public String toString() {
        return lblNombre.getText();  // Mostrar solo el nombre del grupo
    }
}
