package com.example.gmappadmin.models;

public class CocheraConfig {
    private int id;
    private int id_cochera;
    private int tarifa_auto;
    private int tarifa_camioneta;
    private int tarifa_moto;
    private int tolerancia;
    //estos dos atributos son para
    //ue funcione la actualización individual
    private String tipo;
    private int tarifa;
    private int hora_cierre;
    private String email;

    public String getEmail() {return email; }
    public void setEmail(String email) {this.email = email;}

    public int getHoraCierre() {return hora_cierre;}
    public void setHoraCierre(int horaCierre) {this.hora_cierre = horaCierre;}

    /*public int getTarifa() {
        return tarifa;
    }
    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }*/

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

    public int getTarifaAuto() {
        return tarifa_auto;
    }
    public void setTarifaAuto(int tarifa_auto) {
        this.tarifa_auto = tarifa_auto;
    }

    public int getTarifaCamioneta() {
        return tarifa_camioneta;
    }
    public void setTarifaCamioneta(int tarifa_camioneta) {
        this.tarifa_camioneta = tarifa_camioneta;
    }

    public int getTarifaMoto() {
        return tarifa_moto;
    }
    public void setTarifaMoto(int tarifa_moto) {
        this.tarifa_moto = tarifa_moto;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTolerancia() {return tolerancia; }
    public void setTolerancia(int tolerancia) {this.tolerancia = tolerancia;  }

}
