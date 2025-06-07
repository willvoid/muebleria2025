/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;


import com.lp2.muebleria2025.modelo.Iva;
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
public class IvaCrudImpl implements Crud<Iva> {

    Connection conec;
    PreparedStatement sentencia;

    //lo primero que se ejecuta cuando se crea un objeto
    public IvaCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }

    @Override
    public void insertar(Iva m) {
        try {
            //Preparar sentencia
            String sql = "insert into Iva (nombre,valor) values(?,?)";
            sentencia = conec.prepareStatement(sql);
            //Asginar valor a los parametros
            sentencia.setString(1, m.getNombre());
            sentencia.setInt(2, m.getValor());
            //Ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(IvaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actualizar(Iva obj) {
        try {
            String sql = "update Iva set nombre=?, valor=? where id=?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getNombre());
            sentencia.setInt(2, obj.getValor());
            sentencia.setInt(3, obj.getId());
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(IvaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar(Iva obj) {
        try {
            //Prepara sentencia sql
            String sql = "delete from Iva where id=?";
            sentencia = conec.prepareStatement(sql);
            // enviar valores de los parametros
            sentencia.setInt(1, obj.getId());
            // ejecutar sentencia
            sentencia.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(IvaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Iva> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Iva> lista = new ArrayList<>();
        try {
            String sql = "select * from Iva where nombre ilike ?  order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Iva Iva = new Iva();
                Iva.setId(rs.getInt("id"));
                Iva.setNombre(rs.getString("nombre"));
                Iva.setValor(rs.getInt("valor"));
                lista.add(Iva);
            }

        } catch (SQLException ex) {
            Logger.getLogger(IvaCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

}
