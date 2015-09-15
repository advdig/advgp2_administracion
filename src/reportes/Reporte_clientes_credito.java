/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import conexion.conexion_facturacion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import reportes.reporte_clientes_credito_pendiente;

/**
 *
 * @author r
 */
public class Reporte_clientes_credito extends javax.swing.JInternalFrame {

    /**
     * Creates new form reporte_clientes_credito
     */
    String usuario,

    /**
     * Creates new form Reporte_clientes_credito
     */
    contraseña;
    private DefaultListModel modelo = new DefaultListModel();
    MiModelo litabla = new MiModelo();

    public Reporte_clientes_credito(String usuario, String contraseña) {
        this.usuario = usuario;
        this.contraseña = contraseña;

        initComponents();

        tabla.setModel(litabla);

        litabla.addColumn("DI");
        litabla.addColumn("Razon Social");
        litabla.addColumn("Pagares facturados");
        litabla.addColumn("Pagares por facturar");

        // llenar();
    }

    public void llenar(String condicion) {

        try {
            String nombre = "SELECT\n"
                    + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                    + "     cliente.`nombre` AS cliente_nombre,\n"
                    + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and facturado=1 and cedula_ruc=cliente_cedula_ruc ),\n"
                    + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and  facturado=0 and cedula_ruc=cliente_cedula_ruc )\n"
                    + "FROM\n"
                    + "     `cliente` cliente INNER JOIN `credito_cliente` credito_cliente ON cliente.`idcliente` = credito_cliente.`cliente_idcliente`group by cedula_ruc";
            conexion_facturacion n = new conexion_facturacion(usuario, contraseña);
            n.conectar();

