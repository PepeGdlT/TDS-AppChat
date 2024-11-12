package modelo;

import java.time.LocalDateTime;

public class Mensaje implements Comparable<Mensaje> {
    private int codigo;
    private String texto;
    private int emoticono;
    private LocalDateTime hora;
    private Usuario emisor;
    private Object receptor; // Puede ser Grupo o ChatIndividual

    // Constructor para mensajes de texto
    public Mensaje(String texto, LocalDateTime hora, Usuario emisor, Object receptor) {
        this.texto = texto;
        this.hora = hora;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    // Constructor para mensajes de emoticono
    public Mensaje(int emoticono, LocalDateTime hora, Usuario emisor, Object receptor) {
        this.texto = "";
        this.hora = hora;
        this.emoticono = emoticono;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    // Constructor para mensajes de texto y emoticono
    public Mensaje(String texto, int emoticono, LocalDateTime hora) {
        this.texto = texto;
        this.emoticono = emoticono;
        this.hora = hora;
    }

    // Getters
    public String getTexto() {
        return texto;
    }

    public LocalDateTime getHora() {
        return hora;
    }

    public int getEmoticono() {
        return emoticono;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public Object getReceptor() {
        return receptor;
    }

    public int getCodigo() {
        return codigo;
    }

    // Setters
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setReceptor(Object receptor) {
        this.receptor = receptor;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    // Comparator
    @Override
    public int compareTo(Mensaje o) {
        return hora.compareTo(o.hora);
    }
}
