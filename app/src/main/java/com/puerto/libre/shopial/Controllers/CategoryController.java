package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 18/2/2016.
 */
public class CategoryController {

    private DatabaseHelper helper;
    private Context contexto;

    public void categoryController(){}

    public CategoryController(Context contexto){
        this.contexto = contexto;
        categoryController();
    }

    //Funcion que permite la creacion de un pais
    public boolean create(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.create(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.update(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public Category show(Context context){
        Category category;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            category = categoryDao.queryForId(1);
        } catch (Exception ex) {
            category = null;
            Log.e("CategoryController(show)", "Error: " + ex.getMessage());
        }
        return category;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public List<Category> list(Context context){
        List<Category> categories = new ArrayList<>();
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categories = categoryDao.queryForAll();
        } catch (Exception ex) {
            Log.e("CategoryController(show)", "Error: " + ex.getMessage());
        }
        return categories;
    }

    //Funcion que permite eliminar un pais
    public boolean delete(Category category){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            categoryDao.delete(category);
        } catch (Exception ex) {
            res = false;
            Log.e("CategoryController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

    public Category searchForId(int id){
        Category category;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            category = categoryDao.queryForId(id);
        }catch (Exception ex){
            category = null;
            Log.e("CategoryController(searchForId)", "Error: " + ex.getMessage());
        }
        return category;
    }

    public Integer searchForName(String name){
        Integer code;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            code = categoryDao.queryBuilder().where().eq("name",name).query().get(0).getCode();
        }catch (Exception ex){
            code = 0;
            Log.e("CategoryController(searchForName)", "Error: " + ex.getMessage());
        }
        return code;
    }

    public int getCode(int id){
        int code = 0;
        try{
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> categoryDao = helper.getCategoryRuntimeDao();
            code = categoryDao.queryForId(id).getCode();
        }catch (Exception ex){
            Log.e("CategoryController(searchForId)", "Error: " + ex.getMessage());
        }
        return code;
    }

    public boolean isLoadData(){
        boolean res = false;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Category, Integer> countryDao = helper.getCategoryRuntimeDao();
            int cantidad = (int)countryDao.countOf();
            if(cantidad > 0){
                res = true;
            }
        } catch (Exception ex) {
            Log.e("CategoryController(session)", "Error: " + ex.getMessage());
        }
        return res;
    }

}
