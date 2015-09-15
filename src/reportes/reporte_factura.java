/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

/**
 *
 * @author r
 */
import Principal.enviar_email;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class reporte_factura implements Runnable{

    String nf;
    Map pars;
     Properties props ;
      Session session ;
      String correo,xml,pdf,correocliente,rz,npdf,nxml;
      MimeMessage message;
      String ncliente,contraseña;
      
      LinkedList<producto> listaproducto;
  public void reporte_factura(String nf,String ruc,String dm,String ds,String ce,String nce,String numAut,String fechahora,String clave,String temision,String ambiente,String rz,String nc,String rzc,String ruc_comp,String f_emision,String guia,String subtotal,String iva,String total,String descuento,String cp,String producto,String cantidad,String detalle,String pu,String descuentop){
    
    
    try {
         
        this.nf=nf;
        
        //1. Se compila el reporte
        InputStream icono = new FileInputStream("ad.jpg");  
       
        
        InputStream marca_agua;
        
        if(ambiente.equalsIgnoreCase("Pruebas")){
            marca_agua = new FileInputStream("pruebas.jpeg");
        
        }else
            
        
        { 
             marca_agua = new FileInputStream("produccion.jpeg");

        }
            JasperCompileManager.compileReportToFile("factura.jrxml");

            //2. Se llena el reporte con la informacion necesaria
          
            pars = new HashMap();
            pars.put("RUC",ruc);
            pars.put("LOGO",icono);
            pars.put("NUM_FACT",nf);
            pars.put("NUM_AUT",numAut);
            pars.put("FECHA_AUT",fechahora);
            pars.put("CLAVE_ACC",clave);
            pars.put("TIPO_EMISION",temision);
            pars.put("AMBIENTE",ambiente);
            pars.put("RAZON_SOCIAL",rz);
            pars.put("AMBIENTE",ambiente);
            pars.put("NOM_COMERCIAL",nc);
            pars.put("DIR_MATRIZ",dm);
            pars.put("DIR_SUCURSAL",ds);
            pars.put("CONT_ESPECIAL",nce);
            pars.put("LLEVA_CONTABILIDAD",ce);
            pars.put("RS_COMPRADOR",rzc);
            pars.put("RUC_COMPRADOR",ruc_comp);
            pars.put("FECHA_EMISION",f_emision);
            pars.put("GUIA",guia);
            pars.put("IVA_12",subtotal);
            pars.put("SUBTOTAL",subtotal);
            pars.put("IVA",iva);
            pars.put("TOTAL_DESCUENTO",descuento);
            pars.put("IVA_0","0");
            pars.put("NO_OBJETO_IVA","0");
            pars.put("ICE","0");
            pars.put("VALOR_TOTAL",total);

            pars.put("MARCA_AGUA", marca_agua);

            Collection info = null;
            
           listaproducto = new LinkedList<producto>();
           
           System.out.println(cp +"  "+ cantidad + "  "+ detalle + "   "+ pu +"  "+subtotal +"  "+detalle +"  "+descuentop);
           
           listaproducto.add(new producto(cp,"",cantidad,detalle,pu,subtotal,detalle,"","",descuentop,null));
           //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
           //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));

          JasperPrint jasperPrint = JasperFillManager.fillReport( "factura.jasper", pars,  new JRBeanCollectionDataSource(listaproducto));

            //3. Se exporta a PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\"+nf+".pdf");

            System.out.println("Done!");
            
       

            
        
       } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reporte_factura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   public void reporte_factura(String nf,String ruc,String dm,String ds,String ce,String nce,String numAut,String fechahora,String clave,String temision,String ambiente,String rz,String nc,String rzc,String ruc_comp,String f_emision,String guia,String subtotal,String iva,String total,String descuento,String cp,String[] producto,Double []cantidad,String []detalle,Double [] pu,String descuentop,Double []subtotalp){
    
    
    try {
         
        this.nf=nf;
        
        //1. Se compila el reporte
        InputStream icono = new FileInputStream("ad.jpg");  
        
        InputStream marca_agua;
        
        if(ambiente.equalsIgnoreCase("Pruebas")){
            marca_agua = new FileInputStream("pruebas.jpeg");
        
        }else
            
        
        { 
             marca_agua = new FileInputStream("produccion.jpeg");

        }
            JasperCompileManager.compileReportToFile("factura.jrxml");

            //2. Se llena el reporte con la informacion necesaria
          
            pars = new HashMap();
            pars.put("RUC",ruc);
            pars.put("LOGO",icono);
            pars.put("NUM_FACT",nf);
            pars.put("NUM_AUT",numAut);
            pars.put("FECHA_AUT",fechahora);
            pars.put("CLAVE_ACC",clave);
            pars.put("TIPO_EMISION",temision);
            pars.put("AMBIENTE",ambiente);
            pars.put("RAZON_SOCIAL",rz);
            pars.put("AMBIENTE",ambiente);
            pars.put("NOM_COMERCIAL",nc);
            pars.put("DIR_MATRIZ",dm);
            pars.put("DIR_SUCURSAL",ds);
            pars.put("CONT_ESPECIAL",nce);
            pars.put("LLEVA_CONTABILIDAD",ce);
            pars.put("RS_COMPRADOR",rzc);
            pars.put("RUC_COMPRADOR",ruc_comp);
            pars.put("FECHA_EMISION",f_emision);
            pars.put("GUIA",guia);
            pars.put("IVA_12",subtotal);
            pars.put("SUBTOTAL",subtotal);
            pars.put("IVA",iva);
            pars.put("TOTAL_DESCUENTO",descuento);
            pars.put("IVA_0","0");
            pars.put("NO_OBJETO_IVA","0");
            pars.put("ICE","0");
            pars.put("VALOR_TOTAL",total);

            pars.put("MARCA_AGUA", marca_agua);

            Collection info = null;
            
           listaproducto = new LinkedList<producto>();
           
           System.out.println(cp +"  "+ cantidad + "  "+ detalle + "   "+ pu +"  "+subtotal +"  "+detalle +"  "+descuentop);
           
           for(int i=0; i< cantidad.length ; i++){
           if(detalle[i].equalsIgnoreCase("Extra")){
           
               cp="1";
           }else  if(detalle[i].equalsIgnoreCase("Super")){
               
                cp="2";
           
           }else{
           
                cp="3";
           }
               
           listaproducto.add(new producto(cp,"",String.valueOf(cantidad[i]),detalle[i],String.valueOf(pu[i]),String.valueOf(subtotalp[i]),detalle[i],"","",descuentop,null));
           }
           //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
           //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));

          JasperPrint jasperPrint = JasperFillManager.fillReport( "factura.jasper", pars,  new JRBeanCollectionDataSource(listaproducto));

            //3. Se exporta a PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\"+nf+".pdf");

            System.out.println("Done!");
            
       

            
        
       } catch (JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(reporte_factura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
  
   
    public void enviar_email(String contraseña,String correo,String xml,String pdf,String correocliente,String ncliente,String rz,String npdf,String nxml){
    
        this.correo=correo;
        this.xml=xml;
        this.pdf=pdf;
        this.correocliente=correocliente;
        this.rz=rz;
        this.npdf=npdf;
        this.nxml=nxml;
        this.ncliente=ncliente;
        this.contraseña=contraseña;
         
        
        
    
    
    }
    
    public void run() {
        try
        {
         
           
            
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

            texto.setText("Estimado(a) " + ncliente + "\n"
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
            
                if(correocliente==null){
            
            }
            else{
            message.setFrom(new InternetAddress("yo@yo.com"));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(correocliente));
          
            message.setContent(multiParte);

            

            
            // Se envia el correo.
           
        
       
             Transport t = session.getTransport("smtp");
                 t.connect(correo,contraseña);
                 t.sendMessage(message, message.getAllRecipients());
                 t.close();
        } 
        
        } catch (NoSuchProviderException ex) {
             Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
         } catch (MessagingException ex) {
             Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
}