/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import seguridad.CertificadosSSL;
import ws.EnvioComprobantesWS;
import ws.AutorizacionComprobantesWS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

/**
 *
 * @author Edisson
 */
public class ArchivosUtil {

    public static void validarArchivo(File archivo, String nombre)
            throws IllegalStateException {
        if (null == archivo || archivo.length() <= 0L) {

            throw new IllegalStateException((new StringBuilder()).append(nombre).append(" es nulo o esta vacio").toString());



        } else {
            return;
        }
    }

    public static byte[] convertirArchivoAByteArray(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } catch (Exception ex) {
            buffer = null;
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        return buffer;
    }

    public static void guardarDocumentoXMLAutorizado(Autorizacion autorizacion, String ruta) throws JAXBException, IOException {
        autorizacion.setComprobante((new StringBuilder()).append("<![CDATA[").append(autorizacion.getComprobante()).append("]]>").toString());

        JAXBContext jc = JAXBContext.newInstance(Autorizacion.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        Writer writer = new FileWriter(ruta);
        writer.write("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>");
        marshaller.marshal(new JAXBElement<Autorizacion>(new QName("autorizacion"), Autorizacion.class, autorizacion), writer);
        writer.close();

        /*
         Autorizacion2 objAutorizacion = new Autorizacion2();
         objAutorizacion.setComprobante(autorizacion.getComprobante());
         objAutorizacion.setEstado(autorizacion.getEstado());
         objAutorizacion.setFechaAutorizacion(autorizacion.getFechaAutorizacion());
         objAutorizacion.setMensajes(autorizacion.getMensajes());
         objAutorizacion.setNumeroAutorizacion(autorizacion.getNumeroAutorizacion());
         JAXBContext jaxbContext = JAXBContext.newInstance(Autorizacion2.class);
         Marshaller marshaller = jaxbContext.createMarshaller();
         marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
         Writer writer = new FileWriter(ruta);
         DataWriter dataWriter = new DataWriter(writer, "UTF-8", new JaxbCharacterEscapeHandler());
         marshaller.marshal(objAutorizacion, dataWriter);
         writer.close();*/
    }

    public static boolean existeConexion(String wse, String wsa) {
        int i = 0;



        boolean respuesta = false;
        while (i < 1) {

         

            System.out.println("W e " + wse);
            System.out.println("W a " + wsa);
            try {
                File axml = new File("ejemplo.xml");
                axml.createNewFile();
                CertificadosSSL.instalarCertificados();

                Object obj = new EnvioComprobantesWS(wse);
             

                obj = null;
                if (obj  == null) {
                    
                    Object obj1 = new AutorizacionComprobantesWS(wsa);
             

                     obj1 = null;
                      if (obj1  == null) {
                      
                             return true;
                      }
                    if ((obj1  instanceof WebServiceException)) {
                    respuesta = false;
                    }
                    
                 
                }
                if ((obj  instanceof WebServiceException)) {
                    respuesta = false;
                
                
                }
                 
                
                
                
               // RespuestaSolicitud response = ws.enviarComprobante(axml);



                //System.out.println(response.getEstado());


/*                if (response.getEstado().equalsIgnoreCase("DEVUELTA")) {




                    RespuestaComprobante respuestaC = null;
                    respuestaC = (new AutorizacionComprobantesWS(wsa)).llamadaWSAutorizacionInd("0000000");
                    List<Autorizacion> listaAutorizaciones = respuestaC.getAutorizaciones().getAutorizacion();

                    
                    String estado = respuestaC.getNumeroComprobantes();

                    System.out.println(estado);
                    
                    if (estado.equalsIgnoreCase("0")) {


                        respuesta = true;


                    } else {


                        respuesta = false;
                    }









                } else {

                    System.out.println("No hay Conexion");
                    return false;
                }*/

            } catch (WebServiceException e) {

                // Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, e);
              
                return false;


            } catch (MalformedURLException ex) {
                
                //    Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);

                return false;

            } catch (java.lang.NullPointerException ex) {
                
                // Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);

                return false;
            } catch (IOException ex) {
                Logger.getLogger(ArchivosUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

             
            
            i++;
        }
        return respuesta;
    }
}