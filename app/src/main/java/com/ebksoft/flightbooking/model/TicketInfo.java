package com.ebksoft.flightbooking.model;

import java.util.ArrayList;

/**
 * Created by chauminhnhut on 1/12/16.
 */
public class TicketInfo implements Comparable<TicketInfo> {

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

    public boolean isTimeCampare = false;

    @Override
    public int compareTo(TicketInfo ticketInfo) {

        if (isTimeCampare) {
            String startDate = ticketInfo.StartDate;
            startDate = startDate.substring(6, startDate.length() - 2);
            long date = Long.parseLong(startDate);


            String start = StartDate;
            start = start.substring(6, start.length() - 2);
            long date1 = Long.parseLong(start);

            if (date1 < date)
                return 1;
            else if (date1 > date)
                return -1;
            return 0;
        } else {

            double price = ticketInfo.PriceCollection.get(0).AdultTotalPrice;

            double price1 = PriceCollection.get(0).AdultTotalPrice;

            if (price1 < price)
                return 1;
            else if (price1 > price)
                return -1;
            return 0;
        }


    }
}
