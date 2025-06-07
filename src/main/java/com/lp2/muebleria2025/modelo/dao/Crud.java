/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.lp2.muebleria2025.modelo.dao;

import java.util.List;

/**
 *
 * @author HP
 */
public interface Crud<T> {
    public void insertar(T obj);
    public void actualizar(T obj);
    public void eliminar(T obj);
    public List<T> listar(String t);
}
