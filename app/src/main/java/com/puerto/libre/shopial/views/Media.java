package com.puerto.libre.shopial.views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
//import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.fabtransitionactivity.SheetLayout;
import com.puerto.libre.shopial.Helpers.SweetDialog;
import com.puerto.libre.shopial.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import at.markushi.ui.CircleButton;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import butterknife.Bind;
import butterknife.ButterKnife;

public class Media extends AppCompatActivity
        implements
        SheetLayout.OnFabAnimationEndListener {

    private Context contexto;
    private String type_photo;
    private SweetDialog dialog;
    private String path;

    //region Elementos de la pantalla
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.lbl_titulo_media)TextView lbl_titulo;
    @Bind(R.id.layout_gallery)LinearLayout layout_gallery;
    @Bind(R.id.but_gallery)CircleButton but_gallery;
    @Bind(R.id.photo_capture)ImageView photo;
    @Bind(R.id.fab_continue)FloatingActionButton fab;
    @Bind(R.id.fab_ccancel)FloatingActionButton fab_cancel;
    @Bind(R.id.sheet_transition)SheetLayout sheetLayout;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);
        initContexto();
    }

    @Override
    protected void onDestroy() {
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    public void initContexto(){
        contexto = this;
        dialog = new SweetDialog(this);
        setupConfig();
        setupToolbar();
        setupSheet();
        setupCancel();
        configureLoadCamera();
        onClickButtons();
    }

    public void setupConfig(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            type_photo = bundle.getString("type_photo");
        }
    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        if(type_photo.equalsIgnoreCase("store")){
            lbl_titulo.setText(getString(R.string.you_photo_store));
        }
    }

    public void setupSheet(){
        sheetLayout.setFab(fab);
        sheetLayout.setFabAnimationEndListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnPhotoUser();
            }
        });
    }

    public void returnPhotoUser(){
        fab_cancel.hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sheetLayout.expandFab();
            }
        }, 300);
    }

    public void setupCancel(){
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo.setImageResource(0);
                photo.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.FadeIn)
                        .duration(700)
                        .playOn(layout_gallery);
                layout_gallery.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onFabAnimationEnd() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", path);
        setResult(Activity.RESULT_OK, returnIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    public void onClickButtons(){
        but_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {
            if(resultCode == Activity.RESULT_OK){
                path = data.getStringExtra("result");
                File file = new File(path);
                handleCrop(file);
            } if (resultCode == Activity.RESULT_CANCELED) {
                dialog.dialogToast("Crop canceled");
            }
        } else {
            requestPhotoMedia(requestCode, resultCode, data);
        }
    }

    //region Crop file
    public void setupCroped(String path){
        Intent crop = new Intent(Media.this, CropImage.class);
        crop.putExtra("path", path);
        startActivityForResult(crop, 99);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void handleCrop(File data) {
        photo.setImageResource(0);
        Picasso.with(contexto)
                .load(data)
                .fit()
                .centerCrop()
                .into(photo);
        animateElements();
    }

    public void animateElements(){
        layout_gallery.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                YoYo.with(Techniques.FadeIn)
                        .duration(700)
                        .playOn(photo);
                photo.setVisibility(View.VISIBLE);
            }
        }, 900);
    }
    //endregion

    //region Configuracion camara
    public void configureLoadCamera(){
        EasyImage.configuration(contexto)
                .setImagesFolderName("Shopial Images")
                .saveInAppExternalFilesDir()
                .setCopyExistingPicturesToPublicLocation(true);
    }

    public void onClickCamera(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            openDialogGalery();
        } else {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    openDialogGalery();
                }
                @Override
                public void permissionRefused() {
                    dialog.dialogError(getString(R.string.error),"You must accept the permissions to process this function");
                }
            });
        }
    }

    protected void openDialogGalery(){
        EasyImage.openChooserWithGallery(this, "Select you photo user", 0);
    }

    public void requestPhotoMedia(final int requestCode, int resultCode, final Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                dialog.dialogError(getString(R.string.error), "Error loading file");
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //handleCrop(imageFile);
                String path = imageFile.getPath();
                setupCroped(path);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Media.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }
    //endregion

    //region Funciones varias
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                returnBack();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnBack(){
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    //endregion

}
