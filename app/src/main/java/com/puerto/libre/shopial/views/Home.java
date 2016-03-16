package com.puerto.libre.shopial.views;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.puerto.libre.shopial.AdapterViews.TabPagerAdapter;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Controllers.SocialController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import java.util.List;

public class Home extends AppCompatActivity {

    private Context contexto;
    private SocialController socialController;
    private LinkController linkController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions dataOptions;

    //Variables de google plus
    private GoogleApiClient googleApiClient;

    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.view_pager)ViewPager view_pager;
    @Bind(R.id.tab_layout)TabLayout tab_layout;

    //@Bind(R.id.fab)FloatingActionButton fab;
    @Bind(R.id.lbl_toolbar_app)TextView lbl_toolbar;
    //@Bind(R.id.but_logout)Button but_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstanceSocial();
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        inicializarContexto();
    }

    public void initInstanceSocial(){
        String twitter_key = getString(R.string.api_key_twitter);
        String twitter_secret = getString(R.string.api_secret_twitter);
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitter_key, twitter_secret);
        Fabric.with(this, new Twitter(authConfig));
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();
    }

    public void inicializarContexto(){
        contexto = this;
        socialController = new SocialController(contexto);
        linkController = new LinkController(contexto);
        dialog = new SweetDialog(contexto);
        validate = new Validate(contexto);
        dataOptions = new DataOptions();
        setupToolbar();
        setupViewPager();
        setupTabLayout();
        onClickLogout();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_toolbar.setTypeface(billabong);
    }

    public void setupViewPager(){
        List<Link> list = linkController.getAll(contexto);
        List<String> tab_titles = dataOptions.titleTabs(list);
        int page_count = tab_titles.size();
        System.out.println(tab_titles);
        view_pager.setAdapter(new TabPagerAdapter(
                getSupportFragmentManager(),
                page_count,
                tab_titles
        ));
    }

    public void setupTabLayout(){
        int num_tabs = linkController.getAll(contexto).size();
        if(num_tabs <= 2){
            tab_layout.setTabMode(TabLayout.MODE_FIXED);
        } else if(num_tabs > 2){
            tab_layout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        tab_layout.setupWithViewPager(view_pager);
    }

    //region Social utils
    public void onClickLogout(){
        if(validate.connect()){
            /*but_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutSocialApi();
                    logoutLinks();
                    int id = userController.show(contexto).getCode();
                    boolean logout = userController.logout(id);
                    if(logout){
                        Intent tpUsuario = new Intent(Home.this, TipoUsuario.class);
                        startActivity(tpUsuario);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                    }
                }
            });*/
        }else{
            dialog.dialogWarning(getString(R.string.alerta_conexion_1),
                    getString(R.string.alerta_conexion_2));
        }
    }

    public void logoutLinks(){
        List<Link> list = linkController.getAll(contexto);
        for (int i = 0; i < list.size(); i++) {
            String provider = list.get(i).getProvider();
            logoutSocial(provider);
        }
    }

    public void logoutSocialApi(){
        if(socialController.session()){
            String provider = socialController.show(contexto).getProvider();
            logoutSocial(provider);
        }
    }

    public void logoutSocial(String provider){
        switch (provider){
            case "facebook":
                LoginManager.getInstance().logOut();
                System.out.println("facebook cerrado");
                break;
            case "google":
                logoutGoogle();
                System.out.println("google cerrado");
                break;
            case "twitter":
                Twitter.getSessionManager().clearActiveSession();
                Twitter.logOut();
                System.out.println("twitter cerrado");
                break;
        }
    }

    public boolean logoutGoogle(){
        if(googleApiClient.isConnected()){
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
            return true;
        } else {
            return false;
        }
    }
    //endregion

}