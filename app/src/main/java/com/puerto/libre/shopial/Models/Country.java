package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Country:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase country del usuario con la tabla countries en la base de datos.
 * Cualquier atributo o propiedad del objeto country, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "countries")
public class Country {

    @DatabaseField(generatedId = true)
    private int code;

    @DatabaseField(canBeNull = true)
    private Integer id;
    @DatabaseField(canBeNull = true)
    private String name;
    @DatabaseField(defaultValue = "true", canBeNull = true)
    private boolean active;

    public Country(){}

    public Country(Integer id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    //Getters de la entidad
    public int getCode() {
        return code;
    }
    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public boolean isActive() {
        return active;
    }

    //Setters de la entidad
    public void setId(Integer id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Country{" +
            "code=" + code +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", active=" + active +
        '}';
    }

}
