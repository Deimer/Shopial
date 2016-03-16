package com.puerto.libre.shopial.Models;

import com.google.gson.annotations.SerializedName;
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
    private int code;

    @DatabaseField(canBeNull = true)
    private String first_name;
    @DatabaseField(canBeNull = true)
    private String last_name;
    @DatabaseField(canBeNull = true)
    private String username;
    @DatabaseField(canBeNull = true)
    private String email;
    @DatabaseField(canBeNull = true)
    private String password;
    @DatabaseField(canBeNull = true)
    private String phone;
    @DatabaseField(canBeNull = true)
    private Boolean active;
    @DatabaseField(canBeNull = true)
    private Boolean social;
    @DatabaseField(canBeNull = true)
    private String image_profile_url;
    @DatabaseField(canBeNull = true)
    private int profile_id;
    @DatabaseField(canBeNull = true)
    private Integer city_id;
    @SerializedName("id")
    @DatabaseField(canBeNull = true)
    private Integer user_id;

    public User(){}

    public User(String first_name, String last_name, String username, String email,
                String password, String phone, Boolean active, Boolean social,
                String image_profile_url, int profile_id, Integer city_id, Integer user_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.active = active;
        this.social = social;
        this.image_profile_url = image_profile_url;
        this.profile_id = profile_id;
        this.city_id = city_id;
        this.user_id = user_id;
    }

    //region Getters de la entidad user
    public int getCode() {
        return code;
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
    public int getProfile_id() {
        return profile_id;
    }
    public Integer getCity_id() {
        return city_id;
    }
    public Integer getUser_id() {
        return user_id;
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
    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }
    public void setCity_id(Integer city_id) {
        this.city_id = city_id;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    //endregion

    public String full_name(){
        String f_name = first_name.toLowerCase();
        String l_name = last_name.toLowerCase();
        String f_name_format = Character.toUpperCase(f_name.charAt(0))+f_name.substring(1);
        String l_name_format = Character.toUpperCase(l_name.charAt(0))+l_name.substring(1);
        return f_name_format +" "+ l_name_format;
    }

    @Override
    public String toString() {
        return "User{" +
                "code=" + code +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", active=" + active +
                ", social=" + social +
                ", image_profile_url='" + image_profile_url + '\'' +
                ", profile_id=" + profile_id +
                ", city_id=" + city_id +
                ", user_id=" + user_id +
                '}';
    }
}
