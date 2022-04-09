package com.example.gmappadmin.api;

import com.example.gmappadmin.models.Cochera;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.Jornada;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Moth on 11/06/2021.
 */

public interface EntradasServicio {
    public static final String BASE_URL =
            "http://garagemapp.com/garagemapp-api_2/v1/";
    @POST("ingresos/register")
    Call<Ingreso> registrarIngreso(@Body Ingreso ingreso);

    @POST("jornadas/register")
    Call<Jornada> registrarJornada(@Body Jornada jornada);

}
