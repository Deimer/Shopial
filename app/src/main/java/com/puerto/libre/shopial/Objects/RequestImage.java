package com.puerto.libre.shopial.Objects;

/**
 * Creado por Deimer Villa on 3/8/2016.
 */
public class RequestImage {

    private String url_image;
    private String description;
    private String id_media;

    public RequestImage(){}

    public RequestImage(String url_image, String description, String id_media) {
        this.url_image = url_image;
        this.description = description;
        this.id_media = id_media;
    }

    //region Getters
    public String getUrl_image() {
        return url_image;
    }
    public String getDescription() {
        return description;
    }
    public String getId_media() {
        return id_media;
    }
    public int getId() {
        return url_image.hashCode();
    }
    //endregion

    //region Setters
    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setId_media(String id_media) {
        this.id_media = id_media;
    }
    //endregion

}
