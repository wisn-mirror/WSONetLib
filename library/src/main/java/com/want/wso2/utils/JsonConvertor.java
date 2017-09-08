package com.want.wso2.utils;

import com.google.gson.Gson;

/**
 * Created by wisn on 2017/8/22.
 */

public class JsonConvertor {
    private static Gson gson = null;

    private JsonConvertor() {
    }

    public static Gson getInstance() {
        if (gson == null) {
            synchronized (JsonConvertor.class){
                if(gson==null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }
}





//                    new GsonBuilder()
//                    .setPrettyPrinting()
//                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                    .registerTypeAdapter(Date.class, new DateTypeAdapter())
//                    .create();