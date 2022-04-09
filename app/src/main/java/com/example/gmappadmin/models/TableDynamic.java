package com.example.gmappadmin.models;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Moth on 06/06/2021.
 */

public class TableDynamic {

    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC, indexR;
    private boolean multicolor = false;
    int firstColor, secondcolor;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void addHeader(String[] header){
        this.header = header;
        createHeader();
    }

    public void addData(ArrayList<String[]> data){
        this.data = data;
        createDataTable();
    }

    private void newRow(){
        tableRow = new TableRow(context);
    }

    private void newCell(){
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(18);
    }

    private void createHeader(){
        indexC=0;
        newRow();
        while (indexC<header.length){
            newCell();
            txtCell.setText(header[indexC++]);
            txtCell.setAllCaps(true);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private TableRow.LayoutParams newTableRowParams(){
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(2,1,2,1);
        params.weight=1;
        return params;
    }

    private TableRow getRow(int index){
        return (TableRow)tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowIndex, int colIndex){
        tableRow = getRow(rowIndex);
        return (TextView)tableRow.getChildAt(colIndex);
    }

    public void backgroudHeader(int color){
        indexC=0;
        while (indexC<header.length){
            txtCell = getCell(0,indexC++);
            txtCell.setBackgroundColor(color);
        }
    }

    public void backgroudData(int firstColor, int secondcolor) {
        for(indexR=1; indexR<=data.size(); indexR++){
            multicolor = !multicolor;
            for(indexC=0; indexC<=header.length-1; indexC++){
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor((multicolor)?firstColor:secondcolor);
            }
        }
        this.firstColor = firstColor;
        this.secondcolor = secondcolor;
    }

    public void reColoring(){
        indexC=0;
        multicolor = !multicolor;
        while (indexC < header.length){
            txtCell = getCell(data.size()-1,indexC++);
            txtCell.setBackgroundColor((multicolor)?firstColor:secondcolor);
        }
    }

    private void createDataTable(){
        String info;
        for(indexR=1; indexR<=data.size(); indexR++){
            newRow();
            for(indexC=0; indexC<=header.length-1; indexC++){
                newCell();
                String[] row = data.get(indexR-1);
                info = (indexC<row.length)?row[indexC]:"";
                txtCell.setText(info);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    public void addItem(String[] item){
        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while (indexC<header.length) {
            newCell();
            info = (indexC<item.length)?item[indexC++]:"";
            txtCell.setText(info);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow, data.size() - 1);
        reColoring();
    }

}
