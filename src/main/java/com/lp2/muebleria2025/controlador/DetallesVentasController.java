/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.Cliente;
import com.lp2.muebleria2025.modelo.DetalleVentas;
import com.lp2.muebleria2025.modelo.Producto;
import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.modelo.Ventas;
import com.lp2.muebleria2025.modelo.dao.ClienteCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ProductoCrudImpl;
import com.lp2.muebleria2025.modelo.dao.DetalleVentasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.dao.VentasCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.DetalleVentaTablaModel;
import com.lp2.muebleria2025.modelo.tabla.VentasTablaModel;
import com.lp2.muebleria2025.vista.GUIVentasF;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.JTextField;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

public class DetallesVentasController implements ActionListener , KeyListener {
    
    private GUIVentasF gui;
    private DetalleVentasCrudImpl crud;
    private ProductoCrudImpl crudpro = new ProductoCrudImpl();
    private String num_venta;
    private char operacion;
    private int num_productos = 0;
    private Integer cantidad_anterior;
    private Integer id_del_producto;
    DetalleVentas d_ventas = new DetalleVentas();
    
    public int IdAnt = 0;
    public int subAnt = 0;
    public int cantAnt = 0;
    
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
                DetalleVentas detalle = model.getDetalleVentasByRow(row);
                //Devolver el objeto seleccionado en la fila
                
