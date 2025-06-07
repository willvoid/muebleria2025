/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.UsuarioTablaModel;
import com.lp2.muebleria2025.vista.GUIUsuarios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author cmendieta
 */
public class UsuarioController implements ActionListener, KeyListener {
    private GUIUsuarios gui;
    private UsuarioCrudImpl crud;

    private char operacion;
    Usuario usuario = new Usuario();

    UsuarioTablaModel modelo = new UsuarioTablaModel();

    public UsuarioController(GUIUsuarios gui, UsuarioCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        //this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        //AutoCompleteDecorator.decorate(guiv.cbo_usuario);

        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                UsuarioTablaModel model = (UsuarioTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setUsuarioForm(model.getUsuarioByRow(row));
            }
        });

        habilitarCampos(false);
        habilitarBoton(false);

        listar("");
    }

    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }

    public void listar(String valorBuscado) {
        List<Usuario> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");

        if (e.getSource() == gui.txt_buscar) {
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }
        /*if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }*/
        if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }

        if (e.getSource() == gui.btn_eliminar) {
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea elimnar el registro?",
                        "Confirmar operacion",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crud.eliminar(modelo.getUsuarioByRow(fila));
                    listar("");
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
        }
        if (e.getSource() == gui.btn_cancelar) {
            habilitarCampos(false);
            habilitarBoton(false);
            limpiar();
        }

        if (e.getSource() == gui.btn_guardar) {
            boolean v_control = validarDatos();
            if (v_control == true) {
                JOptionPane.showMessageDialog(gui, "favor completar los datos");
                return;
            }
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                crud.insertar(getUsuarioForm());

                gui.txt_nombre.requestFocus();
            }

            if (operacion == 'E') {
                crud.actualizar(getUsuarioForm());
                habilitarCampos(false);
            }

            listar("");
            limpiar();

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
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Usuario getUsuarioForm() {
        usuario.setNombre(gui.txt_nombre.getText());
        usuario.setApellido(gui.txt_apellido.getText());
        usuario.setUsuario(gui.txt_usuario.getText());
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
        return vacio;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        this.listar(gui.txt_buscar.getText());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
