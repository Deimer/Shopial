package com.puerto.libre.shopial.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.CategoryController;
import com.puerto.libre.shopial.Controllers.StoreController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Category;
import com.puerto.libre.shopial.Models.Store;
import com.puerto.libre.shopial.R;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.lujun.androidtagview.TagContainerLayout;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistroTienda extends AppCompatActivity {

    private Context contexto;
    private UserController userController;
    private StoreController storeController;
    private CategoryController categoryController;
    private SweetDialog dialog;
    private DataOptions data;
    private Validate validate;
    private List<Integer> list_options;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab_next)FloatingActionButton fab_next;
    @Bind(R.id.lbl_titulo_registro)TextView lbl_titulo;

    @NotEmpty(messageId = R.string.name_store_empty, order = 1)
    @Bind(R.id.txt_nombre_tienda)EditText txt_nombre_tienda;
    @NotEmpty(messageId = R.string.brief_description, order = 2)
    @Bind(R.id.txt_description_products)EditText txt_description;

    @Bind(R.id.txt_fan_page)EditText txt_fan_page;
    @Bind(R.id.txt_website)EditText txt_website;
    @Bind(R.id.but_products_sell)Button but_products_sell;
    @Bind(R.id.contenedor_tags)LinearLayout contenedor_tags;
    @Bind(R.id.sp_long_business)MaterialSpinner sp_long_business;
    @Bind(R.id.sp_physical_store)MaterialSpinner sp_physical_store;
    @Bind(R.id.sp_company_person)MaterialSpinner sp_company_person;
    @Bind(R.id.sp_many_employes)MaterialSpinner sp_many_employes;
    @Bind(R.id.sp_manufacture_products)MaterialSpinner sp_manufacture_products;
    @Bind(R.id.sp_online_store)MaterialSpinner sp_online_store;
    @Bind(R.id.sp_fan_page)MaterialSpinner sp_fan_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_tienda);
        ButterKnife.bind(this);
        inicializarElementos();
        inicializarContexto();
        initializeButtonFloating();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setSoftInputMode(
                WindowManager
                        .LayoutParams
                        .SOFT_INPUT_STATE_HIDDEN
        );
    }

    public void inicializarContexto(){
        contexto = this;
        userController = new UserController(contexto);
        storeController = new StoreController(contexto);
        categoryController = new CategoryController(contexto);
        dialog = new SweetDialog(contexto);
        data = new DataOptions();
        validate = new Validate(contexto);
        txt_nombre_tienda.setSelected(false);
        list_options = new ArrayList<>();
        onClickOptions();
        onClickNext();
        onClickWebSite();
        onClickFanPage();
        setOnKeyboardListenerEvents();
    }

    public void inicializarElementos(){
        setSupportActionBar(toolbar);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
    }

    public void initializeButtonFloating(){
        fab_next.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab_next.show(true);
            }
        }, 700);
    }

    public void setOnKeyboardListenerEvents(){
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    fab_next.hide(false);
                } else {
                    fab_next.show(true);
                }
            }
        });
    }

    public void captureDataStore(){
        final boolean isValid = FormValidator.validate(this, new SimpleErrorPopupCallback(contexto));
        if(isValid){
            ArrayList<String> array = new ArrayList<>();
            array.add(txt_nombre_tienda.getText().toString());
            array.add(txt_website.getText().toString());
            array.add(txt_description.getText().toString());
            array.add(sp_long_business.getText().toString());
            array.add(sp_physical_store.getText().toString());
            array.add(sp_company_person.getText().toString());
            array.add(sp_many_employes.getText().toString());
            array.add(sp_manufacture_products.getText().toString());
            array.add(sp_online_store.getText().toString());
            array.add(sp_fan_page.getText().toString());
            array.add(txt_fan_page.getText().toString());
            array.add(Integer.toString(userController.show(contexto).getUser_id()));
            if(!list_options.isEmpty()){
                if(validate.validateValues(array, getString(R.string.select))){
                    Store store = data.convertArrayToStore(array);
                    String categories = getCategories(list_options);
                    registerStore(store, categories);
                    //System.out.println(store);
                }else{
                    dialog.dialogError(getString(R.string.error),getString(R.string.first_select));
                }
            } else {
                dialog.dialogError(getString(R.string.error),getString(R.string.categories_empty));
            }
        }
    }

    public String getCategories(List<Integer> list){
        String categories;
        List<Integer> array = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int code = categoryController.getCode(list.get(i));
            array.add(code);
        }
        categories = new Gson().toJson(array);
        return categories;
    }

    public void onClickOptions(){
        but_products_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Category> list = categoryController.list(contexto);
                List<String> items = data.optionsCategories(list);
                dialogOptionsRegister(true, items);
            }
        });
        sp_long_business.setItems(data.longBusiness());
        sp_physical_store.setItems(data.oneOption());
        sp_company_person.setItems(data.personCompany());
        sp_many_employes.setItems(data.manyEmployees());
        sp_manufacture_products.setItems(data.oneOption());
    }

    public void onClickWebSite(){
        sp_online_store.setItems(data.oneOption());
        sp_online_store.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals(getString(R.string.yes))) {
                    txt_website.setText("");
                    txt_website.setVisibility(View.VISIBLE);
                    txt_website.setFocusable(true);
                } else {
                    txt_website.setVisibility(View.GONE);
                    txt_website.setText(getString(R.string.no_data));
                }
            }
        });
    }

    public void onClickFanPage(){
        sp_fan_page.setItems(data.oneOption());
        sp_fan_page.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals(getString(R.string.yes))) {
                    txt_fan_page.setText("");
                    txt_fan_page.setVisibility(View.VISIBLE);
                    txt_fan_page.setFocusable(true);
                } else {
                    txt_fan_page.setVisibility(View.INVISIBLE);
                    txt_fan_page.setText(getString(R.string.no_data));
                }
            }
        });
    }

    public void dialogOptionsRegister(final Boolean options, final List<String> items){
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_options, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        LinearLayout contenedor_opciones = (LinearLayout)view.findViewById(R.id.contenedor_body_dialog);
        final CheckBox checkBase = (CheckBox)view.findViewById(R.id.checkbox_base_dialog);
        Button but_acept = (Button)view.findViewById(R.id.but_accept_dialog);
        final List<CheckBox> list = new ArrayList<>();
        if(options){
            for (int i = 0; i < items.size(); i++) {
                int idBase = i * 3;
                int id = checkBase.getId() + idBase;
                String detalle = items.get(i);
                CheckBox checkBox = optionCheck(id, detalle);
                list.add(checkBox);
                contenedor_opciones.addView(checkBox);
            }
        }
        but_acept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!arrayIsEmpty(list)) {
                    itemsCategories(list);
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public boolean arrayIsEmpty(List<CheckBox> list){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                return false;
            }
        }
        return true;
    }

    public void itemsCategories(List<CheckBox> list){
        contenedor_tags.removeAllViews();
        List<String> listCategories = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CheckBox cbActual = list.get(i);
            if(cbActual.isChecked()){
                String tag = cbActual.getText().toString();
                listCategories.add(tag);
            }
        }
        createTagLabel(listCategories);
        contenedor_tags.setVisibility(View.VISIBLE);
    }

    public void createTagLabel(List<String> list){
        list_options.clear();
        TagContainerLayout tagContainer = new TagContainerLayout(contexto);
        tagContainer.setTagTextColor(R.color.dark);
        tagContainer.setTagTextSize(26);
        tagContainer.setTags(list);
        tagContainer.setBackgroundColor(Color.parseColor("#00000000"));
        tagContainer.setBorderColor(Color.parseColor("#33666666"));
        tagContainer.getBorderRadius();
        tagContainer.setHorizontalInterval(4);
        tagContainer.setTagBackgroundColor(Color.parseColor("#00000000"));
        tagContainer.setTagBorderColor(Color.parseColor("#330000ff"));
        tagContainer.setTagBorderWidth(1);
        contenedor_tags.addView(tagContainer);
        for (int i = 0; i < list.size(); i++) {
            list_options.add(categoryController.searchForName(list.get(i)));
        }
    }

    public CheckBox optionCheck(int id, String detalle){
        final CheckBox check = new CheckBox(contexto);
        check.setId(id);
        check.setText(detalle);
        check.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        check.setTextColor(Color.parseColor("#545759"));
        check.setPadding(0, 0, 0, 8);
        return check;
    }

    public void onClickNext(){
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureDataStore();
            }
        });
    }

    public void registerStore(final Store store, String categories){
        dialog.dialogProgress(getString(R.string.registrar_tienda));
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.createStore(store, categories, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                System.out.println(jsonObject.toString());
                dialog.cancelarProgress();
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    String message = jsonObject.get("message").getAsString();
                    storeController.create(store);
                    dialog.cancelarProgress();
                    dialog.dialogSuccess("", message);
                    avanzar();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("RegistroTienda(registerStore)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("RegistroTienda(registerStore)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void avanzar(){
        Intent linke = new Intent(RegistroTienda.this, LinkNetwork.class);
        startActivity(linke);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                dialog.dialogWarning(getString(R.string.error),"You must register your store");
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
