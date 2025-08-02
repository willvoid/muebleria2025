/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.Cliente;
import com.lp2.muebleria2025.modelo.DetalleCompras;
import com.lp2.muebleria2025.modelo.Producto;
import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.modelo.Compras;
import com.lp2.muebleria2025.modelo.dao.ClienteCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ProductoCrudImpl;
import com.lp2.muebleria2025.modelo.dao.DetalleComprasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ComprasCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.DetalleComprasTablaModel;
import com.lp2.muebleria2025.modelo.tabla.ComprasTablaModel;
import com.lp2.muebleria2025.vista.GUIComprasF;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class DetallesComprasController implements ActionListener , KeyListener {
    
    private GUIComprasF gui;
    private DetalleComprasCrudImpl crud;
    private ProductoCrudImpl crudpro = new ProductoCrudImpl();
    private String num_venta;
    private char operacion;
    private int num_productos = 0;
    DetalleCompras d_ventas = new DetalleCompras();
    
    public int IdAnt = 0;
    public int subAnt = 0;
    public int cantAnt = 0;
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ClienteCrudImpl crudCliente = new ClienteCrudImpl();
    DetalleComprasTablaModel modelo = new DetalleComprasTablaModel();
    
    public DetallesComprasController(GUIComprasF gui, DetalleComprasCrudImpl crud) {
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
                DetalleComprasTablaModel model = (DetalleComprasTablaModel) tabla.getModel();
                DetalleCompras detalle = model.getDetalleComprasByRow(row);
                //Devolver el objeto seleccionado en la fila
                
                if (detalle != null) {
                    setDetalleComprasForm(detalle);
                    String idA = gui.txt_idproducto.getText();
                    String subA = gui.txt_subtotal.getText();
                    String cantA = gui.txt_cantidad_de_venta.getText();
                    IdAnt = Integer.parseInt(idA);
                    subAnt = Integer.parseInt(subA);
                    cantAnt = Integer.parseInt(cantA);
                } else {
                    System.out.println("Advertencia: DetalleCompras es nulo para la fila " + row);
                }
            }
        });
        
        gui.txt_descripcion.addActionListener(e -> {
            String textoFinal = gui.txt_descripcion.getText();
            gui.txt_cantidad_de_venta.requestFocusInWindow();
            actualizarProductoPorName(textoFinal);
        });
        
        gui.txt_cantidad_de_venta.addActionListener(e -> {
            actionPerformed(e);
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
        
        gui.txt_iva.setVisible(false);
        gui.txt_fecha.setVisible(false);
        gui.txt_idproducto.setVisible(false);
        
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
        List<DetalleCompras> lista = crud.listar(valorBuscado);
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
            GUIComprasF ventas = new GUIComprasF(null, true);
            ComprasCrudImpl crudv = new ComprasCrudImpl();
            ComprasController ctrlv = new ComprasController(ventas, crudv);
            ctrlv.generarTicket();
        }*/
        
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.cbo_proveedor.requestFocus();
        }
        if (e.getSource() == gui.btn_editar) {
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
                    operacion = 'E';
                    //crud.actualizar((DetalleCompras) modelo.getDetalleComprasByRow(fila));
                    listar(num_venta);
                    habilitarCampos(true);
                    habilitarBoton(true);
                    gui.txt_descripcion.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
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
                    restarStock();
                    crud.eliminar((DetalleCompras) modelo.getDetalleComprasByRow(fila));
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
            gui.jList1.setVisible(false);
            limpiar();
            operacion = 'N';
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
                    crud.insertar(getDetalleComprasForm());
                    sumarStock();
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesComprasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                num_productos++;
                gui.txt_descripcion.requestFocus();
            }
            
            if (operacion == 'E') {
                try {
                    restarStock();
                    crud.actualizar(getDetalleComprasForm());
                    sumarStock();
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(DetallesComprasController.class.getName()).log(Level.SEVERE, null, ex);
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
        //gui.txt_subtotal.setText("");
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private DetalleCompras getDetalleComprasForm() throws java.text.ParseException {
        d_ventas.setIdcompra(Integer.valueOf(gui.txt_factura.getText()));
        d_ventas.setPrecio(Integer.valueOf(gui.txt_precio_venta.getText()));
        d_ventas.setCantidad(Integer.valueOf(gui.txt_cantidad_de_venta.getText()));
        d_ventas.setIdproducto(Integer.valueOf(gui.txt_idproducto.getText()));
        d_ventas.setSubtotal(CalcularSubtotal());
        d_ventas.setProducto(gui.txt_descripcion.getText());
//        d_ventas.setMontoCuotas(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setMontoPagado(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setNro_cuotas(Integer.valueOf(gui.txt_subtotal.getText()));
//        d_ventas.setInteres(Integer.valueOf(gui.txt_subtotal.getText()));
        return d_ventas;
    }
    

    //Funcion o metodo encargado asignar valor los JTextField
    private void setDetalleComprasForm(DetalleCompras item) {
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
        AutoCompleteDecorator.decorate(listasugerencia, gui.txt_descripcion, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
        //String produ = gui.txt_descripcion.getText();
        //actualizarProductoPorName(produ);
    }
    
    private void actualizarProductoPorName(String name) {
        //String id = gui.txt_idproducto.getText();
        if (!name.isEmpty()) {
            
            //int idProducto = Integer.parseInt(idText);
            DetalleCompras producto = crud.buscarPorNameProducto(name);
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
                DetalleCompras producto = crud.buscarPorIdProducto(idProducto);
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
    
    private void sumarStock(){
        // Actualizar el stock del producto
        Integer idProducto = Integer.valueOf(gui.txt_idproducto.getText());
        Integer cantidadVendida = Integer.valueOf(gui.txt_cantidad_de_venta.getText());

        // Obtener el stock actual del producto
        DetalleCompras producto = crud.buscarPorIdProducto(idProducto); // Suponiendo que tienes un método para buscar productos por ID
        if (producto != null) {
            int stockActual = producto.getCantidad();
            int nuevoStock = stockActual + cantidadVendida;
            crud.actualizarStock(idProducto, nuevoStock);
        }
}

    private void restarStock(){                          
        // Actualizar el stock del producto
        int idProducto = IdAnt;


        // Obtener el stock actual del producto
        DetalleCompras producto = crud.buscarPorIdProducto(idProducto); // Suponiendo que tienes un método para buscar productos por ID
            int stockActual = producto.getCantidad();
            int nuevoStock = stockActual - cantAnt;
            crud.actualizarStock(idProducto, nuevoStock);
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
        List<DetalleCompras> listapro = crud.listar(num_venta);
        Integer subtotal_gral = 0;
        for (DetalleCompras item : listapro){
            subtotal_gral += item.getSubtotal(); 
        }
        gui.txt_subtotal.setText(subtotal_gral.toString());
        //Integer total_desc = crud.DescuentoTotal(num_venta);
        //Integer total_iva = crud.CalcularIvaTotal(num_venta);
        //Integer total = subtotal_gral - total_desc;
        
        /*gui.txt_iva.setText(total_iva.toString());
        gui.txt_descuento.setText(total_desc.toString());*/
        gui.txt_totalventas.setText(subtotal_gral.toString());
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

