package com.puerto.libre.shopial.ApiService;

import com.google.gson.JsonObject;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.Models.Store;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Creado por Deimer Villa on 12/1/2016.
 */
public interface Api {

//region Gestion de la sesion de usuarios
    @FormUrlEncoded
    @POST("/m/login")
    void login(
            @Field("email")String username,
            @Field("password")String password,
            @Field("remember")Boolean remember,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/m/login/social")
    void loginSocial(
            @Field("provider")String provider,
            @Field("uid_provider")String uid_provider,
            Callback<JsonObject> cb
    );

    @FormUrlEncoded
    @POST("/m/register")
    void register(
            //User
            @Field("first_name")String first_name,
            @Field("last_name")String last_name,
            @Field("username")String username,
            @Field("email")String email,
            @Field("phone")String phone,
            @Field("active")int active,
            @Field("social")int social,
            @Field("avatar")String avatar,
            @Field("profile_id")Integer profile_id,
            @Field("city_id")Integer city_id,
            @Field("password")String password,
            @Field("password_confirmation")String password_confirmation,
            //Social
            @Field("full_name")String full_name,
            @Field("provider")String provider,
            @Field("uid_provider")String uid_provider,
            @Field("social_token")String social_token,
            Callback<JsonObject> cb
    );

    @Multipart
    @POST("/m/upload/user/avatar")
    void uploadAvatar(
            @Part("id") int id,
            @Part("username") String user,
            @Part("avatar") TypedFile file,
            Callback<JsonObject> cb
    );

    @Multipart
    @POST("/m/upload/store/avatar")
    void uploadStoreAvatar(
            @Part("avatar") TypedFile file,
            Callback<JsonObject> cb
    );

    @Multipart
    @POST("/m/upload/store/product")
    void uploadProduct(
            @Part("product") TypedFile file,
            Callback<JsonObject> cb
    );
//endregion

//region Gestion de las tiendas
    @POST("/m/store/create/{categories}")
    void createStore(
            @Body Store store,
            @Path("categories") String categories,
            Callback<JsonObject> cb
    );
//endregion

//region Gestion datos de categorias
    @GET("/m/category/get")
    void getCategories(
            Callback<JsonObject> cb
    );
//endregion

//region Gestion de los links
    @POST("/m/link/create")
    void createLink(
            @Body Link link,
            Callback<JsonObject> cb
    );
//endregion

//region Gestion opciones de estadisticas
    @GET("/m/user/numbers/{user_id}")
    void getNumbers(
            @Path("user_id") int user_id,
            Callback<JsonObject> cb
    );
//endregion

//region Endpoints Instagram
    @GET("/users/{user-id}/media/recent/")
    void getMediaInstagram(
            @Path("user-id") String user_id,
            @Query("access_token") String access_token,
            @Query("count") String count,
            Callback<JsonObject> cb
    );
//endregion

//region Endpoints Facebook
    @GET("/{user-id}/albums")
    void getAlbumsFacebook(
            @Path("user-id") String id,
            @Query("access_token") String access_token,
            Callback<JsonObject> cb
    );

    @GET("/{album-id}/photos")
    void getPhotosFacebook(
            @Path("album-id") String id,
            @Query("fields") String fields,
            @Query("access_token") String access_token,
            Callback<JsonObject> cb
    );

    @GET("/{photo-id}/picture")
    void getPictureFacebook(
            @Path("photo-id") String id,
            @Query("type") String type,
            @Query("access_token") String access_token,
            Callback<Response> cb
    );
//endregion

}
