package modelo;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class ExportPDF {

    /**
     * Crea un PDF que exporta la conversación entre el usuario 'u' y el contacto
     * representado por 'chatConContacto', excluyendo mensajes enviados a través de grupos.
     * 
     * @param u                Usuario actual (emisor principal)
     * @param chatConContacto  ChatIndividual que representa la conversación con el contacto seleccionado
     */
    public static void crearPDF(Usuario u, ChatIndividual chatConContacto) {
        try {
            // Construir el nombre del archivo PDF
            String fileName = u.getNombreCompleto() + "_" + chatConContacto.getNombreContacto() + "_" + LocalDate.now() + ".pdf";
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            
            // Abrir el documento para agregar contenido
            document.open();
            
            // Agregar cabecera con datos de la conversación
            document.add(new Paragraph("Conversación entre " + u.getNombreCompleto() + " y " + chatConContacto.getNombreContacto()));
            document.add(new Paragraph("Fecha de exportación: " + LocalDate.now()));
            document.add(new Paragraph("------------------------------------------------------\n"));
            
            // Obtener la lista de mensajes de la conversación.
            // Se asume que getMensajesEnviados() devuelve todos los mensajes intercambiados
            List<Mensaje> mensajes = chatConContacto.getMensajesEnviados();
            
            // Iterar por cada mensaje y agregarlo al PDF, excluyendo mensajes que provengan de un grupo
            for (Mensaje mensaje : mensajes) {
                // Si el receptor es un Grupo, se ignora el mensaje
                if (mensaje.getReceptor() instanceof Grupo) {
                    continue;
                }
                
                // Determinar el emisor: "Tú" si el mensaje fue enviado por el usuario actual, o el nombre del contacto
                String emisor = mensaje.getEmisor().equals(u) ? "Tú" : chatConContacto.getNombreContacto();
                
                // Definir el contenido del mensaje: se usa el texto si existe; de lo contrario, se indica el emoticono (si existe)
                String contenido = (mensaje.getTexto() != null && !mensaje.getTexto().trim().isEmpty())
                        ? mensaje.getTexto()
                        : (mensaje.getEmoticono() != null ? "Emoji: " + mensaje.getEmoticono() : "");
                
                // Solo se agrega el mensaje si tiene contenido no vacío
                if (!contenido.isEmpty()) {
                    document.add(new Paragraph(emisor + ": " + contenido));
                }
            }
            
            // Cerrar el documento
            document.close();
            JOptionPane.showMessageDialog(null, "PDF exportado correctamente: " + fileName, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al exportar el PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
