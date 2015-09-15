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


import modelo.notacredito.*;
import util.XMLUtil;

/**
 *
 * @author r
 */
public class crear_nota_credito_normal1 {

    public int crear_nota_credito_normal(String[] cp, String nc, String oconta, int cest, String ambiente, String rz, String r, String cadena, String s1, String s2, String ss3, String d, String fecha, String ti, String ncliente, String clientr, Double[] subtotalP, Double[] ivaP, Double[] totalP, String[] producto, Double[] cantidad, Double[] pu, String FechaFactura, String numeroFactura, String motivo,Double total,Double subtotal,Double iva) {
   //  crear_factura_credito(String tp, String cp, String nc, String oconta, int cest, String ambiente, String rz, String r, String cadena, String s1, String s2, String ss3, String d, String fecha, String ti, String ncliente, String clientr, Double subtotalF, Double ivaF, Double totalF, String [] producto, Double[] cantidad, Double[] pu,Double ivaP[],Double [] totalP,Double [] subtotalP) {


        DecimalFormat df = new DecimalFormat("#.##");

        total = Double.valueOf(df.format(total).replace(",", "."));

        subtotal = Double.valueOf(df.format(subtotal).replace(",", "."));

        iva = Double.valueOf(df.format(iva).replace(",", "."));
        for (int i=0;i<pu.length;i++){
        pu[i]=Double.valueOf(df.format(pu[i]).replace(",", "."));
        ivaP[i]=Double.valueOf(df.format(ivaP[i]).replace(",", "."));
        }

     //   pu = Double.valueOf(df.format(subtotal / cantidad).replace(",", "."));



        NotaCredito not = new NotaCredito();

        not.setId("comprobante");
        not.setVersion("1.1.0");
        InfoTributaria it = new InfoTributaria();
        it.setAmbiente(ambiente);
        it.setTipoEmision("1");
        it.setRazonSocial(rz);
        it.setNombreComercial(nc);
        it.setRuc(r);
        it.setClaveAcceso(cadena);
        it.setCodDoc("04");
        it.setPtoEmi(s2);
        it.setEstab(s1);
        it.setSecuencial(ss3);
        it.setDirMatriz(d);
        not.setInfoTributaria(it);

        InfoNotaCredito inf = new InfoNotaCredito();
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

        inf.setCodDocModificado("01");
        inf.setNumDocModificado(numeroFactura);
        inf.setFechaEmisionDocSustento(FechaFactura);
        inf.setTotalSinImpuestos(BigDecimal.valueOf(subtotal));
        inf.setValorModificacion(BigDecimal.valueOf(total));
        inf.setMoneda("Dolar");

        TotalImpuesto tim = new TotalImpuesto();
        tim.setCodigo("2");
        tim.setCodigoPorcentaje("2");
        tim.setBaseImponible(BigDecimal.valueOf(subtotal));
        tim.setValor(BigDecimal.valueOf(iva));


        List<TotalImpuesto> listim = new ArrayList<TotalImpuesto>();

        listim.add(tim);

        TotalConImpuestos timps = new TotalConImpuestos();
        timps.setTotalImpuesto(listim);

        inf.setTotalConImpuestos(timps);


        inf.setMotivo(motivo);
        
                List<Detalle> detallef = new ArrayList<Detalle>();

for (int i=0;i<producto.length;i++){
        Detalle detalle = new Detalle();

        detalle.setCodigoInterno(cp[i]);
        detalle.setCodigoAdicional(cp[i]);
        detalle.setDescripcion(producto[i]);
        detalle.setCantidad(BigDecimal.valueOf(cantidad[i]));
        detalle.setPrecioUnitario(BigDecimal.valueOf(pu[i]));
        detalle.setDescuento(BigDecimal.valueOf(0.00));
        detalle.setPrecioTotalSinImpuesto(BigDecimal.valueOf(subtotalP[i]));

        Impuesto imp = new Impuesto();
        imp.setCodigo("2");
        imp.setCodigoPorcentaje("2");
        imp.setTarifa(BigDecimal.valueOf(12));
        imp.setBaseImponible(BigDecimal.valueOf(subtotalP[i]));
        imp.setValor(BigDecimal.valueOf(ivaP[i]));



        List<Impuesto> impuesto = new ArrayList<Impuesto>();

        impuesto.add(imp);



        detalle.setImpuesto(impuesto);




        detallef.add(detalle);

}
      
        
        not.setDetalle(detallef);



        
        


        CampoAdicional adicional = new CampoAdicional();

        adicional.setNombre("Imprime");
        adicional.setValue("Advadvantech");

        List<CampoAdicional> camp = new ArrayList<CampoAdicional>();
        camp.add(adicional);
        not.setCampoAdicional(camp);


        not.setInfoNotaCredito(inf);


        XMLUtil xml = new XMLUtil();



        xml.doMarshall(not, "notas_generadas\\nc" + s1 + "-" + s2 + "-" + ss3 + ".xml");


        return 1;




















    }
}
