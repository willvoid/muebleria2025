/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lp2.muebleria2025.modelo;

import java.util.Date;

/**
 *
 * @author HP
 */
public class Pagos {
    private Integer idpagos;
    private Cliente idcliente;
    private Integer idventa;
    private Integer montocuota;
    private Integer pagado;
    private Integer total;
    private Date vencimiento;
    private String estado;
    private Integer saldo;
    private String producto;
    private Integer total_cuotas;
    private Integer cuotas_pagadas;

    public Integer getIdpagos() {
        return idpagos;
    }

    public void setIdpagos(Integer idpagos) {
        this.idpagos = idpagos;
    }

    public Cliente getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(Cliente idcliente) {
        this.idcliente = idcliente;
    }
        
    public Integer getIdventa() {
        return idventa;
    }

    public void setIdventa(Integer idventa) {
        this.idventa = idventa;
    }

    public Integer getMontocuota() {
        return montocuota;
    }

    public void setMontocuota(Integer montocuota) {
        this.montocuota = montocuota;
    }

    public Integer getPagado() {
        return pagado;
    }

    public void setPagado(Integer pagado) {
        this.pagado = pagado;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getTotal_cuotas() {
        return total_cuotas;
    }

    public void setTotal_cuotas(Integer total_cuotas) {
        this.total_cuotas = total_cuotas;
    }

    public Integer getCuotas_pagadas() {
        return cuotas_pagadas;
    }

    public void setCuotas_pagadas(Integer cuotas_pagadas) {
        this.cuotas_pagadas = cuotas_pagadas;
    }
    
}
