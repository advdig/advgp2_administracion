/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacion;

/**
 *
 * @author r
 */
public class crear_clave_acceso {
    public crear_clave_acceso(){
    
    }
    
    
    public String crear_clave_acceso(String fecha,String tc,String ruc,String ta,String serie,String NC,String CN,String TE){
    
        String cadena=fecha+tc+ruc+ta+serie+NC+CN+TE;
        
        
        modulo11 a = new modulo11();
        int modulo11=a.obtenerSumaPorDigitos(a.invertirCadena(cadena));
        
        
        cadena=cadena+modulo11;
        
        System.out.println(cadena);
        
        return cadena;
        
    }
    
    
    
    
}
