/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import Principal.PDF;
import Principal.prepagoGasolina;
import conexion.ConnectionTableDB;
import conexion.conexion_facturacion;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
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
public class reporte_clientes_credito_pendiente extends javax.swing.JFrame {

    /**
     * Creates new form reporte_clientes_credito_pendiente
     */ 
    conexion_facturacion con;
                ConnectionTableDB model;
                String idCliente,usuario,contraseña;
    public reporte_clientes_credito_pendiente(String usu,String contra,String idCliente) {
        initComponents();
        this.idCliente=idCliente;
        usuario=usu;
        contraseña=contra;
                    try {
                       con =new conexion_facturacion(usu,contra);
                        model = new ConnectionTableDB(usuario, contraseña, "adv_facturacion","" , false);
                    } catch (SQLException ex) {
                        Logger.getLogger(reporte_clientes_credito_pendiente.class.getName()).log(Level.SEVERE, null, ex);
                    }

    }
    
    public void calcularTotal(){
    int filas=tablaReporte.getRowCount();
    double totales=0;
    for (int i=0;i<filas;i++){
    totales=totales+Double.parseDouble(String.valueOf(tablaReporte.getValueAt(i, 8)));
    }
    
    lblTotales.setText(totales+"");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaReporte = new javax.swing.JTable();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblTotales = new javax.swing.JLabel();
        lblTotales1 = new javax.swing.JLabel();
        Rfact = new javax.swing.JRadioButton();
        Rno = new javax.swing.JRadioButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FACTURAS DE PAGARE");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        tablaReporte.setAutoCreateRowSorter(true);
        tablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaReporte);

        jLabel1.setText("FECHA INICIAL");

        jLabel2.setText("FECHA FINAL");

