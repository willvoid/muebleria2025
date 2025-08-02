package com.lp2.muebleria2025.modelo.tabla;


import com.lp2.muebleria2025.modelo.DetalleCompras;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DetalleComprasTablaModel extends AbstractTableModel {

    private List<DetalleCompras> lista = new ArrayList<>();
    private String[] columnas = {"ID", "DESCRIPCION", "CANTIDAD", "PRECIO", "SUBTOTAL"};

    public void setLista(List<DetalleCompras> lista) {
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
        DetalleCompras detalle_compras = lista.get(fila);
        switch (columna) {
            case 0: return detalle_compras.getIdproducto();
            case 1: return detalle_compras.getProducto();
            case 2: return detalle_compras.getCantidad();
            case 3: return detalle_compras.getPrecio();
            case 4: return detalle_compras.getSubtotal();    
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public DetalleCompras getDetalleComprasByRow(int index) {
            return lista.get(index);
    }
}
