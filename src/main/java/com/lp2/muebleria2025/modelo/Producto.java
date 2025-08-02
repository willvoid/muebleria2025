/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo;

import java.util.Objects;

/**
 *
 * @author cmendieta
 */
public class Producto {
    private int id;
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    private Marca marca;
    private Iva iva;
    private Integer precio_compra;
    private String codigo;
    private Integer precio_descuento;
    private Integer comision;
    private Integer nro_cuotas;
    private Integer monto_cuotas;

    public Integer getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(Integer precio_compra) {
        this.precio_compra = precio_compra;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Iva getIva() {
        return iva;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    

    public void setIva(Iva iva) {
        this.iva = iva;
    }

    public Integer getPrecio_descuento() {
        return precio_descuento;
    }

    public void setPrecio_descuento(Integer precio_descuento) {
        this.precio_descuento = precio_descuento;
    }

    public Integer getComision() {
        return comision;
    }

    public void setComision(Integer comision) {
        this.comision = comision;
    }

    public Integer getNro_cuotas() {
        return nro_cuotas;
    }

    public void setNro_cuotas(Integer nro_cuotas) {
        this.nro_cuotas = nro_cuotas;
    }

    public Integer getMonto_cuotas() {
        return monto_cuotas;
    }

    public void setMonto_cuotas(Integer monto_cuotas) {
        this.monto_cuotas = monto_cuotas;
    }
    
    
    
    @Override
    public String toString() {
        return nombre;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto other = (Producto) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
