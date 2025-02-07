package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;


public class ChatIndividual extends Contacto {
    private Usuario contacto;
    private String numeroTelefono;

    public ChatIndividual(String nombre, String numeroTelefono, Usuario contacto) {
        super(nombre);
    	this.contacto = contacto;
        this.numeroTelefono = numeroTelefono;
    }
    public ChatIndividual(String nombre, String numeroTelefono, LinkedList<Mensaje>mensajes,  Usuario contacto) {
        super(nombre, mensajes);
    	this.contacto = contacto;
        this.numeroTelefono = contacto.getNumeroTelefono();
    }
    



	public Usuario getContacto() { return contacto; }
	public String getnumeroTelefono() { return numeroTelefono; }

	public void setnumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
	public void setContacto(Usuario contacto) { this.contacto = contacto; }

	
	
	
	
	public ChatIndividual getChatIndividual(Usuario contacto) {
		return this.contacto.getContactos().stream().filter(c -> c instanceof ChatIndividual)
				.map(c -> (ChatIndividual) c).filter(c -> c.getContacto().equals(contacto)).findAny().orElse(null);
	}
	
	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
		ChatIndividual chat = getChatIndividual(usuario.orElse(null));
		return (chat != null) ? chat.getMensajesEnviados() : new LinkedList<>();

	}
	
	public String getUltimoMensaje() {
		if (getMensajesEnviados().isEmpty())
			return "";
		return getMensajesEnviados().get(getMensajesEnviados().size() - 1).getTexto();
	}
	
	@Override
	public String getFoto() {
         return contacto.getFotoPerfil();
	}
	
	public void addGrupo(Grupo g) {
		this.contacto.addContacto(g);
	}

	public void removeGrupo(Grupo g) {
		this.contacto.removeContacto(g);
	}
	
	public void modificarGrupo(Grupo g) {
		List<Grupo> grupos = contacto.getGrupos();
		grupos.remove(g);
		grupos.add(g);
	}
	
	public void eliminarGrupo(Grupo g) {
		this.contacto.removeContacto(g);
	}
	
	public String getSaludo() {
	    return contacto.getSaludo() != "Write a greeting message (optional)" ? contacto.getSaludo() : "Hey there! I'm using AppChat!";
	}

	
	@Override
	public String toString() {
		return super.toString() + "ChatIndividual [contacto=" + contacto + ", numeroTelefono=" + numeroTelefono + "]";
	}

	
	
	

}
