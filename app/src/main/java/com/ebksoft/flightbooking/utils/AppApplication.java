package com.ebksoft.flightbooking.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ebksoft.flightbooking.model.HistorySearchTrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chauminhnhut on 10/24/15.
 */
public class AppApplication extends Application {

    static AppApplication instance;

    public static AppApplication getInstance(Context context) {
        if (null == instance)
            instance = (AppApplication) context.getApplicationContext();
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /*
    * Các giá trị cần lưu tạm để sử dụng
    * */

    public List<HistorySearchTrip> historySearchTrips;

    public String FromCityCode = "";
    public String ToCityCode = "";
    public int countAdult = 0;
    public int countChild = 0;
    public int countIndent = 0;

    public int indexChooseTicketGo = -1;
    public int indexChooseTicketBack = -1;

    public boolean isOneWay = true;

    public void resetData() {

        FromCityCode = "";
        ToCityCode = "";

        countAdult = 0;
        countChild = 0;
        countIndent = 0;

        indexChooseTicketGo = -1;
        indexChooseTicketBack = -1;
    }

    private boolean isInternetConnnection = false;

    public boolean isInternetConnnection() {
        return isInternetConnnection;
    }

    public void setInternetConnnection(boolean isInternetConnnection) {
        this.isInternetConnnection = isInternetConnnection;
    }


}
