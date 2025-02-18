package modelo.filtro;

import modelo.Mensaje;

public class FiltroTexto extends FiltroBase {
    public FiltroTexto(String criterio) {
        super(criterio);
    }

    @Override
    protected boolean cumpleCriterio(Mensaje mensaje) {
        return mensaje.getTexto().toLowerCase().contains(criterio);
    }
}