            if (Rno.isSelected()) {

                nombre = "SELECT\n"
                        + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                        + "     cliente.`nombre` AS cliente_nombre,\n"
                        + "	   '0'as si, \n"
                        + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and  facturado=0 and cedula_ruc=cliente_cedula_ruc ) as no\n"
                        + "FROM\n"
                        + "     `cliente` cliente INNER JOIN `credito_cliente` credito_cliente ON cliente.`idcliente` = credito_cliente.`cliente_idcliente`group by cedula_ruc";

            }
            if (Rsi.isSelected()) {

                nombre = "SELECT\n"
                        + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                        + "     cliente.`nombre` AS cliente_nombre,\n"
                        + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and facturado=1 and cedula_ruc=cliente_cedula_ruc ),\n"
                        + "	   '0' as no \n"
                        + "FROM\n"
                        + "     `cliente` cliente INNER JOIN `credito_cliente` credito_cliente ON cliente.`idcliente` = credito_cliente.`cliente_idcliente`group by cedula_ruc";

            }

            if (todos.isSelected()) {

                nombre = "SELECT\n"
                        + "     cliente.`cedula_ruc` AS cliente_cedula_ruc,\n"
                        + "     cliente.`nombre` AS cliente_nombre,\n"
                        + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and facturado=1 and cedula_ruc=cliente_cedula_ruc ),\n"
                        + "	   (select sum(total) from pagare,cliente where " + hora + " and cliente_idcliente=idcliente and  facturado=0 and cedula_ruc=cliente_cedula_ruc )\n"
                        + "FROM\n"
                        + "     `cliente` cliente INNER JOIN `credito_cliente` credito_cliente ON cliente.`idcliente` = credito_cliente.`cliente_idcliente`group by cedula_ruc";

            }

            System.out.println("consulta " + nombre);

            ResultSet consultan = n.consulta(nombre);

            Object datos[] = new Object[4];

            while (consultan.next()) {

                for (int i = 0; i < 4; i++) {

                    datos[i] = consultan.getObject(i + 1);

                    //datos[i] = consultac.getObject(i + 1);
                }

                if (consultan.getDouble(3) > 0 || consultan.getDouble(4) > 0) {

                    litabla.addRow(datos);

                }

            }

            tabla.setModel(litabla);

            n.coneccion.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_clientes_credito.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reporte_clientes_credito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void reiniciarJTable(javax.swing.JTable Tabla) {
        DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }

    }

    public class MiModelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {

            return false;

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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        mostrar = new javax.swing.JMenuItem();
        grupo = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fechai = new org.freixas.jcalendar.JCalendarCombo();
        jLabel11 = new javax.swing.JLabel();
        Date date = new Date();
        SpinnerDateModel sm =
        new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
        horai = new javax.swing.JSpinner(sm);
        jLabel9 = new javax.swing.JLabel();
        fechaf = new org.freixas.jcalendar.JCalendarCombo();
        jLabel10 = new javax.swing.JLabel();
        Date date1 = new Date();
        SpinnerDateModel sm1 =
        new SpinnerDateModel(date1, null, null, Calendar.HOUR_OF_DAY);
        horaf = new javax.swing.JSpinner(sm1);
        Rno = new javax.swing.JRadioButton();
        Rsi = new javax.swing.JRadioButton();
        todos = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        mostrar.setText("mostrar detalles");
        mostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mostrarActionPerformed(evt);
            }
        });
        jPopupMenu1.add(mostrar);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/buscar-buscar-ampliar-icono-9048-32.png"))); // NOI18N
        jButton1.setText("BUSCAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        jButton3.setText("Exportar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        getContentPane().add(jPanel2);

        jPanel1.setLayout(new java.awt.GridLayout(3, 4));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("FECHA INICIAL");
        jPanel1.add(jLabel1);
        jPanel1.add(fechai);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("HORA INICIAL");
        jPanel1.add(jLabel11);

        JSpinner.DateEditor de = new JSpinner.DateEditor(horai, "HH:mm:ss");
        horai.setEditor(de);
        jPanel1.add(horai);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("FECHA FINAL");
        jPanel1.add(jLabel9);
        jPanel1.add(fechaf);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("HORA FINAL");
        jPanel1.add(jLabel10);

        JSpinner.DateEditor de1 = new JSpinner.DateEditor(horaf, "HH:mm:ss");
        horaf.setEditor(de1);
        jPanel1.add(horaf);

        grupo.add(Rno);
        Rno.setText("NO FACTURADOS");
        Rno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RnoActionPerformed(evt);
            }
        });
        jPanel1.add(Rno);

        grupo.add(Rsi);
        Rsi.setText("FACTURADOS");
        jPanel1.add(Rsi);

        grupo.add(todos);
        todos.setText("TODOS");
        jPanel1.add(todos);

        getContentPane().add(jPanel1);

        tabla.setAutoCreateRowSorter(true);
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabla.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tabla);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mostrarActionPerformed
        int fila = tabla.getSelectedRow();
        String idCliente = String.valueOf(tabla.getValueAt(fila, 0));
        reporte_clientes_credito_pendiente a = new reporte_clientes_credito_pendiente(usuario, contraseña, idCliente);
        a.setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_mostrarActionPerformed
    String condicionp, condicionf, condicionn, condicion, hora;
    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat horafo = new SimpleDateFormat("HH-MM-SS");
    SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        reiniciarJTable(tabla);

        String fact = "";

        System.out.println(fact);

        if (horai.getValue().toString().equalsIgnoreCase(horaf.getValue().toString())) {

            hora = "fecha between '" + formato.format(fechai.getDate()) + "' and '" + formato.format(fechaf.getDate()) + "' ";

        } else {

            hora = "(CONVERT(concat_ws(' ', fecha, hora),DATETIME) >= CONVERT('" + formato.format(fechai.getDate()) + " " + df1.format(horai.getValue()) + "',DATETIME) and CONVERT(concat_ws(' ', fecha, hora),DATETIME) <= CONVERT('" + formato.format(fechaf.getDate()) + " " + df1.format(horaf.getValue()) + "',DATETIME)) ";

        }

        llenar(hora);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        try {

            conexion_facturacion n = new conexion_facturacion(usuario, contraseña);
            n.conectar();

            Map parameters = new HashMap();

            parameters.put("condicion", hora);
            parameters.put("fechai", formato.format(fechai.getDate()));
            parameters.put("fechaf", formato.format(fechaf.getDate()));
            JasperReport report;
            JasperPrint print = null;
            if (Rno.isSelected()) {
                report = JasperCompileManager.compileReport("Creditos_fechas_no_facturados.jrxml");
                print = JasperFillManager.fillReport(report, parameters, n.coneccion);

            }
            if (Rsi.isSelected()) {
                
                report = JasperCompileManager.compileReport("Creditos_fechas_facturados.jrxml");
                print = JasperFillManager.fillReport(report, parameters, n.coneccion);
                
                
            }

            if (todos.isSelected()) {

                report = JasperCompileManager.compileReport("Creditos_fechas.jrxml");
                print = JasperFillManager.fillReport(report, parameters, n.coneccion);

            }

            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(print, false);
        } catch (JRException ex) {
            Logger.getLogger(reporte_clientes_credito_pendiente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(reporte_clientes_credito_pendiente.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void RnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RnoActionPerformed

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
            java.util.logging.Logger.getLogger(Reporte_clientes_credito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte_clientes_credito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte_clientes_credito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte_clientes_credito.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new Reporte_clientes_credito().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Rno;
    private javax.swing.JRadioButton Rsi;
    private org.freixas.jcalendar.JCalendarCombo fechaf;
    private org.freixas.jcalendar.JCalendarCombo fechai;
    private javax.swing.ButtonGroup grupo;
    private javax.swing.JSpinner horaf;
    private javax.swing.JSpinner horai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem mostrar;
    private javax.swing.JTable tabla;
    private javax.swing.JRadioButton todos;
    // End of variables declaration//GEN-END:variables
}
