package com.example.gmappadmin.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
/**
 * Manejador de preferencias de la sesión del afiliado
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "GARAGEMAPP_PREFS";
    public static final String PREF_ADMIN_ID = "PREF_ADMIN_ID";
    public static final String PREF_ADMIN_NOMBRE = "PREF_ADMIN_NOMBRE";
    public static final String PREF_ADMIN_APELLIDO = "PREF_ADMIN_APELLIDO";
    public static final String PREF_ADMIN_EMAIL = "PREF_ADMIN_EMAIL";
    public static final String PREF_ADMIN_USERNAME = "PREF_ADMIN_USERNAME";
    public static final String PREF_ADMIN_ID_COCHERA = "PREF_ADMIN_ID_COCHERA";
    public static final String PREF_COCHERA_NOMBRE = "PREF_COCHERA_NOMBRE";
    public static final String PREF_COCHERA_DIRECCION = "PREF_COCHERA_DIRECCION";
    public static final String PREF_COCHERA_ESTADO = "PREF_COCHERA_ESTADO";
    public static final String PREF_COCHERA_TOLERANCIA = "PREF_COCHERA_TOLERANCIA";
    public static final String PREF_TARIFA_AUTO = "PREF_TARIFA_AUTO";
    public static final String PREF_TARIFA_CAMIONETA = "PREF_TARIFA_CAMIONETA";
    public static final String PREF_TARIFA_MOTO = "PREF_TARIFA_MOTO";
    public static final String PREF_CIERRE_PARCIAL = "PREF_CIERRE_PARCIAL";
    public static final String PREF_HORA_CIERRE_PARCIAL = "PREF_HORA_CIERRE_PARCIAL";
    public static final String PREF_EMAIL = "PREF_EMAIL";

    private final SharedPreferences mPrefs;
    private boolean mIsLoggedIn = false;
    //private String mIdCochera;

    private static SessionPrefs INSTANCE;
    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    public SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_ADMIN_USERNAME, null));
    }

    public boolean isLoggedIn() { return mIsLoggedIn; }
    public String idCocheraAdmin() { return mPrefs.getString(PREF_ADMIN_ID_COCHERA, null); }
    public String nombreAdmin() { return mPrefs.getString(PREF_ADMIN_NOMBRE, null); }
    public String apellidoAdmin() { return mPrefs.getString(PREF_ADMIN_APELLIDO, null); }
    public String nombreCochera() { return mPrefs.getString(PREF_COCHERA_NOMBRE, null); }
    public String direccionCochera() { return mPrefs.getString(PREF_COCHERA_DIRECCION, null); }
    public String toleranciaCochera() { return mPrefs.getString(PREF_COCHERA_TOLERANCIA, null); }
    public String tarifaAuto() { return mPrefs.getString(PREF_TARIFA_AUTO, null); }
    public String tarifaCamioneta() { return mPrefs.getString(PREF_TARIFA_CAMIONETA, null); }
    public String tarifaMoto() { return mPrefs.getString(PREF_TARIFA_MOTO, null); }
    public String estadoCochera() { return mPrefs.getString(PREF_COCHERA_ESTADO, null); }
    public boolean cierreParcial() { return mPrefs.getBoolean(PREF_CIERRE_PARCIAL, false); }
    public int horaCierreParcial() { return mPrefs.getInt(PREF_HORA_CIERRE_PARCIAL, -1); }
    public String email() { return mPrefs.getString(PREF_EMAIL, null); }

    public void saveAdmin(Admin admin) {
        if (admin != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_ADMIN_ID, Integer.toString(admin.getId())); //tener en cuenta
            editor.putString(PREF_ADMIN_NOMBRE, admin.getNombre());
            editor.putString(PREF_ADMIN_APELLIDO, admin.getApellido());
            editor.putString(PREF_ADMIN_EMAIL, admin.getEmail());
            editor.putString(PREF_ADMIN_USERNAME, admin.getUserName());
            editor.putString(PREF_ADMIN_ID_COCHERA, Integer.toString(admin.getIdCochera()));
            //editor.apply();
            editor.commit();
            //mIdCochera = mPrefs.getString(PREF_ADMIN_ID_COCHERA, null);
            mIsLoggedIn = true;
        }
    }

    public void saveCochera(Cochera cochera) {
        if (cochera != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_COCHERA_NOMBRE, cochera.getNombre());
            editor.putString(PREF_COCHERA_DIRECCION, cochera.getDireccion());
            editor.putString(PREF_COCHERA_ESTADO, String.valueOf(cochera.getEstado()));
            //editor.putString(PREF_COCHERA_TOLERANCIA, Integer.toString(cochera.getTolerancia()));
            //editor.apply();
            editor.commit();
        }
    }

    public void saveEstadoCochera(char estado) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_COCHERA_ESTADO, String.valueOf(estado));
        //editor.apply();
        editor.commit();
    }

    public void saveConfigCochera(CocheraConfig cocheraConfig) {
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_TARIFA_AUTO, String.valueOf(cocheraConfig.getTarifaAuto()));
        editor.putString(PREF_TARIFA_CAMIONETA, String.valueOf(cocheraConfig.getTarifaCamioneta()));
        editor.putString(PREF_TARIFA_MOTO, String.valueOf(cocheraConfig.getTarifaMoto()));
        editor.putString(PREF_COCHERA_TOLERANCIA, String.valueOf(cocheraConfig.getTolerancia()));
        editor.putInt(PREF_HORA_CIERRE_PARCIAL, cocheraConfig.getHoraCierre());
        editor.putString(PREF_EMAIL, cocheraConfig.getEmail());
        //editor.apply();
        editor.commit();
    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_ADMIN_ID, null);
        editor.putString(PREF_ADMIN_NOMBRE, null);
        editor.putString(PREF_ADMIN_APELLIDO, null);
        editor.putString(PREF_ADMIN_EMAIL, null);
        editor.putString(PREF_ADMIN_USERNAME, null);
        editor.putString(PREF_ADMIN_ID_COCHERA, null);
//        editor.putString("user", null);
//        editor.putString("idCochera", null);
        editor.apply();
    }
}
