package com.puerto.libre.shopial.Controllers;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.puerto.libre.shopial.Database.DatabaseHelper;
import com.puerto.libre.shopial.Models.Subcategory;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 26/2/2016.
 */
public class SubcategoryController {

    private DatabaseHelper helper;
    private Context contexto;

    public void subCategoryController(){}

    public SubcategoryController(Context contexto){
        this.contexto = contexto;
        subCategoryController();
    }

    //Funcion que permite la creacion de un pais
    public boolean create(Subcategory subcategory){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto, DatabaseHelper.class);
            RuntimeExceptionDao<Subcategory, Integer> categoryDao = helper.getSubcategoryRuntimeDao();
            categoryDao.create(subcategory);
        } catch (Exception ex) {
            res = false;
            Log.e("SubcategoryController(create)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite la edicion de un usuario
    public boolean update(Subcategory subcategory){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Subcategory, Integer> categoryDao = helper.getSubcategoryRuntimeDao();
            categoryDao.update(subcategory);
        } catch (Exception ex) {
            res = false;
            Log.e("SubcategoryController(update)", "Error: " + ex.getMessage());
        }
        return res;
    }

    //Funcion que permite mostrar toda la informacion del usuario logueado
    public Subcategory show(Context context){
        Subcategory subcategory;
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Subcategory, Integer> subcategoryDao = helper.getSubcategoryRuntimeDao();
            subcategory = subcategoryDao.queryForId(1);
        } catch (Exception ex) {
            subcategory = null;
            Log.e("SubcategoryController(show)", "Error: " + ex.getMessage());
        }
        return subcategory;
    }

    //Funcion que permite mostrar todas las categorias del susteme
    public List<Subcategory> list(Context context){
        List<Subcategory> subcategories = new ArrayList<>();
        try {
            helper = OpenHelperManager.getHelper(context,DatabaseHelper.class);
            RuntimeExceptionDao<Subcategory, Integer> subcategoryDao = helper.getSubcategoryRuntimeDao();
            subcategories = subcategoryDao.queryForAll();
        } catch (Exception ex) {
            Log.e("SubcategoryController(list)", "Error: " + ex.getMessage());
        }
        return subcategories;
    }

    //Funcion que permite eliminar una subcategoria
    public boolean delete(Subcategory subcategory){
        boolean res = true;
        try {
            helper = OpenHelperManager.getHelper(contexto,DatabaseHelper.class);
            RuntimeExceptionDao<Subcategory, Integer> subcategoryDao = helper.getSubcategoryRuntimeDao();
            subcategoryDao.delete(subcategory);
        } catch (Exception ex) {
            res = false;
            Log.e("SubcategoryController(delete)", "Error: " + ex.getMessage());
        }
        return res;
    }

}
