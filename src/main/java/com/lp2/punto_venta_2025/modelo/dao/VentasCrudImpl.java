/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.dao;


import com.lp2.punto_venta_2025.modelo.Cliente;
import com.lp2.punto_venta_2025.modelo.Ventas;
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
public class VentasCrudImpl implements Crud<Ventas> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public VentasCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Ventas m) {
        try {
            //Preparar sentencia
            String sql = "insert into ventas (fecha_venta,idcliente,idusuario,metodo_de_pago,total,estado) values(?,?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaVenta = m.getFechaVenta(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date

            
            //Asginar valor a los parametros
            sentencia.setDate(1, fechaSQL);
            sentencia.setInt(2, m.getIdCliente().getId());
            sentencia.setInt(3, m.getIdUsuario());
            sentencia.setString(4, "Efectivo");
            sentencia.setInt(5, m.getTotal());
            sentencia.setString(6, m.getEstado());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Ventas obj) {
        try {
            String sql = "update ventas set fecha_venta=?, idcliente=?,idusuario=?,metodo_de_pago=?, total=?, estado=? where idventas=?";
            sentencia = conec.prepareStatement(sql);
            
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaVenta = obj.getFechaVenta(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date

            
            sentencia.setDate(1, fechaSQL);
            sentencia.setInt(2, obj.getIdCliente().getId());
            sentencia.setInt(3, obj.getIdUsuario());
            sentencia.setString(4, obj.getMetodo_pago());
            sentencia.setInt(5, obj.getTotal());
            sentencia.setString(6, obj.getEstado());
            sentencia.setInt(7, obj.getIdVentas());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void eliminar(Ventas obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from ventas where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getIdVentas());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(VentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public int obtenerValorActualSecuencia() {
        int numeroActual = 0;
        String sql = "SELECT last_value FROM ventas_id_seq"; // Consulta para obtener el valor actual
        try {
            PreparedStatement ps = conec.prepareStatement(sql); 

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numeroActual = rs.getInt(1); // Obtener el valor de la columna
            }
        } catch (SQLException ex) {
            Logger.getLogger(VentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numeroActual;
    }

    @Override
    public ArrayList<Ventas> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Ventas> lista = new ArrayList<>();
        try {
//            String sql = "select * from producto where nombre ilike ? order by nombre asc";
String sql ="select v.*,c.nombre as cliente\n" +
"from ventas v\n" +
"inner join cliente c on v.idcliente = c.id\n";     
sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Ventas ventas = new Ventas();
                Cliente cliente  = new Cliente();
                
                ventas.setIdVentas(rs.getInt("idventas"));
                ventas.setFechaVenta(rs.getDate("fecha_venta"));
                ventas.setMetodo_pago(rs.getString("metodo_de_pago"));
                ventas.setTotal(rs.getInt("total"));
                ventas.setEstado(rs.getString("estado"));
                
                //asignar valor a cliente
                cliente.setId(rs.getInt("idcliente"));
                cliente.setRazonSocial(rs.getString("cliente"));
                ventas.setIdCliente(cliente);
                
                lista.add(ventas);
            }

        } catch (SQLException ex) {
            Logger.getLogger(VentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
