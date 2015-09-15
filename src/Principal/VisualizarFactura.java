/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.ConnectionTableDB;
import conexion.conexion_facturacion;
import ec.gob.sri.comprobantes.ws.Comprobante;
import ec.gob.sri.comprobantes.ws.RespuestaSolicitud;
import ec.gob.sri.comprobantes.ws.aut.Autorizacion;
import ec.gob.sri.comprobantes.ws.aut.Mensaje;
import facturacion.crear_clave_acceso;
import facturacion.crear_nota_credito_normal;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceException;

import reportes.estados_cuenta;
import reportes.reporte_factura;
import reportes.reporte_nota_credito;
import seguridad.CertificadosSSL;
import seguridad.FirmaDigital;
import sockets.metodosServidor;
import util.ArchivosUtil;
import ws.AutorizacionComprobantesWS;
import ws.EnvioComprobantesWS;

/**
 *
 * @author Advantech Digital
 */
public class VisualizarFactura extends javax.swing.JFrame {

    private final String Extra = "Extra";
    private final String Super = "Super";
    private final String Diesel = "Diesel";
    private String USER, PASSWORD, pago;
    private String DB = "adv_facturacion";
    private int idFactura;
    ConnectionTableDB conn;
    private ConnectionTableDB model;
    private InputStream PKCS12_RESOURCE;
    private String PKCS12_PASSWORD;
    private final int PTO_EMISION = 2;
    private String prod;
    private int rowClientes = -1;
    private int rowXML = -1;
    private Date fechaIn, fechaFin;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private TableRowSorter<TableModel> sorter;
    public String ambiente, razonSocial, nombreComercial, ruc, sec2, sec22, sec1, secuencial, direccion, emailEstacion;
    public String fechaEmision, tipo, nCliente, idC, contribuyente, contabilidad, factura, fechaFactura, passwEmail;
    private String claveAcceso, Ewsri, Awsri, email;
    private Double cantidadT, ppuT, subtotalT, ivaT, totalT;
    private Double cantidadE = 0.000, ppuE = 0.000, subtotalE = 0.00, ivaE = 0.000, totalE = 0.00;
    private Double cantidadS = 0.000, ppuS = 0.000, subtotalS = 0.00, ivaS = 0.000, totalS = 0.00;
    private Double cantidadD = 0.000, ppuD = 0.000, subtotalD = 0.00, ivaD = 0.000, totalD = 0.00;
    private String[] productos;
    private Double[] cantidades, ppus, subtotales, ivas, totales;
    private javax.swing.JCheckBox[] pagares;
    private int notaCredito, Factura, idGasolinera;
    private int idUser, idCliente = 0, idClave;
    Map pars;
    Properties props;
    Session session;
    String correo, xml, pdf, correocliente, rz, npdf, nxml;
    MimeMessage message;
      conexion_facturacion n;
    private FileOutputStream fos;
    
