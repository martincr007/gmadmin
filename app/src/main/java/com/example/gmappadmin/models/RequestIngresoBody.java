package com.example.gmappadmin.models;

public class RequestIngresoBody {
    private String pagado;
    private String fecha;
    private int id_cochera;

    public RequestIngresoBody(String pagado, String fecha, int id_cochera) {
        this.pagado = pagado;
        this.fecha = fecha;
        this.id_cochera = id_cochera;
    }

    public String getPagado() {
        return pagado;
    }
    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_cochera() {return id_cochera; }
    public void setId_cochera(int id_cochera) {this.id_cochera = id_cochera; }
}
