/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Adv
 */
package Principal;

import Principal.buscarFactura;
import conexion.ConnectionTableDB;
import conexion.conexion_facturacion;
import facturacion.crear_nota_credito_normal;
import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import Principal.prepagoGasolina;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Formatter;
import sockets.metodosServidor;

public class buscarFacturaGasolina extends javax.swing.JFrame implements Runnable {

    /**
     * Creates new form buscarFactura
     */
    
    ConnectionTableDB model;
    String usu;
    String contra;
    public int filas = 0;
    private int notaCredito;
    Calendar c = Calendar.getInstance();
    private String metodo;
    Thread h1;
    String hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
    String minutos = Integer.toString(c.get(Calendar.MINUTE));
    String segundos = Integer.toString(c.get(Calendar.SECOND));
    private InputStream PKCS12_RESOURCE;
    private String PKCS12_PASSWORD;
    private String Ewsri, Awsri;
    private String ipservidor, nfactura;
    String est="";
    int idUser;
    public buscarFacturaGasolina(String usuario, String contras) {
        this.usu = usuario;
        this.contra = contras;


        initComponents();
        h1 = new Thread(this);
        h1.start();
        //saco las ip de la configuracion
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("servidores.adv");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String linea, linea1 = "";
            int i = 0;
            while ((linea = br.readLine()) != null) {
                if (i == 1) {
                    linea1 = linea;
                }
                i++;
            }
            String[] ips = new String[1];
            ips = linea1.split(":");
            //aqui esta la ip del servidor sacada del archivo
            ipservidor = ips[1];
            //System.out.println(ipservidor);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT s2 from punto_emision ", false);
            //String[] la=model.getValueAt(0, 0).toString().split("");


            for (int i = 0; i < model.getRowCount(); i++) {
                numeros.addItem(model.getValueAt(i, 0) + "           " + model.getValueAt(i, 1).toString().toUpperCase());
            }
             model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT secuencia1_factura from datos_gasolinera ", false);
            //String[] la=model.getValueAt(0, 0).toString().split("");

            est=model.getValueAt(0, 0).toString();

        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(ipservidor);

        ResultSet rs;
        try {
            conexion_facturacion he = new conexion_facturacion(usuario, contras);
            he.conectar();
            Statement st_in = he.coneccion.createStatement();


            rs = st_in.executeQuery("select idusuarios from usuarios where usuario='"+usu+"' limit 1");

            rs.next();
            
            
                 
                 //System.out.println("select idusuarios from usuarios where usuario='"+usu+"' limit 1");
          idUser = rs.getInt(1);
            
          
            
            
            he.coneccion.close();
        } catch (SQLException ex) {
            Logger.getLogger(buscarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(buscarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }


        String dia = Integer.toString(c.get(Calendar.DATE));
        String mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        String ano = Integer.toString(c.get(Calendar.YEAR));

        if (Integer.parseInt(minutos) < 10) {
            minutos = "0" + minutos;
        }
        if (Integer.parseInt(segundos) < 10) {
            segundos = "0" + segundos;
        }

        hoy.setText(ano + "-" + mes + "-" + dia);
        Hora.setText(hora + ":" + minutos + ":" + segundos);
        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", "select tipo_ambiente,secuencia1_factura,nombre_comercial,obligado_llevar_contabilidad,contribuyente_especial,razon_social,direccion,ruc from datos_gasolinera", false);
        } catch (SQLException ex) {
            Logger.getLogger(buscarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ambiente = String.valueOf(model.getValueAt(0, 0));

        if (ambiente.equalsIgnoreCase("1")) {
            Ewsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
            Awsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
        } else {
            Ewsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
            Awsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
        }




        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.white);

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
        numero = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaResultado = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();
        ci = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        hoy = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        Hora = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaArticulos = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        numeros = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        l = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nota Gasolina");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Ingrese el Numero de Factura");

        numero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numeroActionPerformed(evt);
            }
        });

        tablaResultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaResultado);

        jLabel2.setText("Ingrese el CI/RUC del cliente correspondiente");

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombres", "CI/RUC", "Telefono", "Correo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCliente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClienteMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablaClienteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tablaClienteMouseExited(evt);
            }
        });
        jScrollPane4.setViewportView(tablaCliente);

        ci.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ci.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ciActionPerformed(evt);
            }
        });
        ci.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                ciKeyTyped(evt);
            }
        });

        jLabel3.setText("Ingrese el Nombre del CLiente Correspondiente");

        nombre.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });
        nombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreKeyTyped(evt);
            }
        });

        jButton1.setText("Crear Nota de Credito");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Fecha Hoy");

        hoy.setEnabled(false);

        jLabel5.setText("Hora");

        Hora.setEnabled(false);

        tablaArticulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tablaArticulos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaArticulosKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tablaArticulosKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(tablaArticulos);

        jButton2.setText("limpiar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("COMPRAS");

        jLabel7.setText("--");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ci, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addGap(58, 58, 58)
                        .addComponent(numeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numero, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel4)
                        .addGap(33, 33, 33)
                        .addComponent(hoy, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(l, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(numero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(hoy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addComponent(numeros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addComponent(l, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(ci, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(7, 7, 7)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    String nmanguera;

    private void numeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeroActionPerformed
        String z = numero.getText();
        String cero = "";
        for (int i = z.length(); i < 9; i++) {
            cero = cero + "0";
        }
        z = cero + z;
        String num = est+"-" + numeros.getSelectedItem().toString().replace(" ", "") + "-" + z;
        numero.setText(num);





        DefaultTableModel tabla = (DefaultTableModel) tablaResultado.getModel();
        try {// hago la busqueda de la factura,datos del cliente y vendedor
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT Estado_Factura,Fecha,Hora,Metodo_Pago,Cedula_ruc,cliente.nombre as nombreCliente,usuarios.nombre as nombreVendedor,punto_emision_idpunto_emision,configuracion_nmanguera\n"
                    + "FROM factura,cliente,usuarios,factura_detalle\n"
                    + "WHERE idfactura=factura_idfactura and cliente_idcliente=idcliente AND usuarios_idusuarios=idusuarios AND numero='" + num + "' AND (Estado_factura='AUTORIZADO')", false);
            if (String.valueOf(model.getValueAt(0, 0)).equals("")) {
                JOptionPane.showMessageDialog(this, "ese numero de factura no existe o no esta autorizada");
                numero.setText("");
                if (tabla.getRowCount() != 0) {
                    for (int i = 0; i < tabla.getColumnCount(); i++) {
                        tabla.setValueAt("", 0, i);
                    }
                }


            } else {
                metodo = String.valueOf(model.getValueAt(0, 3));
                nmanguera = String.valueOf(model.getValueAt(0, 9));

                String[] a = new String[model.getColumnCount()];
                for (int i = 0; i < model.getColumnCount(); i++) {
                    a[i] = String.valueOf(model.getColumnName(i));
                }
                tabla.setColumnIdentifiers(a);
                tabla.addRow(new Object[]{"", "", "", ""});

                for (int i = 0; i < model.getColumnCount(); i++) {
                    tabla.setValueAt(String.valueOf(model.getValueAt(0, i)), 0, i);
                }
                numero.setEnabled(false);
                DefaultTableModel tabla2 = (DefaultTableModel) tablaArticulos.getModel();

                model = new ConnectionTableDB(usu, contra, "adv_facturacion", "select cantidad,factura_detalle.subtotal,factura_detalle.total,factura_detalle.iva,nombre,punit,idproducto \n"
                        + "from factura,factura_detalle,producto\n"
                        + "where idproducto=producto_idproducto and factura_idfactura=idfactura and numero='" + num + "'", false);
                a = new String[model.getColumnCount()];
                for (int i = 0; i < model.getColumnCount(); i++) {
                    a[i] = String.valueOf(model.getColumnName(i));
                }

                tabla2.setColumnIdentifiers(a);
                for (int i = 0; i < model.getRowCount(); i++) {
                    tabla2.addRow(new Object[]{"", "", "", "", "", "", ""});

                    for (int j = 0; j < model.getColumnCount(); j++) {
                        tabla2.setValueAt(String.valueOf(model.getValueAt(i, j)), i, j);

                    }
                }
            }



// TODO add your handling code here:
        } catch (SQLException ex) {
            JOptionPane.showConfirmDialog(this, "ERROR CON LA BASE DE DATOS");
            Logger.getLogger(buscarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_numeroActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//principalAceites.conS.setEnabled(true);        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void tablaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseClicked
        int filas = tablaCliente.getSelectedRow();
        nombre.setText(String.valueOf(tablaCliente.getValueAt(filas, 0)));
        ci.setText(String.valueOf(tablaCliente.getValueAt(filas, 1)));

        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseClicked

    private void tablaClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseEntered
        this.setCursor(Cursor.HAND_CURSOR);        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseEntered

    private void tablaClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseExited
        this.setCursor(Cursor.DEFAULT_CURSOR);        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseExited

    private void ciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ciActionPerformed
        //me realiza el ingreso de los datos directamento por ci
        String consulta = "SELECT nombre,telefono,email FROM cliente WHERE cedula_ruc='" + ci.getText() + "'";
        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", consulta, false);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }
        nombre.setText((String) model.getValueAt(0, 0));



        // TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_ciActionPerformed

    private void ciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ciKeyTyped
        //hago las busquedas por ci
        char c = evt.getKeyChar();
        int filas;
        busqueda(ci.getText(), "cedula_ruc");

        // TODO add your handling code here:
    }//GEN-LAST:event_ciKeyTyped

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
    }//GEN-LAST:event_nombreActionPerformed

    private void nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreKeyTyped
        char c = evt.getKeyChar();
        int filas;
        //realizo las busquedas de los clientes segun lo queescriba en el nombre

        busqueda(nombre.getText(), "nombre");
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreKeyTyped
    String factura;
      String [] detalled=new String[20];
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (tablaResultado.getRowCount() > 0 & !nombre.getText().equals("") & !ci.getText().equals("")) {
            try {
                //servidor impresion = new servidor();

                String fechaFactura = String.valueOf(tablaResultado.getValueAt(0, 1));

                String f[] = new String[3];
                f = fechaFactura.split("-");
                fechaFactura = f[2] + "/" + f[1] + "/" + f[0];
                String idC = String.valueOf(tablaResultado.getValueAt(0, 4));
                String motivo = JOptionPane.showInputDialog("Cual es el motivo (max 3 palabras)");
                if (motivo.equals("")) {
                    motivo = "reverso";
                }
                motivo = motivo.toUpperCase();

                factura = numero.getText();
                String fechaEmision = hoy.getText();

                f = fechaEmision.split("-");
                if (Integer.parseInt(f[2]) < 10) {
                    f[2] = "0" + f[2];
                }
                if (Integer.parseInt(f[1]) < 10) {
                    f[1] = "0" + f[1];
                }
                fechaEmision = f[2] + "/" + f[1] + "/" + f[0];
                String fechaEmi = f[0] + "-" + f[1] + "-" + f[2];
                System.out.println(fechaEmision);
                System.out.println(fechaFactura);








                model = new ConnectionTableDB(usu, contra, "adv_facturacion", "select tipo_ambiente,secuencia1_factura,nombre_comercial,obligado_llevar_contabilidad,contribuyente_especial,razon_social,direccion,ruc from datos_gasolinera", false);
                String ambiente = String.valueOf(model.getValueAt(0, 0));
                String sec1 = String.valueOf(model.getValueAt(0, 1));
                String nomComercial = String.valueOf(model.getValueAt(0, 2));
                String contabilidad = String.valueOf(model.getValueAt(0, 3));
                int contribuyente = Integer.parseInt(String.valueOf(model.getValueAt(0, 4)));
                String razon = String.valueOf(model.getValueAt(0, 5));
                String direccion = String.valueOf(model.getValueAt(0, 6));
                String ruc = String.valueOf(model.getValueAt(0, 7));

                model.consulta("select idpunto_emision from punto_emision where s2='" + numeros.getSelectedItem() + "'");
                int punto = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));
                model.consulta("select s2 from punto_emision where idpunto_emision=1");
                String sec2 = String.valueOf(model.getValueAt(0, 0));
                String pun = numeros.getSelectedItem().toString().replace(" ", "");
                model.consulta("SELECT CONVERT(SUBSTRING(numero,9)+1, UNSIGNED INTEGER)   FROM adv_facturacion.nota_credito,nota_credito_detalle where idnota_credito=nota_credito_idnota_credito order by numero desc limit 1;");
                 System.out.println(model.getValueAt(0, 0).toString());
                Formatter fmt = new Formatter();
                 fmt.format("%09d",Integer.parseInt(model.getValueAt(0, 0).toString()));

               String num = fmt.toString();

                System.out.println("estatus");
                model.consulta("select  cantidad,factura_detalle.subtotal,factura_detalle.total,factura_detalle.iva, producto.nombre,idproducto,punit,tipo_identificacion,idcliente,factura.usuarios_idusuarios,factura.idfactura,cliente.email,factura.metodo_pago,factura_detalle.configuracion_nmanguera\n"
                        + "from producto,factura,factura_detalle,cliente\n"
                        + "where producto_idproducto=idproducto and idfactura=factura_idfactura and numero='" + factura + "' and idcliente=cliente_idcliente");



                // num="000000453";




                System.out.println(String.valueOf(model.getValueAt(0, 0)));
                /*double cantidad = Double.parseDouble(String.valueOf(model.getValueAt(0, 0)));

                 /*
                 double subtotal; = Double.parseDouble(String.valueOf(model.getValueAt(0, 1)));
                 double tot = Double.parseDouble(String.valueOf(model.getValueAt(0, 2)));
                 double iva = Double.parseDouble(String.valueOf(model.getValueAt(0, 3)));
                 String nomProd = String.valueOf(model.getValueAt(0, 4)).toUpperCase();
                 String idProd = String.valueOf(model.getValueAt(0, 5));
                 double punit = Double.parseDouble(String.valueOf(model.getValueAt(0, 6)));
                 * */


                String tipo;
                switch ((String) model.getValueAt(0, 7)) {
                    case "cedula":
                        tipo = "05";
                        break;
                    case "ruc":
                        tipo = "04";
                        break;
                    case "pasaporte":
                        tipo = "06";
                        break;
                    case "placa":
                        tipo = "09";
                        break;
                    default:
                        tipo = null;
                        break;
                }


                nmanguera = tablaResultado.getValueAt(0, 8).toString();
                int idCliente = Integer.parseInt(String.valueOf(model.getValueAt(0, 8)));
              
                String nCliente = String.valueOf(tablaResultado.getValueAt(0, 5));
                String idFactura = String.valueOf(model.getValueAt(0, 10));
                String emailC = String.valueOf(model.getValueAt(0, 11));
                String metodos = String.valueOf(model.getValueAt(0, 12));
                int conf = Integer.parseInt(String.valueOf(model.getValueAt(0, 13)));


                 
                 
                String[] idProd = new String[tablaArticulos.getRowCount()];
                String[] nomProd = new String[tablaArticulos.getRowCount()];

                Double[] cantidad = new Double[tablaArticulos.getRowCount()];
                Double[] subt = new Double[tablaArticulos.getRowCount()];
                Double[] tots = new Double[tablaArticulos.getRowCount()];
                Double[] ivas1 = new Double[tablaArticulos.getRowCount()];
                Double[] punit = new Double[tablaArticulos.getRowCount()];
                Double subtotal = 0.00;
                Double total = 0.00;
                Double iva = 0.00;
                Double cantidadu = 0.00;

                String[] cantidades = new String[tablaArticulos.getRowCount()];
                for (int i = 0; i < tablaArticulos.getRowCount(); i++) {

                    if (String.valueOf(tablaArticulos.getValueAt(i, 0)).equalsIgnoreCase("null")) {
                   
                    } else {

                        idProd[i] = String.valueOf(tablaArticulos.getValueAt(i, 6));
                        nomProd[i] = String.valueOf(tablaArticulos.getValueAt(i, 4));

                        System.out.println("CANTIDAD " + tablaArticulos.getValueAt(i, 0));

                        cantidad[i] = Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 0)));

                        cantidadu = cantidadu + cantidad[i];

                        //cantidades[i] = String.valueOf(cantidad[i]);
                        punit[i] = Double.parseDouble(df.format((Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 5))) / 1.12)).replace(",", "."));


                        //Punit[i] = df.format(punit[i]).replace(",", "");
                        subt[i] = Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 1)));
                        subtotal = subtotal + Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 1)));

                        tots[i] = Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 2)));
                        total = total + Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 2)));


                        ivas1[i] = Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 3)));
                        iva = iva + Double.parseDouble(String.valueOf(tablaArticulos.getValueAt(i, 3)));
                    }
                    //sub[i] = df.format(subt[i]).replace(",", ".");
                }



                //creo la clave de acceso

                //System.out.println( fechaEmision);
                // System.out.println( clave);
                // System.out.println("clave nota"+fechaEmision.replace("/","")+" "+ci.getText()+" "+ambiente+" "+sec1 + sec2+" "+num);



                l.setText(sec1 + "-" + sec2 + "-" + num);


                metodosServidor estado;
                estado = (metodosServidor) Naming.lookup("rmi://" + ipservidor + "/servidor");

                int ef = estado.nota(punto, sec1, sec2, num, idCliente, metodos, idUser, subtotal, total, iva, cantidad, subt, tots, ivas1, idProd, contra, usu, punit, nomProd, fechaEmi, factura, motivo);

                if (ef == 1) {
                    JOptionPane.showMessageDialog(this, "Nota de credito Creada Corretamente ");




                    System.out.println(pun);

                    switch (pun) {
                        case "050":


                            System.out.println("entro");

                            int seleccion = JOptionPane.showOptionDialog(
                                    this,
                                    "Que Documento desea Generar ",
                                    "Nota de Credito",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null, // null para icono por defecto.
                                    new Object[]{"Factura", "Pagare", "Solo Anular Factura"}, // null para YES, NO y CANCEL
                                    "Factura");

                            if (seleccion + 1 == 1) {

                                System.out.println("seleccionada opcion factura " + (seleccion + 1));
                                model.consulta("select numero from adv_facturacion.factura where punto_emision_idpunto_emision=2 order by idfactura desc limit 1");
                                nfactura = model.getValueAt(0, 0).toString();
                                String fact[] = new String[2];
                                int num1 = 0;
                                if (String.valueOf(model.getValueAt(0, 0)).equals("")) {
                                    num1 = 1;
                                } else {
                                    fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                                    System.out.println(fact[2]);
                                    num1 = Integer.parseInt(fact[2]) + 1;
                                }


                                String nume = String.valueOf(num1);
                                String cero = "";
                                for (int i = nume.length(); i < 9; i++) {
                                    cero = cero + "0";
                                }
                                nume = cero + nume;











                                String nuevo1 = nume;

                                model.consulta("select idcliente from cliente where cedula_ruc='" + ci.getText() + "'");
                                idCliente = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));
                              

                                int ef1 = estado.factura(2, sec1, "051", nuevo1, idCliente, metodo, idUser, subtotal, total, iva, cantidad, subt, tots, ivas1, idProd, contra, usu, punit, nomProd,detalled);

                                if (ef == 1) {







                                    JOptionPane.showMessageDialog(null, "Factura Numero" + nuevo1 + " Creada Correctamente");
                                }




                            } else if (seleccion + 1 == 2) {

                                model.consulta("select idcliente from cliente where cedula_ruc='" + ci.getText() + "'");
                                idCliente = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));


                                pagare(nmanguera, total, subtotal, iva, cantidadu, fechaEmi, Hora.getText().toString(), idCliente, idUser, "");

                                System.out.println("seleccionada opcion pagare" + (seleccion + 1));


                            } else if (seleccion + 1 == 3) {
                            }




                            break;
                        case "051":

                            int seleccion1 = JOptionPane.showOptionDialog(
                                    this,
                                    "Que Documento desea Generar ",
                                    "Nota de Credito",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null, // null para icono por defecto.
                                    new Object[]{"Factura", "Solo Anular Factura"}, // null para YES, NO y CANCEL
                                    "Factura");

                            if (seleccion1 + 1 == 1) {

                                System.out.println("seleccionada opcion factura " + (seleccion1 + 1));


                                model.consulta("select numero from adv_facturacion.factura where punto_emision_idpunto_emision=2 order by idfactura desc limit 1");
                                nfactura = model.getValueAt(0, 0).toString();
                                String fact[] = new String[2];
                                int num1 = 0;
                                if (String.valueOf(model.getValueAt(0, 0)).equals("")) {
                                    num1 = 1;
                                } else {
                                    fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                                    System.out.println(fact[2]);
                                    num1 = Integer.parseInt(fact[2]) + 1;
                                }


                                String nume = String.valueOf(num1);
                                String cero = "";
                                for (int i = nume.length(); i < 9; i++) {
                                    cero = cero + "0";
                                }
                                nume = cero + nume;











                                String nuevo1 = nume;

                                model.consulta("select idcliente from cliente where cedula_ruc='" + ci.getText() + "'");
                                idCliente = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));


                                int ef1 = estado.factura(2, sec1, "051", nuevo1, idCliente, metodo, idUser, subtotal, total, iva, cantidad, subt, tots, ivas1, idProd, contra, usu, punit, nomProd,detalled);

                                if (ef == 1) {







                                    JOptionPane.showMessageDialog(null, "Factura Numero" + nuevo1 + " Creada Correctamente");
                                }




                            } else if (seleccion1 + 1 == 2) {

                                System.out.println("seleccionada opcion pagare" + (seleccion1 + 1));

                                conexion_facturacion he1 = new conexion_facturacion(usu, contra);
                                he1.conectar();

                                Statement st_in1 = he1.coneccion.createStatement();


                                ResultSet rs1 = st_in1.executeQuery("select idfactura from factura where numero='" + factura + "'");


                                System.out.println("numero" + factura);

                                if (rs1.first()) {

                                    PreparedStatement myStatement1 = he1.coneccion.prepareStatement("UPDATE `adv_facturacion`.`pagare` SET factura_idfactura='0' , facturado=0 WHERE factura_idfactura='" + rs1.getString(1) + "'");

                                    myStatement1.executeUpdate();


                                }

                                JOptionPane.showMessageDialog(this, "Pagares asignados a factura reversados correctamente");



                            }





                            break;
                        case "052":

                            
                                 int seleccion2 = JOptionPane.showOptionDialog(
                                    this,
                                    "Que Documento desea Generar ",
                                    "Nota de Credito",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null, // null para icono por defecto.
                                    new Object[]{"Factura", "Solo Anular Factura"}, // null para YES, NO y CANCEL
                                    "Factura");

                            if (seleccion2 + 1 == 1) {

                                System.out.println("seleccionada opcion factura " + (seleccion2 + 1));


                                model.consulta("select numero from adv_facturacion.factura where punto_emision_idpunto_emision=3 order by idfactura desc limit 1");
                                nfactura = model.getValueAt(0, 0).toString();
                                String fact[] = new String[2];
                                int num1 = 0;
                                if (String.valueOf(model.getValueAt(0, 0)).equals("")) {
                                    num1 = 1;
                                } else {
                                    fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                                    System.out.println(fact[2]);
                                    num1 = Integer.parseInt(fact[2]) + 1;
                                }


                                String nume = String.valueOf(num1);
                                String cero = "";
                                for (int i = nume.length(); i < 9; i++) {
                                    cero = cero + "0";
                                }
                                nume = cero + nume;











                                String nuevo1 = nume;

                                model.consulta("select idcliente from cliente where cedula_ruc='" + ci.getText() + "'");
                                idCliente = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));


                                int ef1 = estado.factura(3, sec1, "052", nuevo1, idCliente, metodo, idUser, subtotal, total, iva, cantidad, subt, tots, ivas1, idProd, contra, usu, punit, nomProd,detalled);

                                if (ef == 1) {







                                    JOptionPane.showMessageDialog(null, "Factura Numero" + nuevo1 + " Creada Correctamente");
                                }




                            } else if (seleccion2 + 1 == 2) {

                               


                            }

 
                            
                            
                            
                            break;



                    }

                    numero.setText("");
                    numero.setEnabled(true);
                    nombre.setText("");
                    ci.setText("");
                    limpiarTabla(tablaResultado);
                    limpiarTabla(tablaArticulos);




                } else {

                    JOptionPane.showMessageDialog(this, "Hubo un error en la creacio de la factura  ");


                }


                System.out.println(notaCredito + "   " + String.valueOf(subt));





            } catch (SQLException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            JOptionPane.showMessageDialog(this, "debe ingresar todos los datos");
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        numero.setText("");
        numero.setEnabled(true);
        nombre.setText("");
        ci.setText("");
        limpiarTabla(tablaResultado);
        limpiarTabla(tablaArticulos);
// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
    DecimalFormat df = new DecimalFormat("#.######");
    DecimalFormat dc = new DecimalFormat("#.######");
    DecimalFormat dos = new DecimalFormat("#.##");

    private void tablaArticulosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaArticulosKeyTyped
    }//GEN-LAST:event_tablaArticulosKeyTyped

    private void tablaArticulosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaArticulosKeyPressed
        String k = String.valueOf(evt.getKeyCode());
        double cant = 0;
        DefaultTableModel Tabla = (DefaultTableModel) tablaArticulos.getModel();
        double aux = 0;
        double canti = 0;
        double precio = 0;
        int fila = tablaArticulos.getSelectedRow();
        int col = tablaArticulos.getSelectedColumn();
        String nom = String.valueOf(Tabla.getValueAt(fila, 1));
        try {
            if (k.equals("127")) {
                Tabla.removeRow(fila);
                //calculos(Tabla);
            }

        } catch (Exception ex) {

            Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);


