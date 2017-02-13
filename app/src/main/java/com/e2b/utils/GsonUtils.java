package com.e2b.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Class is used to object to json string conversion and vice versa.
 */
public class GsonUtils {

    public static <T> T parseJson(String json, Class<T> tClass) {
        return new Gson().fromJson(json, tClass);
    }


    public static <T> T parseJson(String json, Type type) {
        return new Gson().fromJson(json, type);
    }


    public static String getJson(Object profile) {
        return new Gson().toJson(profile);
    }

    public static <T> T getObject(final String jsonString, final Class<T> objectClass) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, objectClass);
    }

}
