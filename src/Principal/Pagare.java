/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import sockets.metodosServidor;

/**
 *
 * @author r
 */
public class Pagare extends javax.swing.JFrame {

    String ip;
    Blob bin;
    String npagare;
    
      java.io.BufferedReader br;
       InputStream Ipagare = null;
    /**
     * Creates new form Pagare
     */
    public Pagare(String usuario, String contraseña, String npagare) {
        try {
           
            servidores();
            initComponents();
            this.npagare=npagare;
            
            
            conexion_facturacion n = new conexion_facturacion(usuario, contraseña);
            n.conectar();
            
            ResultSet consultan = n.consulta("SELECT pagare FROM adv_facturacion.pagare where idpagare="+npagare+"");
            if (consultan.first()) {
             
                 bin = consultan.getBlob(1);
                 System.out.println(consultan.getBlob(1).getBinaryStream());
                  Ipagare = consultan.getBlob(1).getBinaryStream();
                 
             
             }
            
            
            java.io.BufferedReader br;
            try {
                String line, txtPagare = "";
                br = new java.io.BufferedReader(new java.io.InputStreamReader(Ipagare));
                int l = 0;
                while ((line = br.readLine()) != null) {
                    ++l;
                    if (l == 1) {
                        //txtPagare += line.substring(3) + "\n";
                    } else if (l < 20) {
                        txtPagare += line + "\n";
                    } else {
                        break;
                    }

                }
                pagare.setText(txtPagare);
                br.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Pagare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Pagare.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pagare = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        pagare.setColumns(20);
        pagare.setRows(5);
        jScrollPane1.setViewportView(pagare);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, java.awt.BorderLayout.PAGE_END);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void servidores() {

        try {
            java.io.BufferedReader buffer = new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea;


            int cont = 0;
            while ((linea = buffer.readLine()) != null) {

                //ip=linea.substring(1,2);

                if (cont == 1) {
                    ip = linea.substring(18, linea.length());


                }


                cont++;

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            
        try {
            metodosServidor estado;
            estado = (metodosServidor) Naming.lookup("rmi://" + ip + "/servidor");

            System.out.println(npagare);
            
            estado.impresion_pagare(npagare);

            JOptionPane.showMessageDialog(null, "Imprimiendo Factura en Isla1");



        } catch (RemoteException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(Pagare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Pagare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Pagare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Pagare.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new Pagare().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea pagare;
    // End of variables declaration//GEN-END:variables
}
