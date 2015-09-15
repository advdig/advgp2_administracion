package Principal;

import conexion.ConnectionTableDB;
import conexion.conexion_facturacion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import sockets.metodosServidor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Adv
 */
public class prepagoGasolina extends javax.swing.JFrame implements Runnable {

    String[] detalled = new String[20];
    public String usu = "", contra = "", ip, vende = "";
    ConnectionTableDB model;
    conexion_facturacion he;
    String hora, minutos, segundos, ampm;
    Calendar calendario;
    Thread h1;
    public int filas = 0;
    public int cantidad = 1;
    public double sub = 0;
    String local = "001";
    public String ipservidor = "";
    // tablaArticulos a=new tablaArticulos("root","manager","factura");
    DecimalFormat df = new DecimalFormat("#.######");
    DecimalFormat dc = new DecimalFormat("#.######");
    DecimalFormat dos = new DecimalFormat("#.##");
    String secuencial = "";
    conexion_facturacion modelo;
    private InputStream PKCS12_RESOURCE;
    private String PKCS12_PASSWORD;
    private String Ewsri, Awsri;
    /**
     *
     * Creates new form venderAceites
     */
    Calendar c = Calendar.getInstance();

    public prepagoGasolina(String user, String password, String vend, String codvend) {
        //usuario y contrasena para la base de datos

        System.out.println(vend);

        usu = user;
        contra = password;
        vende = vend;

        String dia, mes, ano;
        initComponents();

        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", "", false);
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        h1 = new Thread(this);
        h1.start();
        //proceso para sacar la ip del servidor
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
            String linea, linea1 = "", linea2 = "";
            int i = 0;
            while ((linea = br.readLine()) != null) {
                if (i == 1) {
                    linea1 = linea;
                }
                if (i == 2) {
                    linea2 = linea;
                }
                i++;
            }
            String[] ips = new String[1];
            ips = linea1.split(":");
            String[] ipsa = new String[1];
            ipsa = linea2.split(":");

            ip = ipsa[1];
            //aqui esta la ip del servidor sacada del archivo
            ip = ips[1];
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

        descuentos.setText(0 + "");
        this.setExtendedState(this.MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.white);

        facturar.setBackground(Color.orange);
        String impresora = "";

        //agrefo el numero a la factura
        try {
            model.consulta("SELECT numero FROM factura where punto_emision_idpunto_emision=2 ORDER BY idfactura DESC LIMIT 1 ");
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!String.valueOf(model.getValueAt(0, 0)).equals("")) {
            String fact[] = new String[2];
            fact = String.valueOf(model.getValueAt(0, 0)).split("-");
            int num = Integer.parseInt(fact[2]) + 1;
            String isla = "";

            try {
                model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                local = String.valueOf(model.getValueAt(0, 0));
                model.consulta( "SELECT s2 FROM punto_emision WHERE idpunto_emision = 2 ");
                isla = String.valueOf(model.getValueAt(0, 0));

            } catch (SQLException ex) {
                Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
            }

            String nume = String.valueOf(num);
            String cero = "";
            for (int i = nume.length(); i < 9; i++) {
                cero = cero + "0";
            }
            nume = cero + nume;
            emision.setText(isla);
            factura.setText(local + "-" + isla + "-" + nume);
        } else {
            String fact[] = new String[2];
            fact = String.valueOf(model.getValueAt(0, 0)).split("-");
            int num = 1;
            String isla = "";

            try {
               model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                local = String.valueOf(model.getValueAt(0, 0));
                model.consulta("SELECT s2 FROM punto_emision WHERE idpunto_emision = 2 ");
                isla = String.valueOf(model.getValueAt(0, 0));

            } catch (SQLException ex) {
                Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
            }

            String nume = String.valueOf(num);
            String cero = "";
            for (int i = nume.length(); i < 9; i++) {
                cero = cero + "0";
            }
            nume = cero + nume;
            emision.setText(isla);
            factura.setText(local + "-" + isla + "-" + nume);

        }

        try {
           model.consulta( "SELECT idproducto,nombre from producto ");
            //String[] la=model.getValueAt(0, 0).toString().split("");

            for (int i = 0; i < model.getRowCount(); i++) {
                lista.addItem(model.getValueAt(i, 0) + "           " + model.getValueAt(i, 1).toString().toUpperCase());
            }

        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }

//agrego la fecha y la hora actual del sistema
        String hora, minutos, segundos;

        hora = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        minutos = Integer.toString(c.get(Calendar.MINUTE));
        segundos = Integer.toString(c.get(Calendar.SECOND));
        dia = Integer.toString(c.get(Calendar.DATE));
        mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        ano = Integer.toString(c.get(Calendar.YEAR));
        if (Integer.parseInt(minutos) < 10) {
            minutos = "0" + minutos;
        }
        if (Integer.parseInt(segundos) < 10) {
            segundos = "0" + segundos;
        }

        Fecha.setText(ano + "-" + mes + "-" + dia);
        Hora.setText(hora + ":" + minutos + ":" + segundos);

        try {
            model.consulta( "SELECT nombre FROM usuarios WHERE usuario='" + vend + "'");
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }
        vendedor.setText(String.valueOf(model.getValueAt(0, 0)));

        total.setEnabled(true);
        total.setEditable(false);
       
        

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
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        derecho = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        factura = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        ci = new javax.swing.JTextField();
        Hora = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        Fecha = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaArticulo = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        subtotal = new javax.swing.JTextField();
        iva = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        emision = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaCliente = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        correo = new javax.swing.JTextField();
        facturar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        vendedor = new javax.swing.JTextField();
        metodoPago = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        netos = new javax.swing.JTextField();
        descuentos = new javax.swing.JTextField();
        lista = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable2);

