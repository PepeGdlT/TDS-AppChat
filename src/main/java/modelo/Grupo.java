package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Grupo {
    private int codigo;
    private String nombre;
    private Usuario administrador;
    private List<Mensaje> mensajes;
    private List<ChatIndividual> miembros;

    // Constructores
    public Grupo(String nombre, Usuario administrador) {
        this.nombre = nombre;
        this.administrador = administrador;
        this.mensajes = new LinkedList<>();
        this.miembros = new LinkedList<>();
    }

    public Grupo(String nombre, List<Mensaje> mensajes, List<ChatIndividual> miembros, Usuario administrador) {
        this.nombre = nombre;
        this.mensajes = mensajes;
        this.miembros = miembros;
        this.administrador = administrador;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
    }

    public List<ChatIndividual> getListaMiembros() {
        return miembros;
    }

    public void setListaMiembros(List<ChatIndividual> listaMiembros) {
        this.miembros = listaMiembros;
    }

    public List<Mensaje> getMensajesEnviados() {
        return mensajes;
    }

    public List<Mensaje> getMensajesRecibidos() {
        return this.miembros.stream()
                .flatMap(miembro -> miembro.getMensajesEnviados().stream())
                .collect(Collectors.toList());
    }

	public void setMensajesEnviados(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}
    
    public void addMiembro(ChatIndividual chat) {
        miembros.add(chat);
    }

    // Métodos de gestión de mensajes
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
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Grupo otro = (Grupo) obj;
        return nombre != null && nombre.equals(otro.nombre);
    }

    // toString para mostrar el nombre del grupo
    @Override
    public String toString() {
        return nombre;
    }
}
