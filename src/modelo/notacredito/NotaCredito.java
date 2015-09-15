/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.notacredito;

import modelo.CampoAdicional;
import modelo.InfoTributaria;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import modelo.factura.FacturaDetalle;

/**
 *
 * @author Rolando
 */
@XmlRootElement(name = "notaCredito")
@XmlType(propOrder = {
    "id", "version", "infoTributaria", "infoNotaCredito", "detalle", "campoAdicional"})
public class NotaCredito {

    protected InfoTributaria infoTributaria;
    protected InfoNotaCredito infoNotaCredito;
   protected List<Detalle> detalle;
    private List<CampoAdicional> campoAdicional;
    protected String id;
    protected String version;

    
    @XmlElementWrapper(name = "detalles")
    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }

  

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper(name = "infoAdicional")
    public List<CampoAdicional> getCampoAdicional() {
        return campoAdicional;
    }

    public void setCampoAdicional(List<CampoAdicional> campoAdicional) {
        this.campoAdicional = campoAdicional;
    }

    public InfoNotaCredito getInfoNotaCredito() {
        return infoNotaCredito;
    }

    public void setInfoNotaCredito(InfoNotaCredito infoNotaCredito) {
        this.infoNotaCredito = infoNotaCredito;
    }

    public InfoTributaria getInfoTributaria() {
        return infoTributaria;
    }

    public void setInfoTributaria(InfoTributaria infoTributaria) {
        this.infoTributaria = infoTributaria;
    }

    @XmlAttribute(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
