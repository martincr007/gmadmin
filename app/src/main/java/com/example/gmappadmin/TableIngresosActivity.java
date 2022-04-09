package com.example.gmappadmin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.RequestIngresoBody;
import com.example.gmappadmin.models.SessionPrefs;
import com.example.gmappadmin.models.TableDynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TableIngresosActivity extends AppCompatActivity {

    Button btnBuscarIngresos;
    private TableLayout tableLayout;
    private String[] header = {"Hora", "Dominio", "Tipo", "Total"};
    private ArrayList<String[]> rows;
    private TableDynamic tableDynamic;
    private Retrofit retrofit;
    private TextView tvFechaDetalles;
    CocherasServicio cocherasServicio;
    SimpleDateFormat sdFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_ingresos);
        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        tvFechaDetalles = (TextView)findViewById(R.id.tv_fecha_detalles);
        tableLayout = (TableLayout)findViewById(R.id.tableIngresos);
        tableDynamic = new TableDynamic(tableLayout, getApplicationContext());
        rows = new ArrayList<String[]>();
        sdFormat = new SimpleDateFormat("dd-MM-yyyy");
        tvFechaDetalles.setText("Fecha: " + sdFormat.format(new Date()));
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasServicio = retrofit.create(CocherasServicio.class);
        //
        obtenerIngresos();
        //
        /*btnBuscarIngresos = (Button)findViewById(R.id.btnBuscarIngresos);
        btnBuscarIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerIngresos();
            }
        });*/
    }

    public void obtenerIngresos(){
        rows.clear(); //limpia el arrayList
        tableLayout.removeAllViews();
        sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        Call<ArrayList<Ingreso>> call = cocherasServicio.obtenerIngresosEstadoFecha(
                new RequestIngresoBody("S",sdFormat.format(new Date()),
                        Integer.valueOf(SessionPrefs.get(TableIngresosActivity.this).idCocheraAdmin()))
        );
        call.enqueue(new Callback(){
            @Override
            public void onResponse(Call call, Response response) {
                ArrayList<Ingreso> ingresoList = (ArrayList<Ingreso>)response.body();
                //Prueba de cierre parcial
                int horaCierre;
                if (SessionPrefs.get(TableIngresosActivity.this).cierreParcial()) {
                    horaCierre = SessionPrefs.get(TableIngresosActivity.this).horaCierreParcial();
                    int horaActual = new Date().getHours();
                    if (horaActual >= horaCierre ){
                        for (Iterator<Ingreso> iter = ingresoList.iterator(); iter.hasNext(); ) {
                            Ingreso ingreso = iter.next();
                            int hora = Integer.valueOf(ingreso.getFechaHora().substring(11, 13));
                            if (hora < horaCierre) {
                                iter.remove();
                            }
                        }
                    }
                }
                //
                for (int i = 0; i < ingresoList.size(); i++) {
                    Ingreso ingreso= ingresoList.get(i);
                    rows.add(new String[] {ingreso.getFechaHora().substring(11),ingreso.getDominio(),
                            String.valueOf(ingreso.getTipoRodado()), "$" + String.valueOf(ingreso.getTotal())});
                }
                tableDynamic.addHeader(header);
                tableDynamic.backgroudHeader(Color.argb(200, 100,170,240));
                tableDynamic.addData(rows);
                tableDynamic.backgroudData(Color.argb(200,180,240,240), Color.argb(200,20,220,200));
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(TableIngresosActivity.this, "No se pudo procesar la petición", Toast.LENGTH_LONG).show();
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
}
