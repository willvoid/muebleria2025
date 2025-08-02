/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;


import com.lp2.muebleria2025.modelo.Transaccion;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class IngresosTablaModel extends AbstractTableModel {

    List<Transaccion> lista;
    private String[] columnas = {"ID", "MONTO", "FECHA", "CONCEPTO", "METODO DE PAGO", "ID OPERACION"};

    public void setLista(List<Transaccion> lista) {
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
        Transaccion transaccion = lista.get(fila);
        switch (columna) {
            case 0:
                return transaccion.getId();
            case 1:
                return transaccion.getMonto();
            case 2:
                return transaccion.getFecha();
            case 3:
                return transaccion.getConcepto();
            case 4:
                return transaccion.getForma_pago();
            case 5:
                return transaccion.getIdpago(); 
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Transaccion getTransaccionByRow(int index) {
        return lista.get(index);
    }

}
