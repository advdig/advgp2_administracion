/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.InputMismatchException;

/**
 *
 * @author 
 */
public class ClaveAcceso {

    public static String generarClaveAcceso(String fechaEmision, String tipoComprobante,
            String ruc, String ambiente, String serie, String numeroComprobante,
            String codigoNumerico, String tipoEmision) {
        
            int verificador = 0;
            String claveGenerada = "";
            if (ruc != null && ruc.length() < 13) {
                ruc = String.format("%013d", new Object[]{ruc});
            }
            //SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            //String fecha = dateFormat.format(Date.parse(fechaEmision));
            StringBuilder clave = new StringBuilder(fechaEmision.replace("/", ""));
            clave.append(tipoComprobante);
            clave.append(ruc);
            clave.append(ambiente);
            clave.append(serie);
            clave.append(numeroComprobante);
            clave.append(codigoNumerico);
            clave.append(tipoEmision);
            verificador = generaDigitoModulo11(clave.toString());
            clave.append(Integer.valueOf(verificador));
            claveGenerada = clave.toString();
            if (clave.toString().length() != 49) {
                claveGenerada = null;
            }
            return claveGenerada;
    }

    public static String generaClaveContingencia(String fechaEmision, String tipoComprobante, String clavesContigencia, String tipoEmision) throws InputMismatchException {
        int verificador = 0;
        String claveGenerada = "";
        StringBuilder clave = new StringBuilder(fechaEmision.replace("/", ""));
        clave.append(tipoComprobante);
        clave.append(clavesContigencia);
        clave.append(tipoEmision);
        verificador = generaDigitoModulo11(clave.toString());
        if (verificador != 10) {
            clave.append(Integer.valueOf(verificador));
            claveGenerada = clave.toString();
        }
        if (clave.toString().length() != 49) {
            claveGenerada = null;
        }
        return claveGenerada;
    }

    public static int generaDigitoModulo11(String cadena) {
        int baseMultiplicar = 7;
        int aux[] = new int[cadena.length()];
        int multiplicador = 2;
        int total = 0;
        int verificador = 0;
        for (int i = aux.length - 1; i >= 0; i--) {
            aux[i] = Integer.parseInt((new StringBuilder()).append("").append(cadena.charAt(i)).toString());
            aux[i] = aux[i] * multiplicador;
            if (++multiplicador > baseMultiplicar) {
                multiplicador = 2;
            }
            total += aux[i];
        }
        if (total == 0 || total == 1) {
            verificador = 0;
        }else {
            verificador = 11 - total % 11 != 11 ? 11 - total % 11 : 0;
        }
        if (verificador == 10) {
            verificador = 1;
        }
        return verificador;
    }
}