/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import com.lp2.muebleria2025.modelo.Marca;
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
public class MarcaCrudImpl implements Crud<Marca>{
    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public MarcaCrudImpl() {
        Conexion conectar = new Conexion();
        conec =  conectar.conectarBD();
    }
    
    
    

    @Override
    public void insertar(Marca m) {
        try {
            //Preparar sentencia
            String sql="insert into marca (nombre) values(?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Marca obj) {
        try {
            String sql="update marca set nombre=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setInt(2, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Marca obj) {
        try {
            //Prepara sentencia sql
            String sql="delete from marca where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1,obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MarcaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public List<Marca> listar(String textoBuscado) {
        System.out.println("texto buscado "+ textoBuscado);
        ArrayList<Marca> lista = new ArrayList<>();
        try {
            String sql="select * from marca where nombre ilike ? order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" +textoBuscado+"%");
            ResultSet rs = sentencia.executeQuery();
            
            //recorrer una lista
            while(rs.next()){
                Marca marca = new Marca();
                marca.setId(rs.getInt("id"));
                marca.setNombre(rs.getString("nombre"));
                lista.add(marca);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(MarcaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
