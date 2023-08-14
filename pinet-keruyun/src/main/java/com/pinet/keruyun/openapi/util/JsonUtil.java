package com.pinet.keruyun.openapi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pinet.keruyun.openapi.vo.KryResponse;

public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) throws JsonSyntaxException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(json, typeToken.getType());
    }

    public static <T> KryResponse<T> fromJson(String responseStr, TypeReference<KryResponse<T>> typeReference) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(responseStr, typeReference.getType());
    }
}
