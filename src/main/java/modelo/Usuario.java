package modelo;


import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;




public class Usuario {

	private static final String SALUDO = "Hey there, I'm using AppChat!";
	
	private int codigo;
    public String nombreCompleto;
    public int numeroTelefono;
    public String email;
    public String contrasena;
    public String saludo;
    public String fotoPerfilURL;
    public LocalDate fechaNacimiento;
    public List<Contacto> contactos;
    private List<Grupo> gruposAdmin;
    public boolean Premium;

    public Usuario(String nombreCompleto, int numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, LocalDate fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = saludo;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaNacimiento = fechaNacimiento;
        this.contactos = new LinkedList<Contacto>();
        this.Premium = false; 
    }
    
    public Usuario(String nombreCompleto, int numeroTelefono, String email, String contrasena, String fotoPerfilURL, LocalDate fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = SALUDO;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaNacimiento = fechaNacimiento;
        this.contactos = new LinkedList<Contacto>();
        this.Premium = false; 
    }
    

	public int getCodigo() {
		return codigo;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}



	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}



	public int getNumeroTelefono() {
		return numeroTelefono;
	}



	public void setNumeroTelefono(int numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getContrasena() {
		return contrasena;
	}



	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}



	public String getSaludo() {
		return saludo;
	}



	public void setSaludo(String saludo) {
		this.saludo = saludo;
	}



	public String getFotoPerfilURL() {
		return fotoPerfilURL;
	}



	public void setFotoPerfilURL(String fotoPerfilURL) {
		this.fotoPerfilURL = fotoPerfilURL;
	}



	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}



	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}



	public List<Contacto> getContactos() {
		return contactos;
	}



	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}



	public List<Grupo> getGruposAdmin() {
		return gruposAdmin;
	}



	public void setGruposAdmin(List<Grupo> gruposAdmin) {
		this.gruposAdmin = gruposAdmin;
	}



	public boolean isPremium() {
		return Premium;
	}



	public void setPremium(boolean premium) {
		Premium = premium;
	}



	public void addGrupoAdmin(Grupo g) {
		gruposAdmin.add(g);
	}
	
	public void removeGrupoAdmin(Grupo g) {
		gruposAdmin.remove(g);
	}
	
	public void addContacto(ChatIndividual c) {
		contactos.add(c);
	}


	public void setCodigo(int id) {
		this.codigo = id;
    }
		
	




}




