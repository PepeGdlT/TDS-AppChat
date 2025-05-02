package modelo;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportPDF {
    
    private static final Font USER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new BaseColor(0, 51, 102));
    private static final Font CONTACT_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL, new BaseColor(102, 0, 51));
    private static final BaseColor USER_BG = new BaseColor(225, 239, 255);  // Azul claro
    private static final BaseColor CONTACT_BG = new BaseColor(255, 230, 240); // Rosa claro

    public static void crearPDF(Usuario u, ChatIndividual chatConContacto) {
        try {
            String fileName = String.format("%s_%s_%s.pdf", 
                u.getNombreCompleto().replace(" ", "_"),
                chatConContacto.getNombreContacto().replace(" ", "_"),
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();
            
            Paragraph header = new Paragraph();
            header.add(new Chunk("Conversación entre ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            header.add(new Chunk(u.getNombreCompleto(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD, BaseColor.BLUE)));
            header.add(new Chunk(" y ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            header.add(new Chunk(chatConContacto.getNombreContacto(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD, BaseColor.RED)));
            header.add(new Chunk("\nFecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                FontFactory.getFont(FontFactory.HELVETICA, 10)));
            
            document.add(header);
            document.add(Chunk.NEWLINE);
            
            List<Mensaje> mensajes = chatConContacto.getMensajesEnviados();
            
            for (Mensaje mensaje : mensajes) {
                if (mensaje.getReceptor() instanceof Grupo) continue;
                
                boolean isUserMessage = mensaje.getEmisor().equals(u);
                String contenido = obtenerContenidoMensaje(mensaje);
                
                if (!contenido.isEmpty()) {
                    Paragraph messagePara = new Paragraph();
                    messagePara.setAlignment(isUserMessage ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                    messagePara.setSpacingBefore(5f);
                    
                    PdfPTable table = new PdfPTable(1);
                    table.setWidthPercentage(isUserMessage ? 70 : 70);
                    table.setHorizontalAlignment(isUserMessage ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                    
                    PdfPCell cell = new PdfPCell(new Phrase(contenido, isUserMessage ? USER_FONT : CONTACT_FONT));
                    cell.setBackgroundColor(isUserMessage ? USER_BG : CONTACT_BG);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setPadding(8);
         
                    table.addCell(cell);
                    messagePara.add(table);
                    
                    Paragraph metaPara = new Paragraph();
                    metaPara.setAlignment(isUserMessage ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                    metaPara.add(new Chunk(isUserMessage ? "Tú" : chatConContacto.getNombreContacto(), 
                        FontFactory.getFont(FontFactory.HELVETICA, 8, Font.ITALIC)));
                    metaPara.add(new Chunk(" - " + mensaje.getHora().format(
                        DateTimeFormatter.ofPattern("HH:mm")), 
                        FontFactory.getFont(FontFactory.HELVETICA, 8)));
                    
                    document.add(messagePara);
                    document.add(metaPara);
                }
            }
            
            document.close();
            JOptionPane.showMessageDialog(null, 
                "PDF exportado correctamente:\n" + fileName, 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error al exportar el PDF:\n" + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private static String obtenerContenidoMensaje(Mensaje mensaje) {
        if (mensaje.getTexto() != null && !mensaje.getTexto().trim().isEmpty()) {
            return mensaje.getTexto();
        } else if (mensaje.getEmoticono() != null) {
            return "[Emoji: " + mensaje.getEmoticono() + "]";
        }
        return "";
    }
}