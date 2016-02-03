package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Store;

import java.util.List;

/**
 * Creado por Deimer Villa on 27/1/2016.
 */
public class StoreController {

    private DatabaseHelper helper;
    private Context contexto;

    public void storeController(){}

    public StoreController(Context contexto){
        this.contexto = contexto;
        storeController();
    }

    //Funcion que permite la creacion de un usuario de red social nuevo
    public boolean create(Store store){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Store, Integer> storeDao = helper.getStoreRuntimeDao();
            storeDao.create(store);
        } catch (Exception ex) {
            res = false;
            Log.e("StoreController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(Store store){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Store, Integer> storeDao = helper.getStoreRuntimeDao();
            storeDao.update(store);
        } catch (Exception ex) {
            res = false;
            Log.e("StoreController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public Store show(Context context){
        Store store;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Store, Integer> storeDao = helper.getStoreRuntimeDao();
            store = storeDao.queryForId(1);
        } catch (Exception ex) {
            store = null;
            Log.e("StoreController(show)", "Error: " + ex.getMessage());
        }
        return store;
    }

    //Funcion que permite eliminar un Usuario de la base de datos
    public boolean delete(Store store){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Store, Integer> storeDao = helper.getStoreRuntimeDao();
            storeDao.delete(store);
        } catch (Exception ex) {
            res = false;
            Log.e("StoreController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar todas las tiendas de los usuarios
    public List<Store> list(Context context){
        List<Store> store;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Store, Integer> storeDao = helper.getStoreRuntimeDao();
            store = storeDao.queryForAll();
        } catch (Exception ex) {
            store = null;
            Log.e("StoreController(list)", "Error: " + ex.getMessage());
        }
        return store;
    }

}
