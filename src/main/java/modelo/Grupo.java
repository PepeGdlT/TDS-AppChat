package modelo;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

import vista.IconsResource;

public class Grupo extends Contacto {

	private Usuario administrador;
    public List<ChatIndividual> miembros;

    // ----------------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------------
    
    public Grupo(String nombreGrupo, Usuario administrador) {
    	super(nombreGrupo);
    	this.administrador = administrador;
        this.miembros = new LinkedList<ChatIndividual>();
    }

	public Grupo(String nombreGrupo, List<Mensaje> mensajes, List<ChatIndividual> listaMiembros, Usuario administrador) {
		super(nombreGrupo, mensajes);
		this.administrador = administrador;
		this.miembros = new LinkedList<ChatIndividual>();
	}

    
	// ----------------------------------------------------------------------------
	// Getters
	// ----------------------------------------------------------------------------


	public List<ChatIndividual> getListaMiembros() {
		return miembros;
	}

	public String getnombreGrupo() {
        return super.getNombre();
    }
	
	public void setListaMiembros(List<ChatIndividual> listaMiembros) {
		this.miembros = listaMiembros;
	}

	public void setAdministrador(Usuario administrador) {
		this.administrador = administrador;
	}
	
	public Usuario getAdministrador() {
		return administrador;
	}
	

	@Override
	public List<Mensaje> getMensajesRecibidos(Optional<Usuario> emptyOpt) {
		return this.miembros.stream().flatMap(c -> c.getUsuario().getContactos().stream())
				.filter(c -> c instanceof Grupo).map(c -> (Grupo) c).filter(g -> this.equals(g))
				.flatMap(g -> g.getMensajesEnviados().stream()).collect(Collectors.toList());
	}

	public List<Mensaje> removeMensajesRecibidos() {
		List<Mensaje> recibidos = getMensajesRecibidos(Optional.empty());
		List<Mensaje> copia = new LinkedList<Mensaje>(recibidos);
		recibidos.clear();
		return copia;
	}

	@Override
	public String getFoto() {
		ImageIcon imagen =  IconsResource.GROUP_ICON;
		imagen.setDescription((IconsResource.GROUP_ICON).toString()); ;
		return imagen.getDescription();
	}
	
	
	public void addMiembro(ChatIndividual chat) {
		miembros.add(chat);
	}

	
	
	// ----------------------------------------------------------------------------
	// HashCode e Equals
	// ----------------------------------------------------------------------------


	@Override
	public int hashCode() {
		final int prime = 73;
		int result = 1;
		result = prime * result + ((getnombreGrupo() == null) ? 0 : getnombreGrupo().hashCode());
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
		Grupo otro = (Grupo) obj;
		if (getnombreGrupo() == null) {
			if (otro.getnombreGrupo() != null)
				return false;
		} else if (!getnombreGrupo().equals(otro.getnombreGrupo()))
			return false;
		return true;
	}




}
