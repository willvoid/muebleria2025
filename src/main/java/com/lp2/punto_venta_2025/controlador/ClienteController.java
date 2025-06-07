/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.controlador;


import com.lp2.punto_venta_2025.modelo.Cliente;
import com.lp2.punto_venta_2025.modelo.dao.ClienteCrudImpl;
import com.lp2.punto_venta_2025.modelo.tabla.ClienteTablaModel;
import com.lp2.punto_venta_2025.vista.GUICliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
//import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author cmendieta
 */
public class ClienteController implements ActionListener, KeyListener {
    private GUICliente gui;
    private ClienteCrudImpl crud;

    private char operacion;
    Cliente cliente = new Cliente();

    ClienteTablaModel modelo = new ClienteTablaModel();

    public ClienteController(GUICliente gui, ClienteCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        //AutoCompleteDecorator.decorate(guiv.cbo_cliente);

        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                ClienteTablaModel model = (ClienteTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setClienteForm(model.getClienteByRow(row));
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
        List<Cliente> lista = crud.listar(valorBuscado);
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
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_rut.requestFocus();
        }
        if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(true);
            habilitarBoton(true);
            gui.txt_rut.requestFocus();
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
                    crud.eliminar(modelo.getClienteByRow(fila));
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
                crud.insertar(getClienteForm());

                gui.txt_rut.requestFocus();
            }

            if (operacion == 'E') {
                crud.actualizar(getClienteForm());
                habilitarCampos(false);
            }

            listar("");
            limpiar();

        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_rut.setEnabled(estado);
        gui.txt_razon_social.setEnabled(estado);
        gui.txt_direccion.setEnabled(estado);
        gui.txt_telefono.setEnabled(estado);
        gui.txt_correo.setEnabled(estado);
    }

    private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }

    private void limpiar() {
        gui.txt_rut.setText("");
        gui.txt_razon_social.setText("");
        gui.txt_direccion.setText("");
        gui.txt_telefono.setText("");
        gui.txt_correo.setText("");
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Cliente getClienteForm() {
        cliente.setRut(gui.txt_rut.getText());
        cliente.setRazonSocial(gui.txt_razon_social.getText());
        cliente.setDireccion(gui.txt_direccion.getText());
        cliente.setCorreo(gui.txt_correo.getText());
        cliente.setTelefono(gui.txt_telefono.getText());
        return cliente;
    }

    //Funcion o metodo encargado asignar valor los JTextField
    private void setClienteForm(Cliente item) {
        System.out.println(item);
        cliente.setId(item.getId());
        gui.txt_rut.setText(item.getRut());
        gui.txt_razon_social.setText(item.getRazonSocial());
        gui.txt_direccion.setText(item.getDireccion());
        gui.txt_telefono.setText(item.getTelefono());
        gui.txt_correo.setText(item.getCorreo());

    }

    private boolean validarDatos() {
        boolean vacio = false;
        if (gui.txt_rut.getText().isEmpty()) {
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
