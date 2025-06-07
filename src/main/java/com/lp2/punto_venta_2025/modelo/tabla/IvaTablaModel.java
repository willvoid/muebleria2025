/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.tabla;


import com.lp2.punto_venta_2025.modelo.Iva;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class IvaTablaModel extends AbstractTableModel{
    List<Iva> lista;
    private String[] columnas ={"ID","NOMBRE","VALOR"};
    
    public void setLista(List<Iva> lista){
        // Inicializamos las lista de ivas
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
        Iva iva = lista.get(fila);
        switch (columna) {
            case 0:
                return iva.getId();
            case 1:
                return iva.getNombre();
                
                 case 2:
                return iva.getValor();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column]; 
    }
    
    public Iva getIvaByRow(int index){
        return lista.get(index);
    }
    
    
}
