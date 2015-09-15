/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import static util.ArchivosUtil.validarArchivo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 *
 * @author
 */
public class XMLUtil {

    public static String doMarshall(Object comprobante, String pathArchivoSalida) {
        String respuesta = null;
        try {
            //permite la interaccion entre codigo java y xml y xsd
            //crea una nueva instancia de jaxb para el objeto comprobante ejm. factura, guia, etc
            JAXBContext context = JAXBContext.newInstance(new Class[]{comprobante.getClass()});
            //crea un nuevo objeto maarshal, que organiza la informacion en el xml
            Marshaller marshaller = context.createMarshaller();
            //se da formato utf-8 para el idioma
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
            //se escribe el archivo xml en la ruta especificada
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(pathArchivoSalida), "UTF-8");
            marshaller.marshal(comprobante, out);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
        return respuesta;
    }

    public static String doMarshallRes(Object respuesta) {
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(respuesta.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
    //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new JAXBElement(new QName("http://www.namespace.com/schema/My", "My"), Object.class, respuesta), sw);
            sw.close();
            return sw.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String doMarshallaut(Object Respuesta) {
        try {
            java.io.StringWriter sw = new java.io.StringWriter();
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(Respuesta.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
    //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new JAXBElement(new QName("http://www.namespace.com/schema/My", "My"), Object.class, Respuesta), sw);
            sw.close();
            return sw.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String validacion(File archivoXSD, File archivoXML) {
        validarArchivo(archivoXSD, "archivoXSD");
        validarArchivo(archivoXML, "archivoXML");
        String mensaje = null;
        //crea una nueva instancia de un schema desde el servidor de smlschema
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema;
        try {
            //crea un nuevo esquema a partir del archivo xsd
            schema = schemaFactory.newSchema(archivoXSD);
        } catch (SAXException e) {
            throw new IllegalStateException("Existe un error en la sintaxis del esquema", e);
        }
        Validator validator = schema.newValidator();
        try {
            //valida el xml a partir del esquema xsd
            validator.validate(new StreamSource(archivoXML));
        } catch (Exception e) {
            return e.getMessage();
        }
        return mensaje;
    }
}
