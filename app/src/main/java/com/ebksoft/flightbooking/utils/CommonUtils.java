package com.ebksoft.flightbooking.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ebksoft.flightbooking.R;

import org.json.JSONObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static ProgressDialog showProgressDialog(Context context) {

        progress = new ProgressDialog(context);
        progress.show();
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        progress.setContentView(R.layout.layout_custom_progress_dialog);

        return progress;
    }

    public static void closeProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    public static String getMonthYearString(int month, int year) {
        String s = String.format("%s %d, %d", "Tháng", month, year);
        return s;
    }

    public static String getDayString(int day) {
        String s = "Thứ ";
        switch (day) {
            case 1:
                s = "Chủ nhật";
                break;

            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                s += day;
                break;
        }
        return s;
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPhoneValid(String phone) {

        if (phone.length() != 10 && phone.length() != 11) {
            return false;
        }

        if (phone.length() == 10) {
            if (!phone.substring(0, 2).equals("09")) {
                return false;
            }
        }

        if (phone.length() == 11) {
            if (!phone.substring(0, 2).equals("01")) {
                return false;
            }
        }

        return true;
    }
}
