package modelo;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.swing.ImageIcon;



public abstract class Contacto {
	private int codigo;
	private String nombreContacto;
	private List<Mensaje> mensajes;
	
	
	public Contacto(String nombre) {
		this(nombre, new LinkedList<>());
	}


	public Contacto(String nombreContacto, List<Mensaje> mensajes) {
		this.nombreContacto = nombreContacto;
		this.mensajes = mensajes;
	}

	public abstract List<Mensaje> getMensajesRecibidos(Optional<Usuario> usuario);
	public abstract String getFoto();
	

	public int getCodigo() { return codigo; }
	public String getNombreContacto() { return nombreContacto; }
	public List<Mensaje> getMensajesEnviados() { return mensajes; }
	
	
	
	public void setNombreAgregado(String nombreContacto) { this.nombreContacto = nombreContacto; }
	public void setCodigo(int codigo) { this.codigo = codigo; }
	public void setMensajesEnviados(List<Mensaje> mensajes) { this.mensajes = mensajes; }
	
	
	
	
	public void addMensajes(List<Mensaje> mensajes) {
		this.mensajes.addAll(mensajes);
	}
	
	
	public List<Mensaje> removeMensajesEnviados() {
		List<Mensaje> lista = new LinkedList<>(mensajes); 
		mensajes.clear();
		return lista;
	}

	public void enviarMensaje(Mensaje mensaje) {
		this.mensajes.add(mensaje);
	}
	

	@Override
	public String toString() {
		return "Contacto [codigo=" + codigo + ", nombreContacto=" + nombreContacto + ", mensajes=" + mensajes + "]";
	}
	
	
	
	
	
}
