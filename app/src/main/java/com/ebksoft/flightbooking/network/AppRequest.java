package com.ebksoft.flightbooking.network;

import android.content.Context;
import android.util.Log;

import com.ebksoft.flightbooking.model.ResponseObj.BookingResultResObj;
import com.ebksoft.flightbooking.model.ResponseObj.DetailNewsResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetAboutUsResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetBagResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetHotlinesResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetSocialResObj;
import com.ebksoft.flightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.flightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.flightbooking.model.ResponseObj.ListNewsResObj;
import com.ebksoft.flightbooking.model.ResponseObj.SVResponseObj;
import com.ebksoft.flightbooking.model.ResponseObj.SearchResObj;
import com.ebksoft.flightbooking.utils.CommonUtils;
import com.ebksoft.flightbooking.utils.ConfigAPI;
import com.ebksoft.flightbooking.utils.DataRequestCallback;
import com.ebksoft.flightbooking.utils.HttpUtils;
import com.ebksoft.flightbooking.utils.JsonParserUtils;
import com.ebksoft.flightbooking.utils.ThreadManager;

import org.json.JSONObject;

import java.util.HashMap;

public class AppRequest {
    public static final String TAG = AppRequest.class.getName();


    public static void reqInitAPI(final Context context,
                                  final HashMap<String, Object> params, final boolean forceUpdate,
                                  final DataRequestCallback<InitResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_INIT;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                InitResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void searchFight(final Context context,
                                   final HashMap<String, Object> params, final boolean forceUpdate,
                                   final DataRequestCallback<SearchResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_SEARCH;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                SearchResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getTicket(final Context context,
                                 final HashMap<String, Object> params, final boolean forceUpdate,
                                 final DataRequestCallback<GetTicketResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_TICKET;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                GetTicketResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void sendSelectedTicket(final Context context,
                                          final HashMap<String, Object> params, final boolean forceUpdate,
                                          final DataRequestCallback<SVResponseObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_SELECT_TICKET;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                SVResponseObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getBag(final Context context,
                              final HashMap<String, Object> params, final boolean forceUpdate,
                              final DataRequestCallback<GetBagResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_BAG;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                GetBagResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void addPassenger(final Context context,
                                    final HashMap<String, Object> params, final boolean forceUpdate,
                                    final DataRequestCallback<SVResponseObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_ADD_PASSENGER;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                SVResponseObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void sendBooking(final Context context,
                                   final HashMap<String, Object> params, final boolean forceUpdate,
                                   final DataRequestCallback<SVResponseObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_BOOKING;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                SVResponseObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getBookingResult(final Context context,
                                        final HashMap<String, Object> params, final boolean forceUpdate,
                                        final DataRequestCallback<BookingResultResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_BOOKING_RESULT;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                BookingResultResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getListNews(final Context context,
                                   final HashMap<String, Object> params, final boolean forceUpdate,
                                   final DataRequestCallback<ListNewsResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_NEWS;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                ListNewsResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getListNewsDetail(final Context context,
                                         final HashMap<String, Object> params, final boolean forceUpdate,
                                         final DataRequestCallback<DetailNewsResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_NEWS_DETAIL;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                DetailNewsResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getSocial(final Context context,
                                 final HashMap<String, Object> params, final boolean forceUpdate,
                                 final DataRequestCallback<GetSocialResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_SOCIAL;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                GetSocialResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }


    public static void getAbout(final Context context,
                                final HashMap<String, Object> params, final boolean forceUpdate,
                                final DataRequestCallback<GetAboutUsResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_ABOUT;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                GetAboutUsResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void getHotlines(final Context context,
                                   final HashMap<String, Object> params, final boolean forceUpdate,
                                   final DataRequestCallback<GetHotlinesResObj> callback) {

        final String url = ConfigAPI.DOMAIN_HTTP
                + ConfigAPI.API_GET_HOTLINES;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST(url, jsonRequest);
                GetHotlinesResObj obj = JsonParserUtils.parseJSONObjectToObject(result, callback.getType());
                callback.setResult(obj);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }

    public static void importBooking(final Context context,
                                     final HashMap<String, Object> params, final boolean forceUpdate,
                                     final DataRequestCallback<SVResponseObj> callback) {

        final String url = ConfigAPI.IMPORT_BOOKING;

        final ThreadManager t = ThreadManager.getInstance();

        int priority;
        if (forceUpdate) {
            priority = ThreadManager.PRIORITY_BLOCKING;
        } else {
            priority = ThreadManager.PRIORITY_NORMAL;
        }

        t.execute(new Runnable() {

            @Override
            public void run() {
                JSONObject jsonRequest = CommonUtils.buildJson(params);
                String result = HttpUtils.requestHttpPOST1(url, jsonRequest);
                Log.e("", result);
                t.callbackOnUIThread(callback, null, false);

            }
        }, priority);
    }


}
