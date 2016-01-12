package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chauminhnhut on 1/10/16.
 */
public class ChooseTimeActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_time_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {
        initTitle();
        initButtonBack();

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);

        calendar.setOnDateSelectedListener(this);

    }

    @Override
    protected void loadData() {

        boolean isRoundTrip = getIntent().getExtras().getBoolean("isRoundTrip");
        if (!isRoundTrip) {
            txtTitle.setText(getString(R.string.choose_time_go).toUpperCase(Locale.getDefault()));
        } else {
            txtTitle.setText(getString(R.string.choose_time_back).toUpperCase(Locale.getDefault()));
        }

    }

    @Override
    public void onDateSelected(Date date) {
        Log.e("", "onDateSelected");

        Intent intent = getIntent();
        intent.putExtra("data", String.valueOf(date.getTime()));

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateUnselected(Date date) {
        Log.e("", "onDateUnselected");
    }
}
