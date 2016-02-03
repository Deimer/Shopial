package com.puerto.libre.shopial.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Deimer Villa on 28/1/2016.
 */
public class DataOptions {

    public List<String> optionCategories(){
        List<String> listaItems = new ArrayList<>();
        listaItems.add("Shoes");
        listaItems.add("Clothes");
        listaItems.add("Watches");
        listaItems.add("Handbags");
        listaItems.add("Technology");
        return listaItems;
    }

    public List<String> longBusiness(){
        List<String> list = new ArrayList<>();
        list.add("One year or less");
        list.add("1 to 2 years");
        list.add("2 to 5 years");
        list.add("Over 5 years");
        return list;
    }

    public ArrayList<String> scopesFacebook(){
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add("public_profile");
        scopes.add("user_friends");
        scopes.add("email");
        //scopes.add("user_birthday");
        //scopes.add("user_hometown");
        //scopes.add("user_location");
        scopes.add("user_posts");
        //scopes.add("read_insights");
        //scopes.add("read_page_mailboxes");
        //scopes.add("manage_pages");
        //scopes.add("publish_pages");
        //scopes.add("publish_actions");
        return scopes;
    }

}
