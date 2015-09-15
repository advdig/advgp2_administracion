/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo.factura;

import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Edisson
 */
@XmlType(propOrder = {"fechaEmision","dirEstablecimiento","contribuyenteEspecial","obligadoContabilidad","tipoIdentificacionComprador","razonSocialComprador","identificacionComprador"
        ,"totalSinImpuestos","totalDescuento", "totalImpuesto","propina","importeTotal","moneda"})
public class InfoFactura {
   private String fechaEmision;
   private String dirEstablecimiento;
   private BigDecimal contribuyenteEspecial;
   private String obligadoContabilidad;
   private String tipoIdentificacionComprador;
   private String razonSocialComprador;
   private String identificacionComprador;
   private BigDecimal totalSinImpuestos;
   private BigDecimal totalDescuento;
   private List<TotalImpuesto> totalImpuesto;
   private BigDecimal propina;
   private BigDecimal importeTotal;
   private String moneda;

   @XmlElementWrapper(name = "totalConImpuestos")
    public List<TotalImpuesto> getTotalImpuesto() {
        return totalImpuesto;
    }

    public void setTotalImpuesto(List<TotalImpuesto> totalImpuesto) {
        this.totalImpuesto = totalImpuesto;
    }
   
    public String getFechaEmision() {
        return fechaEmision;
    }

    public BigDecimal getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(BigDecimal contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public BigDecimal getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(BigDecimal totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public BigDecimal getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(BigDecimal totalDescuento) {
        this.totalDescuento = totalDescuento;
    }
    
    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    
    public InfoFactura(){
    }
    
}
