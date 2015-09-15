/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.ConnectionTableDB;
import ec.gob.sri.comprobantes.ws.Comprobante;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import facturacion.crear_clave_acceso;
import facturacion.crear_factura_credito;
import facturacion.crear_nota_credito_normal;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.PatternSyntaxException;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import reportes.reporte_factura;
import reportes.reporte_nota_credito;
import seguridad.CertificadosSSL;
import seguridad.FirmaDigital;
import util.ArchivosUtil;
import ws.AutorizacionComprobantesWS;
import ws.EnvioComprobantesWS;

/**
 *
 * @author Advantech Digital
 */
public class CreditoFactura extends javax.swing.JInternalFrame {

    private ConnectionTableDB model;
    private String USER = "";
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
    public String ambiente, razonSocial, nombreComercial, ruc, sec2, sec22, sec1, secuencial, direccion, emailEstacion;
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
    public CreditoFactura(String user, String password) {
        USER = user;
        PASSWORD = password;
        try {
            String consulta = "SELECT tipo_ambiente, razon_social, nombre_comercial, ruc, s2, secuencia1_factura, direccion, "
                    + "email_estacion, contribuyente_especial, obligado_llevar_contabilidad, iddatos_gasolinera, "
                    + "email_estacion "
                    + "FROM datos_gasolinera, punto_emision "
                    + "WHERE iddatos_gasolinera = datos_gasolinera_iddatos_gasolinera "
                    + "AND idpunto_emision = " + PTO_EMISION;
            System.out.println(consulta);
            model = new ConnectionTableDB(USER, PASSWORD, DB, consulta, false);
            idGasolinera = (Integer) model.getValueAt(0, 10);
            ambiente = (String) model.getValueAt(0, 0);
            razonSocial = (String) model.getValueAt(0, 1);
            nombreComercial = (String) model.getValueAt(0, 2);
            ruc = (String) model.getValueAt(0, 3);
            sec2 = (String) model.getValueAt(0, 4);

            sec1 = (String) model.getValueAt(0, 5);
            direccion = (String) model.getValueAt(0, 6);
            fechaEmision = String.format("%1$td/%1$tm/%1$tY", Calendar.getInstance());
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

            initComponents();
            setSize(1222, 620);
            fechaInicial.setCalendar(Calendar.getInstance());
            fechaFinal.setCalendar(Calendar.getInstance());
            txtAFacturas.setText("No existen facturas de crédito para el\ncliente y periodo seleccionados.");
            Clientes.setUndecorated(true);
            Clientes.setSize(1200, 560);
            sorter = new TableRowSorter<TableModel>(model);
            sorter.setRowFilter(null);
            tablaClientes.setRowSorter(sorter);


            String consulta1 = "SELECT s2 "
                    + "FROM datos_gasolinera, punto_emision "
                    + "WHERE iddatos_gasolinera = datos_gasolinera_iddatos_gasolinera "
                    + "AND idpunto_emision =1";
            System.out.println(consulta1);

            java.sql.ResultSet rs = model.stSentencias.executeQuery(consulta1);
            if (rs.next()) {
                sec22 = rs.getString(1);

            }


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
        jPanel1 = new javax.swing.JPanel();
        btnBuscarCliente = new javax.swing.JButton();
        btnFacturas = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tpagare = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        fechaInicial = new com.toedter.calendar.JDateChooser();
        fechaFinal = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelPagares = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtALog = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAFacturas = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        btnNotaCredito = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lblEstado = new javax.swing.JLabel();
        barEstado = new javax.swing.JProgressBar();

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

        btnBuscarCliente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnBuscarCliente.setText("Buscar Cliente de Crédito");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        btnFacturas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnFacturas.setText("Buscar Facturas");
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

        tpagare.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Total");

        fechaInicial.setDateFormatString("dd/MM/yyy");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId)
                            .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                            .addComponent(txtCliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 561, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tpagare, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnFacturas, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(fechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fechaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBuscarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel4))
                    .addComponent(fechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5))
                    .addComponent(fechaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tpagare, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanel6.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblHeader.setText(String.format("%15s   %-50s%15s%15s",
            "NÚMERO", "CLIENTE", "FECHA", "TOTAL"));
    jPanel6.add(lblHeader, java.awt.BorderLayout.NORTH);

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

    getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

    jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane2.setPreferredSize(new java.awt.Dimension(400, 400));

    txtAFacturas.setEditable(false);
    txtAFacturas.setColumns(10);
    txtAFacturas.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
    txtAFacturas.setRows(5);
    txtAFacturas.setTabSize(4);
    txtAFacturas.setPreferredSize(new java.awt.Dimension(400, 350));
    jScrollPane2.setViewportView(txtAFacturas);

    getContentPane().add(jScrollPane2, java.awt.BorderLayout.LINE_START);

    jPanel4.setPreferredSize(new java.awt.Dimension(1380, 80));

    btnNotaCredito.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    btnNotaCredito.setText("Crear Nota(s) de Crédito");
    btnNotaCredito.setEnabled(false);
    btnNotaCredito.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnNotaCreditoActionPerformed(evt);
        }
    });
    jPanel4.add(btnNotaCredito);

    jSeparator1.setPreferredSize(new java.awt.Dimension(1200, 10));
    jPanel4.add(jSeparator1);

    lblEstado.setPreferredSize(new java.awt.Dimension(1000, 25));
    jPanel4.add(lblEstado);

    barEstado.setPreferredSize(new java.awt.Dimension(200, 20));
    jPanel4.add(barEstado);

    getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_END);

    pack();
    }// </editor-fold>//GEN-END:initComponents
    Double total = 0.00;

    private void llenarList(int cantidad) {
        if (pagares != null) {
            for (int i = 0; i < pagares.length; i++) {
                panelPagares.remove(pagares[i]);
            }
        }

        pagares = new javax.swing.JCheckBox[cantidad];
        for (int i = 0; i < cantidad; i++) {
            pagares[i] = new javax.swing.JCheckBox();
            pagares[i].setText(String.format("%20s   %-70s\t%15s\t%20.2f",
                    (String) model.getValueAt(i, 0),
                    (String) model.getValueAt(i, 1),
                    String.format("%1$td/%1$tm/%1$tY", model.getValueAt(i, 2)),
                    (model.getValueAt(i, 3) != null) ? ((java.math.BigDecimal) model.getValueAt(i, 3)).doubleValue() : 0.00));
            pagares[i].setFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));
            pagares[i].setBorderPaintedFlat(true);
            pagares[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    try {
                        String num = ((javax.swing.JCheckBox) evt.getComponent()).getText().substring(0, 20).trim();

                        javax.swing.JCheckBox pa;
                        pa = (javax.swing.JCheckBox) evt.getComponent();

                        if (pa.isSelected()) {
                            total = total - Double.parseDouble(pa.getText().trim().substring(107).trim().replace(",", "."));
                            tpagare.setText(String.valueOf(total));
                        } else {

                            total = total + Double.parseDouble(pa.getText().trim().substring(107).trim().replace(",", "."));
                            tpagare.setText(String.valueOf(total));



                        }
                        model.consulta("SELECT fecha FROM factura WHERE numero = '" + num + "'");
                        String fecha = String.format("%tF", model.getValueAt(0, 0));
                        showFact(num, fecha, true);
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

    private void showFact(String idFactura, String fecha, boolean consulta) throws SQLException, IllegalStateException {
        try (java.sql.ResultSet rsDatos = model.stSentencias.executeQuery("SELECT cupo_cliente, producto.nombre, "
                + "cantidad, punit, factura_detalle.subtotal, factura_detalle.iva, factura_detalle.total, email, idfactura "
                + "FROM cliente, producto, factura_detalle, factura "
                + "WHERE idfactura = factura_idfactura "
                + "AND factura.cliente_idcliente = idcliente "
                + "AND idproducto = producto_idproducto "
                + "AND idcliente = " + idCliente + " "
                + "AND numero = '" + idFactura + "'")) {
            String credito = "0.00", producto = "", cantidad = "0.000", precioU = "0.000", subtotal = "0.00", iva = "0.000", total = "0.00";
            if (rsDatos.next()) {
                credito = String.format("%.2f", rsDatos.getBigDecimal(1));
                producto = String.format("%s", rsDatos.getString(2)).toUpperCase();
                cantidad = String.format("%.3f", rsDatos.getBigDecimal(3));
                precioU = String.format("%.3f", rsDatos.getBigDecimal(4));
                subtotal = String.format("%.2f", rsDatos.getBigDecimal(5));
                iva = String.format("%.3f", rsDatos.getBigDecimal(6));
                total = String.format("%.2f", rsDatos.getBigDecimal(7));
                email = rsDatos.getString(8);
                this.idFactura = rsDatos.getInt(9);
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

            txtAFacturas.setText(String.format("Factura Seleccionada: %s\n"
                    + "Fecha:                %s\n\n"
                    + "EMISOR:\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s\n\n"
                    + "CLIENTE:\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s\n\n"
                    + "DETALLE:\n"
                    + "%-15s%20s\n"
                    + "%-15s%20s galones.\n"
                    + "%-15s%20s\n"
                    + "%-15s%19s\n"
                    + "%-15s%20s\n"
                    + "%-15s%19s\n",
                    idFactura,
                    fecha,
                    "Nombre: ", razonSocial,
                    "Comercial: ", nombreComercial,
                    "Id. Emisor: ", ruc,
                    "Nombre: ", txtCliente.getText(),
                    "Id: ", txtId.getText(),
                    "Email: ", email,
                    "CUPO CREDITO: ", credito,
                    "Producto: ", producto,
                    "Cantidad: ", cantidad,
                    "PPU: ", "$ " + precioU,
                    "Subtotal: ", "$ " + subtotal,
                    "IVA: ", "$ " + iva,
                    "Total Factura: ", "$ " + total));
        }
    }

    private void prepareInfo(int index, String num, String fecha) throws SQLException, IllegalStateException {
        factura = num;
        System.out.println(factura + ", Fila: " + index);
        fechaFactura = fecha;
        showFact(factura, fechaFactura, false);
        try (java.sql.ResultSet rsClave = model.stSentencias.executeQuery("SELECT idclave_acceso, clave_acceso FROM clave_acceso WHERE estado = 0 AND tipo = 'contingencia'")) {
            if (rsClave.first()) {
                idClave = rsClave.getInt(1);
                claveAcceso = rsClave.getString(2);
            }
        }
        model.psPrepararSentencias = model.coneccion.prepareStatement("UPDATE clave_acceso SET estado = 1 WHERE idclave_acceso = " + idClave);
        model.psPrepararSentencias.executeUpdate();
        model.psPrepararSentencias.close();
    }

    private void crearNormal(String num) throws SQLException, MalformedURLException, JAXBException, IOException, javax.xml.ws.WebServiceException {
        try (java.sql.ResultSet rs = model.stSentencias.executeQuery("SELECT certificado_digital FROM datos_gasolinera")) {
            if (rs.next()) {
                PKCS12_RESOURCE = rs.getBlob(1).getBinaryStream();
            }
        }
        model.ejecutar("UPDATE clave_acceso SET estado = 0 WHERE idclave_acceso = " + idClave + "");
        String clave = new crear_clave_acceso().crear_clave_acceso(fechaEmision.replace("/", ""), "04", ruc, ambiente,
                sec1 + sec22, num, "12345678", "1");
        txtALog.append("Clave de acceso: " + clave + " generada.\n");
        notaCredito = new crear_nota_credito_normal().crear_nota_credito_normal(prod.substring(0, 1), nombreComercial, contabilidad, Integer.parseInt(contribuyente), ambiente,
                razonSocial, ruc, clave, sec1, sec22, num, direccion, fechaEmision, tipo, nCliente.replace("ñ", "n").replace("Ñ", "N"), idC,
                subtotalT, ivaT, totalT, prod.substring(2), cantidadT, ppuT, fechaFactura, factura, "ANULACION");

        if (notaCredito == 1) {
            txtALog.append("Nota de Credito: " + sec1 + "-" + sec22 + "-" + num + " creada correctamente.\n");
            model.ejecutar("INSERT INTO clave_acceso (clave_acceso, fecha, estado, tipo) VALUES ('" + clave + "', CURDATE(), 1, 'normal')");
            txtALog.append("Clave de acceso: " + clave + " grabada correctamente.\n");
            try (java.sql.ResultSet rsIdClave = model.stSentencias.executeQuery("SELECT idclave_acceso FROM clave_acceso WHERE clave_acceso = '" + clave + "'")) {
                if (rsIdClave.first()) {
                    idClave = rsIdClave.getInt(1);
                }
            }

            FirmaDigital fd = new FirmaDigital("notas_generadas\\nc" + sec1 + "-" + sec22 + "-" + num + ".xml", "notas_firmadas\\nota_firmada" + sec1 + "-" + sec22 + "-" + num + ".xml");
            fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
            fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);
            fd.firmarDocumentoXML();
            txtALog.append("Nota de Credito: " + sec1 + "-" + sec22 + "-" + num + " firmada correctamente.\n");

            //File xsd = new File("factura_v1.0.0.xsd");
            File temp = new File("notas_firmadas\\nota_firmada" + sec1 + "-" + sec22 + "-" + num + ".xml");

            CertificadosSSL.instalarCertificados();
            RespuestaSolicitud response;
            EnvioComprobantesWS ws = new EnvioComprobantesWS(Ewsri);
            response = ws.enviarComprobante(temp);
            System.out.println(response.getEstado());

            System.out.println("---");
            RespuestaSolicitud.Comprobantes comprobantes = response.getComprobantes();
            if (comprobantes != null) {
                List<Comprobante> listaComprobantes = comprobantes.getComprobante();
                if (!listaComprobantes.isEmpty()) {
                    for (Comprobante comprobante : listaComprobantes) {
                        List<ec.gob.sri.comprobantes.ws.Mensaje> mensajes = comprobante.getMensajes().getMensaje();
                        for (ec.gob.sri.comprobantes.ws.Mensaje mensaje : mensajes) {
                            System.out.println(mensaje.getIdentificador() + "\t" + mensaje.getMensaje() + "\t" + mensaje.getInformacionAdicional());
                        }
                    }
                }
            }
            txtALog.append("Autorizando nota de credito " + sec1 + "-" + sec22 + "-" + num + "\n");

            //RespuestaSolicitud response = ws.enviarComprobante(new File("D:\\ejemplos\\Firmados\\pruebaguia.xml"));

            if (response.getEstado().equalsIgnoreCase("RECIBIDA")) {
                List<Autorizacion> listaAutorizaciones = null;
                ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante respuesta;
                int c = 0;
                try {
                    respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                    listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();

                    while (listaAutorizaciones.isEmpty() && c < 50) {
                        respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                        listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                        System.out.println();
                        c++;
                    }
                } catch (javax.xml.ws.WebServiceException ex) {
                    System.err.println(ex.getMessage());
                    try {
                        model.ejecutar("DELETE FROM clave_acceso "
                                + "WHERE clave_acceso = '" + clave + "'");
                    } catch (SQLException ex1) {
                        System.err.println(ex1.getMessage());
                    }
                }
                /*} catch (java.lang.NullPointerException ex) {
                 respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                 listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                 while (listaAutorizaciones.isEmpty()) {
                 respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                 listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                 System.out.println();
                 c++;
                 }
                 }*/

                System.out.println("Intentos " + c);
                System.out.println(listaAutorizaciones != null ? listaAutorizaciones.size() : 0);

                if (listaAutorizaciones != null) {
                    for (Autorizacion autorizacion : listaAutorizaciones) {
                        txtALog.append("Entro a autorizacion: Nota de Credito " + sec1 + "-" + sec22 + "-" + num + "\n");
                        //Lógica de acceso a datos
                        String nAuto = autorizacion.getNumeroAutorizacion();
                        XMLGregorianCalendar fAuto = autorizacion.getFechaAutorizacion();
                        //--
                        String estado = autorizacion.getEstado().toUpperCase();
                        String ta;
                        FileInputStream in;

                        if (estado.compareTo("AUTORIZADO") == 0) {
                            ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "notas_autorizadas\\nota_autorizada" + sec1 + "-" + sec22 + "-" + num + ".xml");

                            ta = (ambiente.equalsIgnoreCase("1") ? "Pruebas" : "Produccion");
                            String motivo = "ANULACION";

                            txtALog.append("Enviando correo al cliente: " + txtCliente.getText() + ".\n");
                            reporte_nota_credito rnc = new reporte_nota_credito();
                            rnc.reporte_factura(sec1 + "-" + sec22 + "-" + num, ruc, direccion, direccion, contabilidad, contribuyente, nAuto, String.valueOf(fAuto), clave, "Normal", ta, razonSocial, nombreComercial, txtCliente.getText(), idC, fechaEmision, "FACTURA", factura, fechaFactura, motivo, "", String.valueOf(subtotalT), String.valueOf(ivaT), String.valueOf(totalT), "0", prod.substring(0, 1), prod.substring(2), String.valueOf(cantidadT), prod.substring(2), String.valueOf(ppuT), "0");
                            boolean hasEmail = rnc.enviar_email(passwEmail, emailEstacion, "notas_autorizadas\\nota_autorizada" + sec1 + "-" + sec22 + "-" + num + ".xml", "pdf\\nota" + sec1 + "-" + sec22 + "-" + num + ".pdf", email, txtCliente.getText(), idC, sec1 + "-" + sec22 + "-" + num + ".pdf", "nota" + sec1 + "-" + sec22 + "-" + num + ".xml");
                            rnc.run();
                            if (hasEmail) {
                                txtALog.append("Archivos enviados exitosamente al correo: " + email + ".\n");
                            } else {
                                txtALog.append("No se pudo enviar los archivos al correo: Cliente sin correo.\n");
                            }

                            temp = new File("notas_autorizadas\\nota_autorizada" + sec1 + "-" + sec22 + "-" + num + ".xml");
                            model.ejecutar("INSERT INTO nota_credito (factura_idfactura, factura_cliente_idcliente, usuarios_idusuarios, clave_acceso_idclave_acceso, valor_modificacion, motivo, estado, autorizacion, numero, fecha) "
                                    + "VALUES (" + idFactura + ", " + idCliente + ", " + idUser + ", '" + idClave + "', " + totalT + ", '" + motivo + "', 'AUTORIZADO', '" + nAuto + "', '" + sec1 + "-" + sec22 + "-" + num + "', '" + String.format("%tF", Calendar.getInstance()) + "')");
                            model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_enviados_autorizados (doc_xml, xml_factura, tipo_doc) "
                                    + "VALUES(?, '" + idFactura + "', 'Nota de Credito')");
                            in = new FileInputStream(temp);
                            model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
                            model.psPrepararSentencias.executeUpdate();
                            model.psPrepararSentencias.close();
                            txtALog.append("Nota de credito: " + sec1 + "-" + sec22 + "-" + num + " AUTORIZADA y grabada correctamente.\n"
                                    + "----------------------------------------------------------------\n");
                        } else {
                            int con = 0;
                            String mensajeE = null;
                            List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                            for (Mensaje mensaje : mensajes) {
                                ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "notas_no_autorizados\\" + sec1 + "-" + sec22 + "-" + num + ".xml");
                                temp = new File("notas_no_autorizados\\" + sec1 + "-" + sec22 + "-" + num + ".xml");
                                con++;
                                if (con == 1) {
                                    mensajeE = mensaje.getMensaje();
                                }
                            }
                            in = new FileInputStream(temp);
                            model.ejecutar("INSERT INTO nota_credito (factura_idfactura, factura_cliente_idcliente, usuarios_idusuarios, clave_acceso_idclave_acceso, valor_modificacion, motivo, estado, numero, fecha) "
                                    + "VALUES (" + idFactura + ", " + idCliente + ", " + idUser + ", " + Integer.parseInt(num) + ", " + totalT + ", 'ANULACION', 'No Autorizado', '" + sec1 + "-" + sec22 + "-" + num + "', '" + String.format("%tF", Calendar.getInstance()) + "')");
                            model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_no_autorizados (doc_xml, xml_factura, motivo_no_autorizado) "
                                    + "VALUES(?, '" + idFactura + "','" + mensajeE + "')");
                            model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
                            model.psPrepararSentencias.executeUpdate();
                            model.psPrepararSentencias.close();
                            txtALog.append("Nota de credito: " + sec1 + "-" + sec22 + "-" + num + " No Autorizada.\n \tDebido a: " + mensajeE + "\n"
                                    + "----------------------------------------------------------------\n");
                        }
                        in.close();
                    }
                } else {
                    txtALog.append("Servicio de Autorizacion del SRI sin conexión.");
                    throw new IllegalStateException("Servicio de Autorizacion SRI sin conexión.");
                }
            }
        }
    }

    private void anularFactura(String num) throws SQLException, IllegalStateException {
        model.psPrepararSentencias = model.coneccion.prepareStatement("UPDATE factura SET Estado_factura = 'ANULADO' "
                + "WHERE numero = '" + num + "'");
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
        int idpro[] = null;
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

        subtotalT = 0.00;
        ivaT = 0.00;
        totalT = 0.00;
        String[] ids;
        if (id.length() != 0) {
            ids = id.split(",");
            System.out.println(Arrays.toString(ids));
            int cant = ids.length, i = 0;
            productos = new String[cant];
            idpro = new int[cant];
            ppus = new Double[cant];
            cantidades = new Double[cant];
            subtotales = new Double[cant];
            ivas = new Double[cant];
            totales = new Double[cant];

            for (String index : ids) {
                switch (index) {
                    case "1":
                        idpro[i] = 1;
                        productos[i] = Extra;
                        ppus[i] = ppuE;
                        cantidades[i] = cantidadE;
                        subtotales[i] = subtotalE;
                        ivas[i] = ivaE;
                        totales[i] = totalE;
                        break;
                    case "2":
                        idpro[i] = 2;
                        productos[i] = Super;
                        ppus[i] = ppuS;
                        cantidades[i] = cantidadS;
                        subtotales[i] = subtotalS;
                        ivas[i] = ivaS;
                        totales[i] = totalS;
                        break;
                    case "3":
                        idpro[i] = 3;
                        productos[i] = Diesel;
                        ppus[i] = ppuD;
                        cantidades[i] = cantidadD;
                        subtotales[i] = subtotalD;
                        ivas[i] = ivaD;
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
            String clave = new crear_clave_acceso().crear_clave_acceso(fechaEmision.replace("/", ""), "01", ruc, ambiente,
                    sec1 + sec2, num, "12345678", "1");
            txtALog.append("Clave de acceso: " + clave + " generada.\n");
            Factura = new crear_factura_credito().crear_factura_credito("01", ids, nombreComercial, contabilidad, Integer.parseInt(contribuyente), ambiente,
                    razonSocial, ruc, clave, sec1, sec2, num, direccion, fechaEmision, tipo, nCliente.replace("ñ", "n").replace("Ñ", "N"), idC,
                    subtotalT, ivaT, totalT, productos, cantidades, ppus, ivas, totales, subtotales);

            if (Factura == 1) {
                txtALog.append("Factura de Credito: " + sec1 + "-" + sec2 + "-" + num + " creada correctamente.\n");
                model.ejecutar("INSERT INTO clave_acceso (clave_acceso, fecha, estado, tipo) VALUES ('" + clave + "', CURDATE(), 1, 'normal')");
                txtALog.append("Clave de acceso: " + clave + " grabada correctamente.\n");
                try (java.sql.ResultSet rsIdClave = model.stSentencias.executeQuery("SELECT idclave_acceso FROM clave_acceso WHERE clave_acceso = '" + clave + "'")) {
                    if (rsIdClave.first()) {
                        idClave = rsIdClave.getInt(1);
                    }
                }

                FirmaDigital fd = new FirmaDigital("generados\\" + sec1 + "-" + sec2 + "-" + num + ".xml", "firmados\\firmado" + sec1 + "-" + sec2 + "-" + num + ".xml");
                fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
                fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);
                fd.firmarDocumentoXML();
                txtALog.append("Factura de Credito: " + sec1 + "-" + sec2 + "-" + num + " firmada correctamente.\n");

                //File xsd = new File("factura_v1.0.0.xsd");
                File temp = new File("firmados\\firmado" + sec1 + "-" + sec2 + "-" + num + ".xml");

                CertificadosSSL.instalarCertificados();
                RespuestaSolicitud response;
                EnvioComprobantesWS ws = new EnvioComprobantesWS(Ewsri);
                response = ws.enviarComprobante(temp);
                System.out.println(response.getEstado());

                System.out.println("---");
                RespuestaSolicitud.Comprobantes comprobantes = response.getComprobantes();
                if (comprobantes != null) {
                    List<Comprobante> listaComprobantes = comprobantes.getComprobante();
                    if (!listaComprobantes.isEmpty()) {
                        for (Comprobante comprobante : listaComprobantes) {
                            List<ec.gob.sri.comprobantes.ws.Mensaje> mensajes = comprobante.getMensajes().getMensaje();
                            for (ec.gob.sri.comprobantes.ws.Mensaje mensaje : mensajes) {
                                System.out.println(mensaje.getIdentificador() + "\t" + mensaje.getMensaje() + "\t" + mensaje.getInformacionAdicional());
                            }
                        }
                    }
                }
                txtALog.append("Autorizando Factura de Credito " + sec1 + "-" + sec2 + "-" + num + "\n");

                //RespuestaSolicitud response = ws.enviarComprobante(new File("D:\\ejemplos\\Firmados\\pruebaguia.xml"));

                if (response.getEstado().equalsIgnoreCase("RECIBIDA")) {
                    List<Autorizacion> listaAutorizaciones = null;
                    ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante respuesta;
                    int c = 0;
                    try {
                        respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                        listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();

                        while (listaAutorizaciones.isEmpty() && c < 50) {
                            respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                            listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                            System.out.println();
                            c++;
                        }
                    } catch (javax.xml.ws.WebServiceException ex) {
                        System.err.println(ex.getMessage());
                        try {
                            model.ejecutar("DELETE FROM clave_acceso "
                                    + "WHERE clave_acceso = '" + clave + "'");
                        } catch (SQLException ex1) {
                            System.err.println(ex1.getMessage());
                        }
                    }
                    /*} catch (java.lang.NullPointerException ex) {
                     respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                     listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                     while (listaAutorizaciones.isEmpty()) {
                     respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(clave);
                     listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                     System.out.println();
                     c++;
                     }
                     }*/

                    System.out.println("Intentos " + c);
                    System.out.println(listaAutorizaciones != null ? listaAutorizaciones.size() : 0);

                    if (listaAutorizaciones != null) {
                        for (Autorizacion autorizacion : listaAutorizaciones) {
                            txtALog.append("Entro a autorizacion: Factura de Credito " + sec1 + "-" + sec2 + "-" + num + "\n");
                            //Lógica de acceso a datos
                            String nAuto = autorizacion.getNumeroAutorizacion();
                            XMLGregorianCalendar fAuto = autorizacion.getFechaAutorizacion();
                            //--
                            String estado = autorizacion.getEstado().toUpperCase();
                            String ta;
                            FileInputStream in;

                            if (estado.compareTo("AUTORIZADO") == 0) {
                                ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "autorizados\\autorizado" + sec1 + "-" + sec2 + "-" + num + ".xml");

                                ta = (ambiente.equalsIgnoreCase("1") ? "Pruebas" : "Produccion");



                                reporte_factura rf = new reporte_factura();

                                //private Double[] cantidades, ppus, subtotales, ivas, totales;
                                rf.reporte_factura(sec1 + "-" + sec2 + "-" + num, ruc, direccion, direccion, contabilidad, String.valueOf(contribuyente), nAuto, String.valueOf(fAuto), clave, "Normal", ta, razonSocial, nombreComercial, txtCliente.getText(), idC, fechaEmision, "", String.valueOf(subtotalT), String.valueOf(ivaT), String.valueOf(totalT), "0", prod.substring(0, 1), productos, cantidades, productos, ppus, "0", subtotales);

                                rf.enviar_email(passwEmail, emailEstacion, "autorizados\\autorizado" + sec1 + "-" + sec2 + "-" + num + ".xml", "pdf\\" + sec1 + "-" + sec2 + "-" + num + ".pdf", email, txtCliente.getText(), razonSocial, sec1 + "-" + sec2 + "-" + num + ".pdf", sec1 + "-" + sec2 + "-" + num + ".xml");
                                rf.run();

                                temp = new File("autorizados\\autorizado" + sec1 + "-" + sec2 + "-" + num + ".xml");
                                model.ejecutar("INSERT INTO factura (numero, fecha, hora, metodo_pago, punto_emision_idpunto_emision, "
                                        + "cliente_idcliente, usuarios_idusuarios, datos_gasolinera_iddatos_gasolinera, "
                                        + "subtotal, iva, total) "
                                        + "VALUES ('" + sec1 + "-" + sec2 + "-" + num + "', '" + String.format("%tF", Calendar.getInstance()) + "', '" + String.format("%tT", Calendar.getInstance())
                                        + "', '" + pago + "', " + PTO_EMISION + ", " + idCliente + ", " + idUser + ", " + idGasolinera + ", " + subtotalT + ", " + ivaT + ", " + totalT + ")");
                                try (java.sql.ResultSet rsFact = model.stSentencias.executeQuery("SELECT idfactura FROM factura "
                                        + "WHERE numero = '" + sec1 + "-" + sec2 + "-" + num + "'")) {
                                    if (rsFact.first()) {
                                        idFactura = rsFact.getInt(1);
                                    }
                                }
                                for (int i = 0; i < productos.length; i++) {


                                    model.ejecutar("INSERT INTO factura_detalle (cantidad, subtotal, iva, total, factura_idfactura, producto_idproducto) "
                                            + "VALUES (" + cantidades[i] + ", " + subtotales[i] + ", " + ivas[i] + ", " + totales[i] + ", " + idFactura + ", " + idpro[i] + ")");
                                }
                                model.ejecutar("UPDATE factura SET Estado_factura = 'AUTORIZADO', "
                                        + "clave_acceso_idclave_acceso = " + idClave + ", "
                                        + "numero_autorizacion = '" + nAuto + "' "
                                        + "WHERE idfactura = " + idFactura);
                                model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_enviados_autorizados (doc_xml, xml_factura, tipo_doc) "
                                        + "VALUES(?, '" + idFactura + "', 'Factura')");
                                in = new FileInputStream(temp);
                                model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
                                model.psPrepararSentencias.executeUpdate();
                                model.psPrepararSentencias.close();
                                txtALog.append("Factura de credito: " + sec1 + "-" + sec2 + "-" + num + " AUTORIZADA y grabada correctamente.\n"
                                        + "----------------------------------------------------------------\n");
                            } else {
                                int con = 0;
                                String mensajeE = null;
                                List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                                for (Mensaje mensaje : mensajes) {
                                    ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "no_autorizados\\" + sec1 + "-" + sec2 + "-" + num + ".xml");
                                    temp = new File("no_autorizados\\" + sec1 + "-" + sec2 + "-" + num + ".xml");
                                    con++;
                                    if (con == 1) {
                                        mensajeE = mensaje.getMensaje();
                                    }
                                }
                                in = new FileInputStream(temp);

                                model.ejecutar("INSERT INTO factura (numero, fecha, hora, metodo_pago, punto_emision_idpunto_emision, "
                                        + "cliente_idcliente, usuarios_idusuarios, datos_gasolinera_iddatos_gasolinera) "
                                        + "VALUES ('" + sec1 + "-" + sec2 + "-" + num + "', '" + String.format("%tF", Calendar.getInstance()) + "', '" + String.format("%tT", Calendar.getInstance())
                                        + "', '" + pago + "', " + PTO_EMISION + ", " + idCliente + ", " + idUser + ", " + idGasolinera + ")");
                                try (java.sql.ResultSet rsFact = model.stSentencias.executeQuery("SELECT idfactura FROM factura "
                                        + "WHERE numero = '" + sec1 + "-" + sec2 + "-" + num + "'")) {
                                    if (rsFact.first()) {
                                        idFactura = rsFact.getInt(1);
                                    }
                                }
                                for (int i = 0; i < productos.length; i++) {
                                    model.ejecutar("INSERT INTO factura_detalle (cantidad, subtotal, iva, total, factura_idfactura, producto_idproducto) "
                                            + "VALUES (" + cantidades[i] + ", " + subtotales[i] + ", " + ivas[i] + ", " + totales[i] + ", " + idFactura + ", " + prod.substring(0, 1) + ")");
                                }
                                model.ejecutar("UPDATE factura SET Estado_factura = 'No Autorizado', "
                                        + "clave_acceso_idclave_acceso = " + idClave + ", "
                                        + "numero_autorizacion = '" + nAuto + "' "
                                        + "WHERE idfactura = " + idFactura);
                                model.psPrepararSentencias = model.coneccion.prepareStatement("INSERT INTO adv_xml.xml_no_autorizados (doc_xml, xml_factura, motivo_no_autorizado) "
                                        + "VALUES(?, '" + idFactura + "','" + mensajeE + "')");
                                model.psPrepararSentencias.setBinaryStream(1, in, (int) temp.length());
                                model.psPrepararSentencias.executeUpdate();
                                model.psPrepararSentencias.close();
                                txtALog.append("Factura de credito: " + sec1 + "-" + sec2 + "-" + num + " No Autorizada.\n \tDebido a: " + mensajeE + "\n"
                                        + "----------------------------------------------------------------\n");
                            }
                            in.close();
                        }
                    }
                }
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
            model.consulta("SELECT DISTINCT idcliente, cedula_ruc, nombre, tipo_identificacion, direccion, telefono, "
                    + "(SELECT codigo FROM codigos WHERE idcliente = cliente_idcliente) AS codigo "
                    + "FROM cliente, codigos WHERE credito_cliente = 1 AND estado = 'AUTORIZADO' LIMIT 100");
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
            } else {
                JOptionPane.showMessageDialog(this, "No ha seleccionado ningún cliente.");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void tablaClientesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMousePressed
        // TODO add your handling code here:
        rowClientes = tablaClientes.rowAtPoint(evt.getPoint());
        txtBuscado.setText(String.format("Seleccionado: %s", (String) tablaClientes.getValueAt(rowClientes, 2)));
    }//GEN-LAST:event_tablaClientesMousePressed

    private void btnFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFacturasActionPerformed
        if (txtCliente.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "No ha seleccionado ningún cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
               int añoi = fechaInicial.getCalendar().get(Calendar.YEAR);
               int mesi = fechaInicial.getCalendar().get(Calendar.MONTH) + 1;
               int diai = fechaInicial.getCalendar().get(Calendar.DAY_OF_MONTH);
               
                int añof = fechaFinal.getCalendar().get(Calendar.YEAR);
               int mesf = fechaFinal.getCalendar().get(Calendar.MONTH) + 1;
               int diaf = fechaFinal.getCalendar().get(Calendar.DAY_OF_MONTH);
                
                
                fechaIn = dateFormat.parse(diai+"/"+mesi+"/"+añoi);
                fechaFin = dateFormat.parse(diaf+"/"+mesf+"/"+añof);
              
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                model.consulta("SELECT numero AS NUMERO, nombre AS NOMBRE, fecha AS FECHA, total AS TOTAL "
                        + "FROM cliente, factura "
                        + "WHERE idCliente = cliente_idcliente "
                        + "AND metodo_pago = 'Credito' "
                        + "AND punto_emision_idpunto_emision = 1 "
                        + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                        + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                        + "AND Estado_factura = 'AUTORIZADO' "
                        + "AND idCliente = " + idCliente);
               
                System.out.println("SELECT numero AS NUMERO, nombre AS NOMBRE, fecha AS FECHA, total AS TOTAL "
                        + "FROM cliente, factura "
                        + "WHERE idCliente = cliente_idcliente "
                        + "AND metodo_pago = 'Credito' "
                        + "AND punto_emision_idpunto_emision = 1 "
                        + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                        + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                        + "AND Estado_factura = 'AUTORIZADO' "
                        + "AND idCliente = " + idCliente);
                
                llenarList(model.getRowCount());
                panelPagares.invalidate();
                revalidate();
                repaint();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
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
        try {
            model.consulta("SELECT numero AS NUMERO, nombre AS NOMBRE, fecha AS FECHA, total AS TOTAL "
                    + "FROM cliente, factura "
                    + "WHERE credito_cliente = 1 "
                    + "AND idCliente = cliente_idcliente "
                    + "AND metodo_pago = 'Credito' "
                    + "AND punto_emision_idpunto_emision = 1 "
                    + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                    + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                    + "AND Estado_factura = 'AUTORIZADO' "
                    + "AND idCliente = " + idCliente);
        } catch (SQLException | IllegalStateException ex) {
            System.err.println(ex.getMessage());
        }
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
        final String[] fecha = new String[selectedRows.length];
        c = 0;
        for (int row : selectedRows) {
            numero[c] = (String) model.getValueAt(row, 0);
            fecha[c] = String.format("%1$td/%1$tm/%1$tY", model.getValueAt(row, 2));
            ++c;
        }
        int option = javax.swing.JOptionPane.showConfirmDialog(this,
                "Esta seguro de CREAR y AUTORIZAR " + selectedRows.length + " Nota(s) de Crédito?",
                "Confirmar Nota(s) de Crédito",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (option == javax.swing.JOptionPane.YES_OPTION) {
            barEstado.setIndeterminate(true);
            nCliente = txtCliente.getText();
            idC = txtId.getText();
            btnNotaCredito.setEnabled(false);
            if (pagares != null && selectedRows.length > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean[] error = new boolean[numero.length];
                        boolean[] autorizado = new boolean[numero.length];
                        String[] notas = new String[numero.length];
                        int i = 0;
                        for (int row : selectedRows) {
                            try {
                                error[i] = false;
                                prepareInfo(row, numero[i], fecha[i]);
                                lblEstado.setText("Creando y autorizando notas de credito...");
                                boolean sri;
                                if ((sri = ArchivosUtil.existeConexion(Ewsri, Awsri))) {
                                    System.out.println(sri ? "Conectado SRI" : "Sin conexion SRI");
                                    String num;
                                    try {
                                        try (java.sql.ResultSet rsNum = model.stSentencias.executeQuery("SELECT numero FROM nota_credito ORDER BY numero DESC LIMIT 1")) {
                                            if (rsNum.first()) {
                                                num = String.format("%09d", Integer.parseInt(rsNum.getString(1).substring(8)) + 1);
                                                notas[i] = sec1 + "-" + sec22 + "-" + num;
                                                crearNormal(num);
                                                lblEstado.setText("Creando y autorizando notas de credito... "
                                                        + "Nota de Credito: " + sec1 + "-" + sec22 + "-" + num);
                                                model.consulta("SELECT estado FROM nota_credito "
                                                        + "WHERE factura_idfactura = (SELECT idfactura FROM factura WHERE numero = '" + numero[i] + "')");
                                                if (((String) model.getValueAt(0, 0)).equalsIgnoreCase("Autorizado")) {
                                                    autorizado[i] = true;
                                                } else {
                                                    autorizado[i] = false;
                                                }
                                            }
                                        }
                                        lblEstado.setText("Notas de credito " + Arrays.toString(numero) + " Autorizadas correctamente.");
                                    } catch (SQLException | IllegalStateException | JAXBException | IOException | javax.xml.ws.WebServiceException ex) {
                                        System.err.println(ex.getMessage());
                                        txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                                + "----------------------------------------------------------------\n");
                                        error[i] = true;
                                        autorizado[i] = false;
                                    }
                                } else {
                                    txtALog.append("No hay conexion con el SRI\n"
                                            + "----------------------------------------------------------------\n");
                                    lblEstado.setText("No es posible enviar las notas de credito.");
                                    error[i] = true;
                                    autorizado[i] = false;
                                }
                                model.consulta("SELECT cedula_ruc AS 'CEDULA/RUC', nombre AS NOMBRE, numero AS NUMERO, fecha AS FECHA "
                                        + "FROM cliente, factura "
                                        + "WHERE credito_cliente = 1 "
                                        + "AND idCliente = cliente_idcliente "
                                        + "AND metodo_pago = 'Credito' "
                                        + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                                        + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                                        + "AND Estado_factura = 'AUTORIZADO' "
                                        + "AND idCliente = " + idCliente);
                                /*System.out.println(String.format(ambiente + ", 1, " + razonSocial + ", " + nombreComercial + ", "
                                 + ruc + ", 04, " + sec2 + ", " + sec1 + ", " + factura.substring(8) + ", " + direccion + "\n"
                                 + fechaEmision + ", " + direccion + ", " + tipo + ", " + nCliente + ", " + idC + ", "
                                 + contribuyente + ", " + contabilidad + ", rise, 01, " + factura + ", " + fechaFactura + ", " + subtotalT + ", " + totalT + "\n"
                                 + prod.substring(0, 1) + ", " + prod.substring(2) + ", "
                                 + cantidadT + ", " + ppuT + ", " + subtotalT + ", " + ivaT + ", " + totalT));*/
                                ++i;
                            } catch (SQLException | IllegalStateException ex) {
                                System.err.println(ex.getMessage());
                                txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                        + "----------------------------------------------------------------\n");
                                error[i] = true;
                            }
                        }

                        for (i = 0; i < numero.length; i++) {
                            try {
                                if (!error[i]) {
                                    anularFactura(numero[i]);
                                }
                            } catch (SQLException | IllegalStateException ex) {
                                System.err.println(ex.getMessage());
                                txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                        + "----------------------------------------------------------------\n");
                            }
                        }
                        boolean aut = false;
                        for (int j = 0; j < autorizado.length; j++) {
                            if (autorizado[j]) {
                                aut = true;
                            } else {
                                aut = false;
                                break;
                            }
                        }
                        barEstado.setIndeterminate(false);

                        lblEstado.setText("Creando y autorizando Factura de Credito.");
                        /*Object[] pagos = {"Contado", "TC", "Cheque"};
                         pago = (String) JOptionPane.showInputDialog(getContentPane(), "Seleccione la forma de pago:",
                         "Confirmar Factura de Crédito ",
                         JOptionPane.INFORMATION_MESSAGE,
                         null, pagos, "Contado");
                         System.out.println(pago);*/
                        pago = "Credito";
                        if (pago != null) {
                            barEstado.setIndeterminate(true);
                            if (aut) {
                                String num = "";
                                try {
                                    try (java.sql.ResultSet rsNum = model.stSentencias.executeQuery("SELECT numero FROM factura, punto_emision "
                                            + "WHERE punto_emision_idpunto_emision = idpunto_emision AND idpunto_emision = " + PTO_EMISION + " "
                                            + "ORDER BY numero DESC LIMIT 1")) {
                                        if (rsNum.first()) {
                                            num = String.format("%09d", Integer.parseInt(rsNum.getString(1).substring(8)) + 1);
                                            crearFactura(num);
                                            model.consulta("SELECT numero AS NUMERO, nombre AS NOMBRE, fecha AS FECHA, total AS TOTAL "
                                                    + "FROM cliente, factura "
                                                    + "WHERE credito_cliente = 1 "
                                                    + "AND idCliente = cliente_idcliente "
                                                    + "AND metodo_pago = 'Credito' "
                                                    + "AND punto_emision_idpunto_emision = 1 "
                                                    + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                                                    + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                                                    + "AND Estado_factura = 'AUTORIZADO' "
                                                    + "AND idCliente = " + idCliente);
                                            lblEstado.setText("Factura de Credito " + sec1 + "-" + sec2 + "-" + num + " Autorizada correctamente.");
                                            llenarList(model.getRowCount());
                                            panelPagares.invalidate();
                                            revalidate();
                                            repaint();
                                        } else {
                                            crearFactura("000000001");
                                            model.consulta("SELECT numero AS NUMERO, nombre AS NOMBRE, fecha AS FECHA, total AS TOTAL "
                                                    + "FROM cliente, factura "
                                                    + "WHERE credito_cliente = 1 "
                                                    + "AND idCliente = cliente_idcliente "
                                                    + "AND metodo_pago = 'Credito' "
                                                    + "AND punto_emision_idpunto_emision = 1 "
                                                    + "AND fecha >= '" + String.format("%tF", fechaIn) + "' "
                                                    + "AND fecha <= '" + String.format("%tF", fechaFin) + "' "
                                                    + "AND Estado_factura = 'AUTORIZADO' "
                                                    + "AND idCliente = " + idCliente);
                                            lblEstado.setText("Factura de Credito " + sec1 + "-" + sec2 + "-" + num + " Autorizada correctamente.");
                                            llenarList(model.getRowCount());
                                            panelPagares.invalidate();
                                            revalidate();
                                            repaint();
                                        }
                                    }
                                } catch (SQLException | JAXBException | IOException | javax.xml.ws.WebServiceException ex) {
                                    System.err.println(ex.getMessage());
                                    txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                            + "----------------------------------------------------------------\n");
                                    try {
                                        model.ejecutar("UPDATE factura SET Estado_factura = 'Contingencia' "
                                                + "WHERE idfactura = " + Integer.parseInt(num));
                                    } catch (SQLException ex1) {
                                        System.err.println(ex1.getMessage());
                                        txtALog.append("ERROR: " + ex.getMessage() + ".\n"
                                                + "----------------------------------------------------------------\n");
                                    }
                                }
                            } else {
                                txtALog.append("ERROR: La factura no fue creada.\n"
                                        + "----------------------------------------------------------------\n");
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
            java.util.logging.Logger.getLogger(CreditoFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CreditoFactura("advjuan", "punk1991").setVisible(true);
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
    private com.toedter.calendar.JDateChooser fechaFinal;
    private com.toedter.calendar.JDateChooser fechaInicial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JPanel panelPagares;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JLabel tpagare;
    private javax.swing.JTextArea txtAFacturas;
    private javax.swing.JTextArea txtALog;
    private javax.swing.JTextField txtBuscado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
