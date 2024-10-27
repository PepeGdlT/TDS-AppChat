package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class Contacto {
	
	private int codigo;
	private String nombre;
	private List<Mensaje> mensajes;


	public Contacto(String nombre) {
		this(nombre, new LinkedList<>());
	}


	public Contacto(String nombre, List<Mensaje> mensajes) {
		this.nombre = nombre;
		this.mensajes = mensajes;
	}


	public String getNombre() {
		return nombre;
	}


	public List<Mensaje> getMensajesEnviados() {
		return mensajes;
	}


	public abstract List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario);


	public int getCodigo() {
		return codigo;
	}


	public abstract String getFoto();


	
	//SETTERS
	
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public List<Mensaje> removeMensajesEnviados() {
		List<Mensaje> lista = new LinkedList<>(mensajes); // copia
		mensajes.clear();
		return lista;
	}

	public void addMensajes(List<Mensaje> mensajes) {
		this.mensajes.addAll(mensajes);
	}
	

	// Methods
	public void sendMessage(Mensaje message) {
		mensajes.add(message);
	}

	@Override
	public String toString() {
		return nombre;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
