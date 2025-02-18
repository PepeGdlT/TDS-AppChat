package modelo.filtro;

import modelo.Mensaje;

public class FiltroNombre extends FiltroBase {
    public FiltroNombre(String criterio) {
        super(criterio);
    }

    @Override
    protected boolean cumpleCriterio(Mensaje mensaje) {
        return mensaje.getReceptor().getNombreContacto().toLowerCase().contains(criterio);
    }
}
