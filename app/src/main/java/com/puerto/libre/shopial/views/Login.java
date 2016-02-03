package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import com.puerto.libre.shopial.social.LoginFacebook;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends Activity {

    private Context contexto;
    private UserController userController;
    private SocialController socialController;
    private SweetDialog dialog;
    private Validate validate;
    private String type_user;
    public static String instagram_client_id;
    public static String instagram_client_secret;
    public static String instagram_redirect_uri;

    @Bind(R.id.lbl_titulo_app)TextView lbl_titulo;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;
    @Bind(R.id.but_login)Button but_login;
    @Bind(R.id.but_login_instagram)ImageButton but_login_instagram;
    @Bind(R.id.but_login_facebook)ImageButton but_login_facebook;
    @Bind(R.id.but_login_google)ImageButton but_login_google;
    @Bind(R.id.but_registrar)Button but_registrar;
    @Bind(R.id.contenedor_login_social)LinearLayout contenedor_social;
    @Bind(R.id.contenedor_middle)LinearLayout contenedor_middle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        inicializarContexto();
        iniciarCargaDatos();
        onClickLogin();
        onClickRegistro();
        onClickLoginSocialInstagram();
        onClickSocialFacebook();
    }

    public void inicializarContexto(){
        contexto = this;
        userController = new UserController(contexto);
        socialController = new SocialController(contexto);
        dialog = new SweetDialog(contexto);
        validate = new Validate(contexto);
        instagram_client_id = getString(R.string.client_id);
        instagram_client_secret = getString(R.string.client_secret);
        instagram_redirect_uri = getString(R.string.redirect_uri);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
    }

    public void iniciarCargaDatos(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            type_user = bundle.getString("type_user");
            if(type_user.equalsIgnoreCase("sell")){
                contenedor_social.setVisibility(View.INVISIBLE);
                contenedor_middle.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void onClickLogin(){
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                    dialog.dialogToastError(getString(R.string.alerta_campos_vacios));
                } else {
                    boolean hayConexion = validate.connect();
                    if (hayConexion) {
                        login(username, password);
                    } else {
                        dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
                    }
                }
            }
        });
    }

    public void onClickRegistro(){
        but_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(Login.this, RegistroUsuario.class);
                registro.putExtra("type_user",type_user);
                startActivity(registro);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }

    public void onClickLoginSocialInstagram(){
        but_login_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnect = validate.connect();
                if (isConnect) {
                    InstaLogin instaLogin = new InstaLogin(
                            Login.this,
                            instagram_client_id,
                            instagram_client_secret,
                            instagram_redirect_uri
                    );
                    instaLogin.login();
                }else{
                    dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
                }
            }
        });
    }

    public void onClickSocialFacebook(){
        but_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnect = validate.connect();
                if (isConnect) {
                    Intent login_facebook = new Intent(Login.this, LoginFacebook.class);
                    startActivity(login_facebook);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
                }
            }
        });
    }

    //public void loginFacebook(){
    //    DataOptions dataOptions = new DataOptions();
    //    String applicationId =  getResources().getString(R.string.facebook_app_id);
    //    ArrayList<String> permissions = dataOptions.scopesFacebook();
    //    FacebookLoginActivity.launch(this, applicationId, permissions);
    //}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String full_name = bundle.getString(InstaLogin.FULLNAME);
                String username = bundle.getString(InstaLogin.USERNAME);
                String avatar = bundle.getString(InstaLogin.PROFILE_PIC);
                String provider = "instagram";
                String id_provider = bundle.getString(InstaLogin.ID);
                String access_token = bundle.getString(InstaLogin.ACCESS_TOKEN);
                loginSocial(full_name,username,avatar,id_provider,access_token,provider);
            }
        }
    }

    public void loginSocial(String full_name, String username, String avatar, String id_provider, String token, String provider){
        Social social = new Social();
        social.setFull_name(full_name);
        social.setUsername(username);
        social.setAvatar(avatar);
        social.setId_user_provider(id_provider);
        social.setSocial_token(token);
        social.setProvider(provider);
        boolean save = socialController.create(social);
        if(save){
            dialog.dialogToastOk("Sign in Successful");
            avanzar();
        }
    }

    public void login(String username, String password){
        dialog.dialogProgress(getString(R.string.inicio_sesion_1));
        final String url = getString(R.string.url_test);
        Boolean remember = true;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.login(username, password, remember, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    User user = new Gson().fromJson(jsonObject.get("data"), User.class);
                    boolean create = userController.create(user);
                    if (create) {
                        dialog.cancelarProgress();
                        avanzar();
                    } else {
                        String message = jsonObject.get("message").getAsString();
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
                    Log.e("Login(login)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(login)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void avanzar(){
        Intent menu = new Intent(Login.this, Home.class);
        startActivity(menu);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}
