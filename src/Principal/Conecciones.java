package Principal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import org.hsqldb.util.DatabaseManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 package sistema;
 import java.sql.*;  
 import java.util.logging.Level;
 import java.util.logging.Logger;
 /**
 *
 * @author Jhon
 */
public class Conecciones {

    public String usuario;
    public String contraseña;
    private DataSource dataSource;
    String ip, ns;

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
    Connection coneccion = null;
    ResultSet rsDatos;
    Statement stSentencias = null;
    PreparedStatement psPrepararSentencias;

    public Conecciones(String usuario, String contraseña) {

        this.setContraseña(contraseña);
        this.setUsuario(usuario);
        servidores();
        
        
        
    }

    public void servidores() {

        try {
            java.io.BufferedReader buffer = new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea = "";


            int cont = 0;
            while ((linea = buffer.readLine()) != null) {

                //ip=linea.substring(1,2);

                if (cont == 0) {
                    ip = linea.substring(38, linea.length());

                }


                cont++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void conectar() throws ClassNotFoundException {
        try {
            ConectarMysql();

        } catch (SQLException ex) {

            Logger.getLogger(Conecciones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conecciones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (StackOverflowError ex2) {
            JOptionPane.showMessageDialog(null, "Servicio Posservice no responde reiniciar servicio ");
        }




    }

    public DataSource  ConectarMysql() throws SQLException, ClassNotFoundException {
        coneccion = null;
        stSentencias = null;
        usuario = this.getUsuario();
        contraseña = this.getContraseña();
        try {

        DriverManagerDataSource basicDataSource = new DriverManagerDataSource();

        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUsername(usuario);
        basicDataSource.setPassword(contraseña);
        basicDataSource.setUrl("jdbc:mysql://"+ip+"/adv_facturacion");

        // Opcional. Sentencia SQL que le puede servir a BasicDataSource
        // para comprobar que la conexion es correcta.
        //basicDataSource.setValidationQuery("select 1");

        dataSource = basicDataSource;
         System.out.print("Conexion a la base");
         
        

        } catch (ClassCastException ex) {
            throw ex;
            
        
        } catch (java.lang.StackOverflowError ex2) {
            JOptionPane.showMessageDialog(null, "Servicio Posservice no responde reiniciar servicio ");
        }
        return dataSource;



    }

    public ResultSet consulta(String sql) {

       ResultSet rs=null;
             Connection conexion = null;
        try {
            conexion = dataSource.getConnection();
            Statement sentencia = conexion.createStatement();
            rs = sentencia.executeQuery(sql);


        } catch (Exception e) {
        }

        return rs;


    }

    public void ejecutar(String sql) throws SQLException {

        try {
            stSentencias.execute(sql);
        } catch (SQLException ex) {

            throw ex;
        }





    }
    public ResultSet Buscar(String con) throws SQLException{
        ResultSet rs=null;
        Connection conexion=dataSource.getConnection();
        Statement stmt=(Statement) conexion.createStatement();
        rs=stmt.executeQuery(con);
        return rs;
    }
}
