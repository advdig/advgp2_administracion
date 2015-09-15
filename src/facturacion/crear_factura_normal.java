/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacion;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import modelo.CampoAdicional;
import modelo.Impuesto;
import modelo.InfoTributaria;
import modelo.factura.Factura;
import modelo.factura.FacturaDetalle;
import modelo.factura.InfoFactura;
import modelo.factura.TotalImpuesto;
import util.XMLUtil;

/**
 *
 * @author r
 */
public class crear_factura_normal {

    public int crear_factura_normal(String tp, String cp, String nc, String oconta, int cest, String ambiente, String rz, String r, String cadena, String s1, String s2, String ss3, String d, String fecha, String ti, String ncliente, String clientr, Double subtotal, Double iva, Double total, String producto, Double cantidad, Double pu) {


        DecimalFormat df = new DecimalFormat("#.##");

        total = Double.valueOf(df.format(total).replace(",", "."));

        subtotal = Double.valueOf(df.format(total / 1.12).replace(",", "."));

        iva = Double.valueOf(df.format(total - subtotal).replace(",", "."));

        pu = Double.valueOf(df.format(subtotal / cantidad).replace(",", "."));

        cantidad=Double.valueOf(df.format(cantidad).replace(",", "."));


        Factura fac = new Factura();

        fac.setId("comprobante");
        fac.setVersion("1.0.0");
        InfoTributaria it = new InfoTributaria();
        it.setAmbiente(ambiente);
        it.setTipoEmision("1");
        it.setRazonSocial(rz);
        it.setNombreComercial(nc);
        it.setRuc(r);
        it.setClaveAcceso(cadena);
        it.setCodDoc("01");
        it.setPtoEmi(s2);
        it.setEstab(s1);
        it.setSecuencial(ss3);
        it.setDirMatriz(d);
        fac.setInfoTributaria(it);
        InfoFactura inf = new InfoFactura();
        inf.setFechaEmision(fecha.replace("-", "/"));
        inf.setDirEstablecimiento(d);
        if (cest == 0) {
        } else {
            inf.setContribuyenteEspecial(BigDecimal.valueOf(cest));

        }
        inf.setObligadoContabilidad(oconta);
        inf.setTipoIdentificacionComprador(ti);
        inf.setRazonSocialComprador(ncliente);
        inf.setIdentificacionComprador(clientr);
        inf.setTotalSinImpuestos(BigDecimal.valueOf(subtotal));
        inf.setTotalDescuento(BigDecimal.valueOf(0.00));


        TotalImpuesto tim = new TotalImpuesto();
        tim.setCodigo("2");
        tim.setCodigoPorcentaje("2");
        tim.setBaseImponible(BigDecimal.valueOf(subtotal));
        tim.setValor(BigDecimal.valueOf(iva));


        List<TotalImpuesto> listim = new ArrayList<TotalImpuesto>();
        listim.add(tim);
        inf.setTotalImpuesto(listim);
        inf.setPropina(BigDecimal.valueOf(0.00));
        inf.setImporteTotal(BigDecimal.valueOf(total));
        inf.setMoneda("Dolar");

        fac.setInfoFactura(inf);


        FacturaDetalle detalle = new FacturaDetalle();

        
        
        
        detalle.setCodigoPrincipal(cp);
        detalle.setCodigoAuxiliar(cp);
        detalle.setDescripcion(producto);
        detalle.setCantidad(BigDecimal.valueOf(cantidad));
        detalle.setPrecioUnitario(BigDecimal.valueOf(pu));
        detalle.setDescuento(BigDecimal.valueOf(0.00));
        detalle.setPrecioTotalSinImpuesto(BigDecimal.valueOf(subtotal));


        Impuesto imp = new Impuesto();
        imp.setCodigo("2");
        imp.setCodigoPorcentaje("2");
        imp.setTarifa(BigDecimal.valueOf(12));
        imp.setBaseImponible(BigDecimal.valueOf(subtotal));
        imp.setValor(BigDecimal.valueOf(iva));



        List<Impuesto> impuesto = new ArrayList<Impuesto>();


        impuesto.add(imp);



        detalle.setImpuesto(impuesto);

        List<FacturaDetalle> detallef = new ArrayList<FacturaDetalle>();
        detallef.add(detalle);




        

        fac.setDetalle(detallef);



        CampoAdicional adicional = new CampoAdicional();

        adicional.setNombre("Imprime");
        adicional.setValue("Advadvantech");
        
       

        List<CampoAdicional> camp = new ArrayList<CampoAdicional>();

        camp.add(adicional);
     



        fac.setCampoAdicional(camp);

        XMLUtil xml = new XMLUtil();

        if (ncliente.equalsIgnoreCase("Consumidor Final")) {

            xml.doMarshall(fac, "consumidores_finales\\" + s1 + "-" + s2 + "-" + ss3 + ".xml");

        } else {

            xml.doMarshall(fac, "generados\\" + s1 + "-" + s2 + "-" + ss3 + ".xml");

        }
        return 1;









    }
}