        derecho.setText("quitar");
        derecho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                derechoActionPerformed(evt);
            }
        });
        jPopupMenu1.add(derecho);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Prepago Gasolina");
        setBackground(javax.swing.UIManager.getDefaults().getColor("window"));
        setForeground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Factura Nro.");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 14, -1, -1));

        factura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        factura.setEnabled(false);
        getContentPane().add(factura, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 11, 146, -1));

        jLabel6.setText("Fecha");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 14, -1, -1));

        lblHora.setText("Hora");
        getContentPane().add(lblHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 40, -1, -1));

        jLabel8.setText("CI/RUC");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 40, -1, -1));

        jLabel9.setText("Nombre");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 71, -1, -1));

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
        getContentPane().add(ci, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 37, 146, -1));

        Hora.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Hora.setEnabled(false);
        getContentPane().add(Hora, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 42, 109, -1));

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
        getContentPane().add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 68, 241, -1));

        Fecha.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        Fecha.setEnabled(false);
        getContentPane().add(Fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 11, 109, -1));

        tablaArticulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "codigo", "descripcion", "cantidad", "P/U", "subtotal", "totales ", "IVA", "desc %"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaArticulo.setComponentPopupMenu(jPopupMenu1);
        tablaArticulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablaArticuloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                tablaArticuloMouseExited(evt);
            }
        });
        tablaArticulo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tablaArticuloFocusLost(evt);
            }
        });
        tablaArticulo.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                tablaArticuloCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        tablaArticulo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaArticuloKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaArticulo);
        if (tablaArticulo.getColumnModel().getColumnCount() > 0) {
            tablaArticulo.getColumnModel().getColumn(0).setPreferredWidth(80);
            tablaArticulo.getColumnModel().getColumn(1).setPreferredWidth(400);
            tablaArticulo.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaArticulo.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaArticulo.getColumnModel().getColumn(4).setPreferredWidth(100);
            tablaArticulo.getColumnModel().getColumn(5).setPreferredWidth(100);
            tablaArticulo.getColumnModel().getColumn(6).setPreferredWidth(100);
            tablaArticulo.getColumnModel().getColumn(7).setPreferredWidth(100);
        }

        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 160, 570, 308));

        jLabel10.setText("SUBTOTAL PVP");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 490, -1, -1));

        jLabel11.setText("IVA 12%");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 590, -1, -1));

        jLabel12.setText("TOTAL");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 630, -1, -1));

        subtotal.setEditable(false);
        getContentPane().add(subtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 550, 70, -1));

        iva.setEditable(false);
        getContentPane().add(iva, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 590, 70, -1));

        total.setEditable(false);
        getContentPane().add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 630, 70, -1));

        jLabel2.setText("PtoEmsion");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        emision.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        emision.setEnabled(false);
        getContentPane().add(emision, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 10, 36, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel4.setText("Articulos");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 130, -1, -1));

        tablaCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombres", "CI/RUC"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaCliente.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
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
        if (tablaCliente.getColumnModel().getColumnCount() > 0) {
            tablaCliente.getColumnModel().getColumn(0).setPreferredWidth(175);
            tablaCliente.getColumnModel().getColumn(1).setPreferredWidth(110);
        }

        getContentPane().add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 420, 440));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 24)); // NOI18N
        jLabel5.setText("Clientes");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, -1, -1));

        jLabel13.setText("Telefono");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        telefono.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        telefono.setEnabled(false);
        getContentPane().add(telefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 102, 144, -1));

        jLabel14.setText("Correo");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(34, 129, -1, -1));

        correo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        correo.setEnabled(false);
        getContentPane().add(correo, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 133, 193, -1));

        facturar.setBackground(java.awt.Color.orange);
        facturar.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        facturar.setText("Facturar");
        facturar.setEnabled(false);
        facturar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facturarActionPerformed(evt);
            }
        });
        getContentPane().add(facturar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 570, 125, 53));

        jLabel15.setText("Vendedor");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(364, 71, -1, -1));

        vendedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        vendedor.setEnabled(false);
        getContentPane().add(vendedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 68, 147, -1));

        metodoPago.setBackground(new java.awt.Color(255, 204, 0));
        metodoPago.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        metodoPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "Tarjeta de Credito", "Credito" }));
        metodoPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoPagoActionPerformed(evt);
            }
        });
        getContentPane().add(metodoPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 146, 35));

        jLabel18.setText("Subtotal Neto");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 560, -1, -1));

        jLabel19.setText("Descuento");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 520, -1, -1));

        jLabel22.setBackground(new java.awt.Color(255, 255, 51));
        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 0));
        jLabel22.setText("Forma de Pago:");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 490, -1, -1));

        netos.setEditable(false);
        getContentPane().add(netos, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 480, 70, -1));

        descuentos.setEditable(false);
        descuentos.setText("0");
        getContentPane().add(descuentos, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 520, 70, -1));

        lista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaActionPerformed(evt);
            }
        });
        getContentPane().add(lista, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 130, 240, -1));

        jButton1.setText("AGREGAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ciActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ciActionPerformed
        //me realiza el ingreso de los datos directamento por ci  
        String consulta = "SELECT nombre,telefono,email FROM cliente WHERE cedula_ruc='" + ci.getText() + "'";
        try {
            model.consulta( consulta);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

        }
        nombre.setText((String) model.getValueAt(0, 0));
        telefono.setText((String) model.getValueAt(0, 1));
        correo.setText((String) model.getValueAt(0, 2));

        facturar.setEnabled(true);

// TODO add your handling code here:        // TODO add your handling code here:
    }//GEN-LAST:event_ciActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //me desconecta de la base solo si esta todo completo
        if (JOptionPane.showConfirmDialog(this, "seguro que desea salir de factura") == 0) {
            if (model.connected == true) {
                model.desconectar();
            }
            this.dispose();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
    }//GEN-LAST:event_nombreActionPerformed
    public void limpiarTabla(JTable tabla) {
        //me borra todo lo que esta en la tabla para la nueva escritura
        DefaultTableModel TablaC = (DefaultTableModel) tabla.getModel();
        TableColumn column = null;
        for (int i = 0; i < tabla.getRowCount(); i++) {
            TablaC.removeRow(i);
            i -= 1;
        }

        for (int i = 0; i < 2; i++) {
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
                model.consulta( "SELECT nombre,cedula_ruc,email,credito_cliente FROM cliente WHERE " + columna + " LIKE'" + cadena + "%' limit 20");

            } catch (SQLException ex) {
                Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);

            }
            DefaultTableModel TablaC = (DefaultTableModel) tablaCliente.getModel();

            String nom, cedula, telefono, mail;

            int credito;
            filas = model.getRowCount();
            int i = 0, j = 0;
            while (filas > 0) {
                nom = ((String) model.getValueAt(i, 0));
                cedula = ((String) model.getValueAt(i, 1));
                //mail = ((String) model.getValueAt(i, 3));

                TablaC.addRow(new Object[]{nom, cedula});

                filas--;
                i++;

            }

        }

    }

    private void nombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreKeyTyped
        char c = evt.getKeyChar();
        int filas;
        //realizo las busquedas de los clientes segun lo queescriba en el nombre

        busqueda(nombre.getText(), "nombre");
