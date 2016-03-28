package com.ebksoft.flightbooking;

import android.os.Bundle;

import com.ebksoft.flightbooking.BaseActivity;

/**
 * Created by chauminhnhut on 3/28/16.
 */
public class ManageBookingTicket extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manage_booking_ticket_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_manage_booking_ticket));
        initButtonBack();

    }

    @Override
    protected void loadData() {

    }
}
