package com.puerto.libre.shopial.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.github.fabtransitionactivity.SheetLayout;
import com.isseiaoki.simplecropview.CropImageView;
import com.puerto.libre.shopial.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CropImage extends Activity
        implements
        SheetLayout.OnFabAnimationEndListener {

    private String path;

    @Bind(R.id.lbl_titulo_media)TextView lbl_titulo;
    @Bind(R.id.fab_crop)FloatingActionButton fab_crop;
    @Bind(R.id.fab_cancel_crop)FloatingActionButton fab_cancel;
    @Bind(R.id.sheet_crop)SheetLayout sheetLayout;
    @Bind(R.id.crop_image)CropImageView crop_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);
        ButterKnife.bind(this);
        initContexto();
    }

    public void initContexto(){
        Typeface billabong = Typeface.createFromAsset(getAssets(), "fonts/billabong.ttf");
        lbl_titulo.setTypeface(billabong);
        initializeElements();
        loadFile();
    }

    public void loadFile(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            path = bundle.getString("path");
            crop_image.setImageBitmap(BitmapFactory.decodeFile(path));
        }
    }

    public void initializeElements(){
        sheetLayout.setFab(fab_crop);
        sheetLayout.setFabAnimationEndListener(this);
        fab_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCropped();
            }
        });
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack();
            }
        });
    }

    public void getImageCropped(){
        try{
            Bitmap bitMap = crop_image.getCroppedBitmap();
            File file = new File(path);
            OutputStream outputStream = new FileOutputStream(file);
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            returnPhotoUser();
        }catch (Exception ex){
            Log.e("CropImage(getImageCropped)", "Error: " + ex.getMessage());
        }
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

    public void returnPhotoCropped(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", path);
        setResult(Activity.RESULT_OK, returnIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void onFabAnimationEnd() {
        returnPhotoCropped();
    }

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
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
    //endregion

}
