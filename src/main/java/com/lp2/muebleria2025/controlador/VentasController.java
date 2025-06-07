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
import com.lp2.muebleria2025.modelo.dao.Conexion;
import com.lp2.muebleria2025.modelo.dao.DetalleVentasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.UsuarioCrudImpl;
import com.lp2.muebleria2025.modelo.dao.VentasCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.VentasTablaModel;
import com.lp2.muebleria2025.vista.GUIVentasF;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
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
public class VentasController implements ActionListener , KeyListener {
    
    private GUIVentasF gui;
    private VentasCrudImpl crud;
    Connection conec;
    
    private char operacion;
    Ventas ventas = new Ventas();
    
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    ClienteCrudImpl crudCliente = new ClienteCrudImpl();
    DetalleVentasCrudImpl crud_dv = new DetalleVentasCrudImpl();
    
    VentasTablaModel modelo = new VentasTablaModel();
    
    public VentasController(GUIVentasF gui, VentasCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.gui.btn_añadir.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_nuevo.addActionListener(this);
        this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.btn_generar_venta.addActionListener(this);
        //this.gui.txt_buscar.addKeyListener(this);
        llenarComboCliente(gui.cbo_cliente);
        setFechaAct();
        operacion = 'N';
        habilitarCampos(true);
        habilitarBoton(true);
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
        if (e.getSource() == gui.btn_nuevo) {
            operacion = 'N';
            //limpiar();
            habilitarCampos(true);
             habilitarBoton(true);
            gui.cbo_cliente.requestFocus();
        }
       
        
        if (e.getSource() == gui.btn_generar_venta) {
            
            if (validarDetalle()==true){
                JOptionPane.showMessageDialog(gui, "No se ha añadido ningún producto para vender");
                return;
            }
            
            boolean v_control = validarDatos();
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            System.out.println("Evento click de Generar Venta");
            
            try {
                crud.insertar(getVentasForm());
                generarTicket();

                
                actVentaID();
            } catch (java.text.ParseException ex) {
                Logger.getLogger(VentasController.class.getName()).log(Level.SEVERE, null, ex);
                
            }

            gui.cbo_cliente.requestFocus();
            
            
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
        //ventas.setNombre(gui.cbo_cliente.getText());
        try {
            Date fecha = sdf.parse(gui.txt_fecha.getText()); // Convierte el texto a un Date
            ventas.setFechaVenta(fecha); // Establece la fecha como un objeto Date
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null; // O maneja el error según lo necesario
        }
        ventas.setIdCliente((Cliente) gui.cbo_cliente.getSelectedItem());
        ventas.setTotal(Integer.valueOf(gui.txt_totalventas.getText()));
        ventas.setIdUsuario(Integer.valueOf(gui.txt_idusuario.getText()));
        return ventas;
    }
    

    //Funcion o metodo encargado asignar valor los JTextField
    private void setVentasForm(Ventas item) {
        System.out.println(item);
        ventas.setIdVentas(item.getIdVentas());
        gui.cbo_cliente.setSelectedItem(item.getIdCliente());
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
        AutoCompleteDecorator.decorate(cbo);
        for (int i = 0; i < lista.size(); i++) {
            Cliente cliente = lista.get(i);
            model.addElement(cliente);
        }
        cbo.setModel(model);
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
        try {
            String venta_id = gui.txt_factura.getText();
            Integer venta_id_int = Integer.valueOf(venta_id);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("venta_id", venta_id_int); 
            JasperReport report = JasperCompileManager.compileReport(
                        "C:\\Users\\HP\\Documents\\NetBeansProjects\\punto_venta_2025\\src\\main\\java\\reportes\\ticket.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, conec);
            //JasperViewer.viewReport(print, false);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setAlwaysOnTop(true); //Muestra la ventana en frente
            viewer.setVisible(true);

            
        } catch (JRException e) {
            Logger.getLogger(GUIVentasF.class.getName()).log(Level.SEVERE, null, e);
        }
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
