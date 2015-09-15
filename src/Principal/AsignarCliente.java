/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.conexion_facturacion;
import conexion.ConnectionTableDB;
import ec.gob.sri.comprobantes.ws.Comprobante;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.PatternSyntaxException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import reportes.reporte_factura;
import seguridad.CertificadosSSL;
import seguridad.FirmaDigital;
import util.ArchivosUtil;
import ws.AutorizacionComprobantesWS;
import ws.EnvioComprobantesWS;

/**
 *
 * @author Advantech Digital
 */
public class AsignarCliente extends javax.swing.JFrame {

    File xmlC;
    int nsurtidor, manguera;
    String producto, cang, punit;
    Double iva, subtotal, total;
    Double cupo = 0.00;
    String tp = "";
    String codigop = "";
    Double cantidad, pu, galones, precio;
    String ocont = "";
    int cespecial = 0;
    String nc = "";
    String rz = "", np = "", d = "", r = "", na = "", clientr = "", tcliente = "";
    String s1 = "", s2 = "", tel = "";
    String ss3 = "", ta = "";
    int s3 = 0, idcliente = 0;
    String ti = "", maile = "", contramail = "";
    String ncliente = "", email = "";
    int tanque = 0;
    int turno = 0;
    String cadena;
    File axml;
    conexion_facturacion he;
    String cadenaC = "";
    int idcl = 0, idusuario = 0;
    String cadena1 = "";
    InputStream PKCS12_RESOURCE;
    String PKCS12_PASSWORD;
    String nxml;

    /**
     * Creates new form AsignarCliente
     */
    public AsignarCliente(String usuario, String contraseña) {
        USER = usuario;
        PASSWORD = contraseña;




        String consulta = "SELECT * FROM cliente LIMIT 50";
        try {
            model = new ConnectionTableDB(USER, PASSWORD, DB, consulta, true);

            FileOutputStream outpu = null;
            Statement st_in = model.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery(" SELECT xml,numero FROM adv_xml.consumidores_finales,adv_facturacion.factura where consumidores_finales.idfactura=factura.idfactura;");

            while (ri.next()) {



                xmlC = new File("consumidores_finales\\" + ri.getString(2) + ".xml");
                outpu = new FileOutputStream(xmlC);
                Blob xmlc = ri.getBlob(1);
                InputStream inStream = xmlc.getBinaryStream();
                int size = (int) xmlc.length();
                byte[] buffer = new byte[size];
                int length = -1;
                while ((length = inStream.read(buffer)) != -1) {
                    outpu.write(buffer, 0, length);

                }
                outpu.close();


            }
            ri.close();


        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        File consumidor = new File(XML_PATH);
        File cliente = new File(ASIGNADOS);
        if (!consumidor.exists()) {
            consumidor.mkdir();
        }
        if (!cliente.exists()) {
            cliente.mkdir();
        }
        xml = new File(XML_PATH).listFiles();
        modeloTabla = new ModeloTablaXML(xml);
        initComponents();
        if (tablaXML.getRowCount() == 0) {
            txtAXML.setText("No existen más facturas a Consumidor Final.");
            btnBuscarCliente.setEnabled(false);
        }
        Clientes.setUndecorated(true);
        Clientes.setSize(1000, 560);
        sorter = new TableRowSorter<TableModel>(model);
        sorter.setRowFilter(null);
        tablaClientes.setRowSorter(sorter);
        this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);


    }

    private void createTable() {
        xml = new File(XML_PATH).listFiles();
        modeloTabla.update(xml);
    }

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
        btnAsignar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaXML = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAXML = new javax.swing.JTextArea();

        Clientes.setExtendedState(2);
        Clientes.setName("frameClientes"); // NOI18N
        Clientes.setResizable(false);

        jLabel3.setText("Buscar:");
        jPanel2.add(jLabel3);

