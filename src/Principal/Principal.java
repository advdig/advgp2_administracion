/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import reportes.ReporteTurnos1;
import reportes.Reporte_diario;
import conexion.conexion_facturacion;
import configuracion.nproductos;
import configuracion.ntanques;
import configuracion.nmangueras;
import configuracion.modificar_configuracion_general;
import java.awt.Toolkit;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.sql.ResultSet;                                        
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import facturacion.PagaresAFactura;
import reportes.Reporte_clientes_credito;
import conexion.ConnectionTableDB;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ExecutionException;
import javax.sql.DataSource;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import reportes.CierreDiario;
import reportes.ControlPasoArchivos;
import reportes.Reporte_diario_consolidado;
import reportes.estados_cuenta;
import reportes.reporte_despachadores;
import reportes.Reporte_Facturas;
import reportes.Reporte_clientes_prepago;
/**
 *
 * @author r
 */
public class Principal extends javax.swing.JFrame {

    String fecha;
    Date h2;
    public String usuarios, contraseña, nombre, idu;
    public String cargo;
    ObjectOutputStream output;
    int nmanguera;
    String ip;
    int numerop;
    Double montos[];
    Double volumen[];
    String rz = null, np = null, d = null, r = null;
    String s1 = null, s2 = null;
    int s3 = 0;
    Dimension dim;
    ConnectionTableDB model;
    private DataOutputStream salida;
    private DataInputStream entrada;
    private String mensaje = "";

    private Socket cliente;
    private int bytesentrada;
    private byte[] respuesta;
    public String td = "";
    Connection cf;

