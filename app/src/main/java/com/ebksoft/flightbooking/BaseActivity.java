package com.ebksoft.flightbooking;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;


/**
 * Created by chauminhnhut on 1/5/16.
 */
public abstract class BaseActivity extends Activity {

    protected abstract void loadView();

    protected abstract void loadData();


    protected Context mContext;
    protected TextView txtTitle;
    private ImageView imgClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initStatusbar();
    }

    protected void initStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        }
    }

    protected void initTitle(String title) {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        if (txtTitle != null)
            txtTitle.setText(title.toUpperCase(Locale.getDefault()));
    }

    protected void initButtonBack() {
        imgClose = (ImageView) findViewById(R.id.imgClose);
        if (null != imgClose)
            imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
    }
}
