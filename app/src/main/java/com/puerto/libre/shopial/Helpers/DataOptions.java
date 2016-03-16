package com.puerto.libre.shopial.Helpers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.puerto.libre.shopial.Models.Category;
import com.puerto.libre.shopial.Models.City;
import com.puerto.libre.shopial.Models.Link;
import com.puerto.libre.shopial.Objects.RequestImage;
import com.puerto.libre.shopial.Models.Store;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 28/1/2016.
 */
public class DataOptions {

    public List<String> optionsCategories(List<Category> list){
        List<String> listaItems = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listaItems.add(list.get(i).getName());
        }
        return listaItems;
    }

    public List<String> longBusiness(){
        List<String> list = new ArrayList<>();
        list.add("Select an option");
        list.add("0 - 1 Year");
        list.add("1 - 2 Years");
        list.add("2 - 5 Years");
        list.add("5+ Years");
        return list;
    }

    public List<String> oneOption(){
        List<String> list = new ArrayList<>();
        list.add("Select an option");
        list.add("Not");
        list.add("Yes");
        return list;
    }

    public List<String> personCompany(){
        List<String> list = new ArrayList<>();
        list.add("Select an option");
        list.add("Company");
        list.add("Person");
        return list;
    }

    public List<String> manyEmployees(){
        List<String> list = new ArrayList<>();
        list.add("Select an option");
        list.add("1-5");
        list.add("6-10");
        list.add("10-20");
        list.add("20-50");
        list.add("50-100");
        list.add("100+");
        return list;
    }

    public ArrayList<String> scopesUserFacebook(){
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("public_profile");
        scopes.add("email");
        scopes.add("user_posts");
        scopes.add("user_photos");
        scopes.add("user_likes");
        scopes.add("user_friends");
        //scopes.add("publish_actions");
        scopes.add("user_location");
        return scopes;
    }

    public JsonObject convertToJsonGson(JSONObject object){
        JsonParser jsonParser = new JsonParser();
        return (JsonObject)jsonParser.parse(object.toString());
    }

    public Store convertArrayToStore(ArrayList<String> array){
        Store store = new Store();
        store.setStore_name(array.get(0));
        store.setWebsite(array.get(1));
        store.setDescription_products(array.get(2));
        store.setLong_business(array.get(3));
        store.setIs_physical_store(convertBoolean(array.get(4)));
        store.setIs_person(convertBooleanPerson(array.get(5)));
        store.setNumber_employees(array.get(6));
        store.setIs_manufucture_product(convertBoolean(array.get(7)));
        store.setIs_online_store(convertBoolean(array.get(8)));
        store.setIs_fan_page(convertBoolean(array.get(9)));
        store.setUrl_fan_page(array.get(10));
        store.setUser_id(Integer.parseInt(array.get(11)));
        return store;
    }

    public List<City> listCities(String country, String state, InputStream inputStream){
        List<City> list = new ArrayList<>();
        String json = loadJsonFromAsset(inputStream);
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        JsonArray arrayData = jsonObject.get("data").getAsJsonArray();
        for (int i = 0; i < arrayData.size(); i++) {
            JsonObject jsonCountries = arrayData.get(i).getAsJsonObject();
            String name_country = jsonCountries.get("name").getAsString();
            if(name_country.equalsIgnoreCase(country)){
                JsonArray arrayStates = jsonCountries.get("states").getAsJsonArray();
                for (int j = 0; j < arrayStates.size(); j++) {
                    JsonObject jsonStates = arrayStates.get(j).getAsJsonObject();
                    String name_state = jsonStates.get("name").getAsString();
                    if(name_state.equalsIgnoreCase(state)){
                        JsonArray arrayCities = jsonStates.get("cities").getAsJsonArray();
                        for (int k = 0; k < arrayCities.size(); k++) {
                            JsonObject jsonCities = arrayCities.get(k).getAsJsonObject();
                            City city = new City();
                            city.setId(jsonCities.get("id").getAsInt());
                            city.setName(jsonCities.get("name").getAsString());
                            city.setState_id(jsonCities.get("state_id").getAsInt());
                            list.add(city);
                        }
                        break;
                    }
                }
            }
        }
        return list;
    }

    public String loadJsonFromAsset(InputStream inputStream){
        String json;
        try {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        System.out.println(json);
        return json;
    }

    public Boolean convertBoolean(String value){
        return value.equalsIgnoreCase("Yes");
    }

    public Boolean convertBooleanPerson(String value){
        return value.equalsIgnoreCase("Person");
    }

    public JsonArray toJsonArray(List<Store> list){
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(list, new TypeToken<List<Store>>() {
        }.getType());
        return element.getAsJsonArray();
    }

    public List<String> parseFullName(String full_name){
        List<String> parse_name = new ArrayList<>();
        int start = full_name.indexOf(' ');
        int end = full_name.lastIndexOf(' ');
        String firstName = "";
        String middleName = "";
        String lastName = "";
        if (start >= 0) {
            firstName = full_name.substring(0, start);
            if (end > start) {
                middleName = full_name.substring(start + 1, end);
            }
            lastName = full_name.substring(end + 1, full_name.length());
        }
        parse_name.add(firstName);
        parse_name.add(middleName);
        parse_name.add(lastName);
        return parse_name;
    }

    public List<String> titleTabs(List<Link> list){
        List<String> titles = new ArrayList<>();
        titles.add("Profile");
        for (int i = 0; i < list.size(); i++) {
            titles.add(list.get(i).getProvider());
        }
        return titles;
    }

    public JsonObject convertStringToGson(String body){
        JsonParser parser = new JsonParser();
        return parser.parse(body).getAsJsonObject();
    }

    public List<RequestImage> parseJsonInstagram(JsonObject jsonObject){
        List<RequestImage> images = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject json = jsonArray.get(i).getAsJsonObject();
            String type = json.get("type").getAsString();
            if(type.equalsIgnoreCase("image")){
                RequestImage request = new RequestImage();
                request.setUrl_image(getImageFromJson(json));
                request.setDescription(getDescriptionFromJson(json));
                request.setId_media(getIdMediaFromJson(json));
                images.add(request);
            }
        }
        return images;
    }

    public String getImageFromJson(JsonObject json){
        JsonObject jsonImage = json.get("images").getAsJsonObject();
        JsonObject jsonResolution = jsonImage.get("low_resolution").getAsJsonObject();
        return jsonResolution.get("url").getAsString();
    }

    public String getDescriptionFromJson(JsonObject json){
        String text = "";
        if(!json.get("caption").isJsonNull()){
            JsonObject jsonCaption = json.get("caption").getAsJsonObject();
            text = jsonCaption.get("text").getAsString();
        }
        return text;
    }

    public String getIdMediaFromJson(JsonObject json){
        String id_media = "";
        if(!json.get("caption").isJsonNull()){
            JsonObject jsonCaption = json.get("caption").getAsJsonObject();
            id_media = jsonCaption.get("id").getAsString();
        }
        return id_media;
    }

    public String parseDateTimeFacebook(String date){
        String[] parse = date.split("T");
        return parse[0];
    }

}
