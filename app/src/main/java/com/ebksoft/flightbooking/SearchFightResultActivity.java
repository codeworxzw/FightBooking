package com.ebksoft.flightbooking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.flightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SearchResObj;
import com.ebksoft.flightbooking.model.TicketInfo;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.ImageUtils;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chauminhnhut on 1/7/16.
 */
public class SearchFightResultActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvPass2Day, tvPass1Day, tvCurrentDay, tvNext1Day, tvNext2Day;
    private TextView tvPass2Time, tvPass1Time, tvCurrentTime, tvNext1Time, tvNext2Time;
    private View llPass2, llPass1, llCurrent, llNext1, llNext2;
    private Button btWaygo, btWayback, btBrief;

    private int indexTab = 1;
//    private int indexSelected = 3;


    private List<TicketInfo> ticketInfos;
    private CustomAdapter adapter;
    private ListView listView;
    private View headerView;

    private TextView tvPlaceFromTo, tvTimeGoTo;

    private boolean isOneWay = true;
    private String[] dayOfWeek;

    private String timeGo = "", timeBack = "";
    private long time_go = 0, time_back = 0;

    private String session_key = "";

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

        listView = (ListView) findViewById(R.id.listView);

        headerView = getLayoutInflater().inflate(R.layout.layout_ticket_row_header, null);

        tvPlaceFromTo = (TextView) findViewById(R.id.tvPlaceFromTo);
        tvTimeGoTo = (TextView) findViewById(R.id.tvTimeGoTo);
    }

    @Override
    protected void loadData() {
        dayOfWeek = getResources().getStringArray(R.array.dayOfWeek);

        showDataOnList();

        // Thuc hien cac cong viec khac trong khi dang cho request
        loadDataActionbar();

        updateNumOfTab();

        focusColorTab(indexTab);

        focusColorDate(3);

        updateDayText();

        // updateTimeActionbar();
    }

    private void showDataOnList() {
        ticketInfos = new ArrayList<>();
        adapter = new CustomAdapter(this, ticketInfos);
        listView.setAdapter(adapter);

        getTicket();
    }

    private void updateDayText() {
        Calendar calendar = Calendar.getInstance();

        long t = time_go;
        if (indexTab == 2) {
            t = time_back;
        }

        calendar.setTimeInMillis(t);
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int indexTemp = index;
        String dateGo = dayOfWeek[index];
        String timeGo = String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

        tvCurrentDay.setText(dateGo);
        tvCurrentTime.setText(timeGo);

        //Next 1 day
        calendar.setTimeInMillis(t + 86400000);
        index = (index + 1) % dayOfWeek.length;
        dateGo = dayOfWeek[index];
        timeGo = String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
        tvNext1Day.setText(dateGo);
        tvNext1Time.setText(timeGo);

        //Next 2 day
        calendar.setTimeInMillis(t + 86400000 * 2);
        index = (index + 1) % dayOfWeek.length;
        dateGo = dayOfWeek[index];
        timeGo = String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
        tvNext2Day.setText(dateGo);
        tvNext2Time.setText(timeGo);

        index = indexTemp;

        //Prev 1 day
        calendar.setTimeInMillis(t - 86400000);
        if (index == 0)
            index = dayOfWeek.length - 1;
        else
            index--;
        dateGo = dayOfWeek[index];
        timeGo = String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
        tvPass1Day.setText(dateGo);
        tvPass1Time.setText(timeGo);

        //Prev 2 day
        calendar.setTimeInMillis(t - 86400000 * 2);
        if (index == 0)
            index = dayOfWeek.length - 1;
        else
            index--;
        dateGo = dayOfWeek[index];
        timeGo = String.format("%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);
        tvPass2Day.setText(dateGo);
        tvPass2Time.setText(timeGo);
    }


    private void updateNumOfTab() {
        if (isOneWay) {
            btWayback.setVisibility(View.GONE);
        }
    }

    private void loadDataActionbar() {

        Calendar calendar = Calendar.getInstance();

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("place_from") + " - " + bundle.getString("place_to");
        tvPlaceFromTo.setText(s.toUpperCase(Locale.getDefault()));

        timeGo = bundle.getString("time_go");
        time_go = Long.parseLong(timeGo);
        calendar.setTimeInMillis(time_go);
        timeGo = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        timeGo += ", " + String.format("%02d/%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        timeBack = bundle.getString("time_back");
        if (!TextUtils.isEmpty(timeBack)) {

            time_back = Long.parseLong(timeBack);
            calendar.setTimeInMillis(time_back);
            timeBack = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            timeBack += ", " + String.format("%02d/%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

            isOneWay = false;
        }

        updateTimeGoTo();

    }

    private void updateTimeActionbar() {
        if (indexTab == 1) {
            long lTimeGo = time_go;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lTimeGo);
            timeGo = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            timeGo += ", " + String.format("%02d/%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }

        if (indexTab == 2) {
            long lTimeGo = time_back;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lTimeGo);
            timeBack = dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            timeBack += ", " + String.format("%02d/%02d/%02d", calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }

        updateTimeGoTo();
    }

    private void updateTimeGoTo() {
        if (TextUtils.isEmpty(timeBack)) {
            tvTimeGoTo.setText(timeGo);
        } else {
            tvTimeGoTo.setText(timeGo + " - " + timeBack);
        }
    }


    private void getTicket() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));
        params.put("firm_id", "");

        AppRequest.getTicket(this, params, true, new DataRequestCallback<GetTicketResObj>() {
            @Override
            public void onResult(GetTicketResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                listView.removeHeaderView(headerView);
                listView.addHeaderView(headerView);

                ticketInfos.clear();

                if (null != result) {
                    if (result.status.equals("0")) {

                        for (int i = 0; i < result.data.size(); i++) {
                            ticketInfos.add(result.data.get(i));
                        }
                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                }

                adapter.notifyDataSetChanged();
                if (ticketInfos.size() != 0) {
                    listView.smoothScrollToPosition(0);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.llPass2:
                if (checkChoosePrevDay(86400000 * 2)) {
                    if (indexTab == 1) {
                        time_go -= (86400000 * 2);
                    } else if (indexTab == 2)
                        time_back -= (86400000 * 2);

                    updateDayText();
                    updateTimeActionbar();

                    reqInitAPI();
                } else {
                    CommonUtils.showToast(this, getString(R.string.time_back_must_be_over_than_time_go));
                }

                break;

            case R.id.llPass1:
                if (checkChoosePrevDay(86400000)) {
                    if (indexTab == 1) {
                        time_go -= 86400000;
                    } else if (indexTab == 2)
                        time_back -= 86400000;

                    updateDayText();
                    updateTimeActionbar();

                    reqInitAPI();
                } else {
                    CommonUtils.showToast(this, getString(R.string.time_back_must_be_over_than_time_go));
                }

                break;

            case R.id.llCurrent:
                updateDayText();
                break;

            case R.id.llNext1:
                if (checkChooseNextDay(86400000)) {
                    if (indexTab == 1)
                        time_go += 86400000;
                    else if (indexTab == 2)
                        time_back += 86400000;
                    updateDayText();
                    updateTimeActionbar();

                    reqInitAPI();
                } else {
                    CommonUtils.showToast(this, getString(R.string.time_go_must_be_less_than_time_back));
                }

                break;

            case R.id.llNext2:
                if (checkChooseNextDay(86400000 * 2)) {
                    if (indexTab == 1)
                        time_go += (86400000 * 2);
                    else if (indexTab == 2)
                        time_back += (86400000 * 2);
                    updateDayText();
                    updateTimeActionbar();

                    reqInitAPI();
                } else {
                    CommonUtils.showToast(this, getString(R.string.time_go_must_be_less_than_time_back));
                }

                break;

            case R.id.btWaygo:
                indexTab = 1;
                focusColorTab(indexTab);

                updateDayText();
                updateTimeActionbar();
                break;

            case R.id.btWayback:
                indexTab = 2;
                focusColorTab(indexTab);

                updateDayText();
                updateTimeActionbar();
                break;

            case R.id.btBrief:
                indexTab = 3;
                focusColorTab(indexTab);
                break;
        }


    }

    private boolean checkChooseNextDay(long nextDay) {
        if (!isOneWay) {// phai 2 chieu moi set DK

            if (indexTab == 1) {// CHon thoi gian di moi
                if ((time_go + nextDay) > time_back)
                    return false;
            }
        }
        return true;
    }

    private boolean checkChoosePrevDay(long prevDay) {
        if (!isOneWay) {

            if (indexTab == 2) {
                if ((time_back - prevDay) < time_go)
                    return false;
            }


        }
        return true;
    }

    private void reqInitAPI() {
        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", "canhdieuvietnet");
        params.put("ip_address", CommonUtils.getIPAddress(this));

        AppRequest.reqInitAPI(this, params, true, new DataRequestCallback<InitResObj>() {
            @Override
            public void onResult(InitResObj result, boolean continueWaiting) {

                if (null != result) {
                    if (result.status.equals("0")) {
                        session_key = result.data.session_key;
                        SharedpreferencesUtils.getInstance(SearchFightResultActivity.this).save("session_key", session_key);

                        searchFight();
                    } else {
                        session_key = "";
                        CommonUtils.showToast(SearchFightResultActivity.this, result.message);
                    }

                } else {
                    session_key = "";
                }


            }
        });
    }

    private void searchFight() {

        HashMap<String, Object> params = new HashMap<String, Object>();
        AppApplication app = AppApplication.getInstance();
        params.put("session_key", session_key);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FromCityCode", app.FromCityCode);
            jsonObject.put("ToCityCode", app.ToCityCode);
            jsonObject.put("DepartDate", "/Date(" + time_go + ")/");

            if (String.valueOf(time_back).equals("")) {
                jsonObject.put("ReturnDate", "");
            } else {
                jsonObject.put("ReturnDate", "/Date(" + time_back + ")/");
            }

            jsonObject.put("AdultNum", app.countAdult);
            jsonObject.put("ChildNum", app.countChild);
            jsonObject.put("InfantNum", app.countIndent);

            params.put("search_info", jsonObject);
        } catch (Exception ex) {
            Log.e("", "SeerchFight Error");
        }


        AppRequest.searchFight(this, params, true, new DataRequestCallback<SearchResObj>() {
            @Override
            public void onResult(SearchResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();
                if (null != result) {
                    if (result.status.equals("0")) {

                        getTicket();
                    } else {
                        CommonUtils.showToast(SearchFightResultActivity.this, result.message);
                    }

                } else {

                }


            }
        });

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

    private void focusColorDate(int index) {
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
                viewHolder.tvStartDate = (TextView) view.findViewById(R.id.tvStartDate);
                viewHolder.tvAdultTotalPrice = (TextView) view.findViewById(R.id.tvAdultTotalPrice);
                viewHolder.rbChoose = (RadioButton) view.findViewById(R.id.rbChoose);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            TicketInfo ticketInfo = tickets.get(i);

            ImageUtils.load(this.context, viewHolder.imgLogo, ticketInfo.FirmImage);
            viewHolder.tvPlanName.setText(ticketInfo.PlanInfoCollection.get(0).PlanName);

            String startDate = ticketInfo.StartDate;
            startDate = startDate.substring(6, startDate.length() - 2);
            long date = Long.parseLong(startDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            String s = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

            String endDate = ticketInfo.EndDate;
            startDate = startDate.substring(6, startDate.length() - 2);
            date = Long.parseLong(startDate);
            calendar.setTimeInMillis(date);
            String s2 = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));


            viewHolder.tvStartDate.setText(s + " - " + s2);

            viewHolder.tvAdultTotalPrice.setText(String.valueOf(ticketInfo.PriceCollection.get(0).AdultTotalPrice));

            return view;
        }


        public class ViewHolder {
            public ImageView imgLogo;
            public TextView tvPlanName;
            public TextView tvStartDate;
            public TextView tvAdultTotalPrice;
            public RadioButton rbChoose;
        }
    }

}
