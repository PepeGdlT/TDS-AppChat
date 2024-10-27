package modelo;

import java.io.ObjectInputFilter.Status;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;

public class ChatIndividual extends Contacto {
	// Properties.
	private int movil;
	private Usuario usuario;

	// Constructor.
	public ChatIndividual(String nombre, int movil, Usuario usuario) {
		super(nombre);
		this.movil = movil;
		this.usuario = usuario;
	}

	public ChatIndividual(String nombre, LinkedList<Mensaje> mensajes, int movil, Usuario usuario) {
		super(nombre, mensajes);
		this.movil = movil;
		this.usuario = usuario;
	}

	// Getters.
	public int getMovil() {
		return movil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public String getFoto() {
		return usuario.getFotoPerfilURL();
	}

	// Setters.
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// Methods

	public ChatIndividual getContacto(Usuario usuario) {
		return this.usuario.getContactos().stream().filter(c -> c instanceof ChatIndividual)
				.map(c -> (ChatIndividual) c).filter(c -> c.getUsuario().equals(usuario)).findAny().orElse(null);
	}

	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario) {
		ChatIndividual contacto = getContacto(usuario.orElse(null));
		if (contacto != null) {
			return contacto.getMensajesEnviados();
		} else
			return new LinkedList<>();
	}


	// Añade al contacto al grupo en cuestion
	public void addGrupo(Grupo grupo) {
		usuario.addGrupoAdmin(grupo);
	}



	public void modificarGrupo(Grupo g) {
		List<Grupo> grupos = usuario.getGruposAdmin();

		grupos.remove(g);
		grupos.add(g);
	}

	// Borra los mensajes que le ha mandado este contacto al usuarioActual
	public List<Mensaje> removeMensajesRecibidos(Usuario usuarioActual) {
		List<Mensaje> recibidos = getContacto(usuarioActual).getMensajesEnviados();
		List<Mensaje> copia = new LinkedList<>(recibidos);
		recibidos.clear();
		return copia;
	}

	// HashCode e Equals

	@Override
	public int hashCode() {
		final int prime = 31; // cambiar
		int result = 1;
		result = prime * result + movil;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatIndividual other = (ChatIndividual) obj;
		if (movil != other.movil)
			return false;
		return true;
	}

	public boolean isUser(Usuario otherUser) {
		return usuario.equals(otherUser);
	}

}
