package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Link;

import java.util.List;

/**
 * Creado por Deimer Villa on 26/2/2016.
 */
public class LinkController {

    private DatabaseHelper helper;
    private Context contexto;

    public void linkeController(){}

    public LinkController(Context contexto){
        this.contexto = contexto;
        linkeController();
    }

    //Funcion que permite la creacion de un link social
    public boolean create(Link linke){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Link, Integer> linkeDao = helper.getSocialLinkRuntimeDao();
            linkeDao.create(linke);
        } catch (Exception ex) {
            res = false;
            Log.e("SocialLinkeController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la creacion de un link social
    public boolean update(Link linke){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Link, Integer> linkeDao = helper.getSocialLinkRuntimeDao();
            linkeDao.createOrUpdate(linke);
        } catch (Exception ex) {
            res = false;
            Log.e("SocialLinkeController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario linkeado
    public Link show(Context context){
        Link linke;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Link, Integer> linkeDao = helper.getSocialLinkRuntimeDao();
            linke = linkeDao.queryForId(1);
        } catch (Exception ex) {
            linke = null;
            Log.e("SocialLinkController(show)", "Error: " + ex.getMessage());
        }
        return linke;
    }

    //Funcion que permite mostrar toda la informacion del usuario linkeado
    public Link show(Context context, String provider){
        Link linke;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Link, Integer> linkeDao = helper.getSocialLinkRuntimeDao();
            linke = linkeDao.queryBuilder().where().eq("provider",provider).query().get(0);
        } catch (Exception ex) {
            linke = null;
            Log.e("SocialLinkController(show)", "Error: " + ex.getMessage());
        }
        return linke;
    }

    //Funcion que permite mostrar toda la informacion del usuario linkeado
    public List<Link> getAll(Context context){
        List<Link> linke;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Link, Integer> linkeDao = helper.getSocialLinkRuntimeDao();
            linke = linkeDao.queryForAll();
        } catch (Exception ex) {
            linke = null;
            Log.e("SocialLinkController(getAll)", "Error: " + ex.getMessage());
        }
        return linke;
    }

}
