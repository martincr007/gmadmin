package com.example.gmappadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.gmappadmin.api.CocherasServicio;
import com.example.gmappadmin.models.Ingreso;
import com.example.gmappadmin.models.RequestIngresoBody;
import com.example.gmappadmin.models.SessionPrefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PagadosFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //
    private EditText edtFiltroPagados;
    private ListView lvPagados;
    private Retrofit retrofit;
    CocherasServicio cocherasService;
    ArrayList<Ingreso> listIngresosPagados = new ArrayList<Ingreso>();
    IngresosAdapter ingresosAdapter;
    int activityCaller, posicion;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int idCochera;

    public PagadosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagados, container, false);
        edtFiltroPagados = (EditText) view.findViewById(R.id.edtFiltroPagados);
        lvPagados = (ListView) view.findViewById(R.id.lvPagados);
        ingresosAdapter = new IngresosAdapter(getContext(), listIngresosPagados);
        idCochera = Integer.valueOf(SessionPrefs.get(getContext()).idCocheraAdmin());
        //
        retrofit = new Retrofit.Builder()
                .baseUrl(CocherasServicio.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cocherasService = retrofit.create(CocherasServicio.class);
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        RequestIngresoBody requestIngresoBody = new RequestIngresoBody("S",
                sdFormat.format(new Date()), idCochera);
        Call<ArrayList<Ingreso>> call = cocherasService.obtenerIngresosEstadoFecha(requestIngresoBody);
        call.enqueue(new Callback(){
            @Override
            public void onResponse(Call call, Response response) {
                listIngresosPagados = (ArrayList<Ingreso>) response.body();
                Collections.sort(listIngresosPagados);
                ingresosAdapter = new IngresosAdapter(getContext(), listIngresosPagados);
                lvPagados.setAdapter(ingresosAdapter);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                /*Toast.makeText(getContext(), "No se pudo procesar la petición",
                        Toast.LENGTH_LONG).show();*/
            }
        });

        //Para filtro
        edtFiltroPagados.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*if (listIngresosPagados.size()==0) {
                    return;
                }*/
            }

            @Override
            public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {
                //if (listIngresosPagados.size()>0) {
                    ingresosAdapter.filtrar(edtFiltroPagados.getText().toString().toLowerCase());
                //}
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //
        lvPagados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activityCaller = 2;
                Intent intent = new Intent(getContext(), SalidaActivity.class);
                intent.putExtra("dominio", listIngresosPagados.get(i).getDominio());
                intent.putExtra("tipo_rodado", String.valueOf(listIngresosPagados.get(i).getTipoRodado()));
                intent.putExtra("fechahora", listIngresosPagados.get(i).getFechaHora());
                intent.putExtra("id", listIngresosPagados.get(i).getId());
                intent.putExtra("pagado", listIngresosPagados.get(i).getPagado());
                intent.putExtra("cantidad_horas", String.valueOf(listIngresosPagados.get(i).getCantidadHoras()));
                intent.putExtra("total", String.valueOf(listIngresosPagados.get(i).getTotal()));
                posicion = i;
                startActivity(intent);
                //startActivityForResult(intent, 1);
            }
        });
        // Inflate the layout for this fragment
        mostrarTeclado(edtFiltroPagados);
        return view;
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