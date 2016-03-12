package com.ebksoft.flightbooking;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.flightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SVResponseObj;
import com.ebksoft.flightbooking.model.ResponseObj.SearchResObj;
import com.ebksoft.flightbooking.model.TicketInfo;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.ImageUtils;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by chauminhnhut on 1/7/16.
 */
public class SearchFightResultActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private TextView tvPass2Day, tvPass1Day, tvCurrentDay, tvNext1Day, tvNext2Day;
    private TextView tvPass2Time, tvPass1Time, tvCurrentTime, tvNext1Time, tvNext2Time;
    private View llPass2, llPass1, llCurrent, llNext1, llNext2;
    private Button btWaygo, btWayback, btBrief;
    private View imvSetting;

    private LinearLayout llSummary;

    private CustomAdapter adapter;
    private ListView listView;
    private View headerView;

    private TextView tvPlaceFromTo, tvTimeGoTo;

    private String[] dayOfWeek;

    private String timeGo = "", timeBack = "";
    private long time_go = 0, time_back = 0;

    private Dialog dialog;

    private View rlTotalPrice;

    private AppApplication application;

    private TextView tvTotalBill;

    /*
   * Layout for man hình tóm tắt
   * */
    private View scSummary;

    /*Session key*/
    private String session_key = "";

    /*
   * Index tab đang focus:
   * 1: CHIEU DI
   * 2: CHIEU VE
   * 3: TOM TAT
   * */
    private int indexTab = 1;

    /*
    Mangr chứa danh sách các vé đang hiển thị
    * */
    private List<TicketInfo> ticketInfos;

    /*
  * Mảng tạm, chưa danh sách vé lấy từ server về lần gọi API {#getTicket}, dung cho tính năng lọc
  * */
    private List<TicketInfo> temp;


    /*
    * true: One Way, false: round trip
    * */
    private boolean isOneWay = true;

    /*
    * Chứa mã hãng và url của hảng tương ứng (dùng cho tính năng lọc)
    * */

    private List<String> FirmIDs, FirmImages;

    /*
    * Điều kiện sort chiều đi
    * */
    private boolean isSortByTimeGo = false;
    private boolean isSortByPriceGo = false;

    /*
    * Điều kiện sort chiều về
    * */
    private boolean isSortByTimeBack = false;
    private boolean isSortByPriceBack = false;


    /*
    * Điều kiện lọc tương ứng của 2 chiều
    * */

    private String filterIDWayGo = "";
    private String filterIDWayBack = "";

    /*
    * Lưu thông tin VÉ khách hàng đã chọn để gưi lên server
    * */
    private TicketInfo ticketWayGo, ticketWayBack;

    /*
    * Flag change tab
    * */

    public boolean isChangeTab = false;

    /*
    * Số lượng Passenger
    * */

    private int countAdult = 0, countChild = 0, countIndent = 0;

    /*
    * Tổng tiền vé
    * */

    private double totalBill = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search_fight_result_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        initButtonBack();

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
        listView.setOnItemClickListener(this);

        headerView = getLayoutInflater().inflate(R.layout.layout_ticket_row_header, null);

        tvPlaceFromTo = (TextView) findViewById(R.id.tvPlaceFromTo);
        tvTimeGoTo = (TextView) findViewById(R.id.tvTimeGoTo);

        imvSetting = findViewById(R.id.imvSetting);
        imvSetting.setOnClickListener(this);

        findViewById(R.id.llNext).setOnClickListener(this);

        scSummary = findViewById(R.id.scSummary);
        llSummary = (LinearLayout) findViewById(R.id.llSummary);

        rlTotalPrice = findViewById(R.id.rlTotalPrice);

        tvTotalBill = (TextView)findViewById(R.id.tvTotalBill);
    }

    @Override
    protected void loadData() {
        dayOfWeek = getResources().getStringArray(R.array.dayOfWeek);

        init();

        showDataOnList();

        loadDataActionbar();

        updateNumOfTab();

        focusColorTab();

        focusColorDate(3);

        updateDayText();
    }

    private void init() {
        FirmIDs = new ArrayList<>();
        FirmImages = new ArrayList<>();

        application = AppApplication.getInstance();

        countAdult = application.countAdult;
        countChild = application.countChild;
        countIndent = application.countIndent;

        temp = new ArrayList<>();
        ticketInfos = new ArrayList<>();

    }

    private void showDataOnList() {

        adapter = new CustomAdapter(this, ticketInfos);
        listView.setAdapter(adapter);

        getTicket();
    }

    /*
    * Cập nhật các ngày trong tuần khi ngày đi thay đổi
    * */
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


    //Nếu chọn 1 chiều thì ẩn tab chiều về đi
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

    /*
    * Cập nhật thời gian hiển thị trên
    * */
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

    /*
    * Thể hiện thời gian về nếu có
    * */
    private void updateTimeGoTo() {
        if (TextUtils.isEmpty(timeBack)) {
            tvTimeGoTo.setText(timeGo);
        } else {
            tvTimeGoTo.setText(timeGo + " - " + timeBack);
        }
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
                tabWayGoClick();
                break;

            case R.id.btWayback:
                tabWayBackClick();
                break;

            case R.id.btBrief:
                tabSummaryClick();
                break;

            case R.id.imvSetting:
                showDialog();
                break;

            case R.id.llNext:
                pressNext();
                break;
        }


    }

    private void resetNewSession() {
        clearAllData();
    }

    /*
    * Click item on listView
    * */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        int index = i - 1;

        if (indexTab == 1) {
            //Lưu vị trí của vé lượt đi lại
            application.indexChooseTicketGo = index;

            //Luu ve nay lai de gui len server
            ticketWayGo = ticketInfos.get(index);
        } else if (indexTab == 2) {
            //Lưu vị trí của vé lượt về lại
            application.indexChooseTicketBack = index;

            ticketWayBack = ticketInfos.get(index);
        }

        notifyDataChanged();
    }

    //Click vao Tab Chiều đi để lấy danh sách cá vé chiều đi
    private void tabWayGoClick() {

        updateChageTabState(1);

        indexTab = 1;

        setShowingSettingButton(true);

        focusColorTab();

        updateDayText();

        updateTimeActionbar();

        loadTicketDepart();

        hideSummaryScreen();
    }

    /*
    * Nếu chuyền từ 1 tab khác qua thì bật cờ change tab lên
    * */

    private void updateChageTabState(int tab) {
        if (indexTab != tab) {
            isChangeTab = true;
        } else {
            isChangeTab = false;
        }
    }

    //Click vao Tab Chiều về để lấy danh sách cá vé chiều về
    private void tabWayBackClick() {

        updateChageTabState(2);

        indexTab = 2;

        setShowingSettingButton(true);

        focusColorTab();

        updateDayText();

        updateTimeActionbar();

        loadTicketReturn();

        hideSummaryScreen();
    }

    private void tabSummaryClick() {
        indexTab = 3;

        focusColorTab();

        //An nut Setting
        setShowingSettingButton(false);

        showSummaryScreen();
    }

    /*
    * Hiển thị hay ẩn nút Setting
    * */
    private void setShowingSettingButton(boolean isShow) {
        if (isShow) {
            imvSetting.setVisibility(View.VISIBLE);
        } else {
            imvSetting.setVisibility(View.INVISIBLE);
        }
    }

    //Bấm nút Next để thực hiện bước tiếp theo
    private void pressNext() {

        if (indexTab == 1 && ticketWayGo == null) {
            CommonUtils.showToast(this, getString(R.string.please_choose_go_ticket));
            return;
        }

        if (indexTab == 2 && ticketWayBack == null) {
            CommonUtils.showToast(this, getString(R.string.please_choose_back_ticket));
            return;
        }

        if (indexTab == 3) {
            if (ticketWayGo == null) {
                tabWayGoClick();
                return;
            }

            if (!isOneWay && ticketWayBack == null) {
                tabWayBackClick();
                return;
            }
        }

        switch (indexTab) {
            case 1:
                if (!isOneWay) {
                    // Next qua tab CHIEU VE
                    tabWayBackClick();
                } else {
                    //Next qua tab TOM TAT
                    tabSummaryClick();
                }
                break;

            case 2:
                //Next qua tab TOM TAT
                tabSummaryClick();
                break;

            case 3:
                //GUi thong tin ve da chon len Server
                sendSelectedTicket();
                break;
        }
    }

    // Show màn hình tóm tắt
    private void showSummaryScreen() {
        llSummary.removeAllViews();

        if (ticketWayGo != null) {
            llSummary.addView(createTicketItemSummary(ticketWayGo, true));
        }

        if (ticketWayBack != null) {
            llSummary.addView(createTicketItemSummary(ticketWayBack, false));
        }


        String price  = String.format("TỔNG CỘNG: %1$,.1f VND",totalBill);
        tvTotalBill.setText(price);

        if (ticketWayGo != null) {
            rlTotalPrice.setVisibility(View.VISIBLE);
        } else {
            rlTotalPrice.setVisibility(View.GONE);
        }

        scSummary.setVisibility(View.VISIBLE);
    }

    private View createTicketItemSummary(TicketInfo ticketInfo, boolean isWayGo) {
        View view = getLayoutInflater().inflate(R.layout.layout_summary_item, null);
        TextView tvWayFlight = (TextView) view.findViewById(R.id.tvWayFlight);
        TextView tvCodeFlight = (TextView) view.findViewById(R.id.tvCodeFlight);
        ImageView imvLogoFirm = (ImageView) view.findViewById(R.id.imvLogoFirm);

        TextView tvFirmNameFrom = (TextView) view.findViewById(R.id.tvFirmNameFrom);
        TextView tvFirmNameTo = (TextView) view.findViewById(R.id.tvFirmNameTo);


        TextView tvTimeFrom = (TextView) view.findViewById(R.id.tvTimeFrom);
        TextView tvTimeTo = (TextView) view.findViewById(R.id.tvTimeTo);

        TextView tvNumOfAdult = (TextView) view.findViewById(R.id.tvNumOfAdult);
        TextView tvNumOfAdult2 = (TextView) view.findViewById(R.id.tvNumOfAdult2);
        TextView tvPriceOfAdult = (TextView) view.findViewById(R.id.tvPriceOfAdult);

        TextView tvNumOfChild = (TextView) view.findViewById(R.id.tvNumOfChild);
        TextView tvNumOfChild2 = (TextView) view.findViewById(R.id.tvNumOfChild2);
        TextView tvPriceOfChild = (TextView) view.findViewById(R.id.tvPriceOfChild);

        TextView tvNumOfIndent = (TextView) view.findViewById(R.id.tvNumOfIndent);
        TextView tvNumOfIndent2 = (TextView) view.findViewById(R.id.tvNumOfIndent2);
        TextView tvPriceOfIndent = (TextView) view.findViewById(R.id.tvPriceOfIndent);

        TextView tvTotalPrice = (TextView) view.findViewById(R.id.tvTotalPrice);

        View viewParentAdult = view.findViewById(R.id.viewParentAdult);
        View viewParentChild = view.findViewById(R.id.viewParentChild);
        View viewParentIndent = view.findViewById(R.id.viewParentIndent);

        //Set Value

        /*Chuyến đi hay về*/
        if (isWayGo) {
            tvWayFlight.setText(getString(R.string.go_way).toUpperCase(Locale.getDefault()));
        } else {
            tvWayFlight.setText(getString(R.string.back_way).toUpperCase(Locale.getDefault()));
        }

        /*Tên Chuyến bay*/
        tvCodeFlight.setText(ticketInfo.PlanInfoCollection.get(0).PlanName);

        /*Logo hãng*/
        ImageUtils.load(this, imvLogoFirm, ticketInfo.FirmImage);

        /*Mã thành phố đi, đến*/
        tvFirmNameFrom.setText(application.FromCityCode);
        tvFirmNameTo.setText(application.ToCityCode);

        /*Thời gian đi đến*/
        String startDate = ticketInfo.StartDate;
        startDate = startDate.substring(6, startDate.length() - 2);
        long date = Long.parseLong(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        String s = String.format("%02d:%02d, %02d/%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

        String endDate = ticketInfo.EndDate;
        endDate = endDate.substring(6, endDate.length() - 2);
        date = Long.parseLong(endDate);
        calendar.setTimeInMillis(date);
        String s2 = String.format("%02d:%02d, %02d/%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1);

        tvTimeFrom.setText(s);
        tvTimeTo.setText(s2);

        /*Nếu có người lớn đi*/
        double adultPrice = ticketInfo.PriceCollection.get(0).AdultTotalPrice;
        if (countAdult != 0) {
            tvNumOfAdult.setText(String.valueOf(countAdult));
            tvNumOfAdult2.setText(String.valueOf(countAdult) + " x ");

            String price  = String.format("%1$,.1f VND",adultPrice);
            tvPriceOfAdult.setText(price);
        } else {
            viewParentAdult.setVisibility(View.GONE);
        }

        /*Nếu có trẻ em*/
        double childPrice = ticketInfo.PriceCollection.get(0).ChildTotalPrice;
        if (countChild != 0) {
            tvNumOfChild.setText(String.valueOf(countChild));
            tvNumOfChild2.setText(String.valueOf(countChild) + " x ");

            String price  = String.format("%1$,.1f VND",childPrice);
            tvPriceOfChild.setText(price);
        } else {
            viewParentChild.setVisibility(View.GONE);
        }

        /*Nếu có sơ sinh*/
        double indentPrice = ticketInfo.PriceCollection.get(0).BabyTotalPrice;
        if (countIndent != 0) {
            tvNumOfIndent.setText(String.valueOf(countIndent));
            tvNumOfIndent2.setText(String.valueOf(countIndent) + " x ");

            String price  = String.format("%1$,.1f VND",indentPrice);
            tvPriceOfIndent.setText(price);
        } else {
            viewParentIndent.setVisibility(View.GONE);
        }

        double totalPrice = countAdult * adultPrice + countChild * childPrice + countIndent * indentPrice;
        String strTotal = String.format("%1$,.1f VND",totalPrice);
        tvTotalPrice.setText(strTotal);

        totalBill+=totalPrice;

        return view;
    }

    //Ẩn layout summary screen
    private void hideSummaryScreen() {
        scSummary.setVisibility(View.GONE);
    }

    /*
    * Lọc danh sách vé lượt đi từ server trả về rồi show lên UI
    * */
    private void loadTicketDepart() {

        ticketInfos.clear();

        //Đổ dữ liệu được lưu ở mạng temp với đk mã hãng vào

        for (int i = 0; i < temp.size(); i++) {
            if (String.valueOf(temp.get(i).TypeID).equals("1")) {

                if (filterIDWayGo.equals(""))//Chưa có ĐK lọc theo hãng
                    ticketInfos.add(temp.get(i));
                else {
                    if (String.valueOf(temp.get(i).FirmID).equals(filterIDWayGo)) {//Đã có ĐK lọc
                        ticketInfos.add(temp.get(i));
                    }
                }
            }
        }

        if (!isSortByTimeGo && !isSortByPriceGo) {//Truong hop chua chon Sort

            notifyDataChanged();

        } else {
            if (isSortByTimeGo)
                updateSort(0);

            if (isSortByPriceGo)
                updateSort(1);
        }
//        ToastUtils.toast(this, "Go: " + String.valueOf(ticketInfos.size()));
    }

    /*
   * Lọc danh sách vé lượt về từ server trả về rồi show lên UI
   * */

    private void loadTicketReturn() {

        ticketInfos.clear();

        //Đổ dữ liệu được lưu ở mạng temp với đk mã hãng vào

        for (int i = 0; i < temp.size(); i++) {
            if (String.valueOf(temp.get(i).TypeID).equals("2")) {

                if (filterIDWayBack.equals(""))//Chưa có ĐK lọc theo hãng
                    ticketInfos.add(temp.get(i));
                else {
                    if (String.valueOf(temp.get(i).FirmID).equals(filterIDWayBack)) {//Đã có ĐK lọc
                        ticketInfos.add(temp.get(i));
                    }
                }

            }
        }

        if (!isSortByTimeBack && !isSortByPriceBack) {//Truong hop chua chon Sort

            notifyDataChanged();

        } else {
            if (isSortByTimeBack)
                updateSort(0);

            if (isSortByPriceBack)
                updateSort(1);
        }


//        ToastUtils.toast(this, "Back: " + String.valueOf(ticketInfos.size()));
    }

    private void showDialog() {

        View v = getLayoutInflater().inflate(R.layout.layout_custom_filter_dialog, null);

        GridView gridView = (GridView) v.findViewById(R.id.gridView);
        CustomBrankAdapter brankAdapter = new CustomBrankAdapter(this, FirmIDs, FirmImages);
        gridView.setAdapter(brankAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Chọn tính năng sort thì huỷ tất cả các thông tin về index, vé đã chọn của tab hiện tại... trước đó
                clearSelectedByTabIndex();

                updateFilter(FirmIDs.get(i));

                dialog.dismiss();
            }
        });

        RadioButton rbTime = (RadioButton) v.findViewById(R.id.rbTime);
        RadioButton rbPrice = (RadioButton) v.findViewById(R.id.rbPrice);

        if (indexTab == 1) {
            rbTime.setChecked(isSortByTimeGo);
            rbPrice.setChecked(isSortByPriceGo);
        } else if (indexTab == 2) {
            rbTime.setChecked(isSortByTimeBack);
            rbPrice.setChecked(isSortByPriceBack);
        }


        rbTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (indexTab == 1) {
                    isSortByTimeGo = true;
                    isSortByPriceGo = false;
                } else if (indexTab == 2) {
                    isSortByTimeBack = true;
                    isSortByPriceBack = false;
                }

                // Chọn tính năng sort thì huỷ tất cả các thông tin về index, vé đã chọn... trước đó
                clearSelectedByTabIndex();

                updateSort(0);//sort theo thoi gian
            }
        });

        rbPrice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (indexTab == 1) {
                    isSortByTimeGo = false;
                    isSortByPriceGo = true;
                } else if (indexTab == 2) {
                    isSortByTimeBack = false;
                    isSortByPriceBack = true;
                }

                // Chọn tính năng sort thì huỷ tất cả các thông tin về index, vé đã chọn... trước đó
                clearSelectedByTabIndex();

                updateSort(1);//sort theo gia
            }
        });

        v.findViewById(R.id.llTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (indexTab == 1) {
                    isSortByTimeGo = true;
                    isSortByPriceGo = false;
                } else if (indexTab == 2) {
                    isSortByTimeBack = true;
                    isSortByPriceBack = false;
                }

                // Chọn tính năng sort thì huỷ tất cả các thông tin về index, vé đã chọn... trước đó
                clearSelectedByTabIndex();

                updateSort(0);
            }
        });
        v.findViewById(R.id.llPrice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (indexTab == 1) {
                    isSortByTimeGo = false;
                    isSortByPriceGo = true;
                } else if (indexTab == 2) {
                    isSortByTimeBack = false;
                    isSortByPriceBack = true;
                }

                // Chọn tính năng sort thì huỷ tất cả các thông tin về index, vé đã chọn... trước đó
                clearSelectedByTabIndex();

                updateSort(1);
            }
        });

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(v);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.width = width;
        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.show();
    }

    /*
    * Sort lại danh sách đã lấy từ server theo đk sort: 0_Sort theo time, 1_Sort theo gia
    * */
    private void updateSort(int index) {


        for (int i = 0; i < ticketInfos.size(); i++) {

            for (int j = 0; j < ticketInfos.size(); j++) {

                if (index == 0)
                    ticketInfos.get(i).isTimeCampare = true;
                else
                    ticketInfos.get(i).isTimeCampare = false;

                if (ticketInfos.get(i).compareTo(ticketInfos.get(j)) == 1) {

                    swap(ticketInfos, i, j);
                }

            }

        }

        notifyDataChanged();

        dialog.dismiss();
    }

    /*
     *  Cập nhật danh sách và scroll về vị trí đầu tiên
     * */

    private void notifyDataChanged() {

        //Cập nhật index focus trước khi refresh lại
        updateFocusByTab();

        adapter.notifyDataSetChanged();

        if (isChangeTab) {

            //Scroll tới vị trí đã chọn
            if (indexTab == 1) {

                if (application.indexChooseTicketGo != -1) {//ĐÃ chọn trước đó
                    listView.setSelection(application.indexChooseTicketGo);
                } else if (ticketInfos.size() > 0) {
                    listView.setSelection(0);
                }

            } else if (indexTab == 2) {

                if (application.indexChooseTicketBack != -1) {//ĐÃ chọn trước đó
                    listView.setSelection(application.indexChooseTicketBack);
                } else if (ticketInfos.size() > 0) {
                    listView.setSelection(0);
                }
            }

            isChangeTab = false;
        }

    }

    private void swap(List<TicketInfo> ticketInfos, int index1, int index2) {
        TicketInfo t = ticketInfos.get(index1);
        ticketInfos.set(index1, ticketInfos.get(index2));
        ticketInfos.set(index2, t);
    }

    /*Lọc theo hãng*/
    private void updateFilter(String filterID) {

        //Xoá mảng chứa dự liệu hiện
        ticketInfos.clear();

        //Đổ dữ liệu được lưu ở mạng temp với đk mã hãng vào

        for (int i = 0; i < temp.size(); i++) {
            if (String.valueOf(temp.get(i).FirmID).equals(filterID)) {
                ticketInfos.add(temp.get(i));
            }
        }

        if (indexTab == 1) {

            //Lưu lại mã của hãng vừa lọc
            filterIDWayGo = filterID;

            if (!isSortByPriceGo && !isSortByTimeGo) {

                notifyDataChanged();

            } else {
                if (isSortByTimeGo)
                    updateSort(0);

                if (isSortByPriceGo)
                    updateSort(1);
            }

        } else if (indexTab == 2) {

            //Lưu lại mã của hãng vừa lọc
            filterIDWayBack = filterID;

            if (!isSortByPriceBack && !isSortByTimeBack) {

                notifyDataChanged();

            } else {
                if (isSortByTimeBack)
                    updateSort(0);

                if (isSortByPriceBack)
                    updateSort(1);
            }

        }


    }

    /*
     * Xoá hết tất cả các giá trị đã lưu, trở về trạng thái giống như từ màn hình trước vào
     * */

    private void clearAllData() {

            /*
        * Bỏ các index đã chọn trước đó
        * */

        application.indexChooseTicketGo = -1;
        application.indexChooseTicketBack = -1;

          /*
        * Do huy focus nên vé đã chọn cũng bị huỷ luôn
        * */

        ticketWayGo = null;
        ticketWayBack = null;

        /*
        * Huỷ bỏ điều kiện sort đã chọn trước đó
        * */

        isSortByPriceGo = false;
        isSortByTimeGo = false;

        isSortByPriceBack = false;
        isSortByTimeBack = false;

        /*
        * Bỏ các ĐK lọc
        * */

        filterIDWayGo = "";
        filterIDWayBack = "";

    }


    private void clearSelectedByTabIndex() {

        if (indexTab == 1) {
               /*
                * Bỏ các index đã chọn trước đó
                * */

            application.indexChooseTicketGo = -1;

               /*
                * Do huy focus nên vé đã chọn cũng bị huỷ luôn
                * */

            ticketWayGo = null;

        } else if (indexTab == 2) {

            application.indexChooseTicketBack = -1;

            ticketWayBack = null;
        }
    }

    /*
    * Reset lại trạng thái là chưa chọn vé nào
    * */
    private void updateFocusByTab() {

        if (indexTab == 1) {
            adapter.indexClick = application.indexChooseTicketGo;
        } else if (indexTab == 2) {
            adapter.indexClick = application.indexChooseTicketBack;
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

        /*
        * Khi goi API này có nghĩa là mọi thông tin của session trước đều ko xài được nữa, cần reset lại giá trị ban đầu
        * */
        resetNewSession();

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

    /*
    * Focus vào ngày đi
    * */
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

    //   Reset mau mac dinh
    private void resetColorTab() {
        btWaygo.setBackgroundResource(R.color.colorPrimary);
        btWayback.setBackgroundResource(R.color.colorPrimary);
        btBrief.setBackgroundResource(R.color.colorPrimary);
    }

    // Focus vao tab (CHIEU DI, CHIEU VE, TOM TAT)
    private void focusColorTab() {
        resetColorTab();

        switch (indexTab) {
            case 1:// chieu di
                btWaygo.setBackgroundResource(R.color.colorPrimaryDark);
                break;

            case 2://chieu ve
                btWayback.setBackgroundResource(R.color.colorPrimaryDark);
                break;

            case 3://tom tat
                btBrief.setBackgroundResource(R.color.colorPrimaryDark);
                break;
        }
    }

    public class CustomAdapter extends BaseAdapter {

        List<TicketInfo> tickets;
        LayoutInflater inflater;
        Context context;

        /*
        * Vị trí để biết đang chọn vị trí nào
        * */
        public int indexClick = -1;

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
        public View getView(final int i, View view, ViewGroup viewGroup) {

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
            endDate = endDate.substring(6, endDate.length() - 2);
            date = Long.parseLong(endDate);
            calendar.setTimeInMillis(date);
            String s2 = String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));


            viewHolder.tvStartDate.setText(s + " - " + s2);

            viewHolder.tvAdultTotalPrice.setText(String.valueOf(ticketInfo.PriceCollection.get(0).AdultTotalPrice));

            if (i == indexClick) {
                viewHolder.rbChoose.setChecked(true);
            } else {
                viewHolder.rbChoose.setChecked(false);
            }

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

                temp.clear();
                ticketInfos.clear();

                if (null != result) {
                    if (result.status.equals("0")) {

                        for (int i = 0; i < result.data.size(); i++) {

                            TicketInfo item = result.data.get(i);

                            ticketInfos.add(item);
                            temp.add(item);

                            if (!FirmIDs.contains(String.valueOf(item.FirmID))) {
                                FirmIDs.add(String.valueOf(item.FirmID));
                                FirmImages.add(item.FirmImage);
                            }

                        }


//                        ToastUtils.toast(mContext, "Sum: " + String.valueOf(ticketInfos.size()));

//                        switch (indexTab) {
//                            case 1:
//                                loadTicketDepart();
//                                break;
//
//                            case 2:
//                                loadTicketReturn();
//                                break;
//                        }

                        tabWayGoClick();


                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    private void sendSelectedTicket() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));

        String TicketClass = "";

        if (ticketWayGo.PriceCollection.size() != 0)
            TicketClass = ticketWayGo.PriceCollection.get(0).TicketClass;
        params.put("ticket_depart", String.format("%s_%s", ticketWayGo.TicketID, TicketClass));

        if (isOneWay)
            params.put("ticket_return", "");
        else {

            if (ticketWayBack.PriceCollection.size() != 0)
                TicketClass = ticketWayBack.PriceCollection.get(0).TicketClass;
            params.put("ticket_return", String.format("%s_%s", ticketWayBack.TicketID, TicketClass));
        }

        AppRequest.sendSelectedTicket(this, params, true, new DataRequestCallback<SVResponseObj>() {
            @Override
            public void onResult(SVResponseObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {

                        //Next qua man hinh Customer
                        startActivity(new Intent(mContext, CustommerInfoActivity.class));

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    public class CustomBrankAdapter extends BaseAdapter {

        private List<String> FirmIDs;
        private List<String> FirmImages;
        private LayoutInflater inflater;
        private Context context;

        public CustomBrankAdapter(Context c, List<String> FirmIDs, List<String> FirmImages) {
            this.FirmIDs = FirmIDs;
            this.FirmImages = FirmImages;
            this.inflater = LayoutInflater.from(c);
            this.context = c;
        }

        @Override
        public int getCount() {
            return FirmIDs.size();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolderBrank viewHolderBrank;

            if (null == view) {
                view = this.inflater.inflate(R.layout.layout_filter_row_item, viewGroup, false);

                viewHolderBrank = new ViewHolderBrank();
                viewHolderBrank.imageView = (ImageView) view.findViewById(R.id.imageView);

                view.setTag(viewHolderBrank);
            } else {
                viewHolderBrank = (ViewHolderBrank) view.getTag();
            }

            ImageUtils.load(this.context, viewHolderBrank.imageView, FirmImages.get(i));

            return view;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }
    }

    public class ViewHolderBrank {
        public ImageView imageView;
    }

}
