package modelo.filtro;

import modelo.Mensaje;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FiltroBase implements Filtro {
    protected String criterio;

    public FiltroBase(String criterio) {
        this.criterio = criterio.toLowerCase();
    }

    protected abstract boolean cumpleCriterio(Mensaje mensaje);

    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        return mensajes.stream()
                .filter(this::cumpleCriterio)
                .collect(Collectors.toList());
    }
}
