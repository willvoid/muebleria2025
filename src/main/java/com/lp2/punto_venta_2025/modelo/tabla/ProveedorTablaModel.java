/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.tabla;
import com.lp2.punto_venta_2025.modelo.Proveedor;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class ProveedorTablaModel extends AbstractTableModel {

    List<Proveedor> lista;
    private String[] columnas = {"ID", "CI / RUC", "RAZON SOCIAL", "TELEFONO", "DIRECCION", "CORREO"};

    public void setLista(List<Proveedor> lista) {
        // Inicializamos las lista de proveedor
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
        Proveedor proveedor = lista.get(fila);
        switch (columna) {
            case 0:
                return proveedor.getId();
            case 1:
                return proveedor.getRut();
            case 2:
                return proveedor.getRazonSocial();
            case 3:
                return proveedor.getTelefono();
            case 4:
                return proveedor.getDireccion();
            case 5:
                return proveedor.getCorreo();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Proveedor getProveedorByRow(int index) {
        return lista.get(index);
    }

}
