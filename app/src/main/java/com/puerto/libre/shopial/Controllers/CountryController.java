package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 31/1/2016.
 */
public class CountryController {

    private DatabaseHelper helper;
    private Context contexto;

    public void countryController(){}

    public CountryController(Context contexto){
        this.contexto = contexto;
        countryController();
    }

    //Funcion que permite la creacion de un pais
    public boolean create(Country country){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            countryDao.create(country);
        } catch (Exception ex) {
            res = false;
            Log.e("CountryController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(Country country){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            countryDao.update(country);
        } catch (Exception ex) {
            res = false;
            Log.e("CountryController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public Country show(Context context){
        Country country;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            country = countryDao.queryForId(1);
        } catch (Exception ex) {
            country = null;
            Log.e("CountryController(show)", "Error: " + ex.getMessage());
        }
        return country;
    }

    //Funcion que permite eliminar un pais
    public boolean delete(Country country){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            countryDao.delete(country);
        } catch (Exception ex) {
            res = false;
            Log.e("CountryController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    public List<String> search(String name){
        List<String> list = new ArrayList<>();
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            QueryBuilder<Country,Integer> builder = countryDao.queryBuilder();
            builder.where().like("name", "%"+ name +"%");
            PreparedQuery<Country> prepared = builder.prepare();
            List<Country> countries = countryDao.query(prepared);
            for (int i = 0; i < countries.size(); i++) {
                String name_country = countries.get(i).getName();
                list.add(name_country);
            }
        }catch (Exception ex){
            list = null;
            Log.e("CountryController(search)", "Error: " + ex.getMessage());
        }
        return list;
    }

    //Funcion que permite mostrar todas las tiendas de los usuarios
    public List<Country> list(){
        List<Country> countries;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            countries = countryDao.queryForAll();
        } catch (Exception ex) {
            countries = null;
            Log.e("CountryController(list)", "Error: " + ex.getMessage());
        }
        return countries;
    }

    public int getId(String name){
        int id;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            id = countryDao.queryBuilder().where().eq("name",name).query().get(0).getId();
        }catch (Exception ex){
            id = 0;
            Log.e("CountryController(getId)", "Error: " + ex.getMessage());
        }
        return id;
    }

    public boolean isLoadData(){
        boolean res = false;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Country, Integer> countryDao = helper.getCountryRuntimeDao();
            int cantidad = (int)countryDao.countOf();
            if(cantidad > 0){
                res = true;
            }
        } catch (Exception ex) {
            Log.e("CountryController(session)", "Error: " + ex.getMessage());
        }
        return res;
    }

}
