package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto State:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase state del usuario con la tabla states en la base de datos.
 * Cualquier atributo o propiedad del objeto state, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "states")
public class State {

    @DatabaseField(generatedId = true)
    private int code;

    @DatabaseField(canBeNull = true)
    private Integer id;
    @DatabaseField(canBeNull = true)
    private String name;
    @DatabaseField(defaultValue = "true", canBeNull = true)
    private boolean active;
    @DatabaseField(canBeNull = true)
    private Integer country_id;

    public State() {}

    public State(Integer id, String name, boolean active, Integer country_id) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.country_id = country_id;
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
    public Integer getCountry_id() {
        return country_id;
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
    public void setCountry_id(Integer country_id) {
        this.country_id = country_id;
    }

    @Override
    public String toString() {
        return "State{" +
            "code=" + code +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", active=" + active +
            ", country_id=" + country_id +
        '}';
    }
}
