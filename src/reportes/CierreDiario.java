/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import Principal.Conecciones;
import conexion.conexion_facturacion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author r
 */
public class CierreDiario extends javax.swing.JFrame {
    /**
     * Creates new form CierreDiario
     */
    String usuario,contraseña;
   conexion_facturacion n ;
    DefaultTableModel modelo=new DefaultTableModel(); 
    private DataSource dataSource;
    public CierreDiario(String usuario,String contraseña) {
        this.contraseña=contraseña;
        this.usuario=usuario;
        
        n = new conexion_facturacion(usuario, contraseña);
        initComponents();
           Mostrar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        turnos = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jMenuItem1.setText("Ver Reporte");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("CIERRES DIARIOS");

        jScrollPane2.setComponentPopupMenu(jPopupMenu1);

        turnos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        turnos.setComponentPopupMenu(jPopupMenu1);
        jScrollPane2.setViewportView(turnos);

        jButton1.setText("Anterior");

        jButton2.setText("Siguiente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Mostrar();

    }//GEN-LAST:event_formWindowOpened

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
                        Map parameters = new HashMap();
                     
                        System.out.println(turnos.getModel().getValueAt(turnos.getSelectedRow(),0));
                        System.out.println(turnos.getModel().getValueAt(turnos.getSelectedRow(),1));
                        
                        
                        parameters.put("desde", turnos.getModel().getValueAt(turnos.getSelectedRow(),0));
                       
                        parameters.put("hasta", turnos.getModel().getValueAt(turnos.getSelectedRow(),2));
                    



                        JasperReport report = JasperCompileManager.compileReport("Cierre_Diario.jrxml");
                        JasperPrint print = JasperFillManager.fillReport(report, parameters, n.coneccion);
                        // Exporta el informe a PDF
                        //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
                        //Para visualizar el pdf directamente desde java
                        JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            Logger.getLogger(CierreDiario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    public void Mostrar()
    {
        try {
          // TODO add your handling code here:
          String[] titulos={"Fecha Inicial","Hora Inicial","Fecha Final","Hora Final"};
          modelo=new DefaultTableModel(null,titulos);
         
          n.conectar();
          ResultSet rd;
          rd = n.consulta("SELECT fecha_inicial,hora_inicio,fecha_final,hora_final FROM adv_facturacion.cierre_diario group by fecha_inicial,fecha_final order by fecha_inicial asc ;");
           
          String[] datos = new String[4];
          if(rd.first()){
            do{
                datos[0]=rd.getString(1);
                datos[1]=rd.getString(2);
                datos[2]=rd.getString(3);
                datos[3]=rd.getString(4);
                modelo.addRow(datos);
            }while (rd.next());
          }else{
              JOptionPane.showMessageDialog(this,"No existen datos", "No hay cierres diarios",JOptionPane.INFORMATION_MESSAGE);
          }
           turnos.setModel(modelo);   
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CierreDiario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
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
            java.util.logging.Logger.getLogger(CierreDiario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CierreDiario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CierreDiario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CierreDiario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
              //  new CierreDiario().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable turnos;
    // End of variables declaration//GEN-END:variables
}