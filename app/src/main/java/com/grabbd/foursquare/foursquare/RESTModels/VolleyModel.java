package com.grabbd.foursquare.foursquare.RESTModels;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class VolleyModel {
    public Context context;
    public static String TAG = VolleyModel.class.getSimpleName();
    public static final String SERVER_PATH = "https://api.foursquare.com/v2/";
    public static final String EXPLORE = "venues/explore";
    public static final String SEARCH = "venues/search";

    public static final String TAG_CLIENT_ID = "client_id";
    public static final String TAG_CLIENT_SECRET = "client_secret";
    public static final String TAG_VERSIONING = "v";


    public static final String CLIENT_ID = "JYE5233VPFAFMCMTFETF3RMYUS3DRASHNRGF4HLFWGP0QLOE";
    public static final String CLIENT_SECRET = "G5EPRZOOWLNCSWQ5Z5B2LFOWZF242KZESXV0HBL30VTC2LQB";
    public static final String DATE = "20180623";


    private static RequestQueue requestQueue;

    public static RequestQueue getInstanceRequestQueue(Context context) {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        return requestQueue;
    }

    public Context getContext() {
        if (context == null) {
            throw new RuntimeException("Context is null, send context before getting it.");
            //            return CrafterApplication.getContext();
        }
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    protected void setAuthParameters(Map<String, String> params) {

        params.put(TAG_CLIENT_ID, CLIENT_ID);
        params.put(TAG_CLIENT_SECRET, CLIENT_SECRET);
        params.put(TAG_VERSIONING, DATE);
    }

    protected String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
    protected String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }


}
