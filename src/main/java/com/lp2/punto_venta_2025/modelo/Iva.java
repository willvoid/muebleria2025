/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo;

/**
 *
 * @author cmendieta
 */
public class Iva {
    
    private Integer id;
    private String nombre;
    private Integer valor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    public boolean equals(Object obj) {
        Integer cod1 = getId();
        Integer cod2 = ((Iva)obj).getId();
        if(cod1.equals(cod2)) return true;
        return false;
    }
    
}
