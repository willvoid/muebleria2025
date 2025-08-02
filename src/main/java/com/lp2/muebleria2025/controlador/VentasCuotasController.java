/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.Cliente;
import com.lp2.muebleria2025.modelo.DetalleVentas;
import com.lp2.muebleria2025.modelo.Pagos;
import com.lp2.muebleria2025.modelo.Producto;
import com.lp2.muebleria2025.modelo.Transaccion;
import com.lp2.muebleria2025.modelo.Usuario;
import com.lp2.muebleria2025.modelo.Ventas;
import com.lp2.muebleria2025.modelo.dao.ClienteCrudImpl;
import com.lp2.muebleria2025.modelo.dao.Conexion;
import com.lp2.muebleria2025.modelo.dao.DetalleVentasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.PagosCrudImpl;
import com.lp2.muebleria2025.modelo.dao.TransaccionCrudImpl;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.dao.VentasCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.VentasTablaModel;
import com.lp2.muebleria2025.vista.GUIVentasCuotas;
import com.lp2.muebleria2025.vista.GUIVentasF;


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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
public class VentasCuotasController implements ActionListener , KeyListener {
    
    private GUIVentasCuotas gui;
    private VentasCrudImpl crud;
    Connection conec;
    
    private char operacion;
    Ventas ventas = new Ventas();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ClienteCrudImpl crudCliente = new ClienteCrudImpl();
    DetalleVentasCrudImpl crud_dv = new DetalleVentasCrudImpl();
    UsuarioCrudImpl crudUser = new UsuarioCrudImpl();
    private final PagosCrudImpl crudp = new PagosCrudImpl();
    Pagos pagos = new Pagos();
    Transaccion transaccion = new Transaccion();
    private final TransaccionCrudImpl crudtr = new TransaccionCrudImpl();
    
    VentasTablaModel modelo = new VentasTablaModel();
    