    Date fechalogs = new Date();
    public int idadnet;
    DefaultListModel modeloLista = new DefaultListModel();
    String tip;
    int he;
    //Serial serial = SerialFactory.createSerialInstance();
    String re = "";
    private DataSource dataSource;
    Conecciones n1;
    /**
     * Creates new form Principal
     */
    public Principal() {

        
        servidores();
        
        
        initComponents();

        dim = this.getToolkit().getScreenSize();

        System.out.println(dim);

        this.setSize(dim.width, dim.height - 40);

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/java_splash/ad.png")));

       // setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
        /**
         * this.addWindowListener(new WindowAdapter() { JPasswordField pas = new
         * JPasswordField(); Object[] obj = {"Ingrese la contraseña del usuario"
         * + ":\n\n", pas}; Object stringArray[] = {"OK", "Cancel"}; String
         * horai, horas;
         *
         * public void windowClosing(WindowEvent we) {
         *
         * String pass = null; Component th = null; String titulo = "ingrese su
         * contraseña"; if (JOptionPane.showOptionDialog(th, obj, titulo,
         * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
         * stringArray, obj) == JOptionPane.YES_OPTION) { try { pass =
         * pas.getText();
         *
         *
         * int id;
         *
         *
         * conexion_facturacion n = new conexion_facturacion("root", "manager");
         * n.conectar(); String a = "SELECT idusuarios FROM usuarios WHERE
         * usuario='" + usuarios + "' AND AES_DECRYPT(contraseña,'thekey')='" +
         * pass + "'"; ResultSet consulta = n.consulta(a); int idu = 0; try { if
         * (consulta.first()) // si es valido el primer reg. hay una fila, tons
         * el usuario y su pw existen { id = consulta.getInt(1);
         * JOptionPane.showMessageDialog(null, "Usuario Validado Correctamente
         * Cerrando Turno y Aplicacion");
         *
         * System.exit(0); } } catch (SQLException ex) {
         * Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
         * ex); } } catch (ClassNotFoundException ex) {
         * Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
         * ex); } }
         *
         * }
         * });
         *
         * /**
         *
         * /*
         * String luv=""; boolean noti=false; try {
         *
         *
         * model = new ConnectionTableDB("root", "manager",
         * "adv_facturacion","SELECT
         * codigo_barra,nombre,punit,stock_actual,stock_maximo,stock_minimo FROM
         * adv_facturacion.producto; ", false); } catch (SQLException ex) {
         * Logger.getLogger(reporteProductos.class.getName()).log(Level.SEVERE,
         * null, ex); }
         *
         * for(int j=0;j<model.getRowCount();j++){
         *
         * if(Double.parseDouble(model.getValueAt(j,
         * 3).toString())<Double.parseDouble(model.getValueAt(j,
         * 5).toString())){ noti=true; luv=luv+"\n se necesita mas
         * "+model.getValueAt(j, 1).toString()+""; }
         *
         * }
         * if(noti){ etiquetaNoti.setForeground(Color.red);
         * etiquetaNoti.setFont(new Font("Serief",Font.BOLD,20)); }
         * model.desconectar(); lubri.setVisible(true); lubri.setText(luv);
         * System.out.print(luv);
         */
        escritorio.setSize(dim);
        
        
        n1 = new Conecciones("adv", "advgp2014");
        try {

            dataSource = n1.ConectarMysql();

            cf = dataSource.getConnection();

        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         sw();
        
        
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        compras = new javax.swing.JFrame();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        num = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        escritorio = new javax.swing.JDesktopPane();
        jButton1 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        lubri = new javax.swing.JTextArea();
        etiquetaNoti = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu10 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem28 = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        jMenuItem40 = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem23 = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jMenuItem29 = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        jMenuItem24 = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        jMenuItem27 = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItem25 = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        jMenuItem20 = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        jMenuItem36 = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        jMenuItem38 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem41 = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JPopupMenu.Separator();
        jMenuItem43 = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();

        jMenu5.setText("File");
        jMenuBar2.add(jMenu5);

        jMenu6.setText("Edit");
        jMenuBar2.add(jMenu6);

        compras.setResizable(false);
        compras.getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(3, 2));

        jLabel1.setText("Codigo Transaccion");
        jPanel2.add(jLabel1);
        jPanel2.add(cod);

        jLabel2.setText("Numero Trascaccion");
        jPanel2.add(jLabel2);
        jPanel2.add(num);

        jButton5.setText("Importar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        compras.getContentPane().add(jPanel2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Advgp2.0");

        jPanel1.setLayout(new java.awt.BorderLayout());

        escritorio.setBackground(new java.awt.Color(51, 102, 255));
        escritorio.setForeground(new java.awt.Color(51, 102, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/java_splash/logo.jpg"))); // NOI18N
        jButton1.setText("jButton1");
        escritorio.add(jButton1);
        jButton1.setBounds(0, 30, 1370, 510);

        lubri.setEditable(false);
        lubri.setColumns(20);
        lubri.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jScrollPane6.setViewportView(lubri);

        escritorio.add(jScrollPane6);
        jScrollPane6.setBounds(210, 0, 300, 30);

        etiquetaNoti.setEditable(false);
        etiquetaNoti.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        etiquetaNoti.setText("     NOTIFICACIONES");
        escritorio.add(etiquetaNoti);
        etiquetaNoti.setBounds(0, 0, 210, 30);

        jScrollPane1.setViewportView(escritorio);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jToolBar1.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar1.setFloatable(false);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar-grupo-icono-7696-32.png"))); // NOI18N
        jButton2.setToolTipText("Editar Cliente");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/anadir-usuario-icono-4000-32.png"))); // NOI18N
        jButton3.setToolTipText("Nuevo Cliente");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Iconos/editar-secuencias-de-comandos-icono-6203-32.png"))); // NOI18N
        jButton4.setToolTipText("Revisar Facturas");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jPanel1.add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenu10.setText("Configuracion");
        jMenu10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu10ActionPerformed(evt);
            }
        });

        jMenu8.setText("Datos Gasolinera");

