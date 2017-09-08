package com.want.wso2.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by wisn on 2017/8/21.
 */

public class JsonTool {
    private static  Gson gson=new Gson();
    public static Gson getInstance(){
        return gson;
    }
    /**
     *
     * @param operation
     * @return
     */
    public static String toJson(Object  operation) {
        try {
            return gson.toJson(operation);
        } catch (Exception e) {
            return "{}";
        }
    }
    public static <T> T fromJson(String jsonStr,Class<T>  T) {
        try {
            return gson.fromJson(jsonStr,T);
        } catch (Exception e) {
            return null;
        }
    }
    public static Object fromJson(String jsonStr,Type type) {
        try {
            return gson.fromJson(jsonStr,type);
        } catch (Exception e) {
            return null;
        }
    }
}
