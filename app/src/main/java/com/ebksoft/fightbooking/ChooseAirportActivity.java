package com.ebksoft.fightbooking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * Created by chauminhnhut on 1/7/16.
 */
public class ChooseAirportActivity extends BaseActivity {

    private static final int NUMBER_AIRPORT_NORTH = 3;
    private static final int NUMBER_AIRPORT_MIDDLE = 12;
    private static final int NUMBER_AIRPORT_SOUTH = 6;

    private LinearLayout llSouth, llNorth, llMiddle;

    private String[] airportName;
    private String[] airportCode;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_airport_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        initTitle();
        initButtonBack();

        llSouth = (LinearLayout) findViewById(R.id.llSouth);
        llNorth = (LinearLayout) findViewById(R.id.llNorth);
        llMiddle = (LinearLayout) findViewById(R.id.llMiddle);

    }

    @Override
    protected void loadData() {
        txtTitle.setText(getString(R.string.choose_airport).toUpperCase(Locale.getDefault()));

        airportName = getResources().getStringArray(R.array.airport_name);
        airportCode = getResources().getStringArray(R.array.airport_code);

        inflater = LayoutInflater.from(this);

        for (int i = 0; i < NUMBER_AIRPORT_SOUTH; i++) {
            llSouth.addView(createView(i));
        }

        int sum = NUMBER_AIRPORT_NORTH + NUMBER_AIRPORT_SOUTH;
        for (int i = NUMBER_AIRPORT_SOUTH; i < sum; i++) {
            llNorth.addView(createView(i));
        }

        int all = NUMBER_AIRPORT_NORTH + NUMBER_AIRPORT_SOUTH + NUMBER_AIRPORT_MIDDLE;
        for (int i = sum; i < all; i++) {
            llMiddle.addView(createView(i));
        }
    }

    private View createView(int i) {

        View view = inflater.inflate(R.layout.layout_choose_airport_row_item, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 2, 0, 2);
        view.setLayoutParams(params);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvCode = (TextView) view.findViewById(R.id.tvCode);

        tvName.setText(airportName[i] + " (" + airportCode[i] + ")");
        tvCode.setText("(" + airportCode[i] + ")");

        return view;
    }
}