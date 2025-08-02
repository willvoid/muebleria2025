/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;


import com.lp2.muebleria2025.modelo.Compras;
import com.lp2.muebleria2025.modelo.Proveedor;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class ComprasCrudImpl implements Crud<Compras> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public ComprasCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Compras m) {
        try {
            //Preparar sentencia
            String sql = "insert into compras (fecha_compra,idproveedor,idusuario,total) values(?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaCompra = m.getFechaCompra(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaCompra.getTime()); // Convertir a java.sql.Date

            
            //Asginar valor a los parametros
            sentencia.setDate(1, fechaSQL);
            sentencia.setInt(2, m.getIdProveedor().getId());
            sentencia.setInt(3, m.getIdUsuario());
            sentencia.setInt(4, m.getTotal());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Compras obj) {
        try {
            String sql = "update compras set fecha_compra=?, idproveedor=?,idusuario=?, total=? where idcompras=?";
            sentencia = conec.prepareStatement(sql);
            
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaCompra = obj.getFechaCompra(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaCompra.getTime()); // Convertir a java.sql.Date

            
            sentencia.setDate(1, fechaSQL);
            sentencia.setInt(2, obj.getIdProveedor().getId());
            sentencia.setInt(3, obj.getIdUsuario());
            sentencia.setInt(4, obj.getTotal());
            sentencia.setInt(5, obj.getIdCompras());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void eliminar(Compras obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from compras where idcompras=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getIdCompras());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int obtenerValorActualSecuencia() {
    int numeroActual = 0;
    String sql = "SELECT last_value FROM compra_id_seq"; // Consulta para obtener el valor actual
    try {
        PreparedStatement ps = conec.prepareStatement(sql); 
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            numeroActual = rs.getInt(1); // Obtener el valor de la columna
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return numeroActual;
}
    
    @Override
    public ArrayList<Compras> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Compras> lista = new ArrayList<>();
        try {
//            String sql = "select * from producto where nombre ilike ? order by nombre asc";
String sql ="select v.*,c.nombre as cliente\n" +
"from compras v\n" +
"inner join proveedores c on v.idproveedor = c.id\n";     
sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Compras compras = new Compras();
                Proveedor proveedor  = new Proveedor();
                
                compras.setIdCompras(rs.getInt("idcompras"));
                compras.setFechaCompra(rs.getDate("fecha_compra"));
                compras.setMetodo_pago(rs.getString("metodo_de_pago"));
                compras.setTotal(rs.getInt("total"));
                
                //asignar valor a cliente
                proveedor.setId(rs.getInt("idproveedor"));
                proveedor.setRazonSocial(rs.getString("proveedores"));
                compras.setIdProveedor(proveedor);
                
                lista.add(compras);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