                if (detalle != null) {
                    setDetalleVentasForm(detalle);
                    String idA = gui.txt_idproducto.getText();
                    String subA = gui.txt_subtotal.getText();
                    String cantA = gui.txt_cantidad_de_venta.getText();
                    IdAnt = Integer.parseInt(idA);
                    subAnt = Integer.parseInt(subA);
                    cantAnt = Integer.parseInt(cantA);
                } else {
                    System.out.println("Advertencia: DetalleVentas es nulo para la fila " + row);
                }
            }
        });
        
        gui.txt_subtotal.setText("0");
        gui.txt_descuento_n.setText("0");
        gui.jLabel11.setVisible(false);
        
        gui.txt_descuento_n.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                Integer subtotal = Integer.valueOf(gui.txt_subtotal.getText());
                Integer promocion = Integer.valueOf(gui.txt_descuento.getText());
                Integer total = subtotal - promocion;  
                String descuento_string = gui.txt_descuento_n.getText();
                if (descuento_string.isEmpty()){
                    gui.txt_totalventas.setText(total.toString());
                }else{
                    CalcularDescPersonalizado();
                }     
            }
        });
        
        gui.txt_idproducto.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                actualizarProductoPorId();       
            }
        });
        
        gui.txt_factura.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                num_venta = gui.txt_factura.getText();
                listar(num_venta);
            }
        });
           
        gui.txt_cantidad_de_venta.addActionListener(e -> {
            actionPerformed(e);
        });
        
        gui.txt_descripcion.addActionListener(e -> {
            String textoFinal = gui.txt_descripcion.getText();
            gui.txt_cantidad_de_venta.requestFocusInWindow();
            actualizarProductoPorName(textoFinal);
        });
        
        gui.txt_descripcion.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                String busqueda = gui.txt_descripcion.getText();
                gui.jList1.setVisible(true);
                llenarJList(gui.jList1, busqueda);  
            }
        });
        
        /*gui.txt_totalventas.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Integer total_desc = Integer.valueOf(gui.txt_totalventas.getText());
                Integer descuento_max = CalcularDescVentaGral();
                if (total_desc < descuento_max) {
                    JOptionPane.showMessageDialog(gui, "El Descuento máximo es de: " + descuento_max);
                    gui.txt_totalventas.requestFocus();
                }
            }
        });*/
        
        
        gui.jList1.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()){//Verifica que el evento no sea ajustado (evita múltiples disparos al cambiar selección)
                    
                    Object seleccion = gui.jList1.getSelectedValue(); 
                    gui.jList1.setVisible(true);
                    if (seleccion != null){
                        String selecciontxt = seleccion.toString();
                        gui.txt_descripcion.setText(selecciontxt);
                        actualizarProductoPorName(selecciontxt);
                        gui.jList1.setVisible(true);
                    }
                }
            }

        });
        operacion = 'N';
        
        
        gui.txt_precio_venta.setEnabled(true);
        gui.txt_fecha.setEnabled(false);
        llenarJList(gui.jList1, "");
        gui.txt_precio_descuento.setVisible(false);
        gui.txt_iva.setVisible(false);
        gui.txt_fecha.setVisible(false);
        gui.txt_idproducto.setVisible(false);
        gui.txt_totalventas.setEnabled(false);
        gui.txt_stockdisponible.setEnabled(false);
        gui.txt_subtotal.setEnabled(false);
        gui.txt_subtotal.setText("0");
        habilitarCampos(true);
        habilitarBoton(true);
        //autoCompletePro();
        num_venta = gui.txt_factura.getText();
        CalcularSubtotalGral();
        listar(num_venta);
        gui.tabla.getColumnModel().removeColumn(gui.tabla.getColumnModel().getColumn(5));
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
            habilitarCampos(false);
            habilitarBoton(false);
            int fila = gui.tabla.getSelectedRow();
            boolean v_control = validarDatos();
            if (fila >= 0 && v_control == false) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea Editar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    operacion = 'E';
                    //crud.actualizar((DetalleVentas) modelo.getDetalleVentasByRow(fila));
                    Integer idProductoo = Integer.valueOf(gui.txt_idproducto.getText());
                    id_del_producto = idProductoo;
                    Integer cantAnterior = Integer.valueOf(gui.txt_cantidad_de_venta.getText());
                    cantidad_anterior = cantAnterior;
                    sumarStock(cantidad_anterior, id_del_producto);
                    listar(num_venta);
                    habilitarCampos(true);
                    habilitarBoton(true);
                    gui.txt_descripcion.requestFocus();
                    actualizarProductoPorName(gui.txt_descripcion.getText());
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
        }
        
        if (e.getSource() == gui.btn_eliminar) {
            int fila = gui.tabla.getSelectedRow();
            boolean v_control = validarDatos();
            if (fila >= 0 && v_control == false) {
                int ok = JOptionPane.showConfirmDialog(gui,
                        "Realmente desea Elimnar el registro?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    Integer idProductoo = Integer.valueOf(gui.txt_idproducto.getText());
                    Integer cantAnterior = Integer.valueOf(gui.txt_cantidad_de_venta.getText());
                    sumarStock(cantAnterior, idProductoo);
                    crud.eliminar((DetalleVentas) modelo.getDetalleVentasByRow(fila));
                    num_productos--;
                    listar(num_venta);
                    operacion = 'N';
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
            operacion = 'N';
            CalcularSubtotalGral();
            limpiar();
        }
        if (e.getSource() == gui.btn_cancelar) {
            //habilitarCampos(false);
            //habilitarBoton(false);
            if (operacion== 'E'){
                restarStock(cantidad_anterior, id_del_producto);
                gui.jList1.setVisible(false);
                limpiar();
                operacion = 'N';
                cantidad_anterior = null;
                id_del_producto = null;
            }else{
                gui.jList1.setVisible(false);
                limpiar();
                operacion = 'N';
            }
        }
        
        
        if (e.getSource() == gui.btn_añadir || e.getSource() == gui.txt_cantidad_de_venta) {
                        boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
                        int cant = Integer.parseInt(gui.txt_cantidad_de_venta.getText());
                        int stock = Integer.parseInt(gui.txt_stockdisponible.getText());
                        int stockact = stock - cant;
            if (stockact < 0){
                JOptionPane.showMessageDialog(gui, "Stock insuficiente para realizar esta venta.");
                gui.txt_cantidad_de_venta.requestFocus();
                return;
            }
                        

            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                try {
                    crud.insertar(getDetalleVentasForm());
                    Integer idProductoo = Integer.valueOf(gui.txt_idproducto.getText());
                    Integer cantAnterior = Integer.valueOf(gui.txt_cantidad_de_venta.getText());
                    restarStock(cantAnterior, idProductoo);
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesVentasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                num_productos++;
                gui.txt_descripcion.requestFocus();
            }
            
            if (operacion == 'E') {
                try {
                    Integer idProductoo = Integer.valueOf(gui.txt_idproducto.getText());
                    Integer cantAnterior = Integer.valueOf(gui.txt_cantidad_de_venta.getText());
                    crud.actualizar(getDetalleVentasForm());
                    restarStock(cantAnterior, idProductoo);
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesVentasController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            CalcularSubtotalGral();
            listar(num_venta);
            limpiar();
            operacion = 'N';
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        //gui.cbo_cliente.setEnabled(estado);
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
        gui.txt_precio_descuento.setText("");
        //gui.txt_subtotal.setText("");
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private DetalleVentas getDetalleVentasForm() throws java.text.ParseException {
        d_ventas.setVenta(Integer.valueOf(gui.txt_factura.getText()));
        d_ventas.setPrecio(Integer.valueOf(gui.txt_precio_venta.getText()));
        d_ventas.setCantidad(Integer.valueOf(gui.txt_cantidad_de_venta.getText()));
        d_ventas.setIdproducto(Integer.valueOf(gui.txt_idproducto.getText()));
        d_ventas.setSubtotal(CalcularSubtotal());
        d_ventas.setPrecio_descuento(Integer.valueOf(gui.txt_precio_descuento.getText()));
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
        gui.txt_idproducto.setText(item.getIdproducto().toString());
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
        if(gui.txt_precio_descuento.getText().isEmpty()){
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

        //Decorar el JTextField para el autocompletado
        AutoCompleteDecorator.decorate(listasugerencia, gui.txt_descripcion, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        /*String produ = gui.txt_descripcion.getText();
        actualizarProductoPorName(produ);*/
    }
    
    private void actualizarProductoPorName(String name) {
        //String id = gui.txt_idproducto.getText();
        if (!name.isEmpty()) {
            
            //int idProducto = Integer.parseInt(idText);
            DetalleVentas producto = crud.buscarPorNameProducto(name);
            if (producto != null) {
                gui.txt_idproducto.setText(producto.getIdproducto().toString());
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
                    gui.txt_precio_descuento.setText(producto.getPrecio_descuento().toString());
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
    
    private void CalcularDescPersonalizado (){
        Integer subtotal = Integer.valueOf(gui.txt_subtotal.getText());
        Integer promocion = Integer.valueOf(gui.txt_descuento.getText());
        Integer total = subtotal - promocion;                                 //El total es El subtotal - el descuento de productos en promocion
        String descuento_string = gui.txt_descuento_n.getText();
        Integer descuento_p = Integer.valueOf(descuento_string);
        try{
            if (descuento_p > total){
               JOptionPane.showMessageDialog(gui, "El descuento no puede ser mayor al Total.");
               gui.txt_descuento_n.requestFocus();
               return;
            }
            if (descuento_p < 0){
               JOptionPane.showMessageDialog(gui, "El descuento no puede ser Negativo.");
               gui.txt_descuento_n.setText("");
               gui.txt_descuento_n.requestFocus();
               return;
            }
            if (descuento_string.isEmpty()){
                gui.txt_totalventas.setText(total.toString());
            }else{
              Integer total_nuevo = total - descuento_p;
              gui.txt_totalventas.setText(total_nuevo.toString());  
            }         
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(gui, "Descuento Inválido. Introduzca un número válido");
            gui.txt_descuento_n.setText("");
        } 
    }
    
    private void restarStock(Integer cantAnterior, Integer idProducto){
        // Actualizar el stock del producto

        // Obtener el stock actual del producto
        DetalleVentas producto = crud.buscarPorIdProducto(idProducto); 
        if (producto != null) {
            int stockActual = producto.getCantidad();
            int nuevoStock = stockActual - cantAnterior;
            crud.actualizarStock(idProducto, nuevoStock);
        }
    }

    private void sumarStock(Integer cantAnterior, Integer idProducto){                          
            // Actualizar el stock del producto

            // Obtener el stock actual del producto
            DetalleVentas producto = crud.buscarPorIdProducto(idProducto); 
                Integer stockActual = producto.getCantidad();
                Integer nuevoStock = stockActual + cantAnterior;
                crud.actualizarStock(idProducto, nuevoStock);
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

