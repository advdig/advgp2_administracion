/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.guia;

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
@XmlRootElement(name = "guiaRemision")
@XmlType(propOrder = {
    "id", "version", "infoTributaria", "infoGuiaRemision", "destinatario", "campoAdicional"})
public class GuiaRemision {

    protected InfoTributaria infoTributaria;
    protected InfoGuiaRemision infoGuiaRemision;
    private List<Destinatario> destinatario;
    protected String id;
    protected String version;
    private List<CampoAdicional> campoAdicional;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElementWrapper(name = "destinatarios")
    public List<Destinatario> getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(List<Destinatario> destinatario) {
        this.destinatario = destinatario;
    }

    @XmlElementWrapper(name = "infoAdicional")
    public List<CampoAdicional> getCampoAdicional() {
        return campoAdicional;
    }

    public void setCampoAdicional(List<CampoAdicional> campoAdicional) {
        this.campoAdicional = campoAdicional;
    }
    
    public InfoGuiaRemision getInfoGuiaRemision() {
        return infoGuiaRemision;
    }

    public void setInfoGuiaRemision(InfoGuiaRemision infoGuiaRemision) {
        this.infoGuiaRemision = infoGuiaRemision;
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