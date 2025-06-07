/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.tabla;

import com.lp2.muebleria2025.modelo.Usuario;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cmendieta
 */
public class UsuarioTablaModel extends AbstractTableModel {

    List<Usuario> lista;
    private String[] columnas = {"ID", "NOMBRE", "APELLIDO","USUARIO" ,"ROL", "ESTADO"};

    public void setLista(List<Usuario> lista) {
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
        Usuario marca = lista.get(fila);
        switch (columna) {
            case 0:
                return marca.getId();
            case 1:
                return marca.getNombre();
            case 2:
                return marca.getApellido();
            case 3:
                return marca.getUsuario();
            case 4:
                return marca.getRol();
            case 5:
                return marca.getEstado();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public Usuario getUsuarioByRow(int index) {
        return lista.get(index);
    }

}
