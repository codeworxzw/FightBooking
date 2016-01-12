package com.ebksoft.flightbooking.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.ebksoft.flightbooking.R;


/**
 * Created by chauminhnhut on 10/30/15.
 */
public class ProgessBarUtils {


    static ProgessBarUtils instance;
    private Context context;

    private ProgressDialog progress;
    private int progressBar;

    public static ProgessBarUtils getInstance(Context context) {
        if (null == instance)
            instance = new ProgessBarUtils(context);
        return instance;
    }

    public ProgessBarUtils(Context context) {
        this.context = context;
        this.progress = new ProgressDialog(context);
        this.progressBar = R.layout.layout_custom_progress_dialog;
    }

    public void showProgess() {
        if (progress == null || !progress.isShowing()) {
            progress.show();
            progress.setCancelable(true);
            progress.setCanceledOnTouchOutside(false);
            progress.setContentView(this.progressBar);
        }
    }

    public void hideProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }
}
