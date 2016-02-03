package com.puerto.libre.shopial.Models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Creado por Deimer Villa on 12/1/2016.
 * ----------------------------------------------------
 * Clase modelo del objeto User:
 * Esta clase usa la libreria ORMLite para manejar la relacion
 * de la clase perfil del usuario con la tabla users en la base de datos.
 * Cualquier atributo o propiedad del objeto user, debe ser agregado
 * aqui para que este se agregue en la base de datos.
 */
@DatabaseTable(tableName = "users")
public class User {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String first_name;
    @DatabaseField(canBeNull = false)
    private String last_name;
    @DatabaseField(canBeNull = false)
    private String username;
    @DatabaseField(canBeNull = false)
    private String email;
    @DatabaseField(canBeNull = false)
    private String password;
    @DatabaseField(canBeNull = false)
    private String phone;
    @DatabaseField(canBeNull = false)
    private Boolean active;
    @DatabaseField(canBeNull = false)
    private Boolean social;
    @DatabaseField(canBeNull = true)
    private String image_profile_url;
    @DatabaseField(canBeNull = false)
    private Integer profile;
    @DatabaseField(canBeNull = false)
    private Integer citiy;

    public User(){}

    public User(String first_name, String last_name, String username, String email, String password,
                String phone, Boolean active, Boolean social, String image_profile_url, Integer profile, Integer citiy) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.active = active;
        this.social = social;
        this.image_profile_url = image_profile_url;
        this.profile = profile;
        this.citiy = citiy;
    }

    //region Getters de la entidad user
    public int getId() {
        return id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phone;
    }
    public Boolean isActive() {
        return active;
    }
    public Boolean getSocial() {
        return social;
    }
    public String getImage_profile_url() {
        return image_profile_url;
    }
    public Integer getProfile() {
        return profile;
    }
    public Integer getCitiy() {
        return citiy;
    }
    //endregion

//region Setters de la entidad user
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setActive(Boolean active) {
        this.active = active;
    }
    public void setSocial(Boolean social) {
        this.social = social;
    }
    public void setImage_profile_url(String image_profile_url) {
        this.image_profile_url = image_profile_url;
    }
    public void setProfile(Integer profile) {
        this.profile = profile;
    }
    public void setCitiy(Integer citiy) {
        this.citiy = citiy;
    }
    //endregion

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", first_name='" + first_name + '\'' +
            ", last_name='" + last_name + '\'' +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            ", active=" + active + '\'' +
            ", social=" + social + '\'' +
            ", image_profile_url='" + image_profile_url + '\'' +
            ", profile=" + profile + '\'' +
            ", citiy=" + citiy + '\'' +
        '}';
    }

}
