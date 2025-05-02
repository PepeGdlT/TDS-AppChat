package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import controlador.ControladorAppChat;
import modelo.Usuario;

public class VentanaDescuentos extends JDialog {

    private JLabel lblPrecioActualizado;
    private JLabel lblEstadoDescuentoFecha;
    private JLabel lblEstadoDescuentoMensajes;
    private JButton btnAplicarDescuentos;
    private JButton btnConfirmarPago;

    private double precioOriginal = 24.99;
    private double precioFinal;
    private Usuario usuario;

    public VentanaDescuentos(JFrame parent, Usuario usuario) {
        super(parent, "Descuentos Premium", true);
        this.usuario = usuario;
        this.precioFinal = precioOriginal;

        // Panel principal
        JPanel panelDescuentos = new JPanel();
        panelDescuentos.setLayout(new BoxLayout(panelDescuentos, BoxLayout.Y_AXIS));

        // Mostrar coste inicial
        JLabel lblCostoInicial = new JLabel("Coste inicial: $" + precioOriginal);
        panelDescuentos.add(lblCostoInicial);

        // Estado de descuentos
        lblEstadoDescuentoFecha = new JLabel("Descuento por Fecha: No aplicado");
        lblEstadoDescuentoMensajes = new JLabel("Descuento por Mensajes: No aplicado");

        panelDescuentos.add(lblEstadoDescuentoFecha);
        panelDescuentos.add(lblEstadoDescuentoMensajes);

        // Etiqueta del precio actualizado
        lblPrecioActualizado = new JLabel("Precio actualizado: $" + precioOriginal);
        lblPrecioActualizado.setForeground(Color.WHITE);
        panelDescuentos.add(lblPrecioActualizado);

        // Botón para aplicar descuentos
        btnAplicarDescuentos = new JButton("Aplicar Descuentos");
        btnAplicarDescuentos.setBackground(Color.RED);
        btnAplicarDescuentos.setForeground(Color.WHITE);
        btnAplicarDescuentos.addActionListener(e -> aplicarDescuentos());
        panelDescuentos.add(btnAplicarDescuentos);

        // Botón para confirmar pago
        btnConfirmarPago = new JButton("Confirmar y Pagar");
        btnConfirmarPago.addActionListener(e -> confirmarPago());
        panelDescuentos.add(btnConfirmarPago);

        // Configuración de la ventana
        this.add(panelDescuentos, BorderLayout.CENTER);
        configurarVentanaDescuentos();
    }

    private void aplicarDescuentos() {
        // Solicitar al controlador que calcule los descuentos y actualice la vista
        double descuentoTotal = ControladorAppChat.INSTANCE.calcularDescuento(usuario);

        // Actualizar los estados de los descuentos en la UI
        lblPrecioActualizado.setText("Precio actualizado: $" + String.format("%.2f", descuentoTotal));
        lblPrecioActualizado.setForeground(Color.GREEN);

        // Obtener el estado de los descuentos desde el controlador
        String estadoFecha = ControladorAppChat.INSTANCE.getEstadoDescuentoFecha();
        String estadoMensajes = ControladorAppChat.INSTANCE.getEstadoDescuentoMensajes();

        // Cambiar el color de las etiquetas según el estado del descuento
        if (estadoFecha.contains("✔ Aplicado")) {
            lblEstadoDescuentoFecha.setForeground(Color.GREEN);
        } else {
            lblEstadoDescuentoFecha.setForeground(Color.RED);
        }

        if (estadoMensajes.contains("✔ Aplicado")) {
            lblEstadoDescuentoMensajes.setForeground(Color.GREEN);
        } else {
            lblEstadoDescuentoMensajes.setForeground(Color.RED);
        }

        // Mostrar el estado de los descuentos
        lblEstadoDescuentoFecha.setText(estadoFecha);
        lblEstadoDescuentoMensajes.setText(estadoMensajes);
    }

    private void confirmarPago() {
        // Solicitar al controlador que actualice el estado del usuario
    	ControladorAppChat.INSTANCE.confirmarPago(usuario);

        // Mostrar mensaje de éxito
        JOptionPane.showMessageDialog(this, "¡Pago realizado exitosamente! Ahora eres usuario Premium.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

        // Cerrar la ventana
        this.dispose();
    }

    private void configurarVentanaDescuentos() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
}
