/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.dao;


import com.lp2.punto_venta_2025.modelo.Iva;
import com.lp2.punto_venta_2025.modelo.Marca;
import com.lp2.punto_venta_2025.modelo.Producto;
import java.sql.Connection;
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
public class ProductoCrudImpl implements Crud<Producto> {

    /**
     *
     * @param idProducto
     * @param nuevoStock
     */

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public ProductoCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Producto m) {
        try {
            //Preparar sentencia
            String sql = "insert into producto (nombre,precio,idmarca,idiva,cantidad,precio_compra,codigo) values(?,?,?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            sentencia.setInt(2, m.getPrecio());
            sentencia.setInt(3, m.getMarca().getId());
            sentencia.setInt(4, m.getIva().getId());
            sentencia.setInt(5, m.getCantidad());
            sentencia.setInt(6, m.getPrecio_compra());
            sentencia.setString(7, m.getCodigo());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Producto obj) {
        try {
            String sql = "update producto set nombre=?, precio=?,idmarca=?,idiva=?, cantidad=?, precio_compra=?, codigo=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setInt(2, obj.getPrecio());
            sentencia.setInt(3, obj.getMarca().getId());
            sentencia.setInt(4, obj.getIva().getId());
            sentencia.setInt(5, obj.getCantidad());
            sentencia.setInt(6, obj.getPrecio_compra());
            sentencia.setString(7, obj.getCodigo());
            sentencia.setInt(8, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Producto obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from producto where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarStock(int idProducto, int nuevaCantidad) {
    try {
        String sql = "UPDATE producto SET cantidad = ? WHERE id = ?";
        PreparedStatement sentencia = conec.prepareStatement(sql);
        sentencia.setInt(1, nuevaCantidad);
        sentencia.setInt(2, idProducto);
        sentencia.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    public List<Producto> listarecopro(String textoBuscado) {
        List<Producto> lista = new ArrayList<>();
        try {
            // Consulta para obtener todos los clientes
            String sql = "SELECT nombre FROM producto WHERE nombre ilike ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();
            // Recorrer los resultados
            while (rs.next()) {
                Producto producto = new Producto();
                //producto.setId(rs.getInt("id")); // Cambia "id" por el nombre correcto de la columna
                producto.setNombre(rs.getString("nombre")); // Cambia "nombre" si es necesario
                lista.add(producto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public List<Producto> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Producto> lista = new ArrayList<>();
        try {
//              String sql = "select * from producto where nombre ilike ? order by nombre asc";
                String sql ="""
                            select p.*,i.nombre as iva,m.nombre as marca
                            from producto p
                             inner join iva i on p.idiva = i.id
                             inner join marca m on p.idmarca = m.id
                            where p.nombre ilike ? order by p.nombre asc"""  ;      
sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Producto producto = new Producto();
                Iva iva = new Iva();
                Marca marca  = new Marca();
                
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getInt("precio"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setPrecio_compra(rs.getInt("precio_compra"));
                producto.setCodigo(rs.getString("codigo"));
                
                //Asignar valor a iva
                iva.setId(rs.getInt("idiva"));
                iva.setNombre(rs.getString("iva"));
                producto.setIva(iva);
                
                //asignar valor a marca
                marca.setId(rs.getInt("idmarca"));
                marca.setNombre(rs.getString("marca"));
                producto.setMarca(marca);
                
                lista.add(producto);
                
                
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
