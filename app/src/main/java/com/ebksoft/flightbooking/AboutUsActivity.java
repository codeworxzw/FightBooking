package com.ebksoft.flightbooking;

import android.os.Bundle;
import android.webkit.WebView;

import com.ebksoft.flightbooking.model.ResponseObj.GetAboutUsResObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;

import java.util.HashMap;

/**
 * Created by chauminhnhut on 3/29/16.
 */
public class AboutUsActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_us_activity);

        loadView();

        loadData();
    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_about_us));
        initButtonBack();

        webView = (WebView) findViewById(R.id.webView);

    }

    @Override
    protected void loadData() {

        if (CommonUtils.isNetWorkAvailable(this))
            getAbout();
        else
            CommonUtils.showToastNoInternetConnecton(this);
    }

    private void getAbout() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);

        AppRequest.getAbout(this, params, true, new DataRequestCallback<GetAboutUsResObj>() {
            @Override
            public void onResult(GetAboutUsResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();


                if (null != result) {
                    if (result.status.equals("0")) {

                        webView.loadData(result.data, "text/html; charset=UTF-8", null);

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
