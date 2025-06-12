/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.UsuarioTablaModel;
import com.lp2.muebleria2025.vista.GUINuevoUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author cmendieta
 */
public class NuevoUsuarioController implements ActionListener, KeyListener {
    private GUINuevoUsuario gui;
    private UsuarioCrudImpl crud;

    private char operacion;
    Usuario usuario = new Usuario();

    UsuarioTablaModel modelo = new UsuarioTablaModel();

    public NuevoUsuarioController(GUINuevoUsuario gui, UsuarioCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        //this.gui.btn_nuevo.addActionListener(this);
        //AutoCompleteDecorator.decorate(guiv.cbo_usuario);

        habilitarCampos(true);
        habilitarBoton(true);
        //listar("");
    }

    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        /*if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }*/

        if (e.getSource() == gui.btn_cancelar) {
            //habilitarCampos(false);
            //habilitarBoton(false);
            limpiar();
        }

        if (e.getSource() == gui.btn_guardar) {
            boolean v_control = validarDatos();
            boolean user_control = crud.ComprobarUser(getUsuarioForm());
            int clave = gui.txt_password.getText().length();
            String usuario = gui.txt_usuario.getText();
            String contraseña = gui.txt_password.getText();
            String rol = gui.cbo_rol.getSelectedItem().toString();
            String contraseña2 = gui.txt_password2.getText();

            // Validaciones previas
            if (v_control) {
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }

            if (user_control){
                JOptionPane.showMessageDialog(gui, "El Usuario '" + usuario + "' ya existe. Use otro Usuario");
                return;
            }

            if (clave < 6) {
                JOptionPane.showMessageDialog(gui, "La contraseña debe tener al menos 6 caracteres");
                return;
            }

            if (clave > 45) {
                JOptionPane.showMessageDialog(gui, "La contraseña no debe tener más de 45 caracteres");
                return;
            }

            // Validar caracteres permitidos (sólo letras y números, sin espacios)
            if (!usuario.matches("^[a-zA-Z0-9._-]+$")) {
                JOptionPane.showMessageDialog(gui, "El usuario solo debe contener letras y números, sin espacios ni caracteres especiales");
                gui.txt_usuario.requestFocus();
                return;
            }

            if (!contraseña.matches("^[a-zA-Z0-9._-]+$")) {
                JOptionPane.showMessageDialog(gui, "La contraseña solo debe contener letras y números, sin espacios ni caracteres especiales");
                gui.txt_password.requestFocus();
                return;
            }
            
            if (!contraseña.equals(contraseña2)) {
                JOptionPane.showMessageDialog(gui, "Las contraseñas no coinciden");
                gui.txt_password2.requestFocus();
                return;
            }
            
            if ("SELECCIONAR ROL".equals(rol)){
                JOptionPane.showMessageDialog(gui, "Favor Seleccione un Rol");
                gui.cbo_rol.requestFocus();
                return;
            }

            // Si todo está correcto
            System.out.println("Evento click de guardar nuevo usuario");
            try{
                crud.insertar(getUsuarioForm());
                gui.txt_nombre.requestFocus();
                JOptionPane.showMessageDialog(gui, "Usuario nuevo añadido correctamente");
                limpiar();
            }catch(Exception ex){
                Logger.getLogger(NuevoUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, ex, "Error al Ingresar Nuevo usuario", JOptionPane.ERROR_MESSAGE);
            }
        }

        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_nombre.setEnabled(estado);
        gui.txt_apellido.setEnabled(estado);
        gui.txt_usuario.setEnabled(estado);
        gui.cbo_rol.setEnabled(estado);
    }

    private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }

    private void limpiar() {
        gui.txt_nombre.setText("");
        gui.txt_apellido.setText("");
        gui.txt_usuario.setText("");
        gui.txt_password.setText("");
        gui.txt_password2.setText("");
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Usuario getUsuarioForm() {
        usuario.setNombre(gui.txt_nombre.getText());
        usuario.setApellido(gui.txt_apellido.getText());
        usuario.setUsuario(gui.txt_usuario.getText());
        usuario.setClave(gui.txt_password.getText().trim());
        usuario.setRol((String) gui.cbo_rol.getSelectedItem());
        return usuario;
    }

    //Funcion o metodo encargado asignar valor los JTextField
    private void setUsuarioForm(Usuario item) {
        System.out.println(item);
        usuario.setId(item.getId());
        gui.txt_nombre.setText(item.getNombre());
        gui.txt_apellido.setText(item.getApellido());
        gui.txt_usuario.setText(item.getUsuario());
        gui.cbo_rol.setSelectedItem(item.getRol());
    }

    private boolean validarDatos() {
        boolean vacio = false;
        if (gui.txt_nombre.getText().isEmpty()) {
            vacio = true;
        }
        if (gui.txt_apellido.getText().isEmpty()) {
            vacio = true;
        }
        if (gui.txt_usuario.getText().isEmpty()) {
            vacio = true;
        }
        if (gui.txt_password.getText().isEmpty()) {
            vacio = true;
        }
        return vacio;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
