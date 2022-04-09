package com.example.gmappadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.RequestIngresoBody;
import com.example.gmappadmin.models.SessionPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class PendientesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //
    private EditText edtFiltroPendientes;
    private ListView lvIngresosBis;
    private TextView tvCantidad;
    private Retrofit retrofit;
    CocherasServicio cocherasService;
    ArrayList<Ingreso> listIngresos = new ArrayList<Ingreso>();
    IngresosAdapter ingresosAdapter;
    Ingreso ingreso;
    int activityCaller, posicion;
    int idCochera;

    public PendientesFragment() {
        // Required empty public constructor
    }

    public static PendientesFragment newInstance(ArrayList<Ingreso> listIngresos) {
        PendientesFragment fragment = new PendientesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, listIngresos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listIngresos = (ArrayList<Ingreso>) getArguments().getSerializable(ARG_PARAM1);
            //Collections.sort(listIngresos);
        }
        if (ingresosAdapter !=null){
            ingresosAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendientes, container, false);
        edtFiltroPendientes = (EditText) view.findViewById(R.id.edtFiltroPendientes);
        lvIngresosBis = (ListView) view.findViewById(R.id.lvIngresosBis);
        ingresosAdapter = new IngresosAdapter(getContext(), listIngresos);
        tvCantidad = view.findViewById(R.id.tv_cant_actual);
        idCochera = Integer.valueOf(SessionPrefs.get(getContext()).idCocheraAdmin());
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        RequestIngresoBody requestIngresoBody = new RequestIngresoBody("N",
                sdFormat.format(new Date()), idCochera);
        //Prueba de cierre parcial
        int horaCierre;
        if (SessionPrefs.get(getContext()).cierreParcial()) {
            horaCierre = SessionPrefs.get(getContext()).horaCierreParcial();
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
        Collections.sort(listIngresos);
        ingresosAdapter = new IngresosAdapter(getContext(), listIngresos);
        lvIngresosBis.setAdapter(ingresosAdapter);
        tvCantidad.setText("Vehiculos en cochera: " + listIngresos.size());

        //Para filtro
        edtFiltroPendientes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //if (listIngresos.size()==0) {return;}
            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                //if (listIngresos.size()>0) {
                    ingresosAdapter.filtrar(edtFiltroPendientes.getText().toString().toLowerCase());
                //}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        lvIngresosBis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activityCaller = 2;
                Intent intent = new Intent(getContext(), SalidaActivity.class);
                intent.putExtra("dominio", listIngresos.get(i).getDominio());
                intent.putExtra("tipo_rodado", String.valueOf(listIngresos.get(i).getTipoRodado()));
                intent.putExtra("fechahora", listIngresos.get(i).getFechaHora());
                intent.putExtra("id", listIngresos.get(i).getId());
                intent.putExtra("pagado", listIngresos.get(i).getPagado());
                intent.putExtra("cantidad_horas", listIngresos.get(i).getCantidadHoras());
                intent.putExtra("total", listIngresos.get(i).getTotal());
                posicion = i;
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        // Inflate the layout for this fragment
        mostrarTeclado(edtFiltroPendientes);
        return view;
        //
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            if (activityCaller == 1) {
                //String ingresoNuevo = data.getStringExtra("ingreso");
                Ingreso ingresoNuevo = (Ingreso) data.getSerializableExtra("ingreso");
                listIngresos.add(ingresoNuevo);
                //lvIngresos.setAdapter(ingresosAdapter);
            }else {
                Ingreso ingresoPagado = (Ingreso) data.getSerializableExtra("ingreso");
                listIngresos.remove(ingresoPagado);
            }
            ingresosAdapter.notifyDataSetChanged();
            Collections.sort(listIngresos);
        }
    }

    public void mostrarTeclado(View v){
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public void ocultarTeclado(){
        View v = this.getActivity().getCurrentFocus();
        if (v != null){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}