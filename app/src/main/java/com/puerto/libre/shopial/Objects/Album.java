package com.puerto.libre.shopial.Objects;

/**
 * Creado por Deimer Villa on 3/11/2016.
 */
public class Album {

    private String id;
    private String name;
    private String created_time;

    public Album(){}

    public Album(String id, String name, String created_time) {
        this.id = id;
        this.name = name;
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCreated_time() {
        return created_time;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }
}
