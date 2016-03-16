package com.puerto.libre.shopial.Fragments;

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
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.puerto.libre.shopial.AdapterViews.AdapterCards;
import com.puerto.libre.shopial.AdapterViews.SpaceItemView;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Controllers.StoreController;
import com.puerto.libre.shopial.Controllers.UserController;
import com.puerto.libre.shopial.Helpers.DataOptions;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Helpers.Validate;
import com.puerto.libre.shopial.Objects.Album;
import com.puerto.libre.shopial.R;
import com.puerto.libre.shopial.views.GridPhotos;

import java.util.ArrayList;
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
public class TabFacebook extends Fragment {

    //region Variables de adecuacion
    private Context context;
    private UserController userController;
    private LinkController linkController;
    private StoreController storeController;
    private SweetDialog dialog;
    private Validate validate;
    private DataOptions dataOptions;
    //endregion

    //Variables para funciones de facebook
    private CallbackManager callbackManager;

    //Elementos
    @Bind(R.id.recycler_facebook)RecyclerView recycler;

    public static TabFacebook newInstance() {
        return new TabFacebook();
    }

    public TabFacebook(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facebook, container, false);
        ButterKnife.bind(this, view);
        setupContext();
        FacebookSdk.sdkInitialize(context);
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
        callbackManager = CallbackManager.Factory.create();
        getAlbumsFacebook();
    }

    public void setupRecycler(List<Album> albums){
        AdapterCards adapter = new AdapterCards(context,albums);
        recycler.setLayoutManager(
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        );
        recycler.setAdapter(adapter);
        SpaceItemView space = new SpaceItemView(8);
        recycler.addItemDecoration(space);
    }

    public void getAlbumsFacebook(){
        String provider = getString(R.string.facebook);
        String user_id = linkController.show(context,provider).getUid_provider();
        String token = linkController.show(context,provider).getToken();
        if(validate.connect()){
            getAlbumsFacebook(user_id, token);
        } else {
            dialog.dialogError(getString(R.string.alerta_conexion_1), getString(R.string.alerta_conexion_2));
        }
    }

    public void getAlbumsFacebook(final String user_id, final String token){
        dialog.dialogProgress(getString(R.string.get_data));
        final String url = getString(R.string.url_facebook);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.getAlbumsFacebook(user_id, token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                List<Album> list = new ArrayList<>();
                JsonArray data = jsonObject.get("data").getAsJsonArray();
                for (int i = 0; i < data.size(); i++) {
                    JsonObject json = data.get(i).getAsJsonObject();
                    Album album = new Album();
                    album.setId(json.get("id").getAsString());
                    album.setName(json.get("name").getAsString());
                    album.setCreated_time(dataOptions.parseDateTimeFacebook(json.get("created_time").getAsString()));
                    list.add(album);
                }
                setupRecycler(list);
                dialog.cancelarProgress();
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

}
