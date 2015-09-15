/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_adv_matriz;
import conexion.conexion_facturacion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class enviar_web {

    String usuario, contraseña;
    conexion_adv_matriz c1;
    conexion_facturacion c2;

    public enviar_web(String usuario, String contraseña) {

        this.usuario = usuario;
        this.contraseña = contraseña;


       
       
        c1 = new conexion_adv_matriz(usuario, contraseña);
        c2 = new conexion_facturacion(usuario, contraseña);
     

        
    }
    

    public void conectar() {
        try {

            int idcliente;
            String numero, fecha, hora, cedula_ruc, email;

             System.out.println("entro");


            File pdf = new File("pdf.pdf");

            
            
            
            
            c1.conectar();
            c2.conectar();

            Statement st_s3 = c2.coneccion.createStatement();


            ResultSet rids3 = st_s3.executeQuery("SELECT numero,fecha,hora,nombre,cedula_ruc,email,doc_xml,razon_social,idfactura FROM adv_facturacion.factura,adv_facturacion.cliente,adv_xml.xml_enviados_autorizados,adv_facturacion.datos_gasolinera\n"
                    + "where cliente_idcliente=idcliente and idfactura=xml_factura and datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and Estado_factura='AUTORIZADO' and enviado_adv=0;");
            while (rids3.next()) {


                System.out.println(rids3.getString(1));
                
               

                //System.out.println(rids3.getString(1));

               

               

                
              





                InputStream inxml = rids3.getBlob(7).getBinaryStream();
                FileInputStream inpdf = null;
                
                try{
                pdf = new File("pdf\\" + rids3.getString(1) + ".pdf");
                inpdf = new FileInputStream(pdf);

                }catch (FileNotFoundException ex) {
                     //Logger.getLogger(enviar_web.class.getName()).log(Level.SEVERE, null, ex);
                       
                    System.out.println("no existe pdf");
                } 
                
                
                Statement st_cli = c1.coneccion.createStatement();
                ResultSet ridcli = st_cli.executeQuery("SELECT idclientes FROM facturacion_matriz.clientes where cedula_ruc='" + rids3.getString(5) + "';");

                System.out.println("Entro busqueda");
                
                if (ridcli.first()) {

                     System.out.println(ridcli.getString(1));
                    Statement st_es = c1.coneccion.createStatement();
                    ResultSet rides = st_es.executeQuery("SELECT idestacion_servicio FROM facturacion_matriz.estacion_servicio where razon_social='" + rids3.getString(8) + "';");




                    if (rides.first()) {


                        System.out.println("Cliente ya existe");
                        idcliente = ridcli.getInt(1);
                        

                        

                        PreparedStatement cli = c1.coneccion.prepareStatement("INSERT INTO `facturacion_matriz`.`documentos_factura` (`fecha`, `hora`, `numero_factura`,`doc_xml`,`doc_pdf`, `clientes_idclientes`, `estacion_servicio_idestacion_servicio`,`tipo_doc`) VALUES ('" + rids3.getString(2) + "', '" + rids3.getString(3) + "', '" + rids3.getString(1) + "',?,?,'" + idcliente + "', '" + rides.getInt(1) + "','factura');");

                        cli.setBinaryStream(1, inxml, inxml.available());
                        cli.setBinaryStream(2, inpdf, (int) pdf.length());

                        cli.executeUpdate();


                        PreparedStatement factura = c2.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET `enviado_adv`='1' WHERE `idfactura`='" + rids3.getInt(9) + "';");

                        factura.execute();

                        
                        pdf.delete();

                    }

                } else {

                    PreparedStatement cli = c1.coneccion.prepareStatement("INSERT INTO `facturacion_matriz`.`clientes` (`nombre`, `cedula_ruc`, `email`) VALUES ('" + rids3.getString(4) + "', '" + rids3.getString(5) + "', '" + rids3.getString(6) + "');");

                    cli.execute();
                    System.out.println("cliente no existe ");




                }


                inxml.close();
                if(inpdf==null){
                
                }else{
                inpdf.close();}
                




            }



            rids3.close();
            c2.coneccion.close();
            c1.coneccion.close();

            
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(enviar_web.class.getName()).log(Level.SEVERE, null, ex);
        }



    }

    public void crear_cliente(String email) {
    }
}
