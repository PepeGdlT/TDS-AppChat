package modelo;

import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private static final String SALUDO = "Hey there, I'm using AppChat!";
    
    private int codigo;
    private String nombreCompleto;
    private String numeroTelefono;
    private String email;
    private String contrasena;
    private String saludo;
    private String fotoPerfilURL;
    private String fechaNacimiento;
    private List<ChatIndividual> chatIndividuales;
    private List<Grupo> grupos;
    private boolean premium;

    public Usuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = saludo;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaNacimiento = fechaNacimiento;
        this.chatIndividuales = new LinkedList<>();
        this.grupos = new LinkedList<>();
        this.premium = false; 
    }

    public int getCodigo() { return codigo; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public String getSaludo() { return saludo; }
    public String getFotoPerfilURL() { return fotoPerfilURL; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public List<ChatIndividual> getChatIndividuales() { return chatIndividuales; }
    public List<Grupo> getGrupos() { return grupos; }
    public boolean isPremium() { return premium; }

    public void setCodigo(int id) { this.codigo = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
    public void setEmail(String email) { this.email = email; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setSaludo(String saludo) { this.saludo = saludo; }
    public void setFotoPerfilURL(String fotoPerfilURL) { this.fotoPerfilURL = fotoPerfilURL; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setChatIndividuales(List<ChatIndividual> chatIndividuales) { this.chatIndividuales = chatIndividuales; }
    public void setGrupos(List<Grupo> grupos) { this.grupos = grupos; }
    public void setPremium(boolean premium) { this.premium = premium; }

    public void addGrupo(Grupo g) { grupos.add(g); }
    public void removeGrupo(Grupo g) { grupos.remove(g); }
    public void addChat(ChatIndividual c) { chatIndividuales.add(c); }
}
