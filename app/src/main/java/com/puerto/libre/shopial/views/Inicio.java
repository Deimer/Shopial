package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.CityController;
import com.puerto.libre.shopial.Controllers.CountryController;
import com.puerto.libre.shopial.Controllers.StateController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Models.City;
import com.puerto.libre.shopial.Models.Country;
import com.puerto.libre.shopial.Models.State;
import com.puerto.libre.shopial.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class Inicio extends Activity {

    private UserController userController;
    private CountryController countryController;
    private StateController stateController;
    private CityController cityController;

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
        inicializarContexto();
        hiloTemporal();
    }

    public void inicializarContexto(){
        Context contexto = this;
        userController = new UserController(contexto);
        countryController = new CountryController(contexto);
        stateController = new StateController(contexto);
        cityController = new CityController(contexto);
        Typeface billabong = Typeface.createFromAsset(getAssets(),"fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        loadDataRegion();
    }

//region Hilos de tiempo
    public void hiloTemporal(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animarTitulo();
            }
        }, 2000);
    }
    public void avanzar(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inicializarPantalla();
            }
        }, 4000);
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

    public void animarTitulo(){
        SmallBang sBang = SmallBang.attach2Window(this);
        sBang.bang(lbl_titulo, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                animar();
                avanzar();
            }
            @Override
            public void onAnimationEnd() {
            }
        });
        lbl_titulo.setVisibility(View.VISIBLE);
    }

    public void avanzarTipoUsuario(){
        Intent tpUsuario = new Intent(Inicio.this, TipoUsuario.class);
        startActivity(tpUsuario);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void avanzarMenu(){
        Intent menu = new Intent(Inicio.this, Home.class);
        startActivity(menu);
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

    public void loadDataRegion(){
        boolean loadData = countryController.isLoadData();
        if(!loadData){
            loadRegion();
        }
    }

//region Descarga de datos para configuracion de la aplicacion

    public void loadRegion(){
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.load(new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonCountries = jsonArray.get(i).getAsJsonObject();
                    Country country = new Country();
                    country.setId(jsonCountries.get("id").getAsInt());
                    country.setName(jsonCountries.get("name").getAsString());
                    countryController.create(country);
                    JsonArray arrayStates = jsonCountries.get("states").getAsJsonArray();
                    for (int j = 0; j < arrayStates.size(); j++) {
                        JsonObject jsonStates = arrayStates.get(j).getAsJsonObject();
                        State state = new State();
                        state.setId(jsonStates.get("id").getAsInt());
                        state.setName(jsonStates.get("name").getAsString());
                        state.setCountry_id(jsonStates.get("country_id").getAsInt());
                        stateController.create(state);
                        JsonArray arrayCities = jsonStates.get("cities").getAsJsonArray();
                        for (int k = 0; k < arrayCities.size(); k++) {
                            JsonObject jsonCities = arrayCities.get(k).getAsJsonObject();
                            City city = new City();
                            city.setId(jsonCities.get("id").getAsInt());
                            city.setName(jsonCities.get("name").getAsString());
                            city.setState_id(jsonCities.get("state_id").getAsInt());
                            cityController.create(city);
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.i("Inicio(loadRegion)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(loadRegion)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

//endregion

}
