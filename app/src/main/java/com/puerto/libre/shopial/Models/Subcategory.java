package com.puerto.libre.shopial.Models;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 26/2/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Subcategory:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase subcategory del usuario con la tabla subcategories en la base de datos.
 * Cualquier atributo o propiedad del objeto subcategory, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "subcategories")
public class Subcategory {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField(canBeNull = false)
    private Boolean active;
    @DatabaseField(canBeNull = false)
    private int category_id;

    public Subcategory() {}

    public Subcategory(String name, Boolean active, int category_id) {
        this.name = name;
        this.active = active;
        this.category_id = category_id;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Boolean getActive() {
        return active;
    }
    public int getCategory_id() {
        return category_id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", active=" + active +
            ", category_id=" + category_id +
        '}';
    }
}
