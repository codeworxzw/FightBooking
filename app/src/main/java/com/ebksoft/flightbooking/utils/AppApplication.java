package com.ebksoft.flightbooking.utils;

import android.app.Application;

/**
 * Created by chauminhnhut on 10/24/15.
 */
public class AppApplication extends Application {

    static AppApplication instance;

    public static AppApplication getInstance(){
        if (null==instance)
            instance = new AppApplication();
        return  instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    ///

    public boolean isInternetConnection = false;

    public String FromCityCode = "";
    public String ToCityCode = "";
    public int countAdult = 0;
    public int countChild = 0;
    public int countIndent = 0;


}
