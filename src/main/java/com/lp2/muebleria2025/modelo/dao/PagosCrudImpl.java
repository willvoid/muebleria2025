/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;


import com.lp2.muebleria2025.modelo.Cliente;
import com.lp2.muebleria2025.modelo.Pagos;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class PagosCrudImpl implements Crud<Pagos> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public PagosCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Pagos m) {
        try {
            //Preparar sentencia
            String sql = "insert into pagos (idcliente,idventa,montocuota,pagado,total,vencimiento,producto,total_cuotas) values(?,?,?,?,?,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            
            // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaVenta = m.getVencimiento(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date

            
            //Asginar valor a los parametros
            sentencia.setInt(1, m.getIdcliente().getId());
            sentencia.setInt(2, m.getIdventa());
            sentencia.setInt(3, m.getMontocuota());
            sentencia.setInt(4, m.getPagado());
            sentencia.setInt(5, m.getTotal());
            sentencia.setDate(6, fechaSQL);
            sentencia.setString(7, m.getProducto());
            sentencia.setInt(8, m.getTotal_cuotas());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Pagos obj) {
        try {
            String sql = "update pagos set idcliente=?, montocuota=?, pagado=?, total=?, vencimiento=?, producto=? where idventa=?";
            sentencia = conec.prepareStatement(sql);
            
           // Convertir java.util.Date a java.sql.Date
            java.util.Date fechaVenta = obj.getVencimiento(); // Obtener fecha como java.util.Date
            java.sql.Date fechaSQL = new java.sql.Date(fechaVenta.getTime()); // Convertir a java.sql.Date
            

            
            //Asginar valor a los parametros
            sentencia.setInt(1, obj.getIdcliente().getId());
            sentencia.setInt(2, obj.getMontocuota());
            sentencia.setInt(3, obj.getPagado());
            sentencia.setInt(4, obj.getTotal());
            sentencia.setDate(5, fechaSQL);
            sentencia.setString(6, obj.getProducto());
            sentencia.setInt(7, obj.getIdventa());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void eliminar(Pagos obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from pagos where idpagos=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getIdpagos());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizar_estados(){
        try{
            String sql = "UPDATE pagos\n" +
"                                SET estado = CASE\n" +
"				 WHEN estado = 'PAGADO' THEN estado -- Si ya está pagado no actualiza\n" +
"                                WHEN pagado = total THEN 'PAGADO'\n" +
"				 WHEN vencimiento < CURRENT_DATE THEN 'ATRASADO'\n" +
"				 WHEN vencimiento = CURRENT_DATE THEN 'COBRAR HOY'\n" +
"                                WHEN vencimiento > CURRENT_DATE THEN 'AL DIA'\n" +
"				 ELSE estado -- Si no cumple ninguna condicion no actualiza\n" +
"				 END\n";
            sentencia = conec.prepareStatement(sql);
            sentencia.executeUpdate();
        }catch (SQLException ex){
            Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int obtenerValorActualSecuenciaPagos() {
        int numeroActual = 0;
        String sql = "SELECT last_value FROM pago_id_seq"; // Consulta para obtener el valor actual
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
    
    public int obtenerPagado(Pagos obj) {
    int pagado = 0;
    String sql = "SELECT pagado FROM pagos WHERE idpagos = ?";
    try (PreparedStatement sentencia = conec.prepareStatement(sql)) {
        sentencia.setInt(1, obj.getIdpagos()); // Configurar el parámetro de la consulta
        try (ResultSet rs = sentencia.executeQuery()) {
            if (rs.next()) {
                pagado = rs.getInt(1); // Obtener el valor de la primera columna
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(DetalleVentasCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    return pagado;
}


public void cobrar(Pagos obj) {
    try {
        String sql = "UPDATE pagos SET pagado = ?, vencimiento = ?, cuotas_pagadas=? WHERE idpagos = ?";
        sentencia = conec.prepareStatement(sql);
        //actualizar cuotas pagadas
        Integer actCuotasPagadas = obj.getCuotas_pagadas() + 1;

        // Obtener fecha actual de vencimiento
        java.util.Date vencimientoActual = obj.getVencimiento();

        // Convertir a LocalDate
        LocalDate fechaActual = vencimientoActual.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Sumar 1 mes
        LocalDate nuevaFecha = fechaActual.plusMonths(1);

        // Convertir de nuevo a java.sql.Date
        java.sql.Date fechaSQL = java.sql.Date.valueOf(nuevaFecha);

        // Asignar valores
        sentencia.setInt(1, obj.getPagado());
        sentencia.setDate(2, fechaSQL);
        sentencia.setInt(3, actCuotasPagadas);
        sentencia.setInt(4, obj.getIdpagos());
        

        // Ejecutar
        sentencia.executeUpdate();
        actualizar_estados();
    } catch (SQLException ex) {
        Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    
    @Override
    public ArrayList<Pagos> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Pagos> lista = new ArrayList<>();
        try {
//            String sql = "select * from producto where nombre ilike ? order by nombre asc";
String sql ="select pa.producto,idpagos, cl.nombre, cl.telefono, cl.ruc, pa.idcliente, pa.idventa, montocuota, pagado, pa.total, vencimiento, saldo, estado, pa.total_cuotas, pa.cuotas_pagadas from pagos pa\n" +
            "inner join cliente cl\n" +
            "on pa.idcliente = cl.id\n" +
            "where cl.ruc ||' '|| cl.nombre ilike ? or pa.producto ilike ? order by idpagos desc";     
sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            sentencia.setString(2, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Cliente cliente = new Cliente();
                Pagos pagos = new Pagos();
                
                pagos.setIdpagos(rs.getInt("idpagos"));
                pagos.setIdventa(rs.getInt("idventa"));
                pagos.setMontocuota(rs.getInt("montocuota"));
                pagos.setPagado(rs.getInt("pagado"));
                pagos.setTotal(rs.getInt("total"));
                pagos.setVencimiento(rs.getDate("vencimiento"));
                pagos.setEstado(rs.getString("estado"));
                pagos.setSaldo(rs.getInt("saldo"));
                pagos.setProducto(rs.getString("producto"));
                pagos.setTotal_cuotas(rs.getInt("total_cuotas"));
                pagos.setCuotas_pagadas(rs.getInt("cuotas_pagadas"));
                //pagos.setEstado(rs.getString("estado"));
                
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setRazonSocial(rs.getString("nombre"));
                cliente.setRut(rs.getString("ruc"));
                pagos.setIdcliente(cliente);
                
                lista.add(pagos);
            }

        } catch (SQLException ex) {
            Logger.getLogger(PagosCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
