package com.example.gmappadmin.models;

//import com.google.gson.annotations.SerializedName;

/**
 * Objeto plano Java para representar el cuerpo de la petición POST /affiliates/login
 */
public class LoginBody {
    //@SerializedName("id")
    private String username;
    private String password;

    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUserId() {
        return username;
    }

    public void setUserId(String userId) {
        this.username = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
