package com.ebksoft.fightbooking.utils;

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

    public boolean isAudioPlaying = false;
    public int currentSong = 0;
    public int currentList = 0;

    public String currentSongName = "";
    public String currentSongDesc = "";

    public boolean currentError = false;

}
