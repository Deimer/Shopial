package com.puerto.libre.shopial.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.CityController;
import com.puerto.libre.shopial.Controllers.CountryController;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Controllers.StateController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.City;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
import retrofit.mime.TypedFile;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;
import static eu.inmite.android.lib.validations.form.annotations.RegExp.EMAIL;

public class RegistroUsuario extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Context contexto;
    private UserController userController;
    private SocialController socialController;
    private CountryController countryController;
    private StateController stateController;
    private CityController cityController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions dataOptions;
    private String type_user;
    private boolean social_register;
    private Social social;
    private int photoSocial;
    private File file;

    //Variables para funciones de instagram
    public static String instagram_client_id;
    public static String instagram_client_secret;
    public static String instagram_redirect_uri;

    //Variables para funciones de facebook
    private CallbackManager callbackManager;

    //Variables para funciones de google
    private GoogleApiClient googleApiClient;
    private boolean mShouldResolve = false;
    private boolean mIsResolving = false;
    private static final int rc_sign_in = 0;
    static final int REQUEST_CODE_ASK_PERMISSIONS = 0;
    private String googleEmail;

    //region Elementos de interaccion
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.but_volver_login)ImageButton but_volver;
    @Bind(R.id.lbl_about_you)TextView lbl_titulo;
    @Bind(R.id.contenedor_foto)LinearLayout layout_foto;
    @Bind(R.id.profile_image)CircleImageView profile_image;
    //endregion

    //region Botones flotantes
    @Bind(R.id.fab_registro_usuario)FloatingActionMenu menu_fab;
    @Bind(R.id.fab_next)FloatingActionButton fab_next;
    @Bind(R.id.fab_instagram)FloatingActionButton fab_instagram;
    @Bind(R.id.fab_facebook)FloatingActionButton fab_facebook;
    @Bind(R.id.fab_google)FloatingActionButton fab_google;
    @Bind(R.id.but_register_facebook)LoginButton but_register_facebook;
    @Bind(R.id.but_register_google)SignInButton but_register_google;
    //endregion

    //region Elementos del formulario
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
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registro_usuario);
        ButterKnife.bind(this);
        inicializarContexto();
        inicializarElementos();
        initializeButtonFloating();
        iniciarCargaDato();
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

    //region Funciones de lanza
    public void inicializarContexto(){
        contexto = this;
        userController = new UserController(contexto);
        socialController = new SocialController(contexto);
        countryController = new CountryController(contexto);
        stateController = new StateController(contexto);
        cityController = new CityController(contexto);
        dialog = new SweetDialog(contexto);
        validate = new Validate(contexto);
        dataOptions = new DataOptions();
        social = new Social();
        social_register = false;
        photoSocial = 0;
        txt_nombres.setSelected(false);
        inicializarVariablesSocial();
        initFacebookInstances();
        initGoogleInstance();
        setOnKeyboardListenerEvents();
    }

    public void inicializarElementos(){
        setSupportActionBar(toolbar);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        onClickNext();
        onClickFabFacebook();
        onClickFabGoogle();
        onClickBack();
        inicializarAutoCompletar();
        eventsText();
        rippleComplete();
    }

    public void initializeButtonFloating(){
        menu_fab.hideMenuButton(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                menu_fab.showMenuButton(true);
            }
        }, 700);
        menu_fab.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    disableItems(false);
                } else {
                    disableItems(true);
                }
            }
        });
    }

    public void iniciarCargaDato(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            type_user = bundle.getString("type_user");
        }
    }
    //endregion

    //region Funciones de eventos, listeners y touch
    public void setOnKeyboardListenerEvents(){
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    menu_fab.hideMenu(false);
                } else {
                    menu_fab.showMenu(true);
                }
            }
        });
    }

    public void onClickNext(){
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        fab_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInstagram();
            }
        });
    }

    public void onClickBack(){
        but_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLogin();
            }
        });
    }

    public void returnLogin(){
        Intent login = new Intent(RegistroUsuario.this, Login.class);
        startActivity(login);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void eventsText(){
        txt_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int id = countryController.getId(txt_country.getText().toString());
                List<String> list = stateController.searchForCountryId(id);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto, android.R.layout.simple_list_item_1, list);
                txt_state.setAdapter(adapter);
            }
        });
        txt_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String country = txt_country.getText().toString();
                    String state = txt_state.getText().toString();
                    if (!country.equalsIgnoreCase("") && !state.equalsIgnoreCase("")) {
                        List<String> list = listCities();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto, android.R.layout.simple_list_item_1, list);
                        txt_city.setAdapter(adapter);
                    }
                }
            }
        });
    }
    //endregion

    //region Load images gallery and init camera
    public void rippleComplete(){
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoYo.with(Techniques.Tada).duration(700).playOn(profile_image);
                initCameraGallery();
            }
        });
    }

    public void initCameraGallery(){
        Intent media = new Intent(RegistroUsuario.this, Media.class);
        media.putExtra("type_photo", "user");
        startActivityForResult(media, 98);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void showAvatar(final String imageUrl){
        SmallBang sBang = SmallBang.attach2Window(this);
        sBang.bang(layout_foto, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                photoSocial = 1;
                Picasso.with(getApplicationContext())
                        .load(imageUrl)
                        .fit()
                        .placeholder(R.drawable.user)
                        .error(R.drawable.ic_launcher)
                        .into(profile_image);
                layout_foto.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd() {}
        });
    }

    public void showAvatarFile(){
        SmallBang sBang = SmallBang.attach2Window(this);
        sBang.bang(layout_foto, new SmallBangListener() {
            @Override
            public void onAnimationStart() {
                photoSocial = 2;
                Picasso.with(getApplicationContext())
                        .load(file)
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
    //endregion

    public void avanzar(){
        if(type_user.equalsIgnoreCase("sell")){
            Intent tienda = new Intent(RegistroUsuario.this, RegistroTienda.class);
            startActivity(tienda);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }else if(type_user.equalsIgnoreCase("buy")){
            Intent home = new Intent(RegistroUsuario.this, Home.class);
            startActivity(home);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    public void disableItems(boolean state){
        profile_image.setEnabled(state);
        txt_nombres.setEnabled(state);
        txt_apellidos.setEnabled(state);
        txt_telefono.setEnabled(state);
        txt_email.setEnabled(state);
        txt_username.setEnabled(state);
        txt_password.setEnabled(state);
        txt_country.setEnabled(state);
        txt_state.setEnabled(state);
        txt_city.setEnabled(state);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(menu_fab.isOpened()){
                    menu_fab.toggle(false);
                }else{
                    returnLogin();
                }
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                social.setFull_name(bundle.getString(InstaLogin.FULLNAME));
                social.setUsername(bundle.getString(InstaLogin.USERNAME));
                social.setAvatar(bundle.getString(InstaLogin.PROFILE_PIC));
                social.setProvider(getString(R.string.instagram));
                social.setUid_provider(bundle.getString(InstaLogin.ID));
                social.setSocial_token(bundle.getString(InstaLogin.ACCESS_TOKEN));
                social_register = true;
                loadDataSocialInstagram();
                dialog.dialogToastInfo("Your social network data loaded correctly");
            }
        } else if(requestCode == rc_sign_in){
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        } else if (requestCode == 98) {
            if(resultCode == Activity.RESULT_OK){
                String path = data.getStringExtra("result");
                if(path != null){
                    file = new File(path);
                    showAvatarFile();
                }else{
                    dialog.dialogToast("Selecting canceled avatar");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                dialog.dialogToast("Error loading photo user");
            }
        }
    }

    public void inicializarAutoCompletar(){
        String name = txt_country.getText().toString();
        List<String> list = countryController.search(name);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(contexto,android.R.layout.simple_list_item_1,list);
        txt_country.setAdapter(adapter);
    }

    public List<String> listCities(){
        List<String> list = new ArrayList<>();
        try{
            String country = txt_country.getText().toString();
            String state = txt_state.getText().toString();
            InputStream is = getAssets().open("data_region/regions.json");
            List<City> listCity = dataOptions.listCities(country,state,is);
            for (int i = 0; i < listCity.size(); i++) {
                cityController.create(listCity.get(i));
                String name = listCity.get(i).getName();
                list.add(name);
            }
        }catch (IOException ex){
            Log.e("RegistroUsuario(listCities)", "Error: " + ex.getMessage());
        }
        return list;
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

    //region Funciones asincronas
    public void registrarUsuario(){
        final boolean isValid = FormValidator.validate(this, new SimpleErrorPopupCallback(this));
        if(isValid){
            if(photoSocial != 0){
                if(validateRegion()){
                    User user = new User();
                    user.setFirst_name(txt_nombres.getText().toString());
                    user.setLast_name(txt_apellidos.getText().toString());
                    user.setUsername(txt_username.getText().toString());
                    user.setEmail(txt_email.getText().toString());
                    user.setPassword(txt_password.getText().toString());
                    user.setPhone(txt_telefono.getText().toString());
                    user.setCity_id(cityController.getId(txt_city.getText().toString()));
                    user.setActive(true);
                    user.setSocial(social_register);
                    if(social_register){
                        user.setImage_profile_url(social.getAvatar());
                    }else{
                        user.setImage_profile_url("");
                    }
                    if(type_user.equalsIgnoreCase("sell")){
                        user.setProfile_id(2);
                    }else if(type_user.equalsIgnoreCase("buy")){
                        user.setProfile_id(3);
                    }
                    register(user);
                }
            } else {
                dialog.dialogError(getString(R.string.error), "Before proceeding, you must add an avatar");
            }
        }
    }

    public void register(final User user){
        dialog.dialogProgress(getString(R.string.registrar_usuario));
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.register(user.getFirst_name(), user.getLast_name(), user.getUsername(), user.getEmail(),
                user.getPhone(), validate.toInt(user.isActive()), validate.toInt(user.getSocial()), user.getImage_profile_url(),
                user.getProfile_id(), user.getCity_id(), user.getPassword(), user.getPassword(),
                social.getFull_name(), social.getProvider(), social.getUid_provider(), social.getSocial_token(),
                new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        boolean success = jsonObject.get("success").getAsBoolean();
                        String message = jsonObject.get("message").getAsString();
                        if (success) {
                            JsonObject jsonUser = jsonObject.get("user").getAsJsonObject();
                            User userRegister = new Gson().fromJson(jsonUser, User.class);
                            userRegister.setPassword(user.getPassword());
                            boolean create = userController.create(userRegister);
                            if (create) {
                                if (social_register) {
                                    social.setUser_id(userRegister.getUser_id());
                                    createPrincipalLinke(social, userRegister.getUser_id());
                                    socialController.create(social);
                                }
                                if(photoSocial == 2){
                                    int id = userRegister.getUser_id();
                                    String username = userRegister.getUsername();
                                    uploadAvatar(id, username);
                                }
                                dialog.cancelarProgress();
                                avanzar();
                            } else {
                                dialog.cancelarProgress();
                                dialog.dialogBasic(message);
                            }
                        } else {
                            dialog.cancelarProgress();
                            dialog.dialogError(getString(R.string.error), message);
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

    public void uploadAvatar(final int id, final String username){
        dialog.dialogProgress(getString(R.string.registrar_usuario));
        final String url = getString(R.string.url_test);
        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.uploadAvatar(id, username, typedFile, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject json, Response response) {
                Log.i("RegistroUsuario(uploadAvatar)", "success: " + json.toString());
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error: " + error.getMessage());
            }
        });
    }

    public void createPrincipalLinke(Social social, int user_id){
        Link linke = new Link();
        linke.setUid_provider(social.getUid_provider());
        linke.setToken(social.getSocial_token());
        linke.setProvider(social.getProvider());
        linke.setUser_id(user_id);
        LinkController linkeController = new LinkController(contexto);
        linkeController.create(linke);
    }
    //endregion

    //region Funciones Instagram
    public void inicializarVariablesSocial(){
        instagram_client_id = getString(R.string.client_id);
        instagram_client_secret = getString(R.string.client_secret);
        instagram_redirect_uri = getString(R.string.redirect_uri);
    }

    public void onClickInstagram(){
        boolean isConnect = validate.connect();
        if(isConnect){
            menu_fab.toggle(false);
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

    public void loadDataSocialInstagram(){
        String full_name = social.getFull_name();
        List<String> name = dataOptions.parseFullName(full_name);
        System.out.println(name.get(1));
        String first_name = name.get(0);
        String last_name = name.get(2);
        txt_nombres.setText(first_name);
        txt_apellidos.setText(last_name);
        txt_username.setText(social.getUsername());
        showAvatar(social.getAvatar());
        fab_instagram.setVisibility(View.GONE);
    }
    //endregion

    //region Funciones de Facebook
    public void onClickFabFacebook(){
        fab_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_register_facebook.performClick();
                onClickSocialFacebook();
            }
        });
    }

    public void initFacebookInstances(){
        but_register_facebook.setReadPermissions(dataOptions.scopesUserFacebook());
        callbackManager = CallbackManager.Factory.create();
    }

    public void onClickSocialFacebook() {
        if (validate.connect()) {
            menu_fab.toggle(false);
            dialog.dialogProgress(getString(R.string.inicio_sesion_1));
            but_register_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JsonObject json = dataOptions.convertToJsonGson(object);
                                    System.out.println("Social: " + json.toString());
                                    String email = json.get("email").getAsString();
                                    social.setSocial_token(loginResult.getAccessToken().getToken());
                                    social.setUid_provider(json.get("id").getAsString());
                                    social.setFull_name(json.get("name").getAsString());
                                    social.setUsername(validate.formatUsername(json.get("email").getAsString()));
                                    social.setAvatar(json
                                            .get("picture").getAsJsonObject()
                                            .get("data").getAsJsonObject()
                                            .get("url").getAsString());
                                    social.setProvider(getString(R.string.facebook));
                                    social_register = true;
                                    loadDataSocialFacebook(email);
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    dialog.cancelarProgress();
                    dialog.dialogWarning("", "User cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    dialog.cancelarProgress();
                    dialog.dialogError("Error", "Error on Login, check your facebook app_id");
                }
            });
            dialog.cancelarProgress();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }

    public void loadDataSocialFacebook(String email){
        String full_name = social.getFull_name();
        List<String> name = dataOptions.parseFullName(full_name);
        String first_name = name.get(0);
        String last_name = name.get(2);
        txt_nombres.setText(first_name);
        txt_apellidos.setText(last_name);
        txt_username.setText(validate.formatUsername(email));
        txt_email.setText(email);
        showAvatar(social.getAvatar());
        fab_facebook.setVisibility(View.GONE);
    }
    //endregion

    //region Funciones de Google
    public void onClickFabGoogle(){
        fab_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_register_google.performClick();
                onClickLoginGoogle();
            }
        });
    }

    public void initGoogleInstance(){
        googleApiClient = new GoogleApiClient.Builder(contexto)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
    }

    public void onClickLoginGoogle(){
        dialog.dialogProgress(getString(R.string.inicio_sesion_1));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mShouldResolve = true;
                    googleApiClient.connect();
                } catch (Exception e) {
                    dialog.cancelarProgress();
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        getTokenGoogle();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        dialog.cancelarProgress();
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Login", "onConnectionFailed:" + connectionResult);
        dialog.cancelarProgress();
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, rc_sign_in);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    googleApiClient.connect();
                    Log.e("Login", "Could not resolve ConnectionResult.", e);
                    dialog.dialogToastWarning("Could not resolve ConnectionResult");
                    mIsResolving = false;
                }
            } else {
                dialog.dialogToastError("Error on Login, check your google + login method");
            }
        }
    }

    private Social getProfileInformation(String token, Person currentPerson, String email) {
        social.setSocial_token(token);
        social.setFull_name(currentPerson.getDisplayName());
        social.setUsername(validate.assignUserInvalid(email, currentPerson.getNickname()));
        String personPhoto = currentPerson.getImage().getUrl();
        social.setAvatar(personPhoto.substring(0, personPhoto.lastIndexOf("=") + 1) + "400");
        social.setUid_provider(currentPerson.getId());
        social.setProvider(getString(R.string.google));
        social_register = true;
        loadDataSocialGoogle(email, currentPerson);
        dialog.cancelarProgress();
        return social;
    }

    public void getTokenGoogle(){
        final Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        if (currentPerson != null) {
            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;
                    boolean permissions = allowPermissions();
                    if(permissions){
                        try {
                            googleEmail = Plus.AccountApi.getAccountName(googleApiClient);
                            String api_scope = "oauth2:https://www.googleapis.com/auth/plus.me";
                            token = GoogleAuthUtil.getToken(contexto, googleEmail, api_scope);
                        } catch (IOException transientEx) {
                            Log.e("Login(getTokenGoogle): IOException", transientEx.toString());
                        } catch (UserRecoverableAuthException e) {
                            startActivityForResult(e.getIntent(), 1001);
                            Log.e("Login(getTokenGoogle): AuthException", e.toString());
                        } catch (GoogleAuthException authEx) {
                            Log.e("Login(getTokenGoogle): GoogleAuthException", authEx.toString());
                        }
                    }
                    return token;
                }

                @Override
                protected void onPostExecute(String token) {
                    String email = googleEmail;
                    getProfileInformation(token, currentPerson, email);
                }
            };
            task.execute();
        }
    }

    public boolean allowPermissions(){
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.GET_ACCOUNTS},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleEmail = Plus.AccountApi.getAccountName(googleApiClient);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void loadDataSocialGoogle(String email, Person currentPerson){
        txt_nombres.setText(currentPerson.getName().getGivenName());
        txt_apellidos.setText(currentPerson.getName().getFamilyName());
        txt_username.setText(validate.formatUsername(email));
        txt_email.setText(email);
        showAvatar(social.getAvatar());
        fab_google.setVisibility(View.GONE);
        menu_fab.toggle(false);
    }
    //endregion

}
