/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import java.util.Collection;

/**
 *
 * @author r
 */
public class producto {

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private String cantidad;
    private String descripcion;
    private String precioUnitario;
    private String precioTotalSinImpuesto;
    private String detalle1;
    private String detalle2;
    private String detalle3;
    private String descuento;
    private Collection infoAdicional;

    public producto(String codigoPrincipal, String codigoAuxiliar, String cantidad, String descripcion, String precioUnitario, String precioTotalSinImpuesto, String detalle1, String detalle2, String detalle3, String descuento,Collection infoAdicional) {
        this.cantidad=cantidad;
        this.codigoAuxiliar=codigoAuxiliar;
        this.codigoPrincipal=codigoPrincipal;
        this.descripcion=descripcion;
        this.descuento=descuento;
        this.detalle1=detalle1;
        this.detalle2=detalle2;
        this.detalle3=detalle3;
        this.precioTotalSinImpuesto=precioTotalSinImpuesto;
        this.precioUnitario=precioUnitario;
        this.infoAdicional=infoAdicional;
    
    
    }

    public Collection getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(Collection infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

   

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getPrecioTotalSinImpuesto() {
        return precioTotalSinImpuesto;
    }

    public void setPrecioTotalSinImpuesto(String precioTotalSinImpuesto) {
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
    }

    public String getDetalle1() {
        return detalle1;
    }

    public void setDetalle1(String detalle1) {
        this.detalle1 = detalle1;
    }

    public String getDetalle2() {
        return detalle2;
    }

    public void setDetalle2(String detalle2) {
        this.detalle2 = detalle2;
    }

    public String getDetalle3() {
        return detalle3;
    }

    public void setDetalle3(String detalle3) {
        this.detalle3 = detalle3;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }





}
