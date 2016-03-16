package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.CategoryController;
import com.puerto.libre.shopial.Controllers.CountryController;
import com.puerto.libre.shopial.Controllers.StateController;
import com.puerto.libre.shopial.Controllers.SubcategoryController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Models.Category;
import com.puerto.libre.shopial.Models.Country;
import com.puerto.libre.shopial.Models.State;
import com.puerto.libre.shopial.Models.Subcategory;
import com.puerto.libre.shopial.R;
import java.io.IOException;
import java.io.InputStream;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TipoUsuario extends Activity {

    private CountryController countryController;
    private StateController stateController;
    private SubcategoryController subcategoryController;
    private CategoryController categoryController;
    private SweetDialog dialog;
    private DataOptions data;

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
        inicializarContexto();
        inicializarElementos();
        loadDataCategories();
    }

    public void inicializarContexto(){
        Context contexto = this;
        countryController = new CountryController(contexto);
        stateController = new StateController(contexto);
        subcategoryController = new SubcategoryController(contexto);
        categoryController = new CategoryController(contexto);
        dialog = new SweetDialog(contexto);
        data = new DataOptions();
        loadDataRegion();
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
                login.putExtra("type_user", "buy");
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

//region Descarga de datos para configuracion de la aplicacion
    public void loadDataRegion(){
        boolean loadData = countryController.isLoadData();
        if(!loadData){
            loadRegions load = new loadRegions();
            load.execute();
        }
    }

    public void loadDataCategories(){
        boolean loadData = categoryController.isLoadData();
        if(!loadData){
            loadCategories();
        }
    }

    public boolean saveJsonFromAsset(){
        boolean res = true;
        try{
            InputStream is = getAssets().open("data_region/regions.json");
            String json = data.loadJsonFromAsset(is);
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonCountries = jsonArray.get(i).getAsJsonObject();
                Country country = new Country();
                country.setId(jsonCountries.get("id").getAsInt());
                country.setName(jsonCountries.get("name").getAsString());
                boolean create1 = countryController.create(country);
                if(create1){
                    JsonArray arrayStates = jsonCountries.get("states").getAsJsonArray();
                    for (int j = 0; j < arrayStates.size(); j++) {
                        JsonObject jsonStates = arrayStates.get(j).getAsJsonObject();
                        State state = new State();
                        state.setId(jsonStates.get("id").getAsInt());
                        state.setName(jsonStates.get("name").getAsString());
                        state.setCountry_id(jsonStates.get("country_id").getAsInt());
                        stateController.create(state);
                    }
                }else{
                    return false;
                }
            }
            System.out.println("Paises y estados cargados correctamente");
        }catch (IOException ex){
            Log.e("TipoUsuario(saveJsonFromAsset)", "Error ex: " + ex.getMessage());
            return false;
        }
        return res;
    }

    public void loadCategories(){
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.getCategories(new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if(success){
                    JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonCat = jsonArray.get(i).getAsJsonObject();
                        Category category = new Gson().fromJson(jsonCat, Category.class);
                        boolean create = categoryController.create(category);
                        if(create){
                            JsonArray array = jsonCat.get("subcategories").getAsJsonArray();
                            for (int j = 0; j < array.size(); j++) {
                                JsonObject jsonSub = array.get(j).getAsJsonObject();
                                Subcategory subcategory = new Gson().fromJson(jsonSub, Subcategory.class);
                                subcategory.setCategory_id(category.getCode());
                                subcategoryController.create(subcategory);
                            }
                        }
                    }
                    System.out.println("Categorias y subcategorias cargadas correctamente");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Log.e("Login(login)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(login)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    private class loadRegions extends AsyncTask<String, Integer, Boolean> {
        protected void onPreExecute(){
            dialog.dialogProgress(getString(R.string.load_data_start));
        }
        @Override
        protected Boolean doInBackground(String... params) {
            return saveJsonFromAsset();
        }
        @Override
        protected void onPostExecute(Boolean result) {
            dialog.cancelarProgress();
        }
    }
//endregion

}
