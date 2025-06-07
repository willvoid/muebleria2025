/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;


import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.Iva;
import com.lp2.muebleria2025.modelo.Marca;
import com.lp2.muebleria2025.modelo.Descuentos;
import com.lp2.muebleria2025.modelo.Producto;
import com.lp2.muebleria2025.modelo.dao.IvaCrudImpl;
import com.lp2.muebleria2025.modelo.dao.MarcaCrudImpl;
import com.lp2.muebleria2025.modelo.dao.DescuentosCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ProductoCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.DescuentosTablaModel;
import com.lp2.muebleria2025.vista.GUIDescuentos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author cmendieta
 */
public class DescuentosController implements ActionListener , KeyListener {
    
    private GUIDescuentos gui;
    private DescuentosCrudImpl crud;
    
    private char operacion;
    Descuentos descuento = new Descuentos();
    
    ProductoCrudImpl crudPro = new ProductoCrudImpl();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    DescuentosTablaModel modelo = new DescuentosTablaModel();
    
    public DescuentosController(GUIDescuentos gui, DescuentosCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        
        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                DescuentosTablaModel model = (DescuentosTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setDescuentosForm(model.getDescuentosByRow(row));
            }
        });
        llenarComboProducto(gui.cbo_producto);
        gui.txt_desde.setEnabled(false);
        gui.txt_hasta.setEnabled(false);
        habilitarCampos(false);
        habilitarBoton(false);
        
        listar("");
    }
    
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    public void listar(String valorBuscado) {
        List<Descuentos> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        
        gui.tabla.updateUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.txt_nombre.requestFocus();
        }
        if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(false);
            habilitarBoton(false);
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea Editar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    listar("");
                    habilitarCampos(true);
                    habilitarBoton(true);
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
            gui.txt_nombre.requestFocus();
        }
        
        if (e.getSource() == gui.btn_eliminar) {
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea Elimnar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crud.eliminar(modelo.getDescuentosByRow(fila));
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
            Producto seleccionado = (Producto) gui.cbo_producto.getSelectedItem();
            if (seleccionado.getId() == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar un producto válido.");
                gui.cbo_producto.requestFocus();
                return;
            }
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor, completar todos los datos");
                return;
            }
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                try {
                    crud.insertar(getDescuentosForm());
                    gui.txt_nombre.requestFocus();
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DescuentosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            if (operacion == 'E') {
                try {
                    crud.actualizar(getDescuentosForm());
                    habilitarCampos(false);
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DescuentosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            listar("");
            limpiar();
            
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_nombre.setEnabled(estado);
        gui.cbo_producto.setEnabled(estado);
        gui.txt_descuento.setEnabled(estado);
    }
    
      private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
        gui.btn_calendar1.setEnabled(estado);
        gui.btn_calendar2.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_nombre.setText("");
        gui.txt_desde.setText("");
        gui.txt_hasta.setText("");
        gui.txt_descuento.setText("");
        gui.cbo_producto.setSelectedIndex(0);
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Descuentos getDescuentosForm() throws java.text.ParseException {
        descuento.setNombre(gui.txt_nombre.getText());
        descuento.setDesde(StringToDate(gui.txt_desde, 1));
        descuento.setHasta(StringToDate(gui.txt_hasta, 2));
        descuento.setDescuento(Double.valueOf(gui.txt_descuento.getText()));
        descuento.setProducto((Producto) gui.cbo_producto.getSelectedItem());
        return descuento;
    }
    

    //Funcion o metodo encargado asignar valor los JTextField
    private void setDescuentosForm(Descuentos item) {
        
        System.out.println(item);
        descuento.setId(item.getId());
        gui.txt_nombre.setText(item.getNombre());
        gui.txt_desde.setText(sdf.format(item.getDesde()));
        gui.txt_hasta.setText(sdf.format(item.getHasta())); //sdf.format formatea la fecha en "dd/mm/yyyy"
        gui.txt_descuento.setText(item.getDescuento().toString());
        gui.cbo_producto.setSelectedItem(item.getProducto());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        if(gui.txt_nombre.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_desde.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_hasta.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_descuento.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    private void llenarComboProducto(JComboBox cbo){
        DefaultComboBoxModel<Producto> model = new DefaultComboBoxModel();
        
        // Agregar el item "Seleccionar Producto"
        Producto seleccionar = new Producto();
        seleccionar.setId(-1); // ID especial para distinguir
        seleccionar.setNombre("Seleccionar Producto");
        model.addElement(seleccionar);
        
        List<Producto> lista = crudPro.listar("");
        for (int i = 0; i < lista.size(); i++) {
            Producto productoo = lista.get(i);
            model.addElement(productoo);
        }
        cbo.setModel(model);
    }
    
    private Date StringToDate(JTextField txt, Integer i) throws java.text.ParseException{
        try {
            Date fecha = sdf.parse(txt.getText()); // Convierte el texto a un Date   
            return fecha;
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            if (i==1){
               gui.btn_calendar1.requestFocus();
            } else {
               gui.btn_calendar2.requestFocus();
            }
            
            return null; // O maneja el error según lo necesario
        }   
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
