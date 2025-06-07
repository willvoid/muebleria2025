/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.punto_venta_2025.modelo.Cliente;
import com.lp2.punto_venta_2025.modelo.DetalleVentas;
import com.lp2.punto_venta_2025.modelo.Producto;
import com.lp2.punto_venta_2025.modelo.Ventas;
import com.lp2.punto_venta_2025.modelo.dao.ClienteCrudImpl;
import com.lp2.punto_venta_2025.modelo.dao.ProductoCrudImpl;
import com.lp2.punto_venta_2025.modelo.dao.DetalleVentasCrudImpl;
import com.lp2.punto_venta_2025.modelo.dao.VentasCrudImpl;
import com.lp2.punto_venta_2025.modelo.tabla.DetalleVentaTablaModel;
import com.lp2.punto_venta_2025.modelo.tabla.VentasTablaModel;
import com.lp2.punto_venta_2025.vista.GUIVentasF;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class DetallesVentasController implements ActionListener , KeyListener {
    
    private GUIVentasF gui;
    private DetalleVentasCrudImpl crud;
    private ProductoCrudImpl crudpro = new ProductoCrudImpl();
    private String num_venta;
    private char operacion;
    private int num_productos = 0;
    DetalleVentas d_ventas = new DetalleVentas();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ClienteCrudImpl crudCliente = new ClienteCrudImpl();
    
    DetalleVentaTablaModel modelo = new DetalleVentaTablaModel();
    
    public DetallesVentasController(GUIVentasF gui, DetalleVentasCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_añadir.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        //this.gui.txt_buscar.addKeyListener(this);
        
        
        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                DetalleVentaTablaModel model = (DetalleVentaTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setDetalleVentasForm(model.getDetalleVentasByRow(row));
            }
        });
        
        gui.txt_idproducto.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                actualizarProductoPorCod();       
            }
        });
        
        gui.txt_factura.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                num_venta = gui.txt_factura.getText();
                listar(num_venta);
            }
        });
        
        
       /* gui.txt_cantidad_de_venta.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                CalcularSubtotal();       
            }
        });*/
        
        
        gui.txt_descripcion.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                String busqueda = gui.txt_descripcion.getText();
                gui.jList1.setVisible(true);
                llenarJList(gui.jList1, busqueda);  
            }
        });
        
        gui.jList1.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){//Verifica que el evento no sea ajustado (evita múltiples disparos al cambiar selección)
                    
                    Object seleccion = gui.jList1.getSelectedValue(); 
                    gui.jList1.setVisible(false);
                    if (seleccion != null){
                        String selecciontxt = seleccion.toString();
                        gui.txt_descripcion.setText(selecciontxt);
                        actualizarProductoPorName(selecciontxt);
                        gui.jList1.setVisible(false);
                    }
                }
            }

        });
        operacion = 'N';
        gui.txt_precio_venta.setEnabled(false);
        gui.txt_fecha.setEnabled(false);
        gui.txt_stockdisponible.setEnabled(false);
        gui.txt_subtotal.setText("0");
        habilitarCampos(true);
        habilitarBoton(true);
        autoCompletePro();
        num_venta = gui.txt_factura.getText();
        CalcularSubtotalGral();
        listar(num_venta);
    }

    /*public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }*/
    
    public void listar(String valorBuscado) {
        List<DetalleVentas> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        /*if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }*/
        /*if (e.getSource() == gui.btn_generar_venta){
            GUIVentasF ventas = new GUIVentasF(null, true);
            VentasCrudImpl crudv = new VentasCrudImpl();
            VentasController ctrlv = new VentasController(ventas, crudv);
            ctrlv.generarTicket();
        }*/
        
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.cbo_cliente.requestFocus();
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
                    crud.actualizar((DetalleVentas) modelo.getDetalleVentasByRow(fila));
                    listar(num_venta);
                    habilitarCampos(true);
                    habilitarBoton(true);
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
            gui.cbo_cliente.requestFocus();
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
                    crud.eliminar((DetalleVentas) modelo.getDetalleVentasByRow(fila));
                    num_productos--;
                    listar(num_venta);
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
            CalcularSubtotalGral();
            limpiar();
        }
        if (e.getSource() == gui.btn_cancelar) {
            //habilitarCampos(false);
            //habilitarBoton(false);
            gui.jList1.setVisible(false);
            limpiar();
        }
        
        if (e.getSource() == gui.btn_añadir) {
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                try {
                    crud.insertar(getDetalleVentasForm());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesVentasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                num_productos++;
                gui.cbo_cliente.requestFocus();
            }
            
            if (operacion == 'E') {
                try {
                    crud.actualizar(getDetalleVentasForm());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesVentasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                habilitarCampos(false);
            }
            
            CalcularSubtotalGral();
            listar(num_venta);
            limpiar();
            
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.cbo_cliente.setEnabled(estado);
        //gui.txt_fecha.setEnabled(estado);
        //gui.cbo_formapago.setEnabled(estado);
        
    }
    
      private void habilitarBoton(Boolean estado) {
        gui.btn_añadir.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_descripcion.setText("");
        gui.txt_idproducto.setText("");
        gui.txt_precio_venta.setText("");
        gui.txt_stockdisponible.setText("");
        //gui.txt_subtotal.setText("");
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private DetalleVentas getDetalleVentasForm() throws java.text.ParseException {
        d_ventas.setVenta(Integer.valueOf(gui.txt_factura.getText()));
        d_ventas.setPrecio(Integer.valueOf(gui.txt_precio_venta.getText()));
        d_ventas.setCantidad(Integer.valueOf(gui.txt_cantidad_de_venta.getText()));
        d_ventas.setCodigo(gui.txt_idproducto.getText());
        d_ventas.setSubtotal(CalcularSubtotal());
        d_ventas.setProducto(gui.txt_descripcion.getText());
//        d_ventas.setMontoCuotas(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setMontoPagado(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setNro_cuotas(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setInteres(Integer.valueOf(gui.txt_subtotal.getText()));
        return d_ventas;
    }
    

    //Funcion o metodo encargado asignar valor los JTextField
    private void setDetalleVentasForm(DetalleVentas item) {
        System.out.println(item);
        d_ventas.setId(item.getId());
        gui.txt_idproducto.setText(item.getCodigo());
        gui.txt_cantidad_de_venta.setText(item.getCantidad().toString());
        //gui.cbo_cliente.setSelectedItem(item.getIdCliente());
        //gui.cbo_formapago.setSelected(item.getMetodo_pago());
        //gui.txt_fecha.setText(item.getFechaVenta().toString());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        
        /*if(gui.txt_formadepago.getText().isEmpty()){
            vacio = true;
        }*/
        if(gui.txt_descripcion.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_idproducto.getText().isEmpty()){
            vacio = true;
        }
        
        if(gui.txt_cantidad_de_venta.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }

    private void llenarJList(JList jlist, String busqueda){
       List<Producto> listapro = crudpro.listarecopro(busqueda);

        // Crear un modelo de lista compatible
        DefaultListModel<Producto> modelo = new DefaultListModel<>();
        for (Producto producto : listapro) {
            modelo.addElement(producto); // Agregar cada producto al modelo
        }

        // Crear la lista de sugerencias con el modelo
        JList<Producto> listasugerencia = new JList<>(modelo);
        
        jlist.setModel(modelo);
    }
    
    private void autoCompletePro() {
        // Obtener la lista de productos
        List<Producto> listapro = crudpro.listarecopro("");

        // Crear un modelo de lista compatible
        DefaultListModel<Producto> modelo = new DefaultListModel<>();
        for (Producto producto : listapro) {
            modelo.addElement(producto); // Agregar cada producto al modelo
        }

        // Crear la lista de sugerencias con el modelo
        JList<Producto> listasugerencia = new JList<>(modelo);

        // Decorar el JTextField para el autocompletado
        /*AutoCompleteDecorator.decorate(listasugerencia, gui.txt_descripcion, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);*/
        String produ = gui.txt_descripcion.getText();
        actualizarProductoPorName(produ);
    }
    
    private void actualizarProductoPorName(String name) {
        //String id = gui.txt_idproducto.getText();
        if (!name.isEmpty()) {
            
            //int idProducto = Integer.parseInt(idText);
            DetalleVentas producto = crud.buscarPorNameProducto(name);
            if (producto != null) {
                gui.txt_idproducto.setText(producto.getCodigo());
            } else {
                //gui.txt_descripcion.setText("Producto no encontrado");
                gui.txt_precio_venta.setText("0");
                gui.txt_cantidad_de_venta.setText("");
            }          
        } else {
            gui.txt_descripcion.setText("");
            gui.txt_precio_venta.setText("0");
            gui.txt_cantidad_de_venta.setText("");
        }
    }
    
    private void actualizarProductoPorId() {
        String idText = gui.txt_idproducto.getText();
        if (!idText.isEmpty()) {
            try {
                int idProducto = Integer.parseInt(idText);
                DetalleVentas producto = crud.buscarPorIdProducto(idProducto);
                if (producto != null) {
                    gui.txt_descripcion.setText(producto.getProducto());
                    gui.txt_precio_venta.setText(producto.getPrecio().toString());
                    gui.txt_stockdisponible.setText(producto.getCantidad().toString());
                    gui.txt_cantidad_de_venta.requestFocus();
                } else {
                    
                    JOptionPane.showMessageDialog(gui, "ID del Producto no encontrado");
                          
                    gui.txt_descripcion.setText("");
                    gui.txt_precio_venta.setText("0");
                    gui.txt_cantidad_de_venta.setText("");
                }
                gui.jList1.setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(gui, "ID inválido. Introduzca un número");
            }
        } else {
            gui.txt_descripcion.setText("");
            gui.txt_precio_venta.setText("0");
            gui.txt_cantidad_de_venta.setText("");
        }
    }
    
    private void actualizarProductoPorCod() {
        String CodText = gui.txt_idproducto.getText();
        if (!CodText.isEmpty()) {
            try {
                //int idProducto = Integer.parseInt(idText);
                DetalleVentas producto = crud.buscarCodProducto(CodText);
                if (producto != null) {
                    gui.txt_descripcion.setText(producto.getProducto());
                    gui.txt_precio_venta.setText(producto.getPrecio().toString());
                    gui.txt_stockdisponible.setText(producto.getCantidad().toString());
                    gui.txt_cantidad_de_venta.requestFocus();
                } else {
                    
                    JOptionPane.showMessageDialog(gui, "Código del Producto no encontrado");
                          
                    gui.txt_descripcion.setText("");
                    gui.txt_precio_venta.setText("0");
                    gui.txt_cantidad_de_venta.setText("");
                }
                gui.jList1.setVisible(false);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(gui, "ID inválido. Introduzca un número");
            }
        } else {
            gui.txt_descripcion.setText("");
            gui.txt_precio_venta.setText("0");
            gui.txt_cantidad_de_venta.setText("");
        }
    }
    
    private Integer CalcularSubtotal(){
        String cantidad = gui.txt_cantidad_de_venta.getText();
        String precio = gui.txt_precio_venta.getText();
        Integer subtotal = 0;
        if (!cantidad.isEmpty()) {
            Integer cant = Integer.valueOf(cantidad);
            Integer price = Integer.valueOf(precio);
            subtotal = cant*price;
        }
        return subtotal;
    }
    
    private void CalcularSubtotalGral(){
        List<DetalleVentas> listapro = crud.listar(num_venta);
        Integer subtotal_gral = 0;
        for (DetalleVentas item : listapro){
            subtotal_gral += item.getSubtotal(); 
        }
        gui.txt_subtotal.setText(subtotal_gral.toString());
        Integer total_desc = crud.DescuentoTotal(num_venta);
        Integer total_iva = crud.CalcularIvaTotal(num_venta);
        Integer total = subtotal_gral - total_desc;
        
        gui.txt_iva.setText(total_iva.toString());
        gui.txt_descuento.setText(total_desc.toString());
        gui.txt_totalventas.setText(total.toString());
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
      
    }

    /*@Override
    public void keyPressed(KeyEvent e) {
        this.listar(gui.txt_buscar.getText());
    }*/

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    private abstract class DocumentChangeListener implements DocumentListener {
        public abstract void onChange();

        @Override
        public void insertUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onChange();
        }
    }
}

