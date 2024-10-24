package modelo;

import com.toedter.calendar.JCalendar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Usuario {

    public String nombreCompleto;
    public String numeroTelefono;
    public String email;
    public String contrasena;
    public String saludo;
    public String fotoPerfilURL;
    public JCalendar fechaNacimiento;
    public LinkedList<Contacto> contactos;
    public LinkedList<LinkedList<Contacto>> grupos;
    public ArrayList<Mensaje> mensajes;
    public boolean Premium;



    public Usuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, JCalendar fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = saludo;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaNacimiento = fechaNacimiento;
        this.contactos = new LinkedList<Contacto>();
        this.Premium = false; // Por defecto, el usuario no es premium
    }

    public void agregarContacto(Contacto contacto) {
        contactos.add(contacto);
    }


    public void eliminarContacto(String telefono) {
        for (Contacto contacto : contactos) {
            if (contacto.getTelefono().equals(telefono)) {
                contactos.remove(contacto);
                break;
            }
        }
    }



    public List<Mensaje> buscarMensajes(String texto, String telefono, String nombreContacto) {
        List<Mensaje> resultados = new ArrayList<>();

        // Loop through all messages the user has sent and received.
        for (Mensaje mensaje : this.mensajes) {
            // Apply filters: by text, by phone, or by contact name.
            if ((texto == null || mensaje.getTexto().contains(texto)) &&
                    (telefono == null || mensaje.getEmisor().equals(telefono) || mensaje.getReceptor().equals(telefono)) &&
                    (nombreContacto == null || this.contactoConNombre(mensaje.getEmisor(), nombreContacto) ||
                            this.contactoConNombre(mensaje.getReceptor(), nombreContacto))) {
                resultados.add(mensaje);
            }
        }
        return resultados;
    }

    private boolean contactoConNombre(String telefono, String nombreContacto) {
        // Check if this user has a contact with the given phone number and name.
        for (Contacto contacto : this.contactos) {
            if (contacto.getTelefono().equals(telefono) && contacto.getNombre().equals(nombreContacto)) {
                return true;
            }
        }
        return false;
    }
        

}



