/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

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

/**
 *
 * @author cmendieta
 */
public class DetalleVentasCrudImpl implements Crud<DetalleVentas> {
    
  
     
    

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
        public DetalleVentasCrudImpl() {
            Conexion conectar = new Conexion();
            conec = conectar.conectarBD();
        }

        @Override
        public void insertar(DetalleVentas m) {
            try {
                //Preparar sentencia
                String sql = "insert into detalle_ventas (venta_id,producto_cod,cantidad,precio_unitario,subtotal,descripcion,idproducto,precio_descuento) values(?,?,?,?,?,?,?,?)";
                sentencia = conec.prepareStatement(sql);
                //Asginar valor a los parametros
                sentencia.setInt(1, m.getVenta());
                sentencia.setString(2, m.getCodigo());
                sentencia.setInt(3, m.getCantidad());
                sentencia.setInt(4, m.getPrecio());
                sentencia.setInt(5, m.getSubtotal());
                sentencia.setString(6,m.getProducto());
                sentencia.setInt(7,m.getIdproducto());
                sentencia.setInt(8,m.getPrecio_descuento());
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    public void insertarcuotas(DetalleVentas m) {
        try {
            //Preparar sentencia
            String sql = "insert into detalle_ventas (venta_id,idproducto,cantidad,precio_unitario,subtotal,descripcion,montocuotas,nro_cuotas,montopagado) values(?,?,?,?,?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setInt(1, m.getVenta());
            sentencia.setInt(2, m.getIdproducto());
            sentencia.setInt(3, m.getCantidad());
            sentencia.setInt(4, m.getPrecio());
            sentencia.setInt(5, m.getSubtotal());
            sentencia.setString(6,m.getProducto());
            sentencia.setInt(7,m.getMontoCuotas());
            sentencia.setInt(8,m.getNro_cuotas());
            sentencia.setInt(9,m.getMontoPagado());
            //sentencia.setInt(10,m.getInteres());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        @Override
        public void actualizar(DetalleVentas obj) {
            try {
                String sql = "update detalle_ventas set idproducto=?,cantidad=?,precio_unitario=?,subtotal=?,descripcion=?,precio_descuento=? where id_detalle=?";
                sentencia = conec.prepareStatement(sql);
                sentencia.setInt(1, obj.getIdproducto());
                sentencia.setInt(2, obj.getCantidad());
                sentencia.setInt(3, obj.getPrecio());
                sentencia.setInt(4, obj.getSubtotal());
                sentencia.setString(5,obj.getProducto());
                sentencia.setInt(6, obj.getPrecio_descuento());
                sentencia.setInt(7, obj.getId());
                //sentencia.setInt(10,obj.getInteres());
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void actualizar_cuotass(DetalleVentas obj) {
            try {
                String sql = "update detalle_ventas set idproducto=?,cantidad=?,precio_unitario=?,subtotal=?,descripcion=? where id_detalle=?";
                sentencia = conec.prepareStatement(sql);
                sentencia.setInt(1, obj.getIdproducto()); 
                sentencia.setInt(2, obj.getCantidad());
                sentencia.setInt(3, obj.getPrecio());
                sentencia.setInt(4, obj.getSubtotal());
                sentencia.setString(5,obj.getProducto());
                //sentencia.setInt(6,obj.getMontoCuotas());
                //sentencia.setInt(7,obj.getNro_cuotas());
                //sentencia.setInt(8,obj.getMontoPagado());
                sentencia.setInt(6, obj.getId());
                //sentencia.setInt(10,obj.getInteres());
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
        public void actualizarcuota(int montoC, int nroC, int montoP, int idventa) {
            try {
                String sql = "update detalle_ventas set montocuotas=?,nro_cuotas=?,montopagado=? where venta_id=?";
                PreparedStatement sentencia = conec.prepareStatement(sql);
                sentencia.setInt(1, montoC);
                sentencia.setInt(2, nroC);
                sentencia.setInt(3, montoP);
                sentencia.setInt(4, idventa);
                //sentencia.setInt(10,obj.getInteres());
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void actualizarmontopagado(int montoP, int idventa) {
            try {
                String sql = "update detalle_ventas set montopagado=? where venta_id=?";
                PreparedStatement sentencia = conec.prepareStatement(sql);
                sentencia.setInt(1, montoP);
                sentencia.setInt(2, idventa);
                System.out.println("Monto Pagado actualizado");
                //sentencia.setInt(10,obj.getInteres());
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void eliminar(DetalleVentas obj) {
            try {
                //Prepara sentencia sql
                String sql = "delete from detalle_ventas where id_detalle=?";
                sentencia = conec.prepareStatement(sql);
                // enviar valores de los parametros
                sentencia.setInt(1, obj.getId());
                // ejecutar sentencia
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void eliminartodo(Integer idventa) {
            try {
                //Prepara sentencia sql
                String sql = "delete from detalle_ventas where venta_id=?";
                sentencia = conec.prepareStatement(sql);
                // enviar valores de los parametros
                sentencia.setInt(1,idventa);
                // ejecutar sentencia
                sentencia.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public DetalleVentas buscarPorIdProducto(int id) {
           DetalleVentas detalleVentas = null;
           String sql = "SELECT * FROM producto WHERE id = ?";
           Producto producto = null;

           try {
               PreparedStatement ps = conec.prepareStatement(sql); 
               ps.setInt(1, id);
               ResultSet rs = ps.executeQuery();

               if (rs.next()) {
                   detalleVentas = new DetalleVentas();
                   detalleVentas.setIdproducto(rs.getInt("id"));
                   detalleVentas.setProducto(rs.getString("nombre"));
                   detalleVentas.setPrecio(rs.getInt("precio"));
                   detalleVentas.setCantidad(rs.getInt("cantidad"));
                   detalleVentas.setPrecio_descuento(rs.getInt("precio_descuento"));
                   detalleVentas.setNro_cuotas(rs.getInt("nro_cuotas"));
                   detalleVentas.setMontoCuotas(rs.getInt("monto_cuotas"));
                   // Asume otros campos según tu modelo y tabla
               }
           } catch (Exception e) {
               System.out.println("Error buscando producto por ID: " + e.getMessage());
           }
           return detalleVentas;
       }
       
       public DetalleVentas buscarCodProducto(String cod) {
           DetalleVentas detalleVentas = null;
           String sql = "SELECT * FROM producto WHERE id = ?";
           Producto producto = null;

           try {
               PreparedStatement ps = conec.prepareStatement(sql); 
               ps.setString(1, cod);
               ResultSet rs = ps.executeQuery();

               if (rs.next()) {
                   detalleVentas = new DetalleVentas();
                   detalleVentas.setIdproducto(rs.getInt("id"));
                   detalleVentas.setProducto(rs.getString("nombre"));
                   detalleVentas.setPrecio(rs.getInt("precio"));
                   detalleVentas.setCantidad(rs.getInt("cantidad"));
                   detalleVentas.setPrecio_descuento(rs.getInt("precio_descuento"));
                   // Asume otros campos según tu modelo y tabla
               }
           } catch (SQLException e) {
               System.out.println("Error buscando producto por ID: " + e.getMessage());
           }
           return detalleVentas;
       } 
       
        public DetalleVentas buscarPorNameProducto(String nombre) {
           DetalleVentas detalleVentas = null;
           String sql = "SELECT * FROM producto WHERE nombre = ?";

           try {
               PreparedStatement ps = conec.prepareStatement(sql); 
               ps.setString(1, nombre);
               ResultSet rs = ps.executeQuery();

               if (rs.next()) {
                   detalleVentas = new DetalleVentas();
                   detalleVentas.setIdproducto(rs.getInt("id"));
                   detalleVentas.setProducto(rs.getString("nombre"));
                   detalleVentas.setPrecio(rs.getInt("precio"));
                   detalleVentas.setCantidad(rs.getInt("cantidad"));
                   detalleVentas.setCodigo(rs.getString("id"));
                   // Asume otros campos según tu modelo y tabla
               }
           } catch (SQLException e) {
               System.out.println("Error buscando producto por Name: " + e.getMessage());
           }
           return detalleVentas;
       }
        
        
        
    
        public void actualizarStock(int nombre, int nuevaCantidad) {
           try {
               String sql = "UPDATE producto SET cantidad = ? WHERE id = ?";
               PreparedStatement sentencia = conec.prepareStatement(sql);
               sentencia.setInt(1, nuevaCantidad);
               sentencia.setInt(2, nombre);
               sentencia.executeUpdate();
           } catch (SQLException ex) {
               Logger.getLogger(ProductoCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
 
        public void SumarStock(int idProducto, int nuevaCantidad) {
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
                ex.printStackTrace();
            }
            return numeroActual;
        }



        public int obtenerTotal(DetalleVentas obj) {
            int total = 0;
            String sql = "SELECT SUM(subtotal) FROM detalle_ventas WHERE venta_id = ?";
            try (PreparedStatement sentencia = conec.prepareStatement(sql)) {
                sentencia.setInt(1, obj.getVenta()); // Configurar el parámetro de la consulta
                try (ResultSet rs = sentencia.executeQuery()) {
                    if (rs.next()) {
                        total = rs.getInt(1); // Obtener el valor de la primera columna
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            return total;
        }


    public int CalcularIvaTotal(String idventa) {
        double totalIva = 0;
        String sql = "SELECT SUM(" +
"			(dv.precio_unitario * dv.cantidad) * (i.valor::numeric/100)/(1+(i.valor::numeric/100))" +
"		  ) AS total_iva " +
"                        FROM detalle_ventas dv " +
"                        LEFT JOIN producto p ON dv.idproducto = p.id " +
"                        LEFT JOIN iva i ON p.idiva = i.id " +
"                        WHERE dv.venta_id = ?";

        try (PreparedStatement smt = conec.prepareStatement(sql)) {
            smt.setInt(1, Integer.parseInt(idventa));
            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    totalIva = rs.getDouble("total_iva");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        // Redondear y convertir a entero
        return (int) Math.round(totalIva);
    }

    public int DescuentoTotal(String idventa) {
        double totalDescuento = 0;
        
        String sql = "SELECT " +
"    COALESCE(SUM(" +
"        CASE " +
"            WHEN CURRENT_DATE BETWEEN d.desde AND d.hasta THEN " +
"                (dv.precio_unitario * dv.cantidad) * (d.descuento::numeric / 100) " +
"            ELSE 0 " +
"        END" +
"    ), 0) AS total_descuentos " +
"FROM detalle_ventas dv " +
"LEFT JOIN producto p ON dv.idproducto = p.id " +
"LEFT JOIN descuentos d ON d.idproducto = p.id " +
"WHERE dv.venta_id = ?;";
        
        try (PreparedStatement smt = conec.prepareStatement(sql)) {
            smt.setInt(1, Integer.parseInt(idventa));
            try (ResultSet rs = smt.executeQuery()) {
                if (rs.next()) {
                    totalDescuento= rs.getDouble("total_descuentos");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return (int) Math.round(totalDescuento);
    }

public List<DetalleVentas> listardescripcion(String textoBuscado) {
    System.out.println("Texto buscado: " + textoBuscado);
    List<DetalleVentas> lista = new ArrayList<>();
    
    if (textoBuscado != null && !textoBuscado.trim().isEmpty()) {
        try {
            String sql = "SELECT descripcion " +
                         "FROM detalle_ventas " +
                         "WHERE venta_id = ?";
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
public List<DetalleVentas> listar(String textoBuscado) {
    System.out.println("Texto buscado: " + textoBuscado);
    List<DetalleVentas> lista = new ArrayList<>();
    
    if (textoBuscado != null && !textoBuscado.trim().isEmpty()) {
        try {
            String sql = "SELECT id_detalle, idproducto, descripcion, cantidad, precio_unitario, subtotal, venta_id, montocuotas,nro_cuotas,montopagado,precio_descuento " +
                         "FROM detalle_ventas " +
                         "WHERE venta_id = ?";
            try (PreparedStatement preparedStatement = conec.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.parseInt(textoBuscado));
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Producto producto = new Producto();
                        DetalleVentas detalleVentas = new DetalleVentas();
                        detalleVentas.setId(rs.getInt("id_detalle"));
                        detalleVentas.setIdproducto(rs.getInt("idproducto"));
                        detalleVentas.setProducto(rs.getString("descripcion"));
                        detalleVentas.setCantidad(rs.getInt("cantidad"));
                        detalleVentas.setPrecio(rs.getInt("precio_unitario"));
                        detalleVentas.setSubtotal(rs.getInt("subtotal"));
                        detalleVentas.setVenta(rs.getInt("venta_id"));
                        detalleVentas.setMontoCuotas(rs.getInt("montocuotas"));
                        detalleVentas.setNro_cuotas(rs.getInt("nro_cuotas"));
                        detalleVentas.setMontoPagado(rs.getInt("montopagado"));
                        detalleVentas.setPrecio_descuento(rs.getInt("precio_descuento"));
                        
                        
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


}
