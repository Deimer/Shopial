package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 26/2/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto Social Link:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase social link del usuario con la tabla linkes en la base de datos.
 * Cualquier atributo o propiedad del objeto social link, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "linkes")
public class Link {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = true)
    private String uid_provider;
    @DatabaseField(canBeNull = true)
    private String token;
    @DatabaseField(canBeNull = true)
    private String provider;
    @DatabaseField(canBeNull = true)
    private int user_id;
    @DatabaseField(defaultValue = "true", canBeNull = true)
    private boolean active;


    public Link(){}

    public Link(String uid_provider, String token, String provider, int user_id, boolean active) {
        this.uid_provider = uid_provider;
        this.token = token;
        this.provider = provider;
        this.user_id = user_id;
        this.active = active;
    }

    //region Getters entity
    public int getId() {
        return id;
    }
    public String getUid_provider() {
        return uid_provider;
    }
    public String getToken() {
        return token;
    }
    public String getProvider() {
        return provider;
    }
    public int getUser_id() {
        return user_id;
    }
    public boolean isActive() {
        return active;
    }
    //endregion

    //region Setters entity
    public void setUid_provider(String uid_provider) {
        this.uid_provider = uid_provider;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    //endregion

    @Override
    public String toString() {
        return "Link{" +
            "id=" + id +
            ", uid_provider='" + uid_provider + '\'' +
            ", token='" + token + '\'' +
            ", provider='" + provider + '\'' +
            ", user_id=" + user_id +
            ", active=" + active +
        '}';
    }
}
