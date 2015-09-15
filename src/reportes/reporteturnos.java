/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import conexion.conexion_facturacion;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author r
 */
public class reporteturnos {

    public String usu,contra;
    static Connection conn = null;

    public reporteturnos(String usu,String contra,String man, String hi,String hf,String fi,String ff) throws SQLException, ClassNotFoundException {
        {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("MySQL JDBC Driver not found.");
                System.exit(1);
            }
            //Para iniciar el Logger.
            //inicializaLogger();



            conexion_facturacion con= new conexion_facturacion(usu,contra);
            con.conectar();
            con.coneccion.setAutoCommit(false);
            //conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/adv_facturacion", usu, contra);
            //conn.setAutoCommit(false);




            

            try {
                
                InputStream icono = new FileInputStream("plantilla-para-programa02.jpg");
                
             
                Map parameters = new HashMap();
                parameters.put("mangueras", man);
               parameters.put("fondo",icono);
                parameters.put("hi", hi);
                parameters.put("hf", hf);
                parameters.put("fi", fi);
                parameters.put("fef", ff);
                JasperReport report = JasperCompileManager.compileReport("ReporteVentas.jrxml");
                JasperPrint print = JasperFillManager.fillReport(report, parameters, con.coneccion);
                // Exporta el informe a PDF
                JasperExportManager.exportReportToPdfFile(print, "ReporteVentas.pdf");
                //Para visualizar el pdf directamente desde java
                JasperViewer.viewReport(print, false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                /*
                 * Cleanup antes de salir
                 */
                try {
                    if (con.coneccion != null) {
                        con.coneccion.rollback();
                        System.out.println("ROLLBACK EJECUTADO");
                        con.coneccion.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


public void usuarios(String usuario,String contraseña){

contra=contraseña;
usu=usuario;
    
}



}
