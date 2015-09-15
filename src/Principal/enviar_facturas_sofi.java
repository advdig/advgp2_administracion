/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import conexion.conexion_sis_contable;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author r
 */
public class enviar_facturas_sofi {

    private static final int NUM_PROVINCIAS = 24;
    private static int[] coeficientes = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static int constante = 11;
    
    String contraseña, usuario;
    conexion_facturacion cf;
    conexion_sis_contable cc;

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public enviar_facturas_sofi(String usuario, String contraseña) {
        try {
            this.usuario = usuario;
            this.contraseña = contraseña;

            cf = new conexion_facturacion(usuario, contraseña);
            cc = new conexion_sis_contable("useradv", "adv111");

            cf.conectar();
            cc.conectar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void verificar_facturas() {

        String mp = "";
        try {


            Statement st_in = cf.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT\n"
                    + "     factura.idfactura,\n"
                    + "	    factura.`Estado_factura` AS factura_Estado_factura,\n"
                    + "     SUBSTRING(factura.`numero`,9) AS factura_numero,\n"
                    + "     factura.`fecha` AS factura_fecha,\n"
                    + "     factura.`hora` AS factura_hora,\n"
                    + "     datos_gasolinera.`secuencia1_factura` AS datos_gasolinera_ruc,\n"
                    + "     punto_emision.`s2` AS punto_emision_s2,\n"
                    + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                    + "     factura.`usuarios_idusuarios` AS factura_usuarios_idusuarios,\n"
                    + "     factura.`numero_autorizacion` AS factura_numero_autorizacion,\n"
                    + "     factura.`subtotal` AS factura_subtotal,\n"
                    + "     factura.`total` AS factura_total,\n"
                    + "     factura.`iva` AS factura_iva,\n"
                    + "     datos_gasolinera.`ruc` AS datos_gasolinera_ruc,\n"
                    + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                    + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                    + "     usuarios.`usuario` AS usuarios_usuario,\n"
                    + "     cliente.`idcliente` AS cliente_id\n"
                    + "FROM\n"
                    + "     `punto_emision` punto_emision INNER JOIN `factura` factura ON punto_emision.`idpunto_emision` = factura.`punto_emision_idpunto_emision`\n"
                    + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                    + "     INNER JOIN `datos_gasolinera` datos_gasolinera ON punto_emision.`datos_gasolinera_iddatos_gasolinera` = datos_gasolinera.`iddatos_gasolinera`\n"
                    + "     INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                    + "     INNER JOIN `cliente` cliente ON factura.`cliente_idcliente` = cliente.`idcliente`\n"
                    + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                    + "where  punto_emision_idpunto_emision=idpunto_emision and enviado_contable=1 and (factura.Estado_factura='AUTORIZADO' or factura.Estado_factura='ANULADO') and numero_autorizacion > 0");



            String idcliente;


            while (ri.next()) {
                int ndetalle = 0;

                ResultSet fac = cc.consulta("select * from ESFCElectronica where PtoEmision=" + ri.getInt(7) + " and Secuencial=" + ri.getInt(3) + "");



                if (fac.first()) {
                } else {

                    System.out.println("Factura no Existe en el Sistema SOFI");













                    System.out.println("entro " + ri.getInt(1));
                    Statement st_fdc = cf.coneccion.createStatement();
                    ResultSet ri_fdc = st_fdc.executeQuery("SELECT * FROM adv_facturacion.factura_detalle where factura_idfactura=" + ri.getInt(1) + " group by factura_detalle.producto_idproducto ;");
                    while (ri_fdc.next()) {
                        ndetalle++;


                    }


                    Statement st_fd = cf.coneccion.createStatement();

                    ResultSet ri_fd = st_fd.executeQuery("SELECT\n"
                            + "     sum(factura_detalle.`cantidad`) AS factura_detalle_cantidad,\n"
                            + "     sum(factura_detalle.`subtotal`) AS factura_detalle_subtotal,\n"
                            + "     sum(factura_detalle.`total`) AS factura_detalle_total,\n"
                            + "     factura_detalle.`configuracion_nmanguera` AS factura_detalle_configuracion_nmanguera,\n"
                            + "     sum(factura_detalle.`iva`) AS factura_detalle_iva,\n"
                            + "     producto.`nombre` AS producto_nombre,\n"
                            + "     configuracion.`surtidor` AS configuracion_surtidor,\n"
                            + "     configuracion.`tanques_idtanques` AS configuracion_tanques_idtanques,\n"
                            + "     producto.`punit` AS producto_punit\n"
                            + "FROM\n"
                            + "     `producto` producto INNER JOIN `factura_detalle` factura_detalle ON producto.`idproducto` = factura_detalle.`producto_idproducto`\n"
                            + "     INNER JOIN `configuracion` configuracion ON factura_detalle.`configuracion_nmanguera` = configuracion.`nmanguera`\n"
                            + "where factura_idfactura=" + ri.getInt(1) + " group by factura_detalle.producto_idproducto");





                    System.out.println(ndetalle);

                    //  idcliente = ri.getString(1);
                    //System.out.println(idcliente);
                    if (ri.getString(8).equalsIgnoreCase("contado")) {


                        mp = "EF";



                    } else if (ri.getString(8).equalsIgnoreCase("TC")) {

                        mp = "TC";


                    } else if (ri.getString(8).equalsIgnoreCase("Credito")) {

                        mp = "CR";


                    }






                    System.out.println(ri.getString(16));


                    ResultSet ris = cc.consulta("select RUC from ESCliente where RUC='" + ri.getString(16).trim() + "'");



                    if (ris.first()) {

                        System.out.println("Cliente si existe");

                        Date fecha = ri.getDate(4);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");

                        String fechaConFormato = sdf.format(fecha);

                        System.out.println(ndetalle);


                        if (ndetalle == 1) {

                            String detalle = "";

                            while (ri_fd.next()) {
                                //System.out.println("INSERT INTO ESFCElectronica (Fecha,Hora,Establecimiento,PtoEmision,Secuencial,CodCliente,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado) VALUES ( CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ri.getString(16) + "','" + ri.getString(14) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri_fd.getDouble(3) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','grabado');");
                                PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,Subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");

                                System.out.println("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,Subtotal,iva) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1).trim() + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ");");
                                ic.execute();
                                PreparedStatement estadoF = cf.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1 WHERE idfactura='" + ri.getInt(1) + "'");
                                estadoF.execute();



                            }
                        } else {

                            String detalle = "";
                            while (ri_fd.next()) {



                                detalle = detalle + ri_fd.getString(6) + "-" + ri_fd.getDouble(1) + "-" + ri_fd.getDouble(2) + ";";




                            }


                            detalle = detalle.substring(0, detalle.length() - 1);

                            if (ri_fd.first()) {


                                //  System.out.println("INSERT INTO ESFCElectronica (Fecha,Hora,Establecimiento,PtoEmision,Secuencial,CodCliente,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle) VALUES ( CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ri.getString(16) + "','" + ri.getString(14) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,' '," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri_fd.getDouble(3) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','grabado');");
                                PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,''," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','" + detalle + "',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");

                                System.out.println("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1).trim() + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,''," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','" + detalle + "'," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");


                                ic.execute();
                                PreparedStatement estadoF = cf.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1 WHERE idfactura='" + ri.getInt(1) + "'");
                                estadoF.execute();

                            }


                        }






                    } else {


                        System.out.println("cliente no existe ");



                    }







                }



            }


        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }



















    }

    public void modificar() {
        
        int validar=0;
        try {
            ResultSet ris = cc.consulta("select idcliente,CodCliente,TipoDoc from ESCliente");
            



            while (ris.next()) {
            
                
                //System.out.println(ris.getString(2));
                
                if(ris.getString(3).equalsIgnoreCase("C")){
                
                    int v=validarcedula(ris.getString(2));
                    
                    if(v==1){
                    
                    }else{
                    
                         System.out.println("Cedula Incorrecta");
                         PreparedStatement ic = cc.coneccion.prepareStatement("UPDATE ESCliente set TipoDoc='P' where idcliente="+ris.getInt(1)+"");
                         ic.execute();
                    
                    }
                    
                    
                    
                
                }else if(ris.getString(3).equalsIgnoreCase("R")){
                
                
                        validar = validarrucnatural(ris.getString(2));

                        if (validar == 1) {
                             } else {

                            validar = validarrucjuridica(ris.getString(2));

                            if (validar == 1) {

                             } else {

                                validar = validarrucpublicos(ris.getString(2));

                                if (validar == 1) {

                                 


                                } else {

                                     System.out.println("RUC IncorrectO");
                                     PreparedStatement ic = cc.coneccion.prepareStatement("UPDATE ESCliente set TipoDoc='P' where idcliente="+ris.getInt(1)+"");
                                     ic.execute();


                                }



                            }

                        }
                
                
                
                
                }
            
                
                
            
            }
            
            
            ris.close();
        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }





    }

    
  public int validarcedula(String cedula) {

        int cedulaCorrecta = 0;

        try {

            if (cedula.length() == 10) // ConstantesApp.LongitudCedula
            {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
// Coeficientes de validación cédula
// El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = {2, 1, 2, 1, 2, 1, 2, 1, 2};
                    int verificador = Integer.parseInt(cedula.substring(9, 10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1)) * coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = 1;
                    } else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = 1;
                    } else {
                        cedulaCorrecta = 0;
                    }
                } else {
                    cedulaCorrecta = 0;
                }
            } else {
                cedulaCorrecta = 0;
            }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = 0;
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            cedulaCorrecta = 0;
        }

        if (cedulaCorrecta == 0) {
            System.out.println("La Cédula ingresada es Incorrecta");
        }
        return cedulaCorrecta;





    }

    public int validarrucpublicos(String ruc) {

       


        final int prov = Integer.parseInt(ruc.substring(0, 2));
        int resp = 0;

         try{
        
        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            resp = 0;
        }

        // boolean val = false;
        Integer v1, v2, v3, v4, v5, v6, v7, v8, v9;
        Integer sumatoria;
        Integer modulo;
        Integer digito;
        int[] d = new int[ruc.length()];

        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(ruc.charAt(i) + "");
        }

        v1 = d[0] * 3;
        v2 = d[1] * 2;
        v3 = d[2] * 7;
        v4 = d[3] * 6;
        v5 = d[4] * 5;
        v6 = d[5] * 4;
        v7 = d[6] * 3;
        v8 = d[7] * 2;
        v9 = d[8];

        sumatoria = v1 + v2 + v3 + v4 + v5 + v6 + v7 + v8;
        modulo = sumatoria % 11;
        if (modulo == 0) {
            return 1;
        }
        digito = 11 - modulo;

        if (digito.equals(v9)) {
            resp = 1;
        } else {
            resp = 0;
        }
     
        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            resp = 0;
        }
        
        return resp;


    }

    public int validarrucjuridica(String ruc) {
        int resp_dato = 0;
        
        try{
        final int prov = Integer.parseInt(ruc.substring(0, 2));
        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            resp_dato = 0;
        }

        int[] d = new int[10];
        int suma = 0;

        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(ruc.charAt(i) + "");
        }

        for (int i = 0; i < d.length - 1; i++) {
            d[i] = d[i] * coeficientes[i];
            suma += d[i];
        }

        int aux, resp;

        aux = suma % constante;
        resp = constante - aux;

        resp = (aux == 0) ? 0 : resp;

        if (resp == d[9]) {
            resp_dato = 1;
        } else {
            resp_dato = 0;
        }
        
         } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            resp_dato = 0;
        }
        
        return resp_dato;


        
    }

    public int validarrucnatural(String cedula) {

        int isValid = 0;
        try{
        if (cedula == null || cedula.length() != 10) {
            isValid = 0;
        }
        final int prov = Integer.parseInt(cedula.substring(0, 2));

        if (!((prov > 0) && (prov <= NUM_PROVINCIAS))) {
            isValid = 0;
        }

        int[] d = new int[10];
        for (int i = 0; i < d.length; i++) {
            d[i] = Integer.parseInt(cedula.charAt(i) + "");
        }

        int imp = 0;
        int par = 0;

        for (int i = 0; i < d.length; i += 2) {
            d[i] = ((d[i] * 2) > 9) ? ((d[i] * 2) - 9) : (d[i] * 2);
            imp += d[i];
        }

        for (int i = 1; i < (d.length - 1); i += 2) {
            par += d[i];
        }

        final int suma = imp + par;

        int d10 = Integer.parseInt(String.valueOf(suma + 10).substring(0, 1)
                + "0")
                - suma;

        d10 = (d10 == 10) ? 0 : d10;

        if (d10 == d[9]) {
            isValid = 1;
        } else {
            isValid = 0;
        }

        } catch (Exception err) {
            System.out.println("Una excepcion ocurrio en el proceso de validadcion");
            isValid = 0;
        }



        return isValid;









    }
    
    
    
    
    
    
    
    
    public void enviar_facturas() {
        String mp = "";

        try {




            Statement st_in = cf.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT\n"
                    + "     factura.idfactura,\n"
                    + "	    factura.`Estado_factura` AS factura_Estado_factura,\n"
                    + "     SUBSTRING(factura.`numero`,9) AS factura_numero,\n"
                    + "     factura.`fecha` AS factura_fecha,\n"
                    + "     factura.`hora` AS factura_hora,\n"
                    + "     datos_gasolinera.`secuencia1_factura` AS datos_gasolinera_ruc,\n"
                    + "     punto_emision.`s2` AS punto_emision_s2,\n"
                    + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                    + "     factura.`usuarios_idusuarios` AS factura_usuarios_idusuarios,\n"
                    + "     factura.`numero_autorizacion` AS factura_numero_autorizacion,\n"
                    + "     factura.`subtotal` AS factura_subtotal,\n"
                    + "     factura.`total` AS factura_total,\n"
                    + "     factura.`iva` AS factura_iva,\n"
                    + "     datos_gasolinera.`ruc` AS datos_gasolinera_ruc,\n"
                    + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                    + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                    + "     usuarios.`usuario` AS usuarios_usuario,\n"
                    + "     cliente.`idcliente` AS cliente_id\n"
                    + "FROM\n"
                    + "     `punto_emision` punto_emision INNER JOIN `factura` factura ON punto_emision.`idpunto_emision` = factura.`punto_emision_idpunto_emision`\n"
                    + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                    + "     INNER JOIN `datos_gasolinera` datos_gasolinera ON punto_emision.`datos_gasolinera_iddatos_gasolinera` = datos_gasolinera.`iddatos_gasolinera`\n"
                    + "     INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                    + "     INNER JOIN `cliente` cliente ON factura.`cliente_idcliente` = cliente.`idcliente`\n"
                    + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                    + "where  punto_emision_idpunto_emision=idpunto_emision and enviado_contable=0 and (factura.Estado_factura='AUTORIZADO' or factura.Estado_factura='ANULADO') and numero_autorizacion > 0");


            System.out.println("SELECT\n"
                    + "     factura.idfactura,\n"
                    + "	    factura.`Estado_factura` AS factura_Estado_factura,\n"
                    + "     SUBSTRING(factura.`numero`,9) AS factura_numero,\n"
                    + "     factura.`fecha` AS factura_fecha,\n"
                    + "     factura.`hora` AS factura_hora,\n"
                    + "     datos_gasolinera.`secuencia1_factura` AS datos_gasolinera_ruc,\n"
                    + "     punto_emision.`s2` AS punto_emision_s2,\n"
                    + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                    + "     factura.`usuarios_idusuarios` AS factura_usuarios_idusuarios,\n"
                    + "     factura.`numero_autorizacion` AS factura_numero_autorizacion,\n"
                    + "     factura.`subtotal` AS factura_subtotal,\n"
                    + "     factura.`total` AS factura_total,\n"
                    + "     factura.`iva` AS factura_iva,\n"
                    + "     datos_gasolinera.`ruc` AS datos_gasolinera_ruc,\n"
                    + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                    + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                    + "     usuarios.`usuario` AS usuarios_usuario,\n"
                    + "     cliente.`idcliente` AS cliente_id\n"
                    + "FROM\n"
                    + "     `punto_emision` punto_emision INNER JOIN `factura` factura ON punto_emision.`idpunto_emision` = factura.`punto_emision_idpunto_emision`\n"
                    + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                    + "     INNER JOIN `datos_gasolinera` datos_gasolinera ON punto_emision.`datos_gasolinera_iddatos_gasolinera` = datos_gasolinera.`iddatos_gasolinera`\n"
                    + "     INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                    + "     INNER JOIN `cliente` cliente ON factura.`cliente_idcliente` = cliente.`idcliente`\n"
                    + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                    + "where  punto_emision_idpunto_emision=idpunto_emision and enviado_contable=0 and (factura.Estado_factura='AUTORIZADO' or factura.Estado_factura='ANULADO') and numero_autorizacion > 0");


            String idcliente;


            while (ri.next()) {
                int ndetalle = 0;


                System.out.println("entro " + ri.getInt(1));
                Statement st_fdc = cf.coneccion.createStatement();
                ResultSet ri_fdc = st_fdc.executeQuery("SELECT * FROM adv_facturacion.factura_detalle where factura_idfactura=" + ri.getInt(1) + " group by factura_detalle.producto_idproducto ;");
                while (ri_fdc.next()) {
                    ndetalle++;


                }


                Statement st_fd = cf.coneccion.createStatement();

                ResultSet ri_fd = st_fd.executeQuery("SELECT\n"
                        + "     sum(factura_detalle.`cantidad`) AS factura_detalle_cantidad,\n"
                        + "     sum(factura_detalle.`subtotal`) AS factura_detalle_subtotal,\n"
                        + "     sum(factura_detalle.`total`) AS factura_detalle_total,\n"
                        + "     factura_detalle.`configuracion_nmanguera` AS factura_detalle_configuracion_nmanguera,\n"
                        + "     sum(factura_detalle.`iva`) AS factura_detalle_iva,\n"
                        + "     producto.`nombre` AS producto_nombre,\n"
                        + "     configuracion.`surtidor` AS configuracion_surtidor,\n"
                        + "     configuracion.`tanques_idtanques` AS configuracion_tanques_idtanques,\n"
                        + "     producto.`punit` AS producto_punit\n"
                        + "FROM\n"
                        + "     `producto` producto INNER JOIN `factura_detalle` factura_detalle ON producto.`idproducto` = factura_detalle.`producto_idproducto`\n"
                        + "     INNER JOIN `configuracion` configuracion ON factura_detalle.`configuracion_nmanguera` = configuracion.`nmanguera`\n"
                        + "where factura_idfactura=" + ri.getInt(1) + " group by factura_detalle.producto_idproducto");





                System.out.println(ndetalle);

                //  idcliente = ri.getString(1);
                //System.out.println(idcliente);
                if (ri.getString(8).equalsIgnoreCase("contado")) {


                    mp = "EF";



                } else if (ri.getString(8).equalsIgnoreCase("TC")) {

                    mp = "TC";


                } else if (ri.getString(8).equalsIgnoreCase("Credito")) {

                    mp = "CR";


                }






                System.out.println(ri.getString(16));


                ResultSet ris = cc.consulta("select RUC from ESCliente where RUC='" + ri.getString(16).trim() + "'");



                if (ris.first()) {

                    System.out.println("Cliente si existe");

                    Date fecha = ri.getDate(4);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");

                    String fechaConFormato = sdf.format(fecha);

                    System.out.println("numero "+ndetalle);


                    if (ndetalle <= 1) {

                        String detalle = "";

                        while (ri_fd.next()) {
                            //System.out.println("INSERT INTO ESFCElectronica (Fecha,Hora,Establecimiento,PtoEmision,Secuencial,CodCliente,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado) VALUES ( CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ri.getString(16) + "','" + ri.getString(14) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri_fd.getDouble(3) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','grabado');");
                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,Subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");

                            System.out.println("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,Subtotal,iva) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1).trim() + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,'" + ri_fd.getString(6) + "'," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ");");
                            ic.execute();
                            PreparedStatement estadoF = cf.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1 WHERE idfactura='" + ri.getInt(1) + "'");
                            estadoF.execute();



                        }
                    } else {

                        String detalle = "";
                        while (ri_fd.next()) {



                            detalle = detalle + ri_fd.getString(6) + "-" + ri_fd.getDouble(1) + "-" + ri_fd.getDouble(2) + ";";




                        }


                        detalle = detalle.substring(0, detalle.length() - 1);

                        if (ri_fd.first()) {


                            //  System.out.println("INSERT INTO ESFCElectronica (Fecha,Hora,Establecimiento,PtoEmision,Secuencial,CodCliente,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle) VALUES ( CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ri.getString(16) + "','" + ri.getString(14) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,' '," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri_fd.getDouble(3) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','grabado');");
                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1) + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,''," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','" + detalle + "',''," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");

                            System.out.println("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,IVDetalle,SerieFuente,subtotal,iva,placacred) VALUES ('',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri.getString(5) + "','" + ri.getString(6) + "','" + ri.getString(7) + "'," + ri.getString(3) + ",'" + ris.getString(1).trim() + "','" + ri.getString(17) + "'," + ri_fd.getInt(4) + "," + ri_fd.getInt(7) + "," + ri_fd.getInt(8) + ",1,''," + ri_fd.getDouble(1) + "," + ri_fd.getDouble(9) + "," + ri.getDouble(12) + ",'" + mp + "','" + ri.getString(15) + "','" + ri.getString(10) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','','" + detalle + "'," + ri.getDouble(11) + "," + ri.getDouble(13) + ",'');");


                            ic.execute();
                            PreparedStatement estadoF = cf.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1 WHERE idfactura='" + ri.getInt(1) + "'");
                            estadoF.execute();

                        }


                    }






                } else {


                    System.out.println("cliente no existe ");



                }







            }






        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void crear_usuarios() {
        try {

            Statement st_fd = cf.coneccion.createStatement();
            ResultSet ri_fd = st_fd.executeQuery("SELECT usuario,nombre FROM adv_facturacion.usuarios;");

            while (ri_fd.next()) {


                Statement st_r = cc.coneccion.createStatement();
                ResultSet ris = st_r.executeQuery("Select * from ESVendedor where CodVendedor='" + ri_fd.getString(1) + "'");
                System.out.println("Select * from ESVendedor where CodVendedor='" + ri_fd.getString(1) + "'");


                if (ris.next()) {


                    System.out.println("");




                } else {


                    PreparedStatement estadoF = cc.coneccion.prepareStatement("INSERT INTO ESVendedor(CodVendedor,Nombre) VALUES('" + ri_fd.getString(1) + "','" + ri_fd.getString(2) + "');");
                    estadoF.execute();

                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void enviar_notas() {


        String mp = null;

        System.out.println("Enviando Notas");

        try {
            Statement st_fd = cf.coneccion.createStatement();

            ResultSet ri_fd = st_fd.executeQuery("SELECT\n"
                    + "SUBSTRING(numero,1,3),"
                    + "SUBSTRING(numero,5,3),"
                    + "SUBSTRING(numero,9),"
                    + "nota_credito.`factura_idfactura` AS nota_credito_factura_idfactura,\n"
                    + "     usuarios.`usuario` AS usuarios_usuario,\n"
                    + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                    + "     nota_credito.`autorizacion` AS nota_credito_autorizacion\n"
                    + "FROM\n"
                    + "     `usuarios` usuarios INNER JOIN `nota_credito` nota_credito ON usuarios.`idusuarios` = nota_credito.`usuarios_idusuarios`\n"
                    + "     INNER JOIN `clave_acceso` clave_acceso ON nota_credito.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                    + "where   enviado_contable=0 and nota_credito.estado='AUTORIZADO' and autorizacion > 0");

            while (ri_fd.next()) {

                System.out.println(ri_fd.getString(4));

                Statement st_f = cf.coneccion.createStatement();

                ResultSet ri_f = st_f.executeQuery("SELECT\n"
                        + "     factura.idfactura,\n"
                        + "	factura.`Estado_factura` AS factura_Estado_factura,\n"
                        + "     SUBSTRING(factura.`numero`,9) AS factura_numero,\n"
                        + "     SUBSTRING(factura.`numero`,1,7) AS factura_numero,\n"
                        + "     factura.`fecha` AS factura_fecha,\n"
                        + "     factura.`hora` AS factura_hora,\n"
                        + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                        + "     factura.`usuarios_idusuarios` AS factura_usuarios_idusuarios,\n"
                        + "     factura.`numero_autorizacion` AS factura_numero_autorizacion,\n"
                        + "     factura.`subtotal` AS factura_subtotal,\n"
                        + "     factura.`total` AS factura_total,\n"
                        + "     factura.`iva` AS factura_iva,\n"
                        + "     datos_gasolinera.`ruc` AS datos_gasolinera_ruc,\n"
                        + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                        + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                        + "     usuarios.`usuario` AS usuarios_usuario,\n"
                        + "     cliente.`idcliente` AS cliente_id\n"
                        + "FROM\n"
                        + "     `punto_emision` punto_emision INNER JOIN `factura` factura ON punto_emision.`idpunto_emision` = factura.`punto_emision_idpunto_emision`\n"
                        + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                        + "     INNER JOIN `datos_gasolinera` datos_gasolinera ON punto_emision.`datos_gasolinera_iddatos_gasolinera` = datos_gasolinera.`iddatos_gasolinera`\n"
                        + "     INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                        + "     INNER JOIN `cliente` cliente ON factura.`cliente_idcliente` = cliente.`idcliente`\n"
                        + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                        + "where  factura.idfactura=" + ri_fd.getInt(4) + " ");
                while (ri_f.next()) {


                    System.out.println(ri_f.getString(2));
                    Statement st_fde = cf.coneccion.createStatement();

                    ResultSet ri_fde = st_fde.executeQuery("SELECT\n"
                            + "     factura_detalle.`cantidad` AS factura_detalle_cantidad,\n"
                            + "     factura_detalle.`subtotal` AS factura_detalle_subtotal,\n"
                            + "     factura_detalle.`total` AS factura_detalle_total,\n"
                            + "     factura_detalle.`configuracion_nmanguera` AS factura_detalle_configuracion_nmanguera,\n"
                            + "     factura_detalle.`iva` AS factura_detalle_iva,\n"
                            + "     producto.`nombre` AS producto_nombre,\n"
                            + "     configuracion.`surtidor` AS configuracion_surtidor,\n"
                            + "     configuracion.`tanques_idtanques` AS configuracion_tanques_idtanques,\n"
                            + "     producto.`punit` AS producto_punit\n"
                            + "FROM\n"
                            + "     `producto` producto INNER JOIN `factura_detalle` factura_detalle ON producto.`idproducto` = factura_detalle.`producto_idproducto`\n"
                            + "     INNER JOIN `configuracion` configuracion ON factura_detalle.`configuracion_nmanguera` = configuracion.`nmanguera`\n"
                            + "where factura_idfactura=" + ri_fd.getInt(4) + "");


                    Date fecha = ri_f.getDate(5);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM");

                    String fechaConFormato = sdf.format(fecha);





                    if (ri_f.getString(7).equalsIgnoreCase("contado")) {


                        mp = "EF";



                    } else if (ri_f.getString(7).equalsIgnoreCase("TC")) {

                        mp = "TC";


                    } else if (ri_f.getString(7).equalsIgnoreCase("Credito")) {

                        mp = "CR";


                    }

                    ResultSet ris = cc.consulta("select RUC from ESCliente where RUC='" + ri_f.getString(15) + "'");

                    System.out.println("select CodCliente from ESCliente where CodCliente='" + ri_f.getString(15) + "'");

                    if (ris.first()) {

                        System.out.println("cliente existe");

                        if (ri_fde.first()) {

                            System.out.println(ri_fde.getString(2));
                            //                                            System.out.println("INSERT INTO ESFCElectronica (Fecha,Hora,Establecimiento,PtoEmision,Secuencial,CodCliente,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,SerieFuente,SecuencialFuente) VALUES ( CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri_f.getString(6) + "','" + ri_fd.getString(1) + "','" + ri_fd.getString(2) + "'," + ri_fd.getString(3) + ",'" + ri_f.getString(15) + "','" + ri_f.getString(13) + "','" + ri_f.getString(16) + "'," + ri_fde.getInt(4) + "," + ri_fde.getInt(7) + "," + ri_fde.getInt(8) + ",1,'"+ri_fde.getString(6)+"'," + ri_fde.getDouble(1) + "," + ri_fde.getDouble(9) + "," + ri_fde.getDouble(3) + ",'" + mp + "','" + ri_fd.getString(6) + "','" + ri_fd.getString(7) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0','grabado','" + ri_f.getString(4) + "'," + ri_f.getString(3) + ");");
                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,SerieFuente,SecuencialFuente,IVDetalle,subtotal,iva) VALUES (' ',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri_f.getString(6) + "','" + ri_fd.getString(1) + "','" + ri_fd.getString(2) + "'," + ri_fd.getString(3) + ",'" + ris.getString(1) + "','" + ri_f.getString(16) + "'," + ri_fde.getInt(4) + "," + ri_fde.getInt(7) + "," + ri_fde.getInt(8) + ",1,'" + ri_fde.getString(6) + "'," + ri_fde.getDouble(1) + "," + ri_fde.getDouble(9) + "," + ri_fde.getDouble(3) + ",'" + mp + "','" + ri_fd.getString(6) + "','" + ri_fd.getString(7) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0',' ','" + ri_f.getString(4).replace("-", "") + "'," + ri_f.getString(3) + ",' ',"+ri_fde.getDouble(2)+","+ri_fde.getDouble(5)+");");

                            System.out.println("nota INSERT INTO ESFCElectronica (CodCliente,Fecha,Hora,Establecimiento,PtoEmision,Secuencial,RUC,CodUsuario,Manguera,Dispensador,Tanque,Turno,Producto,Galones,Precio_de_Galon,Total,FormaCobro,ClaveAccesoSRI,AutorizacionSRI,FechaAutorizacionSRI,Estado,NumDocCobro,MsgResultado,SerieFuente,SecuencialFuente,IVDetalle) VALUES (' ',CAST ( '" + fechaConFormato + "' AS DATETIME ),'" + ri_f.getString(6) + "','" + ri_fd.getString(1) + "','" + ri_fd.getString(2) + "'," + ri_fd.getString(3) + ",'" + ris.getString(1) + "','" + ri_f.getString(16) + "'," + ri_fde.getInt(4) + "," + ri_fde.getInt(7) + "," + ri_fde.getInt(8) + ",1,'" + ri_fde.getString(6) + "'," + ri_fde.getDouble(1) + "," + ri_fde.getDouble(9) + "," + ri_fde.getDouble(3) + ",'" + mp + "','" + ri_fd.getString(6) + "','" + ri_fd.getString(7) + "',CAST ( '" + fechaConFormato + "' AS DATETIME ),0,'0',' ','" + ri_f.getString(4).replace("-", "") + "'," + ri_f.getString(3) + "),' ';");
                            ic.execute();
                            PreparedStatement estadoF = cf.coneccion.prepareStatement("UPDATE `adv_facturacion`.`nota_credito` SET enviado_contable=1 WHERE factura_idfactura='" + ri_fd.getInt(4) + "'");
                            estadoF.execute();


                        }








                    } else {




                        String codigo = "";
                        Statement st_c = cf.coneccion.createStatement();
                        ResultSet rc = st_c.executeQuery("SELECT nombre,cedula_ruc,direccion,telefono,email,tipo_identificacion,(select codigo from codigos where cliente_idcliente=idcliente and  idcliente ='" + ri_f.getInt(17) + "') FROM adv_facturacion.cliente,adv_facturacion.codigos  WHERE  cedula_ruc ='" + ri_f.getString(15) + "' group by cedula_ruc");

                        while (rc.next()) {
                            System.out.println("Cliente no existe");

                            System.out.println(rc.getString(2));
                            System.out.println(rc.getString(6).toUpperCase().substring(0, 1));
                            System.out.println(ri_f.getString(5));
                            System.out.println(rc.getString(1));
                            System.out.println(rc.getString(3));
                            System.out.println(rc.getString(4));
                            System.out.println(rc.getString(5));
                            System.out.println(rc.getString(7));

                            if (rc.getString(7) == null) {

                                codigo = "";

                            }

                            System.out.println("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + rc.getString(2) + "','" + rc.getString(1) + "','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','" + codigo + "','" + codigo + "');");

                            codigo = rc.getString(7);

                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + rc.getString(13) + "','" + rc.getString(1) + "','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','" + codigo + "','" + codigo + "');");


                            ic.execute();

                            // enviar_notas();





                        }









                    }





                }

            }




            cf.coneccion.close();
            cc.coneccion.close();

        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    public void crear_clientes() {
        try {

            System.out.println("creando clientes");

            Statement st_in = cf.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT\n"
                    + "     factura.idfactura,\n"
                    + "	    factura.`Estado_factura` AS factura_Estado_factura,\n"
                    + "     SUBSTRING(factura.`numero`,9) AS factura_numero,\n"
                    + "     factura.`fecha` AS factura_fecha,\n"
                    + "     factura.`hora` AS factura_hora,\n"
                    + "     datos_gasolinera.`secuencia1_factura` AS datos_gasolinera_ruc,\n"
                    + "     punto_emision.`s2` AS punto_emision_s2,\n"
                    + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                    + "     factura.`usuarios_idusuarios` AS factura_usuarios_idusuarios,\n"
                    + "     factura.`numero_autorizacion` AS factura_numero_autorizacion,\n"
                    + "     factura.`subtotal` AS factura_subtotal,\n"
                    + "     factura.`total` AS factura_total,\n"
                    + "     factura.`iva` AS factura_iva,\n"
                    + "     datos_gasolinera.`ruc` AS datos_gasolinera_ruc,\n"
                    + "     clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,\n"
                    + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                    + "     usuarios.`usuario` AS usuarios_usuario,\n"
                    + "     cliente.`idcliente` AS cliente_id\n"
                    + "FROM\n"
                    + "     `punto_emision` punto_emision INNER JOIN `factura` factura ON punto_emision.`idpunto_emision` = factura.`punto_emision_idpunto_emision`\n"
                    + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                    + "     INNER JOIN `datos_gasolinera` datos_gasolinera ON punto_emision.`datos_gasolinera_iddatos_gasolinera` = datos_gasolinera.`iddatos_gasolinera`\n"
                    + "     INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso`\n"
                    + "     INNER JOIN `cliente` cliente ON factura.`cliente_idcliente` = cliente.`idcliente`\n"
                    + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                    + "where  punto_emision_idpunto_emision=idpunto_emision and enviado_contable=0 and (factura.Estado_factura='AUTORIZADO' or factura.Estado_factura='ANULADO') and numero_autorizacion > 0");
            while (ri.next()) {



                ResultSet ris = cc.consulta("select RUC from ESCliente where RUC='" + ri.getString(16).trim() + "'");

                if (ris.first()) {


                    System.out.println("existe" + ris.getString(1));


                } else {




                    String codigo = "", codigoC;



                    String nombre = "";
                    Statement st_c = cf.coneccion.createStatement();
                    ResultSet rc = st_c.executeQuery("SELECT nombre,cedula_ruc,direccion,telefono,email,tipo_identificacion,(select codigo from codigos where cliente_idcliente=idcliente and  cedula_ruc ='" + ri.getString(16) + "' limit 1) FROM adv_facturacion.cliente,adv_facturacion.codigos  WHERE  cedula_ruc ='" + ri.getString(16) + "' group by cedula_ruc");

                    //System.out.println("SELECT nombre,cedula_ruc,direccion,telefono,email,tipo_identificacion,(select codigo from codigos where cliente_idcliente=idcliente and  cedula_ruc ='" + ri.getString(16) + "' limit 1) FROM adv_facturacion.cliente,adv_facturacion.codigos  WHERE  cedula_ruc ='" + ri.getString(16) + "' group by cedula_ruc");

                    while (rc.next()) {

                        System.out.println("Cliente no existe");

                        System.out.println(rc.getString(2));

                        System.out.println(rc.getString(2));
                        System.out.println(rc.getString(6).toUpperCase().substring(0, 1));
                        System.out.println(ri.getString(6));
                        System.out.println(rc.getString(1));
                        System.out.println(rc.getString(3));
                        System.out.println(rc.getString(4));
                        System.out.println(rc.getString(5));
                        System.out.println(rc.getString(7));
                        codigo = rc.getString(2);
                        if (codigo == null || codigo.length() == 0) {

                            codigo = rc.getString(2);
                            codigoC = "";

                        }
                        /*else{
                    
                    
                         codigo=rc.getString(rc.getString(7));
                        
                    
                         }*/

                        nombre = rc.getString(1);

                        if (rc.getString(6).toUpperCase().substring(0, 1).equalsIgnoreCase("C")) {

                            nombre = rc.getString(1) + "...";


                        }



                        System.out.println("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + ri.getString(14) + "','" + nombre + "','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','','');");


                        try {
                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + rc.getString(2) + "','" + nombre + "','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','','');");


                            ic.execute();
                        } catch (com.microsoft.sqlserver.jdbc.SQLServerException ex) {


                            System.out.println(ex.getMessage());

                            PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + rc.getString(2) + "','" + nombre + "....','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','','');");


                            ic.execute();
                            System.out.println("Nombre duplicado");

                            // PreparedStatement ic = cc.coneccion.prepareStatement("INSERT INTO ESCliente (CodCliente,TipoDoc,Ruc,Nombre,Direccion,Telefono,Email,CodigoAdhesivo,CodigoTag)VALUES ('" + rc.getString(2) + "','" + rc.getString(6).toUpperCase().substring(0, 1) + "','" + rc.getString(2).trim() + "','" + nombre + "....','" + rc.getString(3) + "','" + rc.getString(4) + "','" + rc.getString(5) + "','" + codigo + "','" + codigo + "');");
                            // ic.execute();




                        }



                    }



                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(enviar_facturas_sofi.class.getName()).log(Level.SEVERE, null, ex);
        }




    }
}
