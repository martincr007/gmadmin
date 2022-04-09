package com.example.gmappadmin.models;

/**
 * Created by Ricky on 10/02/2018.
 */
public class Admin {
    private int id;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String email;
    private int idcochera;
    private int admintipo;

    public Admin(int id, String nombre, String apellido, String userName,
                 String password, String email, int idcochera, int admintipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = userName;
        this.password = password;
        this.email = email;
        this.idcochera = idcochera;
        this.admintipo = admintipo;
    }

    public Admin() {

    }

    public int getId() { return id;  }
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUserName() {
        return username;
    }
    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdCochera() { return idcochera;  }
    public void setIdCochera(int idCochera) {
        this.idcochera = idCochera;
    }

    public int getAdmintipo() {
        return admintipo;
    }

    public void setAdmintipo(int admintipo) {
        this.admintipo = admintipo;
    }
}