        jButton1.setText("BUSCAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("MOSTRAR TODOS");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblTotales.setText("                                  ");

        lblTotales1.setText("TOTAL");

        Rfact.setText("Facturados");
        Rfact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RfactActionPerformed(evt);
            }
        });

        Rno.setSelected(true);
        Rno.setText("No Facturados");
        Rno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RnoActionPerformed(evt);
            }
        });

        jButton3.setText("Exportar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jLabel2)
                .addGap(33, 33, 33)
                .addComponent(fechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Rfact)
                .addGap(18, 18, 18)
                .addComponent(Rno)
                .addGap(52, 52, 52)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(57, 57, 57)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblTotales1)
                        .addGap(27, 27, 27)
                        .addComponent(lblTotales)
                        .addGap(83, 83, 83))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Rfact)
                                .addComponent(Rno)))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTotales1)
                        .addGap(0, 5, Short.MAX_VALUE))
                    .addComponent(lblTotales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
model.desconectar();        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing
 String where="";
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

                         DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
tcr.setHorizontalAlignment(SwingConstants.CENTER);
               
        String consulta1="";
        String fact="";
        if (Rno.isSelected()){
        fact="0";
        }else{
        fact="1";
        }
      try {
            
            System.out.println("select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where facturado="+fact+" and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"'");
            
            where="facturado="+fact+" and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"'";
            
            consulta1="select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where facturado="+fact+" and fecha between '"+fechaInicial+"' and '"+fechaFinal+"' and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"'";
            
                  /* if((!hini.equalsIgnoreCase("")) & (!hfin.equalsIgnoreCase(""))){
                   consulta1=consulta1+" and hora between '"+hini+"' and '"+hfin+"'";
                   }*/

 
            model.consulta(consulta1); 
 
            
            tablaReporte.setModel(model);
            for(int i=0;i<tablaReporte.getColumnCount();i++){
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
            //

            calcularTotal();
            
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }  
        
        
        
        
        try{
            if(fechaInicial.getDate()==null || fechaFinal.getDate()==null){
            
            }else{
        String inicial;
String formato = fechaInicial.getDateFormatString();
Date dateI = fechaInicial.getDate();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
inicial=sdf.format(dateI);
//JOptionPane.showMessageDialog(this, inicial);
String ini[]=new String[2];
ini=inicial.split("-");
//String mesI=obtMes(ini[1]);
inicial=ini[2]+"-"+ini[1]+"-"+ini[0];
String ffinal;
String formato2 = fechaFinal.getDateFormatString();
Date dateF = fechaFinal.getDate();
sdf = new SimpleDateFormat(formato2);
 ffinal=sdf.format(dateF);
String fi[]=new String[2];
fi=ffinal.split("/");
//String mesF=obtMes(fi[1]);
ffinal=fi[2]+"-"+fi[1]+"-"+fi[0];

tcr.setHorizontalAlignment(SwingConstants.CENTER);

System.out.println("select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where facturado="+fact+" and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' and pagare.fecha between '"+inicial+"' and '"+ffinal+"'");


where=" facturado="+fact+" and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' and pagare.fecha between '"+inicial+"' and '"+ffinal+"'";


model.consulta("select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where facturado="+fact+" and pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' and pagare.fecha between '"+inicial+"' and '"+ffinal+"'");

            for(int i=0;i<tablaReporte.getColumnCount();i++){
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
            calcularTotal();
            }
}catch(Exception ex){
JOptionPane.showMessageDialog(this, "Debe llenar las dos fechas");
}
// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
                  DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
tcr.setHorizontalAlignment(SwingConstants.CENTER);
                
        String consulta1="";
      try {
            
            System.out.println("select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' ");
            
            
 
        where=" pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' ";
        
            consulta1="select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' ";
            
                  /* if((!hini.equalsIgnoreCase("")) & (!hfin.equalsIgnoreCase(""))){
                   consulta1=consulta1+" and hora between '"+hini+"' and '"+hfin+"'";
                   }*/

 
            model.consulta(consulta1);
 
            
            tablaReporte.setModel(model);
            for(int i=0;i<tablaReporte.getColumnCount();i++){
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
            //

            calcularTotal();
            
                       if(fechaInicial.getDate()==null || fechaFinal.getDate()==null){
            
            }else{
            
String inicial;
String formato = fechaInicial.getDateFormatString();
Date dateI = fechaInicial.getDate();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
inicial=sdf.format(dateI);
//JOptionPane.showMessageDialog(this, inicial);
String ini[]=new String[2];
ini=inicial.split("-");
//String mesI=obtMes(ini[1]);
inicial=ini[0]+"-"+ini[1]+"-"+ini[2];
String ffinal;
String formato2 = fechaFinal.getDateFormatString();
Date dateF = fechaFinal.getDate();
sdf = new SimpleDateFormat("yyyy-MM-dd");
 ffinal=sdf.format(dateF);
String fi[]=new String[2];
fi=ffinal.split("-");
//String mesF=obtMes(fi[1]);
ffinal=fi[0]+"-"+fi[1]+"-"+fi[2];
                          
         where="pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' and pagare.fecha between '"+inicial+"' and '"+ffinal+"'";
         
         
            model.consulta("select idpagare as NUMERO_PAGARE,cliente.nombre as NOMBRE_CLIENTE,cliente.cedula_ruc as CEDULA_CLIENTE,cliente.direccion as DIRECCION,cliente.telefono as TELEFONO,cliente.placa as PLACA,pagare.fecha as FECHA_PAGARE,pagare.hora as PAGARE_HORA,pagare.total as TOTAL,if(facturado=0,'NO FACTURADO','FACTURADO') as ESTADO\n" +
"from pagare,cliente \n" +
"where  pagare.cliente_idcliente=cliente.idcliente\n" +
"AND cliente.cedula_ruc='"+idCliente+"' and pagare.fecha between '"+inicial+"' and '"+ffinal+"'");
                       }
            tablaReporte.setModel(model);
                        for(int i=0;i<tablaReporte.getColumnCount();i++){
            tablaReporte.getColumnModel().getColumn(i).setCellRenderer(tcr);
            }
                        calcularTotal();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "no hay resultados");
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }  
                // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void RfactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RfactActionPerformed
if(Rfact.isSelected()){
Rno.setSelected(false);
}        // TODO add your handling code here:
    }//GEN-LAST:event_RfactActionPerformed

    private void RnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RnoActionPerformed
if(Rno.isSelected()){
Rfact.setSelected(false);
}          // TODO add your handling code here:
    }//GEN-LAST:event_RnoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
                    try {
                      con.conectar();
                        String cedula=tablaReporte.getValueAt(0, 2).toString();
                        String total=lblTotales.getText();
                           Map parameters = new HashMap();
                                
                           System.out.println("where rpoe "+where);
                           parameters.put("condicion",where);
                                   
                                    JasperReport report = JasperCompileManager.compileReport("reporteClientesCredito.jrxml");
                                    JasperPrint print = JasperFillManager.fillReport(report, parameters,con.coneccion);
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

    /**
     * @param args the command line arguments
     */
 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Rfact;
    private javax.swing.JRadioButton Rno;
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotales;
    private javax.swing.JLabel lblTotales1;
    private javax.swing.JTable tablaReporte;
    // End of variables declaration//GEN-END:variables
}