// TODO add your handling code here:
    }//GEN-LAST:event_nombreKeyTyped

    private void ciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ciKeyTyped
        //hago las busquedas por ci
        char c = evt.getKeyChar();
        int filas;
        busqueda(ci.getText(), "cedula_ruc");

        // TODO add your handling code here:
    }//GEN-LAST:event_ciKeyTyped

    private void tablaClienteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseClicked

        int filas = tablaCliente.getSelectedRow();
        nombre.setText((String) tablaCliente.getValueAt(filas, 0));
        ci.setText((String) tablaCliente.getValueAt(filas, 1));
        try {
            model.consulta("select telefono,email from cliente where cedula_ruc='"+ci.getText()+"'");
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }
        telefono.setText(String.valueOf(model.getValueAt(0, 0)));
        correo.setText(String.valueOf(model.getValueAt(0, 1)));
        //cada vez que selecciono un cliente de la tabla me pasa los datos a los campos
        facturar.setEnabled(true);
       
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseClicked

    int s2;
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


    private void facturarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facturarActionPerformed

        if (ci.getText().length() == 10 | ci.getText().length() == 13) {
            DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();
            he = new conexion_facturacion(usu, contra);
            try {
                he.conectar();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (Tabla.getRowCount() != 0) {

                try {

                    String fechaEmision = Fecha.getText();
                    String f[];
                    f = fechaEmision.split("-");
                    if (Integer.parseInt(f[2]) < 10) {
                        f[2] = "0" + f[2];
                    }
                    if (Integer.parseInt(f[1]) < 10) {
                        f[1] = "0" + f[1];
                    }
                    fechaEmision = f[2] + "/" + f[1] + "/" + f[0];
                    String fechaEmi = f[0] + "-" + f[1] + "-" + f[2];

                    double subtot = Double.parseDouble(subtotal.getText());
                    double tot = Double.parseDouble(total.getText());
                    double iv = Double.parseDouble(iva.getText());
                    String cedu = ci.getText();
                    String fecha = Fecha.getText();
                    String hora = Hora.getText();
                    String metodo = String.valueOf(metodoPago.getSelectedItem());
                    String nombreUsu = vendedor.getText();

                    String isla = "";

                    try {
                        model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                        local = String.valueOf(model.getValueAt(0, 0));
                        isla = emision.getText();

                    } catch (SQLException ex) {
                        Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String[] idProd = new String[tablaArticulo.getRowCount()];
                    String[] nomProd = new String[tablaArticulo.getRowCount()];

                    Double[] cantidad = new Double[tablaArticulo.getRowCount()];
                    Double[] subt = new Double[tablaArticulo.getRowCount()];
                    Double[] tots = new Double[tablaArticulo.getRowCount()];
                    Double[] ivas1 = new Double[tablaArticulo.getRowCount()];
                    Double[] punit = new Double[tablaArticulo.getRowCount()];
                    String[] Punit = new String[tablaArticulo.getRowCount()];
                    String[] sub = new String[tablaArticulo.getRowCount()];

                    String[] cantidades = new String[tablaArticulo.getRowCount()];
                    for (int i = 0; i < tablaArticulo.getRowCount(); i++) {
                        idProd[i] = String.valueOf(tablaArticulo.getValueAt(i, 0));
                        nomProd[i] = String.valueOf(tablaArticulo.getValueAt(i, 1));
                        cantidad[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 2)));
                        cantidades[i] = String.valueOf(cantidad[i]);
                       
                        if(iv==0){
                            
                            punit[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 3)));
                           Punit[i] = String.valueOf(punit[i]).replace(",", ".");
                           System.out.println("sin iva ");
                        
                        }else
                        
                        {
                        punit[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 3)))/1.12;
                        Punit[i] = String.valueOf(punit[i]).replace(",", ".");
                        }
                        
                        subt[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 4)));
                        tots[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 5)));
                        ivas1[i] = Double.parseDouble(String.valueOf(tablaArticulo.getValueAt(i, 6)));
                        sub[i] = df.format(subt[i]).replace(",", ".");

                        System.out.println(Punit[i]);

                    }

                  
                    
                    
                    String sec1 = local;
                    String sec2 = isla;
                    String tipo;
                    String nuevo[] = factura.getText().split("-");
                    String nuevo1 = nuevo[2];
                    //nuevo1="000000097";
                    System.out.println(sec1 + "-" + sec2 + "-" + nuevo1);

                    //  nuevo1="000005663";
                    model.consulta("SELECT ruc,tipo_ambiente,nombre_comercial,obligado_llevar_contabilidad,contribuyente_especial,razon_social,direccion  from datos_gasolinera where iddatos_gasolinera=1");
                    String ruc = String.valueOf(model.getValueAt(0, 0));
                    String ambiente = String.valueOf(model.getValueAt(0, 1));
                    String nomComercial = String.valueOf(model.getValueAt(0, 2));
                    String contabilidad = String.valueOf(model.getValueAt(0, 3));
                    int contribuyente = Integer.parseInt(String.valueOf(model.getValueAt(0, 4)));
                    String razon = String.valueOf(model.getValueAt(0, 5));
                    String direccion = String.valueOf(model.getValueAt(0, 6));

                    model.consulta("SELECT tipo_identificacion from cliente where cedula_ruc='" + cedu + "'");
                    String ti = String.valueOf(model.getValueAt(0, 0));
                    switch (ti) {
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

                    model.consulta("select idcliente from adv_facturacion.cliente where cedula_ruc='" + ci.getText() + "'");

                    int idc = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));

                    model.consulta("SELECT idusuarios from usuarios where usuario='" + usu + "'");
                    int idu = Integer.parseInt(String.valueOf(model.getValueAt(0, 0)));

                    String ci1 = ci.getText();
                    String nombre1 = nombre.getText();

                    try {
                        metodosServidor estado;
                        estado = (metodosServidor) Naming.lookup("rmi://" + ip + "/servidor");

                        int ef = estado.factura(s2, sec1, sec2, nuevo1, idc, metodo, idu, subtot, tot, iv, cantidad, subt, tots, ivas1, idProd, contra, usu, punit, nomProd, detalled);

                        if (ef == 1) {

                            System.out.println(detalled[0]);

                            JOptionPane.showMessageDialog(null, "Factura Numero" + nuevo1 + " Creada Correctamente");

                            this.dispose();

                        } else {

                            JOptionPane.showMessageDialog(null, "Hubo un error en la creacion de la factura");

                        }

                    } catch (RemoteException ex) {
                        Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (NotBoundException ex) {
                        Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {
                JOptionPane.showMessageDialog(this, "no hay datos en la factura");
            }

        } else {
            JOptionPane.showMessageDialog(this, "la cedula es incorrecta");
        }
// TODO add your handling code here:
    }//GEN-LAST:event_facturarActionPerformed
    public static void calculos(DefaultTableModel Tabla, int t) {

        if (t == 1) {
            double totales = 0;
            double subt = 0;
            double subtotales = 0;
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                double desc = Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 7)));
                totales = (Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 2))) * (Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 3)))));
                BigDecimal p = new BigDecimal(totales );
                p = p.setScale(2, RoundingMode.HALF_UP);
                Tabla.setValueAt(p, i, 5);
                p = new BigDecimal(totales -(totales/1.12));
                p = p.setScale(2, RoundingMode.HALF_UP);
                Tabla.setValueAt(p, i, 6);
                subt += totales;
                subtotales = subtotales + Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 4)));
            }
            BigDecimal p = new BigDecimal(subtotales);
            p = p.setScale(2, RoundingMode.HALF_UP);
            subtotal.setText(p + "");

            netos.setText(Double.parseDouble(subtotal.getText()) - Double.parseDouble(descuentos.getText()) + "");
            p = new BigDecimal((subt-(subt/1.12)));
            p = p.setScale(2, RoundingMode.HALF_UP);
            iva.setText(p + "");
            p = new BigDecimal(subt);
            p = p.setScale(2, RoundingMode.HALF_UP);
            total.setText(p + "");
        } else {

            double totales = 0;
            double subt = 0;
            double subtotales = 0;
            for (int i = 0; i < Tabla.getRowCount(); i++) {
                
                double desc = Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 7)));

                totales = (Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 2))) * (Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 3)))));
                System.out.println(Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 2))));
                System.out.println(Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 3))));
                System.out.println(totales);

                BigDecimal p = new BigDecimal(totales);
                System.out.println(p);
                p = p.setScale(2, RoundingMode.HALF_UP);

                Tabla.setValueAt(p, i, 4);
                
                System.out.println("subtotal"+p);
                
                
                Tabla.setValueAt(p, i, 5);
                p = new BigDecimal(totales);
                p = p.setScale(2, RoundingMode.HALF_UP);
              
              
                
                Tabla.setValueAt(0.00, i, 6);
                subt += totales;
                subtotales = subtotales + Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 4)));
            
            
            }
            BigDecimal p = new BigDecimal(subtotales);
            p = p.setScale(2, RoundingMode.HALF_UP);
            subtotal.setText(p + "");

            netos.setText(Double.parseDouble(subtotal.getText()) - Double.parseDouble(descuentos.getText()) + "");
            p = new BigDecimal(Double.parseDouble(subtotal.getText()));
            p = p.setScale(2, RoundingMode.HALF_UP);
            iva.setText("0.00");
            p = new BigDecimal(totales + (Double.parseDouble(iva.getText())));
            p = p.setScale(2, RoundingMode.HALF_UP);
            total.setText(Double.parseDouble(subtotal.getText()) - Double.parseDouble(descuentos.getText()) + "");

        }
    }

    private void tablaArticuloKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaArticuloKeyPressed

        String k = String.valueOf(evt.getKeyCode());
        double cant = 0;
        DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();
        double aux = 0;
        double canti = 0;
        double precio = 0;
        int fila = tablaArticulo.getSelectedRow();
        int col = tablaArticulo.getSelectedColumn();

        try {

            model.consulta( "SELECT nombre,punit,idproducto,tipo_producto FROM producto,tipo_producto  WHERE tipo_producto_idtipo_producto=idtipo_producto and idproducto='" + Tabla.getValueAt(fila, 0) + "'");
                    String tp = String.valueOf(model.getValueAt(0, 3));

            
            
            if (k.equals("10") | k.equals("37") | k.equals("38") | k.equals("39") | k.equals("40")) {

                aux = Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 2)));
                double desc = Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 7)));

                BigDecimal p = new BigDecimal((Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 2))) * (Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 3))))) - ((Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 2))) * (Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 3))))) * (desc / 100)));
                p = p.setScale(6, RoundingMode.HALF_UP);
                canti = Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 2)));
                precio = Double.parseDouble(String.valueOf(Tabla.getValueAt(fila, 3)));
                precio = canti * precio;
                if (desc >= 0 && desc <= 100) {
                   
                    
                    System.out.println(tp);

                    if ((tp.equalsIgnoreCase("Transporte"))) {

                     Tabla.setValueAt(p, fila, 5);
                    Tabla.setValueAt(dos.format(precio).replace(",", "."), fila, 4);
                    Tabla.setValueAt(dos.format(precio).replace(",", "."), fila, 6);
                    
                    calculos(Tabla, 0);
    
                        
                    }else{
                     Tabla.setValueAt(p, fila, 5);
                    Tabla.setValueAt(dos.format(precio/1.12).replace(",", "."), fila, 4);
                    Tabla.setValueAt(dos.format(precio -(precio/1.12)).replace(",", "."), fila, 6);
                    
                    calculos(Tabla, 1);

                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this, "el dato ingresado es incorrecto vuelva a ingrese nuevamente la cantidad  ");
                    Tabla.setValueAt(1, fila, 2);
            
                     if ((tp.equalsIgnoreCase("Transporte"))) {
                     
                     calculos(Tabla, 0);
                     }else
                     {calculos(Tabla, 1);
                     }
                     }
            } else {
                if (k.equals("127")) {
                    Tabla.removeRow(fila);
                     if ((tp.equalsIgnoreCase("Transporte"))) {
                     
                     calculos(Tabla, 0);
                     }else
                     {calculos(Tabla, 1);
                     }
                } else {
                    if (col == 2) {
                        Tabla.setValueAt("", fila, 2);
                    }
                }

            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "el dato ingresado es incorrecto vuelva a ingrese nuevamente la cantidad  ");
            Tabla.setValueAt(1, fila, 2);
            calculos(Tabla, 1);
        }// TODO add your handling code here:
    }//GEN-LAST:event_tablaArticuloKeyPressed

    private void derechoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_derechoActionPerformed
        try {
            int filas = tablaArticulo.getSelectedRow();
            int cant = Integer.parseInt(String.valueOf(tablaArticulo.getValueAt(filas, 1)));
            DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();
            int menos = Integer.parseInt(JOptionPane.showInputDialog("ingrese lo que desea quitar"));

            if (cant - menos == 0) {
                Tabla.removeRow(filas);
            } else {
                Tabla.setValueAt((cant - menos), filas, 1);
            }
            calculos(Tabla, 1);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "debe seleccionar un articulo o el valor ingresado es incorrecto");
        }        // TODO add your handling code here:
    }//GEN-LAST:event_derechoActionPerformed

    private void tablaClienteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseEntered
        this.setCursor(Cursor.HAND_CURSOR);        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseEntered

    private void tablaClienteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClienteMouseExited
        this.setCursor(Cursor.DEFAULT_CURSOR);        // TODO add your handling code here:
    }//GEN-LAST:event_tablaClienteMouseExited

    private void tablaArticuloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaArticuloMouseEntered
        this.setCursor(Cursor.HAND_CURSOR);        // TODO add your handling code here:
    }//GEN-LAST:event_tablaArticuloMouseEntered

    private void tablaArticuloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaArticuloMouseExited
        this.setCursor(Cursor.DEFAULT_CURSOR); // TODO add your handling code here:
    }//GEN-LAST:event_tablaArticuloMouseExited

    private void metodoPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoPagoActionPerformed

    private void listaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaActionPerformed
        String[] a = new String[1];
        a = lista.getSelectedItem().toString().split("           ");
        String res = a[1];
        if (res.equalsIgnoreCase("OTROS")) {

        } else {

        }        // TODO add your handling code here:
    }//GEN-LAST:event_listaActionPerformed


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setBackground(Color.decode("#F7BE81"));
        String q1 = lista.getSelectedItem().toString();
        String q[] = q1.split("           ");
        System.out.println(q[0]);

        int j = 0;
        try {

            String tp = "";
            int cod = Integer.parseInt(q[0]);
            double precio = 0, precioU = 0;
            String descripcion = "";
            //tomo los datos de la base
            //saco la descripcion
            try {
               model.consulta("SELECT nombre,punit,idproducto,tipo_producto FROM producto,tipo_producto  WHERE tipo_producto_idtipo_producto=idtipo_producto and idproducto='" + q[0] + "'");

            } catch (SQLException ex) {
                System.err.println(ex.getMessage());

            } catch (java.lang.NumberFormatException ex) {
                System.err.println("Articulo no encontrado\n" + ex.getMessage());

            }
            if (model.getRowCount() != 0) {

                descripcion = ((String) model.getValueAt(0, 0));
            }
            //saco el precio
            if (descripcion != "") {

                tp = String.valueOf(model.getValueAt(0, 3));

                System.out.println(tp);

                if (tp.equalsIgnoreCase("Servicios") || (tp.equalsIgnoreCase("Transporte"))) {

                    String seleccion = JOptionPane.showInputDialog(this, "Ingrese Un detalle Adicional", JOptionPane.QUESTION_MESSAGE);

                    System.out.println(tablaArticulo.getRowCount());
                    detalled[tablaArticulo.getRowCount()] = seleccion;

                    try {
                        emision.setText("053");

                        model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT numero FROM factura,punto_emision where idpunto_emision=punto_emision_idpunto_emision and s2='053'  ORDER BY idfactura DESC LIMIT 1 ", false);
                        System.out.println(model.getValueAt(0, 0));

                    } catch (Exception ex) {
                        Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (String.valueOf(model.getValueAt(0, 0)).length() == 0) {

                        String fact[] = new String[2];

                        //System.out.println("entro1");
                        int num = 1;
                        String isla = "";
                        try {

                          model.consulta ("SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                            model.consulta( "SELECT s2 FROM punto_emision WHERE  s2='053'  ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);
                    } else {
                        String fact[] = new String[2];
                        fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                        int num = Integer.parseInt(fact[2]) + 1;
                        String isla = "";

                        try {
                          model.consulta( "SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                           model.consulta("SELECT s2 FROM punto_emision WHERE idpunto_emision = 4 ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);

                    }

                    s2 = 4;

                } else if (tp.equalsIgnoreCase("Gasolina")) {

                    try {
                        emision.setText("051");

                        model.consulta( "SELECT numero FROM factura,punto_emision where idpunto_emision=punto_emision_idpunto_emision and s2='051'  ORDER BY idfactura DESC LIMIT 1 ");
                        System.out.println(model.getValueAt(0, 0));

                    } catch (Exception ex) {
                        Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (String.valueOf(model.getValueAt(0, 0)).length() == 0) {

                        String fact[] = new String[2];

                        System.out.println("entro1");
                        int num = 1;
                        String isla = "";
                        try {

                            model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                            model.consulta("SELECT s2 FROM punto_emision WHERE  s2='051'  ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);
                    } else {
                        String fact[] = new String[2];
                        fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                        int num = Integer.parseInt(fact[2]) + 1;
                        String isla = "";

                        try {
                            model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                            model.consulta("SELECT s2 FROM punto_emision WHERE idpunto_emision = 2 ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);

                    }

                    s2 = 2;

                } else if (tp.equalsIgnoreCase("Lubricantes")) {
                    try {
                        emision.setText("052");

                        model.consulta("SELECT numero FROM factura,punto_emision where idpunto_emision=punto_emision_idpunto_emision and s2='052'  ORDER BY idfactura DESC LIMIT 1 ");
                        System.out.println(model.getValueAt(0, 0));

                    } catch (Exception ex) {
                        Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if (String.valueOf(model.getValueAt(0, 0)).length() == 0) {

                        String fact[] = new String[2];

                        System.out.println("entro1");
                        int num = 1;
                        String isla = "";
                        try {

                            model.consulta("SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                            model.consulta("SELECT s2 FROM punto_emision WHERE  s2='052'  ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);
                    } else {
                        String fact[] = new String[2];
                        fact = String.valueOf(model.getValueAt(0, 0)).split("-");
                        int num = Integer.parseInt(fact[2]) + 1;
                        String isla = "";

                        try {
                            model.consulta( "SELECT secuencia1_factura FROM datos_gasolinera ");
                            local = String.valueOf(model.getValueAt(0, 0));
                            model.consulta("SELECT s2 FROM punto_emision WHERE idpunto_emision = 3 ");
                            isla = String.valueOf(model.getValueAt(0, 0));

                        } catch (SQLException ex) {
                            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String nume = String.valueOf(num);
                        String cero = "";
                        for (int i = nume.length(); i < 9; i++) {
                            cero = cero + "0";
                        }
                        nume = cero + nume;
                        emision.setText(isla);
                        factura.setText(local + "-" + isla + "-" + nume);

                    }

                    s2 = 3;

                }

                double IVA;
                BigDecimal iv;
//Asigno los valores a la tabla
                Double subtotales[];
                if (tp.equalsIgnoreCase("Transporte")) {

                    model.consulta( "SELECT nombre,precio_sin_iva,idproducto,tipo_producto FROM producto,tipo_producto  WHERE tipo_producto_idtipo_producto=idtipo_producto and idproducto='" + q[0] + "'");

                    precio = Double.valueOf(String.valueOf(model.getValueAt(0, 1)));
                    String idprod = String.valueOf(model.getValueAt(0, 2));

                    model.consulta("SELECT descuento FROM producto WHERE idproducto='" + q[0] + "'");

                    double des = Double.parseDouble(String.valueOf(model.getValueAt(0, 0)));
                    precio = precio - (precio * (des / 100));

                    precioU = precio;

                    //creo el modelo de la tabla
                    DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();

                    String temp[] = new String[Tabla.getRowCount()];
                    //verifico si los datos de la factura son duplicados
                    //para el cambio de cantidad y de precio
                    for (int i = 0; i < Tabla.getRowCount(); i++) {
                        temp[i] = String.valueOf(Tabla.getValueAt(i, 1));

                        if (temp[i].equalsIgnoreCase(descripcion)) {
                            cantidad = Integer.parseInt(String.valueOf(Tabla.getValueAt(i, 2))) + 1;
                            precio = precioU * cantidad;
                            Tabla.removeRow(i);

                        }

                    }

                    System.out.println("entro iva");
                    iv = new BigDecimal(0.00);

                    Tabla.addRow(new Object[]{idprod, descripcion, df.format(cantidad).replace(",", "."), precioU, dos.format(precioU * cantidad).replace(",", "."), dos.format(precioU).replace(",", "."), 0.00, des});
                    subtotales = new Double[Tabla.getRowCount()];

                    tablaArticulo.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
                    tablaArticulo.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

                    for (int i = 0; i < Tabla.getRowCount(); i++) {
                        subtotales[i] = Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 4)));
                        sub = sub + subtotales[i];
                    }

                    BigDecimal c = new BigDecimal(sub);
                    c = c.setScale(2, RoundingMode.HALF_UP);
                    subtotal.setText(c + "");
                    netos.setText(dos.format(sub - Double.parseDouble(descuentos.getText())).replace(",", "."));
                    //saco el iva
                    double Iva = 0.00;
                    BigDecimal b = new BigDecimal(Iva);
                    b = b.setScale(2, RoundingMode.HALF_UP);

                    iva.setText("0.00");
                    //saco el total
                    double Total = sub + Iva;
                    BigDecimal a = new BigDecimal(Total);
                    a = a.setScale(2, RoundingMode.HALF_UP);
                    total.setText(a + "");
                    calculos(Tabla, 0);

                    sub = 0;
                    cantidad = 1;

                } else {

                   model.consulta("SELECT nombre,punit,idproducto,tipo_producto FROM producto,tipo_producto  WHERE tipo_producto_idtipo_producto=idtipo_producto and idproducto='" + q[0] + "'");

                    precio = Double.valueOf(String.valueOf(model.getValueAt(0, 1)));
                    String idprod = String.valueOf(model.getValueAt(0, 2));

                   model.consulta("SELECT descuento FROM producto WHERE idproducto='" + q[0] + "'");

                    double des = Double.parseDouble(String.valueOf(model.getValueAt(0, 0)));
                    precio = precio - (precio * (des / 100));

                    precioU = precio;

                    //creo el modelo de la tabla
                    DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();

                    String temp[] = new String[Tabla.getRowCount()];
                    //verifico si los datos de la factura son duplicados
                    //para el cambio de cantidad y de precio
                    for (int i = 0; i < Tabla.getRowCount(); i++) {
                        temp[i] = String.valueOf(Tabla.getValueAt(i, 1));

                        if (temp[i].equalsIgnoreCase(descripcion)) {
                            cantidad = Integer.parseInt(String.valueOf(Tabla.getValueAt(i, 2))) + 1;
                            precio = precioU * cantidad;
                            Tabla.removeRow(i);

                        }

                    }

                    IVA = precio-(precio/1.12);
                    iv = new BigDecimal(IVA);
                    iv = iv.setScale(6, RoundingMode.HALF_UP);
                    BigDecimal p = new BigDecimal(precio);
                    p = p.setScale(6, RoundingMode.HALF_UP);

                    Tabla.addRow(new Object[]{idprod, descripcion, df.format(cantidad).replace(",", "."), df.format(precioU ).replace(",", "."), df.format(precio / 1.12).replace(",", "."), dos.format(precio).replace(",", "."), dos.format(iv).replace(",", "."), des});
                    subtotales = new Double[Tabla.getRowCount()];

                    tablaArticulo.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
                    tablaArticulo.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

                    for (int i = 0; i < Tabla.getRowCount(); i++) {
                        subtotales[i] = Double.parseDouble(String.valueOf(Tabla.getValueAt(i, 4)));
                        sub = sub + subtotales[i];
                    }

                    BigDecimal c = new BigDecimal(sub);
                    c = c.setScale(2, RoundingMode.HALF_UP);
                    subtotal.setText(c + "");
                    netos.setText(dos.format(sub - Double.parseDouble(descuentos.getText())).replace(",", "."));
                    //saco el iva
                    double Iva = precio-(precio/1.12);
                    BigDecimal b = new BigDecimal(Iva);
                    b = b.setScale(2, RoundingMode.HALF_UP);

                    iva.setText(b + "");
                    //saco el total
                    double Total = sub + Iva;
                    BigDecimal a = new BigDecimal(Total);
                    a = a.setScale(2, RoundingMode.HALF_UP);
                    total.setText(a + "");
                    calculos(Tabla, 1);

                    sub = 0;
                    cantidad = 1;

                }

                //calculo el subtotal y las cantidades
            } else {
                JOptionPane.showMessageDialog(this, "Este articulo no existe ");
            }
        } catch (Exception ex) {

            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);

            JOptionPane.showMessageDialog(this, "el codigo es incorrecto");
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tablaArticuloCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_tablaArticuloCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaArticuloCaretPositionChanged

    private void tablaArticuloFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tablaArticuloFocusLost
        DefaultTableModel Tabla = (DefaultTableModel) tablaArticulo.getModel();
          int fila = tablaArticulo.getSelectedRow();
        try {
            model = new ConnectionTableDB(usu, contra, "adv_facturacion", "SELECT nombre,punit,idproducto,tipo_producto FROM producto,tipo_producto  WHERE tipo_producto_idtipo_producto=idtipo_producto and idproducto='" + Tabla.getValueAt(fila,0) + "'", false);
        } catch (SQLException ex) {
            Logger.getLogger(prepagoGasolina.class.getName()).log(Level.SEVERE, null, ex);
        }
        String tp = String.valueOf(model.getValueAt(0, 3));
          if ((tp.equalsIgnoreCase("Transporte"))) {calculos(Tabla, 0);}else{
        calculos(Tabla, 1);   }     // TODO add your handling code here:
    }//GEN-LAST:event_tablaArticuloFocusLost
    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Fecha;
    private javax.swing.JTextField Hora;
    private javax.swing.JTextField ci;
    private javax.swing.JTextField correo;
    private javax.swing.JMenuItem derecho;
    private static javax.swing.JTextField descuentos;
    private javax.swing.JTextField emision;
    private javax.swing.JTextField factura;
    private javax.swing.JButton facturar;
    private static javax.swing.JTextField iva;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblHora;
    private javax.swing.JComboBox lista;
    private javax.swing.JComboBox metodoPago;
    private static javax.swing.JTextField netos;
    private javax.swing.JTextField nombre;
    private static javax.swing.JTextField subtotal;
    public static javax.swing.JTable tablaArticulo;
    private javax.swing.JTable tablaCliente;
    private javax.swing.JTextField telefono;
    private static javax.swing.JTextField total;
    private javax.swing.JTextField vendedor;
    // End of variables declaration//GEN-END:variables

    public void tomarHora() {
        Calendar calendario = new GregorianCalendar();

        hora = calendario.get(Calendar.HOUR_OF_DAY) > 9 ? "" + calendario.get(Calendar.HOUR_OF_DAY) : "0" + calendario.get(Calendar.HOUR_OF_DAY);

        minutos = calendario.get(Calendar.MINUTE) > 9 ? "" + calendario.get(Calendar.MINUTE) : "0" + calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND) > 9 ? "" + calendario.get(Calendar.SECOND) : "0" + calendario.get(Calendar.SECOND);
    }

    @Override
    public void run() {
        Thread ct = Thread.currentThread();

        while (ct == h1) {
            tomarHora();

            Hora.setText(hora + ":" + minutos + ":" + segundos + " ");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
