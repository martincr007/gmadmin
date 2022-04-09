package com.example.gmappadmin.models;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Moth on 11/06/2021.
 */

public class Ingreso implements Comparable<Ingreso>, Serializable {
    private int id;
    //private Date fecha_hora;
    private String fecha_hora;
    private String dominio;
    private int cantidad_horas;
    private char es_reserva;
    private Time hora;
    private char tipo_rodado;
    private char pagado;
    private int total;
    private int id_cochera;

    public int getIdCochera() {
        return id_cochera;
    }

    public void setIdCochera(int id_cochera) {
        this.id_cochera = id_cochera;
    }

    private static final long serialVersionUID = -6009011239477487992L;

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public char getPagado() {
        return pagado;
    }
    public void setPagado(char pagado) {
        this.pagado = pagado;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /*public Date getFechaHora() {
        return fecha_hora;
    }
    public void setFechaHora(Date fechaHora) {
        this.fecha_hora = fechaHora;
    }*/

    public String getFechaHora() {return fecha_hora;}
    public void setFechaHora(String fechaHora) {this.fecha_hora = fechaHora;}

    public String getDominio() {
        return dominio;
    }
    public void setDominio(String dominio) {this.dominio = dominio;}

    public int getCantidadHoras() {
        return this.cantidad_horas;
    }
    public void setCantidadHoras(int cant) {
        this.cantidad_horas = cant;
    }

    public char getEsReserva() {
        return this.es_reserva;
    }
    public void setEsReserva(char reserva) {
        this.es_reserva = reserva;
    }

    public char getTipoRodado() {
        return this.tipo_rodado;
    }
    public void setTipoRodado(char tipo_rodado) {
        this.tipo_rodado = tipo_rodado;
    }

    public Time getHora() {
        return this.hora;
    }
    public void setHora(Time hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return id +
                "," + fecha_hora +
                "," + dominio +
                "," + cantidad_horas +
                "," + es_reserva +
                "," + tipo_rodado +
                "," + hora +
                "," + pagado +
                ";";
    }

    @Override
    public int compareTo(Ingreso ingreso) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null, date2 = null;
        try {
                date1 = format.parse(fecha_hora);
                date2 = format.parse(ingreso.fecha_hora);
        } catch (ParseException e) {
                e.printStackTrace();
            }
        if (date1.compareTo(date2) > 0) {
            return -1;
        }
        if (date1.compareTo(date2) < 0) {
            return 1;
        }
        return 0;
    }
}
