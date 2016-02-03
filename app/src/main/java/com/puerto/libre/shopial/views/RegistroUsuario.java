package com.puerto.libre.shopial.views;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.CityController;
import com.puerto.libre.shopial.Controllers.CountryController;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Controllers.StateController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import eu.inmite.android.lib.validations.form.FormValidator;
import eu.inmite.android.lib.validations.form.annotations.MinLength;
import eu.inmite.android.lib.validations.form.annotations.NotEmpty;
import eu.inmite.android.lib.validations.form.annotations.RegExp;
import eu.inmite.android.lib.validations.form.callback.SimpleErrorPopupCallback;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

public class RegistroUsuario extends AppCompatActivity {

    private Context contexto;
    private UserController userController;
    private SocialController socialController;
    private CountryController countryController;
    private StateController stateController;
    private CityController cityController;
    private SweetDialog dialog;
    private Validate validate;
    private String type_user;
    private boolean social_register;
    private String avatar;
    private Social social;

    public static String instagram_client_id;
    public static String instagram_client_secret;
    public static String instagram_redirect_uri;

    ImageView iconSiguiente;
    ImageView iconInstagram;
    ImageView iconFacebook;
    ImageView iconGoogle;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab_registro_usuario)FloatingActionButton fab;
    @Bind(R.id.lbl_about_you)TextView lbl_titulo;
    @Bind(R.id.contenedor_foto)LinearLayout layout_foto;
    @Bind(R.id.profile_image)CircleImageView profile_image;

    @NotEmpty(messageId = R.string.name_vacio, order = 1)
    @Bind(R.id.txt_nombres)EditText txt_nombres;

    @NotEmpty(messageId = R.string.name_vacio, order = 2)
    @Bind(R.id.txt_apellidos)EditText txt_apellidos;

    @NotEmpty(messageId = R.string.username_vacio, order = 3)
    @Bind(R.id.txt_telefono_registro)EditText txt_telefono;

    @NotEmpty(messageId = R.string.email_vacio, order = 4)
    @RegExp(value = EMAIL, messageId = R.string.email_vacio, order = 5)
    @Bind(R.id.txt_email)EditText txt_email;

    @NotEmpty(messageId = R.string.username_vacio, order = 6)
    @Bind(R.id.txt_usuario)EditText txt_username;

    @MinLength(value = 6, messageId = R.string.pass_vacia, order = 7)
    @Bind(R.id.txt_password_registro)EditText txt_password;

    @NotEmpty(messageId = R.string.country_vacio, order = 8)
    @Bind(R.id.txt_pais_registro)AutoCompleteTextView txt_country;

    @NotEmpty(messageId = R.string.state_vacio, order = 9)
    @Bind(R.id.txt_estado_registro)AutoCompleteTextView txt_state;

    @NotEmpty(messageId = R.string.city_vacio, order = 10)
    @Bind(R.id.txt_ciudad_registro)AutoCompleteTextView txt_city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        ButterKnife.bind(this);
        inicializarContexto();
        inicializarElementos();
        iniciarCargaDato();
    }

    public void inicializarContexto(){
        contexto = this;
        userController = new UserController(contexto);
        socialController = new SocialController(contexto);
        countryController = new CountryController(contexto);
        stateController = new StateController(contexto);
        cityController = new CityController(contexto);
        dialog = new SweetDialog(contexto);
        validate = new Validate(contexto);
        iconSiguiente = new ImageView(contexto);
        iconInstagram = new ImageView(contexto);
        iconFacebook = new ImageView(contexto);
        iconGoogle = new ImageView(contexto);
        social_register = false;
        avatar = "";
        inicializarVariablesSocial();
    }

    public void inicializarVariablesSocial(){
        instagram_client_id = getString(R.string.client_id);
        instagram_client_secret = getString(R.string.client_secret);
        instagram_redirect_uri = getString(R.string.redirect_uri);
    }

    public void inicializarElementos(){
        setSupportActionBar(toolbar);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        onClickNext();
        inicializarAutoCompletar();
        eventsText();
    }

    public void onClickNext(){
        SubActionButton.Builder builder = new SubActionButton.Builder(this);
        iconSiguiente.setImageDrawable(getResources().getDrawable(R.drawable.next_mini));
        iconInstagram.setImageDrawable(getResources().getDrawable(R.drawable.instagram_mini));
        iconFacebook.setImageDrawable(getResources().getDrawable(R.drawable.facebook_mini));
        iconGoogle.setImageDrawable(getResources().getDrawable(R.drawable.google_mini));
        final FloatingActionMenu menuFab = new FloatingActionMenu.Builder(this)
                .addSubActionView(builder.setContentView(iconSiguiente).build())
                .addSubActionView(builder.setContentView(iconInstagram).build())
                .addSubActionView(builder.setContentView(iconFacebook).build())
                .addSubActionView(builder.setContentView(iconGoogle).build())
                .attachTo(fab)
                .build();
        menuFab.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
            @Override
            public void onMenuOpened(FloatingActionMenu floatingActionMenu) {
                fab.setRotation(0);
                PropertyValuesHolder property = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(fab, property);
                animator.start();
            }

            @Override
            public void onMenuClosed(FloatingActionMenu floatingActionMenu) {
                fab.setRotation(45);
                PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
                ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fab, pvhR);
                animation.start();
            }
        });
        iconSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSocial();
            }
        });
    }

    public void dataSocial(){
        boolean isConnect = validate.connect();
        if(isConnect){
            InstaLogin instaLogin = new InstaLogin(
                    RegistroUsuario.this,
                    instagram_client_id,
                    instagram_client_secret,
                    instagram_redirect_uri
            );
            instaLogin.login();
        }else{
            dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String full_name = bundle.getString(InstaLogin.FULLNAME);
                String username = bundle.getString(InstaLogin.USERNAME);
                avatar = bundle.getString(InstaLogin.PROFILE_PIC);
                String provider = "instagram";
                String id_provider = bundle.getString(InstaLogin.ID);
                String access_token = bundle.getString(InstaLogin.ACCESS_TOKEN);
                registerSocial(full_name, username, avatar, id_provider, access_token, provider);
            }
        }
    }

    public void registerSocial(String full_name, String username, String avatar,
                            String id_provider, String token, String provider){
        social = new Social();
        social.setFull_name(full_name);
        social.setUsername(username);
        social.setAvatar(avatar);
        social.setId_user_provider(id_provider);
        social.setSocial_token(token);
        social.setProvider(provider);
        social_register = true;
        txt_nombres.setText(full_name);
        txt_username.setText(username);
        showAvatar(avatar);
        dialog.dialogToastInfo("Your social network data loaded correctly");
    }

    public void showAvatar(final String imageUrl){
        SmallBang sBang = SmallBang.attach2Window(this);
        sBang.bang(layout_foto, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .fit()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.ic_launcher)
                        .into(profile_image);
                layout_foto.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(){}
        });
    }

    public void iniciarCargaDato(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            type_user = bundle.getString("type_user");
        }
    }

    public void inicializarAutoCompletar(){
        String name = txt_country.getText().toString();
        List<String> list = countryController.search(name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1,list);
        txt_country.setAdapter(adapter);
    }

    public void eventsText(){
        txt_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int id = countryController.getId(txt_country.getText().toString());
                List<String> list = stateController.searchForCountryId(id);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1,list);
                txt_state.setAdapter(adapter);
            }
        });
        txt_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int id = stateController.getId(txt_state.getText().toString());
                List<String> list = cityController.searchForStateId(id);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1,list);
                txt_city.setAdapter(adapter);
            }
        });
    }

    public boolean validateRegion(){
        boolean res = false;
        String country = txt_country.getText().toString();
        String state = txt_state.getText().toString();
        String city = txt_city.getText().toString();
        if(countryController.getId(country) > 0){
            if(stateController.getId(state) > 0){
                if(cityController.getId(city) > 0){
                    res = true;
                }else{
                    dialog.dialogWarning("Error",getString(R.string.city_error));
                }
            }else{
                dialog.dialogWarning("Error",getString(R.string.state_error));
            }
        }else{
            dialog.dialogWarning("Error", getString(R.string.country_error));
        }
        return res;
    }

    public void registrarUsuario(){
        final boolean isValid = FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        if(isValid){
            if(validateRegion()){
                String nombres = txt_nombres.getText().toString();
                String apellidos = txt_apellidos.getText().toString();
                String username = txt_username.getText().toString();
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                String phone = txt_telefono.getText().toString();
                int city_id = cityController.getId(txt_city.getText().toString());
                Boolean active = true;
                Integer profile = 2;
                if(type_user.equalsIgnoreCase("sell")){
                    active = false;
                    profile = 1;
                }
                register(nombres,apellidos,username,email,password,phone,active,profile,city_id);
            }
        }
    }

    public void register(final String first_name, final String last_name, final String username,
                         final String email, final String password, final String phone,
                         final Boolean active, final Integer profile, final Integer city){
        dialog.dialogProgress(getString(R.string.registrar_usuario));
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.register(first_name, last_name, username, email, phone, active, social_register, avatar, profile, city, password, password, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                String message = jsonObject.get("message").getAsString();
                if (success) {
                    User user = serializar(first_name, last_name, username, email, password, phone, active, profile, city);
                    boolean create = userController.create(user);
                    if (create) {
                        dialog.cancelarProgress();
                        dialog.dialogToast(message);
                        if(social_register){socialController.create(social);}
                        avanzar();
                    } else {
                        dialog.cancelarProgress();
                        dialog.dialogBasic(message);
                    }
                }
            }
            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("RegistroUsuario(register)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("RegistroUsuario(register)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public User serializar(String nombres, String apellidos, String username,
                           String email, String password, String phone,
                           Boolean active, Integer profile, Integer city){
        User user = new User();
        user.setFirst_name(nombres);
        user.setLast_name(apellidos);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setCitiy(city);
        user.setActive(active);
        user.setSocial(social_register);
        user.setImage_profile_url(avatar);
        user.setProfile(profile);
        return user;
    }

    public void avanzar(){
        if(type_user.equalsIgnoreCase("sell")){
            Intent menu = new Intent(RegistroUsuario.this, RegistroTienda.class);
            startActivity(menu);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(type_user.equalsIgnoreCase("buy")){
            Intent menu = new Intent(RegistroUsuario.this, Home.class);
            startActivity(menu);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

}
