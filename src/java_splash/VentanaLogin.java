package java_splash;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import Principal.Principal;
import conexion.conexion_facturacion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Jhon
 */
public class VentanaLogin extends JFrame {

    private JTextField txtUser, txtPass;
    private JLabel lblUser, lblPass;
    private JButton btnAceptar, btnCancelar;
    String usuario, elPassword;

    VentanaLogin() {

        Container contenedor = getContentPane();
        contenedor.setLayout(new FlowLayout());

        // crear etiqueta y cuadro de texxto del usuario
        txtUser = new JTextField(10);
        lblUser = new JLabel("Usuario: ");
        txtUser.setToolTipText("Escriba su nombre de usuario");
        contenedor.add(Box.createVerticalStrut(50));
        contenedor.add(lblUser);
        contenedor.add(txtUser);

        //crear etiqueta y cuadro de texxto del pw
        txtPass = new JPasswordField(10);
        lblPass = new JLabel("Contraseña: ");
        txtPass.setToolTipText("Escriba su contraseña");
        contenedor.add(lblPass);
        contenedor.add(txtPass);

        //Crear y agregar los botones 
        btnAceptar = new JButton("Aceptar");
        //establecer Boton aceptar por defecto
        getRootPane().setDefaultButton(btnAceptar);

        btnCancelar = new JButton("Cancelar");
        contenedor.add(btnAceptar);
        contenedor.add(btnCancelar);



        // Crear un escuchador al boton Aceptar 
        ActionListener escuchadorbtnAceptar;
        escuchadorbtnAceptar = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    //chekar si el usuario escrbio el nombre de usuario y pw
                    if (txtUser.getText().length() > 0 && txtPass.getText().length() > 0) {
                        // Si el usuario si fue validado correctamente
                        if (txtPass.getText().equalsIgnoreCase("1234")) {
                            try {
                                conexion_facturacion n = new conexion_facturacion( txtUser.getText() , txtPass.getText());
                                n.conectar();
                                String a = "SELECT * FROM usuarios WHERE usuario='" + txtUser.getText() + "' AND AES_DECRYPT(contraseña,'thekey')='" + txtPass.getText() + "'";
                                ResultSet consulta = n.consulta(a);

                                if (consulta.first()) {

                                     JPasswordField pas = new JPasswordField();
                                    Object[] obj = {"Ingrese la contraseña del usuario" + ":\n\n", pas};
                                    Object stringArray[] = {"OK", "Cancel"};
                                    String pass = null;
                                    Component th = null;
                                    String titulo = "ingrese su contraseña";
                                    JOptionPane.showOptionDialog(th, obj, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, stringArray, obj);
                                     String texto = pas.getText();
                                    
                                    //   String texto= JOptionPane.showInputDialog(rootPane, "Se debe cambiar su Contraseña: ", "showInputDialog", JOptionPane.INFORMATION_MESSAGE);

                                    //
                                    String consul = ("UPDATE `adv_facturacion`.`usuarios` SET `contraseña`=AES_ENCRYPT('" + texto + "','thekey') WHERE `usuario`='" + txtUser.getText() + "';");
                                    Statement st1 = n.coneccion.createStatement();
                                    st1.executeUpdate(consul);

                                    Statement st_u = n.coneccion.createStatement();
                                    ResultSet riu = st_u.executeQuery("set password for " + txtUser.getText() + "@'%' = password('" + texto + "');");

                                    /* Statement st_u1 = n.coneccion.createStatement();
                                     ResultSet riu1 = st_u1.executeQuery("update user set password=PASSWORD(\""+texto+"\") where user=\""+usuario.getText()+"\";");
                                     */

                                    Statement st_u2 = n.coneccion.createStatement();
                                    ResultSet riu2 = st_u2.executeQuery("flush privileges;");



                                    JOptionPane.showMessageDialog(rootPane, "Contraseña Cambiada Correctamente");


                                }
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(VentanaLogin.class.getName()).log(Level.SEVERE, null, ex);
                                


                            } catch (SQLException ex) {
                                Logger.getLogger(VentanaLogin.class.getName()).log(Level.SEVERE, null, ex);
                            
                                    JOptionPane.showMessageDialog(rootPane, "Usuario y contraseña invalidos");
                            
                            }

                        } else if (validarUsuario(txtUser.getText(), txtPass.getText())) //enviar datos a validar
                        {
                            // Codigo para mostrar la ventana principal
                            setVisible(false);
                            Principal p = new Principal();
                            p.setVisible(true);
                            p.usuarios = txtUser.getText();
                            p.perfil(txtUser.getText(), txtPass.getText());


                        } else {
                            JOptionPane.showMessageDialog(null, "El nombre de usuario y/o contrasenia no son validos.");
                            txtUser.setText("");    //limpiar campos
                            txtPass.setText("");

                            txtUser.requestFocusInWindow();
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Debe escribir nombre de usuario y contrasenia.\n"
                                + "NO puede dejar ningun campo vacio");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        btnAceptar.addActionListener(escuchadorbtnAceptar);      // Asociar escuchador para el boton Aceptar


        // Agregar escuchador al boton Cancelar
        ActionListener escuchadorbtnCancelar = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.exit(0);         // terminar el programa
            }
        };
        btnCancelar.addActionListener(escuchadorbtnCancelar);      // Asociar escuchador para el boton Cancelar
        setTitle("Autentificacion de usuarios");
        setSize(400, 150);           // Tamanio del Frame 
        setResizable(false);       // que no se le pueda cambiar el tamanio 
        //Centrar la ventana de autentificacion en la pantalla
        Dimension tamFrame = this.getSize();//para obtener las dimensiones del frame
        Dimension tamPantalla = Toolkit.getDefaultToolkit().getScreenSize();      //para obtener el tamanio de la pantalla
        setLocation((tamPantalla.width - tamFrame.width) / 2, (tamPantalla.height - tamFrame.height) / 2);  //para posicionar
        setVisible(true);           // Hacer visible al frame 

    }   // fin de constructor

    // Metodo que conecta con el servidor MYSQL y valida los usuarios
    boolean validarUsuario(String elUsr, String elPw) throws IOException {
        try {
            //nombre de la BD: bdlogin
            //nombre de la tabla: usuarios
            //                             id      integer auto_increment not null     <--llave primaria
            //                   campos:    usuario    char(25)
            //                              password char(50)

            //   Connection unaConexion  = DriverManager.getConnection ("jdbc:mysql://localhost/laboratorio","root", "manager");
            // Preparamos la consulta
            // Statement instruccionSQL = unaConexion.createStatement();

            conexion_facturacion n = new conexion_facturacion("root", "manager");
            n.conectar();

            String a = "SELECT * FROM usuarios WHERE usuario='" + elUsr + "' AND AES_DECRYPT(contraseña,'thekey')='" + elPw + "'";
            
           // System.out.println(a);
            ResultSet consulta = n.consulta(a);

            if (consulta.first()) // si es valido el primer reg. hay una fila, tons el usuario y su pw existen
            {
                return true;        //usuario validado correctamente
            } else {
                return false;        //usuario validado incorrectamente
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}