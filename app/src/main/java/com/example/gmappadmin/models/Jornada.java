package com.example.gmappadmin.models;

public class Jornada {
    private int id;
    private int id_cochera;
    private int total;
    private int cantidad_autos;
    private int cantidad_camionetas;
    private int cantidad_motos;
    private String fecha;

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCochera() {
        return id_cochera;
    }

    public void setIdCochera(int id_cochera) {
        this.id_cochera = id_cochera;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCantidadAutos() {
        return cantidad_autos;
    }

    public void setCantidadAutos(int cantidad_autos) {
        this.cantidad_autos = cantidad_autos;
    }

    public int getCantidadCamionetas() {
        return cantidad_camionetas;
    }

    public void setCantidadCamionetas(int cantidad_camionetas) {
        this.cantidad_camionetas = cantidad_camionetas;
    }

    public int getCantidadMotos() {
        return cantidad_motos;
    }

    public void setCantidadMotos(int cantidad_motos) {
        this.cantidad_motos = cantidad_motos;
    }
}
