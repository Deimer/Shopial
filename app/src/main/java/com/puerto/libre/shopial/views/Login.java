package com.puerto.libre.shopial.views;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import org.json.JSONObject;
import java.io.IOException;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Login extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //Variables de implementacion
    private Context contexto;
    private UserController userController;
    private SocialController socialController;
    private LinkController linkController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions dataOptions;
    private String type_user;

    //Variables para funciones de instagram
    public static String instagram_client_id;
    public static String instagram_client_secret;
    public static String instagram_redirect_uri;

    //Variables para funciones de facebook
    private CallbackManager callbackManager;

    //Variables para funciones de Google+
    private GoogleApiClient googleApiClient;
    private boolean mShouldResolve = false;
    private boolean mIsResolving = false;
    private static final int rc_sign_in = 0;
    static final int REQUEST_CODE_ASK_PERMISSIONS = 0;
    private String googleEmail;

    @Bind(R.id.lbl_titulo_app)TextView lbl_titulo;
    @Bind(R.id.txt_email)EditText txt_email;
    @Bind(R.id.txt_password)EditText txt_password;
    @Bind(R.id.but_login)Button but_login;
    @Bind(R.id.but_registrar)Button but_registrar;
    @Bind(R.id.lbl_login_social)TextView lbl_login_social;

    Button but_login_instagram;
    LoginButton but_login_facebook;
    SignInButton but_login_google;
    Button but_cancel_dialog_social;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        inicializarContexto();
        iniciarCargaDatos();
        onClickLogin();
        onClickRegistro();
        onClickSocialLogin();
    }

    public void inicializarContexto(){
        contexto = this;
        userController = new UserController(contexto);
        socialController = new SocialController(contexto);
        linkController = new LinkController(contexto);
        dialog = new SweetDialog(contexto);
        validate = new Validate(contexto);
        dataOptions = new DataOptions();
        instagram_client_id = getString(R.string.client_id);
        instagram_client_secret = getString(R.string.client_secret);
        instagram_redirect_uri = getString(R.string.redirect_uri);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
    }

    public Social createUserSocial(String full_name, String username,
                                   String avatar, String id_provider,
                                   String token, String provider){
        Social social = new Social();
        social.setFull_name(full_name);
        social.setUsername(username);
        social.setAvatar(avatar);
        social.setUid_provider(id_provider);
        social.setSocial_token(token);
        social.setProvider(provider);
        return social;
    }

    public void iniciarCargaDatos(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            type_user = bundle.getString("type_user");
        }
    }

    //region Login nativo de shopial
    public void onClickLogin(){
        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                if (email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                    dialog.dialogToastError(getString(R.string.alerta_campos_vacios));
                } else {
                    if (validate.isEmailValid(email)) {
                        if (validate.isPasswordValid(password)) {
                            boolean hayConexion = validate.connect();
                            if (hayConexion) {
                                login(email, password);
                            } else {
                                dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
                            }
                        } else {
                            dialog.dialogError("Error", "The password must be more than 6 characters");
                        }
                    } else {
                        dialog.dialogError("Error", "The email entered is invalid or misspelled");
                    }
                }
            }
        });
    }

    public void login(String email, String password) {
        dialog.dialogProgress(getString(R.string.inicio_sesion_1));
        final String url = getString(R.string.url_test);
        Boolean remember = true;
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.login(email, password, remember, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    User user = new Gson().fromJson(jsonObject.get("data"), User.class);
                    JsonArray data = jsonObject.get("data").getAsJsonObject().get("links").getAsJsonArray();
                    loadLinks(data);
                    userController.create(user);
                    dialog.cancelarProgress();
                    avanzar();
                } else {
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogBasic(message);
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

    public void loadLinks(JsonArray data){
        for (int i = 0; i < data.size(); i++) {
            JsonObject json = data.get(i).getAsJsonObject();
            Link link = new Gson().fromJson(json, Link.class);
            linkController.create(link);
        }
    }

    public void avanzar(){
        Intent menu = new Intent(Login.this, Home.class);
        startActivity(menu);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    //endregion

    //region Registro
    public void onClickRegistro(){
        but_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registro = new Intent(Login.this, RegistroUsuario.class);
                registro.putExtra("type_user", type_user);
                startActivity(registro);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });
    }
    //endregion

    //region Resultados de social login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String full_name = bundle.getString(InstaLogin.FULLNAME);
                String username = bundle.getString(InstaLogin.USERNAME);
                String avatar = bundle.getString(InstaLogin.PROFILE_PIC);
                String provider = getString(R.string.instagram);
                String id_provider = bundle.getString(InstaLogin.ID);
                String access_token = bundle.getString(InstaLogin.ACCESS_TOKEN);
                Social social = createUserSocial(full_name,username,avatar,id_provider,access_token,provider);
                loginSocialApis(social);
            }
        } else if(requestCode == rc_sign_in){
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }
    //endregion

    //region login con Instagram
    public void onClickLoginInstagram(){
        boolean isConnect = validate.connect();
        if (isConnect) {
            InstaLogin instaLogin = new InstaLogin(
                    Login.this,
                    instagram_client_id,
                    instagram_client_secret,
                    instagram_redirect_uri
            );
            instaLogin.login();
        } else {
            dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }
    //endregion

    //region Login con Facebook
    public void initFacebookInstances(){
        but_login_facebook.setReadPermissions(dataOptions.scopesUserFacebook());
        callbackManager = CallbackManager.Factory.create();
    }

    public void onClickSocialFacebook() {
        if (validate.connect()) {
            dialog.dialogProgress(getString(R.string.inicio_sesion_1));
            but_login_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JsonObject json = dataOptions.convertToJsonGson(object);
                                    String token = loginResult.getAccessToken().getToken();
                                    String uid_provider = json.get("id").getAsString();
                                    String full_name = json.get("name").getAsString();
                                    String username = validate.formatUsername(json.get("email").getAsString());
                                    String avatar = json
                                            .get("picture").getAsJsonObject()
                                            .get("data").getAsJsonObject()
                                            .get("url").getAsString();
                                    String provider = getString(R.string.facebook);
                                    Social social = createUserSocial(full_name, username, avatar, uid_provider, token, provider);
                                    System.out.println("Social: " + social.toString());
                                    loginSocialApis(social);
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
    //endregion

    //region Login con Google+
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
        String full_name = currentPerson.getDisplayName();
        String username = validate.assignUserInvalid(email, currentPerson.getNickname());
        String personPhoto = currentPerson.getImage().getUrl();
        String avatar = personPhoto.substring(0,personPhoto.lastIndexOf("=")+1)+"400";
        String uid_provider= currentPerson.getId();
        String provider = getString(R.string.google);
        Social social = createUserSocial(full_name, username, avatar, uid_provider, token, provider);
        System.out.println("Social: " + social.toString());
        loginSocialApis(social);
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
                    getProfileInformation(token,currentPerson, googleEmail);
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
    //endregion

    //region Login api's social
    public void loginSocialApis(final Social social){
        final String url = getString(R.string.url_test);
        String provider = social.getProvider();
        String uid_provider = social.getUid_provider();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.loginSocial(provider, uid_provider, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                int success = jsonObject.get("success").getAsInt();
                if (success == 1) {
                    User user = new Gson().fromJson(jsonObject.get("data").getAsJsonObject(), User.class);
                    boolean create = userController.create(user);
                    if (create) {
                        social.setUser_id(user.getUser_id());
                        JsonArray data = jsonObject.get("data").getAsJsonObject().get("links").getAsJsonArray();
                        loadLinks(data);
                        socialController.create(social);
                        dialog.cancelarProgress();
                        avanzar();
                    }
                } else if (success == 2) {
                    logoutSocial(social.getProvider());
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogWarning("Error", message);
                } else if (success == 3) {
                    logoutSocial(social.getProvider());
                    String message = jsonObject.get("message").getAsString();
                    dialog.cancelarProgress();
                    dialog.dialogWarning("Error", message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("Login(loginSocialApis)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("Login(loginSocialApis)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void logoutSocial(String provider){
        if(provider.equalsIgnoreCase(getString(R.string.google))){
            if(googleApiClient.isConnected()){
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                googleApiClient.disconnect();
            }
        } else if(provider.equalsIgnoreCase(getString(R.string.facebook))) {
            LoginManager.getInstance().logOut();
        }
    }
    //endregion

    //region Dialog redes sociales
    public void onClickSocialLogin(){
        lbl_login_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSocialAcounts();
            }
        });
    }

    public void dialogSocialAcounts(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_social_login, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        but_login_instagram = (Button)view.findViewById(R.id.but_login_instagram);
        but_login_facebook = (LoginButton)view.findViewById(R.id.but_login_facebook);
        but_login_google = (SignInButton)view.findViewById(R.id.but_login_google);
        but_cancel_dialog_social = (Button)view.findViewById(R.id.but_cancel_dialog_social);
        but_login_google.setSize(SignInButton.SIZE_WIDE);
        initFacebookInstances();
        initGoogleInstance();
        but_login_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLoginInstagram();
                alertDialog.dismiss();
            }
        });
        but_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSocialFacebook();
                alertDialog.dismiss();
            }
        });
        but_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLoginGoogle();
                alertDialog.dismiss();
            }
        });
        but_cancel_dialog_social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    //endregion

    //region Funciones varias
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                returnLogin();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnLogin(){
        Intent tipo_usuario = new Intent(Login.this, TipoUsuario.class);
        startActivity(tipo_usuario);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    //endregion

}
