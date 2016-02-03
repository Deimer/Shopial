package com.puerto.libre.shopial.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.puerto.libre.shopial.Models.City;
import com.puerto.libre.shopial.Models.Country;
import com.puerto.libre.shopial.Models.Social;
import com.puerto.libre.shopial.Models.State;
import com.puerto.libre.shopial.Models.Store;
import com.puerto.libre.shopial.Models.User;
import com.puerto.libre.shopial.R;
import java.sql.SQLException;

/**
 * Creado por Deimer Villa on 12/1/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "shopial.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    private Dao<User, Integer> userDao = null;
    private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;
    private Dao<Social, Integer> socialDao = null;
    private RuntimeExceptionDao<Social, Integer> socialRuntimeDao = null;
    private Dao<Store, Integer> storeDao = null;
    private RuntimeExceptionDao<Store, Integer> storeRuntimeDao = null;
    private Dao<Country, Integer> countryDao = null;
    private RuntimeExceptionDao<Country, Integer> countryRuntimeDao = null;
    private Dao<State, Integer> stateDao = null;
    private RuntimeExceptionDao<State, Integer> stateRuntimeDao = null;
    private Dao<City, Integer> cityDao = null;
    private RuntimeExceptionDao<City, Integer> cityRuntimeDao = null;

    /*Funcion que permite crear la base de datos cuando inicia la aplicacion
    * Usa como parametros;
    * @param sqLiteDatabase -> extension de la base de datos para sqlite
    * @param source -> variable para la conexion a los recursos de sqlite
    */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {
        try {
            TableUtils.createTable(source, User.class);
            TableUtils.createTable(source, Social.class);
            TableUtils.createTable(source, Store.class);
            TableUtils.createTable(source, Country.class);
            TableUtils.createTable(source, State.class);
            TableUtils.createTable(source, City.class);
        } catch (SQLException sqlEx) {
            Log.e("DatabaseHelper(onCreate)", "Error: " + sqlEx.getMessage());
            throw new RuntimeException(sqlEx);
        }
    }

    /*Funcion que permite actualizar la base de datos cuando sea necesario
    * Usa como parametros;
    * @param db -> extension de la base de datos
    * @param source -> variable para la conexion a la base de datos
    * @param oldVersion -> numero de version actual de la base de datos
    * @param newVersion -> numero de la nueva version de la base de datos
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(source, User.class, true);
            TableUtils.dropTable(source, Social.class, true);
            TableUtils.dropTable(source, Store.class, true);
            TableUtils.dropTable(source, Country.class, true);
            TableUtils.dropTable(source, State.class, true);
            TableUtils.dropTable(source, City.class, true);
            onCreate(db, source);
        } catch (SQLException sqlEx) {
            Log.e("DatabaseHelper(onUpgrade)", "Error: " + sqlEx.getMessage());
            Log.e(DatabaseHelper.class.getSimpleName(), "Imposible eliminar la base de datos", sqlEx);
        }
    }

    /*Funcion que permite resetear la base de datos cuando se cierra la sesión del usuario
    * Todos los datos de la aplicación son borrados para evitar información basura.
    * No recibe ningun parametro para funcionar.
    */
    public void onResetDataBase(){
        try {
            //Se eliminan las tablas existentes
            ConnectionSource source = this.getConnectionSource();
            TableUtils.dropTable(source, User.class, true);
            TableUtils.dropTable(source, Social.class, true);
            TableUtils.dropTable(source, Store.class, true);
            TableUtils.dropTable(source, Country.class, true);
            TableUtils.dropTable(source, State.class, true);
            TableUtils.dropTable(source, City.class, true);
            //Recreacion de las tablas
            TableUtils.createTable(source, User.class);
            TableUtils.createTable(source, Social.class);
            TableUtils.createTable(source, Store.class);
            TableUtils.createTable(source, Country.class);
            TableUtils.createTable(source, State.class);
            TableUtils.createTable(source, City.class);
        }catch (SQLException sqlEx){
            Log.i("DatabaseHelper(onResetDataBase)", "Error: " + sqlEx.getMessage());
            throw new RuntimeException(sqlEx);
        }
    }

    public void close(){
        super.close();
        userDao = null;
        userRuntimeDao = null;
        socialDao = null;
        socialRuntimeDao = null;
        storeDao = null;
        storeRuntimeDao = null;
        countryDao = null;
        countryRuntimeDao = null;
        stateDao = null;
        stateRuntimeDao = null;
        cityDao = null;
        cityRuntimeDao = null;
    }

    public Dao<User, Integer> getUserDao() throws SQLException {
        if(userDao == null) userDao = getDao(User.class);
        return userDao;
    }
    public RuntimeExceptionDao<User, Integer> getUsuarioRuntimeDao() {
        if(userRuntimeDao == null) userRuntimeDao = getRuntimeExceptionDao(User.class);
        return userRuntimeDao;
    }

    public Dao<Social, Integer> getSocialDao() throws SQLException {
        if(socialDao == null) socialDao = getDao(Social.class);
        return socialDao;
    }
    public RuntimeExceptionDao<Social, Integer> getSocialRuntimeDao() {
        if(socialRuntimeDao == null) socialRuntimeDao = getRuntimeExceptionDao(Social.class);
        return socialRuntimeDao;
    }

    public Dao<Store, Integer> getStoreDao() throws SQLException {
        if(storeDao == null) storeDao = getDao(Store.class);
        return storeDao;
    }
    public RuntimeExceptionDao<Store, Integer> getStoreRuntimeDao() {
        if(storeRuntimeDao == null) storeRuntimeDao = getRuntimeExceptionDao(Store.class);
        return storeRuntimeDao;
    }

    public Dao<Country, Integer> getCountryDao() throws SQLException {
        if(countryDao == null) countryDao = getDao(Country.class);
        return countryDao;
    }
    public RuntimeExceptionDao<Country, Integer> getCountryRuntimeDao() {
        if(countryRuntimeDao == null) countryRuntimeDao = getRuntimeExceptionDao(Country.class);
        return countryRuntimeDao;
    }

    public Dao<State, Integer> getStateDao() throws SQLException {
        if(stateDao == null) stateDao = getDao(State.class);
        return stateDao;
    }
    public RuntimeExceptionDao<State, Integer> getStateRuntimeDao() {
        if(stateRuntimeDao == null) stateRuntimeDao = getRuntimeExceptionDao(State.class);
        return stateRuntimeDao;
    }

    public Dao<City, Integer> getCityDao() throws SQLException {
        if(cityDao == null) cityDao = getDao(City.class);
        return cityDao;
    }
    public RuntimeExceptionDao<City, Integer> getCityRuntimeDao() {
        if(cityRuntimeDao == null) cityRuntimeDao = getRuntimeExceptionDao(City.class);
        return cityRuntimeDao;
    }

}
