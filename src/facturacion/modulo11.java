/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacion;

/**
 *
 * @author r
 */
public class modulo11 {
 
    public String invertirCadena(String cadena) {
        String cadenaInvertida = "";
        for (int x = cadena.length() - 1; x >= 0; x--) {
            cadenaInvertida = cadenaInvertida + cadena.charAt(x);
        }
        return cadenaInvertida;
    }
 
    public int obtenerSumaPorDigitos(String cadena) {
        int pivote = 2;
        int longitudCadena = cadena.length();
        int cantidadTotal = 0;
        int b = 1;
        for (int i = 0; i < longitudCadena; i++) {
            if (pivote == 8) {
                pivote = 2;
            }
            int temporal = Integer.parseInt("" + cadena.substring(i, b));
            b++;
            temporal *= pivote;
            pivote++;
            cantidadTotal += temporal;
        }
        cantidadTotal = 11 - cantidadTotal % 11;
        
        if(cantidadTotal==11){
        
            cantidadTotal=0;
             
        }else
         if(cantidadTotal==10){
        
            cantidadTotal=1;
            
        }
        
        return cantidadTotal;
    }
 
    public static void main(String args[]) throws Exception {
        modulo11 a = new modulo11();
        System.out.println(a.obtenerSumaPorDigitos(a.invertirCadena("1501201501079173549100120010500000413261234567810")));
    }
}
