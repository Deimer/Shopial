package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 1/2/2016.
 */
public class StateController {

    private DatabaseHelper helper;
    private Context contexto;

    public void stateController(){}

    public StateController(Context contexto){
        this.contexto = contexto;
        stateController();
    }

    //Funcion que permite la creacion de un pais
    public boolean create(State state){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            stateDao.create(state);
        } catch (Exception ex) {
            res = false;
            Log.e("StateController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(State state){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            stateDao.update(state);
        } catch (Exception ex) {
            res = false;
            Log.e("StateController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public State show(Context context){
        State state;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            state = stateDao.queryForId(1);
        } catch (Exception ex) {
            state = null;
            Log.e("StateController(show)", "Error: " + ex.getMessage());
        }
        return state;
    }

    //Funcion que permite eliminar un pais
    public boolean delete(State state){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            stateDao.delete(state);
        } catch (Exception ex) {
            res = false;
            Log.e("StateController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    public List<String> searchForCountryId(int id){
        List<String> list = new ArrayList<>();
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            List<State> states = stateDao.queryBuilder().where().eq("country_id",id).query();
            for (int i = 0; i < states.size(); i++) {
                String name = states.get(i).getName();
                list.add(name);
            }
        }catch (Exception ex){
            list = null;
            Log.e("StateController(searchForCountryId)", "Error: " + ex.getMessage());
        }
        return list;
    }

    public int getId(String name){
        int id;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<State, Integer> stateDao = helper.getStateRuntimeDao();
            System.out.println(stateDao.queryBuilder().where().eq("name",name).query().get(0).getId());
            id = stateDao.queryBuilder().where().eq("name",name).query().get(0).getId();
        }catch (Exception ex){
            id = 0;
            Log.e("StateController(getId)", "Error: " + ex.getMessage());
        }
        return id;
    }

}
