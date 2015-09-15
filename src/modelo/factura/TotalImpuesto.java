/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo.factura;

import modelo.Impuesto;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Edisson
 */
@XmlType (propOrder = {"codigo","codigoPorcentaje","baseImponible","valor"})
public class TotalImpuesto {    
    private String codigo;
    private String codigoPorcentaje;
    private BigDecimal baseImponible;
    private BigDecimal valor;
    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo ;
    }

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public TotalImpuesto(Impuesto impuesto) {
        this.codigo = impuesto.getCodigo();
        this.codigoPorcentaje = impuesto.getCodigoPorcentaje();
        this.baseImponible = impuesto.getBaseImponible();
        this.valor = impuesto.getValor();
    }  
    
    public TotalImpuesto() {
        
    }  
}
