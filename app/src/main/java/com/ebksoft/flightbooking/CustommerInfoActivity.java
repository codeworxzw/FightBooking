package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
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

    private List<String> lstBagPriceWays, lstBagPriceWaysBack;
    private ArrayAdapter<String> dataAdapterWays;
    private ArrayAdapter<String> dataAdapterWaysBack;

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

        lstBagPriceWays = new ArrayList<>();
        lstBagPriceWaysBack = new ArrayList<>();

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
                                lstBagPriceWays.add(s);
                            } else {
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
            dataAdapterWaysBack = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstBagPriceWaysBack);
            dataAdapterWaysBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerBagBack.setAdapter(dataAdapterWaysBack);

            /*
            * Spinner giới tính
            * */
            List<String> dataGender = new ArrayList<>();
            dataGender.add("Nam");
            dataGender.add("Nữ");

            ArrayAdapter<String> adapterGenner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataGender);
            adapterGenner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGender.setAdapter(adapterGenner);


            llContentPassenger.addView(view);
        }

        //Scroll lên đầu trang

        scrollView.pageScroll(View.FOCUS_UP);

        llPersonnalInfo.setVisibility(View.VISIBLE);

        scrollView.setVisibility(View.VISIBLE);
    }

    /*
    * Gui thong tin Passenger lên server
    * */
    private void addPassenger() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("session_key", SharedpreferencesUtils.getInstance(this).read("session_key"));

        Booking booking = new Booking();
        String name = etContactName.getText().toString();
        int indexFirstSpace = name.indexOf(" ");

        booking.ContactFirstName = name.substring(0, indexFirstSpace);
        booking.ContactLastName = name.substring(indexFirstSpace, name.length() - 1);
        booking.ContactAddress = etAddress.getText().toString();
        booking.ContactEmail = etEmail.getText().toString();
        booking.ContactPhone = etPhone.getText().toString();

        try {
            String jsonString = new Gson().toJson(booking);
            JSONObject jsonObject = new JSONObject(jsonString);
            params.put("booking", jsonObject);
        } catch (Exception ex) {

        }

        //
        PassengerModel passenger = new PassengerModel();
        passenger.FirstName = "Duc";
        passenger.LastName = "Ngo Dinh Minh";
        passenger.PassengerType = 1;

        long bt = System.currentTimeMillis();
        String birthDay = "/Date(" + bt + ")/";
        passenger.Birthday = birthDay;
        passenger.DepartBagID = 213;
        passenger.DepartBagValue = 15;
        passenger.ReturnBagID = 214;
        passenger.ReturnBagValue = 20;

        passenger.Sex = "M";

        JSONArray jsonArray = new JSONArray();

        try {
            String jsonPassenger = new Gson().toJson(passenger);
            JSONObject jsonObject = new JSONObject(jsonPassenger);

            jsonArray.put(jsonObject);
        } catch (Exception ex) {

        }

        params.put("passengers", jsonArray);


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
