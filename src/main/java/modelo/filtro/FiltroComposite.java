package modelo.filtro;

import modelo.Mensaje;
import java.util.ArrayList;
import java.util.List;

public class FiltroComposite implements Filtro {
    private List<Filtro> filtros = new ArrayList<>();

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    @Override
    public List<Mensaje> filtrar(List<Mensaje> mensajes) {
        List<Mensaje> resultado = new ArrayList<>(mensajes);
        for (Filtro filtro : filtros) {
            resultado = filtro.filtrar(resultado);
        }
        return resultado;
    }
}
