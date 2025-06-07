/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;


import com.lp2.muebleria2025.modelo.Descuentos;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class DescuentosTablaModel extends AbstractTableModel {

    List<Descuentos> lista;
    private String[] columnas = {"ID", "NOMBRE", "DESDE", "HASTA", "DESCUENTO", "PRODUCTO"};

    public void setLista(List<Descuentos> lista) {
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
        Descuentos producto = lista.get(fila);
        switch (columna) {
            case 0:
                return producto.getId();
            case 1:
                return producto.getNombre();
            case 2:
                return producto.getDesde();
            case 3:
                return producto.getHasta();
            case 4:
                return producto.getDescuento();
            case 5:
                return producto.getProducto().getNombre();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Descuentos getDescuentosByRow(int index) {
        return lista.get(index);
    }

}
