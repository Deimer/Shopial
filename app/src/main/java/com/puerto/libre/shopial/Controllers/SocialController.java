package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Social;

/**
 * Creado por Deimer Villa on 25/1/2016.
 */
public class SocialController {

    private DatabaseHelper helper;
    private Context contexto;

    public void socialController(){}

    public SocialController(Context contexto){
        this.contexto = contexto;
        socialController();
    }

    //Funcion que permite la creacion de un usuario de red social nuevo
    public boolean create(Social social){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            socialDao.create(social);
        } catch (Exception ex) {
            res = false;
            Log.e("SocialController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(Social social){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            socialDao.update(social);
        } catch (Exception ex) {
            res = false;
            Log.e("SocialController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public Social show(Context context){
        Social social;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            social = socialDao.queryForId(1);
        } catch (Exception ex) {
            social = null;
            Log.e("SocialController(show)", "Error: " + ex.getMessage());
        }
        return social;
    }

    //Funcion que permite eliminar un Usuario de la base de datos
    public boolean delete(Social social){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            socialDao.delete(social);
        } catch (Exception ex) {
            res = false;
            Log.e("SocialController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite saber si hay una sesion iniciada
    public boolean session(){
        boolean res = false;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            int cantidad = (int)socialDao.countOf();
            if(cantidad > 0){
                res = true;
            }
        } catch (Exception ex) {
            Log.e("SocialController(session)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite reiniciar la base de datos al cerrar una sesion del usuario
    public boolean logout(int id){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Social, Integer> socialDao = helper.getSocialRuntimeDao();
            socialDao.deleteById(id);
            helper.onResetDataBase();
        }catch (Exception ex){
            res = false;
            Log.e("SocialController(logout)", "Error: " + ex.toString());
        }
        return res;
    }

}
