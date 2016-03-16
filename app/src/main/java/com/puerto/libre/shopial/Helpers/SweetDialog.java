package com.puerto.libre.shopial.Helpers;

import android.content.Context;
import android.widget.Toast;
import com.github.pierry.simpletoast.SimpleToast;
import com.puerto.libre.shopial.R;
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
    public void dialogToastOk(String message){
        SimpleToast.info(contexto, message, "{fa-check-square}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastInfo(String message){
        SimpleToast.ok(contexto, message, "{fa-info-circle}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastWarning(String message){
        SimpleToast.ok(contexto, message, "{fa-exclamation-triangle}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToastError(String message){
        SimpleToast.error(contexto, message, "{fa-times}");
    }

    //Funcion que lanza una alerta en un toast con un tiempo definido
    public void dialogToast(String message){
        Toast.makeText(contexto, message, Toast.LENGTH_LONG).show();
    }

    public void dialogBasic(String message){
        new SweetAlertDialog(contexto)
                .setTitleText(message)
                .show();
    }

    public void dialogSuccess(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void dialogWarning(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public void dialogError(String title, String message){
        new SweetAlertDialog(contexto, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    //Funcion que lanza una alerta asincronica para los procesos que requieren tiempo
    public void dialogProgress(String title){
        sweetDialog = new SweetAlertDialog(contexto, SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.getProgressHelper().setBarColor(R.color.colorPrimary);
        sweetDialog.setTitleText(title);
        sweetDialog.setCancelable(false);
        sweetDialog.show();
    }

    //Funcion que permite cancelar una alerta asincronica
    public void cancelarProgress(){
        sweetDialog.cancel();
    }

}
