package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ebksoft.flightbooking.utils.CommonUtils;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by chauminhnhut on 1/10/16.
 */
public class ChooseTimeActivity extends BaseActivity implements CalendarPickerView.OnDateSelectedListener {

    private CalendarPickerView calendar;

    private boolean isRoundTrip = false;
    private boolean isOneWay = false;
    private long longDepartDate = 0, longReturnDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_choose_time_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        initButtonBack();

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime())
                .withSelectedDate(today);

        calendar.setOnDateSelectedListener(this);

    }

    @Override
    protected void loadData() {

        Bundle bundle = getIntent().getExtras();
        isRoundTrip = bundle.getBoolean("isRoundTrip");
        isOneWay = bundle.getBoolean("isOneWay");

        if (!isRoundTrip) {
            initTitle(getString(R.string.choose_time_go));
        } else {
            initTitle(getString(R.string.choose_time_back));
        }

        Date date;

        String DepartDate = bundle.getString("DepartDate");
        if (TextUtils.isEmpty(DepartDate)) {
            longDepartDate = 0;
            date = new Date(System.currentTimeMillis());
        } else {
            longDepartDate = Long.parseLong(DepartDate);
            date = new Date(longDepartDate);
        }

        String ReturnDate = bundle.getString("ReturnDate");
        if (TextUtils.isEmpty(ReturnDate)) {
            longReturnDate = 0;
            date = new Date(System.currentTimeMillis());
        }else {
            longReturnDate = Long.parseLong(ReturnDate);
            date = new Date(longReturnDate);
        }

//        calendar.selectDate(date);

    }

    @Override
    public void onDateSelected(Date date) {
        Log.e("", "onDateSelected");

        if (!isRoundTrip) {// Chon thoi gian di
            if (!isOneWay && this.longReturnDate != 0 && date.getTime() > this.longReturnDate) {
                CommonUtils.showToast(this, getString(R.string.time_go_must_be_less_than_time_back));
                return;
            }
        } else {// Chon thoi gian ve
            if (this.longDepartDate != 0 && date.getTime() < this.longDepartDate) {
                CommonUtils.showToast(this, getString(R.string.time_back_must_be_over_than_time_go));
                return;
            }
        }

        Intent intent = getIntent();
        intent.putExtra("data", String.valueOf(date.getTime()));

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateUnselected(Date date) {
        Log.e("", "onDateUnselected");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
