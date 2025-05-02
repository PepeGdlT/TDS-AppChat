package vista.Ventana;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class VentanaContactos extends JPanel {

    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JButton addButton, backButton;
    private JPopupMenu popupMenu;
    private JMenuItem itemVisualizar, itemEditar;
    private VentanaInicio mainFrame;

    public VentanaContactos(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Contactos");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Modelo de tabla
        tableModel = new DefaultTableModel(new String[]{"Nombre", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        add(scrollPane, BorderLayout.CENTER);

        // Menú contextual
        popupMenu = new JPopupMenu();
        itemVisualizar = new JMenuItem("Visualizar contacto");
        itemEditar = new JMenuItem("Editar contacto");
        popupMenu.add(itemVisualizar);
        popupMenu.add(itemEditar);

        contactTable.setComponentPopupMenu(popupMenu);
        contactTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = contactTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    contactTable.setRowSelectionInterval(row, row);
                }
            }
        });

        // Botones inferiores
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Volver a inicio
        backButton = new JButton("Volver a Inicio");
        backButton.addActionListener(e -> mainFrame.showMainWindow());
        buttonPanel.add(backButton);

        // Agregar contacto
        addButton = new JButton("Agregar Contacto");
        addButton.addActionListener(e -> {
            // Crear un panel personalizado con dos campos de texto
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField nombreField = new JTextField();
            JTextField telefonoField = new JTextField();

            panel.add(new JLabel("Nombre:"));
            panel.add(nombreField);
            panel.add(new JLabel("Teléfono:"));
            panel.add(telefonoField);

            // Mostrar el panel en un JOptionPane
            int result = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Agregar Contacto",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // Si el usuario hace clic en "Aceptar"
            if (result == JOptionPane.OK_OPTION) {
                String nombre = nombreField.getText().trim();
                String telefono = telefonoField.getText().trim();

                // Validar que los campos no estén vacíos
                if (!nombre.isEmpty() && !telefono.isEmpty()) {
                    if (ControladorAppChat.INSTANCE.agregarContacto(nombre, telefono)) {
                        JOptionPane.showMessageDialog(this, "Contacto agregado correctamente.");
                        cargarContactos(ControladorAppChat.INSTANCE); // Actualizar la tabla
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo agregar el contacto.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, complete ambos campos.");
                }
            }
        });
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listener para visualizar contacto
        itemVisualizar.addActionListener(e -> {
            int row = contactTable.getSelectedRow();
            if (row >= 0) {
                String nombre = (String) tableModel.getValueAt(row, 0);
                ChatIndividual chat = (ChatIndividual) ControladorAppChat.INSTANCE.getContactoPorNombre(nombre);
                if (chat != null) {
                    new VentanaContactoVer(mainFrame.frame, chat).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo encontrar el contacto.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto.");
            }
        });

        // Listener para editar contacto
        itemEditar.addActionListener(e -> {
            int row = contactTable.getSelectedRow();
            if (row >= 0) {
                String nombre = (String) tableModel.getValueAt(row, 0);
                ChatIndividual chat = (ChatIndividual) ControladorAppChat.INSTANCE.getContactoPorNombre(nombre);
                if (chat != null) {
                    new VentanaContactoEdit(mainFrame.frame, chat).setVisible(true);
                    cargarContactos(ControladorAppChat.INSTANCE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo encontrar el contacto.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto.");
            }
        });

        // Cargar contactos iniciales
        cargarContactos(ControladorAppChat.INSTANCE);
    }

    private void cargarContactos(ControladorAppChat controlador) {
        tableModel.setRowCount(0); // Limpiar la tabla
        List<String[]> contactos = controlador.getContactosParaTabla();
        for (String[] contacto : contactos) {
            tableModel.addRow(contacto);
        }
    }
}
