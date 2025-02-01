package modelo;

import java.time.LocalDateTime;

public class Mensaje implements Comparable<Mensaje> {
    private int codigo;
    private String texto;
    private Integer emoticono;
    private LocalDateTime hora;
    private Usuario emisor;
    private Contacto receptor; // Puede ser Grupo o ChatIndividual

    // Constructor para mensajes de texto
    public Mensaje(String texto, LocalDateTime hora, Usuario emisor, Contacto receptor) {
        this.texto = texto;
        this.hora = hora;
        this.emisor = emisor;
        this.receptor = receptor;
        this.emoticono = -1;
    }

    // Constructor para mensajes de emoticono
    public Mensaje(int emoticono, LocalDateTime hora, Usuario emisor, Contacto receptor) {
        this.texto = "";
        this.hora = hora;
        this.emoticono = emoticono;
        this.emisor = emisor;
        this.receptor = receptor;
    }


    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Integer getEmoticono() { return emoticono; }
    public void setEmoticono(int emoticono) { this.emoticono = emoticono; }
    public LocalDateTime getHora() { return hora; }
    public void setHora(LocalDateTime hora) { this.hora = hora; }
    public Usuario getEmisor() { return emisor; }
    public void setEmisor(Usuario emisor) { this.emisor = emisor; }
    public Object getReceptor() { return receptor; }
    public void setReceptor(Contacto receptor) { this.receptor = receptor; }

    // Comparador para ordenamiento por hora
    @Override
    public int compareTo(Mensaje o) { return this.hora.compareTo(o.hora); }

    @Override
    public String toString() {
        return "Mensaje{" +
                "texto='" + texto + '\'' +
                ", emoticono=" + emoticono +
                ", hora=" + hora +
                ", emisor=" + emisor.getNombreCompleto() +
                '}';
    }
}
