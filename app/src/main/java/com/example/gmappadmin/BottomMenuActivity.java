package com.example.gmappadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.RequestIngresoBody;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BottomMenuActivity extends AppCompatActivity {

    private BottomAppBar bottomBar;
    //private TextView tvCantidad;
    private BottomNavigationView bottomNavigationView;
    public boolean newIngreso = false;
    ArrayList<Ingreso> listIngresos = new ArrayList<Ingreso>();
    IngresosAdapter ingresosAdapter;
    private Retrofit retrofit;
    CocherasServicio cocherasService;
    int activityCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);
        //tvCantidad = (TextView)findViewById(R.id.tv_cant_actual);
        bottomBar = (BottomAppBar)findViewById(R.id.bottomAppBar);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setOnNavigationItemReselectedListener(navReListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new PendientesFragment()).commit();
        //setSupportActionBar(mBottomBar);
        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(0xffd5185a));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newIngreso = true;
                Intent intent = new Intent(BottomMenuActivity.this, IngresosActivity.class);
                intent.putExtra("caller", 2);
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        RequestIngresoBody requestIngresoBody = new RequestIngresoBody("N",
                sdFormat.format(new Date()), 1);
        Call<ArrayList<Ingreso>> call = cocherasService.obtenerIngresosEstadoFecha(requestIngresoBody);
        call.enqueue(new Callback(){
            @Override
            public void onResponse(Call call, Response response) {
                listIngresos = (ArrayList<Ingreso>) response.body();
                Collections.sort(listIngresos);
                ingresosAdapter = new IngresosAdapter(BottomMenuActivity.this, listIngresos);
                PendientesFragment pendientesFragment = PendientesFragment.newInstance(listIngresos);
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, pendientesFragment).commit();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                /*Toast.makeText(getContext(), "No se pudo procesar la petición",
                        Toast.LENGTH_LONG).show();*/
            }
        });

        //
        //Click de BottomAppBar
        /*bottomBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu1){
                    PendientesFragment pendientesFragment = new PendientesFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, pendientesFragment);
                    fragmentTransaction.commit();
                }
                if (item.getItemId() == R.id.menu2){
                    PagadosFragment pagadosFragment = new PagadosFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, pagadosFragment);
                    fragmentTransaction.commit();
                }
                return false;
            }
        });*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selected = null;
            switch (item.getItemId()){
                case R.id.menu1:
                    selected = PendientesFragment.newInstance(listIngresos);
                    break;
                case R.id.menu2:
                    selected = new PagadosFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                    selected).commit();
            return true;
        }
    };

    private BottomNavigationView.OnNavigationItemReselectedListener navReListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            int i = 0;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            /*if (bottomNavigationView.getSelectedItemId()==R.id.menu2) {
                bottomNavigationView.setSelectedItemId(R.id.menu1);
            }*/
            Ingreso ingresoNuevo = (Ingreso) data.getSerializableExtra("ingreso");
            if (activityCaller == 1) {
                listIngresos.add(ingresoNuevo);
            }else{
                listIngresos.remove(ingresoNuevo);
            }
            //PendientesFragment pendientesFragment = PendientesFragment.newInstance(ingresoNuevo);
            PendientesFragment pendientesFragment = PendientesFragment.newInstance(listIngresos);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, pendientesFragment);
            fragmentTransaction.commit();
        }
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