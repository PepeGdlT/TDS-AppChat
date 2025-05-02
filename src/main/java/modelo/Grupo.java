package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;
import vista.utils.IconsResource;

public class Grupo extends Contacto {
    private Usuario administrador;
    private List<ChatIndividual> miembros;

    public Grupo(String nombre, List<ChatIndividual> miembros, Usuario administrador) {
        super(nombre);
        this.administrador = administrador;
        this.miembros = miembros;
    }

    public Grupo(String nombre, List<Mensaje> mensajes, List<ChatIndividual> miembros, Usuario administrador) {
        super(nombre, mensajes);
        this.miembros = miembros;
        this.administrador = administrador;
    }

    public Usuario getAdministrador() { return administrador; }
    public List<ChatIndividual> getMiembros() { return miembros; }

    public void setAdministrador(Usuario administrador) { this.administrador = administrador; }
    public void setMiembros(List<ChatIndividual> miembros) { this.miembros = miembros; }

    @Override
    public List<Mensaje> getMensajesEnviados() {
        return super.getMensajesEnviados();
    }

    @Override
    public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
        return new LinkedList<>();
    }

    @Override
    public String getFoto() {
        return IconsResource.AVATAR.getDescription();
    }

    public boolean existeContacto(Usuario usuario) {
        return miembros.stream().anyMatch(m -> m.getContacto().equals(usuario));
    }

    public String getUltimoMensaje() {
        return getMensajesEnviados().stream()
            .max((m1, m2) -> m1.getHora().compareTo(m2.getHora()))
            .map(Mensaje::getTexto)
            .orElse("");
    }

    @Override
    public String toString() {
        return super.toString() + "Grupo [administrador=" + administrador + ", miembros=" + miembros + "]";
    }
}