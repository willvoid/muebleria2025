/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.lp2.muebleria2025;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lp2.muebleria2025.vista.GUILogin;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author HP
 */
public class muebleria2025 {

    public static void main(String[] args) {
         //boolean licenciaValida = LicenciaSupabase.verificarLicencia();
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        GUILogin gui = new GUILogin();
        
        gui.setVisible(true);
        
        /*if (licenciaValida) {
            System.out.println("Licencia activa");
            // guardar fecha local, continuar app
        } else {
            JOptionPane.showMessageDialog(null, "Tu licencia ha expirado. Contacta con soporte.", "Licencia vencida", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }*/
    }
}
