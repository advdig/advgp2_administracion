/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Advantech Digital
 */
public class ArchivoClaves {
    String USUARIO = "";
    String PASSWORD = "";
       conexion_facturacion con;
    public ArchivoClaves(File claves,String usuario,String contraseña){
       
   
    
     USUARIO=usuario;
        PASSWORD=contraseña;
        
          con = new conexion_facturacion(USUARIO, PASSWORD);
        BufferedReader br = null;
        String line;
        try {
             con.conectar();
            br = new BufferedReader(new FileReader(claves));
            while ((line = br.readLine()) != null){
                setClaves(line, String.format("%tF", Calendar.getInstance()), 0, "contingencia");
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ArchivoClaves.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
    
    public void setClaves(String clave, String date, int estado, String tipo) {
        try {        
       
           
            //ResultSet rs = con.consulta("SELECT DATE_FORMAT(CURDATE(),'%d'\"-\"'%m'\"-\"'%Y');");
            //rs.next();
              Statement st1 = con.coneccion.createStatement();
            
                String consulta="INSERT INTO clave_acceso (clave_acceso, fecha, estado, tipo) VALUES('" + clave + "', '" + date + "', " + estado + ", '" + tipo + "');";
            
            
             st1.executeUpdate(consulta);
           
             st1.close();
            
        } catch (SQLException ex) {
           System.out.println(ex.getMessage());
        }
        
        
    }
}
