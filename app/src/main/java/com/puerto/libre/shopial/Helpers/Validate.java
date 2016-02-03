package com.puerto.libre.shopial.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creado por Deimer Villa on 16/1/2016.
 */
public class Validate {

    //Variable para alerta asincronica
    private Context contexto;

    public void Validaciones(){}

    public Validate(Context contexto){
        this.contexto = contexto;
        Validaciones();
    }

    public void setContext(Context contexto){
        this.contexto = contexto;
    }

    //Metodo para verificar la conectividad a internet
    public boolean connect() {
        boolean res = false;
        if (contexto != null){
            ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            res = netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return res;
    }

    public boolean validateString(ArrayList<String> lista){
        for (int i = 0; i < lista.size(); i++) {
            String cadena = lista.get(i);
            if(cadena.equalsIgnoreCase("")){
                return false;
            }
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

}
