/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;
import com.lp2.muebleria2025.modelo.Compras;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class ComprasTablaModel extends AbstractTableModel {

    List<Compras> lista;
    private String[] columnas = {"ID", "FECHA", "PROVEEDOR", "METODO DE PAGO", "TOTAL"};

    public void setLista(List<Compras> lista) {
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
        Compras ventas= lista.get(fila);
        switch (columna) {
            case 0:
                return ventas.getIdCompras();
            case 1:
                return ventas.getFechaCompra();
            case 2:
                return ventas.getIdProveedor().getRazonSocial();
            case 3:
                return ventas.getMetodo_pago();
            case 4:
                return ventas.getTotal();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Compras getComprasByRow(int index) {
        return lista.get(index);
    }
    
    public void getComprasByGui(){
        
    }
}
