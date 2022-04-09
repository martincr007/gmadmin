package com.example.gmappadmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Cochera;
import com.example.gmappadmin.models.SessionPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GestionCocheraActivity extends AppCompatActivity {

    Button btnSetDisponible, btnSetOcupado;
    TextView tvNombre, tvNombreCochera, tvDireccionCochera, tvEstadoCochera;
    CocherasServicio cocherasService;
    private Retrofit retrofit;
    private Cochera cochera;
    private View mProgressView;
    private ImageView mLogoView;
    private View mLoginFormView;
    //
    Call<Cochera> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cochera);
        btnSetDisponible = (Button)findViewById(R.id.btnSetDisponible);
        btnSetOcupado = (Button)findViewById(R.id.btnSetOcupado);
        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvNombreCochera = (TextView)findViewById(R.id.tvNombreCochera);
        tvDireccionCochera = (TextView)findViewById(R.id.tvDireccionCochera);
        tvEstadoCochera = (TextView)findViewById(R.id.tvEstadoCochera);
        mLogoView = (ImageView) findViewById(R.id.image_logo);
        mProgressView = findViewById(R.id.bar_progress);
        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        mLoginFormView = findViewById(R.id.gestion_form);
        //Obtengo info de la cochera asociada al admin logueado
        //ObtenerCochera();
        mostrarInfoCochera();
        //Muestro info del admin y  la cochera
        tvNombre.setText(SessionPrefs.get(this).nombreAdmin() + " " +SessionPrefs.get(this).apellidoAdmin());
        //tvIdCochera.setText(SessionPrefs.get(this).idCocheraAdmin());
        //
        btnSetDisponible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                SetDisponible();
            }
        });

        btnSetOcupado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    //showLoginError(getString(R.string.error_network));
                    return;
                }
                SetOcupado();
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void mostrarInfoCochera(){
        showProgress(true);
        cochera = new Cochera();
        cochera.setId(Integer.valueOf(SessionPrefs.get(this).idCocheraAdmin()));
        cochera.setEstado(SessionPrefs.get(GestionCocheraActivity.this).estadoCochera().charAt(0));
        tvDireccionCochera.setText(SessionPrefs.get(GestionCocheraActivity.this).nombreCochera());
        tvNombreCochera.setText(SessionPrefs.get(GestionCocheraActivity.this).direccionCochera());
        if (cochera.getEstado()=='D'){
            tvEstadoCochera.setText("Estado: Lugares disponibles");
            btnSetDisponible.setEnabled(false);
            btnSetDisponible.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this,
                    R.drawable.boton_round_gray));
            btnSetOcupado.setEnabled(true);
            btnSetOcupado.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this,
                    R.drawable.boton_round_red));
        }
        else{
            tvEstadoCochera.setText("Estado: Sin lugares");
            btnSetOcupado.setEnabled(false);
            btnSetOcupado.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this,
                    R.drawable.boton_round_gray));
            btnSetDisponible.setEnabled(true);
            btnSetDisponible.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this,
                    R.drawable.boton_round_blue));
            tvEstadoCochera.setText("Estado: Sin lugares");
        }
        showProgress(false);
    }
    
    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        int visibility = show ? View.GONE : View.VISIBLE;
        mLogoView.setVisibility(visibility);
        mLoginFormView.setVisibility(visibility);
    }

    private void SetDisponible()
    {
        /*Cochera cochera = new Cochera();
        cochera.setId(1);*/
        cochera.setEstado('D');
        boolean cancel = false;
        Call<Cochera> call = cocherasService.registrarCambioEstado(cochera);
        call.enqueue(new Callback<Cochera>() {
            @Override
            public void onResponse(Call<Cochera> call, Response<Cochera> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GestionCocheraActivity.this, "Se cambio el estado a DISPONIBLE", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(AdminAccountActivity.this, GestionActivity.class);
                    startActivity(intent);*/
                    btnSetDisponible.setEnabled(false);
                    btnSetDisponible.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this, R.drawable.boton_round_gray));
                    btnSetOcupado.setEnabled(true);
                    btnSetOcupado.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this, R.drawable.boton_round_red));
                    tvEstadoCochera.setText("Estado: Lugares disponibles");
                    SessionPrefs.get(GestionCocheraActivity.this).saveEstadoCochera('D');
                }
            }
            @Override
            public void onFailure(Call<Cochera> call, Throwable t) {
                Toast.makeText(GestionCocheraActivity.this, "No se pudo realizar la operación", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SetOcupado()
    {
        /*Cochera cochera = new Cochera();
        cochera.setId(1);*/
        cochera.setEstado('O');
        boolean cancel = false;
        Call<Cochera> call = cocherasService.registrarCambioEstado(cochera);
        call.enqueue(new Callback<Cochera>() {
            @Override
            public void onResponse(Call<Cochera> call, Response<Cochera> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(GestionCocheraActivity.this, "Se cambio el estado a OCUPADO", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(AdminAccountActivity.this, GestionActivity.class);
                    startActivity(intent);*/
                    btnSetOcupado.setEnabled(false);
                    btnSetOcupado.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this, R.drawable.boton_round_gray));
                    btnSetDisponible.setEnabled(true);
                    btnSetDisponible.setBackground(ContextCompat.getDrawable(GestionCocheraActivity.this, R.drawable.boton_round_blue));
                    tvEstadoCochera.setText("Estado: Sin lugares");
                    SessionPrefs.get(GestionCocheraActivity.this).saveEstadoCochera('O');
                }
            }
            @Override
            public void onFailure(Call<Cochera> call, Throwable t) {
                Toast.makeText(GestionCocheraActivity.this, "No se pudo realizar la operación", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                SessionPrefs.get(GestionCocheraActivity.this).logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                SessionPrefs.get(GestionCocheraActivity.this).logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        if (call != null){
            call.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