//JOptionPane.showMessageDialog(this, "el dato ingresado es incorrecto vuelva a ingrese nuevamente la cantidad  ");
//Tabla.setValueAt(1,fila,2);
//calculos(Tabla);
        }
    }//GEN-LAST:event_tablaArticulosKeyPressed

    public void limpiarTabla(JTable tabla) {
        //me borra todo lo que esta en la tabla para la nueva escritura
        DefaultTableModel TablaC = (DefaultTableModel) tabla.getModel();
        TableColumn column = null;
        for (int i = 0; i < tabla.getRowCount(); i++) {
            TablaC.removeRow(i);
            i -= 1;
        }

        for (int i = 0; i < 3; i++) {
            column = tabla.getColumnModel().getColumn(i);
            if (i == 0 || i == 3) {
                column.setPreferredWidth(230);
            } else {
                column.setPreferredWidth(100);
            }
        }

    }

    public void busqueda(String cadena, String columna) {
        //metodo para realizar las busquedas y que me muestren en las tablas
        if (cadena.length() > 4) {
            limpiarTabla(tablaCliente);
            try {
                model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT nombre,cedula_ruc,telefono,email,credito_cliente FROM cliente WHERE " + columna + " LIKE'" + cadena + "%' limit 20", false);


            } catch (SQLException ex) {
                Logger.getLogger(buscarFacturaGasolina.class.getName()).log(Level.SEVERE, null, ex);

            }
            DefaultTableModel TablaC = (DefaultTableModel) tablaCliente.getModel();

            String nom, cedula, telefono, mail;

            int credito;
            filas = model.getRowCount();
            int i = 0, j = 0;
            while (filas > 0) {
                nom = ((String) model.getValueAt(i, 0));
                cedula = ((String) model.getValueAt(i, 1));
                telefono = ((String) model.getValueAt(i, 2));
                mail = ((String) model.getValueAt(i, 3));

                TablaC.addRow(new Object[]{nom, cedula, telefono, mail});

                filas--;
                i++;

            }


        }

    }
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Hora;
    private javax.swing.JTextField ci;
    private javax.swing.JTextField hoy;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel l;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField numero;
    private javax.swing.JComboBox numeros;
    private javax.swing.JTable tablaArticulos;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTable tablaResultado;
    // End of variables declaration//GEN-END:variables

    public void tomarHora() {
        Calendar calendario = new GregorianCalendar();
        Date fechaHoraActual = new Date();
        calendario.setTime(fechaHoraActual);

        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);

        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }

    @Override
    public void run() {
        Thread ct = Thread.currentThread();

        while (ct == h1) {
            tomarHora();

            Hora.setText(hora + ":" + minutos + ":" + segundos);



            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
    String rz = "", np = "", d = "", r = "", na = "", clientr = "", tcliente = "", producto = "";
    int idpagare = 0;

    //  pagare(nmanguera,total,subtotal,iva,cantidadu,fechaEmi,Hora.getText().toString(),idCliente,idUser,"");
    private void pagare(String nmanguera, double tot, double subt, double iva, double cantidad, String fechaEmi, String hora, int idCliente, int idUser, String placa) {

        try {
            conexion_facturacion n = new conexion_facturacion(usu, contra);
            n.conectar();
            Statement st_in = n.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey'),mantenimiento FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");

            while (ri.next()) {

                rz = ri.getString(2);
                np = ri.getString(5);
                d = ri.getString(4);
                r = ri.getString(3);


            }


            String punit = null;


            Statement st_im1 = n.coneccion.createStatement();
            ResultSet ridim1 = st_im1.executeQuery("SELECT punit,nombre FROM adv_facturacion.configuracion,adv_facturacion.producto where producto_idproducto=idproducto and nmanguera=" + nmanguera + "");

            while (ridim1.next()) {


                punit= ridim1.getString(1);
                producto = ridim1.getString(2);

            }
            Statement st_n = n.coneccion.createStatement();
            ResultSet rid_n = st_n.executeQuery("SHOW TABLE STATUS FROM adv_facturacion LIKE 'pagare'");


            while (rid_n.next()) {

                idpagare = rid_n.getInt(11);
            }






            String impresora = "";
            Font fuente;
            FileWriter file = null;
            file = new FileWriter("ticket" + nmanguera + ".txt");

            BufferedWriter buffer = new BufferedWriter(file);
            PrintWriter ps = new PrintWriter(buffer);




            char[] ESC_CUT_PAPER = new char[]{0x1B, '!', (char) 0};
            ps.write(ESC_CUT_PAPER);



            ESC_CUT_PAPER = new char[]{0x1B, '!', (char) 24};
            ps.write(ESC_CUT_PAPER);

            ps.println(rz.replace("ñ", "n"));

            ESC_CUT_PAPER = new char[]{0x1B, '!', (char) 1};
            ps.write(ESC_CUT_PAPER);
            ps.println("Pagare Numero:" + idpagare);
            ps.println("FECHA: " + fechaEmi);
            ps.println("HORA: " + hora);
            ps.println("RUC/CED:");
            ps.println(ci.getText());
            ps.println("MANGUERA: " + nmanguera);
            ps.println("CANTIDAD: " + cantidad);
            ps.println("PRODUCTO:  " + producto);
            ps.println("VALOR TOTAL: " + tot + " DOLARES");
            ps.println("-------------------------------");
            ps.println("YO: ");
            ps.println(nombre.getText());
            ps.println("DEBO Y PAGARE  ");
            ps.println("en esta ciudad a la orden de:");
            ps.println(rz);
            ps.println("la cantidad de");
            ps.println(tot + " Dolares Americanos");
            ps.println("Por concepto de Venta de ");
            ps.println("Combustible:" + producto);
            ps.println("Me someto a los jueces o");
            ps.println("tribunales competentes de la ciudad ");
            ps.println("y renuncio a fuero y domicilio");
            ps.println("y a la via ejecutiva o verbal");
            ps.println("sumaria a eleccion de");
            ps.println(rz);
            ps.println("                      ");
            ps.println("                      ");
            ps.println("----------------------");
            ps.println("Firma Cliente");
            ps.println("                      ");
            ps.println("----------------------");
            ps.println("Cedula Cliente");
            ps.println("Placa Vehiculo " + placa);
            ps.println("                       ");
            ps.close();


            System.out.println("idu " + idUser);


            FileInputStream inputStream1 = new FileInputStream("ticket" + nmanguera + ".txt");










            PreparedStatement myStatement = n.coneccion.prepareStatement("INSERT INTO `adv_facturacion`.`pagare` (`pagare`,`facturado`, `factura_idfactura`, `cliente_idcliente`, `usuarios_idusuarios`,`configuracion_nmanguera`,`total`,`subtotal`,`iva`,`cantidad`,`fecha`,`hora`,`placa`,`punit`,`despacho`) VALUES(?,0,'0','" + idCliente + "','" + idUser + "','" + nmanguera + "','" + tot + "','" + subt + "','" + iva + "','" + cantidad + "','" + fechaEmi + "','" + hora + "','" + placa + "','" + punit + "','1')");


            myStatement.setBinaryStream(1, inputStream1, inputStream1.available());
            myStatement.executeUpdate();


            JOptionPane.showMessageDialog(this, "PAGARE Numero " + idpagare + " creado correctamente ");


            n.coneccion.close();


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(crear_pagare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(crear_pagare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(crear_pagare.class.getName()).log(Level.SEVERE, null, ex);
        }



    }
}
