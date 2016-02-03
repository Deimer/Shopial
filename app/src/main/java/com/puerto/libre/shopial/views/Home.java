package com.puerto.libre.shopial.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    private Context contexto;
    private SweetDialog dialog;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab)FloatingActionButton fab;
    @Bind(R.id.lbl_toolbar_app)TextView lbl_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        inicializarContexto();
        inicializarElementos();
    }

    public void inicializarContexto(){
        contexto = this;
        dialog = new SweetDialog(contexto);
    }

    public void inicializarElementos(){
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_toolbar.setTypeface(billabong);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
