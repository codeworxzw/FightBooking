package com.ebksoft.fightbooking.model;

import java.util.ArrayList;

/**
 * Created by chauminhnhut on 1/12/16.
 */
public class TicketInfo {
    public String TicketID;
    public int TypeID;
    public int FirmID;
    public String FirmImage;
    public String FromCityCode;
    public String FromCityName;
    public String ToCityCode;
    public String ToCityName;
    public String StartDate;
    public String EndDate;
    public int DuringTime;

    public ArrayList<PriceCollection> PriceCollection;
    public ArrayList<PlanInfoCollection> PlanInfoCollection;
    public ArrayList<FilghtInfoCollection> FilghtInfoCollection;


    public String HoldToDate;
    public int Pair;
    public int Transit;
    public String TransitInfo;

}
