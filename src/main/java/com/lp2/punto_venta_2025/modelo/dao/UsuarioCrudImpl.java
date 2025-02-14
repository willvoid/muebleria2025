/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo.dao;

import com.lp2.punto_venta_2025.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP
 */
public class UsuarioCrudImpl implements Crud<Usuario>{

    Connection conec;
    PreparedStatement sentencia;
    //lo primero que se ejecuta cuando se crea un objeto
    public UsuarioCrudImpl() {
        Conexion conectar = new Conexion();
        conec = conectar.conectarBD();
    }
    
    @Override
    public void insertar(Usuario obj) {
        try {
            String sql = "INSERT INTO usuario (nombre,apellido,usuario,clave,rol,estado) VALUES (?,?,?,?,?,'ACTIVO')";
            sentencia = conec.prepareStatement(sql);
            //Dar valor a los parametros "?"
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getApellido());
            sentencia.setString(3, obj.getUsuario());
            sentencia.setString(4, obj.getClave());
            sentencia.setString(5, obj.getRol());
            
            sentencia.executeUpdate();//Ejecutar Sentencia
        } catch (SQLException e) {
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    @Override
    public void actualizar(Usuario obj) {
       try {
            String sql = "UPDATE usuario SET nombre=?,apellido=?,usuario=?,clave=?,rol=? WHERE id=?";
            sentencia = conec.prepareStatement(sql);
            //Dar valor a los parametros "?"
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getApellido());
            sentencia.setString(3, obj.getUsuario());
            sentencia.setString(4, obj.getClave());
            sentencia.setString(5, obj.getRol());
            sentencia.setInt(6, obj.getId());
            
            sentencia.executeUpdate();//Ejecutar Sentencia
        } catch (SQLException e) {
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        } 
    }

    @Override
    public void eliminar(Usuario obj) {
        try {
            String sql = "UPDATE usuario SET estado='INACTIVO' WHERE id=?";
            sentencia = conec.prepareStatement(sql);
            //Dar valor a los parametros "?"
            sentencia.setInt(1, obj.getId());
            
            sentencia.executeUpdate();//Ejecutar Sentencia
        } catch (SQLException e) {
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        } 
    }
     
    public boolean loginUser(Usuario obj){
        boolean respuesta = false;
        try {
            String sql = "SELECT usuario from usuario where usuario = ? and clave = ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getUsuario());
            sentencia.setString(2, obj.getClave());
            ResultSet rs = sentencia.executeQuery();
            //recorrer una lista
            while (rs.next()){ 
                respuesta = true;
            }
            
        } catch (SQLException e) {
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }
    
    @Override
    public List<Usuario> listar(String t) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
