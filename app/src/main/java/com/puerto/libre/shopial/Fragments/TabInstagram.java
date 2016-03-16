package com.puerto.libre.shopial.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.JsonObject;
import com.nomad.instagramlogin.InstaLogin;
import com.nomad.instagramlogin.Keys;
import com.puerto.libre.shopial.AdapterViews.RecyclerAdapter;
import com.puerto.libre.shopial.AdapterViews.SpaceItemView;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Controllers.StoreController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.Objects.RequestImage;
import com.puerto.libre.shopial.R;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Creado por Deimer Villa on 3/5/2016.
 */
public class TabInstagram extends Fragment {

    //region Variables de adecuacion
    private Context context;
    private UserController userController;
    private LinkController linkController;
    private StoreController storeController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions dataOptions;
    //endregion

    //region Variables para funciones de instagram
    public static String instagram_client_id;
    public static String instagram_client_secret;
    public static String instagram_redirect_uri;
    //endregion

    //Elementos
    @Bind(R.id.recycler_instagram)RecyclerView recycler;

    public static TabInstagram newInstance() {
        return new TabInstagram();
    }

    public TabInstagram(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instagram, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        return view;
    }

    public void setupContext(){
        context = getActivity().getApplicationContext();
        userController = new UserController(context);
        linkController = new LinkController(context);
        storeController = new StoreController(context);
        dialog = new SweetDialog(getActivity());
        validate = new Validate(context);
        dataOptions = new DataOptions();
        getMediaInstagram();
    }

    public void setupRecycler(JsonObject jsonObject){
        List<RequestImage> requests = dataOptions.parseJsonInstagram(jsonObject);
        RecyclerAdapter adapter = new RecyclerAdapter(context,requests);
        recycler.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        recycler.setAdapter(adapter);
        SpaceItemView space = new SpaceItemView(16);
        recycler.addItemDecoration(space);
    }

    public void getMediaInstagram(){
        dialog.dialogProgress(getString(R.string.get_data));
        final String url = getString(R.string.url_instagram);
        String provider = getString(R.string.instagram);
        Link link = linkController.show(context, provider);
        String user_id = link.getUid_provider();
        String token = link.getToken();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.getMediaInstagram(user_id, token, "-1", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                dialog.cancelarProgress();
                System.out.println(jsonObject);
                setupRecycler(jsonObject);
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancelarProgress();
                try {
                    Log.e("TabInstagram(getMediaInstagram)", "Error: " + error.getBody().toString());
                    String body = error.getBody().toString();
                    JsonObject json = dataOptions.convertStringToGson(body);
                    String message = json.get("meta").getAsJsonObject().get("error_message").getAsString();
                    dialog.dialogError("Access Error", message);
                    String code = json.get("meta").getAsJsonObject().get("code").getAsString();
                    requestError(code);
                } catch (Exception ex) {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("TabInstagram(getMediaInstagram)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    public void requestError(String code_error){
        if(code_error.equalsIgnoreCase("400")){
            reconnectInstagram();
        }
    }

    //region Reconexion instagram
    public void reconnectInstagram(){
        boolean isConnect = validate.connect();
        if (isConnect) {
            InstaLogin instaLogin = new InstaLogin(
                    getActivity(),
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

    //region Resultados de social login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Keys.LOGIN_REQ) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                String provider = getString(R.string.instagram);
                Link link = linkController.show(context, provider);
                String id_provider = bundle.getString(InstaLogin.ID);
                String access_token = bundle.getString(InstaLogin.ACCESS_TOKEN);
                link.setUid_provider(id_provider);
                link.setToken(access_token);
                boolean update = linkController.update(link);
                if(update){
                    getMediaInstagram();
                }
            }
        }
    }
    //endregion

}
