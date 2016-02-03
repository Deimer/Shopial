package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 1/2/2016.
 */
public class CityController {

    private DatabaseHelper helper;
    private Context contexto;

    public void cityController(){}

    public CityController(Context contexto){
        this.contexto = contexto;
        cityController();
    }

    //Funcion que permite la creacion de un pais
    public boolean create(City city){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            cityDao.create(city);
        } catch (Exception ex) {
            res = false;
            Log.e("CityController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(City city){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            cityDao.update(city);
        } catch (Exception ex) {
            res = false;
            Log.e("CityController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public City show(Context context){
        City city;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            city = cityDao.queryForId(1);
        } catch (Exception ex) {
            city = null;
            Log.e("CityController(show)", "Error: " + ex.getMessage());
        }
        return city;
    }

    //Funcion que permite eliminar un pais
    public boolean delete(City city){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            cityDao.delete(city);
        } catch (Exception ex) {
            res = false;
            Log.e("CityController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    public List<String> searchForStateId(int id){
        List<String> list = new ArrayList<>();
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            List<City> citys = cityDao.queryBuilder().where().eq("state_id",id).query();
            for (int i = 0; i < citys.size(); i++) {
                String name = citys.get(i).getName();
                list.add(name);
            }
        }catch (Exception ex){
            list = null;
            Log.e("CityController(searchForStateId)", "Error: " + ex.getMessage());
        }
        return list;
    }

    public int getId(String name){
        int id;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<City, Integer> cityDao = helper.getCityRuntimeDao();
            id = cityDao.queryBuilder().where().eq("name",name).query().get(0).getId();
        }catch (Exception ex){
            id = 0;
            Log.e("CityController(getId)", "Error: " + ex.getMessage());
        }
        return id;
    }

}
