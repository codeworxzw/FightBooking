package com.ebksoft.flightbooking.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    private static final String TAG = HttpUtils.class.getName();
    private static final int CONNECTION_TIMEOUT = 30000;

    public static String requestHttpPOST(String url, JSONObject json) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);

        BasicHttpParams timeoutParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(timeoutParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(timeoutParams, CONNECTION_TIMEOUT);
        httpPost.setParams(timeoutParams);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("data", json.toString()));

        try {
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,
                    HTTP.UTF_8);
            httpPost.setHeader(HTTP.CONTENT_TYPE,
                    "application/x-www-form-urlencoded;charset=UTF-8");
            httpPost.setEntity(ent);
            HttpResponse responsePOST = httpClient.execute(httpPost);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() != null ? e.getMessage() : "Error");
        } finally {
            // httpClient.close();
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }

    ///
    public static String requestSSLPOST(String url, JSONObject json) {
        String result = null;
        HttpClient client = getNewHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(client.getParams(), CONNECTION_TIMEOUT);
        HttpResponse httpResponse;
        try {
            String stringUrl = url;
            HttpPost post = new HttpPost(stringUrl);
            post.setHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("data", json.toString()));

            HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            post.setEntity(httpEntity);
            httpResponse = client.execute(post);
            if (httpResponse != null) {
                InputStream is = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                StringBuilder responseStr = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseStr.append(line);
                }
                result = responseStr.toString().trim();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            // registry.register(new Scheme("http",
            // PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public static String downloadUrl(String myurl) throws IOException {
        InputStream is = null;


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string

            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }

            return total.toString();

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

}
