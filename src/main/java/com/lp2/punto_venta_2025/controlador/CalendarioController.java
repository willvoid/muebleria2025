/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.controlador;

import com.lp2.punto_venta_2025.vista.GUICalendario;
//import com.lp2.punto_venta_2025.vista.GUIDescuentos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;

/**
 *
 * @author HP
 */
public class CalendarioController {
    GUICalendario gui;
    public CalendarioController(GUICalendario gui, JTextField txt) {
        this.gui = gui;
        
        gui.btn_listo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    obtenerFecha(txt);
                    gui.dispose(); // Cierra la ventana después de establecer la fecha
                }
        });
        
        gui.btn_cancelar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gui.dispose(); // Cierra la ventana después de establecer la fecha
                }
        });
    }
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    private void obtenerFecha(JTextField txt) {                                          
        // TODO add your handling code here:
        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = ff.format(gui.jCalendar1.getCalendar().getTime());
        txt.setText(fecha);
    }
}

  
