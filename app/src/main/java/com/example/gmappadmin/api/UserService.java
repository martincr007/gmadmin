package com.example.gmappadmin.api;

import com.example.gmappadmin.models.Admin;
import com.example.gmappadmin.models.LoginBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Ricardo on 14/02/2018.
 */

public interface UserService {
    public static final String BASE_URL = "http://garagemapp.com/garagemapp-api_2/v1/";
    //public static final String BASE_URL = "https://sistemassinapsys.000webhostapp.com/saludmock-api/v1/";

    @POST("admins/login")
    Call<Admin> login(@Body LoginBody loginBody);

    @POST("admins/register")
    Call<Admin> registrarAdmin(@Body Admin admin);
}
