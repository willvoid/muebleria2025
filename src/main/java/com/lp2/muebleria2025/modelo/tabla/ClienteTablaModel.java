/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;


import com.lp2.muebleria2025.modelo.Cliente;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class ClienteTablaModel extends AbstractTableModel {

    List<Cliente> lista;
    private String[] columnas = {"ID", "CI / RUC", "RAZON SOCIAL", "TELEFONO", "DIRECCION","CORREO"};

    public void setLista(List<Cliente> lista) {
        // Inicializamos las lista de marcas
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
        Cliente marca = lista.get(fila);
        switch (columna) {
            case 0:
                return marca.getId();
            case 1:
                return marca.getRut();
            case 2:
                return marca.getRazonSocial();
            case 3:
                return marca.getTelefono();
            case 4:
                return marca.getDireccion();
            case 5:
                return marca.getCorreo();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Cliente getClienteByRow(int index) {
        return lista.get(index);
    }

}
