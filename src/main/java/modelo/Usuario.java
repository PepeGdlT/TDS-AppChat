package modelo;


import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;




public class Usuario {

	private static final String SALUDO = "Hey there, I'm using AppChat!";
	
	private int codigo;
    public String nombreCompleto;
    public String numeroTelefono;
    public String email;
    public String contrasena;
    public String saludo;
    public String fotoPerfilURL;
    public String fechaNacimiento;
    public List<ChatIndividual> chatIndividuales;
    public List<Grupo> grupos;
    public boolean Premium;

    public Usuario(String nombreCompleto, String numeroTelefono, String email, String contrasena, String saludo, String fotoPerfilURL, String fechaNacimiento) {
        this.nombreCompleto = nombreCompleto;
        this.numeroTelefono = numeroTelefono;
        this.email = email;
        this.contrasena = contrasena;
        this.saludo = saludo;
        this.fotoPerfilURL = fotoPerfilURL;
        this.fechaNacimiento = fechaNacimiento;
        this.chatIndividuales = new LinkedList<ChatIndividual>();
        this.grupos = new LinkedList<Grupo>();
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



	public String getNumeroTelefono() {
		return numeroTelefono;
	}



	public void setNumeroTelefono(String numeroTelefono) {
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



	public String getFechaNacimiento() {
		return fechaNacimiento;
	}



	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}



	public List<ChatIndividual> getChatIndividuales() {
		return chatIndividuales;
	}



	public void setChatIndividuales(List<ChatIndividual> chatIndividuales) {
		this.chatIndividuales = chatIndividuales;
	}



	public List<Grupo> getGrupos() {
		return grupos;
	}



	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}



	public boolean isPremium() {
		return Premium;
	}



	public void setPremium(boolean premium) {
		Premium = premium;
	}



	public void addGrupo(Grupo g) {
		grupos.add(g);
	}
	
	public void removeGrupo(Grupo g) {
		grupos.remove(g);
	}
	
	public void addChat(ChatIndividual c) {
		chatIndividuales.add(c);
	}


	public void setCodigo(int id) {
		this.codigo = id;
    }
		
	




}




