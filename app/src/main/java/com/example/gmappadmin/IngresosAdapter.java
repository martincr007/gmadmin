package com.example.gmappadmin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.gmappadmin.models.Ingreso;
import java.util.ArrayList;

public class IngresosAdapter extends BaseAdapter{
    //private LayoutInflater mInflater;
    private ArrayList<Ingreso> listIngresos;
    private LayoutInflater mInflater;
    ArrayList<Ingreso> copiaListIngresos = new ArrayList<Ingreso>();
    public IngresosAdapter(Context context, ArrayList<Ingreso> listCocheras) {
        mInflater = LayoutInflater.from(context);
        this.listIngresos = listCocheras;
        copiaListIngresos.addAll(listCocheras);
    }

    public int getCount() {
        return listIngresos.size();
    }

    public Object getItem(int position) { return listIngresos.get(position); }

    public long getItemId(int position) {
        return listIngresos.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_custom, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(android.R.id.text1);
            holder.subtext = (TextView) convertView.findViewById(android.R.id.text2);
            //holder.icon = (ImageView) convertView.findViewById(R.id.managementObjectIcon);
            convertView.setTag(holder);
            //Prueba para intercambiar el color de los items
            /*if (position % 2 == 1){
                convertView.setBackgroundColor(Color.GREEN);
            }else{
                convertView.setBackgroundColor(Color.GRAY);
            }*/
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //
        String horaIngreso = listIngresos.get(position).getFechaHora().substring(11);
        //
        holder.text.setText(listIngresos.get(position).getDominio());
        if (listIngresos.get(position).getTipoRodado() == 'A'){
            holder.subtext.setText("Auto | Ingreso: " + horaIngreso);
        }else if (listIngresos.get(position).getTipoRodado() == 'C'){
            holder.subtext.setText("Camioneta | Ingreso: " + horaIngreso);
        }else{
            holder.subtext.setText("Moto | Ingreso: " + horaIngreso);
        }
        //holder.subtext.setText(listIngresos.get(position).getTipoRodado());
        //holder.icon.setImageResource(R.drawable.user);
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        TextView subtext;
        //ImageView icon;
    }

    /* Filtra los datos del adaptador */
    public void filtrar(String texto) {
        // Elimina todos los datos del ArrayList que se cargan en los
        // elementos del adaptador
        listIngresos.clear();
        // Si no hay texto: agrega de nuevo los datos del ArrayList copiado
        // al ArrayList que se carga en los elementos del adaptador
        if (texto.length() == 0) {
            listIngresos.addAll(copiaListIngresos);
        } else {
            // Recorre todos los elementos que contiene el ArrayList copiado
            // y dependiendo de si estos contienen el texto ingresado por el usuario
            for (Ingreso ing : copiaListIngresos) {
                if (ing.getDominio().toLowerCase().contains(texto)) {
                    listIngresos.add(ing);
                }
            }
        }
        // Actualiza el adaptador para aplicar los cambios
        notifyDataSetChanged();
    }
}

