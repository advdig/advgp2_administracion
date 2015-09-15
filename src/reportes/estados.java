/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package reportes;

import Principal.enviar_email;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author r
 */
import conexion.conexion_facturacion;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class estados extends javax.swing.JInternalFrame {

    public DefaultListModel modelo = new DefaultListModel();
    MiModelo litabla = new MiModelo();
    conexion_facturacion n;
    String nf;
    Map pars;
    Properties props;
    Session session;
    String correo, xml, pdf, correocliente, rz, npdf, nxml;
    MimeMessage message;
    String ncliente, contraseña;

    /**
     * Creates new form estados_cuenta
     */
    public estados(String usuario, String contraseña) {
        try {
            this.usu = usuario;
            this.contra = contraseña;

            n = new conexion_facturacion(usu, contra);
            n.conectar();
            initComponents();
            //this.setExtendedState(this.MAXIMIZED_BOTH);

            litabla.addColumn("Nº");
            litabla.addColumn("Nombre");
            litabla.addColumn("Cedula/ruc");
            litabla.addColumn("Correo");
            litabla.addColumn("Credito");
            litabla.addColumn("Cupo");
        
        
        
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String generateCID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        fechai = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        fechaf = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        rcliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

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
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabla);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 260, 1060, 313);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(5, 2, 0, 5));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Fecha Inicial");
        jPanel2.add(jLabel2);
        jPanel2.add(fechai);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Fecha Final");
        jPanel2.add(jLabel1);
        jPanel2.add(fechaf);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Cliente Ruc");
        jPanel2.add(jLabel3);

        rcliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                rclienteKeyTyped(evt);
            }
        });
        jPanel2.add(rcliente);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Cliente Razon Social");
        jPanel2.add(jLabel4);

        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });
        jPanel2.add(nombre);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("E-mail");
        jPanel2.add(jLabel6);

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });
        jPanel2.add(email);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(20, 30, 520, 200);

        jButton1.setText("Visualizar Estado de Cuenta");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(580, 30, 220, 23);

        jButton2.setText("Enviar al E-mail del cliente");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(580, 80, 220, 23);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("Cliente Razon Social");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(0, 0, 182, 22);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>                        
    String ruc;

    public void enviar() {
        try {




            npdf = "estado_cuenta.pdf";
            pdf = npdf;
            correocliente = email.getText();
            ncliente = nombre.getText();

            Statement st_in = n.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey') FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

                rz = ri.getString(2);
                correo = ri.getString(5);
                contraseña = ri.getString(18);
            }


            // se obtiene el objeto Session. La configuración es para
            // una cuenta de gmail.
            props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", correo);
            props.setProperty("mail.smtp.auth", "true");

            session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);

            // Se compone la parte del texto
            BodyPart texto = new MimeBodyPart();
                   
            
            message = new MimeMessage(session);
             
                message.setSubject(rz + "  Estado de Cuenta");
            
            String cid = ContentIdGenerator.getContentId();
            
          MimeMultipart content = new MimeMultipart("related");
          MimeBodyPart textPart = new MimeBodyPart(); 
          textPart.setText("<html><head>"
                    + "<title>This is not usually displayed</title>"
                    + "</head>\n"
                    + "<body><div><b>Estimado(a) " + ncliente + "</b></div>"
                    + "<div>Presente</div>\n"
                    + "<div>Gracias por preferirnos</div>\n"
                    + "<div>Adjuntamos a este email estado de cuenta </div>\n"
                    + "<div>Con fecha de inicio " + fi + "</div>\n"
                    + "<div>y fecha de corte " + ff + "</div>\n"
                    +"<div>Por concepto de consumo de combustible</div>\n"
                    +"<div>Atentamente</div>\n"
                    +"<div>" + rz + "</div>\n"
                    + "<div><img src=\"cid:"
                    + cid
                    + "\" /></div>\n" + "</body></html>",
                    "US-ASCII", "html");
           content.addBodyPart(textPart);
