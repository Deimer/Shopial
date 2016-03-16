package com.puerto.libre.shopial.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hanks.library.AnimateCheckBox;
import com.puerto.libre.shopial.AdapterViews.AdapterPhotos;
import com.puerto.libre.shopial.ApiService.Api;
import com.puerto.libre.shopial.Controllers.LinkController;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.Objects.RequestImage;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GridPhotos extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private Context contexto;
    private LinkController linkController;
    private SweetDialog dialog;
    private AdapterPhotos adapter;
    private List<RequestImage> images;

    @Bind(R.id.toolbar_grid)Toolbar toolbar;
    @Bind(R.id.grid_photos)GridView grid;
    @Bind(R.id.lbl_toolbar_grid)TextView lbl_toolbar;

    @OnClick(R.id.img_select_photos)
    public void getSelectedImages(){
        List<AnimateCheckBox> checkBoxes = adapter.checkBoxes;
        for (int i = 0; i < checkBoxes.size(); i++) {
            AnimateCheckBox checkBox = checkBoxes.get(i);
            if(checkBox.isChecked()){
                System.out.println(checkBox.getTag());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_photos);
        ButterKnife.bind(this);
        setupContext();
        setupData();
    }

    protected void setupData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String id_album = bundle.getString("id_album");
            String fields = "source";
            String token = linkController.show(contexto, getString(R.string.facebook)).getToken();
            getMediaFacebook(id_album, fields, token);
        }
    }

    public void setupContext(){
        contexto = this;
        linkController = new LinkController(contexto);
        dialog = new SweetDialog(contexto);
        images = new ArrayList<>();
        setupToolbar();
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_toolbar.setTypeface(billabong);
    }

    public void setupGrid(){
        adapter = new AdapterPhotos(contexto, images);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RequestImage item = (RequestImage) parent.getItemAtPosition(position);
        dialogDetailPhoto(item.getUrl_image());
    }

    public void dialogDetailPhoto(String url){
        final AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_photo_detail, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        ViewHolder holder = new ViewHolder(view);
        Picasso.with(contexto).load(url)
                .fit()
                .centerInside()
                .placeholder(R.drawable.background_image)
                .error(R.drawable.background_image)
                .into(holder.img_detail);
        alertDialog.show();
    }

    static class ViewHolder {
        @Bind(R.id.img_detail_photo)ImageView img_detail;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //region Funciones para descargar datos
    public void getMediaFacebook(String id_album, String fields, String token){
        final String url = getString(R.string.url_facebook);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(url)
                .build();
        Api api = restAdapter.create(Api.class);
        api.getPhotosFacebook(id_album, fields, token, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                JsonArray data = jsonObject.get("data").getAsJsonArray();
                for (int i = 0; i < data.size(); i++) {
                    JsonObject json = data.get(i).getAsJsonObject();
                    RequestImage image = new RequestImage();
                    image.setId_media(json.get("id").getAsString());
                    image.setUrl_image(json.get("source").getAsString());
                    images.add(image);
                }
                setupGrid();
            }
            @Override
            public void failure(RetrofitError error) {
                try {
                    dialog.dialogError("Error", getString(R.string.mensaje_error));
                    Log.e("GridPhotos(getMediaFacebook)", "Error: " + error.getBody().toString());
                } catch (Exception ex) {
                    Log.e("GridPhotos(getMediaFacebook)", "Error ret: " + error + "; Error ex: " + ex.getMessage());
                }
            }
        });
    }

    //public void getPhotoMedia(String id_photo, String type, String token){
    //    final String url = getString(R.string.url_facebook);
    //    RestAdapter restAdapter = new RestAdapter.Builder()
    //            .setLogLevel(RestAdapter.LogLevel.FULL)
    //            .setEndpoint(url)
    //            .build();
    //    Api api = restAdapter.create(Api.class);
    //    api.getPictureFacebook(id_photo, type, token, new Callback<Response>() {
    //        @Override
    //        public void success(Response response, Response response2) {
    //            System.out.println(response);
    //        }
//
    //        @Override
    //        public void failure(RetrofitError error) {
//
    //        }
    //    });
    //}

}