    /**
     * Creates new form VisualizarFactura
     */
    public VisualizarFactura(String user, String password, int idFactura, int manguera) throws SQLException {
        this.USER = user;
        this.PASSWORD = password;
        this.idFactura = idFactura;

        n = new conexion_facturacion(user, password);
        servidores();
        String consulta1 = "SELECT tipo_ambiente, razon_social, nombre_comercial, ruc, s2, secuencia1_factura, direccion, "
                + "email_estacion, contribuyente_especial, obligado_llevar_contabilidad, iddatos_gasolinera, "
                + "email_estacion "
                + "FROM datos_gasolinera, punto_emision "
                + "WHERE iddatos_gasolinera = datos_gasolinera_iddatos_gasolinera "
                + "AND idpunto_emision = " + PTO_EMISION;
        model = new ConnectionTableDB(USER, PASSWORD, DB, consulta1, false);
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


        String consulta = "SELECT numero, Estado_factura, metodo_pago, factura.fecha, "
                + "numero_autorizacion, clave_acceso, cliente.nombre, cedula_ruc, email, direccion, "
                + "subtotal, iva, total "
                + "FROM cliente, clave_acceso, factura "
                + "WHERE cliente_idcliente = idcliente "
                + "AND clave_acceso_idclave_acceso = idclave_acceso "
                + "AND idfactura = '" + idFactura + "'";
        try {
            conn = new ConnectionTableDB(USER, PASSWORD, DB, consulta, false);
            initComponents();
            if (conn.getRowCount() > 0) {
                lblNumero.setText((String) conn.getValueAt(0, 0));
                txtEstado.setText((String) conn.getValueAt(0, 1));
                txtPago.setSelectedItem((String) conn.getValueAt(0, 2));
                //txtPago.setText();
                pago = (String) txtPago.getSelectedItem();
                txtFecha.setText(String.format("%1$tY/%1$tm/%1$td", conn.getValueAt(0, 3)));
                txtAutorizacion.setText((String) conn.getValueAt(0, 4));
                txtClave.setText((String) conn.getValueAt(0, 5));
                txtCliente.setText((String) conn.getValueAt(0, 6));
                txtIdCliente.setText((String) conn.getValueAt(0, 7));
                txtEmail.setText((String) conn.getValueAt(0, 8));
                txtDireccion.setText((String) conn.getValueAt(0, 9));
                txtSubtotal.setText(String.format("%.2f", ((BigDecimal) conn.getValueAt(0, 10)).doubleValue()));
                txtIva.setText(String.format("%.6f", ((BigDecimal) conn.getValueAt(0, 11)).doubleValue()));
                txtTotal.setText(String.format("%.2f", ((BigDecimal) conn.getValueAt(0, 12)).doubleValue()));


                conn.consulta("SELECT cantidad, nombre, factura_detalle.`p_unit`/1.12, total/1.12 "
                        + "FROM factura_detalle, producto "
                        + "WHERE producto_idproducto = idproducto "
                        + "AND factura_idfactura = " + idFactura + "");
                for (int i = 0; i < conn.getRowCount(); i++) {
                    addProduct(i);
                }
            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Error al leer La Factura", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }



       if (txtEstado.getText().equalsIgnoreCase("AUTORIZADO")) {
            if (manguera == 0) {

                anular.setEnabled(true);
            } else {

                anular.setEnabled(false);
            }
        } else {

            btnModificar.setEnabled(false);
            anular.setEnabled(false);

        }

        String consulta2 = "SELECT s2 "
                + "FROM datos_gasolinera, punto_emision "
                + "WHERE iddatos_gasolinera = datos_gasolinera_iddatos_gasolinera "
                + "AND idpunto_emision =1";
        System.out.println(consulta1);

        java.sql.ResultSet rs = model.stSentencias.executeQuery(consulta2);
        if (rs.next()) {
            sec22 = rs.getString(1);

        }




    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void crearNormal(String num) throws SQLException, MalformedURLException, JAXBException, IOException, javax.xml.ws.WebServiceException {



        Statement st_f = model.coneccion.createStatement();
        ResultSet rif = st_f.executeQuery("SELECT s2 FROM adv_facturacion.punto_emision;");





        try (java.sql.ResultSet rs = model.stSentencias.executeQuery("SELECT certificado_digital FROM datos_gasolinera")) {
            if (rs.next()) {
                PKCS12_RESOURCE = rs.getBlob(1).getBinaryStream();
            }
        }
        model.ejecutar("UPDATE clave_acceso SET estado = 0 WHERE idclave_acceso = " + idClave + "");



        System.out.println(sec1);
        System.out.println(sec22);
        System.out.println(num);

        String clave = new crear_clave_acceso().crear_clave_acceso(fechaEmision.replace("/", ""), "04", ruc, ambiente, sec1 + sec22, num, "12345678", "1");
        txtALog.append("Clave de acceso: " + clave + " generada.\n");
        //System.out.println(prod);






        notaCredito = new crear_nota_credito_normal().crear_nota_credito_normal(prod.substring(0, 1), nombreComercial, contabilidad, Integer.parseInt(contribuyente), ambiente,
                razonSocial, ruc, clave, sec1, sec22, num, direccion, fechaEmision, tipo, nCliente, idC,
                subtotalT, ivaT, totalT, prod.substring(2), cantidadT, ppuT, fechaFactura, factura, "ANULACION");

        if (notaCredito == 1) {
            //  txtALog.append("Nota de Credito: " + sec1 + "-" + sec22 + "-" + num + " creada correctamente.\n");
            model.ejecutar("INSERT INTO clave_acceso (clave_acceso, fecha, estado, tipo) VALUES ('" + clave + "', CURDATE(), 1, 'normal')");
            //txtALog.append("Clave de acceso: " + clave + " grabada correctamente.\n");
            try (java.sql.ResultSet rsIdClave = model.stSentencias.executeQuery("SELECT idclave_acceso FROM clave_acceso WHERE clave_acceso = '" + clave + "'")) {
                if (rsIdClave.first()) {
                    idClave = rsIdClave.getInt(1);
                }
            }

            FirmaDigital fd = new FirmaDigital("notas_generadas\\nc" + sec1 + "-" + sec22 + "-" + num + ".xml", "notas_firmadas\\nota_firmada" + sec1 + "-" + sec22 + "-" + num + ".xml");
            fd.setPKCS12_RESOURCE(PKCS12_RESOURCE);
            fd.setPKCS12_PASSWORD(PKCS12_PASSWORD);
            fd.firmarDocumentoXML();
            //  txtALog.append("Nota de Credito: " + sec1 + "-" + sec22 + "-" + num + " firmada correctamente.\n");

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
            // txtALog.append("Autorizando nota de credito " + sec1 + "-" + sec22 + "-" + num + "\n");

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
                        //       txtALog.append("Entro a autorizacion: Nota de Credito " + sec1 + "-" + sec22 + "-" + num + "\n");
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

                            //   txtALog.append("Enviando correo al cliente: " + txtCliente.getText() + ".\n");
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

    private void addProduct(int index) {
        javax.swing.JLabel producto =
                new javax.swing.JLabel(String.format("%10.6f                 %-30s   %10.6f      %10.6f",
                ((BigDecimal) conn.getValueAt(index, 0)).doubleValue(),
                (String) conn.getValueAt(index, 1),
                ((BigDecimal) conn.getValueAt(index, 2)).doubleValue(),
                ((BigDecimal) conn.getValueAt(index, 3)).doubleValue()));
        producto.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 13));
        panelDetalles.add(producto);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        frameXML = new javax.swing.JFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAXML = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtAutorizacion = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtClave = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        txtIdCliente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtEstado = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelDetalles = new javax.swing.JPanel();
        txtSubtotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtIva = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtPago = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtALog = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        anular1 = new javax.swing.JButton();
        anular = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnXml = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        frameXML.setTitle("Comprobante SRI");
        frameXML.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                frameXMLFocusLost(evt);
            }
        });

        txtAXML.setColumns(20);
        txtAXML.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
        txtAXML.setLineWrap(true);
        txtAXML.setRows(5);
        txtAXML.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtAXML);

