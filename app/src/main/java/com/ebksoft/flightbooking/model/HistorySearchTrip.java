package com.ebksoft.flightbooking.model;

import java.util.Calendar;

/**
 * Created by chauminhnhut on 3/30/16.
 */
public class HistorySearchTrip {

    public String From;
    public String To;
    public String WayTime;
    public String Passenger;

    //
    public boolean isOneWay;

    public String FromCityCode;
    public String FromCityName;
    public String ToCityCode;
    public String ToCityName;

    public Calendar calendarGo;
    public Calendar calendarBack;

    public int countAdult, countChild, countIdent;
}

