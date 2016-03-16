package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.R;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class Inicio extends Activity {

    private UserController userController;

    //Elementos de la vista
    @Bind(R.id.lbl_nombre_app)TextView lbl_titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicio);
        ButterKnife.bind(this);
        initInstanceFacebook();
        inicializarContexto();
        initialThread();
    }

    public void inicializarContexto(){
        Context contexto = this;
        userController = new UserController(contexto);
        Typeface billabong = Typeface.createFromAsset(getAssets(),"fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
    }

//region Hilos de tiempo
    public void initialThread(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animarTitulo();
            }
        }, 2000);
    }

    public void animarTitulo(){
        SmallBang sBang = SmallBang.attach2Window(this);
        sBang.bang(lbl_titulo, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                animar();
                avanzar();
            }
            @Override
            public void onAnimationEnd(){}
        });
        lbl_titulo.setVisibility(View.VISIBLE);
    }

    public void avanzar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inicializarPantalla();
            }
        }, 3000);
    }

    public void animar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.ZoomOut)
                        .duration(700)
                        .playOn(lbl_titulo);
            }
        }, 2000);
    }
//endregion
    public void avanzarTipoUsuario(){
        Intent tpUsuario = new Intent(Inicio.this, TipoUsuario.class);
        startActivity(tpUsuario);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void avanzarMenu(){
        Intent home = new Intent(Inicio.this, Home.class);
        startActivity(home);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void inicializarPantalla(){
        boolean sesionActiva = userController.session();
        if(sesionActiva){
            avanzarMenu();
        }else{
            avanzarTipoUsuario();
        }
    }

    public void initInstanceFacebook(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.puerto.libre.shopial",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Inicio(NameNotFoundException): ", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("Inicio(NoSuchAlgorithmException): ", e.getMessage());
        }
    }

}
