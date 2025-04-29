package vista.Ventana;

import javax.swing.*;
import java.awt.*;
import modelo.Usuario;
import modelo.Descuento.Descuento;
import modelo.Descuento.FactoriaDescuento;
import controlador.ControladorAppChat;

public class VentanaDescuentos extends JDialog {

    private JLabel lblPrecioActualizado;
    private JLabel lblEstadoDescuentoFecha;
    private JLabel lblEstadoDescuentoMensajes;
    private JButton btnAplicarDescuentos;
    private JButton btnConfirmarPago;

    private double precioOriginal = 24.99;
    private double precioFinal;
    private Usuario usuario;
    private ControladorAppChat controlador;

    public VentanaDescuentos(JFrame parent, Usuario usuario, ControladorAppChat controlador) {
        super(parent, "Descuentos Premium", true);
        this.usuario = usuario;
        this.controlador = controlador;
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
        btnConfirmarPago.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "¡Pago realizado exitosamente! Ahora eres usuario Premium.", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
            controlador.hacerPremium(true);
            this.dispose();
        });
        panelDescuentos.add(btnConfirmarPago);

        // Configuración de la ventana
        this.add(panelDescuentos, BorderLayout.CENTER);
        configurarVentanaDescuentos();
    }

    private void aplicarDescuentos() {
        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Error: Usuario no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double totalDescuento = 0;
            boolean descuentoFechaAplicado = false;
            boolean descuentoMensajesAplicado = false;

            // Verificar descuento por fecha
            Descuento descuentoFecha = FactoriaDescuento.crearDescuento(
                "modelo.Descuento.DescuentoPorFecha", 10.0, "2025-01-01", "2025-07-31"
            );
            if (descuentoFecha.esAplicable(usuario)) {
                totalDescuento += descuentoFecha.getDescuento(usuario);
                lblEstadoDescuentoFecha.setText("Descuento por Fecha: ✔ Aplicado (10%)");
                lblEstadoDescuentoFecha.setForeground(Color.GREEN);
                descuentoFechaAplicado = true;
            } else {
                lblEstadoDescuentoFecha.setText("Descuento por Fecha: ✖ No válido en esta fecha");
                lblEstadoDescuentoFecha.setForeground(Color.RED);
            }

            // Verificar descuento por mensajes
            Descuento descuentoMensajes = FactoriaDescuento.crearDescuento(
                "modelo.Descuento.DescuentoPorMensaje", 15.0, 20
            );
            if (descuentoMensajes.esAplicable(usuario)) {
                totalDescuento += descuentoMensajes.getDescuento(usuario);
                lblEstadoDescuentoMensajes.setText("Descuento por Mensajes: ✔ Aplicado (15%)");
                lblEstadoDescuentoMensajes.setForeground(Color.GREEN);
                descuentoMensajesAplicado = true;
            } else {
                lblEstadoDescuentoMensajes.setText("Descuento por Mensajes: ✖ No cumple con los mensajes mínimos");
                lblEstadoDescuentoMensajes.setForeground(Color.RED);
            }

            // Si hay descuentos aplicados, actualizamos el precio final
            if (descuentoFechaAplicado || descuentoMensajesAplicado) {
                precioFinal = precioOriginal * (1 - totalDescuento / 100);
                lblPrecioActualizado.setText("Precio actualizado: $" + String.format("%.2f", precioFinal));
                lblPrecioActualizado.setForeground(Color.GREEN);
                btnAplicarDescuentos.setBackground(Color.GREEN);
                btnAplicarDescuentos.setText("Descuentos Aplicados");
            } else {
                precioFinal = precioOriginal;
                lblPrecioActualizado.setText("Precio actualizado: $" + precioOriginal);
                lblPrecioActualizado.setForeground(Color.WHITE);
                btnAplicarDescuentos.setBackground(Color.RED);
                btnAplicarDescuentos.setText("No Aplicable");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aplicar descuentos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void configurarVentanaDescuentos() {
        this.setSize(400, 200);
        this.setLocationRelativeTo(this.getParent());
        this.setVisible(true);
    }
}
