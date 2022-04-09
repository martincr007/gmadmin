package com.example.gmappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.SessionPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalidaActivity extends AppCompatActivity {
    private TextView tvDominio, tvTipo, tvTiempoEstadia,
            tvTotal, tvTotalCent, tvIngreso;
    private Button btnFacturar;
    long horas, minutos;
    private int precioAuto = Integer.valueOf(SessionPrefs.get(this).tarifaAuto());
    private int precioCamioneta = Integer.valueOf(SessionPrefs.get(this).tarifaCamioneta());
    private int precioMoto = Integer.valueOf(SessionPrefs.get(this).tarifaMoto());
    private int total = 0;
    //
    CocherasServicio cocherasService;
    private Retrofit retrofit;
    //
    //SharedPreferences mPrefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida);
        //
        btnFacturar = (Button)findViewById(R.id.btn_facturar);
        tvDominio = (TextView)findViewById(R.id.tv_dominio);
        tvTipo = (TextView)findViewById(R.id.tv_tipo);
        tvIngreso = (TextView)findViewById(R.id.tv_ingreso);
        tvTiempoEstadia = (TextView)findViewById(R.id.tv_tiempo_estadia);
        tvTotal = (TextView)findViewById(R.id.tv_total);
        tvTotalCent = (TextView)findViewById(R.id.tv_total_cent);
        tvDominio.setText("Dominio: " + getIntent().getStringExtra("dominio"));
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        String dtStart = getIntent().getStringExtra("fechahora");
        String horaIngreso = dtStart.substring(11);
        String tipo_rodado = getIntent().getStringExtra("tipo_rodado");
        //
        tvTipo.setText("Tipo: " + tipo_rodado);
        tvIngreso.setText("Hora de ingreso: " + String.valueOf(horaIngreso));
        char pagado = getIntent().getCharExtra("pagado",'N');
        String cantidad_horas = getIntent().getStringExtra("cantidad_horas");
        String strTotal = getIntent().getStringExtra("total");
        if (pagado == 'S'){
            btnFacturar.setEnabled(false);
            tvTiempoEstadia.setText("Tiempo: " + cantidad_horas + " horas");
            tvTotal.setText("$" + strTotal);
            return;
        }
        //
        int tolerancia = Integer.valueOf(SessionPrefs.get(SalidaActivity.this).toleranciaCochera());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
            Date ahora = new Date();
            long diff = ahora.getTime() - date.getTime(); //expresado en milisegundos
            //long segundos = diff / 1000;
            minutos = diff / 1000 / 60;
            horas = minutos / 60;
            //long dias = horas / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        int minutosResto = (int) (minutos % 60);
        //
        tvTiempoEstadia.setText("Tiempo: " + String.valueOf(horas) + " horas " +
                String.valueOf(minutosResto) + " minutos");
        if (horas == 0 & minutos >= 0) {  //si el vehiculo estuvo una fracción de hora
            horas++;
        }else if (minutosResto > tolerancia) { //si se paso del tiempo de tolerancia
            horas++;
        }
        if (tipo_rodado.equals("A")){
            total = (int) (horas * precioAuto);
        }else if (tipo_rodado.equals("C")){
            total = (int) (horas * precioCamioneta);
        }else{
            total = (int) (horas * precioMoto);
        }
        tvTotal.setText("$" + String.valueOf(total));
        //tvTotalCent.setText(Html.fromHtml("<sup>00</sup>"));
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        //
        btnFacturar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingreso ingreso = new Ingreso();
                ingreso.setId(getIntent().getIntExtra("id", 0));
                ingreso.setCantidadHoras((int) horas);
                ingreso.setPagado('S');
                ingreso.setTotal(total);
                boolean cancel = false;
                Call<Ingreso> call = cocherasService.registrarCambioIngreso(ingreso);
                call.enqueue(new Callback<Ingreso>() {
                    @Override
                    public void onResponse(Call<Ingreso> call, Response<Ingreso> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SalidaActivity.this, "Se registró el pago", Toast.LENGTH_SHORT).show();
                            btnFacturar.setEnabled(false);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("ingreso", ingreso);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<Ingreso> call, Throwable t) {
                        Toast.makeText(SalidaActivity.this, "No se pudo realizar la operación", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_nav, menu);
        return true;
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}