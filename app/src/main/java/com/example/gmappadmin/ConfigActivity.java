package com.example.gmappadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.CocheraConfig;
import com.example.gmappadmin.models.SessionPrefs;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfigActivity extends AppCompatActivity {
    private Button btnGuardarTarifa, btnGuardarHora;
    private EditText edtTarifaAuto, edtTarifaCamioneta,
            edtTarifaMoto, edtTolerancia, edtHoraCierre, edtEmail;
    private TextInputLayout floatLabelTolerancia, floatLabelTarifaAuto, floatLabelEmail,
            floatLabelTarifaCamioneta, floatLabelTarifaMoto, floatLabelParcial;
    CheckBox chkCierreParcial;
    CocherasServicio cocherasService;
    private Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnGuardarTarifa = findViewById(R.id.btnGuardarTarifa);
        //btnGuardarHora = (Button)findViewById(R.id.btnGuardarHora);
        chkCierreParcial = (CheckBox)findViewById(R.id.chk_hora_cierre);
        edtTarifaAuto = findViewById(R.id.edtTarifaAuto);
        edtTarifaCamioneta = (EditText)findViewById(R.id.edtTarifaCamioneta);
        edtTarifaMoto = (EditText)findViewById(R.id.edtTarifaMoto);
        edtTolerancia = (EditText)findViewById(R.id.edtTolerancia);
        edtHoraCierre = (EditText)findViewById(R.id.edtHoraCierre);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        floatLabelTolerancia = findViewById(R.id.float_label_tolerancia);
        floatLabelTarifaAuto = findViewById(R.id.float_label_tarifa_auto);
        floatLabelTarifaCamioneta = findViewById(R.id.float_label_tarifa_camioneta);
        floatLabelTarifaMoto = findViewById(R.id.float_label_tarifa_moto);
        floatLabelParcial = findViewById(R.id.float_label_parcial);
        floatLabelEmail = findViewById(R.id.float_label_email);
        //
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtTarifaAuto.setText(SessionPrefs.get(ConfigActivity.this).tarifaAuto());
        edtTarifaCamioneta.setText(SessionPrefs.get(ConfigActivity.this).tarifaCamioneta());
        edtTarifaMoto.setText(SessionPrefs.get(ConfigActivity.this).tarifaMoto());
        edtTolerancia.setText(SessionPrefs.get(ConfigActivity.this).toleranciaCochera());
        edtEmail.setText(SessionPrefs.get(ConfigActivity.this).email());
        chkCierreParcial.setChecked(SessionPrefs.get(ConfigActivity.this).horaCierreParcial() >= 0);
        if (chkCierreParcial.isChecked()) {
            edtHoraCierre.setText(String.valueOf(
                    SessionPrefs.get(ConfigActivity.this).horaCierreParcial()));
            edtHoraCierre.setEnabled(true);
        }
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        //
        chkCierreParcial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                edtHoraCierre.setEnabled(b);
                if (b) {mostrarTeclado(edtHoraCierre);}
                else {
                    ocultarTeclado();
                    edtHoraCierre.setText("");
                }
                edtHoraCierre.setEnabled(b);
            }
        });
        /*btnGuardarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkCierreParcial.isChecked()){
                    if (TextUtils.isEmpty(edtHoraCierre.getText().toString())) {
                        floatLabelParcial.setError(getString(R.string.error_field_required));
                        edtHoraCierre.requestFocus();
                        return;
                    }
                    if (! isNumberValid(edtHoraCierre.getText().toString())){
                        floatLabelParcial.setError("Hora no válida");
                        edtHoraCierre.requestFocus();
                        return;
                    }
                    SessionPrefs.get(ConfigActivity.this).saveCierreParcial(true,
                            edtHoraCierre.getText().toString());
                    Toast.makeText(ConfigActivity.this,
                            "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
                    floatLabelParcial.setError("");
                }else{
                    SessionPrefs.get(ConfigActivity.this).saveCierreParcial(
                            false, "-1");
                }
            }
        });*/
        btnGuardarTarifa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validaciones de los campos
                if (TextUtils.isEmpty(edtTarifaAuto.getText().toString())) {
                    floatLabelTarifaAuto.setError(getString(R.string.error_field_required));
                    edtTarifaAuto.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edtTarifaCamioneta.getText().toString())) {
                    floatLabelTarifaCamioneta.setError(getString(R.string.error_field_required));
                    edtTarifaCamioneta.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edtTarifaMoto.getText().toString())) {
                    floatLabelTarifaMoto.setError(getString(R.string.error_field_required));
                    edtTarifaMoto.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edtTolerancia.getText().toString())) {
                    floatLabelTolerancia.setError(getString(R.string.error_field_required));
                    edtTolerancia.requestFocus();
                    return;
                }
                Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                Matcher matcher = pattern.matcher(edtEmail.getText().toString());
                if (! matcher.find()) {
                    floatLabelEmail.setError(getString(R.string.error_invalid_email));
                    return;
                }
                if (chkCierreParcial.isChecked()){
                    if (TextUtils.isEmpty(edtHoraCierre.getText().toString())) {
                        floatLabelParcial.setError(getString(R.string.error_field_required));
                        edtHoraCierre.requestFocus();
                        return;
                    }
                    if (! isNumberValid(edtHoraCierre.getText().toString())){
                        floatLabelParcial.setError("Hora no válida");
                        edtHoraCierre.requestFocus();
                        return;
                    }
                }
                CocheraConfig cocheraConfig = new CocheraConfig();
                cocheraConfig.setIdCochera(Integer.valueOf(SessionPrefs.get(ConfigActivity.this).idCocheraAdmin()));
                cocheraConfig.setTarifaAuto(Integer.valueOf(edtTarifaAuto.getText().toString()));
                cocheraConfig.setTarifaCamioneta(Integer.valueOf(edtTarifaCamioneta.getText().toString()));
                cocheraConfig.setTarifaMoto(Integer.valueOf(edtTarifaMoto.getText().toString()));
                cocheraConfig.setTolerancia(Integer.valueOf(edtTolerancia.getText().toString()));
                cocheraConfig.setEmail(edtEmail.getText().toString());
                if (chkCierreParcial.isChecked()) {
                    cocheraConfig.setHoraCierre(Integer.valueOf(edtHoraCierre.getText().toString()));
                }else{
                    cocheraConfig.setHoraCierre(-1);
                }
                Call<CocheraConfig> call = cocherasService.actualizarConfig(cocheraConfig);
                call.enqueue(new Callback<CocheraConfig>() {
                    @Override
                    public void onResponse(Call<CocheraConfig> call, Response<CocheraConfig> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ConfigActivity.this,
                                    "Se guardaron los cambios", Toast.LENGTH_SHORT).show();
                            ocultarTeclado();
                            SessionPrefs.get(ConfigActivity.this).saveConfigCochera(cocheraConfig);
                            floatLabelTarifaAuto.setError("");
                            floatLabelTarifaCamioneta.setError("");
                            floatLabelTarifaMoto.setError("");
                            floatLabelTolerancia.setError("");
                            floatLabelParcial.setError("");
                            floatLabelEmail.setError("");
                        }
                    }
                    @Override
                    public void onFailure(Call<CocheraConfig> call, Throwable t) {
                        Toast.makeText(ConfigActivity.this,
                                "No se pudo realizar la operación", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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

    private boolean isNumberValid(String valor) {
        return Integer.valueOf(valor) >= 0 && Integer.valueOf(valor) <=23;
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