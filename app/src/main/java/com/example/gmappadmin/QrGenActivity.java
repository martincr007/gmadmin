package com.example.gmappadmin;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gmappadmin.models.SessionPrefs;

import net.glxn.qrgen.android.QRCode;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QrGenActivity extends AppCompatActivity {
    private EditText edtCochera, edtDireccion;
    private Button btnGenerar, btnGuardar;
    //private ImageView imagenCodigo;
    private String textoCodigo = "";
    private boolean tienePermisoParaEscribir = false; // Permisos en tiempo de ejecución
    private static final int CODIGO_PERMISO_ESCRIBIR = 1;
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_gen);
        final ImageView imagenCodigo = findViewById(R.id.ivCodigoGenerado);
        edtCochera = findViewById(R.id.edt_cochera);
        edtDireccion = findViewById(R.id.edt_direccion);
        btnGenerar = findViewById(R.id.btn_generar);
        btnGuardar = findViewById(R.id.btn_save);
        edtCochera.requestFocus();
        btnGenerar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGuardar.setVisibility(View.GONE);
                String cochera = edtCochera.getText().toString();
                String direccion = edtDireccion.getText().toString();
                if (cochera.isEmpty()){
                    edtCochera.setError("Debe ingresar un valor");
                    return;
                }
                if (direccion.isEmpty()){
                    edtDireccion.setError("Debe ingresar un valor");
                    return;
                }
                edtCochera.setError(null);
                edtDireccion.setError(null);
                textoCodigo = "http://sinapsys.com.ar" + ";"
                        + SessionPrefs.get(QrGenActivity.this).idCocheraAdmin() + ";"
                        + edtCochera.getText().toString() + ";"
                        + edtDireccion.getText().toString() + ";"
                        + SessionPrefs.get(QrGenActivity.this).tarifaAuto() + ";"
                        + SessionPrefs.get(QrGenActivity.this).tarifaCamioneta() + ";"
                        + SessionPrefs.get(QrGenActivity.this).tarifaMoto() + ";"
                        + SessionPrefs.get(QrGenActivity.this).toleranciaCochera();
                if (textoCodigo.isEmpty()) return;
                Bitmap bitmap = QRCode.from(textoCodigo).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap();
                imagenCodigo.setImageBitmap(bitmap);
                Toast.makeText(QrGenActivity.this,
                        "Código generado", Toast.LENGTH_SHORT).show();
                btnGuardar.setVisibility(View.VISIBLE);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tienePermisoParaEscribir) {
                    Toast.makeText(QrGenActivity.this,
                            "No has dado permiso para escribir", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Crear stream del código QR
                ByteArrayOutputStream byteArrayOutputStream =
                        QRCode.from(textoCodigo).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream();
                // e intentar guardar
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/codigo.png");
                    byteArrayOutputStream.writeTo(fos);
                    Toast.makeText(QrGenActivity.this, "Código guardado", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        verificarYPedirPermisos();
    }

    private void verificarYPedirPermisos() {
        if (ContextCompat.checkSelfPermission(
            QrGenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            // En caso de que haya dado permisos ponemos la bandera en true
            tienePermisoParaEscribir = true;
        } else {
            // Si no, entonces pedimos permisos
            ActivityCompat.requestPermissions(QrGenActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CODIGO_PERMISO_ESCRIBIR);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODIGO_PERMISO_ESCRIBIR:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // SÍ dieron permiso
                    tienePermisoParaEscribir = true;

                } else {
                    // NO dieron permiso
                    Toast.makeText(QrGenActivity.this,
                            "No has dado permiso para escribir", Toast.LENGTH_SHORT).show();
                }
        }
    }

}