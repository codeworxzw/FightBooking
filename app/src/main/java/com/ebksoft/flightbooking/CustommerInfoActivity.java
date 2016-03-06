package com.ebksoft.flightbooking;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ebksoft.flightbooking.model.BagModel;
import com.ebksoft.flightbooking.model.PassengerModel;
import com.ebksoft.flightbooking.model.ResponseObj.GetBagResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SVResponseObj;
import com.ebksoft.flightbooking.network.AppRequest;
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

    private Spinner spinnerBagGo, spinnerBagBack, spinnerGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custommer_info_activity);

        loadView();
        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.custommer_info));
        initButtonBack();

        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        spinnerBagGo = (Spinner) findViewById(R.id.spinnerBagGo);
        spinnerBagBack = (Spinner) findViewById(R.id.spinnerBagBack);

        etContactName = (EditText) findViewById(R.id.etContactName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);


        findViewById(R.id.btFinish).setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        lstBagPriceWays = new ArrayList<>();
        lstBagPriceWaysBack = new ArrayList<>();

        dataAdapterWays = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstBagPriceWays);
        dataAdapterWaysBack = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lstBagPriceWaysBack);

        dataAdapterWays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapterWaysBack.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerBagGo.setAdapter(dataAdapterWays);
        spinnerBagBack.setAdapter(dataAdapterWaysBack);


        List<String> dataGender = new ArrayList<>();
        dataGender.add("Nam");
        dataGender.add("Nữ");

        ArrayAdapter<String> adapterGenner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataGender);
        adapterGenner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGenner);

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

                        dataAdapterWays.notifyDataSetChanged();
                        dataAdapterWaysBack.notifyDataSetChanged();

                    } else {
                        CommonUtils.showToast(mContext, result.message);
                    }

                } else {
                    CommonUtils.showToast(mContext, getString(R.string.connection_timeout));
                }

            }
        });
    }

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
        }catch (Exception ex){

        }

        //
        PassengerModel passenger = new PassengerModel();
        passenger.FirstName = "Duc";
        passenger.LastName = "Ngo Dinh Minh";
        passenger.PassengerType = 1;

        long bt = System.currentTimeMillis();
        String birthDay  = "/Date(" + bt + ")/";
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
        }catch (Exception ex){

        }

        params.put("passengers", jsonArray);


        AppRequest.addPassenger(this, params, true, new DataRequestCallback<SVResponseObj>() {
            @Override
            public void onResult(SVResponseObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();

                if (null != result) {
                    if (result.status.equals("0")) {



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

    private class Booking{
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
