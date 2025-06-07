/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo;

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
    
    @Override
    public String toString() {
        return nombre;
    }
    @Override
    public boolean equals(Object obj) {
        Integer cod1 = getId();
        Integer cod2 = ((Producto)obj).getId();
        if(cod1.equals(cod2)) return true;
        return false;
    }
}