// Image part
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile("ad.jpg");
            imagePart.setContentID("<" + cid + ">");
            imagePart.setDisposition(MimeBodyPart.INLINE);
            content.addBodyPart(imagePart);

            /* texto.setText("<img src=\"cid:ad.jpg\">"
             + "Estimado(a) " + ncliente + "\n"
             + "Presente.\n"
             + "Gracias por preferirnos.\n"
             + "\n"
             + "Adjuntamos a este email estado de cuenta ,\n"
             + "Con fecha de inicio " + fi + "\n"
             + "y fecha de corte " + ff + "\n"
             + "por concepto de consumo de combustible\n"
             + "Atentamente,\n"
             + "" + rz + "\n"
             + "--------------------------------------------------------------------------------\n");
             */



            // Se compone el adjunto con la imagen
            BodyPart adjuntopdf = new MimeBodyPart();
            adjuntopdf.setDataHandler(new DataHandler(new FileDataSource(pdf)));
            adjuntopdf.setFileName(npdf);

            content.addBodyPart(adjuntopdf);


            // Una MultiParte para agrupar texto e imagen.
            //MimeMultipart multiParte = new MimeMultipart();
            //multiParte.addBodyPart(texto);
           
            //multiParte.addBodyPart(imagePart);
            

            // Se compone el correo, dando to, from, subject y el
            // contenido.

            if (correocliente == null) {
            } else {
                message.setFrom(new InternetAddress(correo));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(correocliente));

                message.setContent(content);




                // Se envia el correo.



                Transport t = session.getTransport("smtp");
                t.connect(correo, contraseña);
                t.sendMessage(message, message.getAllRecipients());
                t.close();


                JOptionPane.showMessageDialog(rootPane, "Correo enviado Correctamente ");

            }

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void LimpiarJTable() {

        litabla.setNumRows(0);


    }
    Object datos[] = new Object[7];
    private void rclienteKeyTyped(java.awt.event.KeyEvent evt) {                                  

        char c = evt.getKeyChar();


        if (c < '0' || c > '9') {
            getToolkit().beep();

            evt.consume();



        }




        ruc = rcliente.getText();
        try {

            n.conectar();

            LimpiarJTable();
            int id = 0, telefono = 0;
            String nombrec = null, mail = null, cedula;



            Statement st_im1 = n.coneccion.createStatement();
            ResultSet ridim1 = st_im1.executeQuery("SELECT idcliente,nombre,cedula_ruc,email,credito_cliente,cupo_cliente FROM adv_facturacion.cliente  WHERE credito_cliente=1 and (cedula_ruc) like ( '" + ruc + "%' )  group by nombre limit 100");


            System.out.println("SELECT idcliente,nombre,cedula_ruc,email,codigo,credito_cliente,cupo_cliente FROM adv_facturacion.cliente  WHERE  (cedula_ruc) like ( '" + ruc + "%' ) limit 100");




            while (ridim1.next()) {



                for (int i = 0; i < 6; i++) {
                    datos[i] = ridim1.getObject(i + 1);

                    //datos[i] = consultac.getObject(i + 1);
                }
                litabla.addRow(datos);
            }
            tabla.setModel(litabla);


            st_im1.close();
            ridim1.close();

            n.coneccion.close();
        } catch (SQLException ex) {
        } catch (java.lang.NullPointerException ex) {

            System.out.println("cliente no encontrado");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    String contra, usu;

    public void usuarios(String usuario, String contraseña) {
        try {
            contra = contraseña;
            usu = usuario;
            n = new conexion_facturacion(usu, contra);
            n.conectar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    private void cclienteKeyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();


        if (c < '0' || c > '9') {
            getToolkit().beep();

            evt.consume();



        }




    }                                 

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
    }                                      
    String idc = "";
    private void tablaMouseClicked(java.awt.event.MouseEvent evt) {                                   
        String cedula = "";
        String nombrec = "";
        String direccion = "";

        try {

            DefaultTableModel tm = (DefaultTableModel) tabla.getModel();
            cedula = String.valueOf(tm.getValueAt(tabla.getSelectedRow(), 2));
            nombrec = String.valueOf(tm.getValueAt(tabla.getSelectedRow(), 1));
            direccion = String.valueOf(tm.getValueAt(tabla.getSelectedRow(), 4));
            correo = String.valueOf(tm.getValueAt(tabla.getSelectedRow(), 3));

            if (correo == null) {

                correo = "";

            }
            idc = String.valueOf(tm.getValueAt(tabla.getSelectedRow(), 0));


            nombre.setText(nombrec);
            rcliente.setText(cedula);
            email.setText(correo);








        } catch (java.lang.NumberFormatException ex) {
        }
    }                                  
    String ff, fi;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        try {
             InputStream marca_agua;
               marca_agua = new FileInputStream("plantilla-para-programa02.jpg");
            int añoi = fechai.getCalendar().get(Calendar.YEAR);
            int mesi = fechai.getCalendar().get(Calendar.MONTH) + 1;
            int diai = fechai.getCalendar().get(Calendar.DAY_OF_MONTH);


            fi = añoi + "-" + mesi + "-" + diai;


            int añof = fechaf.getCalendar().get(Calendar.YEAR);
            int mesf = fechaf.getCalendar().get(Calendar.MONTH) + 1;
            int diaf = fechaf.getCalendar().get(Calendar.DAY_OF_MONTH);


            ff = añof + "-" + mesf + "-" + diaf;
            System.out.println(fi);
            System.out.println(ff);
            System.out.println(idc);

            n.conectar();


            Statement st_im1 = n.coneccion.createStatement();
            ResultSet ridim1 = st_im1.executeQuery("SELECT idcliente,nombre,cedula_ruc,email,credito_cliente,cupo_cliente FROM adv_facturacion.cliente  WHERE credito_cliente=1 and (cedula_ruc) like ( '" + ruc + "%' )  group by nombre limit 100");


            Map parameters = new HashMap();
            parameters.put("fechai", fi);
            parameters.put("fechaf", ff);
            parameters.put("cliente", idc);
            parameters.put("marca",marca_agua);

            JasperReport report = JasperCompileManager.compileReport("estado_cuenta.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, n.coneccion);
            // Exporta el informe a PDF
            //JasperExportManager.exportReportToPdfFile(print, "venta_combu.pdf");
            //Para visualizar el pdf directamente desde java
            JasperViewer.viewReport(print, false);



        } catch (ClassNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         

        try {

            
            
            JOptionPane.showMessageDialog(rootPane, "Creando Pdf ");

               InputStream marca_agua;
               marca_agua = new FileInputStream("plantilla-para-programa02.jpg");
            int añoi = fechai.getCalendar().get(Calendar.YEAR);
            int mesi = fechai.getCalendar().get(Calendar.MONTH) + 1;
            int diai = fechai.getCalendar().get(Calendar.DAY_OF_MONTH);


            fi = añoi + "-" + mesi + "-" + diai;


            int añof = fechaf.getCalendar().get(Calendar.YEAR);
            int mesf = fechaf.getCalendar().get(Calendar.MONTH) + 1;
            int diaf = fechaf.getCalendar().get(Calendar.DAY_OF_MONTH);


            ff = añof + "-" + mesf + "-" + diaf;
            System.out.println(fi);
            System.out.println(ff);
            System.out.println(idc);

            n.conectar();

            Map parameters = new HashMap();

            parameters.put("fechai", fi);
            parameters.put("fechaf", ff);
            parameters.put("cliente", idc);
            parameters.put("marca",marca_agua);


            JasperReport report = JasperCompileManager.compileReport("estado_cuenta.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, n.coneccion);
            // Exporta el informe a PDF
            JasperExportManager.exportReportToPdfFile(print, "estado_cuenta.pdf");


            JOptionPane.showMessageDialog(rootPane, "Pdf creado correctamente enviando mail ");

            if (email.getText().length() == 0) {

                JOptionPane.showMessageDialog(rootPane, "Ingrese Un correo para poder enviar mail ");

            } else {

                JOptionPane.showMessageDialog(rootPane, "Enviando mail ");
                enviar();


            }


            //Para visualizar el pdf directamente desde java
            // JasperViewer.viewReport(print, false);



        } catch (ClassNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }                                        

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {                                      
        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(estados_cuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(estados_cuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(estados_cuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(estados_cuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new estados_cuenta("root", "manager").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JTextField email;
    private com.toedter.calendar.JDateChooser fechaf;
    private com.toedter.calendar.JDateChooser fechai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField rcliente;
    private javax.swing.JTable tabla;
    // End of variables declaration                   
}
