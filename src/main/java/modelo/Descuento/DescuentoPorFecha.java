package modelo.Descuento;
import java.time.LocalDate;

import modelo.Usuario;

public class DescuentoPorFecha implements Descuento {
    private double porcentaje;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public DescuentoPorFecha(double porcentaje, String inicio, String fin) {
        this.porcentaje = porcentaje;
        this.fechaInicio = LocalDate.parse(inicio);
        this.fechaFin = LocalDate.parse(fin);
    }

    @Override
    public double getDescuento(Usuario usuario) {
        return esAplicable(usuario) ? porcentaje : 0;
    }

    @Override
    public boolean esAplicable(Usuario usuario) {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(fechaInicio) && !hoy.isAfter(fechaFin);
    }
}
