/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

//import ws.AutorizacionComprobantes;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantes;
import ec.gob.sri.comprobantes.ws.aut.AutorizacionComprobantesService;
import ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author Edisson
 */
public class AutorizacionComprobantesWS {

    private AutorizacionComprobantesService service;
    public static final String ESTADO_AUTORIZADO = "AUTORIZADO";
    public static final String ESTADO_NO_AUTORIZADO = "NO AUTORIZADO";

    public AutorizacionComprobantesWS(String wsdlLocation) {
        try {
            
            service = new AutorizacionComprobantesService(new URL(wsdlLocation), new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService"));
        
        } catch (javax.xml.ws.WebServiceException ex) {
             Logger.getLogger(AutorizacionComprobantesWS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AutorizacionComprobantesWS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public RespuestaComprobante llamadaWSAutorizacionInd(String claveDeAcceso) {
        RespuestaComprobante response = null;
        try {
            AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
            ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 3000);
            ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 3000);
            response = port.autorizacionComprobante(claveDeAcceso);
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
        
        return response;
    }
}
