/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import Principal.VisualizarFactura;
import conexion.conexion_facturacion;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
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
public class Reporte_Facturas extends javax.swing.JInternalFrame {

    String usuarios, contraseña;

    /**
     * Creates new form Reporte_Facturas
     *
     * @param usuarios
     * @param contraseña
     */
    private DefaultListModel modelo = new DefaultListModel();
    MiModelo modeloclientes = new MiModelo();
    MiModelo modelofacturas = new MiModelo();

    public Reporte_Facturas(String usuarios, String contraseña) {
        initComponents();
        this.usuarios = usuarios;
        this.contraseña = contraseña;

        tabla_clientes.setModel(modeloclientes);

        modeloclientes.addColumn("ID");
        modeloclientes.addColumn("DI");
        modeloclientes.addColumn("Razon Social");

        tabla_facturas.setModel(modelofacturas);

        modelofacturas.addColumn("Fecha");
        modelofacturas.addColumn("Hora");
        modelofacturas.addColumn("Punto");
        modelofacturas.addColumn("Numero");
        modelofacturas.addColumn("Total");
        modelofacturas.addColumn("Estado");
        modelofacturas.addColumn("Metodo_Pago");
        modelofacturas.addColumn("Manguera");
        
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
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        panel = new javax.swing.JSplitPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_clientes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        scroll = new javax.swing.JScrollPane();
        tabla_facturas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        fechai = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fechaf = new com.toedter.calendar.JDateChooser();
        ced = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jMenuItem1.setText("Ver Factura");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        panel.setDividerLocation(350);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        tabla_clientes.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_clientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_clientesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tabla_clientesMousePressed(evt);
            }
        });
        tabla_clientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabla_clientesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_clientes);

        jPanel2.add(jScrollPane2);

        panel.setLeftComponent(jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        tabla_facturas.setAutoCreateRowSorter(true);
        tabla_facturas.setModel(new javax.swing.table.DefaultTableModel(
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
        tabla_facturas.setComponentPopupMenu(jPopupMenu1);
        scroll.setViewportView(tabla_facturas);

        jPanel3.add(scroll);

        panel.setRightComponent(jPanel3);

        jPanel1.add(panel, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Fecha Inicial");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Reporte de Facturas por Clientes ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Fecha Final");

        ced.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cedKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("CEDULA_RUC");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("NOMBRE");

        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });

        jButton1.setText("Buscar Facturas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Exportar Reporte");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ced, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fechai, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fechaf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(45, 45, 45)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(332, 332, 332))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(298, 298, 298)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fechai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ced, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(31, 31, 31))))
        );

        jPanel1.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cedKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cedKeyPressed
       
        
        
        
        try {
             modeloclientes.setNumRows(0);
            
             if (ced.getText().length() > 7) {
            
               Object datos[] = new Object[3];
            
            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
            n.conectar();
            
            
            
             Statement st_im1 = n.coneccion.createStatement();
            ResultSet  ridim1 = st_im1.executeQuery("SELECT idcliente,cedula_ruc,nombre FROM adv_facturacion.cliente WHERE  cedula_ruc like ('" + ced.getText() + "%')  limit 100");

                //  System.out.println()

                while (ridim1.next()) {

                    //(select codigo from codigos,cliente where cliente_idcliente=idcliente and  cedula_ruc like '" + rcliente.getText() + "%' limit 1)

                    for (int i = 0; i < 3; i++) {



                        datos[i] = ridim1.getObject(i + 1);




                        //datos[i] = consultac.getObject(i + 1);
                    }
                    modeloclientes.addRow(datos);
                }
                tabla_clientes.setModel(modeloclientes);
             }else{
             
             }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cedKeyPressed

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
      
         try {
             modeloclientes.setNumRows(0);
             if (nombre.getText().length() > 7) {
            
               Object datos[] = new Object[2];
            
            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
            n.conectar();
            
            
            
             Statement st_im1 = n.coneccion.createStatement();
            ResultSet  ridim1 = st_im1.executeQuery("SELECT cedula_ruc,nombre FROM adv_facturacion.cliente WHERE  nombre like ('%" + nombre.getText() + "%')  limit 100");

                //  System.out.println()

                while (ridim1.next()) {

                    //(select codigo from codigos,cliente where cliente_idcliente=idcliente and  cedula_ruc like '" + rcliente.getText() + "%' limit 1)

                    for (int i = 0; i < 2; i++) {



                        datos[i] = ridim1.getObject(i + 1);




                        //datos[i] = consultac.getObject(i + 1);
                    }
                    modeloclientes.addRow(datos);
                }
                tabla_clientes.setModel(modeloclientes);
             }else{
             
             }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_nombreActionPerformed
SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Date fechaini = fechai.getDate();
           Date fechafi = fechaf.getDate();

           String fi = null, ff = null;

           SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd");

           String horai, horaf;

           String estado = "",punto = null;
            modelofacturas.setNumRows(0);
            
             
            
               Object datos[] = new Object[8];
      
           
      

           try {
               int añoi = fechai.getCalendar().get(Calendar.YEAR);
               int mesi = fechai.getCalendar().get(Calendar.MONTH) + 1;
               int diai = fechai.getCalendar().get(Calendar.DAY_OF_MONTH);
               fi = añoi + "-" + mesi + "-" + diai;
           } catch (NullPointerException ex) {
               JOptionPane.showMessageDialog(this, "Se debe ingresar una fecha de inicio");

           }
           
           try {

               int añof = fechaf.getCalendar().get(Calendar.YEAR);
               int mesf = fechaf.getCalendar().get(Calendar.MONTH) + 1;
               int diaf = fechaf.getCalendar().get(Calendar.DAY_OF_MONTH);

               ff = añof + "-" + mesf + "-" + diaf;
               
               
               
           } catch (NullPointerException ex) {

               JOptionPane.showMessageDialog(this, "Se debe ingresar una fecha final");

           }
           
           fi = formato.format(fechai.getDate());
            ff = formato.format(fechaf.getDate());
           
           
            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
            n.conectar();
        
           String consulta = "SELECT\n"
                        + "     factura.`fecha` AS factura_fecha,\n"
                        + "     factura.`hora` AS factura_hora,\n"
                        + "     s2,\n"
                        + "     CAST(SUBSTRING(numero,9)  AS DECIMAL(10)),\n"
                        + "     factura.`total` AS factura_total,\n"
                        + "     factura.`Estado_factura` AS factura_Estado_factura,\n"
                        + "     factura.`metodo_pago` AS factura_metodo_pago,\n"
                        + "     configuracion.`nmanguera` AS configuracion_nmanguera\n"
                        + "FROM\n"
                        + "     `cliente` cliente INNER JOIN `factura` factura ON cliente.`idcliente` = factura.`cliente_idcliente`\n"
                        + "     INNER JOIN `usuarios` usuarios ON factura.`usuarios_idusuarios` = usuarios.`idusuarios`\n"
                        + "     INNER JOIN `factura_detalle` factura_detalle ON factura.`idfactura` = factura_detalle.`factura_idfactura`\n"
                        + "     INNER JOIN `punto_emision` punto_emision ON factura.`punto_emision_idpunto_emision` = punto_emision.`idpunto_emision`\n"
                        + "     AND punto_emision.`datos_gasolinera_iddatos_gasolinera` = factura.`datos_gasolinera_iddatos_gasolinera`\n"
                        + "     INNER JOIN `configuracion` configuracion ON factura_detalle.`configuracion_nmanguera` = configuracion.`nmanguera`\n"
                        + "     INNER JOIN `producto` producto ON factura_detalle.`producto_idproducto` = producto.`idproducto`\n"
                        + "     where fecha between '"+fi+"' and '"+ff+"' and factura.cliente_idcliente="+tabla_clientes.getValueAt(tabla_clientes.getSelectedRow(), 0)+" group by numero";

            condicion="fecha between '"+fi+"' and '"+ff+"' and factura.cliente_idcliente="+tabla_clientes.getValueAt(tabla_clientes.getSelectedRow(), 0)+"";
            
           System.out.println(consulta);

            Statement st_im1 = n.coneccion.createStatement();
            ResultSet  ridim1 = st_im1.executeQuery(consulta);

                //  System.out.println()

                while (ridim1.next()) {

                    //(select codigo from codigos,cliente where cliente_idcliente=idcliente and  cedula_ruc like '" + rcliente.getText() + "%' limit 1)

                    for (int i = 0; i < 8; i++) {



                        datos[i] = ridim1.getObject(i + 1);




                        //datos[i] = consultac.getObject(i + 1);
                    }
                    modelofacturas.addRow(datos);
                }
                tabla_facturas.setModel(modelofacturas);
        
           
           
           

           int c = 0;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
       

        

      
        
      
        
      
        
        

       
        
        
        

       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
                 n.conectar();
                 
                                 String factura = String.valueOf(tabla_facturas.getValueAt(tabla_facturas.getSelectedRow(), 3));

                ResultSet consultan = n.consulta("select idfactura from factura,punto_emision where CAST(SUBSTRING(numero,9)  AS DECIMAL(10))='" + factura + "' and punto_emision_idpunto_emision=idpunto_emision and s2='" + String.valueOf(tabla_facturas.getValueAt(tabla_facturas.getSelectedRow(), 2)) + "'");

                System.out.println("select idfactura from factura where  CAST(SUBSTRING(numero,9)  AS DECIMAL(10))='" + factura + "'");

                int manguera;
               System.out.println(tabla_facturas.getValueAt(tabla_facturas.getSelectedRow(), 7));
               manguera = (int) (tabla_facturas.getValueAt(tabla_facturas.getSelectedRow(), 7));
                 System.out.println(factura);
                
                if (consultan.first()) {

                    VisualizarFactura vf = new VisualizarFactura(usuarios, contraseña, consultan.getInt(1), manguera);
                    vf.setVisible(true);
                     System.out.println("entro");
                }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tabla_clientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabla_clientesKeyPressed
     System.out.println("entro");
       
    }//GEN-LAST:event_tabla_clientesKeyPressed

    private void tabla_clientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_clientesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_clientesMouseClicked

    private void tabla_clientesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_clientesMousePressed
         nombre.setText((String) tabla_clientes.getValueAt(tabla_clientes.getSelectedRow(),2));
    }//GEN-LAST:event_tabla_clientesMousePressed
String condicion;
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
            n.conectar();

            Map parameters = new HashMap();

            parameters.put("condicion", condicion);
            parameters.put("fechai", formato.format(fechai.getDate()));
            parameters.put("fechaf", formato.format(fechaf.getDate()));
            JasperReport report;
            JasperPrint print = null;
            
            
            
            report = JasperCompileManager.compileReport("Reporte_facturas.jrxml");
            print = JasperFillManager.fillReport(report, parameters, n.coneccion);
            
            
             JasperViewer.viewReport(print, false);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(Reporte_Facturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Reporte_Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte_Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte_Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte_Facturas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Reporte_Facturas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ced;
    private com.toedter.calendar.JDateChooser fechaf;
    private com.toedter.calendar.JDateChooser fechai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nombre;
    private javax.swing.JSplitPane panel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable tabla_clientes;
    private javax.swing.JTable tabla_facturas;
    // End of variables declaration//GEN-END:variables
}
