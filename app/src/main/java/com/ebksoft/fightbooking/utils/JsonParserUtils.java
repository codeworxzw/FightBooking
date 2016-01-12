package com.ebksoft.fightbooking.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class JsonParserUtils {

    /**
     * Parse 1 json array thành 1 danh sách object
     *
     * @param jsonStr
     * @return
     */
    public static <T> List<T> parseJSONObjectToListObject(String jsonStr,
                                                          Type type) {
//        if (TextUtils.isEmpty(jsonStr)){
//            return null;
//        }
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, type);
    }

    /**
     * Parse 1 json object to 1 object
     *
     * @param jsonStr
     * @param type
     * @return
     */
    public static <T> T parseJSONObjectToObject(String jsonStr, Type type) {
//        if (TextUtils.isEmpty(jsonStr)){
//            return null;
//        }
        T data = new Gson().fromJson(jsonStr, type);
        return data;
    }
}