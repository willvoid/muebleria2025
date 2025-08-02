/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import com.lp2.muebleria2025.modelo.Transaccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cmendieta
 */
public class TransaccionCrudImpl implements Crud<Transaccion>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public TransaccionCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(Transaccion m) {
        try {
            //Preparar sentencia
            String sql="insert into transaccion (monto, id_pago, fecha, tipo, forma_pago, concepto) values(?,?,CURRENT_DATE,?,?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setInt(1, m.getMonto());
            sentencia.setInt(2, m.getIdpago());
            sentencia.setString(3, m.getTipo());
            sentencia.setString(4, m.getForma_pago());
            sentencia.setString(5, m.getConcepto());
            //Ejecutar sentencia
            sentencia.executeUpdate();
            System.out.println("Transaccion insertada correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Transaccion obj) {
        try {
            String sql="update transaccion set monto=?, fecha=CURRENT_DATE where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setInt(1, obj.getMonto());
            sentencia.setInt(2, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Transaccion obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from transaccion where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    public Integer totalVentas (Date desde, Date hasta){
        Integer totalventas = 0;
        try {
            String sql="select SUM(monto) as totalventas from transaccion where tipo='Ingreso' and fecha<=? and fecha>=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setDate(1, new java.sql.Date(hasta.getTime())); // primero el máximo
            sentencia.setDate(2, new java.sql.Date(desde.getTime())); // luego el mínimo

            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                totalventas = rs.getInt("totalventas");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalventas;      
    }
    
    public Integer totalGastos (Date desde, Date hasta){
        Integer totalventas = 0;
        try {
            String sql="select SUM(monto) as totalventas from transaccion where tipo='Egreso' and fecha<=? and fecha>=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setDate(1, new java.sql.Date(hasta.getTime())); // primero el máximo
            sentencia.setDate(2, new java.sql.Date(desde.getTime())); // luego el mínimo

            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                totalventas = rs.getInt("totalventas");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalventas;      
    }
    
    public List<Transaccion> listarEgresos(String textoBuscado, Date desde, Date hasta) {
        //"ID", "MONTO", "FECHA", "CONCEPTO", "METODO DE PAGO" "IDPago"
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<Transaccion> lista = new ArrayList<>();
        try {
            String sql="select * from transaccion where tipo='Egreso' and concepto ilike ? and fecha<=? and fecha>=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            sentencia.setDate(2, new java.sql.Date(hasta.getTime())); // primero el máximo
            sentencia.setDate(3, new java.sql.Date(desde.getTime())); // luego el mínimo

            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                Transaccion transaccion = new Transaccion();
                transaccion.setId(rs.getInt("id"));
                transaccion.setMonto(rs.getInt("monto"));
                transaccion.setFecha(rs.getDate("fecha"));
                transaccion.setConcepto(rs.getString("concepto"));
                transaccion.setForma_pago(rs.getString("forma_pago"));
                transaccion.setIdpago(rs.getInt("id_pago"));
                //transaccion.setNombre(rs.getString("nombre"));
                lista.add(transaccion);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public List<Transaccion> listarIngresos(String textoBuscado, Date desde, Date hasta) {
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<Transaccion> lista = new ArrayList<>();
         try {
            String sql="select * from transaccion where tipo='Ingreso' and concepto ilike ? and fecha<=? and fecha>=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            sentencia.setDate(2, new java.sql.Date(hasta.getTime())); // primero el máximo
            sentencia.setDate(3, new java.sql.Date(desde.getTime())); // luego el mínimo

            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                Transaccion transaccion = new Transaccion();
                transaccion.setId(rs.getInt("id"));
                transaccion.setMonto(rs.getInt("monto"));
                transaccion.setFecha(rs.getDate("fecha"));
                transaccion.setConcepto(rs.getString("concepto"));
                transaccion.setForma_pago(rs.getString("forma_pago"));
                transaccion.setIdpago(rs.getInt("id_pago"));
                //transaccion.setNombre(rs.getString("nombre"));
                lista.add(transaccion);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TransaccionCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    @Override
    public List<Transaccion> listar(String t) {
        ArrayList<Transaccion> lista = new ArrayList<>();
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return lista;
    }
    
}
