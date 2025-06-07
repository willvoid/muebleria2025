/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import com.lp2.muebleria2025.vista.GUILogin;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conexion {

    String url = "jdbc:postgresql://localhost:5433/puntoventa2025";
    String usuario = "postgres";
    String password = "123";
    GUILogin guilogin = new GUILogin();
    public Connection conectarBD() {
        Connection conectar = null;
        try {

            conectar = DriverManager.getConnection(url, usuario, password);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(guilogin, ex);
        }
        return conectar;
    }

}
