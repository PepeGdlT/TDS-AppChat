package modelo;

public class DescuentoPorFecha implements Descuento {
    private double descuento;
    private String fechaInicio;
    private String fechaFin;


    @Override
    public double getDescuento(double precio) {
        return precio;
    }


	public String getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	public double getDescuento() {
		return descuento;
	}


	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}


	public String getFechaInicio() {
		return fechaInicio;
	}


	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
}