        frameXML.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("FACTURA No.:");
        jPanel1.add(jLabel1);

        lblNumero.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(lblNumero);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText("No. Autorizacion:");

        txtAutorizacion.setEditable(false);
        txtAutorizacion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtAutorizacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAutorizacionActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Clave de Acceso:");

        txtClave.setEditable(false);
        txtClave.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Cliente:");

        txtCliente.setEditable(false);
        txtCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        txtIdCliente.setEditable(false);
        txtIdCliente.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("Id:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Estado de Factura:");

        txtEstado.setEditable(false);
        txtEstado.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Fecha Emision:");

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Forma de Pago:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Email:");

        txtEmail.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Direccion:");

        txtDireccion.setEditable(false);
        txtDireccion.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Cantidad");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Descripción");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Precio Unitario");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Precio Total");

        panelDetalles.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane1.setViewportView(panelDetalles);

        txtSubtotal.setEditable(false);
        txtSubtotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtSubtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("Subtotal:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("IVA 12%:");

        txtIva.setEditable(false);
        txtIva.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtIva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setText("TOTAL:");

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        txtPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Contado", "TC", "Credito", "Prepago" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jSeparator2)
                            .addComponent(jSeparator1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAutorizacion)
                                    .addComponent(txtClave)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(176, 176, 176)
                                        .addComponent(jLabel9)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtEmail))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(txtCliente))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtPago, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(txtFecha)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtDireccion)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(509, 509, 509)
                                .addComponent(jLabel17))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtAutorizacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtALog.setColumns(20);
        txtALog.setRows(5);
        txtALog.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtALogKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtALog);

        jPanel4.setLayout(new java.awt.GridLayout(5, 1));

        jButton1.setText("Obtener PDF");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);

        anular1.setText("Imprimir Factura");
        anular1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anular1ActionPerformed(evt);
            }
        });
        jPanel4.add(anular1);

        anular.setText("Nota de Credito");
        anular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anularActionPerformed(evt);
            }
        });
        jPanel4.add(anular);

        btnModificar.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnModificar.setText("Modificar Pago");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel4.add(btnModificar);

        btnXml.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnXml.setText("Guardar Xml");
        btnXml.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXmlActionPerformed(evt);
            }
        });
        jPanel4.add(btnXml);

        jButton2.setText("Enviar Pdf y Xml a E-mail");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 66, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXmlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXmlActionPerformed
        // TODO add your handling code here:
        frameXML.setVisible(true);
        frameXML.setSize(600, 400);
        txtAXML.setText("");








