package com.example.gmappadmin;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gmappadmin.api.UserService;
import com.example.gmappadmin.models.Admin;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminAccountActivity extends AppCompatActivity {
    EditText edtNombre, edtApellido, edtEmail, edtUsername,
        edtPassword, edtPasswordConfirm;
    TextInputLayout mFloatNombre, mFloatApellido, mFloatEmail,
        mFloatUserName, mFloatPass, mFloatConfirmPass;
    Button btnRegistrar;
    private Retrofit retrofit;
    UserService userService;
    private int idCochera = 30;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPasswordConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);
        mFloatNombre = (TextInputLayout)findViewById(R.id.float_label_nombre);
        mFloatApellido = (TextInputLayout)findViewById(R.id.float_label_apellido);
        mFloatEmail = (TextInputLayout)findViewById(R.id.float_label_email);
        mFloatUserName = (TextInputLayout)findViewById(R.id.float_label_username);
        mFloatPass = (TextInputLayout)findViewById(R.id.float_label_password);
        mFloatConfirmPass  = (TextInputLayout)findViewById(R.id.float_label_passwordConfirm);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(UserService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        userService = retrofit.create(UserService.class);
        //
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOnline()) {
                    Toast.makeText(AdminAccountActivity.this, "Error de red", Toast.LENGTH_LONG).show();
                    return;
                }
                registrar();
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void registrar() {
        clearErrors();
        if (isEmpty(edtNombre.getText().toString())){
            mFloatNombre.setError(getString(R.string.error_field_required));
            return;
        }
        //
        if (isEmpty(edtApellido.getText().toString())){
            mFloatApellido.setError(getString(R.string.error_field_required));
            return;
        }
        //
        if (isEmpty(edtEmail.getText().toString())){
            mFloatEmail.setError(getString(R.string.error_field_required));
            return;
        }
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(edtEmail.getText().toString());
        if (! matcher.find()) {
            mFloatEmail.setError(getString(R.string.error_invalid_email));
            return;
        }
        //
        if (isEmpty(edtUsername.getText().toString())){
            mFloatUserName.setError(getString(R.string.error_field_required));
            return;
        }
        if (isEmpty(edtPassword.getText().toString())){
            mFloatPass.setError(getString(R.string.error_field_required));
            return;
        }
        if (!isPasswordValid(edtPassword.getText().toString())){
            mFloatPass.setError(getString(R.string.error_invalid_password));
            return;
        }
        if (!stringEquals(edtPassword.getText().toString(),
                edtPasswordConfirm.getText().toString())){
            mFloatConfirmPass.setError(getString(R.string.error_distinct_password));
            return;
        }
        Admin admin = new Admin();
        //admin.setId("7");
        admin.setNombre(edtNombre.getText().toString());
        admin.setApellido(edtApellido.getText().toString());
        admin.setEmail(edtEmail.getText().toString());
        admin.setUserName(edtUsername.getText().toString());
        admin.setPassword(edtPassword.getText().toString());
        admin.setIdCochera(idCochera);
        admin.setAdmintipo(0);

        Call<Admin> call = userService.registrarAdmin(admin);
        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminAccountActivity.this,
                            "Usuario registrado con exito.", Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(AdminAccountActivity.this, GestionActivity.class);
                    startActivity(intent);*/
                    startActivity(new Intent(AdminAccountActivity.this, GestionActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Toast.makeText(AdminAccountActivity.this, "Nombre de usuario o contraseña incorrectos.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Validaciones de los campos
    private boolean isEmpty(String cadena){
        return TextUtils.isEmpty(cadena);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private boolean stringEquals(String cadena1, String cadena2){
        return (cadena1.equals(cadena2));
    }

    private void clearErrors(){
        mFloatNombre.setError("");
        mFloatApellido.setError("");
        mFloatEmail.setError("");
        mFloatUserName.setError("");
        mFloatPass.setError("");
        mFloatConfirmPass.setError("");
        edtNombre.requestFocus();
    }
}
