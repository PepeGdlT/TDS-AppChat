package modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Usuario {
    private static final String SALUDO = "Hey there, I'm using AppChat!";
    
    private int codigo;
    private String nombreCompleto;
    private String numeroTelefono;
    private String email;
    private String contrasena;
    private String saludo;
    private String fotoPerfil;
    private String fechaNacimiento;
    private List<Contacto> contactos;
    private boolean premium;
    private LocalDate fechaRegistro;
    private int mensajesEnviadosUltimoMes; 

    public Usuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfil, String fechaNacimiento, LocalDate fechaRegistro) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = saludo != null ? saludo : SALUDO; 
        this.fotoPerfil = fotoPerfil;
        this.fechaNacimiento = fechaNacimiento;
        this.contactos = new LinkedList<>();
        this.premium = false;
        this.fechaRegistro = fechaRegistro;
        this.mensajesEnviadosUltimoMes = 0; 
    }


	// Getters
    public int getCodigo() { return codigo; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getNumeroTelefono() { return numeroTelefono; }
    public String getEmail() { return email; }
    public String getContrasena() { return contrasena; }
    public String getSaludo() { return saludo; }
    public String getFotoPerfil() { return fotoPerfil; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public List<Contacto> getContactos() { return contactos; }
    public boolean isPremium() { return premium; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public int getMensajesEnviadosUltimoMes() { return mensajesEnviadosUltimoMes; }

    // Setters
    public void setCodigo(int id) { this.codigo = id; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setNumeroTelefono(String numeroTelefono) { this.numeroTelefono = numeroTelefono; }
    public void setEmail(String email) { this.email = email; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setSaludo(String saludo) { this.saludo = saludo; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; } 
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setContactos(List<Contacto> contactos) { this.contactos = contactos; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public void setMensajesEnviadosUltimoMes(int cantidad) { this.mensajesEnviadosUltimoMes = cantidad; } 

    // Métodos para gestión de contactos
    public void addContacto(Contacto contacto) { this.contactos.add(contacto); }
    public void removeContacto(Contacto contacto) { this.contactos.remove(contacto); }

    public List<ChatIndividual> getChatsIndividuales() {
        return contactos.stream().filter(c -> c instanceof ChatIndividual)
                .map(c -> (ChatIndividual) c)
                .collect(Collectors.toList());
    }

    public List<Grupo> getGrupos() {
        return contactos.stream().filter(c -> c instanceof Grupo)
                .map(c -> (Grupo) c)
                .collect(Collectors.toList());
    }

    public boolean existeContacto(Contacto contacto) {
        return contactos.contains(contacto);
    }
    
    public boolean existeChatIndividual(String nombre) {
        return getChatsIndividuales().stream().anyMatch(c -> c.getNombreContacto().equals(nombre));
    }

    public boolean existeGrupo(String grupo) {
        return getGrupos().stream().anyMatch(g -> g.getNombreContacto().equals(grupo));
    }

    public void setChatIndividuales(List<ChatIndividual> chats) {
        this.contactos.removeIf(c -> c instanceof ChatIndividual);
        this.contactos.addAll(chats);
    }

    public void setGrupos(List<Grupo> grupos) {
        this.contactos.removeIf(c -> c instanceof Grupo);
        this.contactos.addAll(grupos);
    }

}
