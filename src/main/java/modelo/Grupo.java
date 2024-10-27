package modelo;

import java.util.LinkedList;
import java.util.List;

public class Grupo {

    public String nombreGrupo;
    public String descripcion;
    public String fotoGrupoURL;
    public List<ChatIndividual> listaMiembros;

    public Grupo(String nombreGrupo, String descripcion, String fotoGrupoURL) {
        this.nombreGrupo = nombreGrupo;
        this.descripcion = descripcion;
        this.fotoGrupoURL = fotoGrupoURL;
        this.listaMiembros = new LinkedList<ChatIndividual>();
    }


    

    public void cambiarFotoGrupo(String fotoGrupoURL){
        this.fotoGrupoURL = fotoGrupoURL;
    }

    public void cambiarDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public List<ChatIndividual> getListaMiembros() {
        return listaMiembros;
    }
	public void setListaMiembros(List<ChatIndividual> contactos) {
		this.listaMiembros = contactos;
	}
	
	
	// HashCode e Equals

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getNombreGrupo() == null) ? 0 : getNombreGrupo().hashCode());
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
		Grupo other = (Grupo) obj;
		if (getNombreGrupo() == null) {
			if (other.getNombreGrupo() != null)
				return false;
		} else if (!getNombreGrupo().equals(other.getNombreGrupo()))
			return false;
		return true;
	}
}
