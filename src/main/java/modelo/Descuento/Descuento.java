package modelo.Descuento;

import modelo.Usuario;

public interface Descuento {
    double getDescuento(Usuario usuario);
    boolean esAplicable(Usuario usuario);  
}
