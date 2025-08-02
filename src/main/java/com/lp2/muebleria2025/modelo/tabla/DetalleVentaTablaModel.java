package com.lp2.muebleria2025.modelo.tabla;

import com.lp2.muebleria2025.modelo.DetalleVentas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DetalleVentaTablaModel extends AbstractTableModel {

    private List<DetalleVentas> lista = new ArrayList<>();
    private String[] columnas = {"ID", "DESCRIPCION", "CANTIDAD", "PRECIO", "SUBTOTAL", "DESCUENTO MAX"};

    public void setLista(List<DetalleVentas> lista) {
        this.lista = lista;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        DetalleVentas detalle_ventas = lista.get(fila);
        switch (columna) {
            case 0: return detalle_ventas.getIdproducto();
            case 1: return detalle_ventas.getProducto();
            case 2: return detalle_ventas.getCantidad();
            case 3: return detalle_ventas.getPrecio();
            case 4: return detalle_ventas.getSubtotal();
            case 5: return detalle_ventas.getPrecio_descuento();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public DetalleVentas getDetalleVentasByRow(int index) {
            return lista.get(index);
    }
}
