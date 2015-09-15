/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Principal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Advantech Digital
 */
public class ModeloTablaXML extends AbstractTableModel {
    
    private int rows;
    private String[] columnNames = {"XML", "Tamaño", "Fecha Creacion", "Fecha Modificacion"};
    public String[][] data;
    
    public ModeloTablaXML(File[] xml){
        update(xml);
    }
    
    public final void update(File[] xml){
        if (xml != null || xml.length != 0) {
            data = new String[xml.length][columnNames.length];
            try {
                for (int i = 0; i < xml.length; i++){
                    if (xml[i].isFile()){
                        BasicFileAttributes attrs = Files.readAttributes(xml[i].toPath(), BasicFileAttributes.class);
                        data[i][0] = xml[i].getName();
                        data[i][1] = String.format("%d B", attrs.size());
                        data[i][2] = String.format("%1$tY/%1$tm/%1$td", new Date(attrs.creationTime().toMillis()));
                        data[i][3] = String.format("%1$tY/%1$tm/%1$td", new Date(attrs.lastModifiedTime().toMillis()));
                    }
                }
            rows = xml.length;
            fireTableStructureChanged();
            } catch (IOException  ex) {
                System.err.println(ex.getMessage());
            }
        }
        else
            JOptionPane.showMessageDialog(null, "No  existen facturas de consumidor final");
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return columnNames[i].getClass();
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int i, int j) {
        if (data != null)
            return data[i][j];
        return "";
    }

    @Override
    public boolean isCellEditable(int i, int j) {
        return false;
    }
}
