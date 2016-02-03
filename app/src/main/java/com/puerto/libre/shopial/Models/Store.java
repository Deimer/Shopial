package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Store:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase store con la tabla stores en la base de datos.
 * Cualquier atributo o propiedad del objeto store, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "stores")
public class Store {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String long_bussines;
    @DatabaseField(canBeNull = true)
    private Boolean is_physical_store;
    @DatabaseField(canBeNull = true)
    private Boolean is_person;
    @DatabaseField(canBeNull = true)
    private Integer number_employees;
    @DatabaseField(canBeNull = true)
    private Boolean is_manufucture_product;
    @DatabaseField(canBeNull = true)
    private Boolean is_online_store;
    @DatabaseField(canBeNull = true)
    private String website;
    @DatabaseField(canBeNull = true)
    private Integer category_id;

    public Store(){}

    public Store(String long_bussines, Boolean is_physical_store,
                 Boolean is_person, Integer number_employees,
                 Boolean is_manufucture_product, Boolean is_online_store,
                 String website, Integer category_id) {
        this.long_bussines = long_bussines;
        this.is_physical_store = is_physical_store;
        this.is_person = is_person;
        this.number_employees = number_employees;
        this.is_manufucture_product = is_manufucture_product;
        this.is_online_store = is_online_store;
        this.website = website;
        this.category_id = category_id;
    }

//Getters de la entidad store
    public int getId() {
        return id;
    }
    public String getLong_bussines() {
        return long_bussines;
    }
    public Boolean getIs_physical_store() {
        return is_physical_store;
    }
    public Boolean getIs_person() {
        return is_person;
    }
    public Integer getNumber_employees() {
        return number_employees;
    }
    public Boolean getIs_manufucture_product() {
        return is_manufucture_product;
    }
    public Boolean getIs_online_store() {
        return is_online_store;
    }
    public String getWebsite() {
        return website;
    }
    public Integer getCategory_id() {
        return category_id;
    }

//Setters de la entidad store
    public void setLong_bussines(String long_bussines) {
        this.long_bussines = long_bussines;
    }
    public void setIs_physical_store(Boolean is_physical_store) {
        this.is_physical_store = is_physical_store;
    }
    public void setIs_person(Boolean is_person) {
        this.is_person = is_person;
    }
    public void setNumber_employees(Integer number_employees) {
        this.number_employees = number_employees;
    }
    public void setIs_manufucture_product(Boolean is_manufucture_product) {
        this.is_manufucture_product = is_manufucture_product;
    }
    public void setIs_online_store(Boolean is_online_store) {
        this.is_online_store = is_online_store;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }
}
