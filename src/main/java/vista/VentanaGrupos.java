package vista;

import controlador.ControladorAppChat;
import modelo.ChatIndividual;
import modelo.Grupo;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaGrupos extends JPanel {

    private JList<String> groupList;
    private DefaultListModel<String> groupListModel;
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private JButton addGroupButton, addUserButton, removeUserButton;
    private VentanaInicio mainFrame;
    private ControladorAppChat controlador;

    public VentanaGrupos(VentanaInicio mainFrame) {
        this.mainFrame = mainFrame;
        this.controlador = ControladorAppChat.INSTANCE;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ElegantPalette.BACKGROUND);

        // Etiqueta de título
        JLabel titleLabel = new JLabel("Gestión de Grupos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(ElegantPalette.PRIMARY_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal con dos secciones (grupos y participantes)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setBackground(ElegantPalette.BACKGROUND);
        add(splitPane, BorderLayout.CENTER);

        // Panel izquierdo: Lista de grupos
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Grupos"));
        leftPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);

        groupListModel = new DefaultListModel<>();
        groupList = new JList<>(groupListModel);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        groupList.setForeground(ElegantPalette.PRIMARY_TEXT);
        groupList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane groupScrollPane = new JScrollPane(groupList);

        addGroupButton = createStyledButton("Añadir Grupo");
        addGroupButton.addActionListener(e -> agregarGrupo());

        leftPanel.add(groupScrollPane, BorderLayout.CENTER);
        leftPanel.add(addGroupButton, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);

        // Panel derecho: Lista de miembros del grupo
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Miembros del Grupo"));
        rightPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);

        memberTableModel = new DefaultTableModel(new String[]{"Nombre", "Teléfono"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        memberTable = new JTable(memberTableModel);
        memberTable.setBackground(ElegantPalette.TEXT_FIELD_BACKGROUND);
        memberTable.setForeground(ElegantPalette.PRIMARY_TEXT);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane memberScrollPane = new JScrollPane(memberTable);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(ElegantPalette.PANEL_BACKGROUND);

        addUserButton = createStyledButton("Agregar Usuario");
        addUserButton.addActionListener(e -> agregarUsuarioAGrupo());
        removeUserButton = createStyledButton("Eliminar Usuario");
        removeUserButton.addActionListener(e -> removerUsuarioDeGrupo());

        buttonPanel.add(addUserButton);
        buttonPanel.add(removeUserButton);

        rightPanel.add(memberScrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);

        // Botón de volver
        JButton backButton = createStyledButton("Volver");
        backButton.addActionListener(e -> mainFrame.showMainWindow());
        add(backButton, BorderLayout.SOUTH);

        // Evento al seleccionar un grupo
        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarMiembrosGrupo();
            }
        });

        // Cargar datos iniciales
        cargarGrupos();
    }
    
    //CLASE DE BOTONES ESTILIZADOS SE PUEDE PONER EN UTILS

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(ElegantPalette.BUTTON_TEXT);
        button.setBackground(ElegantPalette.ACTION_BUTTON);
        button.setBorder(BorderFactory.createLineBorder(ElegantPalette.BORDER_COLOR, 1));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ElegantPalette.ACTION_BUTTON_HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ElegantPalette.ACTION_BUTTON);
            }
        });

        return button;
    }

    private void cargarGrupos() {
        groupListModel.clear();
        List<Grupo> grupos = controlador.getGrupos();
        for (Grupo grupo : grupos) {
            groupListModel.addElement(grupo.getNombreContacto());
        }
    }

    private void actualizarMiembrosGrupo() {
        memberTableModel.setRowCount(0);
        String selectedGroup = groupList.getSelectedValue();
        if (selectedGroup == null) return;

        List<Grupo> grupos = controlador.getGrupos();
        for (Grupo grupo : grupos) {
            if (grupo.getNombreContacto().equals(selectedGroup)) {
                for (ChatIndividual miembro : grupo.getMiembros()) {
                    Usuario usuario = miembro.getContacto();
                    memberTableModel.addRow(new String[]{usuario.getNombreCompleto(), usuario.getNumeroTelefono()});
                }
                break;
            }
        }
    }

    private void agregarGrupo() {
        String nombreGrupo = JOptionPane.showInputDialog(this, "Ingrese el nombre del grupo:");
        if (nombreGrupo != null && !nombreGrupo.trim().isEmpty()) {
            controlador.crearGrupo(nombreGrupo, List.of());
            JOptionPane.showMessageDialog(this, "Grupo creado.");
            cargarGrupos();
        }
    }


    private void agregarUsuarioAGrupo() {
        String selectedGroup = groupList.getSelectedValue();
        if (selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un grupo.");
            return;
        }

        // Obtener la lista de contactos disponibles
        List<ChatIndividual> contactos = controlador.getChatIndividuals();
        if (contactos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay contactos disponibles para agregar.");
            return;
        }

        // Crear el panel de búsqueda y lista
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JTextField searchField = new JTextField();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> contactList = new JList<>(listModel);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(contactList);

        // Llenar la lista con los contactos disponibles
        for (ChatIndividual contacto : contactos) {
            listModel.addElement(contacto.getNombreContacto() + " - " + contacto.getContacto().getNumeroTelefono());
        }

        // Filtrar la lista a medida que se escribe en el campo de búsqueda
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateList() {
                String filtro = searchField.getText().toLowerCase();
                listModel.clear();
                for (ChatIndividual contacto : contactos) {
                    String nombre = contacto.getNombreContacto().toLowerCase();
                    String telefono = contacto.getContacto().getNumeroTelefono().toLowerCase();
                    if (nombre.contains(filtro) || telefono.contains(filtro)) {
                        listModel.addElement(contacto.getNombreContacto() + " - " + contacto.getContacto().getNumeroTelefono());
                    }
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateList();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateList();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateList();
            }
        });

        panel.add(searchField, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Mostrar cuadro de diálogo con la lista de contactos
        int result = JOptionPane.showConfirmDialog(this, panel, "Seleccione un usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedValue = contactList.getSelectedValue();
            if (selectedValue == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un usuario.");
                return;
            }

            // Extraer solo el nombre del usuario seleccionado
            String selectedContactName = selectedValue.split(" - ")[0];

            // Buscar el grupo seleccionado
            List<Grupo> grupos = controlador.getGrupos();
            for (Grupo grupo : grupos) {
                if (grupo.getNombreContacto().equals(selectedGroup)) {

                    // Buscar el contacto seleccionado
                    for (ChatIndividual contacto : contactos) {
                        if (contacto.getNombreContacto().equals(selectedContactName)) {

                            // Verificar si el usuario ya está en el grupo
                            if (grupo.getMiembros().contains(contacto)) {
                                JOptionPane.showMessageDialog(this, "El usuario ya está en el grupo.");
                                return;
                            }

                            // Agregar al grupo y actualizar la interfaz
                            grupo.getMiembros().add(contacto);
                            controlador.modificarGrupo(grupo, grupo.getNombreContacto(), grupo.getMiembros());
                            actualizarMiembrosGrupo();
                            JOptionPane.showMessageDialog(this, "Usuario agregado al grupo.");
                            return;
                        }
                    }
                }
            }
        }
    }

    
    private void removerUsuarioDeGrupo() {
        int selectedRow = memberTable.getSelectedRow();
        String selectedGroup = groupList.getSelectedValue();
        if (selectedRow < 0 || selectedGroup == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario y un grupo.");
            return;
        }

        String contactName = (String) memberTableModel.getValueAt(selectedRow, 0);
        List<Grupo> grupos = controlador.getGrupos();
        for (Grupo grupo : grupos) {
            if (grupo.getNombreContacto().equals(selectedGroup)) {
                grupo.getMiembros().removeIf(miembro -> miembro.getContacto().getNombreCompleto().equals(contactName));
                controlador.modificarGrupo(grupo, grupo.getNombreContacto(), grupo.getMiembros());
                actualizarMiembrosGrupo();
                JOptionPane.showMessageDialog(this, "Usuario eliminado del grupo.");
                return;
            }
        }
    }
}
