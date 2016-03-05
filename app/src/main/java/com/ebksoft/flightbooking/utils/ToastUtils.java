package com.ebksoft.flightbooking.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chauminhnhut on 2/29/16.
 */
public class ToastUtils {

    public static void toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