    public VentasCuotasController(GUIVentasCuotas gui, VentasCrudImpl crud) {
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
        llenarComboCliente(gui.cbo_cliente);
        //llenarComboUsuario(gui.cbo_usuario);
        
        setFechaAct();
        operacion = 'N';
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
                VentasTablaModel model = (VentasTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila

                setVentasForm(model.getVentasByRow(row));
            }
        });*/
        
        
        
        ////////listar("");
    }

    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    /*public void listar(String valorBuscado) {
        List<Ventas> lista = crud.listar(valorBuscado);
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
            
            List <DetalleVentas> detalleList = crud_dv.listar(gui.txt_factura.getText());
            
            Cliente seleccionadoCliente = (Cliente) gui.cbo_cliente.getSelectedItem();
            
            if ("Seleccionar Cliente".equals(seleccionadoCliente.getRazonSocial())) {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar un Cliente Valido.");
                gui.cbo_cliente.requestFocus();
                return;
            }
            
            if (gui.cbo_forma_pago.getSelectedIndex() == 0){
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una Forma de Pago.");
                gui.cbo_forma_pago.requestFocus();
                return;
            }
            
            if (detalleList.isEmpty()){
                JOptionPane.showMessageDialog(gui, "No se puede cerrar la venta sin ningún producto en la lista.");
                return;
            }
            
            if(gui.txt_montoc.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "El monto de las cuotas no debe estar vacío.");
                gui.txt_n_cuotas.requestFocus();
                return;
            }
            
            if(gui.txt_montop.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "El monto pagado no debe estar vacío.");
                gui.txt_montop.requestFocus();
                return;
            }
            
            if(gui.txt_n_cuotas.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "El número de Cuotas no debe estar vacío.");
                gui.txt_n_cuotas.requestFocus();
                return;
            }
            
            if(gui.txt_vencimiento.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "La Fecha de Vencimiento no debe estar vacía.");
                gui.btn_calendario.requestFocus();
                return;
            }
            
            String descuento = gui.txt_descuento_n.getText();
            if (descuento.isEmpty()){
                JOptionPane.showMessageDialog(gui, "El descuento no debe estar vacío.");
                gui.txt_descuento_n.requestFocus();
                return;
            }
            
            Integer descuento_p = Integer.valueOf(gui.txt_descuento_n.getText());

            Integer total = Integer.valueOf(gui.txt_totalventas.getText());
            if (descuento_p > total){
               JOptionPane.showMessageDialog(gui, "El descuento no puede ser mayor al Total.");
               gui.txt_descuento_n.requestFocus();
               return;
            }
            
            if (validarDetalle()==true){
                JOptionPane.showMessageDialog(gui, "No se ha añadido ningún producto para vender");
                return;
            }
            
            Integer pagado = Integer.valueOf(gui.txt_montop.getText());
            
            if (pagado > total){
                JOptionPane.showMessageDialog(gui, "La entrega no puede ser mayor a el total");
                gui.txt_montop.requestFocus();
                return;
            }
            
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos.");
                return;
            }
            
            String forma_pago = gui.cbo_forma_pago.getSelectedItem().toString();
            if ("Seleccionar".equals(forma_pago)) {
                JOptionPane.showMessageDialog(gui, "Favor seleccionar forma de pago.");
                return;
            }
            
            
            System.out.println("Evento click de Generar Venta");
            
            int ok = JOptionPane.showConfirmDialog(gui,
                        "¿Realmente desea Cerrar la Venta?", 
                        "Confirmar operacion", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    try {
                        crud.insertarcuotas(getVentasForm());
                        crudp.insertar(getPagosForm());
                        crudtr.insertar(getTransaccionForm());
                        actVentaID();
                        
                        generarTicket();
                        
                        gui.txt_totalventas.setEnabled(false);
                        
                        gui.cbo_cliente.requestFocus();
                        limpiar();
                    } catch (java.text.ParseException ex) {
                        Logger.getLogger(VentasCuotasController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            /*if (operacion == 'E') {
                try {
                    crud.actualizar(getVentasForm());
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
                }
                habilitarCampos(false);
            }*/
            
            //limpiar();
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.cbo_cliente.setEnabled(estado);
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
    private Ventas getVentasForm() throws java.text.ParseException {
        Integer Promocion = Integer.valueOf(gui.txt_descuento.getText());
        Integer DescuentoTotal = 0;
        String Descuento = gui.txt_descuento_n.getText();
        if (!Descuento.isEmpty()){
            Integer DescInt = Integer.valueOf(Descuento);
            DescuentoTotal = Promocion + DescInt;
        }else {
            DescuentoTotal = Promocion;
        }
        //ventas.setNombre(gui.cbo_cliente.getText());
        try {
            Date fecha = sdf.parse(gui.txt_fecha.getText()); // Convierte el texto a un Date
            ventas.setFechaVenta(fecha); // Establece la fecha como un objeto Date
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null; // O maneja el error según lo necesario
        }
        
        
        ventas.setMetodo_pago(gui.cbo_forma_pago.getSelectedItem().toString());
        ventas.setIdCliente((Cliente) gui.cbo_cliente.getSelectedItem());
        ventas.setTotal(Integer.valueOf(gui.txt_totalventas.getText()));
        ventas.setIdUsuario(Integer.valueOf(gui.txt_idusuario.getText()));
        ventas.setDescuento(DescuentoTotal);
        return ventas;
    }
    
    public Pagos getPagosForm() throws java.text.ParseException {
        try {
            Date fecha = sdf.parse(gui.txt_vencimiento.getText()); // Convierte el texto a un Date
            pagos.setVencimiento(fecha); // Establece la fecha como un objeto Date
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null; // O maneja el error según lo necesario
        }
         List<DetalleVentas> descripcionesList = crud_dv.listardescripcion(gui.txt_factura.getText()); //Lista de DetalleVentas
        
        String descripciones = descripcionesList.stream().map(DetalleVentas::getProducto).collect(Collectors.joining(", "));
        
        pagos.setIdcliente((Cliente)gui.cbo_cliente.getSelectedItem());
        pagos.setIdventa(Integer.valueOf(gui.txt_factura.getText()));
        pagos.setMontocuota(Integer.valueOf(gui.txt_montoc.getText()));
        pagos.setPagado(Integer.valueOf(gui.txt_montop.getText()));
        pagos.setTotal(Integer.valueOf(gui.txt_totalventas.getText()));
        pagos.setTotal_cuotas(Integer.valueOf(gui.txt_n_cuotas.getText()));
        pagos.setProducto(descripciones);
        return pagos;
    }
    
    private Transaccion getTransaccionForm() throws java.text.ParseException {
        String num_venta = gui.txt_factura.getText();
        List<DetalleVentas> descripcionesList = crud_dv.listardescripcion(num_venta); //Lista de DetalleVentas
        String descripciones = descripcionesList.stream().map(DetalleVentas::getProducto).collect(Collectors.joining(", "));
        System.out.println("Concepto: "+descripciones);
        transaccion.setIdpago(Integer.valueOf(gui.txt_factura.getText()));
        transaccion.setMonto(Integer.valueOf(gui.txt_montop.getText()));
        transaccion.setTipo("Ingreso");
        transaccion.setConcepto("Venta Financiada: " + descripciones);
        transaccion.setForma_pago(gui.cbo_forma_pago.getSelectedItem().toString());
        return transaccion;
    }

    //Funcion o metodo encargado asignar valor los JTextField
    private void setVentasForm(Ventas item) {
        System.out.println(item);
        ventas.setIdVentas(item.getIdVentas());
        gui.cbo_cliente.setSelectedItem(item.getIdCliente());
        gui.cbo_forma_pago.setSelectedItem(item.getMetodo_pago());
        //gui.cbo_formapago.setSelected(item.getMetodo_pago());
        gui.txt_fecha.setText(item.getFechaVenta().toString());
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

    private void llenarComboCliente(JComboBox cbo){
        DefaultComboBoxModel<Cliente> model = new DefaultComboBoxModel();
        List<Cliente> lista = crudCliente.listarcbo("");
        
        Cliente seleccionar = new Cliente();
            seleccionar.setRut("->");//Id especial
            seleccionar.setRazonSocial("Seleccionar Cliente");
            //seleccionar.setApellido("Usuario");
            model.addElement(seleccionar);
        
        AutoCompleteDecorator.decorate(cbo);
        for (int i = 0; i < lista.size(); i++) {
            Cliente cliente = lista.get(i);
            model.addElement(cliente);
        }
        cbo.setModel(model);
    }
    private void limpiar(){
        gui.txt_subtotal.setText("0");
        gui.txt_iva.setText("0");
        gui.txt_descuento.setText("0");
        gui.txt_totalventas.setText("0");
        gui.cbo_cliente.setSelectedIndex(0);
        gui.txt_saldo.setText("");
        gui.txt_montop.setText("");
    }
    
    
    private boolean validarDetalle () {
        boolean vacio = false; // Si la lista no esta esta vacia: false
        String num_venta = gui.txt_factura.getText();
        List<DetalleVentas> listadv = crud_dv.listar(num_venta); //Lista de DetalleVentas
        if (listadv.isEmpty()){
            vacio = true;
        }
        return vacio;
    }
    
    public void generarTicket() {
        System.out.println("Generar Ticket");
            VentasCrudImpl dao = new VentasCrudImpl();
            // Obtener el número actual de la secuencia
            int numeroActual = dao.obtenerValorActualSecuencia();
            Integer venta_id_int = numeroActual;
            try {
                //String venta_id = gui.txt_factura.getText();
                System.out.println(venta_id_int);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("SUBREPORT_DIR", "C:\\reportes\\");
                parameters.put("venta_id", numeroActual); 
                JasperReport report = JasperCompileManager.compileReport(
                           "C:\\reportes\\ticket_mueble_cuotas.jrxml");
                JasperPrint print = JasperFillManager.fillReport(report, parameters, conec);
                //JasperViewer.viewReport(print, false);
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.setAlwaysOnTop(true); //Muestra la ventana en frente
                viewer.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(GUIVentasF.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    private Integer CalcularDescVentaGral(){
        List<DetalleVentas> listapro = crud_dv.listar(gui.txt_factura.getText());
        Integer descuento_gral = 0;
        for (DetalleVentas item : listapro){
            descuento_gral += item.getPrecio_descuento();
        }
        System.out.println("Descuento precio gral: " + descuento_gral);
        return descuento_gral;
    }
    
    private void setUsuario(){
        String iduser = UsuarioCrudImpl.idusuario.toString();
        gui.txt_idusuario.setText(iduser);
    }

    public void actVentaID(){
            // Crear instancia del DAO
        VentasCrudImpl dao = new VentasCrudImpl();
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
