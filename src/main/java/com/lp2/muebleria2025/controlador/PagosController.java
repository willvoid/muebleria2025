/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.DetalleVentas;
import com.lp2.muebleria2025.modelo.Pagos;
import com.lp2.muebleria2025.modelo.Transaccion;
import com.lp2.muebleria2025.modelo.dao.Conexion;
import com.lp2.muebleria2025.modelo.dao.DetalleVentasCrudImpl;
import com.lp2.muebleria2025.modelo.dao.PagosCrudImpl;
import com.lp2.muebleria2025.modelo.dao.TransaccionCrudImpl;
import com.lp2.muebleria2025.modelo.dao.VentasCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.EstadoCellRenderer;
import com.lp2.muebleria2025.modelo.tabla.PagosTablaModel;
import com.lp2.muebleria2025.vista.GUIPagosF;
import com.lp2.muebleria2025.vista.GUIVentasCuotas;
import com.lp2.muebleria2025.vista.GUIVentasF;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

/**
 *
 * @author cmendieta
 */
public class PagosController implements ActionListener , KeyListener {
    
    Connection conec;
    private GUIPagosF gui;
    private GUIVentasCuotas guiv;
    private PagosCrudImpl crud;
    private TransaccionCrudImpl crudtr = new TransaccionCrudImpl();
    private CalendarioController calendario;
    private int idpagos = 0;
    private char operacion;
    Pagos pagos = new Pagos();
    Transaccion transaccion = new Transaccion();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    PagosTablaModel modelo = new PagosTablaModel();
    DetalleVentasCrudImpl crud_dv = new DetalleVentasCrudImpl();
    
