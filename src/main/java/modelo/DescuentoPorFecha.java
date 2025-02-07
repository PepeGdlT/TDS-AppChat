package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DescuentoPorFecha implements Descuento {
    private double descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public DescuentoPorFecha(double descuento, String fechaInicio, String fechaFin) {
        this.descuento = descuento;
        this.fechaInicio = LocalDate.parse(fechaInicio, DateTimeFormatter.ISO_DATE);
        this.fechaFin = LocalDate.parse(fechaFin, DateTimeFormatter.ISO_DATE);
    }

    @Override
    public double getDescuento(Usuario usuario) {
        LocalDate fechaRegistro = usuario.getFechaRegistro();
        boolean estaEnRango = (fechaRegistro.isAfter(fechaInicio) || fechaRegistro.isEqual(fechaInicio)) &&
                              (fechaRegistro.isBefore(fechaFin) || fechaRegistro.isEqual(fechaFin));

        return estaEnRango ? descuento : 0;
    }
}
