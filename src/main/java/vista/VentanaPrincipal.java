package vista;


import java.awt.*;
import javax.swing.*;

public class VentanaPrincipal extends JPanel {
	
    private VentanaInicio mainFrame; // Referencia a VentanaInicio
    private JList<ContactoItem> listaContactos; // Lista de contactos
    private DefaultListModel<ContactoItem> modeloContactos; // Modelo de la lista
    
    public VentanaPrincipal() {
    	setLayout(new BorderLayout(0, 0));
    	
    	// Panel superior (barra de botones)
    	JPanel panelOpciones = new JPanel();
    	add(panelOpciones, BorderLayout.NORTH);
    	
    	JLabel lblNewLabel = new JLabel("contacto o telefono");
    	panelOpciones.add(lblNewLabel);
    	
    	JButton btnNewButton = new JButton("buscar");
    	panelOpciones.add(btnNewButton);
    	
    	JButton btnNewButton_1 = new JButton("contactos");
    	panelOpciones.add(btnNewButton_1);
    	
    	JButton btnNewButton_2 = new JButton("premium");
    	panelOpciones.add(btnNewButton_2);
    	
    	JLabel lblNewLabel_1 = new JLabel("$usuario_actual");
    	panelOpciones.add(lblNewLabel_1);
    	
    	// Panel izquierdo (lista de contactos)
    	JPanel panelListaContactos = new JPanel();
    	add(panelListaContactos, BorderLayout.WEST);
    	panelListaContactos.setLayout(new BorderLayout()); // Cambiar layout para mejor organización
    	
    	// Modelo para la lista de contactos
    	modeloContactos = new DefaultListModel<>();
    	
    	// Crear la lista de contactos y aplicar el renderer personalizado
    	listaContactos = new JList<>(modeloContactos);
    	listaContactos.setCellRenderer(new ContactoListRenderer());
    	
    	// Agregar la lista de contactos a un JScrollPane para que sea scrollable
    	JScrollPane scrollPane = new JScrollPane(listaContactos);
    	panelListaContactos.add(scrollPane, BorderLayout.CENTER); // Asegúrate que ocupa todo el panel
    	
    	// Panel derecho (conversación, para más adelante)
    	JPanel panelMensajes = new JPanel();
    	add(panelMensajes, BorderLayout.CENTER); // Usar BorderLayout.CENTER para más espacio
    	panelMensajes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	
    	JLabel lblNewLabel_2 = new JLabel("mensajes....");
    	panelMensajes.add(lblNewLabel_2);

    }

    
    
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
// 	      			ESTO FALTA AL CONTROLADOR 													//
//////////////////////////////////////////////////////////////////////////////////////////////////    
//////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    //TODO:  Controlador
    
    
    // Método para agregar un contacto a la lista
    public void agregarContacto(ContactoItem contacto) {
    	modeloContactos.addElement(contacto);
    }
	
    public void setMainFrame(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
    }
}
