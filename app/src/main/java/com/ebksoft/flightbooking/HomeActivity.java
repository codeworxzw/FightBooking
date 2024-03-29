package com.ebksoft.flightbooking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebksoft.flightbooking.model.HistorySearchTrip;
import com.ebksoft.flightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SearchResObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static final int PLACE_FROM_CODE = 1000;
    public static final int PLACE_TO_CODE = 1001;

    public static final int TIME_GO_CODE = 1002;
    public static final int TIME_BACK_CODE = 1003;


    private TextView txtTitle;
    private Button btOneWay, btRoundTrip;

    private View rlTimeToParent;

    private Button btDownAdult, btUpAdult, btDownChild, btUpChild, btDownIndent, btUpIndent;
    private TextView tvAdult, tvChild, tvIndent;

    private boolean isNormalType = true;
    private Button btNext, btPrev;
    private TextView tvTicketType;

    private TextView tvPlaceFromCode, tvPlaceFromName, tvPlaceToCode, tvPlaceToName;

    private TextView tvDateGo, tvMonthYearGo, tvDayGo;
    private TextView tvDateTo, tvMonthYearTo, tvDayTo;

    private DrawerLayout drawer;

    /*
    Hình ảnh thể hiện nếu là 2 chiều
     */
    private ImageView imgFightBack;

    /*
    Biến xác định là đang chọn 1 chiều hay 2 chiều
     */
    private boolean isOneWay = true;

    /*
    Thời gian đi và về hiện tại đang được chon, tính bằng giây
     */
    private long ltimeGo, lTimeBack;

    /*
    Các giá trị gửi lên server
     */
    private String session_key = "";
    private String FromCityCode = "", ToCityCode = "", DepartDate = "", ReturnDate = "";
    private int countAdult = 1, countChild = 0, countIndent = 0;

    private AppApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadView();

        loadData();
    }

    private void loadView() {
        initNav();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        txtTitle = (TextView) findViewById(R.id.txtTitle);

        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        Button button = (Button) findViewById(R.id.imgMenu);
        button.setTypeface(font);
        button.setOnClickListener(this);

        findViewById(R.id.rlPlaceFrom).setOnClickListener(this);
        findViewById(R.id.rlPlaceTo).setOnClickListener(this);
        findViewById(R.id.imgSearchFight).setOnClickListener(this);
        findViewById(R.id.rlTimeGo).setOnClickListener(this);
        findViewById(R.id.rlTimeTo).setOnClickListener(this);

        btOneWay = (Button) findViewById(R.id.btOneWay);
        btRoundTrip = (Button) findViewById(R.id.btRoundTrip);
        rlTimeToParent = findViewById(R.id.rlTimeToParent);

        btOneWay.setOnClickListener(this);
        btRoundTrip.setOnClickListener(this);

        btDownAdult = (Button) findViewById(R.id.btDownAdult);
        btUpAdult = (Button) findViewById(R.id.btUpAdult);
        btDownChild = (Button) findViewById(R.id.btDownChild);
        btUpChild = (Button) findViewById(R.id.btUpChild);
        btDownIndent = (Button) findViewById(R.id.btDownIndent);
        btUpIndent = (Button) findViewById(R.id.btUpIndent);

        tvAdult = (TextView) findViewById(R.id.tvAdult);
        tvChild = (TextView) findViewById(R.id.tvChild);
        tvIndent = (TextView) findViewById(R.id.tvIndent);

        btDownAdult.setOnClickListener(this);
        btUpAdult.setOnClickListener(this);
        btDownChild.setOnClickListener(this);
        btUpChild.setOnClickListener(this);
        btDownIndent.setOnClickListener(this);
        btUpIndent.setOnClickListener(this);

/*Code for choose type of chair*/
//        btNext = (Button) findViewById(R.id.btNext);
//        btPrev = (Button) findViewById(R.id.btPrev);
//        btNext.setOnClickListener(this);
//        btPrev.setOnClickListener(this);

        tvTicketType = (TextView) findViewById(R.id.tvTicketType);

        tvPlaceFromCode = (TextView) findViewById(R.id.tvPlaceFromCode);
        tvPlaceFromName = (TextView) findViewById(R.id.tvPlaceFromName);
        tvPlaceToCode = (TextView) findViewById(R.id.tvPlaceToCode);
        tvPlaceToName = (TextView) findViewById(R.id.tvPlaceToName);

        tvDateGo = (TextView) findViewById(R.id.tvDateGo);
        tvMonthYearGo = (TextView) findViewById(R.id.tvMonthYearGo);
        tvDayGo = (TextView) findViewById(R.id.tvDayGo);
        tvDateTo = (TextView) findViewById(R.id.tvDateTo);
        tvMonthYearTo = (TextView) findViewById(R.id.tvMonthYearTo);
        tvDayTo = (TextView) findViewById(R.id.tvDayTo);

        imgFightBack = (ImageView) findViewById(R.id.imgFightBack);

        //Click on menu
        findViewById(R.id.imvHotlineBooking).setOnClickListener(this);
        findViewById(R.id.imvLike).setOnClickListener(this);
        findViewById(R.id.imvManageTicket).setOnClickListener(this);
        findViewById(R.id.imvBooking).setOnClickListener(this);
        findViewById(R.id.imvNews).setOnClickListener(this);
        findViewById(R.id.imvAboutUs).setOnClickListener(this);
    }

    private void loadData() {
        // Title
        txtTitle.setText(getString(R.string.search_fight).toUpperCase(Locale.getDefault()));

        loadDefaultData();
    }

    private void loadDefaultData() {

        //Place to go
        tvPlaceFromCode.setText("SGN");
        tvPlaceFromName.setText("Hồ Chí Minh");

        tvPlaceToCode.setText("HAN");
        tvPlaceToName.setText("Hà Nội");

        FromCityCode = "SGN";
        ToCityCode = "HAN";

        loadGoTime(Calendar.getInstance());
    }

    private void loadGoTime(Calendar calendar) {
        ltimeGo = calendar.getTimeInMillis();
        DepartDate = String.valueOf(ltimeGo);

        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String monthYear = CommonUtils.getMonthYearString(month, year);
        String strDay = CommonUtils.getDayString(day);

        tvDateGo.setText(String.valueOf(date));
        tvMonthYearGo.setText(monthYear);
        tvDayGo.setText(strDay);
    }

    private void loadGoBackTime(Calendar calendar) {
        lTimeBack = calendar.getTimeInMillis();
        ReturnDate = String.valueOf(lTimeBack);

        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String monthYear = CommonUtils.getMonthYearString(month, year);
        String strDay = CommonUtils.getDayString(day);

        tvDateTo.setText(String.valueOf(date));
        tvMonthYearTo.setText(monthYear);
        tvDayTo.setText(strDay);

    }

    @Override
    public void onClick(View view) {

        Intent i = null;
        switch (view.getId()) {
            case R.id.imgMenu:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.rlPlaceFrom:
                i = new Intent(this, ChooseAirportActivity.class);
                startActivityForResult(i, PLACE_FROM_CODE);
                break;

            case R.id.rlPlaceTo:
                i = new Intent(this, ChooseAirportActivity.class);
                startActivityForResult(i, PLACE_TO_CODE);
                break;

            case R.id.imgSearchFight:

                if (CommonUtils.isNetWorkAvailable(this))
                    reqInitAPI();
                else
                    CommonUtils.showToastNoInternetConnecton(this);


                break;

            case R.id.rlTimeGo:
                i = new Intent(this, ChooseTimeActivity.class);
                i.putExtra("isRoundTrip", false);// Chieu di
                i.putExtra("ReturnDate", ReturnDate);// Ngay ve
                i.putExtra("isOneWay", isOneWay);
                startActivityForResult(i, TIME_GO_CODE);
                break;

            case R.id.rlTimeTo:
                i = new Intent(this, ChooseTimeActivity.class);
                i.putExtra("isRoundTrip", true);// Chieu ve
                i.putExtra("DepartDate", DepartDate);// Ngay di
                i.putExtra("isOneWay", isOneWay);
                startActivityForResult(i, TIME_BACK_CODE);
                break;

            case R.id.btOneWay:
                isOneWay = true;
                rlTimeToParent.setVisibility(View.INVISIBLE);
                imgFightBack.setVisibility(View.GONE);

                btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                break;

            case R.id.btRoundTrip:
                isOneWay = false;
                rlTimeToParent.setVisibility(View.VISIBLE);
                imgFightBack.setVisibility(View.VISIBLE);

                btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

                /*
                Load ngay ve, mac dinh sau 2 ngay so voi ngay di
                 */
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ltimeGo + 2 * 86400 * 1000);

                loadGoBackTime(calendar);
                break;

            case R.id.btDownAdult:
                if (countAdult != 1)
                    countAdult--;
                updateNumPassenger();
                break;

            case R.id.btUpAdult:
                if (canCountUp() && countAdult < 9)
                    countAdult++;
                updateNumPassenger();
                break;

            case R.id.btDownChild:
                if (countChild != 0)
                    countChild--;
                updateNumPassenger();
                break;

            case R.id.btUpChild:
                if (canCountUp() && countChild < 4)
                    countChild++;
                updateNumPassenger();
                break;

            case R.id.btDownIndent:
                if (countIndent != 0)
                    countIndent--;
                updateNumPassenger();
                break;

            case R.id.btUpIndent:
                if (canCountUp() && countIndent < 4)
                    countIndent++;
                updateNumPassenger();
                break;

            case R.id.btPrev:
            case R.id.btNext:

                isNormalType = !isNormalType;
                updateTicketType();
                break;

            //Click on menu

            case R.id.imvHotlineBooking:
                startActivity(new Intent(this, HotlineBookingActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.imvLike:
                startActivity(new Intent(this, GetPromotionActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.imvManageTicket:
                startActivity(new Intent(this, ManageBookingTicket.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.imvBooking:
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.imvNews:
                startActivity(new Intent(this, ListNewsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.imvAboutUs:
                startActivity(new Intent(this, AboutUsActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

    private boolean canCountUp() {

        if (countAdult + countChild + countIndent < 9)
            return true;
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.US);
        String dayOfWeek = "";
        String month = "";
        long date = 0;

        Calendar calendar = Calendar.getInstance();

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PLACE_FROM_CODE:
                    FromCityCode = data.getExtras().getString("code");
                    String nameFrom = data.getExtras().getString("name");

                    tvPlaceFromCode.setText(FromCityCode);
                    tvPlaceFromName.setText(nameFrom);

                    break;

                case PLACE_TO_CODE:
                    ToCityCode = data.getExtras().getString("code");
                    String nameTo = data.getExtras().getString("name");

                    tvPlaceToCode.setText(ToCityCode);
                    tvPlaceToName.setText(nameTo);
                    break;

                case TIME_GO_CODE:
                    DepartDate = data.getExtras().getString("data");
                    date = Long.parseLong(DepartDate);
                    calendar.setTimeInMillis(date);

                    loadGoTime(calendar);
                    break;

                case TIME_BACK_CODE:
                    ReturnDate = data.getExtras().getString("data");
                    date = Long.parseLong(ReturnDate);
                    calendar.setTimeInMillis(date);

                    loadGoBackTime(calendar);
                    break;
            }
        }
    }

    private void updateNumPassenger() {
        tvAdult.setText(String.valueOf(countAdult));
        tvChild.setText(String.valueOf(countChild));
        tvIndent.setText(String.valueOf(countIndent));
    }

    private void updateTicketType() {
        if (isNormalType) {
            tvTicketType.setText(getString(R.string.ticket_type_normal));
        } else {
            tvTicketType.setText(getString(R.string.ticket_type_vip));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        //Khi quay về màn hình này, cần reset tất cả dữ liệu lưu trữ trước đó
        AppApplication.getInstance(this).resetData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        /*
        * Do có 2 màn hình gọi về hàm này: MH Quản lý vé, và MH Confirm xong quá trình đặt xé, chỉ gọi load lại khi click từ màn hình Quản lý vé
        * */
        if (intent.getExtras() != null)
            loadHistoryTrip(intent);
    }

    private void loadHistoryTrip(Intent intent) {

        if (intent != null) {
            int index = intent.getExtras().getInt("index");
            HistorySearchTrip historySearchTrip = AppApplication.getInstance(this).historySearchTrips.get(index);

            if (null != historySearchTrip) {


                //Place From
                FromCityCode = historySearchTrip.FromCityCode;
                String nameFrom = historySearchTrip.FromCityName;

                tvPlaceFromCode.setText(FromCityCode);
                tvPlaceFromName.setText(nameFrom);

                //Place To
                ToCityCode = historySearchTrip.ToCityCode;
                String nameTo = historySearchTrip.ToCityName;

                tvPlaceToCode.setText(ToCityCode);
                tvPlaceToName.setText(nameTo);


                isOneWay = historySearchTrip.isOneWay;

                loadGoTime(historySearchTrip.calendarGo);

                if (!historySearchTrip.isOneWay) {

                    rlTimeToParent.setVisibility(View.VISIBLE);
                    imgFightBack.setVisibility(View.VISIBLE);

                    btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

                    loadGoBackTime(historySearchTrip.calendarBack);
                } else {

                    rlTimeToParent.setVisibility(View.INVISIBLE);
                    imgFightBack.setVisibility(View.GONE);

                    btOneWay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                    btRoundTrip.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

                }

                countAdult = historySearchTrip.countAdult;
                countIndent = historySearchTrip.countIdent;
                countChild = historySearchTrip.countChild;
                updateNumPassenger();

            }
        }
    }

    private void initNav() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
    * Add search trip for history
    * */
    private void addSearchTrip() {

        HistorySearchTrip searchTrip = new HistorySearchTrip();

        String s = String.format("%s (%s)", tvPlaceFromName.getText().toString(), FromCityCode);
        searchTrip.From = s;

        s = String.format("%s (%s)", tvPlaceToName.getText().toString(), ToCityCode);
        searchTrip.To = s;

        if (isOneWay) {
            s = "1 chiều\n";
        } else {
            s = "2 chiều\n";
        }

        s += String.format("%s - %s, %s %s",
                tvPlaceFromName.getText().toString(),
                tvDayGo.getText().toString(),
                tvDateGo.getText().toString(),
                tvMonthYearGo.getText().toString());

        if (!isOneWay) {
            s += "\n";
            s += String.format("%s - %s, %s %s",
                    tvPlaceToName.getText().toString(),
                    tvDayTo.getText().toString(),
                    tvDateTo.getText().toString(),
                    tvMonthYearTo.getText().toString());
        }

        searchTrip.WayTime = s;

        s = String.format("%d Người lớn", countAdult);

        if (countChild != 0) {
            s += String.format(", %d Trẻ em", countChild);
        }

        if (countIndent != 0) {
            s += String.format(", %d Sơ sinh", countIndent);
        }

        searchTrip.Passenger = s;

        searchTrip.FromCityCode = FromCityCode;
        searchTrip.FromCityName = tvPlaceFromName.getText().toString();

        searchTrip.ToCityCode = ToCityCode;
        searchTrip.ToCityName = tvPlaceToName.getText().toString();

        searchTrip.calendarGo = Calendar.getInstance();
        searchTrip.calendarGo.setTimeInMillis(ltimeGo);

        searchTrip.calendarBack = Calendar.getInstance();
        searchTrip.calendarBack.setTimeInMillis(lTimeBack);

        searchTrip.countAdult = countAdult;
        searchTrip.countChild = countChild;
        searchTrip.countIdent = countIndent;

        searchTrip.isOneWay = isOneWay;

        CommonUtils.addHistorySearchTrip(this, searchTrip);
    }

    /*
    * Send request to server to get data
    *
    * */

    private void reqInitAPI() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);
        params.put("ip_address", CommonUtils.getIPAddress(this));

        AppRequest.reqInitAPI(this, params, true, new DataRequestCallback<InitResObj>() {
            @Override
            public void onResult(InitResObj result, boolean continueWaiting) {

                if (null != result) {
                    if (result.status.equals("0")) {
                        session_key = result.data.session_key;
                        SharedpreferencesUtils.getInstance(HomeActivity.this).save("session_key", session_key);

                        searchFight();
                    } else {
                        session_key = "";
                        CommonUtils.showToast(HomeActivity.this, result.message);
                    }

                } else {
                    session_key = "";
                }


            }
        });
    }

    private void searchFight() {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", session_key);

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("FromCityCode", FromCityCode);
            jsonObject.put("ToCityCode", ToCityCode);
            jsonObject.put("DepartDate", "/Date(" + DepartDate + ")/");

            if (ReturnDate.equals("")) {
                jsonObject.put("ReturnDate", ReturnDate);
            } else {
                jsonObject.put("ReturnDate", "/Date(" + ReturnDate + ")/");
            }

            jsonObject.put("AdultNum", countAdult);
            jsonObject.put("ChildNum", countChild);
            jsonObject.put("InfantNum", countIndent);

            params.put("search_info", jsonObject);
        } catch (Exception ex) {
            Log.e("", "SeerchFight Error");
        }

        app = AppApplication.getInstance(this);
        app.countAdult = countAdult;
        app.countChild = countChild;
        app.countIndent = countIndent;

        app.FromCityCode = FromCityCode;
        app.ToCityCode = ToCityCode;

        AppRequest.searchFight(this, params, true, new DataRequestCallback<SearchResObj>() {
            @Override
            public void onResult(SearchResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();
                if (null != result) {
                    if (result.status.equals("0")) {

                        //Cache this search for history

                        addSearchTrip();

                        //End

                        Intent intent = new Intent(HomeActivity.this, SearchFightResultActivity.class);
                        intent.putExtra("place_from", tvPlaceFromName.getText().toString());
                        intent.putExtra("place_to", tvPlaceToName.getText().toString());

                        intent.putExtra("time_go", DepartDate);

                        if (!isOneWay)
                            intent.putExtra("time_back", ReturnDate);

                        app.isOneWay = isOneWay;

                        startActivity(intent);

                    } else {
                        CommonUtils.showToast(HomeActivity.this, result.message);
                    }

                } else {

                }


            }
        });

    }


}
