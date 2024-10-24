package modelo;

public class DescuentoPorMensaje implements Descuento{



	private double descuento;
    private int cantidadMensajes;


    @Override
    public double getDescuento(double precio) {
        return precio;
    }
    
    public double getDescuento() {
		return descuento;
	}


	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}


	public int getCantidadMensajes() {
		return cantidadMensajes;
	}


	public void setCantidadMensajes(int cantidadMensajes) {
		this.cantidadMensajes = cantidadMensajes;
	}
}
