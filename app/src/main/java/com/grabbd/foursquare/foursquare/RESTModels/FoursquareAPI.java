package com.grabbd.foursquare.foursquare.RESTModels;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.grabbd.foursquare.foursquare.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoursquareAPI extends VolleyModel {



    public void explore(String location, String section, ResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("near", location);
        if (section != null) {
            params.put("section", section);
        }
        sendRequest(params, EXPLORE, listener);
    }
    public void explore(double lat, double lng, String section, ResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("ll", lat+","+lng);
        if (section != null) {
            params.put("section", section);
        }
        sendRequest(params, EXPLORE, listener);
    }


    public void search(String location, ResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("near", location);
        sendRequest(params, SEARCH, listener);

    }
    public void search(double lat, double lng, ResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("ll", lat+","+lng);
        sendRequest(params, SEARCH, listener);
    }

    private void sendRequest(Map params, String API, ResponseListener listener) {
        setAuthParameters(params);
        String url = SERVER_PATH + API +"?"+ super.urlEncodeUTF8(params);
        Log.i(TAG, url);
        StringRequest jsonObjectRequest = new StringRequest
                (Request.Method.GET, url, response -> {

                    List<Restaurant> restaurants;
                    try {
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray venues = responseObject
                                .getJSONObject("response")
                                .getJSONArray("groups")
                                .getJSONObject(0)
                                .getJSONArray("items");
                        restaurants = Restaurant.getFromVenues(venues);
                        listener.onResponseRecieved(new ResponseListener.Response(true, restaurants));
                        Log.i(TAG, new Gson().toJson(restaurants));
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                        e.printStackTrace();
                        listener.onResponseRecieved(new ResponseListener.Response(false, null));
                    }
                }, error ->
                {
                    error.printStackTrace();
                    listener.onResponseRecieved(new ResponseListener.Response(false, null));
                });
        Log.i(TAG, jsonObjectRequest.toString());
        getInstanceRequestQueue(getContext()).add(jsonObjectRequest);
    }


    private static FoursquareAPI singleton = null;

    public static FoursquareAPI getInstance(Context context) {
        if (singleton == null) {
            singleton = new FoursquareAPI(context);
        }
        return singleton;
    }

    public static String TAG = FoursquareAPI.class.getSimpleName();

    public FoursquareAPI(Context context) {
        setContext(context);
    }
}
