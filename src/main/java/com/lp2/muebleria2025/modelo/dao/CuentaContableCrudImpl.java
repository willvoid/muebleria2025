/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import com.lp2.muebleria2025.modelo.CuentaContable;
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
public class CuentaContableCrudImpl implements Crud<CuentaContable>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public CuentaContableCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(CuentaContable m) {
        try {
            //Preparar sentencia
            String sql="insert into cuentas_contables (nombre,tipo) values(?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            sentencia.setString(2, m.getTipo());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(CuentaContable obj) {
        try {
            String sql="update cuentas_contables set nombre=?, tipo=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getTipo());
            sentencia.setInt(3, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(CuentaContable obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from cuentas_contables where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public List<CuentaContable> listar(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<CuentaContable> lista = new ArrayList<>();
        try {
            String sql="select * from cuentas_contables where nombre ilike ? order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                CuentaContable cuentas_contables = new CuentaContable();
                cuentas_contables.setId(rs.getInt("id"));
                cuentas_contables.setNombre(rs.getString("nombre"));
                cuentas_contables.setTipo(rs.getString("tipo"));
                lista.add(cuentas_contables);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CuentaContableCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
