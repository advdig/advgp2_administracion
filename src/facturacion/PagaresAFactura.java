/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facturacion;

import conexion.ConnectionTableDB;
import facturacion.crear_clave_acceso;
import facturacion.crear_factura_credito;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import seguridad.FirmaDigital;

/**
 *
 * @author Advantech Digital
 */
public class PagaresAFactura extends javax.swing.JFrame {

    private ConnectionTableDB model;
    private String USER = "";
    int[] tp;
    private String PASSWORD = "";
    private final String DB = "adv_facturacion";
    private final String Extra = "Extra";
    private final String Super = "Super";
    private final String Diesel = "Diesel";
    private final int PTO_EMISION = 2;
    private InputStream PKCS12_RESOURCE;
    private String PKCS12_PASSWORD;
    private int idUser, idCliente = 0, idFactura, idClave;
    private int rowClientes = -1;
    private int rowXML = -1;
    private Date fechaIn, fechaFin;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private TableRowSorter<TableModel> sorter;
    public String ambiente, razonSocial, nombreComercial, ruc, sec2, sec1, secuencial, direccion, emailEstacion;
    public String fechaEmision, tipo, nCliente, idC, contribuyente, contabilidad, factura, fechaFactura, passwEmail;
    private String claveAcceso, Ewsri, Awsri, email;
    private String prod, pago;
    private Double cantidadT, ppuT, subtotalT, ivaT, totalT;
    private Double cantidadE = 0.000, ppuE = 0.000, subtotalE = 0.00, ivaE = 0.000, totalE = 0.00;
    private Double cantidadS = 0.000, ppuS = 0.000, subtotalS = 0.00, ivaS = 0.000, totalS = 0.00;
    private Double cantidadD = 0.000, ppuD = 0.000, subtotalD = 0.00, ivaD = 0.000, totalD = 0.00;
    private String[] productos;
    private Double[] cantidades, ppus, subtotales, ivas, totales;
    private javax.swing.JCheckBox[] pagares;
    private int notaCredito, Factura, idGasolinera;
    /**
     * Creates new form CreditoFactura
     */
    Timer totalp = new Timer(900, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            DecimalFormat dft = new DecimalFormat("#.##");
            DecimalFormat dfg = new DecimalFormat("#.######");


            for (int i = 0; i < pagares.length; i++) {

                System.out.println(Double.parseDouble(pagares[i].getText().trim().substring(117).trim().replace(",", ".")));


                // System.out.println("Pagare numero " + i + " " + pagares[i].isSelected());
                if (pagares[i].isSelected()) {

                    // System.out.println(total);
                    //  System.out.println(tp[i]);

                    if (tp[i] == 0) {
                        total = Double.parseDouble(tpagare.getText());
                        total = total + Double.parseDouble(pagares[i].getText().trim().substring(104, 116).trim().replace(",", "."));


                        tpagare.setText(dft.format(total).replace(",", "."));

                        galones = Double.parseDouble(tgalones.getText());





                        galones = galones + Double.parseDouble(pagares[i].getText().trim().substring(117).trim().replace(",", "."));
                        tgalones.setText(dfg.format(galones).replace(",", "."));


                    }

                    tp[i] = 1;
                } else {

                    if (tp[i] == 1) {

                        tp[i] = 0;
                        total = total - Double.parseDouble(pagares[i].getText().trim().substring(104, 116).trim().replace(",", "."));
                        tpagare.setText(dft.format(total).replace(",", "."));


                        galones = galones - Double.parseDouble(pagares[i].getText().trim().substring(117).trim().replace(",", "."));
                        tgalones.setText(dfg.format(galones).replace(",", "."));




                    }


                }


            }
        }
    });

    public PagaresAFactura(String user, String password) {





        USER = user;
        PASSWORD = password;
        try {
            String consulta = "SELECT tipo_ambiente, razon_social, nombre_comercial, ruc, s2, secuencia1_factura, direccion, "
                    + "email_estacion, contribuyente_especial, obligado_llevar_contabilidad, iddatos_gasolinera, "
                    + "email_estacion "
                    + "FROM datos_gasolinera, punto_emision "
                    + "WHERE iddatos_gasolinera = datos_gasolinera_iddatos_gasolinera "
                    + "AND idpunto_emision = " + PTO_EMISION;
            model = new ConnectionTableDB(USER, PASSWORD, DB, consulta, false);
            idGasolinera = (Integer) model.getValueAt(0, 10);
            ambiente = (String) model.getValueAt(0, 0);
            razonSocial = (String) model.getValueAt(0, 1);
            nombreComercial = (String) model.getValueAt(0, 2);
            ruc = (String) model.getValueAt(0, 3);
            sec2 = (String) model.getValueAt(0, 4);
            sec1 = (String) model.getValueAt(0, 5);
            direccion = (String) model.getValueAt(0, 6);

            emailEstacion = (String) model.getValueAt(0, 7);
            //direccionE = (String)model.getValueAt(0, 6);
            contribuyente = (String) model.getValueAt(0, 8);
            contabilidad = (String) model.getValueAt(0, 9);
            if (ambiente.equalsIgnoreCase("1")) {
                Ewsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                Awsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
            } else {
                Ewsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                Awsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";
            }
            try (java.sql.ResultSet rsUser = model.stSentencias.executeQuery("SELECT idusuarios FROM usuarios WHERE usuario = '" + USER + "'")) {
                if (rsUser.first()) {
                    idUser = rsUser.getInt(1);
                }
            }
            try (java.sql.ResultSet rs = model.stSentencias.executeQuery("SELECT certificado_digital, "
                    + "AES_DECRYPT(contraseña_certificado,'thekey'), "
                    + "AES_DECRYPT(contraseña_mail,'thekey') "
                    + "FROM datos_gasolinera")) {
                if (rs.next()) {
                    PKCS12_RESOURCE = rs.getBlob(1).getBinaryStream();
                    PKCS12_PASSWORD = rs.getString(2);
                    passwEmail = rs.getString(3);
                }
            }
            model.consulta("SELECT cedula_ruc AS 'CEDULA/RUC', nombre AS NOMBRE, numero AS NUMERO FROM cliente, factura WHERE credito_cliente = 1 "
                    + "AND idCliente = cliente_idcliente AND fecha <= '" + String.format("%tF", Calendar.getInstance()) + "' "
                    + "AND Estado_factura = 'AUTORIZADO' AND idcliente = 0");

            initComponents();
            fechaa.setDate(Calendar.getInstance().getTime());

            setSize(1222, 620);
            fechaI.setDate(Calendar.getInstance().getTime());

            fechaF.setDate(Calendar.getInstance().getTime());



            txtAFacturas.setText("No existen facturas de crédito para el\ncliente y periodo seleccionados.");

            Clientes.setUndecorated(true);
            Clientes.setSize(1200, 560);
            sorter = new TableRowSorter<TableModel>(model);
            sorter.setRowFilter(null);
            tablaClientes.setRowSorter(sorter);
        } catch (SQLException | IllegalStateException ex) {
            System.err.println(ex.getMessage());
        }





    }

    /*private void createTable(){
        
     }*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Clientes = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        txtBuscado = new javax.swing.JTextField();
        btnOK = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnBuscarCliente = new javax.swing.JButton();
        btnFacturas = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fechaI = new com.toedter.calendar.JDateChooser();
        fechaF = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        ordenar = new javax.swing.JComboBox();
        st = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        fechaa = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblPagares = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelPagares = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtALog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAFacturas = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btnNotaCredito = new javax.swing.JButton();
        barEstado = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        tpagare = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        tgalones = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblEstado = new javax.swing.JLabel();

        Clientes.setExtendedState(2);
        Clientes.setName("frameClientes"); // NOI18N
        Clientes.setResizable(false);

        jLabel3.setText("Buscar:");
        jPanel2.add(jLabel3);

        txtBuscar.setColumns(30);
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });
        jPanel2.add(txtBuscar);

        Clientes.getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        tablaClientes.setModel(model);
        tablaClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaClientesMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaClientes);

        Clientes.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        txtBuscado.setEditable(false);
        txtBuscado.setColumns(30);
        jPanel3.add(txtBuscado);

        btnOK.setText("OK");
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });
        jPanel3.add(btnOK);

        Clientes.getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        Clientes.getAccessibleContext().setAccessibleParent(this);

        setTitle("Factura de Credito");
        setName("interfaz"); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnBuscarCliente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnBuscarCliente.setText("Buscar Cliente de Crédito");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        btnFacturas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnFacturas.setText("Buscar Pagares");
        btnFacturas.setEnabled(false);
        btnFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFacturasActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Id:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Cliente:");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Fecha Inicial:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Fecha Final:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Ordenar por ");

        ordenar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Placa", "Fecha", "Numero" }));

        st.setText("Seleccionar Todos los Pagares");
        st.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("FECHA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtId)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(269, 269, 269)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 8, Short.MAX_VALUE))
                    .addComponent(txtCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(st, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(btnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(fechaI, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ordenar, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(fechaF, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fechaa, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscarCliente)
                        .addComponent(jLabel4))
                    .addComponent(fechaI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(ordenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addComponent(jLabel1))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel2))))
                    .addComponent(fechaF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(fechaa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(st)
                        .addComponent(btnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanel6.setLayout(new java.awt.BorderLayout());

        lblPagares.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPagares.setText(String.format("%15s   %-80s\t%-15s\t%15s\t%15s","Número","Placa","Fecha", "Total","Galones"));
        jPanel6.add(lblPagares, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 400));

        panelPagares.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(panelPagares);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel6);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtALog.setColumns(20);
        txtALog.setRows(5);
        txtALog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtALogKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtALog);

        jPanel5.add(jScrollPane4);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setPreferredSize(new java.awt.Dimension(400, 400));

        txtAFacturas.setEditable(false);
        txtAFacturas.setColumns(10);
        txtAFacturas.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        txtAFacturas.setRows(5);
        txtAFacturas.setTabSize(4);
        txtAFacturas.setPreferredSize(new java.awt.Dimension(400, 350));
        jScrollPane2.setViewportView(txtAFacturas);

        jPanel4.setPreferredSize(new java.awt.Dimension(1380, 80));

        btnNotaCredito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnNotaCredito.setText("Crear Factura");
        btnNotaCredito.setEnabled(false);
        btnNotaCredito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotaCreditoActionPerformed(evt);
            }
        });
        jPanel4.add(btnNotaCredito);

        barEstado.setPreferredSize(new java.awt.Dimension(200, 20));
        jPanel4.add(barEstado);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Total");
        jPanel4.add(jLabel6);

        tpagare.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(tpagare);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Total Galones");
        jPanel4.add(jLabel9);

        tgalones.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jPanel4.add(tgalones);

        jSeparator1.setPreferredSize(new java.awt.Dimension(1200, 10));
        jPanel4.add(jSeparator1);

        lblEstado.setPreferredSize(new java.awt.Dimension(1000, 25));
        jPanel4.add(lblEstado);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1083, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 725, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    Double total = 0.00;
    Double galones = 0.00;

    private void llenarList(int cantidad) {
        if (pagares != null) {
            for (int i = 0; i < pagares.length; i++) {
                panelPagares.remove(pagares[i]);
            }
        }

        pagares = new javax.swing.JCheckBox[cantidad];
        for (int i = 0; i < cantidad; i++) {
            pagares[i] = new javax.swing.JCheckBox();
            pagares[i].setText(String.format("%15d   %-70s\t%15s\t%20.2f\t%20.2f",
                    (Integer) model.getValueAt(i, 0),
                    (String) model.getValueAt(i, 1),
                    String.format("%tF", model.getValueAt(i, 2)),
                    ((java.math.BigDecimal) model.getValueAt(i, 3)).doubleValue(), ((java.math.BigDecimal) model.getValueAt(i, 4)).doubleValue()));
            pagares[i].setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));
            pagares[i].setBorderPaintedFlat(true);
            pagares[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    try {
                        javax.swing.JCheckBox pa;
                        pa = (javax.swing.JCheckBox) evt.getComponent();





                        showFact(((javax.swing.JCheckBox) evt.getComponent()).getText().substring(0, 15).trim(), true);
                        /*if (!((javax.swing.JCheckBox)evt.getComponent()).isSelected()) {
                         btnNotaCredito.setEnabled(true);
                         } else {
                         btnNotaCredito.setEnabled(false);
                         }*/
                        int selected = 0;
                        for (int j = 0; j < pagares.length; j++) {
                            if (pagares[j].isSelected()) {
                                ++selected;
                                //break;
                            }
                        }
                        if (selected > 1 || !((javax.swing.JCheckBox) evt.getComponent()).isSelected()) {
                            btnNotaCredito.setEnabled(true);
                        } else {
                            btnNotaCredito.setEnabled(false);
                        }
                    } catch (SQLException | IllegalStateException ex) {
                        System.err.println(ex.getMessage());
                    }

                }
            });
            panelPagares.add(pagares[i]);
        }
    }

    private void showFact(String idFactura, boolean consulta) throws SQLException, IllegalStateException {
        try (java.sql.ResultSet rsDatos = model.stSentencias.executeQuery("SELECT pagare, producto.nombre, cantidad, pagare.punit, subtotal, iva, total, email "
                + "FROM pagare, cliente, producto, configuracion "
                + "WHERE pagare.cliente_idcliente = idcliente "
                + "AND configuracion_nmanguera = nmanguera AND idproducto = producto_idproducto "
                + "AND idpagare = " + idFactura + " "
                + "AND idcliente = " + idCliente + "")) {
            InputStream pagare = null;
            String producto = "", cantidad = "0.000", precioU = "0.000", subtotal = "0.00", iva = "0.000", total = "0.00";
            if (rsDatos.next()) {
                pagare = rsDatos.getBlob(1).getBinaryStream();
                producto = String.format("%s", rsDatos.getString(2)).toUpperCase();


                cantidad = String.format("%.3f", rsDatos.getBigDecimal(3));
                precioU = String.format("%.3f", rsDatos.getBigDecimal(4));
                subtotal = String.format("%.2f", rsDatos.getBigDecimal(5));
                iva = String.format("%.3f", rsDatos.getBigDecimal(6));
                total = String.format("%.2f", rsDatos.getBigDecimal(7));
                email = rsDatos.getString(8);
                this.idFactura = Integer.parseInt(idFactura);
            }
            if (!consulta) {
                ppuT = rsDatos.getBigDecimal(4).doubleValue();
                cantidadT = rsDatos.getBigDecimal(3).doubleValue();
                subtotalT = rsDatos.getBigDecimal(5).doubleValue();
                ivaT = rsDatos.getBigDecimal(6).doubleValue();
                totalT = rsDatos.getBigDecimal(7).doubleValue();

                System.out.println(producto + ", " + idFactura);

                if (producto.equalsIgnoreCase(Extra)) {
                    prod = "1," + producto;
                    ppuE = ppuT;
                    cantidadE += cantidadT;
                    subtotalE += subtotalT;
                    ivaE += ivaT;
                    totalE += totalT;
                } else if (producto.equalsIgnoreCase(Super)) {
                    prod = "2," + producto;
                    ppuS = ppuT;
                    cantidadS += cantidadT;
                    subtotalS += subtotalT;
                    ivaS += ivaT;
                    totalS += totalT;
                } else if (producto.equalsIgnoreCase(Diesel)) {
                    prod = "3," + producto;
                    ppuD = ppuT;
                    cantidadD += cantidadT;
                    subtotalD += subtotalT;
                    ivaD += ivaT;
                    totalD += totalT;
                }
            }

            model.consulta("SELECT cedula_ruc AS 'CEDULA/RUC', nombre AS NOMBRE, numero AS NUMERO, fecha AS FECHA "
                    + "FROM cliente, factura "
                    + "WHERE credito_cliente = 1 "
                    + "AND idCliente = cliente_idcliente "
                    + "AND metodo_pago = 'Credito' "
                    + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                    + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                    + "AND Estado_factura = 'AUTORIZADO' AND idCliente = " + idCliente);

            java.io.BufferedReader br;
            try {
                String line, txtPagare = "";
                br = new java.io.BufferedReader(new java.io.InputStreamReader(pagare));
                int l = 0;
                while ((line = br.readLine()) != null) {
                    ++l;
                    if (l == 1) {
                        //txtPagare += line.substring(3) + "\n";
                    } else if (l < 20) {
                        txtPagare += line + "\n";
                    } else {
                        break;
                    }

                }
                txtAFacturas.setText(txtPagare);
                br.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    private void prepareInfo(int index, String num) throws SQLException, IllegalStateException {
        factura = num;
        System.out.println(factura + ", Fila: " + index);
        showFact(factura, false);
        /*try (java.sql.ResultSet rsClave = model.stSentencias.executeQuery("SELECT idclave_acceso, clave_acceso FROM clave_acceso WHERE estado = 0 AND tipo = 'contingencia'")) {
         if (rsClave.first()) {
         idClave = rsClave.getInt(1);
         claveAcceso = rsClave.getString(2);
         }
         }
         model.psPrepararSentencias = model.coneccion.prepareStatement("UPDATE clave_acceso SET estado = 1 WHERE idclave_acceso = " + idClave);
         model.psPrepararSentencias.executeUpdate();
         model.psPrepararSentencias.close();*/
    }

    private void eliminarPagare(String num, int fact) throws SQLException, IllegalStateException {
        System.out.println("numero " + fact);

        model.psPrepararSentencias = model.coneccion.prepareStatement("UPDATE pagare "
                + "SET factura_idfactura = " + fact + ", "
                + "facturado = 1 "
                + "WHERE idpagare = '" + num + "'");
        model.psPrepararSentencias.executeUpdate();
        model.psPrepararSentencias.close();
    }

    /*private void crearContingencia(String num) {
     try {
     claveAcceso = claveAcceso.substring(14);
     String ss1 = claveAcceso.substring(0, 5);
     String ss2 = claveAcceso.substring(5, 14);
     String ss3 = claveAcceso.substring(14);
            
            
     String clave = new crear_clave_acceso().crear_clave_acceso(fechaEmision.replace("/", ""), "04", ruc, ambiente, 
     ss1, ss2, ss3, "2");
     model.ejecutar("UPDATE clave_acceso SET clave_acceso = '" + clave + "' WHERE idclave_acceso = " + idClave + "");
     notaCredito = new crear_nota_credito_contingencia().crear_nota_credito_contingencia(prod.substring(0, 1), nombreComercial, contabilidad, Integer.parseInt(contribuyente), ambiente,
     razonSocial, ruc, clave, sec1, sec2, num, direccion, fechaEmision, tipo, nCliente, idC,
     subtotalT, ivaT, totalT, prod.substring(2), cantidadT, ppuT, fechaFactura, factura, "ANULACION");

     if (notaCredito == 1) {
     FirmaDigital fd = new FirmaDigital("notas_contingencia\\" + sec1 + "-" + sec2 + "-" + num + ".xml", "notas_firmadas\\nota_firmada" + sec1 + "-" + sec2 + "-" + num + ".xml");
     fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
     fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);
     fd.firmarDocumentoXML();
     //File xsd = new File("factura_v1.0.0.xsd");
     File temp = new File("firmados\\firmado" + s1 + "-" + s2 + "-" + ss3 + ".xml");
     FileInputStream in = new FileInputStream(temp);
     model.ejecutar("INSERT INTO nota_credito (factura_idfactura, factura_cliente_idcliente, factura_usuarios_idusuarios, clave_acceso_idclave_acceso, valor_modificacion, motivo, estado) "
     + "VALUES (" + idFactura + ", " + idCliente + ", " + idUser + ", " + Integer.parseInt(num) + ", " + totalT + ", 'ANULACION', 'estado')");
     model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_contigencia (xml_contingencia, xml_factura, motivo) "
     + "VALUES(?, '" + idFactura + "','Indisponibilidad del Sistema')");
     model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
     model.psPrepararSentencias.executeUpdate();
     model.psPrepararSentencias.close();
     in.close();
     guardar_datos g = new guardar_datos(tp, idcl, 2, axml, "Contingencia", "indisponibilidad del sistema", nsurtidor, s1 + "-" + s2 + "-" + ss3, f.getText(), h2.getText(), subtotal, total, iva, manguera, idcliente, usuario, producto, cang);
     g.usuarios(usuario, contra);
     g.actualizar(cadena, s1 + "-" + s2 + "-" + ss3, ss3);
     }
     } catch (SQLException ex) {
     System.err.println(ex.getMessage());
     }
     }*/
    private void crearFactura(String num) throws SQLException, MalformedURLException, JAXBException, IOException {
        System.out.println(ppuE + ", " + ppuS + ", " + ppuD);
        String id;
        if (ppuE != 0) {
            if (ppuS != 0) {
                if (ppuD != 0) {
                    id = "1,2,3";
                } else {
                    id = "1,2";
                }
            } else if (ppuD != 0) {
                id = "1,3";
            } else {
                id = "1";
            }
        } else if (ppuS != 0) {
            if (ppuD != 0) {
                id = "2,3";
            } else {
                id = "2";
            }
        } else if (ppuD != 0) {
            id = "3";
        } else {
            id = "";
        }
        System.out.println(id);

        String fecha = null;

        int año = fechaa.getCalendar().get(Calendar.YEAR);
        int mes = fechaa.getCalendar().get(Calendar.MONTH) + 1;
        int diai = fechaa.getCalendar().get(Calendar.DAY_OF_MONTH);

       
        

        if (mes < 10 && diai < 10) {






            fechaEmision = año + "/0" + mes + "/0" + diai;
            fecha = "0" + diai + "/0" + mes + "/" + año;


        } else if (diai < 10) {

            fechaEmision = año + "/" + mes + "/0" + diai;
            fecha = "0" + diai + "/" + mes + "/" + año;

        } else if (mes < 10) {
        
        
            fechaEmision = año + "/0" + mes + "/" + diai;
            fecha = diai + "/0" + mes + "/" + año;
        
        
        
        
        
        }








        System.out.println(fechaEmision);






        subtotalT = 0.00;
        ivaT = 0.00;
        totalT = 0.00;
        String[] ids;
        if (id.length() != 0) {
            ids = id.split(",");
            System.out.println(Arrays.toString(ids));
            int cant = ids.length, i = 0;
            productos = new String[cant];
            ppus = new Double[cant];
            cantidades = new Double[cant];
            subtotales = new Double[cant];
            ivas = new Double[cant];
            totales = new Double[cant];
            for (String index : ids) {
                switch (index) {
                    case "1":
                        productos[i] = Extra;
                        ppus[i] = ppuE;
                        Double pE = ppuE / 1.12;
                        Double subtotale = totalE / 1.12;
                        cantidades[i] = subtotale / pE;
                        subtotales[i] = totalE / 1.12;
                        ivas[i] = totalE - subtotale;
                        totales[i] = totalE;
                        break;
                    case "2":
                        productos[i] = Super;
                        ppus[i] = ppuS;
                        Double pS = ppuS / 1.12;
                        Double subtotals = totalS / 1.12;
                        cantidades[i] = subtotals / pS;
                        subtotales[i] = totalS / 1.12;
                        ivas[i] = totalS - subtotals;
                        totales[i] = totalS;
                        break;


                    case "3":


                        productos[i] = Diesel;
                        ppus[i] = ppuD;
                        Double pD = ppuD / 1.12;
                        Double subtotal = totalD / 1.12;
                        cantidades[i] = subtotal / pD;
                        subtotales[i] = totalD / 1.12;
                        ivas[i] = totalD - subtotal;
                        totales[i] = totalD;
                        break;
                }
                ++i;
            }
            for (int j = 0; j < productos.length; j++) {
                cantidadT += cantidades[j];
                subtotalT += subtotales[j];
                ivaT += ivas[j];
                totalT += totales[j];
            }
        } else {
            ids = null;
        }

        System.out.println(Arrays.toString(productos));
        System.out.println(Arrays.toString(cantidades));
        System.out.println(Arrays.toString(subtotales));
        System.out.println(Arrays.toString(totales));

        if (ids != null) {





            java.sql.ResultSet rs = model.stSentencias.executeQuery("SELECT certificado_digital FROM datos_gasolinera");
            if (rs.next()) {
                PKCS12_RESOURCE = rs.getBlob(1).getBinaryStream();
            }
            //model.ejecutar("UPDATE clave_acceso SET estado = 0 WHERE idclave_acceso = " + idClave + "");
            String clave = new crear_clave_acceso().crear_clave_acceso(fecha.replace("/", ""), "01", ruc, ambiente,
                    sec1 + sec2, num, "12345678", "1");
            txtALog.append("Clave de acceso: " + clave + " generada.\n");

            Factura = new crear_factura_credito().crear_factura_credito("01", ids, nombreComercial, contabilidad, Integer.parseInt(contribuyente), ambiente,
                    razonSocial, ruc, clave, sec1, sec2, num, direccion, fecha, tipo, nCliente.replace("ñ", "n").replace("Ñ", "N"), idC,
                    subtotalT, ivaT, totalT, productos, cantidades, ppus, ivas, totales, subtotales);

            if (Factura == 1) {
                txtALog.append("Factura de Credito: " + sec1 + "-" + sec2 + "-" + num + " creada correctamente.\n");



                FirmaDigital fd = new FirmaDigital("generados\\" + sec1 + "-" + sec2 + "-" + num + ".xml", "firmados\\firmado" + sec1 + "-" + sec2 + "-" + num + ".xml");
                fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
                fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);
                fd.firmarDocumentoXML();
                txtALog.append("Factura de Credito: " + sec1 + "-" + sec2 + "-" + num + " firmada correctamente.\n");

                //File xsd = new File("factura_v1.0.0.xsd");
                File temp = new File("firmados\\firmado" + sec1 + "-" + sec2 + "-" + num + ".xml");



                txtALog.append("Guardando Factura Factura de Credito " + sec1 + "-" + sec2 + "-" + num + "\n");

                //RespuestaSolicitud response = ws.enviarComprobante(new File("D:\\ejemplos\\Firmados\\pruebaguia.xml"));






                txtALog.append("Entro a autorizacion: Factura de Credito " + sec1 + "-" + sec2 + "-" + num + "\n");
                //Lógica de acceso a datos
                String ta;
                FileInputStream in;





                ta = (ambiente.equalsIgnoreCase("1") ? "Pruebas" : "Produccion");





                model.ejecutar("INSERT INTO factura (numero, fecha, hora, metodo_pago, punto_emision_idpunto_emision, "
                        + "cliente_idcliente, usuarios_idusuarios, datos_gasolinera_iddatos_gasolinera, "
                        + "subtotal, iva, total) "
                        + "VALUES ('" + sec1 + "-" + sec2 + "-" + num + "', '" + fechaEmision.replace("/", "-") + "', '" + String.format("%tT", Calendar.getInstance())
                        + "', '" + pago + "', " + PTO_EMISION + ", " + idCliente + ", " + idUser + ", " + idGasolinera + ", " + subtotalT + ", " + ivaT + ", " + totalT + ")");

                model.ejecutar("INSERT INTO clave_acceso (clave_acceso, fecha, estado, tipo) VALUES ('" + clave + "','" + fechaEmision.replace("/", "-") + "', 1, 'normal')");
                txtALog.append("Clave de acceso: " + clave + " grabada correctamente.\n");

                try (java.sql.ResultSet rsIdClave = model.stSentencias.executeQuery("SELECT idclave_acceso FROM clave_acceso WHERE clave_acceso = '" + clave + "'")) {
                    if (rsIdClave.first()) {
                        idClave = rsIdClave.getInt(1);
                    }
                }



                try (java.sql.ResultSet rsFact = model.stSentencias.executeQuery("SELECT idfactura FROM factura "
                        + "WHERE numero = '" + sec1 + "-" + sec2 + "-" + num + "'")) {
                    if (rsFact.first()) {
                        idFactura = rsFact.getInt(1);
                    }
                }
                Double punit =0.00;
                
                String idp = null;
                for (int i = 0; i < productos.length; i++) {

                    java.sql.ResultSet rs1 = model.stSentencias.executeQuery("SELECT idproducto,punit FROM producto where nombre='" + productos[i] + "'");
                    if (rs1.next()) {
                        idp = rs1.getString(1);
                        
                    }
                    
                    if(idp.equalsIgnoreCase("1")){
                    
                        punit=ppuE;
                    }
                    if(idp.equalsIgnoreCase("2")){
                    
                        punit=ppuS;
                        
                        
                    }
                    if(idp.equalsIgnoreCase("3")){
                    
                        punit=ppuD;
                    }
                    
                    
                    System.out.println(punit);
                    
                    model.ejecutar("INSERT INTO factura_detalle (cantidad, subtotal, iva, total, factura_idfactura, producto_idproducto,p_unit) "
                            + "VALUES (" + cantidades[i] + ", " + subtotales[i] + ", " + ivas[i] + ", " + totales[i] + ", " + idFactura + ", " + idp + ","+punit+")");
                }



                model.ejecutar("UPDATE factura SET Estado_factura = 'CONTINGENCIA', "
                        + "clave_acceso_idclave_acceso = " + idClave + " "
                        + "WHERE idfactura = " + idFactura);




                model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_contingencia (xml_contingencia, xml_factura,motivo) "
                        + "VALUES(?, '" + idFactura + "', 'Indisponibilidad del Sistema')");
                in = new FileInputStream(temp);
                model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
                model.psPrepararSentencias.executeUpdate();
                model.psPrepararSentencias.close();
                txtALog.append("Factura de credito: " + sec1 + "-" + sec2 + "-" + num + " grabada correctamente.\n"
                        + "----------------------------------------------------------------\n");


                in.close();



            }
        }
    }

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        // TODO add your handling code here:
        btnBuscarCliente.setEnabled(false);
        Clientes.setVisible(true);
        Clientes.requestFocus();
        setEnabled(false);
        try {
            model.consulta("SELECT idcliente, cedula_ruc, nombre, tipo_identificacion, direccion, telefono FROM cliente, codigos WHERE credito_cliente = 1 AND estado = 'AUTORIZADO' group by cedula_ruc");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        setEnabled(true);
        txtId.requestFocus();
        Clientes.setVisible(false);
        btnFacturas.setEnabled(true);
        btnBuscarCliente.setEnabled(true);
        try {
            if (!(rowClientes < 0)) {
                txtId.setText((String) tablaClientes.getValueAt(rowClientes, 1));
                txtCliente.setText((String) tablaClientes.getValueAt(rowClientes, 2));
                if (tablaClientes.getValueAt(rowClientes, 3) != null) {
                    switch ((String) tablaClientes.getValueAt(rowClientes, 3)) {
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
                }
                //System.out.println(tipo);
                idCliente = (Integer) tablaClientes.getValueAt(rowClientes, 0);
                model.consulta("SELECT cedula_ruc AS 'CEDULA/RUC', nombre AS NOMBRE, numero AS NUMERO, fecha AS FECHA "
                        + "FROM cliente, factura "
                        + "WHERE credito_cliente = 1 "
                        + "AND idCliente = cliente_idcliente "
                        + "AND metodo_pago = 'Credito' "
                        + "AND fecha = '" + String.format("%tF", Calendar.getInstance()) + "' "
                        + "AND Estado_factura = 'AUTORIZADO' "
                        + "AND idCliente = " + idCliente);
            } else {
                JOptionPane.showMessageDialog(this, "No ha seleccionado ningún cliente.");
                model.consulta("SELECT cedula_ruc AS 'CEDULA/RUC', nombre AS NOMBRE, numero AS NUMERO, fecha AS FECHA "
                        + "FROM cliente, factura "
                        + "WHERE credito_cliente = 1 "
                        + "AND idCliente = cliente_idcliente AND fecha = '" + String.format("%tF", Calendar.getInstance()) + "' "
                        + "AND Estado_factura = 'AUTORIZADO' AND idCliente = 0");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void tablaClientesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMousePressed
        // TODO add your handling code here:
        rowClientes = tablaClientes.rowAtPoint(evt.getPoint());
        txtBuscado.setText(String.format("Seleccionado: %s", (String) tablaClientes.getValueAt(rowClientes, 2)));
    }//GEN-LAST:event_tablaClientesMousePressed

    private void btnFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturasActionPerformed

        String or = "";


        int añoi = fechaI.getCalendar().get(Calendar.YEAR);
        int mesi = fechaI.getCalendar().get(Calendar.MONTH) + 1;
        int diai = fechaI.getCalendar().get(Calendar.DAY_OF_MONTH);


        int añof = fechaF.getCalendar().get(Calendar.YEAR);
        int mesf = fechaF.getCalendar().get(Calendar.MONTH) + 1;
        int diaf = fechaF.getCalendar().get(Calendar.DAY_OF_MONTH);



        if (ordenar.getSelectedItem().toString().equalsIgnoreCase("Placa")) {

            or = "pagare.placa";

        } else if (ordenar.getSelectedItem().toString().equalsIgnoreCase("Fecha")) {

            or = "pagare.fecha";

        } else if (ordenar.getSelectedItem().toString().equalsIgnoreCase("Numero")) {

            or = "idpagare";

        }





        if (txtCliente.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "No ha seleccionado ningún cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {


            total = 0.00;
            galones = 0.00;
            tpagare.setText("0.00");
            tgalones.setText("0.00");


            fechaIn = fechaI.getDate();
            fechaFin = fechaF.getDate();

            try {
                model.consulta("SELECT idpagare,pagare.placa,fecha,total,cantidad "
                        + "FROM cliente, pagare "
                        + "WHERE credito_cliente = 1 "
                        + "AND idcliente = cliente_idcliente "
                        + "AND fecha >= '" + String.format("%tF", dateFormat.parse(diai + "/" + mesi + "/" + añoi)) + "' "
                        + "AND fecha <= '" + String.format("%tF", dateFormat.parse(diaf + "/" + mesf + "/" + añof)) + "' "
                        + "AND facturado = 0 "
                        + "AND idCliente = " + idCliente + " order by " + or);
                llenarList(model.getRowCount());
                panelPagares.invalidate();
                revalidate();
                repaint();
                totalp.start();
                btnNotaCredito.setEnabled(true);
                tp = new int[pagares.length];
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            } catch (ParseException ex) {
                Logger.getLogger(PagaresAFactura.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnFacturasActionPerformed

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        // TODO add your handling code here:
        String filtro = txtBuscar.getText();
        if (filtro.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro));
            } catch (PatternSyntaxException ex) {
                System.err.println(ex.getMessage());
            }

        }
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void btnNotaCreditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotaCreditoActionPerformed
        // TODO add your handling code here:
        txtALog.setText("");
        tpagare.setText("");
        ppuT = 0.00;
        cantidadT = 0.00;
        subtotalT = 0.00;
        totalT = 0.00;
        ppuE = 0.00;
        ppuS = 0.00;
        ppuD = 0.00;
        cantidadE = 0.00;
        cantidadS = 0.00;
        cantidadD = 0.00;
        subtotalE = 0.00;
        subtotalS = 0.00;
        subtotalD = 0.00;
        ivaE = 0.00;
        ivaS = 0.00;
        ivaD = 0.00;
        totalE = 0.00;
        totalS = 0.00;
        totalD = 0.00;
        final int[] selectedRows;
        int c = 0;
        for (int i = 0; i < pagares.length; i++) {
            if (pagares[i].isSelected()) {
                ++c;
            }
        }
        selectedRows = new int[c];
        c = 0;
        for (int i = 0; i < pagares.length; i++) {
            if (pagares[i].isSelected()) {
                selectedRows[c] = i;
                ++c;
            }
        }
        final String[] numero = new String[selectedRows.length];
        c = 0;
        for (int row : selectedRows) {
            numero[c] = pagares[row].getText().substring(0, 15).trim();
            ++c;
        }
        int option = javax.swing.JOptionPane.showConfirmDialog(this,
                "Esta seguro de ELIMINAR " + selectedRows.length + " Pagares para Emisión de FACTURA",
                "Confirmar Factura Consolidada",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (option == javax.swing.JOptionPane.YES_OPTION) {

            totalp.stop();
            barEstado.setIndeterminate(true);
            nCliente = txtCliente.getText();
            idC = txtId.getText();
            btnNotaCredito.setEnabled(false);
            if (pagares.length > 0 && selectedRows.length > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean[] error = new boolean[numero.length];
                        int i = 0;
                        for (int row : selectedRows) {
                            try {
                                error[i] = false;
                                prepareInfo(row, numero[i]);
                                lblEstado.setText("Procesando pagares...");
                                ++i;
                            } catch (SQLException | IllegalStateException ex) {
                                System.err.println(ex.getMessage());
                                txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                        + "----------------------------------------------------------------\n");
                                error[i] = true;
                            }
                        }
                        barEstado.setIndeterminate(false);

                        lblEstado.setText("Creando  Factura de Credito.");
                        /*Object[] pagos = {"Contado", "TC", "Cheque"};
                         pago = (String) JOptionPane.showInputDialog(getContentPane(), "Seleccione la forma de pago:",
                         "Confirmar Factura de Crédito ",
                         JOptionPane.INFORMATION_MESSAGE,
                         null, pagos, "Contado");
                         System.out.println(pago);*/
                        pago = "Credito";
                        if (pago != null) {
                            barEstado.setIndeterminate(true);
                            String num = "";

                            try (java.sql.ResultSet rsNum = model.stSentencias.executeQuery("SELECT numero FROM factura, punto_emision "
                                    + "WHERE punto_emision_idpunto_emision = idpunto_emision AND idpunto_emision = " + PTO_EMISION + " "
                                    + "ORDER BY numero DESC LIMIT 1")) {

                                System.out.println("SELECT numero FROM factura, punto_emision "
                                        + "WHERE punto_emision_idpunto_emision = idpunto_emision AND idpunto_emision = " + PTO_EMISION + " "
                                        + "ORDER BY numero DESC LIMIT 1");

                                if (rsNum.first()) {
                                    num = String.format("%09d", Integer.parseInt(rsNum.getString(1).substring(8)) + 1);
                                    crearFactura(num);
                                    //model.consulta("SELECT idfactura FROM factura WHERE numero = '" + num + "'");
                                    int fact = 0;

                                    System.out.println(sec1 + "-" + sec2 + "-" + num);
                                    Statement st_f = model.coneccion.createStatement();
                                    ResultSet rif = st_f.executeQuery("SELECT idfactura FROM factura WHERE numero = '" + sec1 + "-" + sec2 + "-" + num + "'");


                                    while (rif.next()) {

                                        fact = rif.getInt(1);
                                    }
                                    //System.out.println(model.getValueAt(0, 0));

                                    i = 0;
                                    for (int row : selectedRows) {
                                        if (!error[i]) {
                                            eliminarPagare(numero[i], fact);
                                        }
                                        ++i;
                                    }
                                    model.consulta("SELECT idpagare, nombre, fecha, total,cantidad "
                                            + "FROM cliente, pagare "
                                            + "WHERE credito_cliente = 1 "
                                            + "AND idcliente = cliente_idcliente "
                                            + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                                            + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                                            + "AND facturado = 0 "
                                            + "AND idCliente = " + idCliente);
                                    llenarList(model.getRowCount());
                                    panelPagares.invalidate();
                                    revalidate();
                                    repaint();
                                } else {
                                    crearFactura("000000001");
                                    model.consulta("SELECT idpagare, nombre, fecha, total "
                                            + "FROM cliente, pagare "
                                            + "WHERE credito_cliente = 1 "
                                            + "AND idcliente = cliente_idcliente "
                                            + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                                            + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                                            + "AND facturado = 0 "
                                            + "AND idCliente = " + idCliente);
                                    llenarList(model.getRowCount());
                                    panelPagares.invalidate();
                                    revalidate();
                                    repaint();
                                }






                            } catch (SQLException ex) {
                                Logger.getLogger(PagaresAFactura.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (MalformedURLException ex) {
                                Logger.getLogger(PagaresAFactura.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (JAXBException ex) {
                                Logger.getLogger(PagaresAFactura.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(PagaresAFactura.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        barEstado.setIndeterminate(false);
                    }
                }).start();
            }
        }
    }//GEN-LAST:event_btnNotaCreditoActionPerformed

    private void txtALogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtALogKeyPressed
        // TODO add your handling code here:
        int c = evt.getKeyCode();
        if (c != KeyEvent.VK_UP && c != KeyEvent.VK_DOWN && c != KeyEvent.VK_LEFT && c != KeyEvent.VK_RIGHT
                && c != KeyEvent.VK_KP_UP && c != KeyEvent.VK_KP_DOWN && c != KeyEvent.VK_KP_LEFT && c != KeyEvent.VK_KP_RIGHT) {
            evt.consume();
        }
    }//GEN-LAST:event_txtALogKeyPressed

    private void stActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stActionPerformed


        if (st.isSelected()) {
            for (int i = 0; i < pagares.length; i++) {

                pagares[i].setSelected(true);

            }


        } else {


            for (int i = 0; i < pagares.length; i++) {

                pagares[i].setSelected(false);
                tp[i] = 0;

            }
            tpagare.setText("0.00");
            tgalones.setText("0.00");
        }

    }//GEN-LAST:event_stActionPerformed

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
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PagaresAFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PagaresAFactura("advjuan", "punk1991").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame Clientes;
    private javax.swing.JProgressBar barEstado;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnFacturas;
    private javax.swing.JButton btnNotaCredito;
    private javax.swing.JButton btnOK;
    private com.toedter.calendar.JDateChooser fechaF;
    private com.toedter.calendar.JDateChooser fechaI;
    private com.toedter.calendar.JDateChooser fechaa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblPagares;
    private javax.swing.JComboBox ordenar;
    private javax.swing.JPanel panelPagares;
    private javax.swing.JToggleButton st;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JLabel tgalones;
    private javax.swing.JLabel tpagare;
    private javax.swing.JTextArea txtAFacturas;
    private javax.swing.JTextArea txtALog;
    private javax.swing.JTextField txtBuscado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
