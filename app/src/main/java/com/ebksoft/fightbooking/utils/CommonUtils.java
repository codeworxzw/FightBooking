package com.ebksoft.fightbooking.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by chauminhnhut on 12/1/15.
 */
public class CommonUtils {

    static boolean isToastShowing = false;
//    static Handler handler = new Handler();
    static ProgressDialog progress;

    public static JSONObject buildJson(Map<String, Object> params) {
        JSONObject json = new JSONObject();
        try {
            if (params != null && params.entrySet().size() > 0) {
                for (Object key : params.keySet()) {
                    json.putOpt(key.toString(), params.get(key));
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("", e.getMessage());
        }
        return json;
    }


    public static String getIPAddress(Context context) {

        WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        int ipAddress = wifiInf.getIpAddress();
        return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

    }

    public static void showToast(Context c, String message) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
    }
}
