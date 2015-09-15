package Principal;


import conexion.ConnectionTableDB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author r
 */
public class InventarioTanques extends javax.swing.JFrame {

    /**
     * Creates new form InventarioTanques
     */
   ConnectionTableDB model;
    @SuppressWarnings("empty-statement")
    public InventarioTanques(String usu,String contra) throws SQLException {
    
       try {
           model = new ConnectionTableDB(usu, contra, "adv_facturacion","", false);
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "error al conectarse a la Base de Datos");
           Logger.getLogger(InventarioTanques.class.getName()).log(Level.SEVERE, null, ex);
       }

        initComponents();

    javax.swing.JProgressBar barra2;
     javax.swing.JLabel jLabel2;
     javax.swing.JLabel jLabel3;
     javax.swing.JLabel jLabel4;
     javax.swing.JPanel panel2;
     javax.swing.JLabel tanque2;
     javax.swing.JTextField texto2;
     
     int an=0,al=0;
     int anchoBarra=0;
     double galones=0;
     model.consulta("select count(ntanques) from tanques");
     int filas=Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));
     for (int i=0;i<filas;i++){
     model.consulta("select ntanques,capacidad,producto.nombre,producto.stock_actual from tanques,producto where producto_idproducto=idproducto and ntanques="+(i+1));
      
        barra2 = new javax.swing.JProgressBar();
        texto2 = new javax.swing.JTextField();
        tanque2 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel(); 
        jLabel3 = new javax.swing.JLabel();
       
     
     if(i==4){
     al=al+300;
     an=0;
//     an=an+(30*i);
     }
        //inserto las barras con sus caracteristicas
        barra2.setForeground(new java.awt.Color(153, 255, 255));
        barra2.setMaximum(Integer.parseInt(String.valueOf(model.getValueAt(0, 1))));
        barra2.setOrientation(1);
        barra2.setStringPainted(true);
        barra2.setForeground(new java.awt.Color(69, 141, 29));
        barra2.setRequestFocusEnabled(false);
        anchoBarra=barra2.getHeight();
        
        panel.add(barra2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60+an, 100+al, 90, 125));
        barra2.setMaximum(Integer.parseInt(String.valueOf(model.getValueAt(0, 1))));
        barra2.setValue(Integer.parseInt(String.valueOf(model.getValueAt(0, 3))));
        
        //agrego el galonaje al txt
        texto2.setPreferredSize(new java.awt.Dimension(5, 25));
        galones=Double.parseDouble(String.valueOf(model.getValueAt(0, 3)));
        
        texto2.setText(galones+"gls");
        texto2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        texto2.setFont(new java.awt.Font("Tahoma", 1, 11));
        texto2.setEditable(false);
        
        panel.add(texto2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60+an, 50+al, 90, -1));
        int cap=Integer.parseInt(String.valueOf(model.getValueAt(0, 1)));
        int div=cap/4;
        int acu=0;
        int k=1;
        acu=cap;
        for(int j=0;j<=120;j+=30){
        
        jLabel4 = new javax.swing.JLabel();        
        jLabel4.setText("--"+(acu)+"gls");
        jLabel4.setFont(new java.awt.Font("Vrinda", 0, 11));
        panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(an+105+60, 42+al+j, 90, 120));
        acu=cap-(div*k);
        k++;
        }
        tanque2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/tanque.JPG"))); // NOI18N
        panel.add(tanque2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10+an, 10+al, 160, 220));
        
        //agrego la capacidad al label
        jLabel2.setText("TANQUE  "+String.valueOf(model.getValueAt(0, 2)));
        panel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(35+an, 230+al, -1, -1));
        
        jLabel3.setText("CAPACIDAD  "+String.valueOf(model.getValueAt(0, 1)));
        //agrego el panel con todos los componentes anteriores
        panel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(35+an, 245+al, -1, -1));
        
panel.setBackground(Color.WHITE);
        pack();
        an=an+300;
        
        
     }
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

        panel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tanques");

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    // End of variables declaration//GEN-END:variables
}
