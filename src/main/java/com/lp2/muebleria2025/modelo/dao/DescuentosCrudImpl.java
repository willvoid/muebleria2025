/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;


import com.lp2.muebleria2025.modelo.Descuentos;
import com.lp2.muebleria2025.modelo.Producto;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class DescuentosCrudImpl implements Crud<Descuentos> {

    /**
     *
     * @param idDescuentos
     * @param nuevoStock
     */

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public DescuentosCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Descuentos obj) {
        try {
            //Preparar sentencia
            String sql = "insert into descuentos (nombre,desde,hasta,descuento,idproducto) values(?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setDate(2, SqlDateConverter(obj, 1));
            sentencia.setDate(3, SqlDateConverter(obj, 2));
            sentencia.setDouble(4, obj.getDescuento());
            sentencia.setInt(5, obj.getProducto().getId());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DescuentosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Descuentos obj) {
        try {
            String sql = "update descuentos set nombre=?, desde=?, hasta=?, descuento=?, idproducto=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setDate(2, SqlDateConverter(obj, 1));
            sentencia.setDate(3, SqlDateConverter(obj, 2));
            sentencia.setDouble(4, obj.getDescuento());
            sentencia.setInt(5, obj.getProducto().getId());
            sentencia.setInt(6, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DescuentosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Descuentos obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from descuentos where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DescuentosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Convertir java.util.Date a java.sql.Date
    private Date SqlDateConverter(Descuentos m, Integer i) {
        if (i==1) {
          java.util.Date fechaVenta = m.getDesde(); // Obtener fecha como java.util.Date
          java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date
          return fechaSQL;
        }else{
          java.util.Date fechaVenta = m.getHasta(); // Obtener fecha como java.util.Date
          java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date
          return fechaSQL;
        }
    }
    

    @Override
    public List<Descuentos> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Descuentos> lista = new ArrayList<>();
        try {
//              String sql = "select * from producto where nombre ilike ? order by nombre asc";
                String sql ="""
                            select d.*,p.nombre as producto
                            from descuentos d 
                            inner join producto p on p.id = d.idproducto
                            where d.desde ||' '|| p.nombre ilike ? order by d.hasta desc""";      
sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Descuentos descuento = new Descuentos();
                Producto producto = new Producto();
                
                descuento.setId(rs.getInt("id"));
                descuento.setNombre(rs.getString("nombre"));
                descuento.setDesde(rs.getDate("desde"));
                descuento.setHasta(rs.getDate("hasta"));
                descuento.setDescuento(rs.getDouble("descuento"));
                
                //Asignar valor a producto
                producto.setId(rs.getInt("idproducto"));
                producto.setNombre(rs.getString("producto"));
                descuento.setProducto(producto);
                
                
                lista.add(descuento);
                
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(DescuentosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
