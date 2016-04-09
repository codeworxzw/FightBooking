package com.ebksoft.flightbooking.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ebksoft.flightbooking.model.HistorySearchTrip;
import com.ebksoft.flightbooking.model.TicketInfo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chauminhnhut on 10/24/15.
 */
public class AppApplication extends Application {

    static AppApplication instance;

    public static AppApplication getInstance(Context context) {
        if (null == instance)
            instance = new AppApplication();
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

        ticketInfoWayBack = null;
        ticketInfoWayGo = null;
        passenger = null;
    }

    /*
    * Internet Params
    * */
    private boolean isInternetConnnection = false;

    public boolean isInternetConnnection() {
        return isInternetConnnection;
    }

    public void setInternetConnnection(boolean isInternetConnnection) {
        this.isInternetConnnection = isInternetConnnection;
    }

    /*
    * Current Passenger Info
    * */

    public JSONArray getPassenger() {
        return passenger;
    }

    public void setPassenger(JSONArray passenger) {
        this.passenger = passenger;
    }

    public JSONArray passenger;

    /*
    * Lưu tạm thông tin 2 vé đi và về
    * */

    public TicketInfo getTicketInfoWayGo() {
        return ticketInfoWayGo;
    }

    public void setTicketInfoWayGo(TicketInfo ticketInfoWayGo) {
        this.ticketInfoWayGo = ticketInfoWayGo;
    }

    public TicketInfo ticketInfoWayGo;

    public TicketInfo getTicketInfoWayBack() {
        return ticketInfoWayBack;
    }

    public void setTicketInfoWayBack(TicketInfo ticketInfoWayBack) {
        this.ticketInfoWayBack = ticketInfoWayBack;
    }

    public TicketInfo ticketInfoWayBack;


}
