/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;


import conexion.ConnectionTableDB;
import java.awt.Color;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

/**
 *
 * @author Adv
 */
public class eliminarArticulo extends javax.swing.JFrame {
public String usu = "", contra = "", ip;
            ConnectionTableDB model;
    /**
     * Creates new form eliminarArticulo
     */
    public eliminarArticulo(String user, String password,String vend,String codigoVend) {
        initComponents();
                       usu = user;
        contra = password;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cod = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        Sact = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        desc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        Smax = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        agregar = new javax.swing.JButton();
        Smin = new javax.swing.JTextField();
        Descuento = new javax.swing.JLabel();
        unitario = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        descuento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Ingrese el Codigo");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Stock Minimo");

        Sact.setEditable(false);
        Sact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SactActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Stock Actual");

        desc.setEditable(false);
        desc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Descripcion");

        Smax.setEditable(false);
        Smax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SmaxActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Stock Maximo");

        agregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ico_eliminar.png"))); // NOI18N
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        Smin.setEditable(false);
        Smin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SminActionPerformed(evt);
            }
        });

        Descuento.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Descuento.setText("Descuento");

        unitario.setEditable(false);
        unitario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitarioActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Precio Unitario");

        descuento.setEditable(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("ELIMINAR");

        jButton1.setText("LIMPIAR PANTALLA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addComponent(cod, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(62, 62, 62)
                            .addComponent(desc, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(40, 40, 40)
                            .addComponent(unitario, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(Descuento)
                            .addGap(67, 67, 67)
                            .addComponent(descuento))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(44, 44, 44)
                            .addComponent(Smax, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(48, 48, 48)
                            .addComponent(Smin, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(54, 54, 54)
                            .addComponent(Sact, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 154, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(316, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(agregar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jLabel6))
                        .addGap(1, 1, 1)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 11, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(cod, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2)
                        .addComponent(desc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8)
                        .addComponent(unitario, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Descuento)
                        .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4)
                        .addComponent(Smax, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5)
                        .addComponent(Smin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(Sact, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 69, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codActionPerformed
        String descripcion="";
        int sM=0,sm=0,sa=0;
        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion","SELECT nombre,stock_maximo,stock_minimo,stock_actual,punit,descuento FROM producto WHERE codigo_barra='"+cod.getText()+"'", false);
            try{
                if(model.getRowCount()!=0){
                    descripcion=(String)model.getValueAt(0, 0);
                    sM=Integer.valueOf(String.valueOf(model.getValueAt(0, 1)));
                    sm=Integer.valueOf(String.valueOf(model.getValueAt(0, 2)));
                    sa=Integer.valueOf(String.valueOf(model.getValueAt(0, 3)));
                    Double pu=Double.valueOf(String.valueOf(model.getValueAt(0, 4)));
                    Double descuen=Double.valueOf(String.valueOf(model.getValueAt(0, 5)));
                    Sact.setText(sa+"");
                    Smax.setText(sM+"");
                    Smin.setText(sm+"");
                    unitario.setText(pu+"");
                    descuento.setText(descuen+"");
                    desc.setText(descripcion);
                    cod.setEditable(false);

                }else{
                    JOptionPane.showMessageDialog(this, "ese articulo no existe");
                }
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "ese articulo no existe");
                cod.setText("");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        desc.requestFocus();  // TODO add your handling code here:
    }//GEN-LAST:event_codActionPerformed

    private void SactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SactActionPerformed
        agregar.doClick();        // TODO add your handling code here:
    }//GEN-LAST:event_SactActionPerformed

    private void descActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descActionPerformed
        unitario.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_descActionPerformed

    private void SmaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SmaxActionPerformed
        Smin.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_SmaxActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed

        if(JOptionPane.showConfirmDialog(this, "seguro que desea ELIMINAR el articulo")==0){
            String descr="";
            int a=0,max=0,min=0;
            String codigo;
            double unit=0,descuen=0;
            descr=desc.getText();
            if(!descr.equals("")){
                try{

                    codigo=(cod.getText());

                    try{
                        unit=Double.parseDouble(unitario.getText());
                        try{
                            max=Integer.parseInt(Smax.getText());
                            try{
                                min=Integer.parseInt(Smin.getText());
                                try{
                                    a=Integer.parseInt(Sact.getText());
                                    try{
                                        descuen=Double.parseDouble(descuento.getText());
                                        if(max>min){
                                            if(max>a){
                                                Conecciones he = new Conecciones(usu, contra);
                                                try {

                                                    model.ejecutar("delete from producto where codigo_barra='"+codigo+"'");

                                                    //  System.out.println("INSERT INTO `adv_facturacion`.`cliente` (`nombre`, `direccion`, `cedula_ruc`, `telefono`, `credito_cliente`,`placa` ,`estado`, `email`, `tipo_identificacion`, `cupo_cliente`) VALUES ('" + ncliente.getText().toUpperCase() + "', '" + dcliente.getText() + "', '" + ruccliente.getText() + "', '" + tcliente.getText() + "', '0','" + placa.getText() + "', 'No Autorizado', '" + email.getText() + "'," + ti+ ", '0.00');");

                                                    JOptionPane.showMessageDialog(this, "articulo ELIMINADO correctamente");
                                                    desc.setText("");
                                                    unitario.setText("");
                                                    Smax.setText("");
                                                    Smin.setText("");
                                                    Sact.setText("");
                                                    cod.setEnabled(true);
                                                    cod.setEditable(true);
                                                    descuento.setText("");
                                                    cod.setText("");
                                                } catch (SQLException ex) {

                                                    JOptionPane.showMessageDialog(this, "el codigo del articulo no existe");
                                                }
                                            }else{
                                                JOptionPane.showMessageDialog(this, "el actual no puede ser mayor al stock maximo");

                                            }
                                        }else{
                                            JOptionPane.showMessageDialog(this, "el minimo es mayor al maximo");
                                        }
                                    }catch(Exception ex){
                                        JOptionPane.showMessageDialog(this, "debe ingresar el descuento correctamente");

                                    }
                                }catch(Exception ex){
                                    JOptionPane.showMessageDialog(this, "el stock actual debe ser un entero");

                                }//quinto
                            }catch(Exception ex){
                                JOptionPane.showMessageDialog(this, "el stock minimo debe ser un entero");

                            }//cuarto
                        }catch(Exception ex){
                            JOptionPane.showMessageDialog(this, "el stock maximo debe ser un entero");

                        }//tercer try
                    }catch(Exception ex){
                        JOptionPane.showMessageDialog(this, "el precio es incorrecto debe ser de formato 0.00");

                    }//segundo try
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this, "el codigo debe ser un entero");

                }//primer try

            }else{
                JOptionPane.showMessageDialog(this, "debe ingresar la descripcion");
            }
        }

    }//GEN-LAST:event_agregarActionPerformed

    private void SminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SminActionPerformed
        Sact.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_SminActionPerformed

    private void unitarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitarioActionPerformed
        Smax.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_unitarioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
cod.setText(""); 
desc.setText("");
unitario.setText("");
descuento.setText("");
Smax.setText("");
Smin.setText("");
Sact.setText("");
cod.setEditable(true);
cod.requestFocus();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Descuento;
    private javax.swing.JTextField Sact;
    private javax.swing.JTextField Smax;
    private javax.swing.JTextField Smin;
    private javax.swing.JButton agregar;
    private javax.swing.JTextField cod;
    private javax.swing.JTextField desc;
    private javax.swing.JTextField descuento;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JTextField unitario;
    // End of variables declaration//GEN-END:variables
}
