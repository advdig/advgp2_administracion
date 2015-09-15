/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author r
 */
public class enviar_email implements Runnable {

    Properties props;
    Session session;
    String correo, xml, pdf, coreocliente, rz, npdf, nxml;
    MimeMessage message;

    public void enviar(String correo, String xml, String pdf, String correocliente, String cliente, String rz, String npdf, String nxml) {
        this.correo = correo;
        this.xml = xml;
        this.pdf = pdf;
        this.coreocliente = correocliente;
        this.rz = rz;
        this.npdf = npdf;
        this.nxml = nxml;

        try {
            // se obtiene el objeto Session. La configuración es para
            // una cuenta de gmail.
            props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", correo);
            props.setProperty("mail.smtp.auth", "true");

            session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();

            message = new MimeMessage(session);


            message.setSubject(rz + "Factura Electronica N° " + nxml);

            message.setText("Estimado(a) " + cliente + "\n"
                    + "Presente.\n"
                    + "\n"
                    + "Adjunto sírvase encontrar Factura Electrónica Nº"+nxml+" ¹ y el detalle² de dicho comprobante que hemos emitido en nuestra empresa.\n"
                    + "Descargue sus documentos electrónicos de la siguinte página web www.advangas.com\n"
                    + "Gracias por preferirnos.\n"
                    + "\n"
                    + "Atentamente,\n"
                    + ""+rz+"\n"
                    + "--------------------------------------------------------------------------------\n"
                    + "¹ El comprobante electrónico es el archivo XML adjunto, le socilitamos que lo almacene de manera segura puesto que tiene validez tributaria.\n"
                    + "² La representación impresa del comprobante electrónico es el archivo PDF adjunto, no posee validez tributaria y no es necesario que la imprima.");
            texto.setText("Gracias por su compra ");

            // Se compone el adjunto con la imagen
            BodyPart adjuntopdf = new MimeBodyPart();
            adjuntopdf.setDataHandler(new DataHandler(new FileDataSource(pdf)));
            adjuntopdf.setFileName(npdf);

            BodyPart adjuntoxml = new MimeBodyPart();
            adjuntoxml.setDataHandler(new DataHandler(new FileDataSource(xml)));
            adjuntoxml.setFileName(nxml);


            // Una MultiParte para agrupar texto e imagen.
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            multiParte.addBodyPart(adjuntopdf);
            multiParte.addBodyPart(adjuntoxml);

            // Se compone el correo, dando to, from, subject y el
            // contenido.

            message.setFrom(new InternetAddress("yo@yo.com"));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(correocliente));

            message.setContent(multiParte);



            // Se envia el correo.
            Thread hilo = new Thread(this);
            hilo.start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void run() {
        try {
            Transport t = session.getTransport("smtp");
            t.connect(correo, "punk1991");
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args se ignoran
     */
    public static void main(String[] args) {
    }
}