    public PagosController(GUIPagosF gui, PagosCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;
        this.crudtr = crudtr;
        this.gui.btn_guardar.addActionListener(this);
        this.gui.btn_cancelar.addActionListener(this);
        this.gui.btn_cobrar.addActionListener(this);
        //this.gui.btn_editar.addActionListener(this);
        this.gui.btn_eliminar.addActionListener(this);
        this.gui.txt_buscar.addKeyListener(this);
        this.gui.btn_ticket.addActionListener(this);
        
        gui.txt_vencimiento.setEnabled(false);
        gui.txt_idventa.setVisible(false);
        gui.txt_cuotas_pagadas.setVisible(false);
        
        gui.txt_abono.getDocument().addDocumentListener(new PagosController.DocumentChangeListener() {
            @Override
            public void onChange() {
                
                updateMontoPagado();
                
                
            }
        });
        
        gui.tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                PagosTablaModel model = (PagosTablaModel) tabla.getModel();
                //Devolver el objeto seleccionado en la fila
                setPagosForm(model.getPagosByRow(row));
                idpagos = model.getPagosByRow(row).getIdpagos();
            }
        });
        gui.txt_idpagos.setVisible(false);
        gui.txt_total_cuotas.setVisible(false);
        habilitarCampos(false);
        habilitarBoton(false);
        gui.txt_vencimiento.setVisible(false);
        gui.btn_calendar.setVisible(false);
        gui.txt_abono.setEnabled(false);
        Conexion conectar = new Conexion();//Conexion a la BD
        gui.btn_eliminar.setVisible(false);
        gui.btn_eliminar.setEnabled(false);
        conec = conectar.conectarBD();
        listar("");
    }
    
    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }
    
    public void listar(String valorBuscado) {
        List<Pagos> lista = crud.listar(valorBuscado);
        modelo.setLista(lista);
        gui.tabla.setModel(modelo);
        
        // Asignar el renderizador personalizado a la columna "ESTADO" (índice 10)
        gui.tabla.getColumnModel().getColumn(12).setCellRenderer(new EstadoCellRenderer());
        
        gui.tabla.updateUI();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click");
        
        if(e.getSource()== gui.txt_buscar){
            String valor = gui.txt_buscar.getText();
            listar(valor);
        }
        
        if (e.getSource()== gui.btn_ticket){
            generarTicket();
        }
        
        if (e.getSource() == gui.btn_cobrar) {
            String saldotxt = gui.txt_saldo.getText();
            Integer saldo = Integer.valueOf(saldotxt);
            if (saldo == 0){
                JOptionPane.showMessageDialog(gui, "Esta cuota ya fue Pagada totalmente.");
                return;
            }
            
            int fila = gui.tabla.getSelectedRow();
            boolean s = validarDatos();
            if (fila >= 0 && s == false) {
                operacion = 'C';
                //gui.txt_vencimiento.setText("");
                //setFechaAct();
                habilitarCamposCobrar(true);
                habilitarBoton(true);
                updateMontoPagado();
                //gui.btn_calendar.requestFocus();
            } else {
                JOptionPane.showMessageDialog(gui, "Debe seleccionar una fila");
            }
            
            
        }
        /*if (e.getSource() == gui.btn_editar) {
            operacion = 'E';
            habilitarCampos(true);
             habilitarBoton(true);
            
        }*/
        /*if (e.getSource() == gui.btn_calendar){
            Calendario guicalen = new Calendario (null, true);
            CalendarioController calenCtrl = new CalendarioController(guicalen, gui.txt_vencimiento);
            calenCtrl.mostrarVentana();
        }*/
        
        if (e.getSource() == gui.btn_eliminar) {
            int fila = gui.tabla.getSelectedRow();
            if (fila >= 0) {
                int ok = JOptionPane.showConfirmDialog(gui, "Realmente desea elimnar el registro?", "Confirmar operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                    crud.eliminar(modelo.getPagosByRow(fila));
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
            if(gui.txt_abono.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "Favor completar el Abono");
                gui.txt_abono.requestFocus();
                return;
            }
            String forma_pago = gui.cbo_forma_pago.getSelectedItem().toString();
            if ("Seleccionar".equals(forma_pago)) {
                JOptionPane.showMessageDialog(gui, "Favor seleccionar forma de pago.");
                return;
            }
            
            String abonotxt = gui.txt_abono.getText();
            int abonoint = Integer.parseInt(abonotxt);
            int saldo = Integer.parseInt(gui.txt_saldo.getText());
            if (abonoint > saldo){
                JOptionPane.showMessageDialog(gui, "El Abono no puede ser mayor que el Saldo");
                return;
            }
            
            if (abonoint <= 0){
                JOptionPane.showMessageDialog(gui, "El Abono no puede ser Cero o Menor a Cero");
                return;
            }
            
            
            if(gui.txt_vencimiento.getText().isEmpty()){
                JOptionPane.showMessageDialog(gui, "Favor completar la Fecha de Vencimiento");
                gui.txt_vencimiento.requestFocus();
                return;
            }
            if(v_control == true){
                JOptionPane.showMessageDialog(gui, "Favor completar los datos");
                return;
            }
            System.out.println("Evento click de guardar");
            if (operacion == 'C') {
                int ok = JOptionPane.showConfirmDialog(gui, "Verifique que el abono sea correcto.\n¿Confirma que los datos son correctos para cobrar esta cuota?", "Confirmar operacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ok == 0) {
                     try {
                        int montop = Integer.parseInt(gui.txt_pagado.getText());
                        int idventa = Integer.parseInt(gui.txt_idventa.getText());
                        crud.cobrar(getPagosForm());
                        crudtr.insertar(getTransaccionForm());
                        crud_dv.actualizarmontopagado(montop, idventa);
                    } catch (java.text.ParseException ex) {
                        Logger.getLogger(PagosController.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    crud.actualizar_estados();
                    generarTicket();
                }
            }
            
            System.out.println("Evento click de guardar");
            if (operacion == 'N') {
                try {
                    crud.insertar(getPagosForm());
                    
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(PagosController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
            
            if (operacion == 'E') {
                
                try {
                    crud.actualizar(getPagosForm());
                    
                    habilitarCampos(false);
                } catch (java.text.ParseException ex) {
                    Logger.getLogger(PagosController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            listar("");
            limpiar();
            habilitarCampos(false);
            habilitarBoton(false);
        }
        System.out.println(operacion);
    }

    // Metodo encargado de habilitar o deshabilitar los campos
    private void habilitarCampos(Boolean estado) {
        gui.txt_vencimiento.setEnabled(estado);
        gui.txt_montoc.setEnabled(estado);
        gui.txt_pagado.setEnabled(estado);
        gui.txt_total.setEnabled(estado); 
        gui.txt_saldo.setEnabled(estado);
        gui.txt_abono.setEnabled(estado);
        gui.cbo_forma_pago.setEnabled(estado);
    }
    
    private void habilitarCamposCobrar(Boolean estado) {
        gui.txt_vencimiento.setEnabled(estado);
        gui.txt_abono.setEnabled(estado);
        gui.cbo_forma_pago.setEnabled(estado);
    }
    
    private void habilitarBoton(Boolean estado) {
        gui.btn_guardar.setEnabled(estado);
        gui.btn_cancelar.setEnabled(estado);
        gui.btn_calendar.setEnabled(estado);
    }
    
    private void limpiar() {
        gui.txt_vencimiento.setText("");
        gui.txt_montoc.setText("");
        gui.txt_pagado.setText("");
        gui.txt_total.setText("");
        gui.txt_vencimiento.setText("");
        gui.txt_saldo.setText("");
        gui.txt_abono.setText("");
        gui.txt_idventa.setText("");
        gui.cbo_forma_pago.setSelectedIndex(0);
    }
    
    private void generarTicket(){
        System.out.println("Generar Ticket");
            // Obtener el número actual de la secuencia
            Integer Id_pago = Integer.valueOf(gui.txt_idpagos.getText());
            //Integer venta_id_int = numeroActual;
            try {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("pago_id", Id_pago); 
                JasperReport report = JasperCompileManager.compileReport(
                           "C:\\reportes\\pagos.jrxml");
                JasperPrint print = JasperFillManager.fillReport(report, parameters, conec);
                //JasperViewer.viewReport(print, false);
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.setAlwaysOnTop(true); //Muestra la ventana en frente
                viewer.setVisible(true);
            } catch (JRException ex) {
                Logger.getLogger(GUIVentasF.class.getName()).log(Level.SEVERE, null, ex);

            }
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Pagos getPagosForm() throws java.text.ParseException {
                
        try {
            Date fecha = sdf.parse(gui.txt_vencimiento.getText()); // Convierte el texto a un Date
            pagos.setVencimiento(fecha); // Establece la fecha como un objeto Date
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        pagos.setMontocuota(Integer.valueOf(gui.txt_montoc.getText()));
        pagos.setPagado(Integer.valueOf(gui.txt_pagado.getText()));
        pagos.setTotal(Integer.valueOf(gui.txt_total.getText()));
        pagos.setSaldo(Integer.valueOf(gui.txt_saldo.getText()));
        pagos.setCuotas_pagadas(Integer.valueOf(gui.txt_cuotas_pagadas.getText()));
        return pagos;
    }

    //Funcion o metodo encargado asignar valor los JTextField
    private void setPagosForm(Pagos item) {
        System.out.println(item);
        pagos.setIdpagos(item.getIdpagos());
        gui.txt_idpagos.setText(item.getIdpagos().toString());
        gui.txt_montoc.setText(item.getMontocuota().toString());
        gui.txt_abono.setText(item.getMontocuota().toString());
        gui.txt_pagado.setText(item.getPagado().toString());
        gui.txt_total.setText(item.getTotal().toString());
        gui.txt_saldo.setText(item.getSaldo().toString());
        gui.txt_cuotas_pagadas.setText(item.getCuotas_pagadas().toString());
        gui.txt_total_cuotas.setText(item.getTotal_cuotas().toString());
        // Convierte la fecha a String en formato dd/MM/yyyy antes de asignarla al JTextField
        if (item.getVencimiento() != null) {
            java.util.Date fecha = new java.util.Date(item.getVencimiento().getTime()); // Convertir de java.sql.Date a java.util.Date
            gui.txt_vencimiento.setText(sdf.format(fecha)); // Usa el formato deseado
        } else {
            gui.txt_vencimiento.setText(""); // Limpia el campo si no hay fecha
        }
        gui.txt_idventa.setText(item.getIdventa().toString());
    }

    // funcion o metodo encargado de recuperrar los valores de los JTextField en un objeto
    private Transaccion getTransaccionForm() throws java.text.ParseException {
        DetalleVentasCrudImpl crud_dvv = new DetalleVentasCrudImpl();
         String num_venta = gui.txt_idventa.getText();
        List<DetalleVentas> descripcionesList = crud_dvv.listardescripcion(num_venta); //Lista de DetalleVentas
        String descripciones = descripcionesList.stream().map(DetalleVentas::getProducto).collect(Collectors.joining(", "));
        System.out.println("Concepto: "+descripciones);
        transaccion.setIdpago(Integer.valueOf(gui.txt_idpagos.getText()));
        transaccion.setMonto(Integer.valueOf(gui.txt_abono.getText()));
        transaccion.setTipo("Ingreso");
        transaccion.setConcepto("Pago de Cuota: "+descripciones);
        transaccion.setForma_pago(gui.cbo_forma_pago.getSelectedItem().toString());
        return transaccion;
    }
    
    private void updateMontoPagado(){
        String ab = gui.txt_abono.getText();
        int fila = gui.tabla.getSelectedRow();
        Pagos detalle = modelo.getPagosByRow(fila);
       int pagado = crud.obtenerPagado(detalle);
       String abno = gui.txt_abono.getText();
       if (!abno.isEmpty()){
           try{
                int abono = Integer.parseInt(ab);
                gui.txt_pagado.setText(String.valueOf(pagado + abono)); 
           }catch(NumberFormatException ex){
               gui.txt_pagado.setText(String.valueOf(pagado));
           }  
       }else{
           gui.txt_pagado.setText(String.valueOf(pagado));
       }
    }
    /*private void setFechaAct() {
        // Define el formato deseado para la fecha (por ejemplo, dd/MM/yyyy)
        

        // Obtén la fecha actual
        Date fechaHoy = new Date();

        // Formatea la fecha como texto y escribe en el JTextField
        gui.txt_vencimiento.setText(sdf.format(fechaHoy));
    }*/
    
    private boolean validarDatos(){
        boolean vacio = false;
        if(gui.txt_montoc.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_pagado.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_saldo.getText().isEmpty()){
            vacio = true;
        }
        if(gui.txt_total.getText().isEmpty()){
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
