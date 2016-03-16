package com.puerto.libre.shopial.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Models.Link;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.R;
import com.wang.avi.AVLoadingIndicatorView;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.*;
import retrofit.client.Response;

public class LinkNetwork extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private Context contexto;
    private UserController userController;
    private LinkController linkController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions data;

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

    //Variables para funciones de twitter
    private static String twitter_key;
    private static String twitter_secret;

    //Variables para funciones de Pinterest
    private static String app_id_pinterest;
    private PDKClient pdkClient;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.fab_next_link)FloatingActionButton fab_next;
    @Bind(R.id.lbl_link_store_network)TextView lbl_titulo;

    @Bind(R.id.layout_instagram)LinearLayout layout_instagram;
    @Bind(R.id.lbl_instagram)TextView lbl_instagram;
    @Bind(R.id.load_instagram)AVLoadingIndicatorView load_instagram;

    @Bind(R.id.layout_facebook)LinearLayout layout_facebook;
    @Bind(R.id.lbl_facebook)TextView lbl_facebook;
    @Bind(R.id.load_facebook)AVLoadingIndicatorView load_facebook;
    @Bind(R.id.but_link_facebook)LoginButton link_facebook;

    @Bind(R.id.layout_google)LinearLayout layout_google;
    @Bind(R.id.lbl_google)TextView lbl_google;
    @Bind(R.id.load_google)AVLoadingIndicatorView load_google;
    @Bind(R.id.but_link_google)SignInButton link_google;

    @Bind(R.id.layout_twitter)LinearLayout layout_twitter;
    @Bind(R.id.lbl_twitter)TextView lbl_twitter;
    @Bind(R.id.load_twitter)AVLoadingIndicatorView load_twitter;
    @Bind(R.id.but_link_twitter)TwitterLoginButton link_twitter;

    @Bind(R.id.layout_pinterest)LinearLayout layout_pinterest;
    @Bind(R.id.lbl_pinterest)TextView lbl_pinterest;
    @Bind(R.id.load_pinterest)AVLoadingIndicatorView load_pinterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        initTwitterInstance();
        initPinterestInstance();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitter_key, twitter_secret);
        Fabric.with(this, new Twitter(authConfig));
        pdkClient = PDKClient.configureInstance(this, app_id_pinterest);
        pdkClient.onConnect(this);
        setContentView(R.layout.activity_link_network);
        ButterKnife.bind(this);
        initContext();
        initFacebookInstances();
        initGoogleInstance();
    }

    public void initContext(){
        contexto = this;
        userController = new UserController(contexto);
        linkController = new LinkController(contexto);
        dialog = new SweetDialog(contexto);
        data = new DataOptions();
        validate = new Validate(contexto);
        initElements();
        initDataConfiguration();
        onClickNext();
        onClickInstagram();
        onClickFabFacebook();
        onClickFabGoogle();
        onClickTwitter();
        onClickPinterest();
    }

    public void initElements(){
        setSupportActionBar(toolbar);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        Typeface bellopro = Typeface.createFromAsset(getAssets(), "fonts/bellopro.ttf");
        Typeface facebolf = Typeface.createFromAsset(getAssets(), "fonts/facebolf.OTF");
        Typeface gotham = Typeface.createFromAsset(getAssets(), "fonts/gotham.TTF");
        Typeface notosans = Typeface.createFromAsset(getAssets(), "fonts/notosans.ttf");
        lbl_titulo.setTypeface(billabong);
        lbl_instagram.setTypeface(billabong);
        lbl_facebook.setTypeface(facebolf);
        lbl_google.setTypeface(notosans);
        lbl_twitter.setTypeface(gotham);
        lbl_pinterest.setTypeface(bellopro);
    }

    public void initDataConfiguration(){
        if(!linkController.getAll(contexto).isEmpty()){
            String provider_disable = linkController.show(contexto).getProvider();
            switch (provider_disable) {
                case "instagram":
                    layout_instagram.setVisibility(View.GONE);
                    break;
                case "facebook":
                    layout_facebook.setVisibility(View.GONE);
                    break;
                case "google":
                    layout_google.setVisibility(View.GONE);
                    break;
                case "twitter":
                    layout_twitter.setVisibility(View.GONE);
                    break;
                case "pinterest":
                    layout_pinterest.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    public void onClickNext(){
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(getString(R.string.continuar),
                        getString(R.string.remember_links));
            }
        });
    }

    public void confirmDialog(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("Allow")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent home = new Intent(LinkNetwork.this, Home.class);
                        startActivity(home);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                })
                .show();
    }

    public void initLoadData(View view, boolean active){
        if(active){
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    //region Activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        link_twitter.onActivityResult(requestCode, resultCode, data);
        pdkClient.onOauthResponse(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Link linke = new Link();
                linke.setProvider(getString(R.string.instagram));
                linke.setUid_provider(bundle.getString(InstaLogin.ID));
                linke.setToken(bundle.getString(InstaLogin.ACCESS_TOKEN));
                linke.setUser_id(userController.show(contexto).getUser_id());
                loadDataLinke(linke, load_instagram, layout_instagram, "Instagram");
            }
        } else if(requestCode == rc_sign_in){
            if (!googleApiClient.isConnecting()) {
                googleApiClient.connect();
            }
        }
    }

    public void loadDataLinke(final Link link, final View view, final LinearLayout layout, final String provider){
        final String url = getString(R.string.url_test);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.createLink(link, new retrofit.Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                boolean success = jsonObject.get("success").getAsBoolean();
                if (success) {
                    String message = jsonObject.get("message").getAsString();
                    boolean create = linkController.create(link);
                    initLoadData(view, false);
                    if (create) {
                        dialog.dialogToastOk(message + " " + provider);
                        layout.setBackgroundResource(R.drawable.background_disable_view);
                        layout.setEnabled(false);
                    }
                } else {
                    String message = jsonObject.get("message").getAsString();
                    dialog.dialogToastError(message);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("RegistroUsuario(register)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("RegistroUsuario(register)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }
    //endregion

    //region Instagram
    public void initInstagramInstances(){
        instagram_client_id = getString(R.string.client_id);
        instagram_client_secret = getString(R.string.client_secret);
        instagram_redirect_uri = getString(R.string.redirect_uri);
    }

    public void onClickInstagram(){
        initInstagramInstances();
        layout_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkInstagram();
            }
        });
    }

    public void linkInstagram(){
        boolean isConnect = validate.connect();
        if(isConnect){
            initLoadData(load_instagram, true);
            InstaLogin instaLogin = new InstaLogin(
                    LinkNetwork.this,
                    instagram_client_id,
                    instagram_client_secret,
                    instagram_redirect_uri
            );
            instaLogin.login();
        }else{
            dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }
    //endregion

    //region Facebook
    public void onClickFabFacebook(){
        layout_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_facebook.performClick();
                onClickSocialFacebook();
            }
        });
    }

    public void initFacebookInstances(){
        link_facebook.setReadPermissions(data.scopesUserFacebook());
        callbackManager = CallbackManager.Factory.create();
    }

    public void onClickSocialFacebook() {
        if (validate.connect()) {
            initLoadData(load_facebook, true);
            link_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    JsonObject json = data.convertToJsonGson(object);
                                    Link linke = new Link();
                                    linke.setToken(loginResult.getAccessToken().getToken());
                                    linke.setUid_provider(json.get("id").getAsString());
                                    linke.setProvider(getString(R.string.facebook));
                                    linke.setUser_id(userController.show(contexto).getUser_id());
                                    loadDataLinke(linke, load_facebook, layout_facebook,"Facebook");
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    initLoadData(load_facebook, false);
                    dialog.dialogWarning("", "User cancelled");
                }

                @Override
                public void onError(FacebookException error) {
                    initLoadData(load_facebook, false);
                    dialog.dialogError("Error", "Error on Login, check your facebook app_id");
                }
            });
            initLoadData(load_facebook, false);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            dialog.dialogWarning(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }
    //endregion

    //region Google
    public void onClickFabGoogle(){
        layout_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link_google.performClick();
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
        initLoadData(load_google, true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mShouldResolve = true;
                    googleApiClient.connect();
                } catch (Exception e) {
                    initLoadData(load_google, false);
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
        initLoadData(load_google, false);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Login", "onConnectionFailed:" + connectionResult);
        //initLoadData(load_google, false);
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

    private void getProfileInformation(String token, Person currentPerson) {
        Link linke = new Link();
        linke.setToken(token);
        linke.setUid_provider(currentPerson.getId());
        linke.setProvider(getString(R.string.google));
        linke.setUser_id(userController.show(contexto).getUser_id());
        loadDataLinke(linke, load_google, layout_google, "Google");
    }

    public void getTokenGoogle(){
        final Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        if (currentPerson != null) {
            AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    String token = null;
                    if(allowPermissions()){
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
                    getProfileInformation(token,currentPerson);
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

    //region Twitter
    public void onClickTwitter(){
        layout_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate.connect()) {
                    initLoadData(load_twitter, true);
                    loginTwitter();
                    link_twitter.performClick();
                } else {
                    dialog.dialogWarning(getString(R.string.alerta_conexion_1),
                            getString(R.string.alerta_conexion_2));
                }
            }
        });
    }

    public void loginTwitter(){
        link_twitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
                Link linke = new Link();
                linke.setUid_provider(Long.toString(session.getUserId()));
                linke.setToken(session.getAuthToken().token);
                linke.setProvider(getString(R.string.twitter));
                linke.setUser_id(userController.show(contexto).getUser_id());
                loadDataLinke(linke, load_twitter, layout_twitter,"Twitter");
            }
            @Override
            public void failure(TwitterException exception) {
                initLoadData(load_twitter, false);
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    public void initTwitterInstance(){
        twitter_key = getString(R.string.api_key_twitter);
        twitter_secret = getString(R.string.api_secret_twitter);
    }
    //endregion

    //region Pinterest
    public void initPinterestInstance(){
        app_id_pinterest = getString(R.string.pinterest_app_id);
    }

    public void onClickPinterest(){
        layout_pinterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate.connect()) {
                    loginPinterest();
                } else {
                    initLoadData(load_pinterest, true);
                    dialog.dialogWarning(getString(R.string.alerta_conexion_1),
                            getString(R.string.alerta_conexion_2));
                }
            }
        });
    }

    public void loginPinterest(){
        List<String> scopes = new ArrayList<>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);
        pdkClient.login(this, scopes, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Link linke = new Link();
                linke.setUid_provider(response.getUser().getUid());
                linke.setToken(getString(R.string.access_token_pinterest));
                linke.setProvider(getString(R.string.pinterest));
                linke.setUser_id(userController.show(contexto).getUser_id());
                loadDataLinke(linke, load_pinterest, layout_pinterest, "Pinterest");
            }

            @Override
            public void onFailure(PDKException exception) {
                initLoadData(load_pinterest, false);
                System.out.println(exception.getDetailMessage());
            }
        });
    }
    //endregion

}
