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
    private String store_name;
    @DatabaseField(canBeNull = true)
    private String website;
    @DatabaseField(canBeNull = false)
    private String description_products;
    @DatabaseField(canBeNull = true)
    private String long_business;
    @DatabaseField(canBeNull = true)
    private Boolean is_physical_store;
    @DatabaseField(canBeNull = true)
    private Boolean is_person;
    @DatabaseField(canBeNull = true)
    private String number_employees;
    @DatabaseField(canBeNull = true)
    private Boolean is_manufucture_product;
    @DatabaseField(canBeNull = true)
    private Boolean is_online_store;
    @DatabaseField(canBeNull = true)
    private Boolean is_fan_page;
    @DatabaseField(canBeNull = true)
    private String url_fan_page;
    @DatabaseField(canBeNull = true)
    private int user_id;

    public Store(){}

    public Store(String store_name, String website, String description_products,
                 String long_business, Boolean is_physical_store, Boolean is_person,
                 String number_employees, Boolean is_manufucture_product,
                 Boolean is_online_store, Boolean is_fan_page, String url_fan_page, int user_id) {
        this.store_name = store_name;
        this.website = website;
        this.description_products = description_products;
        this.long_business = long_business;
        this.is_physical_store = is_physical_store;
        this.is_person = is_person;
        this.number_employees = number_employees;
        this.is_manufucture_product = is_manufucture_product;
        this.is_online_store = is_online_store;
        this.is_fan_page = is_fan_page;
        this.url_fan_page = url_fan_page;
        this.user_id = user_id;
    }

    //Getters de la entidad store
    public int getId() {
        return id;
    }
    public String getStore_name() {
        return store_name;
    }
    public String getWebsite() {
        return website;
    }
    public String getDescription_products() {
        return description_products;
    }
    public String getLong_business() {
        return long_business;
    }
    public Boolean getIs_physical_store() {
        return is_physical_store;
    }
    public Boolean getIs_person() {
        return is_person;
    }
    public String getNumber_employees() {
        return number_employees;
    }
    public Boolean getIs_manufucture_product() {
        return is_manufucture_product;
    }
    public Boolean getIs_online_store() {
        return is_online_store;
    }
    public Boolean getIs_fan_page() {
        return is_fan_page;
    }
    public String getUrl_fan_page() {
        return url_fan_page;
    }
    public int getUser_id() {
        return user_id;
    }

    //Setters de la entidad store
    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setDescription_products(String description_products) {
        this.description_products = description_products;
    }
    public void setLong_business(String long_business) {
        this.long_business = long_business;
    }
    public void setIs_physical_store(Boolean is_physical_store) {
        this.is_physical_store = is_physical_store;
    }
    public void setIs_person(Boolean is_person) {
        this.is_person = is_person;
    }
    public void setNumber_employees(String number_employees) {
        this.number_employees = number_employees;
    }
    public void setIs_manufucture_product(Boolean is_manufucture_product) {
        this.is_manufucture_product = is_manufucture_product;
    }
    public void setIs_online_store(Boolean is_online_store) {
        this.is_online_store = is_online_store;
    }
    public void setIs_fan_page(Boolean is_fan_page) {
        this.is_fan_page = is_fan_page;
    }
    public void setUrl_fan_page(String url_fan_page) {
        this.url_fan_page = url_fan_page;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Store{" +
            "id=" + id +
            ", store_name='" + store_name + '\'' +
            ", website='" + website + '\'' +
            ", description_products='" + description_products + '\'' +
            ", long_business='" + long_business + '\'' +
            ", is_physical_store=" + is_physical_store +
            ", is_person=" + is_person +
            ", number_employees='" + number_employees + '\'' +
            ", is_manufucture_product=" + is_manufucture_product +
            ", is_online_store=" + is_online_store +
            ", is_fan_page=" + is_fan_page +
            ", url_fan_page='" + url_fan_page + '\'' +
            ", user_id=" + user_id +
        '}';
    }
}
