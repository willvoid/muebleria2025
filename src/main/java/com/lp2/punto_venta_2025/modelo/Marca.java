/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.punto_venta_2025.modelo;

/**
 *
 * @author cmendieta
 */
public class Marca {
    private Integer id;
    private String nombre;

    public Marca() {
    }
    
    

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

    @Override
    public String toString() {
        return id + " - "+nombre;
    }

    @Override
    public boolean equals(Object obj) {
        Integer cod1 = getId();
        Integer cod2 = ((Marca)obj).getId();
        if(cod1.equals(cod2)) return true;
        return false;
    }
    
    
    
    
    
}
