/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;


import com.lp2.muebleria2025.modelo.DetalleCompras;
import com.lp2.muebleria2025.modelo.DetalleVentas;
import com.lp2.muebleria2025.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DetalleComprasCrudImpl implements Crud<DetalleCompras> {
    
  
     
    

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public DetalleComprasCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(DetalleCompras m) {
        try {
            //Preparar sentencia
            String sql = "insert into detalle_compras (compra_id,producto_id,cantidad,precio_compra,subtotal,descripcion) values(?,?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setInt(1, m.getIdcompra());
            sentencia.setInt(2, m.getIdproducto());
            sentencia.setInt(3, m.getCantidad());
            sentencia.setInt(4, m.getPrecio());
            sentencia.setInt(5, m.getSubtotal());
            sentencia.setString(6,m.getProducto());
            
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(DetalleCompras obj) {
        try {
            String sql = "update detalle_compras set producto_id=?,cantidad=?,precio_compra=?,subtotal=?,descripcion=? where idcompra_d=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, obj.getIdproducto());
            sentencia.setInt(2, obj.getCantidad());
            sentencia.setInt(3, obj.getPrecio());
            sentencia.setInt(4, obj.getSubtotal());
            sentencia.setString(5,obj.getProducto());
            sentencia.setInt(6, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(DetalleCompras obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from detalle_compras where idcompra_d=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

 public DetalleCompras buscarPorIdProducto(int idProducto) {
    DetalleCompras detalleCompras = null;
    String sql = "SELECT * FROM producto WHERE id = ?";
    
    try {
        PreparedStatement ps = conec.prepareStatement(sql); 
        ps.setInt(1, idProducto);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            detalleCompras = new DetalleCompras();
            detalleCompras.setId(rs.getInt("id"));
            detalleCompras.setProducto(rs.getString("nombre"));
            detalleCompras.setPrecio(rs.getInt("precio_compra"));
            detalleCompras.setCantidad(rs.getInt("cantidad"));
            // Asume otros campos según tu modelo y tabla
        }
    } catch (Exception e) {
        System.out.println("Error buscando producto por ID: " + e.getMessage());
    }
    return detalleCompras;
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
 


public int obtenerTotal(DetalleCompras obj) {
    int total = 0;
    String sql = "SELECT SUM(subtotal) FROM detalle_compras WHERE compra_id = ?";
    try (PreparedStatement sentencia = conec.prepareStatement(sql)) {
        sentencia.setInt(1, obj.getIdcompra()); // Configurar el parámetro de la consulta
        try (ResultSet rs = sentencia.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1); // Obtener el valor de la primera columna
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DetalleComprasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return total;
}

public DetalleCompras buscarCodProducto(String cod) {
           DetalleCompras detalleCompras = null;
           String sql = "SELECT * FROM producto WHERE id = ?";
           Producto producto = null;

           try {
               PreparedStatement ps = conec.prepareStatement(sql); 
               ps.setString(1, cod);
               ResultSet rs = ps.executeQuery();

               if (rs.next()) {
                   detalleCompras = new DetalleCompras();
                   detalleCompras.setIdproducto(rs.getInt("id"));
                   detalleCompras.setProducto(rs.getString("nombre"));
                   detalleCompras.setPrecio(rs.getInt("precio"));
                   detalleCompras.setCantidad(rs.getInt("cantidad"));
                   // Asume otros campos según tu modelo y tabla
               }
           } catch (SQLException e) {
               System.out.println("Error buscando producto por ID: " + e.getMessage());
           }
           return detalleCompras;
       } 
       
        public DetalleCompras buscarPorNameProducto(String nombre) {
           DetalleCompras detalleCompras = null;
           String sql = "SELECT * FROM producto WHERE nombre = ?";

           try {
               PreparedStatement ps = conec.prepareStatement(sql); 
               ps.setString(1, nombre);
               ResultSet rs = ps.executeQuery();

               if (rs.next()) {
                   detalleCompras = new DetalleCompras();
                   detalleCompras.setIdproducto(rs.getInt("id"));
                   detalleCompras.setProducto(rs.getString("nombre"));
                   detalleCompras.setPrecio(rs.getInt("precio"));
                   detalleCompras.setCantidad(rs.getInt("cantidad"));
                   detalleCompras.setIdproducto(rs.getInt("id"));
                   // Asume otros campos según tu modelo y tabla
               }
           } catch (SQLException e) {
               System.out.println("Error buscando producto por Name: " + e.getMessage());
           }
           return detalleCompras;
       }

        public List<DetalleVentas> listardescripcion(String textoBuscado) {
    System.out.println("Texto buscado: " + textoBuscado);
    List<DetalleVentas> lista = new ArrayList<>();
    
    if (textoBuscado != null && !textoBuscado.trim().isEmpty()) {
        try {
            String sql = "SELECT descripcion " +
                         "FROM detalle_compras " +
                         "WHERE compra_id = ?";
            try (PreparedStatement preparedStatement = conec.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.parseInt(textoBuscado));
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Producto producto = new Producto();
                        DetalleVentas detalleVentas = new DetalleVentas();
                        detalleVentas.setProducto(rs.getString("descripcion"));
                        
                        //detalleVentas.setInteres(rs.getInt("interes"));
                        lista.add(detalleVentas);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: el texto buscado no es un número válido: " + e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, "Error al listar detalles de ventas", ex);
        }
    }
    return lista;
}

    
 @Override
public List<DetalleCompras> listar(String textoBuscado) {
    System.out.println("Texto buscado: " + textoBuscado);
    List<DetalleCompras> lista = new ArrayList<>();
    
    if (textoBuscado != null && !textoBuscado.trim().isEmpty()) {
        try {
            String sql = "SELECT idcompra_d, producto_id, descripcion, cantidad, precio_compra, subtotal, compra_id " +
                         "FROM detalle_compras " +
                         "WHERE compra_id = ?";
            try (PreparedStatement preparedStatement = conec.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.parseInt(textoBuscado));
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        DetalleCompras detalleCompras = new DetalleCompras();
                        detalleCompras.setId(rs.getInt("idcompra_d"));
                        detalleCompras.setIdproducto(rs.getInt("producto_id"));
                        detalleCompras.setProducto(rs.getString("descripcion"));
                        detalleCompras.setCantidad(rs.getInt("cantidad"));
                        detalleCompras.setPrecio(rs.getInt("precio_compra"));
                        detalleCompras.setSubtotal(rs.getInt("subtotal"));
                        detalleCompras.setIdcompra(rs.getInt("compra_id"));
                        lista.add(detalleCompras);
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: el texto buscado no es un número válido: " + e.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DetalleComprasCrudImpl.class.getName()).log(Level.SEVERE, "Error al listar detalles de ventas", ex);
        }
    }
    return lista;
}


}
