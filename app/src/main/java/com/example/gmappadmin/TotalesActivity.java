package com.example.gmappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.api.EntradasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.Jornada;
import com.example.gmappadmin.models.RequestIngresoBody;
import com.example.gmappadmin.models.SessionPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TotalesActivity extends AppCompatActivity {
    private TextView tvFecha, tvCantAuto, tvCantCamioneta, tvCantMoto,
            tvTotalAuto, tvTotalCamioneta, tvTotalMoto, tvTotalDiario;
    private Button btnCerrar, btnDetalles;
    private CheckBox chkEnviarMail;
    private Retrofit retrofit;
    CocherasServicio cocherasService;
    EntradasServicio entradasService;
    ArrayList<Ingreso> listIngresos = new ArrayList<Ingreso>();
    int cantidadAuto, cantidadCamioneta, cantidadMoto,
        totalDiario, totalAuto, totalCamioneta, totalMoto, total;
    SimpleDateFormat sdFormat;
    private Integer idCochera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totales);
        btnCerrar = (Button)findViewById(R.id.btn_cerrar);
        btnDetalles = (Button)findViewById(R.id.btnDetalles);
        chkEnviarMail = (CheckBox)findViewById(R.id.chk_enviar_mail);
        tvTotalDiario = (TextView) findViewById(R.id.tv_total_diario);
        tvFecha = (TextView) findViewById(R.id.tv_fecha_actual);
        tvCantAuto = (TextView) findViewById(R.id.tv_cantidad_auto);
        tvCantCamioneta = (TextView) findViewById(R.id.tv_cantidad_camioneta);
        tvCantMoto = (TextView) findViewById(R.id.tv_cantidad_moto);
        tvTotalAuto = (TextView) findViewById(R.id.tv_total_auto);
        tvTotalCamioneta = (TextView) findViewById(R.id.tv_total_camioneta);
        tvTotalMoto = (TextView) findViewById(R.id.tv_total_moto);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sdFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvFecha.setText(sdFormat.format(new Date()));
        idCochera = Integer.valueOf(SessionPrefs.get(TotalesActivity.this).idCocheraAdmin());
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        //
        sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = sdFormat.format(new Date());
        Jornada jornada = new Jornada();
        jornada.setFecha(fecha);
        Call<Jornada> callJornada = cocherasService.obtenerJornadaFecha(jornada);
        callJornada.enqueue(new Callback<Jornada>(){
            @Override
            public void onResponse(Call<Jornada> callJornada, Response<Jornada> response) {
                if (response.isSuccessful()) {
                    if (response.body().getId() !=0) {
                        Toast.makeText(TotalesActivity.this, "ATENCIÓN: Ya se cerró la jornada: "
                                + sdFormat.format(new Date()), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Jornada> callJornada, Throwable t) {
                Toast.makeText(TotalesActivity.this, "No se pudo procesar la petición",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //
        Call<ArrayList<Ingreso>> callIngreso = cocherasService.obtenerIngresosEstadoFecha(
                new RequestIngresoBody("S",fecha, idCochera));
        callIngreso.enqueue(new Callback(){
            @Override
            public void onResponse(Call callIngreso, Response response) {
                listIngresos = (ArrayList<Ingreso>) response.body();
                //Prueba de cierre parcial
                int horaCierre;
                if (SessionPrefs.get(TotalesActivity.this).cierreParcial()) {
                    horaCierre = SessionPrefs.get(TotalesActivity.this).horaCierreParcial();
                    int horaActual = new Date().getHours();
                    if (horaActual >= horaCierre ){
                        for (Iterator<Ingreso> iter = listIngresos.iterator(); iter.hasNext(); ) {
                            Ingreso ingreso = iter.next();
                            int hora = Integer.valueOf(ingreso.getFechaHora().substring(11, 13));
                            if (hora < horaCierre) {
                                iter.remove();
                            }
                        }
                    }
                }
                //
                for (Ingreso ingreso:listIngresos) {
                    if (ingreso.getTipoRodado() == 'A'){
                        cantidadAuto += 1;
                        totalAuto += ingreso.getTotal();
                    }else if (ingreso.getTipoRodado() == 'C'){
                        cantidadCamioneta += 1;
                        totalCamioneta += ingreso.getTotal();
                    }else{
                        cantidadMoto += 1;
                        totalMoto += ingreso.getTotal();
                    }
                    total += 1;
                    totalDiario += ingreso.getTotal();
                }
                //Calculo porcentaje
                double porcAuto = 0; double porcCamioneta = 0; double porcMoto = 0;
                double porcAutoIng = 0; double porcCamionetaIng = 0; double porcMotoIng = 0;
                if (total > 0) {
                    porcAuto = cantidadAuto * 100 / (total);
                    porcMoto = cantidadMoto * 100 / (total);
                    porcCamioneta = cantidadCamioneta * 100 / (total);
                    porcAutoIng = totalAuto * 100 / totalDiario;
                    porcMotoIng = totalMoto * 100 / totalDiario;
                    porcCamionetaIng = totalCamioneta * 100 / totalDiario;
                }
                //Muestro resultados
                tvCantAuto.setText("Cantidad de autos: " + String.valueOf(cantidadAuto) + "  |  " +
                        String.valueOf(porcAuto) + "%");
                tvCantCamioneta.setText("Cantidad de camionetas: " + String.valueOf(cantidadCamioneta) + "  |  " +
                        String.valueOf(porcCamioneta) + "%");
                tvCantMoto.setText("Cantidad de motos: " + String.valueOf(cantidadMoto) + "  |  " +
                        String.valueOf(porcMoto) + "%");
                tvTotalAuto.setText("Total por autos: $" + String.valueOf(totalAuto) +"  |  " +
                        String.valueOf(porcAutoIng) + "%");
                tvTotalCamioneta.setText("Total por camionetas: $" + String.valueOf(totalCamioneta) +"  |  " +
                        String.valueOf(porcCamionetaIng) + "%");
                tvTotalMoto.setText("Total por motos: $" + String.valueOf(totalMoto) + "  |  " +
                        String.valueOf(porcMotoIng) + "%");
                tvTotalDiario.setText(String.valueOf(totalDiario));
                /*Toast.makeText(TotalesActivity.this, String.valueOf(totalDiario),
                        Toast.LENGTH_LONG).show();*/
            }
            @Override
            public void onFailure(Call callIngreso, Throwable t) {
                Toast.makeText(TotalesActivity.this, "No se registraron ingresos para la fecha actual",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Jornada jornada = new Jornada();
                jornada.setIdCochera(Integer.valueOf(SessionPrefs.get(TotalesActivity.this).idCocheraAdmin()));
                jornada.setTotal(totalDiario);
                jornada.setCantidadAutos(cantidadAuto);
                jornada.setCantidadCamionetas(cantidadCamioneta);
                jornada.setCantidadMotos(cantidadMoto);
                SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                jornada.setFecha(sdFormat.format(new Date()));
                retrofit = new Retrofit.Builder()
                        .baseUrl(EntradasServicio.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                entradasService = retrofit.create(EntradasServicio.class);
                Call<Jornada> call = entradasService.registrarJornada(jornada);
                call.enqueue(new Callback<Jornada>() {
                    @Override
                    public void onResponse(Call<Jornada> call, Response<Jornada> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(TotalesActivity.this, "Se registró el cierre", Toast.LENGTH_SHORT).show();
                            btnCerrar.setEnabled(false);
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);
                            if (chkEnviarMail.isChecked()) {mandarMail();}
                            finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<Jornada> call, Throwable t) {
                        Toast.makeText(TotalesActivity.this, "No se pudo realizar la operación", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnDetalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TotalesActivity.this, TableIngresosActivity.class));
            }
        });
    }

    private void mandarMail( ){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        email.setType("text/plain");
        String[] destinos = new String[] {SessionPrefs.get(TotalesActivity.this).email()};
        email.putExtra(Intent.EXTRA_EMAIL, destinos);
        email.putExtra(Intent.EXTRA_SUBJECT, "Resumen jornada");
        //
        String resultado = "Jornada: " +
                new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        resultado += "\n\nCantidad de autos: " + cantidadAuto;
        resultado += "\nCantidad de camionetas: " + cantidadCamioneta;
        resultado += "\nCantidad de motos: " + cantidadMoto;
        resultado += "\n\nIngresos por autos: $" + totalAuto;
        resultado += "\nIngresos por camionetas: $" + totalCamioneta;
        resultado += "\nIngresos por motos: $" + totalMoto;
        resultado += "\n\nIngreso total: $" + totalDiario;
        //
        email.putExtra(Intent.EXTRA_TEXT, resultado);
        startActivity(Intent.createChooser(email, "Enviando email"));
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
}