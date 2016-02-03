package com.puerto.libre.shopial.ApiService;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Creado por Deimer Villa on 12/1/2016.
 */
public interface Api {

//region Gestion de la sesion de usuarios
    @FormUrlEncoded
    @POST("/m/login")
    void login(
            @Field("username")String  username,
            @Field("password")String  password,
            @Field("remember")Boolean  remember,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/m/register")
    void register(
            @Field("first_name")String  first_name,
            @Field("last_name")String  last_name,
            @Field("username")String  username,
            @Field("email")String  email,
            @Field("phone")String phone,
            @Field("active")Boolean  active,
            @Field("social")Boolean  social,
            @Field("image_profile_url")String  image_profile_url,
            @Field("profile_id")Integer  profile_id,
            @Field("city_id")Integer city_id,
            @Field("password")String password,
            @Field("password_confirmation")String password_confirmation,
            Callback<JsonObject> cb
    );
//endregion

//region Gestion datos de configuracion
    @GET("/c/load/region")
    void load(
            Callback<JsonObject> cb
    );
    @GET("/c/load/region/countries")
    void loadCountries(
            Callback<JsonObject> cb
    );
    @GET("/c/load/region/states/{country_id}")
    void loadStates(
            @Path("country_id")int country_id,
            Callback<JsonObject> cb
    );
    @GET("/c/load/region/cities/{state_id}")
    void loadCities(
            @Path("state_id")int state_id,
            Callback<JsonObject> cb
    );
//endregion

}
