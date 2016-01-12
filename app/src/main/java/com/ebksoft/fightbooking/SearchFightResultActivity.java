package com.ebksoft.fightbooking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebksoft.fightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.fightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.fightbooking.model.TicketInfo;
import com.ebksoft.fightbooking.network.AppRequest;
import com.ebksoft.fightbooking.utils.CommonUtils;
import com.ebksoft.fightbooking.utils.DataRequestCallback;
import com.ebksoft.fightbooking.utils.ImageUtils;
import com.ebksoft.fightbooking.utils.SharedpreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauminhnhut on 1/7/16.
 */
public class SearchFightResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvPass2Day, tvPass1Day, tvCurrentDay, tvNext1Day, tvNext2Day;
    private TextView tvPass2Time, tvPass1Time, tvCurrentTime, tvNext1Time, tvNext2Time;
    private View llPass2, llPass1, llCurrent, llNext1, llNext2;
    private Button btWaygo, btWayback, btBrief;

    private int indexTab = 1;
    private int indexSelected = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_fight_result_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        llPass2 = findViewById(R.id.llPass2);
        llPass2.setOnClickListener(this);

        llPass1 = findViewById(R.id.llPass1);
        llPass1.setOnClickListener(this);

        llCurrent = findViewById(R.id.llCurrent);
        llCurrent.setOnClickListener(this);

        llNext1 = findViewById(R.id.llNext1);
        llNext1.setOnClickListener(this);

        llNext2 = findViewById(R.id.llNext2);
        llNext2.setOnClickListener(this);

        tvPass2Day = (TextView) findViewById(R.id.tvPass2Day);
        tvPass1Day = (TextView) findViewById(R.id.tvPass1Day);
        tvCurrentDay = (TextView) findViewById(R.id.tvCurrentDay);
        tvNext1Day = (TextView) findViewById(R.id.tvNext1Day);
        tvNext2Day = (TextView) findViewById(R.id.tvNext2Day);

        tvPass2Time = (TextView) findViewById(R.id.tvPass2Time);
        tvPass1Time = (TextView) findViewById(R.id.tvPass1Time);
        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);
        tvNext1Time = (TextView) findViewById(R.id.tvNext1Time);
        tvNext2Time = (TextView) findViewById(R.id.tvNext2Time);

        btWaygo = (Button) findViewById(R.id.btWaygo);
        btWayback = (Button) findViewById(R.id.btWayback);
        btBrief = (Button) findViewById(R.id.btBrief);

        btWaygo.setOnClickListener(this);
        btWayback.setOnClickListener(this);
        btBrief.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        updateFocus(indexSelected);
        focusColorTab(indexTab);

        getTicket();
    }


    private void getTicket() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));
        params.put("firm_id", "");

        AppRequest.getTicket(this, params, true, new DataRequestCallback<GetTicketResObj>() {
            @Override
            public void onResult(GetTicketResObj result, boolean continueWaiting) {

                if (null != result) {
                    if (result.status.equals("0")) {

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                }


            }
        });
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.llPass2:
                indexSelected = 1;
                updateFocus(indexSelected);
                break;

            case R.id.llPass1:
                indexSelected = 2;
                updateFocus(indexSelected);
                break;

            case R.id.llCurrent:
                indexSelected = 3;
                updateFocus(indexSelected);
                break;

            case R.id.llNext1:
                indexSelected = 4;
                updateFocus(indexSelected);
                break;

            case R.id.llNext2:
                indexSelected = 5;
                updateFocus(indexSelected);
                break;

            case R.id.btWaygo:
                indexTab = 1;
                focusColorTab(indexTab);
                break;

            case R.id.btWayback:
                indexTab = 2;
                focusColorTab(indexTab);
                break;

            case R.id.btBrief:
                indexTab = 3;
                focusColorTab(indexTab);
                break;
        }


    }


    private void resetTextColor() {
        tvPass2Day.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvPass1Day.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvCurrentDay.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvNext1Day.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvNext2Day.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));

        tvPass2Time.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvPass1Time.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvCurrentTime.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvNext1Time.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
        tvNext2Time.setTextColor(ContextCompat.getColor(this, R.color.homeTextColor));
    }

    private void resetBackgroundColor() {
        llPass2.setBackgroundResource(R.drawable.bg_non_focus_left);
        llPass1.setBackgroundResource(R.drawable.bg_non_focus_center);
        llCurrent.setBackgroundResource(R.drawable.bg_non_focus_center);
        llNext1.setBackgroundResource(R.drawable.bg_non_focus_center);
        llNext2.setBackgroundResource(R.drawable.bg_non_focus_right);
    }

    private void updateFocus(int index) {
        resetTextColor();
        resetBackgroundColor();

        switch (index) {
            case 1:
                tvPass2Day.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvPass2Time.setTextColor(ContextCompat.getColor(this, R.color.white));
                llPass2.setBackgroundResource(R.drawable.bg_focus_left);
                break;

            case 2:
                tvPass1Day.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvPass1Time.setTextColor(ContextCompat.getColor(this, R.color.white));
                llPass1.setBackgroundResource(R.drawable.bg_focus_center);
                break;

            case 3:
                tvCurrentDay.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvCurrentTime.setTextColor(ContextCompat.getColor(this, R.color.white));
                llCurrent.setBackgroundResource(R.drawable.bg_focus_center);
                break;

            case 4:
                tvNext1Day.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvNext1Time.setTextColor(ContextCompat.getColor(this, R.color.white));
                llNext1.setBackgroundResource(R.drawable.bg_focus_center);
                break;

            case 5:
                tvNext2Day.setTextColor(ContextCompat.getColor(this, R.color.white));
                tvNext2Time.setTextColor(ContextCompat.getColor(this, R.color.white));
                llNext2.setBackgroundResource(R.drawable.bg_focus_right);
                break;
        }


    }

    private void resetColorTab() {
        btWaygo.setBackgroundResource(R.color.colorPrimary);
        btWayback.setBackgroundResource(R.color.colorPrimary);
        btBrief.setBackgroundResource(R.color.colorPrimary);
    }

    private void focusColorTab(int index) {
        resetColorTab();

        switch (index) {
            case 1:
                btWaygo.setBackgroundResource(R.color.colorPrimaryDark);
                break;

            case 2:
                btWayback.setBackgroundResource(R.color.colorPrimaryDark);
                break;

            case 3:
                btBrief.setBackgroundResource(R.color.colorPrimaryDark);
                break;
        }
    }

    public class CustomAdapter extends BaseAdapter {

        List<TicketInfo> tickets;
        LayoutInflater inflater;
        Context context;

        public CustomAdapter(Context context, List<TicketInfo> tickets) {
            this.tickets = tickets;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public int getCount() {
            if (tickets != null)
                return tickets.size();
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = inflater.inflate(R.layout.layout_ticket_row_item, viewGroup, false);

                viewHolder.imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
                viewHolder.tvPlanName = (TextView) view.findViewById(R.id.tvPlanName);
                viewHolder.tvTicketClassName = (TextView) view.findViewById(R.id.tvTicketClassName);
                viewHolder.tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
                viewHolder.tvEndDate = (TextView) view.findViewById(R.id.tvEndDate);
                viewHolder.tvAdultTotalPrice = (TextView) view.findViewById(R.id.tvAdultTotalPrice);
                viewHolder.rbChoose = (RadioButton) view.findViewById(R.id.rbChoose);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            TicketInfo ticketInfo = tickets.get(i);

            ImageUtils.load(this.context, viewHolder.imgLogo, ticketInfo.FirmImage);
            viewHolder.tvPlanName.setText(ticketInfo.PlanInfoCollection.get(0).PlanName);
            viewHolder.tvTicketClassName.setText(ticketInfo.PriceCollection.get(0).TicketClassName);
            viewHolder.tvAdultTotalPrice.setText(String.valueOf(ticketInfo.PriceCollection.get(0).AdultTotalPrice));

            String startDate = ticketInfo.StartDate;
            startDate = startDate.substring(6, startDate.length() - 2);
            long date = Long.parseLong(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            String s = String.format("%0.2d:%0.2d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.SECOND));
            viewHolder.tvStartDate.setText(s);

            return view;
        }


        public class ViewHolder {
            public ImageView imgLogo;
            public TextView tvPlanName;
            public TextView tvTicketClassName;
            public TextView tvStartDate;
            public TextView tvEndDate;
            public TextView tvAdultTotalPrice;
            public RadioButton rbChoose;
        }
    }

}
