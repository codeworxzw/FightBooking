package com.ebksoft.flightbooking.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

/**
 * Created by chauminhnhut on 12/1/15.
 */
public class SharedpreferencesUtils {

    static SharedpreferencesUtils instance;
    private SharedPreferences preferences;

    public static SharedpreferencesUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SharedpreferencesUtils(context);
        }
        return instance;
    }

    public SharedpreferencesUtils(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    ////////////////////////////////

    public void save(String key, String value) {
        SharedPreferences.Editor prefsEditor = preferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String read(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        return json;
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getDefaultSharedPreferencesName(Context context) {

        String s = context.getFilesDir().getAbsolutePath();

        return s + "/" + context.getPackageName() + "_preferences";
    }
}
