package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.ebksoft.flightbooking.model.BagModel;
import com.ebksoft.flightbooking.model.PassengerModel;
import com.ebksoft.flightbooking.model.ResponseObj.GetBagResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SVResponseObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauminhnhut on 2/1/16.
 */
public class CustommerInfoActivity extends BaseActivity implements View.OnClickListener {


    private EditText etContactName, etEmail, etPhone, etAddress;

    /*
    * Chứa 2 danh sách thông tin giỏ hàng (đi và về)
    *
    * @Note: vì danh sách trên spinner chỉ để xem, và thông qua index trong spinner để lấy thông tin gửi lên server
    * */

    private List<BagModel> lstBagModelGo, lstBagModelBack;

    /*
    * Chứa các chuỗi hiển thị trên Spinner hàng hoá
    * */
    private List<String> lstBagPriceWays, lstBagPriceWaysBack;

    private ArrayAdapter<String> dataAdapterWays;
    private ArrayAdapter<String> dataAdapterWaysBack;
    private ArrayAdapter<String> adapterGenner;

    private ScrollView scrollView;
    private LinearLayout llContentPassenger;

    private View llPersonnalInfo;

    private AppApplication application;

     /*
    * Số lượng Passenger
    * */

    private int countAdult = 0, countChild = 0, countIndent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custommer_info_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        llContentPassenger = (LinearLayout) findViewById(R.id.llContentPassenger);

        llPersonnalInfo = findViewById(R.id.llPersonnalInfo);

        initTitle(getString(R.string.custommer_info));
        initButtonBack();

        //Thong tin lien he
        etContactName = (EditText) findViewById(R.id.etContactName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);

        findViewById(R.id.btFinish).setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        application = AppApplication.getInstance();

        countAdult = application.countAdult;
        countChild = application.countChild;
        countIndent = application.countIndent;


        lstBagModelGo = new ArrayList<>();
        lstBagModelBack = new ArrayList<>();

        lstBagPriceWays = new ArrayList<>();
        lstBagPriceWaysBack = new ArrayList<>();

        List<String> dataGender = new ArrayList<>();
        dataGender.add("Nam");
        dataGender.add("Nữ");

