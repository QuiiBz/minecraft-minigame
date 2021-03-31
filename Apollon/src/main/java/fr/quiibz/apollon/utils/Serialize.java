package fr.quiibz.apollon.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class Serialize {

    /*
     *  FIELDS
     */

    private Gson gson;

    /*
     *  CONSTRUCTOR
     */

    public Serialize() {

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .disableHtmlEscaping()
                .create();
    }

    /*
     *  METHODS
     */

    public String serialize(Object object) {

        return this.gson.toJson(object);
    }

    public <T> Object deserialize(String json, Class<T> clazz) {

        return this.gson.fromJson(json, clazz);
    }

    public <T> Object deserialize(String json, Type type) {

        return this.gson.fromJson(json, type);
    }
}