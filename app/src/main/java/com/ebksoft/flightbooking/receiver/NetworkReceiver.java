package com.ebksoft.flightbooking.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ebksoft.flightbooking.utils.AppApplication;
import com.ebksoft.flightbooking.utils.CommonUtils;

/**
 * Created by chauminhnhut on 4/2/16.
 */
public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppApplication.getInstance(context).setInternetConnnection(
                CommonUtils.checkInternetConnection(context));
    }

}
