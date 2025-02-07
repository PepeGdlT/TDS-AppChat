package modelo;

public class DescuentoPorMensaje implements Descuento {
    private double descuento;
    private int cantidadMensajesRequeridos;

    public DescuentoPorMensaje(double descuento, int cantidadMensajesRequeridos) {
        this.descuento = descuento;
        this.cantidadMensajesRequeridos = cantidadMensajesRequeridos;
    }

    @Override
    public double getDescuento(Usuario usuario) {
        return usuario.getMensajesEnviadosUltimoMes() >= cantidadMensajesRequeridos ? descuento : 0;
    }
}
