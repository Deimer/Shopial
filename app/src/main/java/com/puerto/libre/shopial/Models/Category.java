package com.puerto.libre.shopial.Models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Category:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase category del usuario con la tabla categories en la base de datos.
 * Cualquier atributo o propiedad del objeto category, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "categories")
public class Category {

    @SerializedName("code")
    @DatabaseField(generatedId = true)
    private int id;

    @SerializedName("id")
    @DatabaseField(canBeNull = false)
    private Integer code;
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private Boolean active;

    public Category() {}

    public Category(Integer code, String name, Boolean active) {
        this.code = code;
        this.name = name;
        this.active = active;
    }

    //region Getters del modelo category
    public int getId() {
        return id;
    }
    public Integer getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public Boolean getActive() {
        return active;
    }
    //endregion

    //region Setters del modelo category
    public void setCode(Integer code) {
        this.code = code;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    //endregion

    @Override
    public String toString() {
        return "Category{" +
            "id=" + id +
            ", code=" + code +
            ", name='" + name + '\'' +
            ", active=" + active +
        '}';
    }
}
