package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto City:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase city del usuario con la tabla cities en la base de datos.
 * Cualquier atributo o propiedad del objeto city, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "cities")
public class City {

    @DatabaseField(generatedId = true)
    private int code;

    @DatabaseField(canBeNull = true)
    private Integer id;
    @DatabaseField(canBeNull = true)
    private String name;
    @DatabaseField(defaultValue = "true", canBeNull = true)
    private boolean active;
    @DatabaseField(canBeNull = true)
    private Integer state_id;

    public City() {}

    public City(Integer id, String name, boolean active, Integer state_id) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.state_id = state_id;
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
    public Integer getState_id() {
        return state_id;
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
    public void setState_id(Integer state_id) {
        this.state_id = state_id;
    }

    @Override
    public String toString() {
        return "City{" +
            "code=" + code +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", active=" + active +
            ", state_id=" + state_id +
        '}';
    }
}
