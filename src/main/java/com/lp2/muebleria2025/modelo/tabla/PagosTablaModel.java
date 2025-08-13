/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;

import com.lp2.muebleria2025.modelo.Pagos;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class PagosTablaModel extends AbstractTableModel {
    //private Integer cuotas_pagadas = 0; 
    List<Pagos> lista;
    private String[] columnas = {"ID VENTA", "NOMBRE", "CI/RUC" , "CELULAR","PRODUCTO","TOTAL","MONTO CUOTAS","T. CUOTAS","CUOTAS PAGADAS","PAGADO","SALDO", "VENCIMIENTO","ESTADO"};

    public void setLista(List<Pagos> lista) {
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
        Pagos pagos= lista.get(fila);
        switch (columna) {
            case 0:
                return pagos.getIdventa();
            case 1:
                return pagos.getIdcliente().getRazonSocial();
            case 2:
                return pagos.getIdcliente().getRut();
            case 3:
                return pagos.getIdcliente().getTelefono();
            case 4:
                return pagos.getProducto();
            case 5:
                return pagos.getTotal();
            case 6:
                return pagos.getMontocuota();
            case 7:
                return pagos.getTotal_cuotas();
            case 8:
                //cuotas_pagadas = pagos.getPagado()/(pagos.getMontocuota());
                return pagos.getCuotas_pagadas();
            case 9:
                return pagos.getPagado();
            case 10:
                return pagos.getSaldo();
            case 11:
                return pagos.getVencimiento();
            case 12:
                return pagos.getEstado();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Pagos getPagosByRow(int index) {
        return lista.get(index);
    }
    
    public void getPagosByGui(){
        
    }
}
