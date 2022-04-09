package com.example.gmappadmin.api;
import com.example.gmappadmin.models.Cochera;
import com.example.gmappadmin.models.CocheraConfig;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.Jornada;
import com.example.gmappadmin.models.RequestIngresoBody;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
/**
 * Created by Ricardo on 27/08/2017.
 */

public interface CocherasServicio {
    //Usando API saludmock-api
    public static final String BASE_URL =
            "http://garagemapp.com/garagemapp-api/";

    //Usando API garagemapp-api, luego cambiar a saludmock-api...
    /*public static final String BASE_URL =
    "https://sistemassinapsys.000webhostapp.com/garagemapp-api/";*/

    @GET("obtener_cocheras2.php")
    void getCocheras(Callback<ArrayList<Cochera>> callback);

    @POST("actualizar.php")
    Call<Cochera> registrarCambioEstado(@Body Cochera cochera);

    @POST("obtener_cochera_admin.php")
    Call<Cochera> obtenerCocheraAdmin(@Body Cochera cochera);

    @POST("obtener_cocheras2.php")
    Call<ArrayList<Cochera>> obtenerCocheras();
    //
    @GET("obtener_ingresos.php")
    Call<ArrayList<Ingreso>> obtenerIngresos();

    @POST("obtener_ingresos_estado.php")
    Call<ArrayList<Ingreso>> obtenerIngresosEstado(@Body String pagado);

    @POST("obtener_ingresos_estado_fecha.php")
    Call<ArrayList<Ingreso>> obtenerIngresosEstadoFecha(@Body RequestIngresoBody reqIngresoBody);

    @POST("actualizar_ingreso.php")
    Call<Ingreso> registrarCambioIngreso(@Body Ingreso ingreso);

    @POST("actualizar_tolerancia.php")
    Call<Cochera> registrarCambioTolerancia(@Body Cochera cochera);
    //
    @POST("obtener_config_cochera.php")
    Call<CocheraConfig> obtenerConfigCochera(@Body CocheraConfig cocheraTarifa);

    @POST("actualizar_config.php")
    Call<CocheraConfig> actualizarConfig(@Body CocheraConfig cocheraTarifa);

    @POST("actualizar_tarifa_tipo.php")
    Call<CocheraConfig> registrarCambioTarifa(@Body CocheraConfig cocheraTarifa);

    @POST("obtener_jornada_fecha.php")
    Call<Jornada> obtenerJornadaFecha(@Body Jornada jornada);
}
