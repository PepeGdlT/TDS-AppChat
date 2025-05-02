package modelo.filtro;

import modelo.Mensaje;

public class FiltroNombre extends FiltroBase {
    public FiltroNombre(String criterio) {
        super(criterio);
    }

    @Override
    protected boolean cumpleCriterio(Mensaje mensaje) {
        boolean coincideReceptor = mensaje.getReceptor().getNombreContacto().toLowerCase().contains(criterio);
        
        boolean coincideEmisor = mensaje.getEmisor().getNombreCompleto().toLowerCase().contains(criterio);
        
        return coincideReceptor || coincideEmisor;
    }
}
