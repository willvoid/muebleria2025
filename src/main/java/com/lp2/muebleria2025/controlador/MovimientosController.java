package com.lp2.muebleria2025.controlador;

import com.formdev.flatlaf.json.ParseException;
import com.lp2.muebleria2025.modelo.Transaccion;
import com.lp2.muebleria2025.modelo.dao.TransaccionCrudImpl;
import com.lp2.muebleria2025.modelo.dao.ProductoCrudImpl;
import com.lp2.muebleria2025.modelo.tabla.EgresosTablaModel;
import com.lp2.muebleria2025.modelo.tabla.IngresosTablaModel;
import com.lp2.muebleria2025.vista.GUIMovimientosF;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class MovimientosController implements ActionListener, KeyListener {

    private GUIMovimientosF gui;
    private TransaccionCrudImpl crud;
    private char operacion;
    Transaccion descuento = new Transaccion();
    ProductoCrudImpl crudPro = new ProductoCrudImpl();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    EgresosTablaModel modelo = new EgresosTablaModel();
    IngresosTablaModel modeloIngresos = new IngresosTablaModel();

    public MovimientosController(GUIMovimientosF gui, TransaccionCrudImpl crud) {
        this.gui = gui;
        this.crud = crud;

        this.gui.txt_buscar.addKeyListener(this);
        this.gui.txt_buscar.addActionListener(this);

        // Establecemos fechas de prueba por defecto
        /*gui.txt_desde.setText("01/01/2000");
        gui.txt_hasta.setText("31/12/2099");*/

        // Listeners para actualizar la tabla
        gui.txt_desde.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                listarConBusqueda();
            }
        });

        gui.txt_hasta.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                listarConBusqueda();
            }
        });

        gui.txt_buscar.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            public void onChange() {
                String valor = gui.txt_buscar.getText();
                    listarEgresos(valor);
                    listarIngresos(valor);
            }        
        });

        // Evento de selección en tabla
        /*gui.table_ingresos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable tabla = (JTable) e.getSource();
                int row = tabla.rowAtPoint(e.getPoint());
                EgresosTablaModel model = (EgresosTablaModel) tabla.getModel();
                // Aquí podrías usar model.getTransaccionByRow(row)
            }
        });*/

        // Deshabilita botones de calendario si fuera necesario
        gui.txt_desde.setEnabled(false);
        gui.txt_hasta.setEnabled(false);

        // Carga inicial de datos
        //listarEgresos("");
    }
    
    private void setTotalventas() throws java.text.ParseException{
        Date desde = StringToDate(gui.txt_desde, 1);
        Date hasta = StringToDate(gui.txt_hasta, 3);
        
        Integer total_ventas = crud.totalVentas(desde, hasta);
        Integer total_gastos = crud.totalGastos(desde, hasta);
        Integer balance = total_ventas - total_gastos;
        gui.lb_ventas.setText(total_ventas.toString());
        gui.lb_gastos.setText(total_gastos.toString());
        gui.lb_balance.setText(balance.toString());
    }

    public void mostrarVentana() {
        gui.setLocationRelativeTo(gui);
        gui.setVisible(true);
    }

    private void listarConBusqueda() {
        String valor = gui.txt_buscar.getText();
        if (!valor.isEmpty()) {
            listarEgresos(valor);
            listarIngresos(valor);
        } else {
            listarEgresos("");
            listarIngresos("");
        }
    }

    public void listarEgresos(String valorBuscado) {
        try {
            Date desde = StringToDate(gui.txt_desde, 1);
            Date hasta = StringToDate(gui.txt_hasta, 3);
            setTotalventas();
            List<Transaccion> lista = crud.listarEgresos(valorBuscado, desde, hasta);
            System.out.println("Buscando: '" + valorBuscado + "' desde: " + sdf.format(desde) + " hasta: " + sdf.format(hasta));
            System.out.println("Total resultados: " + lista.size());

            modelo.setLista(lista);
            gui.table_egresos.setModel(modelo);
            gui.table_egresos.updateUI();

        } catch (java.text.ParseException ex) {
            Logger.getLogger(MovimientosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void listarIngresos(String valorBuscado) {
        try {
            Date desde = StringToDate(gui.txt_desde, 1);
            Date hasta = StringToDate(gui.txt_hasta, 3);

            List<Transaccion> lista = crud.listarIngresos(valorBuscado, desde, hasta);
            System.out.println("Buscando: '" + valorBuscado + "' desde: " + sdf.format(desde) + " hasta: " + sdf.format(hasta));
            System.out.println("Total resultados: " + lista.size());

            modeloIngresos.setLista(lista);
            gui.table_ingresos.setModel(modeloIngresos);
            gui.table_ingresos.updateUI();

        } catch (java.text.ParseException ex) {
            Logger.getLogger(MovimientosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Evento click: " + e.getSource());
    }

    private Date StringToDate(JTextField txt, Integer i) throws java.text.ParseException {
        try {
            return sdf.parse(txt.getText());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha inválido. Usa el formato dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            if (i == 1) {
                gui.btn_calendar1.requestFocus();
            } else {
                gui.btn_calendar3.requestFocus();
            }
            return new Date(); // fecha actual como fallback
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    private abstract class DocumentChangeListener implements DocumentListener {
        public abstract void onChange();

        @Override
        public void insertUpdate(DocumentEvent e) { onChange(); }

        @Override
        public void removeUpdate(DocumentEvent e) { onChange(); }

        @Override
        public void changedUpdate(DocumentEvent e) { onChange(); }
    }
}
