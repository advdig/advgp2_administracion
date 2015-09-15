/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.retencion;

import modelo.CampoAdicional;
import modelo.InfoTributaria;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Rolando
 */
@XmlRootElement(name = "comprobanteRetencion")
@XmlType(propOrder = {
    "id", "version", "infoTributaria", "infoCompRetencion", "impuesto", "infoAdicional"})
public class ComprobanteRetencion {

    protected InfoTributaria infoTributaria;
    protected InfoCompRetencion infoCompRetencion;
    protected List<Impuesto> impuesto;
    protected List<CampoAdicional> campoAdcicional;
    protected String id;
    protected String version;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper(name = "impuestos")
    public List<Impuesto> getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(List<Impuesto> impuesto) {
        this.impuesto = impuesto;
    }
    
    public List<CampoAdicional> getCampoAdcicional() {
        return campoAdcicional;
    }

    public void setCampoAdcicional(List<CampoAdicional> campoAdcicional) {
        this.campoAdcicional = campoAdcicional;
    }

    public InfoCompRetencion getInfoCompRetencion() {
        return infoCompRetencion;
    }

    public void setInfoCompRetencion(InfoCompRetencion infoCompRetencion) {
        this.infoCompRetencion = infoCompRetencion;
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
