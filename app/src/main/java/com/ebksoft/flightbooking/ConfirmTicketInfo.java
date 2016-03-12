package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.BagModel;
import com.ebksoft.flightbooking.model.BookingResultModel;
import com.ebksoft.flightbooking.model.ResponseObj.BookingResultResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetBagResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SVResponseObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;
import com.ebksoft.flightbooking.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by chauminhnhut on 3/6/16.
 */
public class ConfirmTicketInfo extends BaseActivity implements View.OnClickListener {

    private TextView tvDeadlineToPay, tvBriefTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_confirm_ticket_info);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_info));
        initButtonBack();
        
        tvDeadlineToPay = (TextView) findViewById(R.id.tvDeadlineToPay);
        tvBriefTicket = (TextView) findViewById(R.id.tvBriefTicket);

        findViewById(R.id.btKeepBooking).setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        getBookingResult();
    }

    private void getBookingResult() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));


        AppRequest.getBookingResult(this, params, true, new DataRequestCallback<BookingResultResObj>() {
            @Override
            public void onResult(BookingResultResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {

                        BookingResultModel data = result.data;

                        int start = data.HoldToDate.indexOf("(");
                        int end = data.HoldToDate.indexOf(")");
                        String time = data.HoldToDate.substring(start + 1, end);
                        if (time.length() == 13) {
                            long val = Long.parseLong(time);
                            Date date = new Date(val);
                            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                            tvDeadlineToPay.setText(df2.format(date));
                        } else {
                            tvDeadlineToPay.setText("...");
                        }

                        tvBriefTicket.setText(data.FlightInfo);

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btKeepBooking) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}