        adapterGenner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataGender);
        adapterGenner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getBag();

    }

    private void getBag() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));


        AppRequest.getBag(this, params, true, new DataRequestCallback<GetBagResObj>() {
            @Override
            public void onResult(GetBagResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {

                        String s = "";
                        for (int i = 0; i < result.data.size(); i++) {

                            BagModel b = result.data.get(i);

                            s = String.format("%dKg - %d VND", b.BagValue, b.BagPrice);

                            if (b.BagForWays == 1) {
                                lstBagModelGo.add(b);
                                lstBagPriceWays.add(s);
                            } else {
                                lstBagModelBack.add(b);
                                lstBagPriceWaysBack.add(s);
                            }

                        }

                        addPassengerForm();

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }


    private void addPassengerForm() {

        int count = countAdult + countChild + countIndent;
        LayoutInflater inflater = getLayoutInflater();

        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.layout_passenger_info, null);

            Spinner spinnerBagGo, spinnerBagBack, spinnerGender;

            spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
            spinnerBagGo = (Spinner) view.findViewById(R.id.spinnerBagGo);
            spinnerBagBack = (Spinner) view.findViewById(R.id.spinnerBagBack);

            /*
            * Spinner bag go
            * */
            dataAdapterWays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstBagPriceWays);
            dataAdapterWays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBagGo.setAdapter(dataAdapterWays);



            /*
            * Spinner bag back
            * */

            View viewBagBack = view.findViewById(R.id.viewBagBack);

            if (application.isOneWay) {
                /*
                * Nếu 1 chiều thì ẩn đi Bag back
                * */
                viewBagBack.setVisibility(View.GONE);
            } else {

                dataAdapterWaysBack = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstBagPriceWaysBack);
                dataAdapterWaysBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBagBack.setAdapter(dataAdapterWaysBack);
            }


            /*
            * Spinner giới tính
            * */
            spinnerGender.setAdapter(adapterGenner);


            llContentPassenger.addView(view);
        }

        //Scroll lên đầu trang

        scrollView.pageScroll(View.FOCUS_UP);

        llPersonnalInfo.setVisibility(View.VISIBLE);

        scrollView.setVisibility(View.VISIBLE);
    }

    private JSONArray getPassengerInfoFromForm() {

        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < llContentPassenger.getChildCount(); i++) {

            View view = llContentPassenger.getChildAt(i);
            EditText etName = (EditText) view.findViewById(R.id.etPassengerName);
            String passsengerName = etName.getText().toString();

            if (TextUtils.isEmpty(passsengerName)) {
                CommonUtils.showToast(this, getString(R.string.please_enter_full_name_passenger));
                return null;
            }

            /*
            * Nhận biết first name qua dấu khoảng trắng đầu tiên
            * */

            int indexBlank = passsengerName.indexOf(" ");

            String firstName = "", lastName = "";
            if (indexBlank != -1) {
                firstName = passsengerName.substring(0, indexBlank);
                lastName = passsengerName.substring(indexBlank + 1, passsengerName.length());
            } else {
                firstName = passsengerName;
            }

            PassengerModel passenger = new PassengerModel();
            passenger.FirstName = firstName;
            passenger.LastName = lastName;

            /*
            * Do việc add theo thứ tự nên ta có thể sếp loại theo index
            * */

            if (i < countAdult) {
                passenger.PassengerType = 1;//adult
            } else if (i < countAdult + countChild) {
                passenger.PassengerType = 2;//child
            } else {
                passenger.PassengerType = 3;//indent
            }


            /*
            * Birthday: để mặc định, server tự xử lý
            * */

            long bt = System.currentTimeMillis();
            String birthDay = "/Date(" + bt + ")/";
            passenger.Birthday = birthDay;


            /*
            * Lấy thông tin Kg giỏ sách (hàng hoá)
            * */

            Spinner spinnerBagGo = (Spinner) view.findViewById(R.id.spinnerBagGo);
            int indexGo = spinnerBagGo.getSelectedItemPosition();
            BagModel b = lstBagModelGo.get(indexGo);
            passenger.DepartBagID = b.BagID;
            passenger.DepartBagValue = b.BagValue;


            if (application.isOneWay) {

                /*
                * 1 chiều nên ko có thông tin về
                * */

                passenger.ReturnBagID = 0;
                passenger.ReturnBagValue = 0;

            } else {

                 /*
                 * Lấy thông tin Kg giỏ sách (hàng hoá) về
                 * */
                
                Spinner spinnerBagBack = (Spinner) view.findViewById(R.id.spinnerBagBack);
                int indexBack = spinnerBagBack.getSelectedItemPosition();
                BagModel bb = lstBagModelBack.get(indexBack);
                passenger.ReturnBagID = bb.BagID;
                passenger.ReturnBagValue = bb.BagValue;
            }




            /*
            * Thông tin giới tín đc lấy từ spinner
            * */
            Spinner spinnerGender = (Spinner) view.findViewById(R.id.spinnerGender);
            if (spinnerGender.getSelectedItemPosition() == 0)
                passenger.Sex = "M";
            else
                passenger.Sex = "F";
            try {
                String jsonPassenger = new Gson().toJson(passenger);
                JSONObject jsonObject = new JSONObject(jsonPassenger);

                jsonArray.put(jsonObject);
            } catch (Exception ex) {
                Log.e("", ex.getMessage());
            }
        }

        return jsonArray;
    }

    /*
    * Gui thong tin Passenger lên server
    * */
    private void addPassenger() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));

        JSONArray jsonArray = getPassengerInfoFromForm();
        if (jsonArray == null) {
            CommonUtils.closeProgressDialog();
            return;
        }
        params.put("passengers", jsonArray);


        //Check DK nguoi lien he
        if (!checkContactInfo()) {
            CommonUtils.closeProgressDialog();
            return;
        }

        Booking booking = new Booking();
        String name = etContactName.getText().toString();
        int indexBlank = name.indexOf(" ");

        String firstName = "", lastName = "";
        if (indexBlank != -1) {
            firstName = name.substring(0, indexBlank);
            lastName = name.substring(indexBlank + 1, name.length());
        } else {
            firstName = name;
        }

        booking.ContactFirstName = firstName;
        booking.ContactLastName = lastName;
        booking.ContactAddress = etAddress.getText().toString();
        booking.ContactEmail = etEmail.getText().toString();
        booking.ContactPhone = etPhone.getText().toString();


        try {
            String jsonString = new Gson().toJson(booking);
            JSONObject jsonObject = new JSONObject(jsonString);
            params.put("booking", jsonObject);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
            return;
        }

        AppRequest.addPassenger(this, params, true, new DataRequestCallback<SVResponseObj>() {
            @Override
            public void onResult(SVResponseObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {

                        sendBooking();

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

    private boolean checkContactInfo() {

        String name = etContactName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            CommonUtils.showToast(this, getString(R.string.please_enter_full_name_contact_guy));
            return false;
        }

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            CommonUtils.showToast(this, getString(R.string.please_enter_email_contact_guy));
            return false;
        }

        if (!CommonUtils.isEmailValid(email)) {
            CommonUtils.showToast(this, getString(R.string.email_wrong_format));
            return false;
        }

        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            CommonUtils.showToast(this, getString(R.string.please_enter_phone_contact_guy));
            return false;
        }

        if (!CommonUtils.isPhoneValid(phone)) {
            CommonUtils.showToast(this, getString(R.string.phone_wrong_format));
            return false;
        }

        String address = etAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            CommonUtils.showToast(this, getString(R.string.please_enter_address_contact_guy));
            return false;
        }
        return true;
    }

    private void sendBooking() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));


        AppRequest.sendBooking(this, params, true, new DataRequestCallback<SVResponseObj>() {
            @Override
            public void onResult(SVResponseObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {

                        startActivity(new Intent(mContext, ConfirmTicketInfo.class));

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
        if (view.getId() == R.id.btFinish) {
            addPassenger();
        }
    }

    private class Booking {
        public int BookingID;
        public String ContactFirstName;
        public String ContactLastName;
        public String ContactPhone;
        public String ContactAddress;
        public String ContactEmail;
        public boolean IsVAT;
        public String VatCompanyName;
        public String VatCompanyNo;
        public String VatCompanyAddress;

        public int BookingStatus;
        public String PNR;
        public String FlightInfo;
        public int TotalPrice;
        public int GrossPrice;
        public String HoldToDate;
        public String City;
        public String Country;
        public String Province;
        public String Meassage;
    }
}
