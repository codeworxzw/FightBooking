package com.ebksoft.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ebksoft.flightbooking.model.HistorySearchTrip;
import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.SharedpreferencesUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chauminhnhut on 1/5/16.
 */
public class SplashScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(R.drawable.ic_splash);
        setContentView(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext, HomeActivity.class));
                finish();
            }
        }, 3000);

        loadView();
        loadData();
    }

    @Override
    protected void loadData() {
        loadListHistorySearchTrip();
    }

    @Override
    protected void loadView() {

    }

    /*
    * Load danh sach các lịch sử đã search trước đó
    * */
    private void loadListHistorySearchTrip() {

        AppApplication appApplication = AppApplication.getInstance(this);
        appApplication.historySearchTrips = new ArrayList<>();

        String history = SharedpreferencesUtils.getInstance(this).read("history");
        if(!TextUtils.isEmpty(history)){

            appApplication.historySearchTrips = new Gson().fromJson(history, new TypeToken<List<HistorySearchTrip>>() {
            }.getType());

        }

    }
}
