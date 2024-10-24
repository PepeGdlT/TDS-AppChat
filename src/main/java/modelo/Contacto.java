package modelo;

class Contacto {
    String nombre;
    String telefono;


    public Contacto(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }




    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Teléfono: " + telefono + "\n";
    }
}