FileOutputStream fos = null;

        if (txtEstado.getText().equalsIgnoreCase("Autorizado")) {
            try {


                try (java.sql.ResultSet rsXML = conn.stSentencias.executeQuery("SELECT doc_xml "
                        + "FROM adv_xml.xml_enviados_autorizados "
                        + "WHERE xml_factura = " + idFactura + "")) {
                    if (rsXML.first()) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(rsXML.getBlob(1).getBinaryStream()))) {
                            try {
                                final JFileChooser elegirCarpeta = new JFileChooser();
                                elegirCarpeta.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                                int o = elegirCarpeta.showOpenDialog(this);
                                if (o == JFileChooser.APPROVE_OPTION) {
                                    String path = elegirCarpeta.getSelectedFile().getAbsolutePath();
                                    
                                    File file = new File(path);

                                    fos = new FileOutputStream(file);
                                    Blob bin = rsXML.getBlob(1);
                                    InputStream inStream = bin.getBinaryStream();
                                    int size = (int) bin.length();
                                    byte[] buffer = new byte[size];
                                    int length = -1;
                                    while ((length = inStream.read(buffer)) != -1) {
                                        fos.write(buffer, 0, length);
                                    }


                                    System.out.println(path.length() + " : " + path);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                            }







                            String line;
                            while ((line = br.readLine()) != null) {
                                txtAXML.append(line);
                            }
                        }
                    } else {




                        JOptionPane.showMessageDialog(this,
                                "Error al leer el archivo.",
                                "Error de archivo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (txtEstado.getText().equalsIgnoreCase("NO AUTORIZADO")) {
            try {


                try (java.sql.ResultSet rsXML = conn.stSentencias.executeQuery("SELECT doc_xml "
                        + "FROM adv_xml.xml_no_autorizados "
                        + "WHERE xml_factura = " + idFactura + "")) {
                    if (rsXML.first()) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(rsXML.getBlob(1).getBinaryStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                txtAXML.append(line);
                            }
                        }
                    } else {

                        JOptionPane.showMessageDialog(this,
                                "Error al leer el archivo.",
                                "Error de archivo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex.getMessage());
            }



        } else if (txtEstado.getText().equalsIgnoreCase("CONTINGENCIA")) {

            try {


                try (java.sql.ResultSet rsXML = conn.stSentencias.executeQuery("SELECT xml_contingencia "
                        + "FROM adv_xml.xml_contingencia "
                        + "WHERE xml_factura = " + idFactura + "")) {
                    if (rsXML.first()) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(rsXML.getBlob(1).getBinaryStream()))) {
                            String line;
                            while ((line = br.readLine()) != null) {
                                txtAXML.append(line);
                            }
                        }
                    } else {




                        JOptionPane.showMessageDialog(this,
                                "Error al leer el archivo.",
                                "Error de archivo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex.getMessage());
            }

        }




    }//GEN-LAST:event_btnXmlActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        try {
            if (!String.valueOf(txtPago.getSelectedItem()).equalsIgnoreCase(pago)) {
                JOptionPane.showConfirmDialog(this,
                        "Está seguro de cambiar la forma de pago de la factura: " + lblNumero.getText() + "\n"
                        + "De " + pago + " a " + txtPago.getSelectedItem(),
                        "Cambio de Forma de Pago",
                        JOptionPane.YES_NO_OPTION);
                conn.ejecutar("UPDATE factura "
                        + "SET metodo_pago = '" + txtPago.getSelectedItem() + "' "
                        + "WHERE idFactura = " + idFactura);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se realizará ningún cambio.",
                        "Forma de Pago sin cambios", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void frameXMLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_frameXMLFocusLost
        // TODO add your handling code here:
        frameXML.setVisible(false);
    }//GEN-LAST:event_frameXMLFocusLost

    private void txtALogKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtALogKeyPressed
        // TODO add your handling code here:
        int c = evt.getKeyCode();
        if (c != KeyEvent.VK_UP && c != KeyEvent.VK_DOWN && c != KeyEvent.VK_LEFT && c != KeyEvent.VK_RIGHT
                && c != KeyEvent.VK_KP_UP && c != KeyEvent.VK_KP_DOWN && c != KeyEvent.VK_KP_LEFT && c != KeyEvent.VK_KP_RIGHT) {
            evt.consume();
        }
    }//GEN-LAST:event_txtALogKeyPressed

    private void anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anularActionPerformed
        int s3 = 0;
        String ss3 = null;
        try {
            try (java.sql.ResultSet rsClave = model.stSentencias.executeQuery("SELECT idclave_acceso, clave_acceso FROM clave_acceso WHERE estado = 0 AND tipo = 'contingencia'")) {
                if (rsClave.first()) {
                    idClave = rsClave.getInt(1);
                    claveAcceso = rsClave.getString(2);
                }
            }
            model.psPrepararSentencias = model.coneccion.prepareStatement("UPDATE clave_acceso SET estado = 1 WHERE idclave_acceso = " + idClave);
            model.psPrepararSentencias.executeUpdate();
            model.psPrepararSentencias.close();

            nCliente = txtCliente.getText();
            idC = txtIdCliente.getText();

            conexion_facturacion n = new conexion_facturacion(USER, PASSWORD);
            n.conectar();
            ResultSet consultan = n.consulta("select count(*) from nota_credito");

            while (consultan.next()) {
                s3 = consultan.getInt(1);
            }


            if (s3 >= 9) {


                ss3 = "00000000" + s3;
            }
            if (s3 <= 99 & s3 > 9) {


                ss3 = "0000000" + s3;
            }
            if (s3 <= 999 & s3 > 99) {


                ss3 = "000000" + s3;
            }

            if (s3 <= 9999 & s3 > 999) {


                ss3 = "00000" + s3;
            }
            if (s3 <= 99999 & s3 > 9999) {


                ss3 = "0000" + s3;
            }

            if (s3 <= 999999 & s3 > 99999) {


                ss3 = "000" + s3;
            }
            if (s3 <= 9999999 & s3 > 999999) {


                ss3 = "00" + s3;
            }
            if (s3 <= 99999999 & s3 > 9999999) {


                ss3 = "0" + s3;
            }


            crearNormal(ss3);



        } catch (SQLException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WebServiceException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_anularActionPerformed

    private void txtAutorizacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAutorizacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAutorizacionActionPerformed

    private void anular1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anular1ActionPerformed

        try {
            metodosServidor estado;
            estado = (metodosServidor) Naming.lookup("rmi://" + ip + "/servidor");

            estado.impresion(lblNumero.getText(), "0");

            JOptionPane.showMessageDialog(null, "Imprimiendo Factura en Isla1");



        } catch (RemoteException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_anular1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

       
             PDF pdf = new PDF();
             pdf.visualizar_pdf(lblNumero.getText(), USER, PASSWORD);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
         PDF pdf = new PDF();
         pdf.crear_pdf(lblNumero.getText(), USER, PASSWORD);
         
         
          if (txtEmail.getText().length() == 0) {

                JOptionPane.showMessageDialog(rootPane, "Ingrese Un correo para poder enviar mail ");

            } else {

                JOptionPane.showMessageDialog(rootPane, "Enviando mail ");
                enviar();


            }
         
         
         
         
    }//GEN-LAST:event_jButton2ActionPerformed
    String ip,ncliente,contraseña;

     private String generateCID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void enviar() {
       
        String path=""; 
        try {



            n.conectar();
           
            try {


                try (java.sql.ResultSet rsXML = conn.stSentencias.executeQuery("SELECT doc_xml "
                        + "FROM adv_xml.xml_enviados_autorizados "
                        + "WHERE xml_factura = " + idFactura + "")) {
                    if (rsXML.first()) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(rsXML.getBlob(1).getBinaryStream()))) {
                            try {
                               
                            
                              
                                    path="autorizados\\"+lblNumero.getText()+".xml";
                                    
                                    File file = new File(path);

                                    fos = new FileOutputStream(file);
                                    Blob bin = rsXML.getBlob(1);
                                    InputStream inStream = bin.getBinaryStream();
                                    int size = (int) bin.length();
                                    byte[] buffer = new byte[size];
                                    int length = -1;
                                    while ((length = inStream.read(buffer)) != -1) {
                                        fos.write(buffer, 0, length);
                                    }


                                    System.out.println(path.length() + " : " + path);
                                
                            } catch (Exception ex) {
                                Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                            }







                            String line;
                            while ((line = br.readLine()) != null) {
                                txtAXML.append(line);
                            }
                        }
                    } else {




                        JOptionPane.showMessageDialog(this,
                                "Error al leer el archivo.",
                                "Error de archivo",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex.getMessage());
            }
            
            
            
            
            
            
            
            
            
            
            npdf = "pdf\\"+lblNumero.getText()+".pdf";
           
            
            
            pdf = npdf;
            correocliente = txtEmail.getText();
            ncliente = txtCliente.getText();

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
             
            message.setSubject("Factura Estacion de Combustible "+rz +" Numero"+ lblNumero.getText());
            
          
            
          MimeMultipart content = new MimeMultipart("related");
          MimeBodyPart textPart = new MimeBodyPart(); 
          textPart.setText("<html><head>"
                    + "<title>This is not usually displayed</title>"
                    + "</head>\n"
                    + "<body><div><b>Estimado(a) " + ncliente + "</b></div>"
                    + "<div>Presente</div>\n"
                    + "<div>Gracias por preferirnos</div>\n"
                    + "<div>Adjuntamos a este email pdf y xml de su factura </div>\n"
                    +"<div>Por concepto de consumo de combustible</div>\n"
                    +"<div>Atentamente</div>\n"
                    +"<div>" + rz + "</div>"
                    + "</body></html>",
                    "US-ASCII", "html");
                  
                    
           content.addBodyPart(textPart);
// Image part
           

          



            // Se compone el adjunto con la imagen
            BodyPart adjuntopdf = new MimeBodyPart();
            adjuntopdf.setDataHandler(new DataHandler(new FileDataSource(pdf)));
            adjuntopdf.setFileName(npdf);
             BodyPart adjuntoxml = new MimeBodyPart();
            adjuntoxml.setDataHandler(new DataHandler(new FileDataSource(path)));
            adjuntoxml.setFileName(lblNumero.getText()+".xml");

            content.addBodyPart(adjuntopdf);
            content.addBodyPart(adjuntoxml);


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
            
            
            n.coneccion.close();
            

        } catch (NoSuchProviderException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(enviar_email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(estados_cuenta.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void servidores() {

        try {
            java.io.BufferedReader buffer = new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea;


            int cont = 0;
            while ((linea = buffer.readLine()) != null) {

                //ip=linea.substring(1,2);

                if (cont == 1) {
                    ip = linea.substring(18, linea.length());


                }


                cont++;

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VisualizarFactura.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new VisualizarFactura("root", "manager", 12130, 0).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(VisualizarFactura.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anular;
    private javax.swing.JButton anular1;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnXml;
    private javax.swing.JFrame frameXML;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JPanel panelDetalles;
    private javax.swing.JTextArea txtALog;
    private javax.swing.JTextArea txtAXML;
    private javax.swing.JTextField txtAutorizacion;
    private javax.swing.JTextField txtClave;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtEstado;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIva;
    private javax.swing.JComboBox txtPago;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
