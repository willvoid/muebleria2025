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
import com.lp2.muebleria2025.modelo.DetalleVentas;
import com.lp2.muebleria2025.modelo.Proveedor;
import com.lp2.muebleria2025.modelo.Transaccion;
import com.lp2.muebleria2025.modelo.dao.ClienteCrudImpl;
import com.lp2.muebleria2025.modelo.dao.Conexion;
import com.lp2.muebleria2025.modelo.dao.DetalleComprasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ComprasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ProveedorCrudImpl;
import com.lp2.muebleria2025.modelo.dao.TransaccionCrudImpl;
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
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author cmendieta
 */
public class ComprasController implements ActionListener , KeyListener {
    
    private GUIComprasF gui;
    private ComprasCrudImpl crud;
    Connection conec;
    
    private char operacion;
    Compras ventas = new Compras();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ProveedorCrudImpl crudProveedor = new ProveedorCrudImpl();
    DetalleComprasCrudImpl crud_dv = new DetalleComprasCrudImpl();
    UsuarioCrudImpl crudUser = new UsuarioCrudImpl();
    Transaccion transaccion = new Transaccion();
    private final TransaccionCrudImpl crudtr = new TransaccionCrudImpl();
    
    ComprasTablaModel modelo = new ComprasTablaModel();
    
