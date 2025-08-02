/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo;

/**
 *
 * @author HP
 */
public class DetalleVentas {
    private Integer id;
    private Integer idventa;
    private String producto;
    private Integer idproducto;
    private Integer cantidad;
    private Integer precio;
    public Integer subtotal;
    private Integer montoCuotas;
    private Integer nro_cuotas;
    private Integer montoPagado;
    private Integer interes;
    private String codigo;
    private Integer precio_descuento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    
    
    
    
    /**
     *
     * @return
     */
    

    public Integer getVenta() {
        return idventa;
    }

    public void setVenta(Integer venta) {
        this.idventa = venta;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getIdventa() {
        return idventa;
    }

    public void setIdventa(Integer idventa) {
        this.idventa = idventa;
    }

    public Integer getMontoCuotas() {
        return montoCuotas;
    }

    public void setMontoCuotas(Integer montoCuotas) {
        this.montoCuotas = montoCuotas;
    }

    public Integer getNro_cuotas() {
        return nro_cuotas;
    }

    public void setNro_cuotas(Integer nro_cuotas) {
        this.nro_cuotas = nro_cuotas;
    }

    public Integer getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Integer montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Integer getInteres() {
        return interes;
    }

    public void setInteres(Integer interes) {
        this.interes = interes;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getPrecio_descuento() {
        return precio_descuento;
    }

    public void setPrecio_descuento(Integer precio_descuento) {
        this.precio_descuento = precio_descuento;
    }
    
    
    
    
    @Override
public String toString() {
    return "DetalleVentas{" +
            "id=" + id +
            ", idproducto=" + idproducto +
            ", producto='" + producto + '\'' +
            ", precio=" + precio +
            ", cantidad=" + cantidad +
            ", subtotal=" + subtotal +
            ", venta=" + idventa +
            '}';
}

    /*public Object getId() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }*/
    
    

}
