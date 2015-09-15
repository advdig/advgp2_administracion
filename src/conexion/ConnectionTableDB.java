package conexion;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.AbstractTableModel;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
package sistema;
/**
 *
 *
 */
public class ConnectionTableDB extends AbstractTableModel {
   
    private String usuario;
    private String contraseña;
    public Connection coneccion =null;
    ResultSet rsDatos;
    ResultSetMetaData rsMetaDatos;
    public Statement stSentencias=null;
    public PreparedStatement psPrepararSentencias;
    private String ip,ns;
    private int nDatos;
    public boolean connected = false;
   
    public String getContraseña() {
        return contraseña;
    }

    private void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getUsuario() {
        return usuario;
    }

    private void setUsuario(String usuario) {
        this.usuario = usuario;
    }
   
    public ConnectionTableDB(String usuario , String contraseña, String DB, String consultaInicial, boolean isLocal) throws  SQLException {
        try {
            this.setContraseña(contraseña);
            this.setUsuario(usuario);
            if (!isLocal)
                servidores();
            else
                servidores();
                //ip = "localhost";
            conectar(DB);
            consulta(consultaInicial);
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    private boolean conectar(String baseDatos) throws SQLException, ClassNotFoundException{
        try {
            coneccion = null;
            stSentencias = null; 
            
            usuario = this.getUsuario();
            contraseña = this.getContraseña();
         
            Class.forName("com.mysql.jdbc.Driver");
              
            String url = "jdbc:mysql://" + ip + ":3306/" + baseDatos;
              
            coneccion= DriverManager.getConnection(url,usuario,contraseña);
             
            stSentencias = coneccion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            connected = true;
            
        } catch (SQLException | ClassNotFoundException ex) {
            System.err.println(ex.getMessage());  
        }
        return connected;
    }
  
    public Connection conectarMysql(String DB) throws SQLException,ClassNotFoundException
    {
        coneccion=null;
        stSentencias=null; 
    
        usuario=this.getUsuario();
        contraseña=this.getContraseña();
         
        Class.forName("com.mysql.jdbc.Driver");
              
        String url = "jdbc:mysql://" + ip + ":3306/" + DB;
              
        coneccion= DriverManager.getConnection(url,usuario,contraseña);
             
        stSentencias = coneccion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
        
        connected = true;
        
        return coneccion;
    }
    
    @Override
    public Class getColumnClass(int col) throws IllegalStateException
    {
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        try 
        {
            String clase = rsMetaDatos.getColumnClassName(col + 1);
            return Class.forName(clase);
      }
      catch (ClassNotFoundException | SQLException ex)
      {
         System.err.println(ex.getMessage());
      }
      
      return Object.class;
   }

    @Override
   public int getColumnCount() throws IllegalStateException
   {   
      if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
       
      try 
      {
        return rsMetaDatos.getColumnCount(); 
      }
      catch (SQLException sqlEx) 
      {
        System.err.println(sqlEx.getMessage());
      }
      
      return 0;
   }

    @Override
   public String getColumnName(int col) throws IllegalStateException
   {    
       if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
      
       try {
            return rsMetaDatos.getColumnLabel(col + 1);//.getColumnName(col + 1);  
        } catch ( SQLException sqlEx ) {
            System.err.println(sqlEx.getMessage());
        }
      
        return "";
    }

    @Override
    public int getRowCount() throws IllegalStateException
    {
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        return nDatos;
    }

    @Override
    public Object getValueAt(int row, int col) 
        throws IllegalStateException
    {
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        try 
        {
            rsDatos.absolute(row + 1);
            return (/*rsDatos.getObject(col + 1) == null ? "" : */rsDatos.getObject(col + 1));
        }
        catch (SQLException sqlEx) 
        {
            System.err.println(sqlEx.getMessage());
        }
      
        return "";
    }
  
    public final void consulta(String sql) throws SQLException
    {
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        rsDatos = stSentencias.executeQuery(sql);
        rsMetaDatos = rsDatos.getMetaData();
        
        rsDatos.last();
        nDatos = rsDatos.getRow();
        
        fireTableStructureChanged();
    }
  
    public void ejecutar(String sql) throws SQLException 
    {
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        stSentencias.execute(sql);
    }
    
    public void desconectar()            
    {              
        if (!connected) 
            throw new IllegalStateException("Sin conexion a Base de Datos");
        
        try                                          
        {                                            
            stSentencias.close();
            coneccion.close();
            System.out.println("Sesión de DB finalizada correctamente.");
        }
        catch (SQLException sqlEx)          
        {                                            
            System.err.println(sqlEx.getMessage());
        }
        finally
        {                                            
            connected = false;
        }
    }
   
    private void servidores(){
   
        try{
            java.io.BufferedReader buffer=new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea;
           
            int cont=0;
            while((linea=buffer.readLine())!=null){  
                //ip=linea.substring(1,2);
                if(cont==0){
                    ip = linea.substring(38,linea.length());
                    
                }
                cont++;
            }
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    
    public void setServidor(String ipServidor){
        this.ip = ipServidor;
    }
}

