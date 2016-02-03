package com.puerto.libre.shopial.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.puerto.libre.shopial.DataManager.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.R;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

public class RegistroTienda extends AppCompatActivity {

    private Context contexto;
    private SweetDialog dialog;
    private DataOptions data;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab_registro_tienda)FloatingActionButton fab;
    @Bind(R.id.lbl_about_store)TextView lbl_titulo;
    @Bind(R.id.txt_nombre_tienda)EditText txt_nombre_tienda;
    @Bind(R.id.txt_website)EditText txt_website;
    @Bind(R.id.but_products_sell)Button but_products_sell;
    @Bind(R.id.contenedor_tags)LinearLayout contenedor_tags;
    @Bind(R.id.but_long_business)Button but_long_business;
    @Bind(R.id.but_physical_store)Button but_physical_store;
    @Bind(R.id.but_company_person)Button but_company_person;
    @Bind(R.id.but_many_employes)Button but_many_employes;
    @Bind(R.id.but_manufacture_products)Button but_manufacture_products;
    @Bind(R.id.but_online_store)Button but_online_store;
    @Bind(R.id.but_volver_registro)ImageButton but_volver_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);
        ButterKnife.bind(this);
        inicializarElementos();
        inicializarContexto();
    }

    public void inicializarContexto(){
        contexto = this;
        dialog = new SweetDialog(contexto);
        data = new DataOptions();
        onClickProductSell();
        onClickPhysicalStore();
    }

    public void inicializarElementos(){
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNext(view);
            }
        });
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
    }

    public void onClickNext(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void onClickProductSell(){
        but_products_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> items = data.optionCategories();
                dialogOptionsRegister(true, items);
            }
        });
    }

    public void onClickPhysicalStore(){
        but_physical_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> items = data.longBusiness();
                dialogOptionsRegister(false, items);
            }
        });
    }

    public void dialogOptionsRegister(Boolean options, List<String> items){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(view);
        LinearLayout contenedor_opciones = (LinearLayout)view.findViewById(R.id.contenedor_body_dialog);
        CheckBox checkBase = (CheckBox)view.findViewById(R.id.checkbox_base_dialog);
        RadioButton radioBase = (RadioButton)view.findViewById(R.id.radio_base_dialog);
        Button but_acept = (Button)view.findViewById(R.id.but_accept_dialog);
        if(options){
            for (int i = 0; i < items.size(); i++) {
                int id = checkBase.getId() + i;
                String detalle = items.get(i);
                CheckBox checkBox = optionCheck(id, detalle);
                contenedor_opciones.addView(checkBox);
            }
        }else{
            radioOptions(items,radioBase);
        }
        but_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        builder.create().show();
    }

    public CheckBox optionCheck(int id, String detalle){
        final CheckBox check = new CheckBox(contexto);
        check.setId(id);
        check.setText(detalle);
        check.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        check.setTextColor(Color.parseColor("#545759"));
        return check;
    }

    public RadioGroup radioOptions(List<String> items, RadioButton radio){
        final RadioGroup group = new RadioGroup(contexto);
        group.setOrientation(LinearLayout.VERTICAL);
        int id = radio.getId();
        for (int i = 0; i < items.size(); i++) {
            String detalle = items.get(i);
            final RadioButton option = new RadioButton(contexto);
            option.setId(id + i);
            option.setText(detalle);
            option.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            option.setTextColor(Color.parseColor("#545759"));
            group.addView(option);
        }
        return group;
    }



}
