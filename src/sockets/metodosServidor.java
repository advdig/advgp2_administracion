package sockets;

import java.io.InputStream;
import java.rmi.*;
import java.sql.Blob;
import java.sql.ResultSet;

public interface metodosServidor extends Remote {

    public String estadosurtidor(String arg) throws RemoteException;

    public String productosurtidor(String arg) throws RemoteException;

    public String montosurtidor(String arg) throws RemoteException;

    public String volumensurtidor(String arg) throws RemoteException;

    public String ppusurtidor(String arg) throws RemoteException;

    public int cerrarTurno(int arg, String usuario) throws RemoteException;

    public int entrada_turnos(String arg, String arg1) throws RemoteException;

    public int salida_turnos(String arg, String arg1) throws RemoteException;

    public int impresion(String arg, String arg1) throws RemoteException;

    public void impresion_nota(String numero) throws RemoteException;
    
    public int factura(int punto, String s1,String s2,String s3, int idcliente, String metodo_pago, int idusuario, Double subtotal, Double total, Double iva, Double[] cantidadp, Double[] subtotalp, Double[] totalp, Double[] ivap, String[] idproducto, String contraseña, String nusuario,Double[] punit,String [] producto,String []detalled) throws RemoteException;

    public int impresion_pagare(String bin) throws RemoteException;
    
    public int nota(int punto, String s1,String s2,String s3, int idcliente, String metodo_pago, int idusuario, Double subtotal, Double total, Double iva, Double[] cantidadp, Double[] subtotalp, Double[] totalp, Double[] ivap, String[] idproducto, String contraseña, String nusuario,Double[] punit,String [] producto,String fechaFactura,String factura,String motivo) throws RemoteException;

    
    public  ResultSet consulta(String consulta) throws RemoteException;
        
    public int pagare_lubricantes(int idcliente,String [] detalle,Double[] cantidad,Double []subtotalp,Double []totalp,Double []ivap,Double total,Double iva,int idusuario,String []producto)throws RemoteException;
     
    public int reparar_factura(String estado,int punto, String s1,String s2,String s3, int idcliente, String metodo_pago, int idusuario, Double subtotal, Double total, Double iva, Double[] cantidadp, Double[] subtotalp, Double[] totalp, Double[] ivap, String[] idproducto, String contraseña, String nusuario,Double[] punit,String [] producto,String []detalled,String fecha) throws RemoteException;

     
}
