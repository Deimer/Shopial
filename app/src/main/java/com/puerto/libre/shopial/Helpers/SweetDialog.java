package com.puerto.libre.shopial.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.github.pierry.simpletoast.SimpleToast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Creado por Deimer Villa on 13/1/2016.
 */
public class SweetDialog {

    //Variable para alerta asincronica
    SweetAlertDialog sweetDialog;
    private Context contexto;

    public void dialogs(){}

    public SweetDialog(Context contexto){
        this.contexto = contexto;
        dialogs();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastOk(String mensaje){
        SimpleToast.info(contexto, mensaje, "{fa-check-square}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastInfo(String mensaje){
        SimpleToast.ok(contexto, mensaje, "{fa-info-circle}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastWarning(String mensaje){
        SimpleToast.ok(contexto, mensaje, "{fa-exclamation-triangle}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastError(String mensaje){
        SimpleToast.error(contexto, mensaje, "{fa-times}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToast(String mensaje){
        Toast.makeText(contexto, mensaje, Toast.LENGTH_LONG).show();
    }

    public void dialogBasic(String mensaje){
        new SweetAlertDialog(contexto)
                .setTitleText(mensaje)
                .show();
    }

    public void dialogSuccess(String titulo, String mensaje){
        new SweetAlertDialog(contexto, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public void dialogWarning(String titulo, String mensaje){
        new SweetAlertDialog(contexto, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    public void dialogError(String titulo, String mensaje){
        new SweetAlertDialog(contexto, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titulo)
                .setContentText(mensaje)
                .show();
    }

    //Funcion que lanza una alerta asincronica para los procesos que requieren tiempo
    public void dialogProgress(String titulo){
        sweetDialog = new SweetAlertDialog(contexto, SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetDialog.setTitleText(titulo);
        sweetDialog.setCancelable(false);
        sweetDialog.show();
    }

    //Funcion que permite cancelar una alerta asincronica
    public void cancelarProgress(){
        sweetDialog.cancel();
    }

}
