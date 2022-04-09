package com.example.gmappadmin;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Cochera;
import com.example.gmappadmin.models.CocheraConfig;
import com.example.gmappadmin.models.SessionPrefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GestionActivity extends AppCompatActivity {
    ImageView imgGestionCochera, imgGestionIngresos,
            imgIngresos, imgGestionTarifas, imgTotales, imgGenQR;
    SharedPreferences preferences;
    private Retrofit retrofit;
    CocherasServicio cocherasService;
    private Call<Cochera> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion);
        // Redirección al Login
        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabClose);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffd5185a));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionPrefs.get(GestionActivity.this).logOut();
                Toast.makeText(GestionActivity.this,
                        "Sesión cerrada", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent( this, LoginActivity.class));
                finish();
            }
        });
        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels; // ancho absoluto en pixels
        int height = metrics.heightPixels; // alto absoluto en pixels*/
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        //
        imgGestionCochera = (ImageView)findViewById(R.id.imgBoton1);
        imgGestionIngresos = (ImageView)findViewById(R.id.imgGestionIngresos);
        imgGestionTarifas = (ImageView)findViewById(R.id.imgTarifas);
        imgIngresos = (ImageView)findViewById(R.id.imgIngresos);
        imgTotales = (ImageView)findViewById(R.id.imgTotales);
        imgGenQR = findViewById(R.id.imgQR);
        //asignamos listener onClick a los botones
        imgGestionCochera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                Intent intent = new Intent(GestionActivity.this, GestionCocheraActivity.class);
                intent.putExtra("caller", 1);
                startActivity(intent);
            }
        });
        imgIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                startActivity(new Intent(GestionActivity.this, IngresosActivity.class));
            }
        });
        imgGestionIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                startActivity(new Intent(GestionActivity.this, BottomMenuActivity.class));
            }
        });
        imgGestionTarifas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                startActivity(new Intent(GestionActivity.this, ConfigActivity.class));
            }
        });
        imgTotales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    return;
                }
                //comprobarCierreDiario();
                startActivity(new Intent(GestionActivity.this, TotalesActivity.class));
            }
        });
        imgGenQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GestionActivity.this, QrGenActivity.class));
            }
        });
        obtenerCochera();
        obtenerTarifas();
    }

    private void obtenerCochera()
    {
        Cochera cochera = new Cochera();
        cochera.setId(Integer.valueOf(SessionPrefs.get(this).idCocheraAdmin()));
        boolean cancel = false;
        call = cocherasService.obtenerCocheraAdmin(cochera);
        call.enqueue(new Callback<Cochera>() {
            @Override
            public void onResponse(Call<Cochera> call, Response<Cochera> response) {
                if (response.isSuccessful()) {
                    cochera.setNombre(response.body().getNombre());
                    cochera.setDireccion(response.body().getDireccion());
                    cochera.setLatitud(response.body().getLatitud());
                    cochera.setLongitud(response.body().getLongitud());
                    cochera.setEstado(response.body().getEstado());
                    cochera.setLugares(response.body().getLugares());
                    SessionPrefs.get(GestionActivity.this).saveCochera(cochera);
                }
            }
            @Override
            public void onFailure(Call<Cochera> call, Throwable t) {
                Toast.makeText(GestionActivity.this, "No se pudo realizar la operación", Toast.LENGTH_SHORT).show();
                //showProgress(false);
            }
        });
    }

    public void obtenerTarifas(){
        int id_cochera = Integer.valueOf(SessionPrefs.get(this).idCocheraAdmin());
        CocheraConfig cocheraConfig = new CocheraConfig();
        cocheraConfig.setIdCochera(id_cochera);
        Call<CocheraConfig> call2 = cocherasService.obtenerConfigCochera(cocheraConfig);
        call2.enqueue(new Callback<CocheraConfig>() {
            @Override
            public void onResponse(Call<CocheraConfig> call, Response<CocheraConfig> response) {
                if (response.isSuccessful()) {
                    CocheraConfig cocheraConfig = response.body();
                    SessionPrefs.get(GestionActivity.this).saveConfigCochera(cocheraConfig);
                }
            }
            @Override
            public void onFailure(Call<CocheraConfig> call, Throwable t) {
                Toast.makeText(GestionActivity.this, "No se pudo realizar la operación", Toast.LENGTH_SHORT).show();
                //showProgress(false);
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        /*String prueba = SessionPrefs.get(this).idCocheraAdmin();
        Toast.makeText(this, prueba, Toast.LENGTH_LONG).show();*/
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void goGestionCochera(View view){
        Intent intent = new Intent(GestionActivity.this, GestionCocheraActivity.class);
        intent.putExtra("caller", 1);
        startActivity(intent);
    }

    public void goIngresos(View view){
        //if (view.getId() == "tvCrearCenta") {
        startActivity(new Intent(this, IngresosActivity.class));
        //finish();  /*finish() destruye la Activity actual de manera que no se regresará a ella*/
    }

    public void goGestionIngresos(View view){
        //if (view.getId() == "tvCrearCenta") {
        startActivity(new Intent(this, BottomMenuActivity.class));
        //finish();  /*finish() destruye la Activity actual de manera que no se regresará a ella*/
    }

    public void goGestionTarifas(View view){
        //if (view.getId() == "tvCrearCenta") {
        startActivity(new Intent(this, ConfigActivity.class));
        //finish();  /*finish() destruye la Activity actual de manera que no se regresará a ella*/
    }

    public void goGestionTotales(View view){
        //comprobarCierreDiario();
        startActivity(new Intent(this, TotalesActivity.class));
    }

    public void goGenerarQR(View view){
        startActivity(new Intent(this, QrGenActivity.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (!SessionPrefs.get(this).isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }
}
