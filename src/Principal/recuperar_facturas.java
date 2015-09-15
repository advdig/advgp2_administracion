/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import seguridad.FirmaDigital;

/**
 *
 * @author r
 */
public class recuperar_facturas extends javax.swing.JFrame {

    private DefaultListModel modelo = new DefaultListModel();
  
    String codigop = "";
    Double cantidad, pu;
    String ocont = "";
    int cespecial = 0;
    String nc = "";
    String rz = "", np = "", d = "", r = "", na = "", clientr = "", tcliente = "";
    String s1 = "", s2 = "", tel = "";
    String ss3 = "", ta = "";
    int s3 = 0, idcliente = 0;
    String ti = "";
    String ncliente = "", email = "", cadena = "", mail, contra;
    String usuario, contraseña;
    InputStream PKCS12_RESOURCE;
    String PKCS12_PASSWORD;
    /**
     * Creates new form recuperar_facturas
     */
    public recuperar_facturas(String usuarios,String contras) {
        try {
           
            this.usuario=usuarios;
            this.contra=contras;
            
            
            initComponents();
            
             conexion_facturacion  he = new conexion_facturacion(usuario, contra);

            he.conectar();
            
               Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

             
                Blob archivo = ri.getBlob(13);
             

                PKCS12_RESOURCE = archivo.getBinaryStream();
                PKCS12_PASSWORD = ri.getString(17);
              
            }
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(recuperar_facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(recuperar_facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Elejir archivo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(276, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(230, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File file;
        JFileChooser fc = new JFileChooser();
        int open = fc.showOpenDialog(null);
        if (open == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            FirmaDigital fd = new FirmaDigital(file.getPath(), "firmados\\firmado" + s1 + "-" + s2 + "-" + ss3 + ".xml");
                    fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
                    fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);

                    fd.firmarDocumentoXML();
            
            
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(recuperar_facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(recuperar_facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(recuperar_facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(recuperar_facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new recuperar_facturas().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    // End of variables declaration//GEN-END:variables
}