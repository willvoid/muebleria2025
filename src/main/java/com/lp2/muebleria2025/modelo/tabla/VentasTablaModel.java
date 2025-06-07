/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;

import com.lp2.muebleria2025.modelo.Ventas;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class VentasTablaModel extends AbstractTableModel {

    List<Ventas> lista;
    private String[] columnas = {"ID", "FECHA", "CLIENTE", "METODO DE PAGO", "TOTAL", "ESTADO"};

    public void setLista(List<Ventas> lista) {
        // Inicializamos las lista de productos
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        // El tamaño de la lista
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        // El numero de columnass
        return columnas.length;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Ventas ventas= lista.get(fila);
        switch (columna) {
            case 0:
                return ventas.getIdVentas();
            case 1:
                return ventas.getFechaVenta();
            case 2:
                return ventas.getIdCliente().getRazonSocial();
            case 3:
                return ventas.getMetodo_pago();
            case 4:
                return ventas.getTotal();
            case 5:
                return ventas.getEstado();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Ventas getVentasByRow(int index) {
        return lista.get(index);
    }
    
    public void getVentasByGui(){
        
    }
}
