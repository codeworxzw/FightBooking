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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class HttpUtils {

    private static final String TAG = HttpUtils.class.getName();
    private static final int CONNECTION_TIMEOUT = 30;

//	public static String requestHttpPOST(String url, JSONObject json) {
//
//		URL path;
//		HttpURLConnection urlConnection = null;
//		JSONArray response = new JSONArray();
//
//		try {
//			path = new URL(url);
//			urlConnection = (HttpURLConnection) path.openConnection();
//			urlConnection.setDoOutput(true);
//			urlConnection.setRequestMethod("POST");
//			urlConnection.setRequestProperty("Content-Type",
//					"application/x-www-form-urlencoded");
//
//			int responseCode = urlConnection.getResponseCode();
//
//			if(responseCode == HttpStatus.SC_OK){
//				String responseString = readStream(urlConnection.getInputStream());
//				Log.v("CatalogClient", responseString);
//				response = new JSONArray(responseString);
//			}else{
//				Log.v("CatalogClient", "Response code:"+ responseCode);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(urlConnection != null)
//				urlConnection.disconnect();
//		}
//
//		return response.toString();
//
//	}
//
//	private static String readStream(InputStream in) {
//		BufferedReader reader = null;
//		StringBuffer response = new StringBuffer();
//		try {
//			reader = new BufferedReader(new InputStreamReader(in));
//			String line = "";
//			while ((line = reader.readLine()) != null) {
//				response.append(line);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return response.toString();
//	}
//
//	public static String requestUrl(String url, String postParameters)
//			throws Exception {
//
//		HttpURLConnection urlConnection = null;
//		try {
//			// create connection
//			URL urlToRequest = new URL(url);
//			urlConnection = (HttpURLConnection) urlToRequest.openConnection();
//			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//			urlConnection.setReadTimeout(30);
//
//			// handle POST parameters
//			if (postParameters != null) {
//
//				urlConnection.setDoOutput(true);
//				urlConnection.setRequestMethod("POST");
//				urlConnection.setFixedLengthStreamingMode(
//						postParameters.getBytes().length);
//				urlConnection.setRequestProperty("Content-Type",
//						"application/x-www-form-urlencoded");
//
//				//send the POST out
//				PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
//				out.print(postParameters);
//				out.close();
//			}
//
//			// handle issues
//			int statusCode = urlConnection.getResponseCode();
//			if (statusCode != HttpURLConnection.HTTP_OK) {
//				// throw some exception
//			}
//
//			// read output (only for GET)
//			if (postParameters != null) {
//				return null;
//			} else {
//				InputStream in =
//						new BufferedInputStream(urlConnection.getInputStream());
//				return readStream(in);
//			}
//
//
//        } catch (MalformedURLException e) {
//            // handle invalid URL
//        } catch (SocketTimeoutException e) {
//            // hadle timeout
//        } catch (IOException e) {
//            // handle I/0
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
//
//		return null;
//	}

    public static String requestHttpPOST(String url, JSONObject json) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);

        BasicHttpParams timeoutParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(timeoutParams, 10000);
        HttpConnectionParams.setSoTimeout(timeoutParams, 10000);
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
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpConnectionParams.setSoTimeout(client.getParams(), 10000);
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
}
