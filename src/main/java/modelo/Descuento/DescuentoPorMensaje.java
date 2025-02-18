package modelo.Descuento;

import modelo.Usuario;

public class DescuentoPorMensaje implements Descuento {
    private double porcentaje;
    private int mensajesRequeridos;

    public DescuentoPorMensaje(double porcentaje, int mensajesRequeridos) {
        this.porcentaje = porcentaje;
        this.mensajesRequeridos = mensajesRequeridos;
    }

    @Override
    public double getDescuento(Usuario usuario) {
        return esAplicable(usuario) ? porcentaje : 0;
    }

    @Override
    public boolean esAplicable(Usuario usuario) {
        return usuario.getMensajesEnviadosUltimoMes() >= mensajesRequeridos;
    }
}
