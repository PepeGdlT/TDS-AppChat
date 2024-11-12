package modelo;

import java.util.LinkedList;
import java.util.List;

public class ChatIndividual {
    private int codigo;
    private Usuario contacto;
    private List<Mensaje> mensajes;

    // Constructor
    public ChatIndividual(Usuario contacto) {
        this.contacto = contacto;
        this.mensajes = new LinkedList<>();
    }

    // Getters y Setters
    public String getNombre() {
        return contacto.getNombreCompleto();
    }

    public String getNumeroTelefono() {
        return contacto.getNumeroTelefono();
    }

	public Usuario getContacto() {
		return contacto;
	}
	public void setContacto(Usuario contacto) {
		this.contacto = contacto;
	}
    
    public String getFoto() {
        return contacto.getFotoPerfilURL(); // Devuelve la URL de la foto de perfil del contacto
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public List<Mensaje> getMensajesEnviados() {
        return mensajes;
    }

    // Métodos para gestionar mensajes
    public void enviarMensaje(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    public List<Mensaje> removeMensajesEnviados() {
        List<Mensaje> copia = new LinkedList<>(mensajes);
        mensajes.clear();
        return copia;
    }

    // HashCode y Equals para comparación
    @Override
    public int hashCode() {
        final int prime = 73;
        int result = 1;
        result = prime * result + (contacto.getNumeroTelefono() != null ? contacto.getNumeroTelefono().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ChatIndividual other = (ChatIndividual) obj;
        return contacto.getNumeroTelefono().equals(other.contacto.getNumeroTelefono());
    }

    // toString para mostrar el nombre del contacto
    @Override
    public String toString() {
        return contacto.getNombreCompleto();
    }
}
