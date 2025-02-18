package modelo.filtro;

import java.util.List;

import modelo.Mensaje;

public interface Filtro {
    List<Mensaje> filtrar(List<Mensaje> mensajes);
}
