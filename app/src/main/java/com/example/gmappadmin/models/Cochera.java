package com.example.gmappadmin.models;

/**
 * Created by Ricardo on 27/08/2017.
 */

//Para trabajar con retrofit
public class Cochera {
    private int id;
    private String nombre;
    private String direccion;
    private float latitud;
    private float longitud;
    private float altitud;
    private char estado;
    private int lugares;
    private int tolerancia;

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

    public String getDireccion() {
        return direccion;
    }
    public float getAltitud() {
        return altitud;
    }

    public void setAltitud(float altitud) {
        this.altitud = altitud;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getLatitud() {
        return latitud;
    }
    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }
    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getLugares() {
        return lugares;
    }
    public void setLugares(int id) {
        this.lugares = lugares;
    }

    public char getEstado() {
        return estado;
    }
    public void setEstado(char estado) {
        this.estado = estado;
    }

    public int getTolerancia() {return tolerancia; }
    public void setTolerancia(int tolerancia) {this.tolerancia = tolerancia;  }

    @Override
    public String toString() {
        return id +
                "," + nombre +
                "," + direccion +
                "," + latitud +
                "," + longitud +
                "," + altitud +
                "," + estado +
                "," + lugares +
                "," + tolerancia +
                ";";
    }

    /*public String toString() {
        return "Cochera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", altitud=" + altitud +
                ", estado=" + estado +
                '}';
    }*/
}