        txtBuscar.setColumns(30);
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarKeyPressed(evt);
            }
        });
        jPanel2.add(txtBuscar);

        Clientes.getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        tablaClientes.setModel(model);
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaClientesMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaClientes);

        Clientes.getContentPane().add(jScrollPane3, java.awt.BorderLayout.CENTER);

        txtBuscado.setEditable(false);
        txtBuscado.setColumns(30);
        txtBuscado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscadoActionPerformed(evt);
            }
        });
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

        setTitle("Asignar Cliente");
        setName("interfaz"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        btnBuscarCliente.setText("Buscar Cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        btnAsignar.setText("Asignar Cliente");
        btnAsignar.setEnabled(false);
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });
        btnAsignar.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnAsignarAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jLabel1.setText("Id:");

        jLabel2.setText("Cliente:");

        txtId.setEditable(false);

        txtCliente.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(txtId)
                    .addComponent(btnBuscarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAsignar, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBuscarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnAsignar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(600, 400));

        tablaXML.setModel(modeloTabla);
        tablaXML.setShowVerticalLines(false);
        tablaXML.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tablaXMLMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tablaXML);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.LINE_END);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.PAGE_END);

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerifyInputWhenFocusTarget(false);

        txtAXML.setEditable(false);
        txtAXML.setColumns(10);
        txtAXML.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        txtAXML.setRows(5);
        txtAXML.setTabSize(4);
        jScrollPane2.setViewportView(txtAXML);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        // TODO add your handling code here:
        btnBuscarCliente.setEnabled(false);
        Clientes.setVisible(true);
        Clientes.requestFocus();
        setEnabled(false);
        try {
            model.consulta("SELECT cedula_ruc ID, nombre Cliente, tipo_identificacion Tipo, Direccion, Telefono,(select codigo from codigos where cliente_idcliente=idcliente limit 1) as codigo FROM cliente limit 50");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        if (model.connected) {
            model.desconectar();
        }
        System.exit(0);
    }//GEN-LAST:event_formWindowClosed

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        // TODO add your handling code here:
        setEnabled(true);
        txtId.requestFocus();
        Clientes.setVisible(false);
        if (tablaXML.getRowCount() != 0 && rowXML >= 0) {
            btnAsignar.setEnabled(true);
        }
        btnBuscarCliente.setEnabled(true);
        if (!(rowClientes < 0)) {

            System.out.println(tablaClientes.getValueAt(rowClientes, 3));
            txtId.setText(String.valueOf(tablaClientes.getValueAt(rowClientes, 0)));
            txtCliente.setText(String.valueOf(tablaClientes.getValueAt(rowClientes, 1)));

            if (tablaClientes.getValueAt(rowClientes, 2) != null) {
                switch ((String) tablaClientes.getValueAt(rowClientes, 2)) {
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
        }
    }//GEN-LAST:event_btnOKActionPerformed

    private void tablaClientesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMousePressed
        // TODO add your handling code here:
        rowClientes = tablaClientes.rowAtPoint(evt.getPoint());
        txtBuscado.setText(String.format("Seleccionado: %s", (String) tablaClientes.getValueAt(rowClientes, 1)));
    }//GEN-LAST:event_tablaClientesMousePressed

    private void tablaXMLMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaXMLMousePressed
        // TODO add your handling code here:
        rowXML = tablaXML.rowAtPoint(evt.getPoint());
        if (!(rowXML < 0)) {
            try {
                btnAsignar.setEnabled(true);
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                doc = builder.parse(xml[rowXML]);
                Element root = doc.getDocumentElement();
                root.normalize();
                Node infoTributaria = root.getElementsByTagName("infoTributaria").item(0);
                String emisor = ((Element) infoTributaria).getElementsByTagName("razonSocial").item(0).getFirstChild().getNodeValue();
                String comercial = ((Element) infoTributaria).getElementsByTagName("nombreComercial").item(0).getFirstChild().getNodeValue();
                String ruc = ((Element) infoTributaria).getElementsByTagName("ruc").item(0).getFirstChild().getNodeValue();

                Node detalle = ((Element) root.getElementsByTagName("detalles").item(0)).getElementsByTagName("detalle").item(0);
                String producto = ((Element) detalle).getElementsByTagName("descripcion").item(0).getFirstChild().getNodeValue();
                String cantidad = ((Element) detalle).getElementsByTagName("cantidad").item(0).getFirstChild().getNodeValue();

                Node infoFactura = root.getElementsByTagName("infoFactura").item(0);
                String fecha = ((Element) infoFactura).getElementsByTagName("fechaEmision").item(0).getFirstChild().getNodeValue();
                String nombre = ((Element) infoFactura).getElementsByTagName(NOMBRE).item(0).getFirstChild().getNodeValue();
                String id = ((Element) infoFactura).getElementsByTagName(ID).item(0).getFirstChild().getNodeValue();
                String total = ((Element) infoFactura).getElementsByTagName("importeTotal").item(0).getFirstChild().getNodeValue();

                nodos = new Node[infoFactura.getChildNodes().getLength()];
                for (int i = 0; i < nodos.length; i++) {
                    nodos[i] = infoFactura.getChildNodes().item(i);
                }

                txtAXML.setText(String.format("Factura Seleccionada: %s\n"
                        + "Fecha:                %s\n\n"
                        + "EMISOR:\n"
                        + "%-15s%20s\n"
                        + "%-15s%20s\n"
                        + "%-15s%20s\n\n"
                        + "CLIENTE:\n"
                        + "%-15s%20s\n"
                        + "%-15s%20s\n\n"
                        + "DETALLE:\n"
                        + "%-15s%20s\n"
                        + "%-15s%20s galones.\n"
                        + "%-15s%20s\n",
                        (String) tablaXML.getValueAt(rowXML, 0),
                        fecha,
                        "Nombre: ", emisor,
                        "Comercial: ", comercial,
                        "Id. Emisor: ", ruc,
                        "Nombre: ", nombre,
                        "Id: ", id,
                        "Producto: ", producto,
                        "Cantidad: ", cantidad,
                        "Total Factura: ", "$ " + total));
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_tablaXMLMousePressed

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
        if (txtCliente.getText().length() == 0) {
            JOptionPane.showMessageDialog(this, "No ha seleccionado ningún cliente.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                // TODO add your handling code here:
                for (int i = 0; i < nodos.length; i++) {
                    if (TIPO.equals(nodos[i].getNodeName())) {
                        nodos[i].setTextContent(tipo);
                    }
                    if (NOMBRE.equals(nodos[i].getNodeName())) {
                        nodos[i].setTextContent(txtCliente.getText());
                    }
                    if (ID.equals(nodos[i].getNodeName())) {
                        nodos[i].setTextContent(txtId.getText());
                    }
                }
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource source = new DOMSource(doc);



                nxml = xml[rowXML].getName();
                System.out.println(nxml);

                StreamResult result = new StreamResult(ASIGNADOS + "\\" + xml[rowXML].getName());
                transformer.transform(source, result);
                xml[rowXML].delete();
                createTable();
                if (tablaXML.getRowCount() == 0) {
                    btnAsignar.setEnabled(false);
                    txtAXML.setText("No existen más facturas a Consumidor Final.");
                }
                JOptionPane.showMessageDialog(this, "Cliente asignado correctamente.");
                enviar_factura();


            } catch (TransformerConfigurationException ex) {
                System.out.println(ex.getMessage());
            } catch (TransformerException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_btnAsignarActionPerformed

    public void enviar_factura() {
        try {

            ncliente = txtCliente.getText();
            clientr = txtId.getText();
            String fecha = null, hora = null;
            conexion_facturacion he = new conexion_facturacion(USER, PASSWORD);
            int idfactura = 0;
            
            
            he.conectar();
            Statement st_in = he.coneccion.createStatement();
            ResultSet ri = st_in.executeQuery("SELECT iddatos_gasolinera,razon_social,ruc,direccion,email_estacion,secuencia1_factura,s2,despachadores_turno,tipo_ambiente,obligado_llevar_contabilidad,nombre_comercial,contribuyente_especial,certificado_digital,contraseña_certificado,tipo_cierre_turnos,contraseña_mail,AES_DECRYPT(contraseña_certificado,'thekey'),AES_DECRYPT(contraseña_mail,'thekey') FROM adv_facturacion.datos_gasolinera , adv_facturacion.punto_emision where datos_gasolinera_iddatos_gasolinera=iddatos_gasolinera and idpunto_emision=1;");


            s1 = nxml.substring(0, 3);
            s2 = nxml.substring(4, 7);
            ss3 = nxml.substring(8, 17);


            System.out.println("s1 " + s1);
            System.out.println("s2 " + s2);
            System.out.println("s3 " + ss3);


            Statement st_fac = he.coneccion.createStatement();
            ResultSet rifac = st_fac.executeQuery("SELECT producto.`idproducto` AS producto_idproducto,producto.`nombre` AS producto_nombre,factura_detalle.`iva` AS factura_detalle_iva,factura.`fecha` AS factura_fecha,factura.`hora` AS factura_hora,clave_acceso.`clave_acceso` AS clave_acceso_clave_acceso,factura.`subtotal` AS factura_subtotal,factura.`total` AS factura_total,factura.`iva` AS factura_iva ,producto.punit,factura.idfactura FROM `factura` factura INNER JOIN `factura_detalle` factura_detalle ON factura.`idfactura` = factura_detalle.`factura_idfactura`INNER JOIN `producto` producto ON factura_detalle.`producto_idproducto` = producto.`idproducto`INNER JOIN `clave_acceso` clave_acceso ON factura.`clave_acceso_idclave_acceso` = clave_acceso.`idclave_acceso` and factura.numero='" + s1 + "-" + s2 + "-" + ss3 + "'");
            while (rifac.next()) {

                idfactura=rifac.getInt(11);
                cadena = rifac.getString(6);
                codigop = rifac.getString(1);
                producto = rifac.getString(2);
                iva = rifac.getDouble(9);
                fecha = rifac.getString(4);
                hora = rifac.getString(5);
                subtotal = rifac.getDouble(7);
                total = rifac.getDouble(8);
                punit = rifac.getString(10);
            }


            while (ri.next()) {

                rz = ri.getString(2);
                np = ri.getString(5);
                d = ri.getString(4);
                r = ri.getString(3);
                s1 = ri.getString(6);
                s2 = ri.getString(7);
                ta = ri.getString(9);
                ocont = ri.getString(10);
                nc = ri.getString(11);
                cespecial = ri.getInt(12);
                Blob archivo = ri.getBlob(13);
                System.out.println();

                PKCS12_RESOURCE = archivo.getBinaryStream();
                PKCS12_PASSWORD = ri.getString(17);
                maile = ri.getString(5);
                contramail = ri.getString(18);
            }



            String Ewsri;
            String Awsri;

            if (ta.equalsIgnoreCase("1")) {


                Ewsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                Awsri = "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";

            } else {

                Ewsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?wsdl";
                Awsri = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantes?wsdl";




            }
            String motivo = "";
            ArchivosUtil au = new ArchivosUtil();

            if (au.existeConexion(Ewsri, Awsri)) {


                   JOptionPane.showMessageDialog(this, "Enviando Factura Electronica");

                FirmaDigital fd = new FirmaDigital(ASIGNADOS + "\\" + nxml, "firmados\\firmado" + nxml);
                fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
                fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);

                fd.firmarDocumentoXML();


                axml = new File("firmados\\firmado" + nxml);

                CertificadosSSL.instalarCertificados();
                RespuestaSolicitud response = null;
                try {


                    EnvioComprobantesWS ws = new EnvioComprobantesWS(Ewsri);
                    response = ws.enviarComprobante(axml);

                } catch (Exception ex) {


                    guardar_datos g = new guardar_datos(tp, 0, 1, axml, "Contingencia", "indisponibilidad del sistema", nsurtidor, s1 + "-" + s2 + "-" + ss3, "", "", subtotal, total, iva, manguera, idcliente, "", producto, cang);
                    g.usuarios(USER, PASSWORD);
                    g.actualizar("", "", s1 + "-" + s2 + "-" + ss3, ss3);

                }

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





                if (response.getEstado().equalsIgnoreCase("RECIBIDA")) {

                    List<Autorizacion> listaAutorizaciones = null;
                    ec.gob.sri.comprobantes.ws.aut.RespuestaComprobante respuesta = null;
                    int c = 0;
                    try {

                        System.out.println(cadena);

                        respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(cadena);
                        listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();


                        while (listaAutorizaciones.size() == 0) {

                            respuesta = (new AutorizacionComprobantesWS(Awsri)).llamadaWSAutorizacionInd(cadena);
                            listaAutorizaciones = respuesta.getAutorizaciones().getAutorizacion();
                            System.out.println();
                            c++;


                        }

                    } catch (Exception ex) {



                        guardar_datos g = new guardar_datos(tp, 0, 1, axml, "Contingencia", "indisponibilidad del sistema", nsurtidor, s1 + "-" + s2 + "-" + ss3, "", "", subtotal, total, iva, manguera, idcliente, USER, producto, cang);
                        g.usuarios(USER, PASSWORD);
                        g.actualizar("", "", s1 + "-" + s2 + "-" + ss3, ss3);


                    }
                    System.out.println("Intentos " + c);



                    System.out.println(listaAutorizaciones.size());
                    for (Autorizacion autorizacion : listaAutorizaciones) {
                        System.out.println("entro a autorizacion factura " + ss3);
                        //Lógica de acceso a datos
                        String nAuto = autorizacion.getNumeroAutorizacion();
                        XMLGregorianCalendar fAuto = autorizacion.getFechaAutorizacion();
                        //--


                        String estado = autorizacion.getEstado().toUpperCase();
                        System.out.println("\n Factura de Manguera " + manguera + " " + estado);

                        if (estado.compareTo("AUTORIZADO") == 0) {
                            //msgAutorizacion.append(estado);

                            JOptionPane.showMessageDialog(this, "Factura Electronica Autorizada Correctamente");
                            ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "autorizados\\autorizado" + s1 + "-" + s2 + "-" + ss3 + ".xml");



                            if (ta.equalsIgnoreCase("1")) {

                                ta = "Pruebas";

                            } else {

                                ta = "Producccion";

                            }


                            Statement st_cli = he.coneccion.createStatement();
                            ResultSet ricli = st_cli.executeQuery("SELECT idcliente from adv_facturacion.cliente where cedula_ruc=" + clientr + "");
                            while (ricli.next()) {


                                PreparedStatement cli = he.coneccion.prepareStatement("UPDATE `adv_facturacion`.`factura` SET `cliente_idcliente`='"+ricli.getInt(1)+ "' WHERE `numero`='" + s1 + "-" + s2 + "-" + ss3 + "'");

                                cli.execute();
                            }

                            
                            PreparedStatement xml = he.coneccion.prepareStatement("DELETE FROM `adv_xml`.`consumidores_finales` WHERE `idfactura`='"+idfactura+"';");

                             xml.execute();
                            
                            File auxml = new File("autorizados\\autorizado" + s1 + "-" + s2 + "-" + ss3 + ".xml");


                            guardar_datos g = new guardar_datos(tp, 0, 1, auxml, estado, "", nsurtidor, s1 + "-" + s2 + "-" + ss3, fecha, hora, subtotal, total, iva, manguera, idcliente, USER, producto, cang);
                            g.usuarios(USER, PASSWORD);
                            g.actualizar(nAuto, "", s1 + "-" + s2 + "-" + ss3, ss3);



                            reporte_factura rf = new reporte_factura();
                            rf.reporte_factura(s1 + "-" + s2 + "-" + ss3, r, d, d, ocont, String.valueOf(cespecial), nAuto, String.valueOf(fAuto), cadena, "Normal", ta, rz, nc, ncliente, clientr, fecha, hora, String.valueOf(subtotal), String.valueOf(iva), String.valueOf(total), "0", codigop, producto, String.valueOf(cantidad), producto, String.valueOf(pu), "0");
                            rf.enviar_email(contramail, maile, "autorizados\\autorizado" + s1 + "-" + s2 + "-" + ss3 + ".xml", "pdf\\" + s1 + "-" + s2 + "-" + ss3 + ".pdf", email, ncliente, rz, s1 + "-" + s2 + "-" + ss3 + ".pdf", s1 + "-" + s2 + "-" + ss3 + ".xml");
                            rf.run();



                            he.coneccion.close();





                        } else {
                            int con = 0;
                            String mensajeE = null;
                            List<Mensaje> mensajes = autorizacion.getMensajes().getMensaje();
                            File auxml = null;
                            for (Mensaje mensaje : mensajes) {

                                ArchivosUtil.guardarDocumentoXMLAutorizado(autorizacion, "no_autorizados\\" + s1 + "-" + s2 + "-" + ss3 + ".xml");


                                auxml = new File("no_autorizados\\" + s1 + "-" + s2 + "-" + ss3 + ".xml");

                                con++;

                                if (con == 1) {

                                    mensajeE = mensaje.getMensaje();

                                }

                                //msgAutorizacion.append("NO AUTORIZADO").append(".").append(mensaje.getTipo()).append(".").append(mensaje.getIdentificador()).append(".").append(mensaje.getMensaje()).append(".").append(mensaje.getInformacionAdicional()).append("\n");

                            }

                            guardar_datos g = new guardar_datos(tp, 0, 1, auxml, estado, mensajeE, nsurtidor, s1 + "-" + s2 + "-" + ss3, "", "", subtotal, total, iva, manguera, idcliente, USER, producto, cang);
                            g.usuarios(USER, PASSWORD);
                            g.actualizar("", cadena, s1 + "-" + s2 + "-" + ss3, ss3);

                        }
                    }






















                } else {


                    System.out.println("Devuelta " + response.getComprobantes().getComprobante().size());
                    guardar_datos g = new guardar_datos(tp, idcl, 1, axml, "Devuelta ", "indisponibilidad del sistema", nsurtidor, s1 + "-" + s2 + "-" + ss3, "", "", subtotal, total, iva, manguera, idcliente, USER, producto, cang);
                    g.usuarios(USER, PASSWORD);
                    g.actualizar("", "", s1 + "-" + s2 + "-" + ss3, ss3);


                }




            }




        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AsignarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }





    }

    private void txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyPressed
        // TODO add your handling code here:
        try {

            if (txtBuscar.getText().length() > 4) {

                //System.out.println("SELECT idcliente,nombre,cedula_ruc,email,credito_cliente,cupo_cliente FROM adv_facturacion.cliente,adv_facturacion.codigos  WHERE  cedula_ruc like ('" + txtBuscar.getText() + "%') group by nombre limit 50");
                model.consulta("SELECT cedula_ruc,nombre,tipo_identificacion,email,credito_cliente,cupo_cliente FROM adv_facturacion.cliente  WHERE  cedula_ruc like ('" + txtBuscar.getText() + "%') group by nombre limit 50");

            }
            // txtBuscar.getText()

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        String filtro = txtBuscar.getText();
        if (filtro.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            try {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + filtro));
            } catch (PatternSyntaxException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }//GEN-LAST:event_txtBuscarKeyPressed

    private void txtBuscadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscadoActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void btnAsignarAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnAsignarAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAsignarAncestorAdded

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AsignarCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new AsignarCliente(USER, PASSWORD).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame Clientes;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnOK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTable tablaXML;
    private javax.swing.JTextArea txtAXML;
    private javax.swing.JTextField txtBuscado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
    private ConnectionTableDB model;
    private String USER = "";
    private String PASSWORD = "";
    private final String DB = "adv_facturacion";
    private final String XML_PATH = "consumidores_finales";
    private final String ASIGNADOS = "clientes_asignados";
    private final String TIPO = "tipoIdentificacionComprador";
    private final String NOMBRE = "razonSocialComprador";
    private final String ID = "identificacionComprador";
    private String tipo;
    private int rowClientes = -1;
    private int rowXML = -1;
    private File[] xml;
    private Document doc;
    private Node[] nodos;
    private ModeloTablaXML modeloTabla;
    private TableRowSorter<TableModel> sorter;
}
