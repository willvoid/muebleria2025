package com.lp2.muebleria2025.modelo.tabla;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class EstadoCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Llamar al método padre para mantener la funcionalidad básica
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verificar el valor de la celda y cambiar el color según corresponda
        if (value != null && value.toString().equalsIgnoreCase("ATRASADO")) {
            cell.setBackground(Color.RED);  // Fondo rojo
            cell.setForeground(Color.WHITE); // Texto blanco para mejor contraste
        } else if (value != null && value.toString().equalsIgnoreCase("0")) {
            cell.setBackground(Color.RED);  // Fondo rojo
            cell.setForeground(Color.WHITE); // Texto blanco para mejor contraste
        } else if (value != null && value.toString().equalsIgnoreCase("COBRAR HOY")) {
            cell.setBackground(Color.BLUE);  // Fondo amarillo
            cell.setForeground(Color.WHITE);  // Texto negro
        } else if (value != null && value.toString().equalsIgnoreCase("AL DIA")) {
            cell.setBackground(Color.GREEN);  
            cell.setForeground(Color.WHITE);
        } else if (value != null && value.toString().equalsIgnoreCase("PAGADO")){
            cell.setBackground(Color.CYAN);
            cell.setForeground(Color.BLACK);
        }

        return cell;
    }
}
