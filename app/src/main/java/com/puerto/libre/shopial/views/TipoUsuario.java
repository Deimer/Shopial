package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.puerto.libre.shopial.R;
import butterknife.Bind;
import butterknife.ButterKnife;

public class TipoUsuario extends Activity {

    //Elementos de la vista
    @Bind(R.id.lbl_titulo_tipo_usuario)
    TextView lbl_titulo;
    @Bind(R.id.but_buy)
    Button but_buy;
    @Bind(R.id.but_sell)
    Button but_sell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tipo_usuario);
        ButterKnife.bind(this);
        inicializarElementos();
    }

    public void inicializarElementos(){
        Typeface billabong = Typeface.createFromAsset(getAssets(),"fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        butBuy();
        butSell();
    }

    public void butBuy() {
        but_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(TipoUsuario.this, Login.class);
                login.putExtra("type_user","buy");
                startActivity(login);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    public void butSell(){
        but_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(TipoUsuario.this, Login.class);
                login.putExtra("type_user","sell");
                startActivity(login);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

}
