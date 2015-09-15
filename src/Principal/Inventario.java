package Principal;


import conexion.ConnectionTableDB;
import java.awt.Color;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Adv
 */
public class Inventario extends javax.swing.JFrame {
    public String usu = "", contra = "";
    ConnectionTableDB model;
    public boolean bandera=false;
    /**
     * Creates new form Inventario
     */
    public Inventario(String user, String password,String vend,String codigoVend) {
 

                initComponents();
        usu = user;
        contra = password;
        
               try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion","select nombre from producto where tipo_producto_idtipo_producto=2", false);
            for(int i=0;i<model.getRowCount();i++){
            listaProductos.addItem(String.valueOf(model.getValueAt(i, 0)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
       //this.getContentPane().setBackground(Color.white);
model.desconectar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        desc = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Sact = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Smax = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Smin = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        Ing = new javax.swing.JTextField();
        agregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        listaProductos = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Inventario");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        cod.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codActionPerformed(evt);
            }
        });
        getContentPane().add(cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 130, 40));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Descripcion");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, -1));

        desc.setEditable(false);
        desc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        getContentPane().add(desc, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 320, 40));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Stock Actual");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, -1));

        Sact.setEditable(false);
        Sact.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        getContentPane().add(Sact, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 130, 40));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Stock Maximo");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 210, -1, -1));

        Smax.setEditable(false);
        Smax.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        getContentPane().add(Smax, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, 130, 40));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Stock Minimo");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 270, -1, -1));

        Smin.setEditable(false);
        Smin.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        getContentPane().add(Smin, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 130, 40));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("INGRESAR");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, -1, -1));

        Ing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IngActionPerformed(evt);
            }
        });
        getContentPane().add(Ing, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 330, 79, 30));

        agregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/agregar articulo.png"))); // NOI18N
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });
        getContentPane().add(agregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 370, 80, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("AGREGAR");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 390, -1, 30));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/ico_inventario.png"))); // NOI18N
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 90, -1));

        jButton1.setText("LIMPIAR PANTALLA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 373, -1, 50));

        listaProductos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "OPCIONAL" }));
        listaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaProductosActionPerformed(evt);
            }
        });
        getContentPane().add(listaProductos, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 220, 30));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Ingrese el Codigo");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        String codigo=cod.getText();
    if(JOptionPane.showConfirmDialog(this, "Seguro que desea ingresar estos datos")==0){   
        if(codigo!=""){
        try{
        String Descripcion=desc.getText();
        int sa=0,sm=0,sM=0,ing=0;
        sa=Integer.parseInt(Sact.getText());
        sm=Integer.parseInt(Smin.getText());
        sM=Integer.parseInt(Smax.getText());
        ing=Integer.parseInt(Ing.getText());
        
            
            try {
            
                if(sa + ing <=sM){
                    model.ejecutar("UPDATE producto set stock_actual= "+(sa+ing)+" WHERE codigo_barra = '"+codigo+"'"); 
  
                    JOptionPane.showMessageDialog(this, "se a ingresado correctamente");
                    cod.setText("");
                }else{
            
                     JOptionPane.showMessageDialog(this, "Sobrepasa el Maximo soportado");
                 }
                
            } catch (SQLException ex) {
                Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
            }
        
                    desc.setText("");
                    Smax.setText("");
                    Smin.setText("");
                    Sact.setText("");
                    cod.setEnabled(true);
                    cod.setEditable(true);
                    listaProductos.setSelectedIndex(0);

        }catch(Exception ex){
           JOptionPane.showMessageDialog(this, "ingrese correctamente todos los datos");

        }//primer try para el ingreso de los datos
        }
       
    }
// TODO add your handling code here:
    }//GEN-LAST:event_agregarActionPerformed

    private void codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codActionPerformed
             String descripcion="";
             int sM=0,sm=0,sa=0;
                  try {
             model = new ConnectionTableDB(usu, contra, "adv_facturacion","SELECT nombre,stock_maximo,stock_minimo,stock_actual FROM producto WHERE codigo_barra='"+cod.getText()+"'", false);
                        try{
                            if(model.getRowCount()!=0){
                            descripcion=(String)model.getValueAt(0, 0);
                            sM=Integer.valueOf(String.valueOf(model.getValueAt(0, 1)));
                            sm=Integer.valueOf(String.valueOf(model.getValueAt(0, 2)));
                            sa=Integer.valueOf(String.valueOf(model.getValueAt(0, 3)));                            
                            Sact.setText(sa+"");
                            Smax.setText(sM+"");
                            Smin.setText(sm+"");                  
                            desc.setText(descripcion); 
                            cod.setEditable(true);
                            Ing.requestFocus();
                            
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

        // TODO add your handling code here:
    }//GEN-LAST:event_codActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
  if(!cod.isEnabled()){
        model.desconectar(); 
  };// TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void IngActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IngActionPerformed
agregar.doClick();        // TODO add your handling code here:
    }//GEN-LAST:event_IngActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cod.setText("");
        desc.setText("");
        Smax.setText("");
        Smin.setText("");
        Sact.setText("");
        listaProductos.setSelectedIndex(0);
        cod.setEditable(true);
        cod.requestFocus();

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void listaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaProductosActionPerformed
        desc.setText("");
        Smax.setText("");
        Smin.setText("");
        Sact.setText("");
        if(!listaProductos.getSelectedItem().toString().equalsIgnoreCase("OPCIONAL")){
        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion","select codigo_barra from producto where nombre='"+listaProductos.getSelectedItem().toString()+"'", false);
            cod.setText(String.valueOf(model.getValueAt(0, 0)));
            cod.setEditable(true);
            cod.requestFocus();
 
            model.desconectar();
        } catch (SQLException ex) {
            Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        }else{
        cod.setText("");
        cod.setEnabled(true);
        }
    }//GEN-LAST:event_listaProductosActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Ing;
    private javax.swing.JTextField Sact;
    private javax.swing.JTextField Smax;
    private javax.swing.JTextField Smin;
    private javax.swing.JButton agregar;
    private javax.swing.JTextField cod;
    private javax.swing.JTextField desc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JComboBox listaProductos;
    // End of variables declaration//GEN-END:variables
}