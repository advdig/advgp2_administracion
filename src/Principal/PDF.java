/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import reportes.detalle;
import reportes.producto;

/**
 *
 * @author r
 */
public class PDF {

    String nf;
    Map pars;
    Properties props;
    Session session;
    String correo, xml, pdf, correocliente, npdf, nxml;
    MimeMessage message;
    String ncliente, contraseña;
    LinkedList<producto> listaproducto;

    public PDF() {

    }

    public void visualizar_pdf(String numero, String usuario, String contraseña) {
        String ruc = "", dm = "", ds = "", ce = "", nce = "", numAut = "", fechahora = "", clave = "", temision = "", ambiente = "", rz = "", nc = "", rzc = "", ruc_comp = "", f_emision = "", guia = "", subtotal = "", iva = "", total = "", descuento = "", cp = "";
        String producto = null, detalle = null;
        Double cantidad = null, pu = null, subtotalp = null;
        String descuentop = "", tipo = "";
        Collection infoAdicional;
        InputStream icono = null;
        try {
            icono = new FileInputStream("ad.jpg");
            InputStream marca_agua;
            conexion_facturacion he = new conexion_facturacion(usuario, contraseña);

            he.conectar();

            Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision,adv_facturacion.factura where factura.datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and factura.numero='"+numero+"';");

            while (ri.next()) {

                rz = ri.getString(2);

                dm = ri.getString(4);
                
                ds = ri.getString(4);

                ruc = ri.getString(3);

                if (ri.getInt(9) == 1) {

                    ambiente = "PRUEBAS";
                } else {

                    ambiente = "PRODUCCION";
                }

                ce = ri.getString(10);
                nc = ri.getString(11);
                nce = ri.getString(12);

            }

            String idf = "";

            Statement st_fac = he.coneccion.createStatement();
            ResultSet rifa = st_fac.executeQuery("SELECT factura.`fecha` AS factura_fecha,factura.`hora` AS factura_hora,factura.`numero_autorizacion` AS factura_numero_autorizacion,factura.`subtotal` AS factura_subtotal,factura.`iva` AS factura_iva,factura.`total` AS factura_total,cliente.`nombre` AS cliente_nombre, cliente.`cedula_ruc` AS cliente_cedula_ruc,clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,clave_acceso.`tipo` AS clave_acceso_tipo,factura.idfactura\n"
                    + " FROM  `cliente` cliente INNER JOIN `factura` factura ON cliente.`idcliente` = factura.`cliente_idcliente` INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` where numero='" + numero + "'");

            while (rifa.next()) {

                idf = rifa.getString(11);
                numAut = rifa.getString(3);
                fechahora = rifa.getString(1) + "  " + rifa.getString(2);
                clave = rifa.getString(9);
                rzc = rifa.getString(7);
                ruc_comp = rifa.getString(8);
                f_emision = rifa.getString(1);
                subtotal = rifa.getString(4);
                iva = rifa.getString(5);
                total = rifa.getString(6);
                temision = rifa.getString(10);
            }
            int cd;

            marca_agua = new FileInputStream("produccion.jpeg");

            JasperCompileManager.compileReportToFile("factura.jrxml");
            //2. Se llena el reporte con la informacion necesaria
            pars = new HashMap();
            pars.put("RUC", ruc);
            pars.put("LOGO", icono);
            pars.put("NUM_FACT", numero);
            pars.put("NUM_AUT", numAut);
            pars.put("FECHA_AUT", fechahora);
            pars.put("CLAVE_ACC", clave);
            pars.put("TIPO_EMISION", temision);
            pars.put("AMBIENTE", ambiente);
            pars.put("RAZON_SOCIAL", rz);
            pars.put("TIPO_EMISION", temision);
            pars.put("NOM_COMERCIAL", nc);
            pars.put("DIR_MATRIZ", dm);
            pars.put("DIR_SUCURSAL", ds);
            if (Integer.parseInt(nce) == 0) {
            } else {
                pars.put("CONT_ESPECIAL", nce);
            }
            pars.put("LLEVA_CONTABILIDAD", ce);
            pars.put("RS_COMPRADOR", rzc);
            pars.put("RUC_COMPRADOR", ruc_comp);
            pars.put("FECHA_EMISION", f_emision);
            pars.put("GUIA", guia);

            pars.put("SUBTOTAL", subtotal);

            if (iva.equalsIgnoreCase("0.00")) {

                pars.put("IVA_12","0");

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0",subtotal);

            } else {
                pars.put("IVA_12", subtotal);

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0", "0");
            }
            pars.put("NO_OBJETO_IVA", "0");
            pars.put("ICE", "0");
            pars.put("VALOR_TOTAL", total);
            pars.put("MARCA_AGUA", marca_agua);
            Collection info = null;

            Statement st_facd = he.coneccion.createStatement();
            ResultSet rifad = st_facd.executeQuery("SELECT\n"
                    + "     factura_detalle.`cantidad` AS factura_detalle_cantidad,\n"
                    + "     factura_detalle.`subtotal` AS factura_detalle_subtotal,\n"
                    + "     factura_detalle.`total` AS factura_detalle_total,\n"
                    + "     factura_detalle.`iva` AS factura_detalle_iva,\n"
                    + "     producto.`idproducto` AS producto_idproducto,\n"
                    + "     producto.`nombre` AS producto_nombre,\n"
                    + "     factura_detalle.`p_unit` AS producto_punit,\n"
                    + "     IFNULL(factura_detalle.`detalle_adicional` ,' ')AS producto_nombre,\n"
                    + "     factura_detalle.`p_unit` AS producto_punit,\n"
                    + "     producto.tipo_producto_idtipo_producto AS producto_nombre\n"
                    + "FROM\n"
                    + "     `producto` producto INNER JOIN `factura_detalle` factura_detalle ON producto.`idproducto` = factura_detalle.`producto_idproducto` "
                    + "	where  factura_idfactura=" + idf + "");
            DecimalFormat df1 = new DecimalFormat("#.######");
            listaproducto = new LinkedList<producto>();

            Collection lista = new LinkedList<detalle>();

            while (rifad.next()) {

                
                
                if(rifad.getInt(10)==4){
                
                    lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                  listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), rifad.getString(9), rifad.getString(2), rifad.getString(6), "", "", descuentop, lista));

                    
                    
                }else{
                
                lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), df1.format(rifad.getDouble(9) / 1.12), rifad.getString(2), rifad.getString(6), "", "", descuentop, lista));

                }   
                
            }

            //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
            //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));
            JasperReport report = JasperCompileManager.compileReport("factura.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, pars, new JRBeanCollectionDataSource(listaproducto));

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(jasperPrint, false);
            //3. Se exporta a PDF

            System.out.println("pdf\\" + numero + ".pdf");
            // JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\" + numero + ".pdf");
            System.out.println("Done!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                icono.close();
            } catch (IOException ex) {
                Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
    
        public void visualizar_pdf_notas(String numero, String usuario, String contraseña) {
        String ruc = "", dm = "", ds = "", ce = "", nce = "", numAut = "", fechahora = "", clave = "", temision = "", ambiente = "", rz = "", nc = "", rzc = "", ruc_comp = "", f_emision = "", guia = "", subtotal = "", iva = "", total = "", descuento = "", cp = "";
        String producto = null, detalle = null;
        Double cantidad = null, pu = null, subtotalp = null;
        String descuentop = "", tipo = "";
        Collection infoAdicional;
        InputStream icono = null;
        try {
            icono = new FileInputStream("ad.jpg");
            InputStream marca_agua;
            conexion_facturacion he = new conexion_facturacion(usuario, contraseña);

            he.conectar();

            Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

                rz = ri.getString(2);

                dm = ri.getString(4);
                
                ds = ri.getString(4);

                ruc = ri.getString(3);

                if (ri.getInt(9) == 1) {

                    ambiente = "PRUEBAS";
                } else {

                    ambiente = "PRODUCCION";
                }

                ce = ri.getString(10);
                nc = ri.getString(11);
                nce = ri.getString(12);

            }

            String idf = "";

            Statement st_fac = he.coneccion.createStatement();
            ResultSet rifa = st_fac.executeQuery("SELECT nota_credito.`fecha` AS nota_fecha,\n" +
"nota_credito.`autorizacion` AS nota_numero_autorizacion,\n" +
"nota_credito.`subtotal` AS nota_subtotal,\n" +
"nota_credito.`iva` AS nota_iva,\n" +
"nota_credito.`valor_modificacion` AS nota_total,\n" +
"cliente.`nombre` AS cliente_nombre, \n" +
"cliente.`cedula_ruc` AS cliente_cedula_ruc,\n" +
"clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n" +
"clave_acceso.`tipo` AS clave_acceso_tipo,nota_credito.idnota_credito,\n" +
"nota_credito.`motivo` As motivo,\n" +
"factura.`numero` As numero_factura,\n" +
"factura.`fecha` as facha_factura,\n" +
"nota_credito.`hora` as nota_hora\n" +
"FROM  `cliente` cliente\n" +
"INNER JOIN `nota_credito` nota_credito ON cliente.`idcliente` = nota_credito.`factura_cliente_idcliente` \n" +
"INNER JOIN `clave_acceso` clave_acceso ON nota_credito.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` \n" +
"INNER JOIN `factura` factura ON factura.`idfactura`=nota_credito.factura_idfactura\n" +
"where      nota_credito.numero='" + numero + "'");

String modifica="";
String modificaFecha="";
String modificaRazon="";
while (rifa.next()) {

                idf = rifa.getString(10);
                numAut = rifa.getString(2);
                fechahora = rifa.getString(1) + rifa.getString(14) ;
                clave = rifa.getString(8);
                rzc = rifa.getString(6);
                ruc_comp = rifa.getString(7);
                f_emision = rifa.getString(1);
                subtotal = rifa.getString(3);
                iva = rifa.getString(4);
                total = rifa.getString(5);
                temision = rifa.getString(9);
                modifica=rifa.getString(12);
                modificaRazon=rifa.getString(11);
                modificaFecha=rifa.getString(13);
                System.out.println("hora  ........"+rifa.getString(14) );

            }
            int cd;

            marca_agua = new FileInputStream("produccion.jpeg");

            JasperCompileManager.compileReportToFile("nota_credito.jrxml");
            //2. Se llena el reporte con la informacion necesaria
            pars = new HashMap();
            pars.put("RUC", ruc);
            pars.put("LOGO", icono);
            pars.put("NUM_FACT", numero);
            pars.put("NUM_AUT", numAut);
            pars.put("FECHA_AUT", fechahora);
          
            System.out.println(clave);
            
            pars.put("CLAVE_ACC", clave);
            pars.put("TIPO_EMISION", temision);
            pars.put("AMBIENTE", ambiente);
            pars.put("RAZON_SOCIAL", rz);
            pars.put("TIPO_EMISION", temision);
            pars.put("NOM_COMERCIAL", nc);
            pars.put("DIR_MATRIZ", dm);
            pars.put("DIR_SUCURSAL", ds);
            if (Integer.parseInt(nce) == 0) {
            } else {
                pars.put("CONT_ESPECIAL", nce);
            }
            pars.put("LLEVA_CONTABILIDAD", ce);
            pars.put("RS_COMPRADOR", rzc);
            pars.put("RUC_COMPRADOR", ruc_comp);
            pars.put("FECHA_EMISION", f_emision);
            pars.put("GUIA", guia);

            pars.put("SUBTOTAL", subtotal);

            if (iva.equalsIgnoreCase("0.00")) {

                pars.put("IVA_12","0");

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0",subtotal);

            } else {
                pars.put("IVA_12", subtotal);

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0", "0");
            }
            pars.put("NO_OBJETO_IVA", "0");
            pars.put("ICE", "0");
            
            pars.put("DOC_MODIFICADO", modifica);
            pars.put("MOTIVO", modificaRazon);
            pars.put("DOC_FECHA", modificaFecha);
            
            pars.put("VALOR_TOTAL", total);
            pars.put("MARCA_AGUA", marca_agua);
            Collection info = null;

            Statement st_facd = he.coneccion.createStatement();
            ResultSet rifad = st_facd.executeQuery("SELECT\n" +
"                         nota_credito_detalle.`cantidad` AS nota_detalle_cantidad,\n" +
"                         nota_credito_detalle.`subtotal` AS nota_detalle_subtotal,\n" +
"                         nota_credito_detalle.`total` AS nota_detalle_total,\n" +
"                         nota_credito_detalle.`iva` AS nota_detalle_iva,\n" +
"                         producto.`idproducto` AS producto_idproducto,\n" +
"                         producto.`nombre` AS producto_nombre,\n" +
"                         producto.`punit` AS producto_punit,\n" +
"                         producto.`nombre` AS producto_nombre,\n" +
"                         producto.punit/1.12 AS producto_precio_SIN,\n" +
"                         producto.tipo_producto_idtipo_producto AS producto_nombre\n" +
"                    FROM\n" +
"                         `producto` producto INNER JOIN `nota_credito_detalle` nota_credito_detalle ON producto.`idproducto` = nota_credito_detalle.`producto_idproducto` \n" +
"                    	where  nota_credito_idnota_credito=" + idf + "");
            DecimalFormat df1 = new DecimalFormat("#.######");
            listaproducto = new LinkedList<producto>();

            Collection lista = new LinkedList<detalle>();

            while (rifad.next()) {

                
                if(rifad.getInt(10)==4){
                
                    lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), rifad.getString(9), rifad.getString(2), rifad.getString(6), "", "", descuentop, null));

                    
                    
                }else{
                
                lista.add(new detalle(rifad.getString(5),""));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), df1.format(rifad.getDouble(7) / 1.12), rifad.getString(2), rifad.getString(6), "", "", descuentop, null));

                }   
                
            }

            //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
            //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));
            JasperReport report = JasperCompileManager.compileReport("nota_credito.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, pars, new JRBeanCollectionDataSource(listaproducto));

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(jasperPrint, false);
            //3. Se exporta a PDF
            

            System.out.println("pdf\\" + numero + ".pdf");
            // JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\" + numero + ".pdf");
            System.out.println("Done!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                icono.close();
            } catch (IOException ex) {
                Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
      
        
                public void visualizar_pdf_clientes_credito(String cedula,String total,String condicion) {
        Collection infoAdicional;
        InputStream icono = null;
        try {
            icono = new FileInputStream("ad.jpg");
            InputStream marca_agua;

            


            marca_agua = new FileInputStream("produccion.jpeg");

            JasperCompileManager.compileReportToFile("reporteClientesCredito.jrxml");
            //2. Se llena el reporte con la informacion necesaria
            pars = new HashMap();
            pars.put("ruc", cedula);
            pars.put("total", total);
            pars.put("condicion", condicion);
       
      
            Collection info = null;

            //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
            //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));
            JasperReport report = JasperCompileManager.compileReport("reporteClientesCredito.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, pars);

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(jasperPrint, false);
            //3. Se exporta a PDF
            

            // JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\" + numero + ".pdf");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);        
        
        } catch (JRException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                icono.close();
            } catch (IOException ex) {
                Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
        
      public void crear_pdf(String numero, String usuario, String contraseña) {
        String ruc = "", dm = "", ds = "", ce = "", nce = "", numAut = "", fechahora = "", clave = "", temision = "", ambiente = "", rz = "", nc = "", rzc = "", ruc_comp = "", f_emision = "", guia = "", subtotal = "", iva = "", total = "", descuento = "", cp = "";
        String producto = null, detalle = null;
        Double cantidad = null, pu = null, subtotalp = null;
        String descuentop = "", tipo = "";

        InputStream icono = null;
        try {
            icono = new FileInputStream("ad.jpg");
            InputStream marca_agua;
            conexion_facturacion he = new conexion_facturacion(usuario, contraseña);

            he.conectar();

            Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

                rz = ri.getString(2);

                dm = ri.getString(4);
                ds = ri.getString(4);

                ruc = ri.getString(3);

                if (ri.getInt(9) == 1) {

                    ambiente = "PRUEBAS";
                } else {

                    ambiente = "PRODUCCION";
                }

                ce = ri.getString(10);
                nc = ri.getString(11);
                nce = ri.getString(12);

            }

            String idf = "";

            Statement st_fac = he.coneccion.createStatement();
            ResultSet rifa = st_fac.executeQuery("SELECT factura.`fecha` AS factura_fecha,factura.`hora` AS factura_hora,factura.`numero_autorizacion` AS factura_numero_autorizacion,factura.`subtotal` AS factura_subtotal,factura.`iva` AS factura_iva,factura.`total` AS factura_total,cliente.`nombre` AS cliente_nombre, cliente.`cedula_ruc` AS cliente_cedula_ruc,clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,clave_acceso.`tipo` AS clave_acceso_tipo,factura.idfactura\n"
                    + " FROM  `cliente` cliente INNER JOIN `factura` factura ON cliente.`idcliente` = factura.`cliente_idcliente` INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` where numero='" + numero + "'");

            while (rifa.next()) {

                idf = rifa.getString(11);
                numAut = rifa.getString(3);
                fechahora = rifa.getString(1) + "  " + rifa.getString(2);
                clave = rifa.getString(9);
                rzc = rifa.getString(7);
                ruc_comp = rifa.getString(8);
                f_emision = rifa.getString(1);
                subtotal = rifa.getString(4);
                iva = rifa.getString(5);
                total = rifa.getString(6);
                temision = rifa.getString(10);
            }
            int cd;

            Statement st_facd = he.coneccion.createStatement();
            ResultSet rifad = st_facd.executeQuery("SELECT\n"
                    + "     factura_detalle.`cantidad` AS factura_detalle_cantidad,\n"
                    + "     factura_detalle.`subtotal` AS factura_detalle_subtotal,\n"
                    + "     factura_detalle.`total` AS factura_detalle_total,\n"
                    + "     factura_detalle.`iva` AS factura_detalle_iva,\n"
                    + "     producto.`idproducto` AS producto_idproducto,\n"
                    + "     producto.`nombre` AS producto_nombre,\n"
                    + "     factura_detalle.`p_unit` AS producto_punit,\n"
                    + "     IFNULL(factura_detalle.`detalle_adicional`,' ') AS producto_nombre,\n"
                    + "     factura_detalle.`p_unit` AS producto_punit,\n"
                    + "     producto.tipo_producto_idtipo_producto AS producto_nombre\n"
                    + "FROM\n"
                    + "     `producto` producto INNER JOIN `factura_detalle` factura_detalle ON producto.`idproducto` = factura_detalle.`producto_idproducto` "
                    + "	where  factura_idfactura=" + idf + "");
            
            
            
            DecimalFormat df1 = new DecimalFormat("#.######");
            listaproducto = new LinkedList<producto>();

            Collection lista = new LinkedList<detalle>();

            while (rifad.next()) {

                
                if(rifad.getInt(10)==4){
                
                    lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                   listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), rifad.getString(9), rifad.getString(2), rifad.getString(6), "", "", descuentop, lista));

                    
                    
                }else{
                
                lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), df1.format(rifad.getDouble(7) / 1.12), rifad.getString(2), rifad.getString(6), "", "", descuentop, lista));

                }   
                
            }

            marca_agua = new FileInputStream("produccion.jpeg");

            JasperCompileManager.compileReportToFile("factura.jrxml");
            //2. Se llena el reporte con la informacion necesaria
            pars = new HashMap();
            pars.put("RUC", ruc);
            pars.put("LOGO", icono);
            pars.put("NUM_FACT", numero);
            pars.put("NUM_AUT", numAut);
            pars.put("FECHA_AUT", fechahora);
            pars.put("CLAVE_ACC", clave);
            pars.put("TIPO_EMISION", temision);
            pars.put("AMBIENTE", ambiente);
            pars.put("RAZON_SOCIAL", rz);
            pars.put("TIPO_EMISION", temision);
            pars.put("NOM_COMERCIAL", nc);
            pars.put("DIR_MATRIZ", dm);
            pars.put("DIR_SUCURSAL", ds);
            if (Integer.parseInt(nce) == 0) {
            } else {
                pars.put("CONT_ESPECIAL", nce);
            }
            pars.put("LLEVA_CONTABILIDAD", ce);
            pars.put("RS_COMPRADOR", rzc);
            pars.put("RUC_COMPRADOR", ruc_comp);
            pars.put("FECHA_EMISION", f_emision);
            pars.put("GUIA", guia);
             pars.put("SUBTOTAL", subtotal); 
            if (iva.equalsIgnoreCase("0.00")) {

                pars.put("IVA_12","0");

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0",subtotal);

            } else {
                pars.put("IVA_12", subtotal);

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0", "0");
            }
            pars.put("NO_OBJETO_IVA", "0");
            pars.put("ICE", "0");
            pars.put("VALOR_TOTAL", total);
            pars.put("MARCA_AGUA", marca_agua);
            Collection info = null;

            //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
            //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));
            JasperReport report = JasperCompileManager.compileReport("factura.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, pars, new JRBeanCollectionDataSource(listaproducto));

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            //JasperViewer.viewReport(jasperPrint, false);
            //3. Se exporta a PDF
            System.out.println("pdf\\" + numero + ".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, "pdf\\" + numero + ".pdf");
            System.out.println("Done!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                icono.close();
            } catch (IOException ex) {
                Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }  
    public void crear_pdf_notas(String numero, String usuario, String contraseña) {        
        String ruc = "", dm = "", ds = "", ce = "", nce = "", numAut = "", fechahora = "", clave = "", temision = "", ambiente = "", rz = "", nc = "", rzc = "", ruc_comp = "", f_emision = "", guia = "", subtotal = "", iva = "", total = "", descuento = "", cp = "";
        String producto = null, detalle = null;
        Double cantidad = null, pu = null, subtotalp = null;
        String descuentop = "", tipo = "";

        InputStream icono = null;
        try {
            icono = new FileInputStream("ad.jpg");
            InputStream marca_agua;
            conexion_facturacion he = new conexion_facturacion(usuario, contraseña);

            he.conectar();

            Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

                rz = ri.getString(2);

                dm = ri.getString(4);
                ds = ri.getString(4);

                ruc = ri.getString(3);

                if (ri.getInt(9) == 1) {

                    ambiente = "PRUEBAS";
                } else {

                    ambiente = "PRODUCCION";
                }

                ce = ri.getString(10);
                nc = ri.getString(11);
                nce = ri.getString(12);

            }

            String idf = "";

            Statement st_fac = he.coneccion.createStatement();
           
            
  System.out.println("SELECT nota_credito.`fecha` AS nota_fecha,\n" +
"nota_credito.`autorizacion` AS nota_numero_autorizacion,\n" +
"nota_credito.`subtotal` AS nota_subtotal,\n" +
"nota_credito.`iva` AS nota_iva,\n" +
"nota_credito.`valor_modificacion` AS nota_total,\n" +
"cliente.`nombre` AS cliente_nombre, \n" +
"cliente.`cedula_ruc` AS cliente_cedula_ruc,\n" +
"clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n" +
"clave_acceso.`tipo` AS clave_acceso_tipo,nota_credito.idnota_credito,\n" +
"nota_credito.`motivo` As motivo,\n" +
"factura.`numero` As numero_factura,\n" +
"factura.`fecha` as facha_factura,\n" +
"nota_credito.`hora` as nota_hora\n" +
"FROM  `cliente` cliente\n" +
"INNER JOIN `nota_credito` nota_credito ON cliente.`idcliente` = nota_credito.`factura_cliente_idcliente` \n" +
"INNER JOIN `clave_acceso` clave_acceso ON nota_credito.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` \n" +
"INNER JOIN `factura` factura ON factura.`idfactura`=nota_credito.factura_idfactura\n" +
"where      nota_credito.numero='" + numero + "'");
            
            ResultSet rifa = st_fac.executeQuery("SELECT nota_credito.`fecha` AS nota_fecha,\n" +
"nota_credito.`autorizacion` AS nota_numero_autorizacion,\n" +
"nota_credito.`subtotal` AS nota_subtotal,\n" +
"nota_credito.`iva` AS nota_iva,\n" +
"nota_credito.`valor_modificacion` AS nota_total,\n" +
"cliente.`nombre` AS cliente_nombre, \n" +
"cliente.`cedula_ruc` AS cliente_cedula_ruc,\n" +
"clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n" +
"clave_acceso.`tipo` AS clave_acceso_tipo,nota_credito.idnota_credito,\n" +
"nota_credito.`motivo` As motivo,\n" +
"factura.`numero` As numero_factura,\n" +
"factura.`fecha` as facha_factura,\n" +
"nota_credito.`hora` as nota_hora\n" +
"FROM  `cliente` cliente\n" +
"INNER JOIN `nota_credito` nota_credito ON cliente.`idcliente` = nota_credito.`factura_cliente_idcliente` \n" +
"INNER JOIN `clave_acceso` clave_acceso ON nota_credito.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` \n" +
"INNER JOIN `factura` factura ON factura.`idfactura`=nota_credito.factura_idfactura\n" +
"where      nota_credito.numero='" + numero + "'");

            
            
String modifica="";
String modificaFecha="";
String modificaRazon="";
while (rifa.next()) {

                idf = rifa.getString(10);
                numAut = rifa.getString(2);
                fechahora = rifa.getString(1) + rifa.getString(14) ;
                clave = rifa.getString(8);
                rzc = rifa.getString(6);
                ruc_comp = rifa.getString(7);
                f_emision = rifa.getString(1);
                subtotal = rifa.getString(3);
                iva = rifa.getString(4);
                total = rifa.getString(5);
                temision = rifa.getString(9);
                modifica=rifa.getString(12);
                modificaRazon=rifa.getString(11);
                modificaFecha=rifa.getString(13);
                System.out.println("hora  ........"+rifa.getString(14) );

            }
            int cd;

             Statement st_facd = he.coneccion.createStatement();
            ResultSet rifad = st_facd.executeQuery("SELECT\n" +
"                         nota_credito_detalle.`cantidad` AS nota_detalle_cantidad,\n" +
"                         nota_credito_detalle.`subtotal` AS nota_detalle_subtotal,\n" +
"                         nota_credito_detalle.`total` AS nota_detalle_total,\n" +
"                         nota_credito_detalle.`iva` AS nota_detalle_iva,\n" +
"                         producto.`idproducto` AS producto_idproducto,\n" +
"                         producto.`nombre` AS producto_nombre,\n" +
"                         producto.`punit` AS producto_punit,\n" +
"                         producto.`nombre` AS producto_nombre,\n" +
"                         producto.punit/1.12 AS producto_precio_SIN,\n" +
"                         producto.tipo_producto_idtipo_producto AS producto_nombre\n" +
"                    FROM\n" +
"                         `producto` producto INNER JOIN `nota_credito_detalle` nota_credito_detalle ON producto.`idproducto` = nota_credito_detalle.`producto_idproducto` \n" +
"                    	where  nota_credito_idnota_credito=" + idf + "");
            
            
            
            DecimalFormat df1 = new DecimalFormat("#.######");
            listaproducto = new LinkedList<producto>();

            Collection lista = new LinkedList<detalle>();

            while (rifad.next()) {

                
                if(rifad.getInt(10)==4){
                
                    lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), rifad.getString(9), rifad.getString(2), rifad.getString(6), "", "", descuentop, null));

                    
                    
                }else{
                
                lista.add(new detalle(rifad.getString(5),rifad.getString(8).trim()));

                listaproducto.add(new producto(rifad.getString(5), "", String.valueOf(rifad.getDouble(1)), rifad.getString(6), df1.format(rifad.getDouble(7) / 1.12), rifad.getString(2), rifad.getString(6), "", "", descuentop, null));

                }   
                
            }

            marca_agua = new FileInputStream("produccion.jpeg");

            JasperCompileManager.compileReportToFile("nota_credito.jrxml");
            //2. Se llena el reporte con la informacion necesaria
            pars = new HashMap();
            pars.put("RUC", ruc);
            pars.put("LOGO", icono);
            pars.put("NUM_FACT", numero);
            pars.put("NUM_AUT", numAut);
            pars.put("FECHA_AUT", fechahora);
            pars.put("CLAVE_ACC", clave);
            pars.put("TIPO_EMISION", temision);
            pars.put("AMBIENTE", ambiente);
            pars.put("RAZON_SOCIAL", rz);
            pars.put("TIPO_EMISION", temision);
            pars.put("NOM_COMERCIAL", nc);
            pars.put("DIR_MATRIZ", dm);
            pars.put("DIR_SUCURSAL", ds);
            if (Integer.parseInt(nce) == 0) {
            } else {
                pars.put("CONT_ESPECIAL", nce);
            }
            pars.put("LLEVA_CONTABILIDAD", ce);
            pars.put("RS_COMPRADOR", rzc);
            pars.put("RUC_COMPRADOR", ruc_comp);
            pars.put("FECHA_EMISION", f_emision);
            pars.put("GUIA", guia);
             pars.put("SUBTOTAL", subtotal); 
            if (iva.equalsIgnoreCase("0.00")) {

                pars.put("IVA_12","0");

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0",subtotal);

            } else {
                pars.put("IVA_12", subtotal);

                pars.put("IVA", iva);
                pars.put("TOTAL_DESCUENTO", descuento);
                pars.put("IVA_0", "0");
            }
            pars.put("NO_OBJETO_IVA", "0");
            pars.put("ICE", "0");
            pars.put("DOC_MODIFICADO", modifica);
            pars.put("MOTIVO", modificaRazon);
            pars.put("DOC_FECHA", modificaFecha);
            pars.put("VALOR_TOTAL", total);
            pars.put("MARCA_AGUA", marca_agua);
            Collection info = null;

            //listaEmpleados.add(new producto("0987654321", "Marcelo C", 500));
            //listaEmpleados.add(new producto("1234509876", "Don Bill", 5000));
            JasperReport report = JasperCompileManager.compileReport("nota_credito.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, pars, new JRBeanCollectionDataSource(listaproducto));

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            //JasperViewer.viewReport(jasperPrint, false);
            //3. Se exporta a PDF
            System.out.println("notas_pdf\\" + numero + ".pdf");
            JasperExportManager.exportReportToPdfFile(jasperPrint, "notas_pdf\\" + numero + ".pdf");
            System.out.println("Done!");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                icono.close();
            } catch (IOException ex) {
                Logger.getLogger(PDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
