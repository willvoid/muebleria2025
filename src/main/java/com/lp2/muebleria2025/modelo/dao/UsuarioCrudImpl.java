/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.vista.GUILogin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class UsuarioCrudImpl implements Crud<Usuario>{

    Connection conec;
    PreparedStatement sentencia;
    public static Integer idusuario=null;
    public static String usuario_actual=null;
    public static String rol_usuario=null;
    public static String nombre=null;
    public static String apellido=null;
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
            String sql = "UPDATE usuario SET nombre=?,apellido=?,usuario=?,rol=? WHERE id=?";
            sentencia = conec.prepareStatement(sql);
            //Dar valor a los parametros "?"
            sentencia.setString(1, obj.getNombre());
            sentencia.setString(2, obj.getApellido());
            sentencia.setString(3, obj.getUsuario());
            //sentencia.setString(4, obj.getClave());
            sentencia.setString(4, obj.getRol());
            sentencia.setInt(5, obj.getId());
            
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
     
    public void ObtenerUser(Usuario obj){
        try {
            String sql = "SELECT id,nombre,apellido,usuario,rol from usuario where usuario = ? and clave = ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getUsuario());
            sentencia.setString(2, obj.getClave());
            ResultSet rs = sentencia.executeQuery();
            //recorrer una lista
            if (rs.next()){
                obj.setId(rs.getInt("id"));
                obj.setNombre(rs.getString("nombre"));
                obj.setApellido(rs.getString("apellido"));
                obj.setUsuario(rs.getString("usuario"));
                obj.setRol(rs.getString("rol"));
                
                idusuario = obj.getId();
                usuario_actual = obj.getUsuario();
                rol_usuario = obj.getRol();
                nombre = obj.getNombre();
                apellido = obj.getApellido();
            }
            
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
            GUILogin guilogin = new GUILogin();
            JOptionPane.showMessageDialog(guilogin, e);
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }
    
    public boolean ComprobarUser(Usuario obj){
        boolean respuesta = false;
        try {
            String sql = "SELECT usuario from usuario where usuario = ?";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, obj.getUsuario());
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
    public List<Usuario> listar(String textoBuscado) {
        System.out.println("texto buscado " + textoBuscado);
        ArrayList<Usuario> lista = new ArrayList<>();
        try {
            String sql = "select * from usuario where usuario != 'william' and nombre ilike ? order by nombre asc";
            sentencia = conec.prepareStatement(sql);
            sentencia.setString(1, "%" + textoBuscado + "%");
            ResultSet rs = sentencia.executeQuery();

            //recorrer una lista
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setApellido(rs.getString("apellido"));
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setRol(rs.getString("rol"));
                usuario.setEstado(rs.getString("estado"));
                lista.add(usuario);
            }

        } catch (SQLException ex) {
            Logger.getLogger(UsuarioCrudImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
}
