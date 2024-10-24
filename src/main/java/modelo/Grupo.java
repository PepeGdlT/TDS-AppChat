package modelo;

import java.util.LinkedList;

public class Grupo {

    public String nombreGrupo;
    public String descripcion;
    public String fotoGrupoURL;
    public LinkedList<Contacto> listaMiembros;

    public Grupo(String nombreGrupo, String descripcion, String fotoGrupoURL) {
        this.nombreGrupo = nombreGrupo;
        this.descripcion = descripcion;
        this.fotoGrupoURL = fotoGrupoURL;
        this.listaMiembros = new LinkedList<Contacto>();
    }

    public void agregarMiembro(Contacto contacto){
        listaMiembros.add(contacto);
    }
    public void eliminarMiembro(String telefono){
        for (Contacto contacto : listaMiembros) {
            if (contacto.getTelefono().equals(telefono)) {
                listaMiembros.remove(contacto);
                break;
            }
        }
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
    public LinkedList<Contacto> getListaMiembros() {
        return listaMiembros;
    }
}
