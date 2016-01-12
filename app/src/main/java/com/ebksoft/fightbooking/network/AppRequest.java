package com.ebksoft.fightbooking.network;

import android.content.Context;

import com.ebksoft.fightbooking.model.ResponseObj.GetTicketResObj;
import com.ebksoft.fightbooking.model.ResponseObj.InitResObj;
import com.ebksoft.fightbooking.model.ResponseObj.SearchResObj;
import com.ebksoft.fightbooking.utils.CommonUtils;
import com.ebksoft.fightbooking.utils.ConfigAPI;
import com.ebksoft.fightbooking.utils.DataRequestCallback;
import com.ebksoft.fightbooking.utils.HttpUtils;
import com.ebksoft.fightbooking.utils.JsonParserUtils;
import com.ebksoft.fightbooking.utils.ThreadManager;

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
}
