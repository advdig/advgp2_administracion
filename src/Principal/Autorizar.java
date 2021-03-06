/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import conexion.ConnectionTableDB;
import java.awt.event.KeyAdapter;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Advantech Digital
 */
public class Autorizar extends javax.swing.JFrame {
    String id, cliente, fPago;
    double cupo;
    ConnectionTableDB model;
    boolean isClosed = false, cancelado = false;
    JTextField ruc;
    
    /**
     * Creates new form Autorizar
     */
    public Autorizar(boolean isPrepago, String cliente, String id, String cod, String dir,
                    String aPago, String fPago, String cupo, ConnectionTableDB model, JTextField ruc) {
        this.id = id;
        this.cliente = cliente;
        this.model = model;
        this.ruc = ruc;
        this.fPago = fPago;
        int is = 0, confirm, set;
        if (!isPrepago) {
            is = JOptionPane.showConfirmDialog(getContentPane(), 
                    "Ha seleccionado un cliente que NO tiene " + fPago + ".\n"
                    + "Desea continuar?", 
                    "Confirmar Cliente", 
                    JOptionPane.YES_NO_OPTION);
        } /*else {
            is = JOptionPane.showConfirmDialog(getContentPane(), 
                    "Ha seleccionado un cliente PREPAGO.\n"
                    + "Presione Aceptar para Editar el CUPO,\n"
                    + "Presione Cancelar para Cancelar PREPAGO.", 
                    "Confirmar Cliente", 
                    JOptionPane.OK_CANCEL_OPTION);
        }*/
        if (is == JOptionPane.YES_OPTION/* || is == JOptionPane.OK_OPTION*/) {
            javax.swing.JTextArea label = new javax.swing.JTextArea(String.format("%-15s:%-50s\n%-15s:%-50s\n%-15s:%-50s", 
                    "Cliente:", cliente,
                    "Id:", id,
                    "Tipo de Pago:", aPago.toUpperCase()));
            label.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
            label.setEditable(false);
            label.setBorder(null);
            label.setBackground(getBackground());
            confirm = JOptionPane.showConfirmDialog(getContentPane(),
                label, 
                "Confirmar Cliente", 
                JOptionPane.OK_CANCEL_OPTION);
            cancelado = false;
            if (confirm == JOptionPane.OK_OPTION) {
                javax.swing.JPanel panel = inputPanel(cupo.replace(',', '.'));
                if (isPrepago)
                    set = JOptionPane.showConfirmDialog(getContentPane(),
                                panel, 
                                "Editar Cupo", 
                                JOptionPane.OK_CANCEL_OPTION);
                else
                    set = JOptionPane.showConfirmDialog(getContentPane(),
                                panel, 
                                "Ingresar Cupo", 
                                JOptionPane.OK_CANCEL_OPTION);
                cancelado = false;
                if (set == JOptionPane.OK_OPTION) {
                    this.cupo = ((Number)((javax.swing.JFormattedTextField)panel.getComponent(3)).getValue()).doubleValue();
                    cancelado = false;
                } else cancelado = true;
            } else cancelado = true;
        } /*else if (is == JOptionPane.CANCEL_OPTION) {
            try {
                this.model.ejecutar("UPDATE cliente "
                        + "SET credito_cliente = 0, "
                        + "estado = 'No Autorizado', "
                        + "cupo_cliente = 0 "
                        + "WHERE cedula_ruc = '" + id + "'");
                JOptionPane.showMessageDialog(getContentPane(), 
                        "Cliente PREPAGO cancelado correctamente!", 
                        "Cancelar PREPAGO", 
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }*/ else {
            cancelado = true;
        }
        if (!cancelado) {
            initComponents();
            lblCliente.setText(cliente);
            lblId.setText(id);
            lblCodigo.setText(cod);
            lblDireccion.setText(dir);
            lblPago.setText(fPago.toUpperCase());
            lblCupo.setText(String.format("$ %s ", cupo));
            txtCupo.setText(String.format("$ %.2f", this.cupo));
        } else {
            initComponents();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblPago = new javax.swing.JLabel();
        lblCliente = new javax.swing.JLabel();
        lblId = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        lblCupo = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCupo = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        lblDireccion = new javax.swing.JLabel();
        chkSuper = new javax.swing.JCheckBox();
        chkExtra = new javax.swing.JCheckBox();
        chkDiesel = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnAsignarPrepago = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CLIENTE " + fPago);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Usuario:");
        jPanel1.add(jLabel1);

        lblUsuario.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblUsuario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel1.add(lblUsuario);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setText("Cliente:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel3.setText("Id:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel4.setText("Código:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel5.setText("Tipo de Pago:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel6.setText("Cupo Actual:");

        lblPago.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        lblCliente.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        lblId.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        lblCodigo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        lblCupo.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        lblCupo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel7.setText("Cupo Nuevo:");

        txtCupo.setEditable(false);
        txtCupo.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        txtCupo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel8.setText("Dirección:");

        lblDireccion.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N

        chkSuper.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        chkSuper.setText("Super");

        chkExtra.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        chkExtra.setText("Extra");

        chkDiesel.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        chkDiesel.setText("Diesel");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel9.setText("Productos:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCupo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblCupo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(lblPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkExtra)
                            .addComponent(jLabel9)
                            .addComponent(chkSuper)
                            .addComponent(chkDiesel))
                        .addGap(80, 80, 80))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblId, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(lblDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(lblPago, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCupo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCupo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkExtra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSuper)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkDiesel)))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        btnAsignarPrepago.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        btnAsignarPrepago.setText("ASIGNAR " + fPago);
        btnAsignarPrepago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarPrepagoActionPerformed(evt);
            }
        });
        jPanel3.add(btnAsignarPrepago);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean isClosed() {
        return isClosed;
    }
    
    public void checkCancel() {
        if (cancelado) {
            this.dispose();
        }
    }
    
    private void btnAsignarPrepagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarPrepagoActionPerformed
        // TODO add your handling code here:
        Object[] creditos = {"Normal", "Especial"};
        String tipoCredito = (String)JOptionPane.showInputDialog(this, 
                "Seleccione el tipo de Crédito:", 
                "Tipo de Credito", 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                creditos, 
                "Normal");
        int tipo = tipoCredito.equalsIgnoreCase("Especial") ? 2 : 1;
        try {
            model.ejecutar("UPDATE cliente "
                    + "SET credito_cliente = 1, "
                    + "estado = 'AUTORIZADO', "
                    + "cupo_cliente = " + String.format(Locale.UK, "%.2f", cupo) + " "
                    
                    + "WHERE cedula_ruc = '" + id + "'");
            if (btnAsignarPrepago.getText().equalsIgnoreCase("ASIGNAR CREDITO")) {
                model.consulta("SELECT idcliente "
                        + "FROM cliente "
                        + "WHERE cedula_ruc = '" + id + "'");
                int idCliente = (Integer)model.getValueAt(0, 0);
                model.consulta("SELECT * "
                        + "FROM credito_cliente "
                        + "WHERE cliente_idcliente = " + idCliente);
                String productos = "";
                if (chkExtra.isSelected()) {
                    productos += "1";
                    if (chkSuper.isSelected() || chkDiesel.isSelected())
                        productos += ",";
                }
                if (chkSuper.isSelected()) {
                    productos += "2";
                    if (chkDiesel.isSelected())
                        productos += ",";
                }
                if (chkDiesel.isSelected()) productos += "3";
                if (model.getRowCount() != 0) {
                    model.ejecutar("UPDATE credito_cliente "
                            + "SET cupo_cliente = " + String.format(Locale.UK, "%.2f", cupo) + ", "
                            + "tipo_cliente = " + tipo + ", "
                            + "gasolina = '" + productos + "' "
                            + "WHERE cliente_idcliente = '" + idCliente + "'");
                } else {
                    model.ejecutar("INSERT INTO credito_cliente (cupo_cliente, cliente_idcliente, gasolina,tipo_cliente) "
                            + "VALUES ('" + String.format(Locale.UK, "%.2f", cupo) + "', "
                            + "" + idCliente + ", "
                            + "'" + productos + "',"+tipo+") ");
                }
            }
            javax.swing.JTextArea label = new javax.swing.JTextArea(fPago + " Autorizado!\n"
                    + "Cliente: " + cliente + "\n"
                    + "Cupo: " + txtCupo.getText() + "\n");
            label.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
            label.setEditable(false);
            label.setBorder(null);
            label.setBackground(getBackground());
            JOptionPane.showMessageDialog(this, 
                    label, 
                    "Autorizacion Exitosa: " + lblUsuario.getText(), 
                    JOptionPane.INFORMATION_MESSAGE);
            model.consulta("SELECT idcliente AS ID, nombre AS NOMBRE, "
                        + "cedula_ruc AS 'CEDULA/RUC', codigo AS CODIGO, "
                        + "email AS CORREO, credito_cliente AS 'CREDITO?', "
                        + "cupo_cliente AS CUPO "
                        + "FROM cliente, codigos WHERE idcliente = 0");
            dispose();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }//GEN-LAST:event_btnAsignarPrepagoActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        isClosed = true;
        ruc.requestFocus();
    }//GEN-LAST:event_formWindowClosed

    private javax.swing.JPanel inputPanel(final String cupo) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2));
        javax.swing.JLabel label = new javax.swing.JLabel("Ingrese el cupo de " + fPago);
        label.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
        panel.add(label);
        panel.add(new javax.swing.JLabel());
        javax.swing.JLabel labelCupo = new javax.swing.JLabel("Cupo:");
        labelCupo.setFont(new java.awt.Font("Consolas", java.awt.Font.PLAIN, 14));
        panel.add(labelCupo);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMinimumFractionDigits(2);
        final JFormattedTextField text = new JFormattedTextField(format);
        text.setValue(Double.parseDouble(cupo));
        text.setColumns(10);
        text.setFont(new java.awt.Font("Consolas", java.awt.Font.BOLD, 14));
        text.setHorizontalAlignment(JFormattedTextField.RIGHT);
        text.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    Double.parseDouble(String.format(Locale.US, "%.2f", ((Number)text.getValue()).doubleValue()));
                } catch (NumberFormatException ex) {
                    System.err.println(ex.getMessage());
                    JOptionPane.showMessageDialog(getContentPane(), 
                            "Formato de número incorrecto!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    text.setValue(Double.parseDouble(cupo));
                    text.requestFocus();
                }
            }
        });
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != ',' && c != '.')
                    evt.consume();
            }
        });
        panel.add(text);
        text.requestFocus();
        text.select(2, text.getText().length());
        return panel;
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
            System.err.println(ex.getMessage());
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new Autorizar().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignarPrepago;
    private javax.swing.JCheckBox chkDiesel;
    private javax.swing.JCheckBox chkExtra;
    private javax.swing.JCheckBox chkSuper;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblCliente;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblCupo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblPago;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtCupo;
    // End of variables declaration//GEN-END:variables
}
