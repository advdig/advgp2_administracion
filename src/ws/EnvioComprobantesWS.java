/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import util.ArchivosUtil;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantes;
import ec.gob.sri.comprobantes.ws.RecepcionComprobantesService;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;


/**
 *
 * @author Edisson
 */
public class EnvioComprobantesWS {

    private static RecepcionComprobantesService service;

    
    
    public EnvioComprobantesWS(String wsdlLocation) throws MalformedURLException  {
       
        URL url = new URL(wsdlLocation);
        QName qname = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesService");
        service = new RecepcionComprobantesService(url, qname);
        
        
        
    
    }

    public RespuestaSolicitud enviarComprobante(File xmlFile) {
        RespuestaSolicitud response = null;
        try {
            RecepcionComprobantes port = service.getRecepcionComprobantesPort();
            byte[] archivoBytes = ArchivosUtil.convertirArchivoAByteArray(xmlFile);
            
            if (archivoBytes != null) {
            
                response = port.validarComprobante(archivoBytes);
                port = service.getRecepcionComprobantesPort();
                ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 4000);
                ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 4000);
                
                
                
            } else {
                response = new RespuestaSolicitud();
                response.setEstado("NO ARCHIVO");
            }
        } catch (Exception e) {
            response = new RespuestaSolicitud();
            
               
                response.setEstado(e.getClass().getName());
        
        
        }
        
        return response;
    }
}