    public ComprasController(GUIComprasF gui, ComprasCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_añadir.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.btn_generar_venta.addActionListener(this);
        //this.gui.txt_buscar.addKeyListener(this);
        
        gui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("INTENTO DE CIERRE"); 
                if (validarDetalle()==false) {
                    JOptionPane.showMessageDialog(null, "Elimine todos los productos añadidos antes de cerrar");
                    /*int confirmacion = JOptionPane.showConfirmDialog(
                        gui,
                        "Debe borrar todos los Productos añadidos.",
                        "Confirmar cierre",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );*/

                    /*if (confirmacion == JOptionPane.YES_OPTION) {
                        gui.dispose();
                        Integer idventa = Integer.valueOf(gui.txt_factura.getText());
                        crud_dv.eliminartodo(idventa);
                    }*/
                }else{
                    gui.dispose();
                }
            }
        });
        llenarComboProveedor(gui.cbo_proveedor);
        //llenarComboUsuario(gui.cbo_usuario);
        setFechaAct();
        operacion = 'N';
        gui.txt_subtotal.setVisible(false);
        gui.txt_descuento.setVisible(false);
        gui.btn_nuevo.setVisible(false);
        habilitarCampos(true);
        habilitarBoton(true);
        gui.txt_idusuario.setVisible(false);
        gui.cbo_usuario.setVisible(false);
        gui.lbl_nombre.setText(UsuarioCrudImpl.nombre+ " " + UsuarioCrudImpl.apellido);
        setUsuario();
        actVentaID();
        Conexion conectar = new Conexion();//Conexion a la BD
        conec = conectar.conectarBD();
        
        /*gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                ComprasTablaModel model = (ComprasTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setComprasForm(model.getComprasByRow(row));
            }
        });*/
        
        
        
        
        ////////listar("");
    }

    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    /*public void listar(String valorBuscado) {
        List<Compras> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        gui.tabla.updateUI();
    }*/
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        /*if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }*/
        /*if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            //limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.cbo_cliente.requestFocus();
        }*/
       
        
        if (e.getSource() == gui.btn_generar_venta) {
            
            Proveedor seleccionadoProveedor = (Proveedor) gui.cbo_proveedor.getSelectedItem();
            
            if ("Seleccionar Proveedor".equals(seleccionadoProveedor.getRazonSocial())) {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar un Proveedor Valido.");
                return;
            }
            
            if (validarDetalle()==true){
                JOptionPane.showMessageDialog(gui, "No se ha añadido ningún producto para comprar");
                return;
            }
            
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            System.out.println("Evento click de Generar Venta");
            
            int ok = JOptionPane.showConfirmDialog(gui,
                        "¿Realmente desea Cerrar la Compra?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    try {
                        crud.insertar(getComprasForm());
                        crudtr.insertar(getTransaccionForm());
                        
                        actVentaID();
                        
                        //generarTicket();
                        
                        
                        
                        gui.cbo_proveedor.requestFocus();
                        limpiar();
                    } catch (java.text.ParseException ex) {
                        Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            /*if (operacion == 'E') {
                try {
                    crud.actualizar(getComprasForm());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(ComprasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                habilitarCampos(false);
            }*/
            
            //limpiar();
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.cbo_proveedor.setEnabled(estado);
        gui.txt_fecha.setEnabled(estado);
        //gui.cbo_formapago.setEnabled(estado);
        
    }
    
      private void habilitarBoton(Boolean estado) {
        gui.btn_añadir.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
    }
    
    /*private void limpiar() {
        //gui.txt_fecha.setText("");
        //gui.txt_totalventas.setText("");
    }*/

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Compras getComprasForm() throws java.text.ParseException {
        //ventas.setNombre(gui.cbo_cliente.getText());
        try {
            Date fecha = sdf.parse(gui.txt_fecha.getText()); // Convierte el texto a un Date
            ventas.setFechaCompra(fecha); // Establece la fecha como un objeto Date
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null; // O maneja el error según lo necesario
        }
        ventas.setIdProveedor((Proveedor) gui.cbo_proveedor.getSelectedItem());
        ventas.setTotal(Integer.valueOf(gui.txt_totalventas.getText()));
        ventas.setIdUsuario(Integer.valueOf(gui.txt_idusuario.getText()));
        return ventas;
    }
    
    private Transaccion getTransaccionForm() throws java.text.ParseException {
        String num_venta = gui.txt_factura.getText();
        List<DetalleVentas> descripcionesList = crud_dv.listardescripcion(num_venta); //Lista de DetalleVentas
        String descripciones = descripcionesList.stream().map(DetalleVentas::getProducto).collect(Collectors.joining(", "));
        System.out.println("Concepto: "+descripciones);
        transaccion.setIdpago(Integer.valueOf(gui.txt_factura.getText()));
        transaccion.setMonto(Integer.valueOf(gui.txt_totalventas.getText()));
        transaccion.setTipo("Egreso");
        transaccion.setConcepto("Compra: "+descripciones);
        //transaccion.setForma_pago(gui.cbo_forma_pago.getSelectedItem().toString());
        return transaccion;
    }
    

    //Funcion o metodo encargado asignar valor los JTextField
    private void setComprasForm(Compras item) {
        System.out.println(item);
        ventas.setIdCompras(item.getIdCompras());
        gui.cbo_proveedor.setSelectedItem(item.getIdProveedor());
        //gui.cbo_formapago.setSelected(item.getMetodo_pago());
        gui.txt_fecha.setText(item.getFechaCompra().toString());
    }
    
    private boolean validarDatos(){
        boolean vacio = false;
        
        /*if(gui.txt_formadepago.getText().isEmpty()){
            vacio = true;
        }*/
        if(gui.txt_fecha.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_totalventas.getText().isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    private void setFechaAct() {
        // Define el formato deseado para la fecha (por ejemplo, dd/MM/yyyy)
        

        // Obtén la fecha actual
        Date fechaHoy = new Date();

        // Formatea la fecha como texto y escribe en el JTextField
        gui.txt_fecha.setText(sdf.format(fechaHoy));
    }

    private void llenarComboProveedor(JComboBox cbo){
        DefaultComboBoxModel<Proveedor> model = new DefaultComboBoxModel();
        List<Proveedor> lista = crudProveedor.listarcbo("");
        
        Proveedor seleccionar = new Proveedor();
            seleccionar.setRut("->");//Id especial
            seleccionar.setRazonSocial("Seleccionar Proveedor");
            //seleccionar.setApellido("Usuario");
            model.addElement(seleccionar);
        
        AutoCompleteDecorator.decorate(cbo);
        for (int i = 0; i < lista.size(); i++) {
            Proveedor proveedor = lista.get(i);
            model.addElement(proveedor);
        }
        cbo.setModel(model);
    }
    private void limpiar(){
        gui.txt_subtotal.setText("0");
        gui.txt_iva.setText("0");
        gui.txt_descuento.setText("0");
        gui.txt_totalventas.setText("0");
        gui.cbo_proveedor.setSelectedIndex(0);
    }
    
    
    private boolean validarDetalle () {
        boolean vacio = false; // Si la lista no esta esta vacia: false
        String num_venta = gui.txt_factura.getText();
        List<DetalleCompras> listadv = crud_dv.listar(num_venta); //Lista de DetalleCompras
        if (listadv.isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    public void generarTicket() {
        System.out.println("Generar Ticket");
        ComprasCrudImpl dao = new ComprasCrudImpl();
        // Obtener el número actual de la secuencia
        int numeroActual = dao.obtenerValorActualSecuencia();
        Integer venta_id_int = numeroActual;
        try {
            //String venta_id = gui.txt_factura.getText();
            System.out.println(venta_id_int);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("venta_id", venta_id_int); 
            JasperReport report = JasperCompileManager.compileReport(
                       "C:\\Users\\User\\Documents\\reportes\\ticket.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, conec);
            //JasperViewer.viewReport(print, false);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setAlwaysOnTop(true); //Muestra la ventana en frente
            viewer.setVisible(true);

            
        } catch (JRException e) {
            Logger.getLogger(GUIComprasF.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void setUsuario(){
        String iduser = UsuarioCrudImpl.idusuario.toString();
        gui.txt_idusuario.setText(iduser);
    }

    public void actVentaID(){
            // Crear instancia del DAO
        ComprasCrudImpl dao = new ComprasCrudImpl();
        // Obtener el número actual de la secuencia
        int numeroActual = dao.obtenerValorActualSecuencia();
        
        gui.txt_factura.setText(String.valueOf(numeroActual + 1)); // Asignarlo al JTextField
        
    }

        private void llenarComboUsuario(JComboBox cbo){
            DefaultComboBoxModel<Usuario> model = new DefaultComboBoxModel();
            List<Usuario> lista = crudUser.listar("");
            Usuario seleccionar = new Usuario();
            seleccionar.setId(0);//Id especial
            seleccionar.setNombre("Seleccionar");
            seleccionar.setApellido("Usuario");
            model.addElement(seleccionar);
            
            AutoCompleteDecorator.decorate(cbo);
            for (int i = 0; i < lista.size(); i++) {
                Usuario usuario = lista.get(i);
                model.addElement(usuario);
            }
            cbo.setModel(model);
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
}
