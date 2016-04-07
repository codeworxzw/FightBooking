package com.ebksoft.flightbooking.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.ebksoft.flightbooking.GetPromotionActivity;
import com.ebksoft.flightbooking.R;
import com.ebksoft.flightbooking.model.HistorySearchTrip;
import com.ebksoft.flightbooking.model.SocialModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    /**
     * Khởi tạo các thông tin mặc định
     */
    public static void init(Context context) {

        AppApplication.getInstance(context).setInternetConnnection(
                CommonUtils.checkInternetConnection(context
                        .getApplicationContext()));

//        DatabaseManager.initializeInstance(new DatabaseHelper(context
//                .getApplicationContext()));
//
//        String regId = GCMRegistrar.getRegistrationId(context);
//        if (null != regId && !regId.equals("")) {
//            AppApplication.getInstance().setRegistrationId(regId);
//        }

    }


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

    public static void showToastNoInternetConnecton(Context c) {
        showToast(c, c.getString(R.string.no_internet_connection));
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

    public static void showKeyHask(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getApplicationContext().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static Intent getOpenFacebookIntent(Context context, SocialModel model) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            String s = String.format("fb://page/%s", model.ID);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(model.Link));
        }
    }

    public static Intent getOpenGooglePlusIntent(Context context, SocialModel model) {
        try {

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.Link));
            intent.setPackage("com.google.android.apps.plus");// App has been installed

            if (intent.resolveActivity(context.getPackageManager()) != null)//(fix disable)
                return intent;

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return new Intent(Intent.ACTION_VIEW, Uri.parse(model.Link));

    }

    public static void addHistorySearchTrip(Context context, HistorySearchTrip trip) {

        AppApplication appApplication = AppApplication.getInstance(context);

        if (appApplication.historySearchTrips != null) {
            if (appApplication.historySearchTrips.size() >= 10) {
                appApplication.historySearchTrips.remove(0);
            }
            appApplication.historySearchTrips.add(trip);
        }

        /*
        * Save async to Share Pref
        * */
        CommonUtils.saveSharePref(context);
    }

    public static void saveSharePref(final Context context) {

        final ThreadManager t = ThreadManager.getInstance();

        int priority = ThreadManager.PRIORITY_NORMAL;

        t.execute(new Runnable() {

            @Override
            public void run() {

                AppApplication appApplication = AppApplication.getInstance(context);

                if (appApplication.historySearchTrips != null) {
                    String s = new Gson().toJson(appApplication.historySearchTrips);
                    SharedpreferencesUtils.getInstance(context).save("history", s);
                }

                /*
                * Do Backup Share pref
                * */

//                BackupAgentUtils backup = new BackupAgentUtils();
//                backup.requestBackup();


            }
        }, priority);
    }

    /**
     * Kiem tra ket noi mang
     *
     * @param context
     * @return
     */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    public static boolean isNetWorkAvailable(Context context) {
        return AppApplication.getInstance(context).isInternetConnnection();
    }

    /*
    * Show notification for testing
    * */
    public static void showNotify(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_app)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        Intent resultIntent = new Intent(context, GetPromotionActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack
        stackBuilder.addParentStack(GetPromotionActivity.class);
// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());


    }
}
