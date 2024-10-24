package modelo;

import java.util.LinkedList;

import com.toedter.calendar.JCalendar;

public class Mensaje {

    private String texto;
    private JCalendar fecha;
    private String hora;
    private String emisor;
    private String receptor;

    public Mensaje(String texto, JCalendar fecha, String hora, String emisor, String receptor) {
        this.texto  = texto;
        this.fecha = fecha;
        this.hora = hora;
        this.emisor = emisor;
        this.receptor = receptor;
    }
    public Mensaje(String texto, JCalendar fecha, String hora, String emisor, LinkedList<String> receptores) {
        this.texto = texto;
        this.fecha = fecha;
        this.hora = hora;
        this.emisor = emisor;
    }

    public String getTexto() {
        return texto;
    }

    public JCalendar getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEmisor() {
        return emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "contenido='" + texto + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", emisor='" + emisor + '\'' +
                ", receptor='" + receptor + '\'' +
                '}';
    }


}
