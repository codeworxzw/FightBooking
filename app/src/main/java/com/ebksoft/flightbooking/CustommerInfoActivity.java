package com.ebksoft.flightbooking;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ebksoft.flightbooking.model.BagModel;
import com.ebksoft.flightbooking.model.ResponseObj.GetBagResObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chauminhnhut on 2/1/16.
 */
public class CustommerInfoActivity extends BaseActivity {

    private List<String> lstBagPriceWays, lstBagPriceWaysBack;
    private  ArrayAdapter<String> dataAdapterWays;
    private  ArrayAdapter<String> dataAdapterWaysBack;

    private Spinner spinnerBagGo,spinnerBagBack;

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

        spinnerBagGo = (Spinner)findViewById(R.id.spinnerBagGo);
        spinnerBagBack = (Spinner)findViewById(R.id.spinnerBagBack);

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
                        for (int i=0;i<result.data.size();i++){

                            BagModel b = result.data.get(i);

                            s = String.format("%dKg - %d VND", b.BagValue, b.BagPrice);

                            if(b.BagForWays==1){
                                lstBagPriceWays.add(s);
                            }else{
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

}
