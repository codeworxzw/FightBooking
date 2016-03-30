package com.ebksoft.flightbooking;

import android.os.Bundle;
import android.webkit.WebView;

import com.ebksoft.flightbooking.model.ResponseObj.DetailNewsResObj;
import com.ebksoft.flightbooking.network.AppRequest;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;

import java.util.HashMap;

/**
 * Created by chauminhnhut on 3/28/16.
 */
public class DetailNewsActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail_news_activity);


        loadView();

        loadData();


    }

    @Override
    protected void loadView() {

        initTitle(getString(R.string.title_list_news));
        initButtonBack();

        webView = (WebView) findViewById(R.id.webView);
    }

    @Override
    protected void loadData() {

        getListNewsDetail();
    }

    private void getListNewsDetail() {

        CommonUtils.showProgressDialog(this);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("authen_key", ConfigAPI.AUTHEN_KEY);
        params.put("id", getIntent().getExtras().getString("ID"));

        AppRequest.getListNewsDetail(this, params, true, new DataRequestCallback<DetailNewsResObj>() {
            @Override
            public void onResult(DetailNewsResObj result, boolean continueWaiting) {
                CommonUtils.closeProgressDialog();


                if (null != result) {
                    if (result.status.equals("0")) {

                        webView.loadData(result.data.Content, "text/html; charset=UTF-8", null);

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
