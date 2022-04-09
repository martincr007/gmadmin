package com.example.gmappadmin;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
//import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.gmappadmin.api.EntradasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.SessionPrefs;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IngresosActivity extends AppCompatActivity {
    Button btnIngresar;
    TextInputLayout floatLabelDominio;
    TextView txtHora;
    EditText edtDominio;
    private int hora, minutos;
    RadioButton rbAuto, rbCamioneta, rbMoto;
    NumberPicker npHora, npMinutos, npDosPuntos;
    Calendar calendario;
    //
    private Retrofit retrofit;
    EntradasServicio entradasServicio;
    private Ingreso ingreso;
    private int caller;
    //
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);
        btnIngresar = (Button)findViewById(R.id.btnIngresar);
        floatLabelDominio = (TextInputLayout)findViewById(R.id.float_label_dominio);
        rbAuto = (RadioButton)findViewById(R.id.rbAuto);
        rbCamioneta = (RadioButton)findViewById(R.id.rbCamioneta);
        rbMoto = (RadioButton)findViewById(R.id.rbMoto);
        edtDominio = (EditText)findViewById(R.id.edtDominio);
        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        caller = getIntent().getIntExtra("caller",1);
        npHora = (NumberPicker)findViewById(R.id.npHora);
        npMinutos = (NumberPicker)findViewById(R.id.npMinutos);
        npDosPuntos = (NumberPicker)findViewById(R.id.npDosPuntos);
        calendario = Calendar.getInstance();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        //
        npHora.setMinValue(0);
        npHora.setMaxValue(23);
        npHora.setValue(hora);
        npHora.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        //
        final String[] values_dosPuntos = {":"};
        npDosPuntos.setDisplayedValues(values_dosPuntos);
        //npDosPuntos.setValue(":");
        //
        npMinutos.setMinValue(0);
        npMinutos.setMaxValue(59);
        npMinutos.setValue(minutos);
        npMinutos.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        //
        rbAuto.setChecked(true);
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(EntradasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        entradasServicio = retrofit.create(EntradasServicio.class);
        //
        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setearHoraReloj();
                if (!hayErrores()){
                    registrarIngreso();
                    //El siguiente codigo devuelve un valor a la actividad ListIngresos
                    //y cierra la actividad actual
                    if (caller!=1) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("ingreso", ingreso);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }else {
                        edtDominio.requestFocus();
                        mostrarTeclado(edtDominio);
                    }
                }
            }
        });
        edtDominio.requestFocus();
    }

    public boolean hayErrores(){
        String dominio = edtDominio.getText().toString();
        boolean cancel = false;
        View focusView = null;

        //Compureba si se ingreso el dominio.
        if (TextUtils.isEmpty(dominio)) {
            floatLabelDominio.setError(getString(R.string.error_field_required));
            focusView = edtDominio;
            cancel = true;
        } else if (dominio.length() < 6) {
            floatLabelDominio.setError(getString(R.string.error_long_field));
            focusView = edtDominio;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            floatLabelDominio.setError("");
        }
        return cancel;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setearHoraReloj(){
        final Calendar calendar = Calendar.getInstance();
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minutos = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpdHora = new TimePickerDialog(IngresosActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                txtHora.setText(hour + ":" + minute);
            }
        },hora, minutos,true); //true 24 hour time
        tpdHora.setTitle("Elegir hora");
        tpdHora.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ingresar(View view){
        registrarIngreso();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void registrarIngreso(){
        ingreso = new Ingreso();
        ingreso.setDominio(edtDominio.getText().toString());
        if (rbAuto.isChecked()){
            ingreso.setTipoRodado('A');
        }else if (rbCamioneta.isChecked()){
            ingreso.setTipoRodado('C');
        }else if (rbMoto.isChecked()){
            ingreso.setTipoRodado('M');
        }
        String datetime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        datetime = datetime + " " + String.format("%02d", npHora.getValue()) + ":" +
                String.format("%02d", npMinutos.getValue()) + ":00";
        //
        String time = String.format("%02d", npHora.getValue()) + ":" +
                String.format("%02d", npMinutos.getValue()) + ":00";
        try {
            ingreso.setHora(Time.valueOf(time));
        }catch (Exception e) {
            e.printStackTrace();
        }

        //String time = new SimpleDateFormat("HH:mm:ss").format(npHora.getMaxValue()+":"+npMinutos.getValue()+":00");
        ingreso.setFechaHora(datetime);
        ingreso.setEsReserva('N');
        ingreso.setIdCochera(Integer.valueOf(SessionPrefs.get(IngresosActivity.this).idCocheraAdmin()));

        Call<Ingreso> call = entradasServicio.registrarIngreso(ingreso);
        call.enqueue(new Callback<Ingreso>() {
            @Override
            public void onResponse(Call<Ingreso> call, Response<Ingreso> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(IngresosActivity.this, "Ingreso registrado con exito", Toast.LENGTH_LONG).show();
                    limpiarCampos();
                }
            }

            @Override
            public void onFailure(Call<Ingreso> call, Throwable t) {
                Toast.makeText(IngresosActivity.this, "No se pudo registrar el ingreso", Toast.LENGTH_LONG).show();
            }
        });
        //int fecha = calendario.get(Calendar.DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void limpiarCampos(){
        edtDominio.setText("");
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        npHora.setValue(hora);
        npMinutos.setValue(minutos);
        rbAuto.setChecked(true);
        edtDominio.requestFocus();
    }

    public void mostrarTeclado(View v){
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public void ocultarTeclado(){
        View v = this.getCurrentFocus();
        if (v != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
