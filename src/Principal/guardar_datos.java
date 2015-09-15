/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author r
 */
public class guardar_datos {

    String fecha, hora, producto, usuario;
    double subtotal, total, iva;
    String cliente, tp;
    int idcliente, ns;
    String cantidad, estadox, motivox;
    File archivox;
    int manguera;
    public String usu, contra, numerof;
    int ifactu = 0;
    int temi;
    int idcla;
    int clavea = 0;
    conexion_facturacion conectar;

    public guardar_datos(String tp, int idc, int te, File archivo, String estado, String motivo, int nsurtidor, String numerofa, String f, String h, double sub, double tot, double iv, int man, int cli, String usu, String prod, String cantida) {


        this.tp = tp;
        idcla = idc;
        temi = te;
        estadox = estado;
        motivox = motivo;
        archivox = archivo;
        ns = nsurtidor;
        fecha = f;
        hora = h;
        subtotal = sub;
        total = tot;
        iva = iv;
        manguera = man;
        idcliente = cli;
        usuario = usu;
        producto = prod;
        cantidad = cantida;
        numerof = numerofa;


        //cantidad=Double.parseDouble(cantida);



    }

    public void grabar() {
        try {

            int idu = 0, idp = 0, idc, idf = 0;


            conectar.conectar();
            Statement st_d = conectar.coneccion.createStatement();
            ResultSet rid = st_d.executeQuery("SELECT idusuarios from usuarios where usuario='" + usuario + "'");




            Statement st_f = conectar.coneccion.createStatement();
            ResultSet rif = st_f.executeQuery("select curdate();");


            while (rif.next()) {

                fecha = rif.getString(1);
            }


            while (rid.next()) {

                idu = rid.getInt(1);
            }




            Statement st_p = conectar.coneccion.createStatement();
            ResultSet idpro = st_p.executeQuery("SELECT idproducto from producto where nombre='" + producto + "'");


            System.out.println("pro" + producto);

            while (idpro.next()) {

                idp = idpro.getInt(1);






            }







            PreparedStatement factura = conectar.coneccion.prepareStatement("INSERT INTO `adv_facturacion`.`factura` (`Estado_factura`,`numero`, `fecha`, `hora`,`metodo_pago`, `cliente_idcliente`, `usuarios_idusuarios`, `datos_gasolinera_iddatos_gasolinera`,`subtotal`,`total`,`iva`) VALUES ('','" + numerof + "', '" + fecha + "', '" + hora + "','" + tp + "', '" + idcliente + "', '" + idu + "','1','" + subtotal + "','" + total + "'.'" + iva + "');");

            factura.execute();




            Statement st_idfac = conectar.coneccion.createStatement();
            ResultSet idfac = st_idfac.executeQuery("SELECT idfactura  FROM adv_facturacion.factura where numero='" + numerof + "';");


            if (idfac.first()) {

                ifactu = idfac.getInt(1);
            }






            PreparedStatement factura_detalle = conectar.coneccion.prepareStatement("INSERT INTO `adv_facturacion`.`factura_detalle` (`cantidad`, `subtotal`, `total`,`iva` ,`factura_idfactura`, `configuracion_nmanguera`,`producto_idproducto`) VALUES ('" + cantidad + "', '" + subtotal + "', '" + total + "','" + iva + "', '" + ifactu + "','" + manguera + "'," + idp + ");");

            factura_detalle.execute();









            conectar.coneccion.close();
            System.out.println("factura grabada correctamente");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(guardar_datos.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    public void actualizar(String nAuto, String cadena, String nfactura, String idfact) throws SQLException, FileNotFoundException {
        try {


            conectar.conectar();


            if (cadena.length() == 0) {
                
                
            } else {

                if (temi == 1) {

                    Statement st_clave = conectar.coneccion.createStatement();
                    ResultSet idclave = st_clave.executeQuery("SELECT idclave_acceso from clave_acceso where clave_acceso='" + cadena + "'");





                    if (idclave.first()) {

                        clavea = idclave.getInt(1);

                    }






                } else {






                    clavea = idcla;
                }
            }


            Statement st_id = conectar.coneccion.createStatement();
            ResultSet id = st_id.executeQuery("SELECT idfactura from factura where numero='" + nfactura + "'");





            if (id.first()) {

                idfact = id.getString(1);



            }


            System.out.println("idfactura" + idfact);

            if(clavea==0){
            
             PreparedStatement cli = conectar.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET `Estado_factura`='" + estadox + "',`numero_autorizacion`='" + nAuto + "' WHERE `numero`='" + nfactura + "';");

            cli.execute();
            
            
            }else{
            PreparedStatement cli = conectar.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET `Estado_factura`='" + estadox + "',`clave_acceso_idclave_acceso`='" + clavea + "',`numero_autorizacion`='" + nAuto + "' WHERE `numero`='" + nfactura + "';");

            cli.execute();
            }


            if (estadox.equalsIgnoreCase("NO AUTORIZADO")) {

                FileInputStream in = new FileInputStream(archivox);
                long datos = archivox.lastModified();


                PreparedStatement myStatement = conectar.coneccion.prepareStatement("INSERT INTO adv_xml.xml_no_autorizados(doc_xml, xml_factura,motivo_no_autorizado)VALUES(?, '" + idfact + "','" + motivox + "')");

                myStatement.setBinaryStream(1, in, (int) archivox.length());
                myStatement.executeUpdate();

                in.close();



            }

            if (estadox.equalsIgnoreCase("AUTORIZADO")) {

                FileInputStream in = new FileInputStream(archivox);



                PreparedStatement myStatement = conectar.coneccion.prepareStatement("INSERT INTO adv_xml.xml_enviados_autorizados(doc_xml, xml_factura) VALUES(?, '" + idfact + "')");

                myStatement.setBinaryStream(1, in, (int) archivox.length());
                myStatement.executeUpdate();


                in.close();


            }
            if (estadox.equalsIgnoreCase("Contingencia")) {




                FileInputStream in = new FileInputStream(archivox);
                long datos = archivox.lastModified();


                PreparedStatement myStatement = conectar.coneccion.prepareStatement("INSERT INTO adv_xml.xml_contingencia(xml_contingencia, xml_factura,motivo)VALUES(?, '" + idfact + "','" + motivox + "')");

                myStatement.setBinaryStream(1, in, (int) archivox.length());
                myStatement.executeUpdate();


                in.close();




            }
            
            if (estadox.equalsIgnoreCase("Consumidor Final")) {
            
                FileInputStream in = new FileInputStream(archivox);
                long datos = archivox.lastModified();


                PreparedStatement myStatement = conectar.coneccion.prepareStatement("INSERT INTO adv_xml.consumidores_finales(xml,idfactura)VALUES(?, '" + idfact + "')");

                myStatement.setBinaryStream(1, in, (int) archivox.length());
                myStatement.executeUpdate();


                in.close();
            
            
            }
            
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(guardar_datos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(guardar_datos.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void usuarios(String usuario, String contraseña) {

        contra = contraseña;
        usu = usuario;
        conectar = new conexion_facturacion(usu, contra);
    }
}
