package vista;


import java.awt.*;
import javax.swing.*;
import tds.BubbleText;
import javax.swing.border.LineBorder;

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
    	JPanel chat = new JPanel();
    	add(chat, BorderLayout.CENTER); // Usar BorderLayout.CENTER para más espacio
    	chat.setLayout(new BoxLayout(chat,BoxLayout.Y_AXIS));
    	chat.setBackground(ElegantPalette.BACKGROUND);
    	chat.setSize(500,500); 
    	chat.setMinimumSize(new Dimension(400,700)); 
    	chat.setMaximumSize(new Dimension(400,700)); 
    	chat.setPreferredSize(new Dimension(400,700)); 
    	

    	JScrollPane chatScrollPane = new JScrollPane(chat);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScrollPane.setPreferredSize(new Dimension(400, 700)); 
        add(chatScrollPane, BorderLayout.CENTER); 
        
        
        BubbleText burbuja; 
        burbuja=new BubbleText(chat,"Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT); 
        chat.add(burbuja); 
        
        BubbleText burbuja1; 
        burbuja1=new BubbleText(chat,"Hola grupo!!", Color.GREEN, "J.Ramón", BubbleText.SENT); 
        chat.add(burbuja1); 
        
        BubbleText burbuja2; 
        burbuja2=new BubbleText(chat,"Hola grupo!!", Color.GREEN, "J.Ramón", 1); 
        chat.add(burbuja2); 
        
        BubbleText burbuja3; 
        burbuja3=new BubbleText(chat,"Hola grupo!!", Color.GRAY, "J.Ramón", 1); 
        chat.add(burbuja3); 
    	

        BubbleText burbujae=new BubbleText(chat, 20, Color.GREEN, "J.Ramón", BubbleText.SENT,18); 
        chat.add(burbujae);
        

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