        jMenuItem4.setText("Ingresar Datos Gasolinera");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItem4);
        jMenu8.add(jSeparator5);

        jMenu10.add(jMenu8);
        jMenu10.add(jSeparator6);

        jMenu11.setText("Configuracion General");

        jMenuItem13.setText("Crear Configuracion general");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem13);
        jMenu11.add(jSeparator9);

        jMenuItem3.setText("Modificar Configuracion general");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu11.add(jMenuItem3);

        jMenu10.add(jMenu11);
        jMenu10.add(jSeparator1);

        jMenuItem28.setText("Verificar Facturas con SOFI");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem28);
        jMenu10.add(jSeparator22);

        jMenuItem14.setText("Configuracion Productos");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem14);
        jMenu10.add(jSeparator7);

        jMenuItem5.setText("Configurar Turnos");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem5);
        jMenu10.add(jSeparator8);

        jMenuItem12.setText("Configurar Tanques");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem12);
        jMenu10.add(jSeparator26);

        jMenuItem40.setText("Ver Tanques");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem40);
        jMenu10.add(jSeparator20);

        jMenuItem26.setText("Firmar Archivo");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem26);

        jMenuBar1.add(jMenu10);

        jMenu7.setText("Administracion");
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });

        jMenu1.setText("Usuarios");

        jMenuItem19.setText("Restablecer Contraseña de Usuario");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem19);
        jMenu1.add(jSeparator14);

        jMenuItem8.setText("Nuevo Usuario");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenu7.add(jMenu1);
        jMenu7.add(jSeparator15);

        jMenuItem9.setText("Ingresar Claves de acceso");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem9);
        jMenu7.add(jSeparator27);

        jMenuItem44.setText("Ingresar Vehiculos Plaqueados");
        jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem44ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem44);

        jMenuBar1.add(jMenu7);

        jMenu2.setText("Facturacion");

        jMenu14.setText("Enviar Facturas Contingencia");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItem2.setText("Enviar Facturas Contingencia punto 1");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem2);

        jMenuItem31.setText("Enviar Facturas Contingencia punto 2");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem31);

        jMenuItem37.setText("Enviar Notas de Credito Contingencia");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem37);

        jMenu2.add(jMenu14);
        jMenu2.add(jSeparator2);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem6.setText("Revisar Facturas");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);
        jMenu2.add(jSeparator3);

        jMenuItem23.setText("Reportes Facturas por cliente");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem23);
        jMenu2.add(jSeparator11);

        jMenuItem29.setText("Crear Factura ");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem29);
        jMenu2.add(jSeparator23);

        jMenuItem7.setText("Convertir facturas consumidor final");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);
        jMenu2.add(jSeparator10);

        jMenuItem21.setText("Crear Notas de Credito");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem21);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Clientes");

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItem17.setText("Editar Cliente");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem17);

        jMenuItem10.setText("Agregar Nuevo Cliente");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuItem39.setText("Consulta Clientes Credito");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem39);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Credito");

        jMenu13.setText("Reportes");
        jMenu13.add(jSeparator16);

        jMenuItem24.setText("Listado Clientes ");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem24);

        jMenu4.add(jMenu13);
        jMenu4.add(jSeparator17);

        jMenuItem27.setText("Modificar Pagares");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem27);
        jMenu4.add(jSeparator21);

        jMenuItem25.setText("Crear Pagares");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem25);
        jMenu4.add(jSeparator19);

        jMenuItem20.setText("Convertir Pagares a Factura");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem20);
        jMenu4.add(jSeparator18);

        jMenuItem18.setText("Convertir Pagares a Factura Lubricantes");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuBar1.add(jMenu4);

        jMenu16.setText("Prepago");

        jMenuItem42.setText("Reporte Despachos");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem42);

        jMenuBar1.add(jMenu16);

        jMenu12.setText("Reporteria");

        jMenuItem16.setText("Reporte Despachos");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem16);
        jMenu12.add(jSeparator13);

        jMenuItem1.setText("Estados de Cuenta Clientes Credito");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem1);
        jMenu12.add(jSeparator24);

        jMenuItem36.setText("Reportes Lubricantes");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem36);
        jMenu12.add(jSeparator25);

        jMenuItem38.setText("Reportes por Facturas");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem38);
        jMenu12.add(jSeparator4);

        jMenuItem41.setText("Reporte turnos por despachadores");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem41);
        jMenu12.add(jSeparator28);

        jMenuItem43.setText("Reporte Cierre Diario");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem43);
        jMenu12.add(jSeparator12);

        jMenuItem15.setText("Reporte control de Facturacion");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem15);

        jMenuBar1.add(jMenu12);

        jMenu15.setText("Articulos");

        jMenuItem30.setText("Agregar Articulos NUEVOS");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem30);

        jMenuItem32.setText("Agregar Articulos");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem32);

        jMenuItem33.setText("Modificar Articulos");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem33);

        jMenuItem34.setText("Eliminar Articulos");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem34);

        jMenuItem35.setText("Consultar Inventario ");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem35);

        jMenuItem22.setText("Importar Compras Contable");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem22);

        jMenuBar1.add(jMenu15);

        jMenu9.setText("Ayuda");

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem11.setText("Acerca de:");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem11);

        jMenuBar1.add(jMenu9);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    byte[] comando = new byte[3];

    ;
    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed
    }//GEN-LAST:event_jMenu7ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        DatosGasolinera i = new DatosGasolinera(usuarios, contraseña);

        i.setVisible(true);
        i.setLocation(0, 0);

        //i.usuarios(usuarios, contraseña);

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        nuevo_usuario nu = new nuevo_usuario();

        nu.setVisible(true);

        nu.usuarios(usuarios, contraseña);
        nu.setLocation(0, 0);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        File file;
        JFileChooser fc = new JFileChooser();
        int open = fc.showOpenDialog(null);
        if (open == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            ArchivoClaves n = new ArchivoClaves(file, usuarios, contraseña);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        nproductos p = new nproductos();
        p.usuarios(usuarios, contraseña);
        p.setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        nmangueras n = new nmangueras();
        n.usuarios(usuarios, contraseña);
        n.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        ntanques n = new ntanques();
        n.usuarios(usuarios, contraseña);
        n.setVisible(true);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    
    private void enviarDatos(String mensaje) {
        // enviar objeto al servidor
        try {

            salida.flush();
            byte[] bytes = mensaje.getBytes();

            /* Se crea el dato y se escribe en el flujo de salida */
            //DatoSocket aux = new DatoSocket(mensaje);
            salida.write(bytes);

            //modeloLista.addElement("\nCLIENTE>>> " + dato.getText());
        } // procesar los problemas que pueden ocurrir al enviar el objeto
        catch (IOException | NullPointerException excepcionES) {
            modeloLista.addElement("\nError al escribir el objeto");
        }
    }

    private void conectarAServidor() throws IOException {
       System.out.println("Intentando realizar conexión\n");

        System.out.println(ipsofi);
        
        // crear Socket para realizar la conexión con el servidor
        cliente = new Socket(InetAddress.getByName(ipsofi), Integer.parseInt(puerto));

        
  
        
        // mostrar la información de la conexión
       System.out.println("Conectado Correctamente");

    }
     private void obtenerFlujos() throws IOException {
        // establecer flujo de salida para los objetos
        salida = new DataOutputStream(cliente.getOutputStream());
        salida.flush(); // vacíar búfer de salida para enviar información de encabezado
        //salida.writeUTF("Hola servidor");
        // establecer flujo de entrada para los objetos
        entrada = new DataInputStream(cliente.getInputStream());
        // System.out.println("\nSe recibieron los flujos de E/S\n");
    }

    private void procesarConexion() throws IOException {
        // habilitar campoIntroducir para que el usuario del cliente pueda enviar mensajes
        //   establecerCampoTextoEditable(true);

        do {
            try {
                // procesar mensajes enviados del servidor
                respuesta = new byte[256];
                bytesentrada = 0;
                mensaje = "";
                // leer mensaje y mostrarlo en pantalla
                bytesentrada = entrada.read(respuesta, 0, respuesta.length);
                for (int i = 0; i < bytesentrada; i++) {

                    mensaje += (char) (respuesta[i]);

                }
                String cadena, ced = null, nombre = "", direccion = null;
                System.out.println("mensaje " + mensaje);
                dataSource = n1.ConectarMysql();
                
                cf = dataSource.getConnection();

                System.out.println(mensaje);

                mensaje=mensaje.replaceAll(";","");
                
                String[] mensaje1 = mensaje.split(">>");
                System.out.println(mensaje1[0]);

                int j = 0;

                String[] campos = mensaje1[1].split(",");

             
                
                
                String numero;

                
                
                
                
                
                
                
                
                if (mensaje1[0].equalsIgnoreCase("ERR")) {

                    if(campos[0].trim().equalsIgnoreCase("DCP")){
                    
                      JOptionPane.showMessageDialog(this, campos[3]);
                    
                    }else if(campos[0].equalsIgnoreCase("FC"))
                    {
                    
                    numero = String.format("%09d", Integer.parseInt(campos[3]));

                    System.out.println("factura"+campos[1] + "-" + campos[2] + "-" + numero);

                    //System.out.println(mensaje);
                    
                    
                    
                    if (campos[0].trim().equals("NUMERO DE TRANSACCION YA EXISTE EN SOFI")) {

                        
                         System.out.println("entro");
                        
                        if (campos[0].equalsIgnoreCase("FC")) {
                            PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1,log='"+campos[4]+"' WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                            estadoF.execute();
                        } else if (campos[0].equalsIgnoreCase("NC")) {

                            PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`nota_credito` SET enviado_contable=1,log='"+campos[4]+"' WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                            estadoF.execute();

                        }

                    } else {
                        
                        
                        System.out.println("entro "+ mensaje);

                        if (campos[0].equalsIgnoreCase("FC")) {
                            PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=2,log='"+campos[4]+"' WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                            estadoF.execute();
                        } else if (campos[0].equalsIgnoreCase("NC")) {

                            PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`nota_credito`,log='"+campos[4]+"' SET enviado_contable=2 WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                            estadoF.execute();

                        }

                    }

                    // PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1 WHERE numero='" + ri.getInt(1) + "'");
                    //estadoF.execute();
                
                    }
                 } else if (mensaje1[0].equalsIgnoreCase("OK")) {

                    numero = String.format("%09d", Integer.parseInt(campos[3]));

                    System.out.println(campos[1] + "-" + campos[2] + "-" + numero);

                    if (campos[0].equalsIgnoreCase("FC")) {
                        PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`factura` SET enviado_contable=1,log='"+campos[4]+"' WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                        estadoF.execute();
                    } else if (campos[0].equalsIgnoreCase("NC")) {

                        PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`nota_credito` SET enviado_contable=1,log='"+campos[4]+"' WHERE numero='" + campos[1] + "-" + campos[2] + "-" + numero + "'");
                        estadoF.execute();

                    }

                } else if (mensaje1[0].equalsIgnoreCase("DCP")) {

                    //CODITEM\\DESCRIPCION\\PVP\\CANTIDAD--CODITEM\\DESCRIPCION\\PVP\\CANTIDAD
                    //String[] contenido = mensaje1[1].split(",");
                    
                    //System.out.println(contenido[2]);
                    
                    String[] detalle = mensaje1[1].split("--");
                    
                    
                    
                    int stock;

                    for (int i = 0; i < detalle.length; i++) {

                        
                        System.out.println("detalle "+detalle[i]);
                        
                        String[] item = detalle[i].split("\\\\");

                        Statement st_inc = cf.createStatement();

                        
                        
                        
                        System.out.println("SELECT * from producto where codigo_barra='" + item[3] + "'");
                        ResultSet rico = st_inc.executeQuery("SELECT * from producto where codigo_barra='" + item[3] + "'");

                        
                        if (rico.first()) {

                            System.out.println(item[6]);
                            
                            stock = rico.getInt(7) + Integer.parseInt(item[6].trim());

                            PreparedStatement estadoF = cf.prepareStatement("UPDATE `adv_facturacion`.`producto` SET `stock_actual`='" + stock + "' ,`punit`='" + item[5] + "' WHERE `idproducto`='" + rico.getString(1) + "';");
                            estadoF.execute();
                            
                           
                            
                            
                            
                        } else {

                                    
                            
                                //CODITEM\\DESCRIPCION\\PVP\\CANTIDAD||CODITEM\\DESCRIPCION\\PVP\\CANTIDAD
                            
                          //  System.out.println("INSERT INTO `adv_facturacion`.`producto` (`nombre`, `punit`, `nivel_precio`, `stock_minimo`, `stock_maximo`, `stock_actual`, `descuento`, `venta_maxima`, `venta_minima`, `punto_reorden`, `codigo_barra`, `tipo_producto_idtipo_producto`, `precio_sin_iva`) VALUES ('"+item[4]+"', '"+item[5]+"','1','1', '1', '"+item[5]+"', '0.00', '1', '1', '1', '"+item[2]+"', '2', "+Double.parseDouble(item[4])/1.12+");");
                            
                            PreparedStatement estadoF = cf.prepareStatement("INSERT INTO `adv_facturacion`.`producto` (`nombre`, `punit`, `nivel_precio`, `stock_minimo`, `stock_maximo`, `stock_actual`, `descuento`, `venta_maxima`, `venta_minima`, `punto_reorden`, `codigo_barra`, `tipo_producto_idtipo_producto`, `precio_sin_iva`) VALUES ('"+item[4]+"', '"+item[5]+"','1','1', '1', '"+item[6]+"', '0.00', '1', '1', '1', '"+item[3]+"', '2', "+Double.parseDouble(item[5])/1.12+");");
                            estadoF.execute();
                            
                         
                            
                        }

                    }

                     JOptionPane.showMessageDialog(this,"TRANSACCION "+detalle[0]+" :  "+detalle[1]+" MIGRADA CORRECTAMENTE");
                    
                    
                     PreparedStatement estadoC = cf.prepareStatement("INSERT INTO `adv_facturacion`.`compras` (`tipo_trans`, `numero_trans`, `fecha`) VALUES ('"+detalle[0]+"', '"+detalle[1]+"',curdate());");
                     estadoC.execute();
                    
                    String cod, num, coditem, des, pvp, cantidad;

                }

               System.out.println("\n mensaje" + mensaje);
            } catch (SQLException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }

        } while (!mensaje.equals("SERVIDOR>>> TERMINAR"));

    }
    
    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        version v = new version();
        v.setVisible(true);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        modificar_configuracion_general n = new modificar_configuracion_general();
        n.usuarios(usuarios, contraseña);

        n.setVisible(true);

    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenu10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu10ActionPerformed
    }//GEN-LAST:event_jMenu10ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        agregar_clientes ac = new agregar_clientes(usuarios, contraseña, "", "", "");

        ac.setVisible(true);
        ac.usuarios(usuarios, contraseña);

        ac.setLocation(0, 0);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed

        enviar_facturas_contingencia evf = new enviar_facturas_contingencia(usuarios, contraseña);
        evf.pack();

        escritorio.removeAll();

        escritorio.add(evf);

        evf.setLocation(0, 0);
        evf.setSize(dim.width - 10, dim.height - 100);

        evf.setVisible(true);

        evf.setResizable(false);

        evf.setClosable(true);

        evf.setUI(null);


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        AsignarCliente i = new AsignarCliente(usuarios, contraseña);
        i.setVisible(true);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

        escritorio.removeAll();
        EditarCliente ecliente = new EditarCliente(usuarios, contraseña);
        ecliente.pack();

        escritorio.add(ecliente);

        ecliente.setLocation(0, 0);
        ecliente.setSize(dim.width - 10, dim.height - 150);

        ecliente.setVisible(true);

        ecliente.setResizable(false);

        ecliente.setClosable(true);

        ecliente.setUI(null);
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        estados_cuenta ec = new estados_cuenta(usuarios, contraseña);
        ec.pack();
        escritorio.removeAll();

        escritorio.add(ec);
        ec.setLocation(0, 0);
        ec.setSize(dim.width - 10, dim.height - 100);

        ec.setVisible(true);

        ec.setResizable(false);

        ec.setClosable(true);

        ec.setUI(null);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        ReporteTurnos1 rd = new ReporteTurnos1(usuarios, contraseña);

        rd.setLocation(0, 0);

        rd.setVisible(true);


    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        escritorio.removeAll();
        EditarCliente ecliente = new EditarCliente(usuarios, contraseña);
        ecliente.pack();

        escritorio.add(ecliente);

        ecliente.setLocation(0, 0);
        ecliente.setSize(dim.width - 10, dim.height - 150);

        ecliente.setVisible(true);

        ecliente.setResizable(false);

        ecliente.setClosable(true);

        ecliente.setUI(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        agregar_clientes ac = new agregar_clientes(usuarios, contraseña, "", "", "");

        ac.setVisible(true);
        ac.usuarios(usuarios, contraseña);

        ac.setLocation(0, 0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        buscar n = new buscar(escritorio, usuarios, contraseña);
        n.setVisible(true);
        n.setLocation(50, 50);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        escritorio.removeAll();
        buscar n = new buscar(escritorio, usuarios, contraseña);
        n.setVisible(true);
        n.setLocation(50, 50);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed


    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        //escritorio.removeAll();

        PagaresAFactura pf = new PagaresAFactura(usuarios, contraseña);

        pf.setVisible(true);

       // escritorio.add(pf);
        //pf.setLocation(0, 0);
        // pf.setSize(dim.width - 10, dim.height - 150);
      //  pf.setVisible(true);
        //pf.setResizable(false);
        //pf.setClosable(true);
       // pf.setUI(null);

    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed

        buscarFacturaGasolina bF = new buscarFacturaGasolina(usuarios, contraseña);

        bF.setVisible(true);


    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        escritorio.removeAll();

        Reporte_clientes_credito rf = new Reporte_clientes_credito(usuarios, contraseña);

        escritorio.add(rf);

        rf.setLocation(0, 0);
        rf.setSize(dim.width - 10, dim.height - 150);

        rf.setVisible(true);

        rf.setResizable(false);

        //pf.setClosable(true);
        rf.setUI(null);
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        crear_pagare cp = new crear_pagare(usuarios, contraseña);
        cp.setVisible(true);
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        recuperar_facturas rf = new recuperar_facturas(usuarios, contraseña);
        rf.setVisible(true);
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        modificar_pagare cp = new modificar_pagare(usuarios, contraseña);
        cp.setVisible(true);
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed

    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed

        prepagoGasolina pg = new prepagoGasolina(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);


    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed

        enviar_facturas_contingencia2 evf = new enviar_facturas_contingencia2(usuarios, contraseña);
        evf.pack();

        escritorio.removeAll();

        escritorio.add(evf);

        evf.setLocation(0, 0);
        evf.setSize(dim.width - 10, dim.height - 100);

        evf.setVisible(true);

        evf.setResizable(false);

        evf.setClosable(true);

        evf.setUI(null);


    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        agregarArticulo pg = new agregarArticulo(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        Inventario pg = new Inventario(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        modificarArticulo pg = new modificarArticulo(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        eliminarArticulo pg = new eliminarArticulo(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        reporteProductos pg = new reporteProductos(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        reportesLubricantes pg = new reportesLubricantes(usuarios, contraseña, usuarios, contraseña);
        pg.setVisible(true);         // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        enviar_notas_contingencia evf = new enviar_notas_contingencia(usuarios, contraseña);
        evf.pack();

        escritorio.removeAll();

        escritorio.add(evf);

        evf.setLocation(0, 0);
        evf.setSize(dim.width - 10, dim.height - 100);

        evf.setVisible(true);

        evf.setResizable(false);

        evf.setClosable(true);

        evf.setUI(null);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed
        Reporte_diario rd = new Reporte_diario(usuarios, contraseña);
        rd.setVisible(true);
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        ConsultaClientesCredito a = new ConsultaClientesCredito(usuarios, contraseña);
        a.setVisible(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        try {
            InventarioTanques a = new InventarioTanques(usuarios, contraseña);        // TODO add your handling code here:
            a.setVisible(true);
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        PagaresAFacturaLubricantes a = new PagaresAFacturaLubricantes(usuarios, contraseña);
        a.setVisible(true);
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        compras.setVisible(true);
        compras.setBounds(100, 100, 300, 200);
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        
        try {
            Statement st_fdc = cf.createStatement();
            ResultSet ri_fdc = st_fdc.executeQuery("SELECT * FROM adv_facturacion.compras where tipo_trans='" + cod.getText() + "' and numero_trans='" + num.getText() + "' ;");
           if (ri_fdc.first()) {
                
                
               
                JOptionPane.showMessageDialog(this,"ERROR TRASACCION YA ESTA EN ADVGP2.0");
               
            }else
           {
            
            String cadena1 = "DCP>>" + cod.getText() + "," + num.getText() + ";";
            enviarDatos(cadena1);
           }
           
           
           } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed

        plaqueados pl= new plaqueados();
        pl.setVisible(true);
                

// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
        Reporte_diario_consolidado rc=new Reporte_diario_consolidado(usuarios, contraseña);
        rc.setVisible(true);
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        Reporte_Facturas evf = new  Reporte_Facturas(usuarios, contraseña);
        evf.pack();

        escritorio.removeAll();

        escritorio.add(evf);

        evf.setLocation(0, 0);
        evf.setSize(dim.width - 10, dim.height - 100);

        evf.setVisible(true);

        evf.setResizable(false);

        evf.setClosable(true);

        evf.setUI(null);
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed
               escritorio.removeAll();

        Reporte_clientes_prepago rf = new Reporte_clientes_prepago(usuarios, contraseña);

        escritorio.add(rf);

        rf.setLocation(0, 0);
        rf.setSize(dim.width - 10, dim.height - 150);

        rf.setVisible(true);

        rf.setResizable(false);

        //pf.setClosable(true);
        rf.setUI(null);

    }//GEN-LAST:event_jMenuItem42ActionPerformed

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed
        CierreDiario cd=new CierreDiario(usuarios,contraseña);
        cd.setVisible(true);
    }//GEN-LAST:event_jMenuItem43ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        ControlPasoArchivos ca=new ControlPasoArchivos(usuarios,contraseña);
        ca.setVisible(true);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cod;
    private javax.swing.JFrame compras;
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JTextField etiquetaNoti;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator21;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator25;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator27;
    private javax.swing.JPopupMenu.Separator jSeparator28;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea lubri;
    private javax.swing.JTextField num;
    // End of variables declaration//GEN-END:variables
 public void ejecutarCliente() {
        // conectarse al servidor, obtener flujos, procesar la conexión
        try {

            conectarAServidor(); // Paso 1: crear un socket para realizar la conexión
            obtenerFlujos();      // Paso 2: obtener los flujos de entrada y salida
            procesarConexion();

            //this.repaint();// Paso 3: procesar la conexión
        } // el servidor cerró la conexión
        catch (EOFException | SocketTimeoutException excepcionEOF) {
            System.err.println("El cliente termino la conexión");
        } // procesar los problemas que pueden ocurrir al comunicarse con el servidor
        catch (IOException excepcionES) {
            excepcionES.printStackTrace();
        } finally {
            cerrarConexion(); // Paso 4: cerrar la conexión

        }

    }
private void cerrarConexion() {

        // mostrarMensaje("\nServidor no Responde Revise la Conexion");
        //establecerCampoTextoEditable(false); // deshabilitar campoIntroducir
    }

   
    public void sw() {

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {

                // aplicacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                ejecutarCliente();

// aplicacion.setVisible(true);
                return null;

            }

            @Override
            protected void done() {

                try {
                    String str = get();

                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        };

        worker.execute();
        System.out.println("Start");

    }
    public void perfil(String usua, String contra) {
        try {

            servidores();
            usuarios = usua;
            contraseña = contra;

            conexion_facturacion n = new conexion_facturacion(usuarios, contraseña);
            n.conectar();

            Statement st_s5 = n.coneccion.createStatement();

            ResultSet rids5 = st_s5.executeQuery("SELECT cargo,nombre,idusuarios FROM adv_facturacion.usuarios where usuario='" + usuarios + "';");
            while (rids5.next()) {

                cargo = rids5.getString(1);
                nombre = rids5.getString(2);
                idu = rids5.getString(3);

            }
            
            ResultSet ridsE = st_s5.executeQuery("SELECT nombre_comercial FROM adv_facturacion.datos_gasolinera;");
            while (ridsE.next()) {

               this.setTitle(ridsE.getString(1));
               

            }

            if (cargo.equalsIgnoreCase("adv")) {
            }

            if (cargo.equalsIgnoreCase("administrador")) {

                jMenu10.setVisible(false);

            }
            if (cargo.equalsIgnoreCase("despachador")) {

                JOptionPane.showMessageDialog(null, "Error no tiene permisos para Ingresar a esta aplicacion", "Cancelacion", JOptionPane.ERROR_MESSAGE);
                System.exit(0);

            }
            if (cargo.equalsIgnoreCase("Secretaria")) {

                jMenu10.setVisible(false);

            }
            if (cargo.equalsIgnoreCase("aux.contable")) {

                jMenu10.setVisible(false);

            }
            if (cargo.equalsIgnoreCase("ayudante contable")) {

                jMenu7.setVisible(false);
                jMenu10.setVisible(false);

            }

            n.coneccion.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    String ipsofi,puerto;
    
//para cortar el papel de mi ticketera
 
    
//para cortar el papel de mi ticketera
    public void servidores() {

        try {
            java.io.BufferedReader buffer = new java.io.BufferedReader(new java.io.FileReader("servidores.adv"));
            String linea = "";

            int cont = 0;
            while ((linea = buffer.readLine()) != null) {

                //ip=linea.substring(1,2);
                if (cont == 1) {
                    ip = linea.substring(18, linea.length());

                }

                 if (cont == 2) {
                    ipsofi = linea.substring(17, linea.length());

                }
                if (cont == 4) {
                    puerto = linea.substring(linea.indexOf(":")+1, linea.length());

                }
                
                
                
                cont++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
