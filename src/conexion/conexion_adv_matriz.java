package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 package sistema;
 import java.sql.*;  
 import java.util.logging.Level;
 import java.util.logging.Logger;
 /**
 *
 *
 */
public class conexion_adv_matriz {

    public String usuario;
    public String contraseña;
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
    public Connection coneccion = null;
    public ResultSet rsDatos;
    public Statement stSentencias = null;
    public PreparedStatement psPrepararSentencias;

    public conexion_adv_matriz(String usuario, String contraseña) {

        this.setContraseña(contraseña);
        this.setUsuario(usuario);
        servidores();
    }

    public void conectar() throws ClassNotFoundException {




        try {
            ConectarMysql();

        } catch (SQLException ex) {
            Logger.getLogger(conexion_adv_matriz.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(conexion_adv_matriz.class.getName()).log(Level.SEVERE, null, ex);
        }




    }

    public Connection ConectarMysql() throws SQLException, ClassNotFoundException {
        coneccion = null;
        stSentencias = null;

        usuario = this.getUsuario();
        contraseña = this.getContraseña();





        try {
            Class.forName("com.mysql.jdbc.Driver");

            String url = "jdbc:mysql://" + ip + ":3306/facturacion_matriz";

            coneccion = DriverManager.getConnection(url, usuario, contraseña);

            stSentencias = coneccion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);





        } catch (ClassCastException ex) {
            throw ex;
        } catch (SQLException ex1) {
            throw ex1;
        }
        return coneccion;



    }

    public ResultSet consulta(String sql) {

        try {
            rsDatos = stSentencias.executeQuery(sql);



        } catch (Exception e) {
        }

        return rsDatos;


    }

    public void ejecutar(String sql) throws SQLException {

        try {
            stSentencias.execute(sql);
        } catch (SQLException ex) {

            throw ex;
        }





    }

    public void servidores() {

        try {
            java.io.BufferedReader buffer = new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea = "";


            int cont = 0;
            while ((linea = buffer.readLine()) != null) {

                //ip=linea.substring(1,2);

                if(cont==3){
                    ip=linea.substring(17,linea.length());
                    
                }
                System.out.println(ip);


                cont++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
