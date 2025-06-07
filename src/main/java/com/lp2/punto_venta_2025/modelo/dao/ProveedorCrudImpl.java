/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.lp2.punto_venta_2025.modelo.Proveedor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class ProveedorCrudImpl implements Crud<Proveedor> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public ProveedorCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Proveedor m) {
        try {
            //Preparar sentencia
            String sql = "insert into proveedores (ruc, nombre, telefono, direccion, correo) values (?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getRut());
            sentencia.setString(2, m.getRazonSocial());
            sentencia.setString(3, m.getTelefono());
            sentencia.setString(4, m.getDireccion());
            sentencia.setString(5, m.getCorreo());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Proveedor obj) {
        try {
            String sql = "update proveedores set ruc=?,nombre=?,telefono=?,direccion=?,correo=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getRut());
            sentencia.setString(2, obj.getRazonSocial());
            sentencia.setString(3, obj.getTelefono());
            sentencia.setString(4, obj.getDireccion());
            sentencia.setString(5, obj.getCorreo());

            sentencia.setInt(6, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Proveedor obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from proveedores where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProveedorCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

public ArrayList<Proveedor> listarcbo(String textoBuscado) {
    System.out.println("Texto buscado: " + textoBuscado);
    ArrayList<Proveedor> lista = new ArrayList<>();
    try {
        // Consulta para obtener todos los proveedors
        String sql = "SELECT * FROM proveedores WHERE nombre ILIKE ? ORDER BY id ASC";
        sentencia = conec.prepareStatement(sql);
        sentencia.setString(1, "%" + textoBuscado + "%");
        ResultSet rs = sentencia.executeQuery();

        // Recorrer los resultados
        while (rs.next()) {
            Proveedor proveedor = new Proveedor();
            proveedor.setId(rs.getInt("id")); // Cambia "id" por el nombre correcto de la columna
            proveedor.setRazonSocial(rs.getString("nombre")); // Cambia "nombre" si es necesario
            proveedor.setRut(rs.getString("ruc"));
            lista.add(proveedor);
        }
    } catch (SQLException ex) {
        Logger.getLogger(ProveedorCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return lista;
}

    @Override
    public List<Proveedor> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Proveedor> lista = new ArrayList<>();
        try {
            String sql = "select * from proveedores where ruc ||' '|| nombre ilike ? AND nombre != 'SELECCIONAR' order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setRut(rs.getString("ruc"));
                proveedor.setRazonSocial(rs.getString("nombre"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setCorreo(rs.getString("correo"));
                lista.add(proveedor);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProveedorCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
