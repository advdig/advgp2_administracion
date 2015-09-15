/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seguridad;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import org.w3c.dom.Document;

/**
 *
 * @author Edisson
 */
public class FirmaDigital extends GenericXMLSignature {

    private String rutaDocumentoAFirmar;
    private String rutaDocumentoFirmado;

    public FirmaDigital(String rutaDocumentoAFirmar, String rutaDocumentoFirmado) {
        this.rutaDocumentoAFirmar = rutaDocumentoAFirmar;
        this.rutaDocumentoFirmado = rutaDocumentoFirmado;
    }

    public String getRutaDocumentoAFirmar() {
        return rutaDocumentoAFirmar;
    }

    public void setRutaDocumentoAFirmar(String rutaDocumentoAFirmar) {
        this.rutaDocumentoAFirmar = rutaDocumentoAFirmar;
    }

    public String getRutaDocumentoFirmado() {
        return rutaDocumentoFirmado;
    }

    public void setRutaDocumentoFirmado(String rutaDocumentoFirmado) {
        this.rutaDocumentoFirmado = rutaDocumentoFirmado;
    }

    @Override
    protected DataToSign createDataToSign() {
        DataToSign dataToSign = new DataToSign();
        try {
            dataToSign.setXadesFormat(es.mityc.javasign.EnumFormatoFirma.XAdES_BES);
            dataToSign.setEsquema(XAdESSchemas.XAdES_132);
            dataToSign.setXMLEncoding("UTF-8");
            dataToSign.setEnveloped(true);
            dataToSign.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "Documento de ejemplo", null, "text/xml", null));
            Document docToSign = getDocument(getRutaDocumentoAFirmar());
            dataToSign.setDocument(docToSign);
        } catch (Exception ex) {
            dataToSign = null;
            System.err.println(ex.getMessage());
        } finally {
            return dataToSign;
        }
    }

    @Override
    protected String getSignatureFileName() {
        return rutaDocumentoFirmado;
    }
    
    public void firmarDocumentoXML() {
        execute();
    }

}
