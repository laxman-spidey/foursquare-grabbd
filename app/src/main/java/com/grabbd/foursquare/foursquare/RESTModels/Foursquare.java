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


public class Foursquare extends VolleyModel {

    public void explore(String location, ResponseListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("near", location);
        setAuthParameters(params);
        String url = SERVER_PATH + EXPLORE +"?"+ super.urlEncodeUTF8(params);

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

    public void search(String location, ResponseListener listener) {

        Map<String, String> params = new HashMap<>();
        params.put("near", location);
        setAuthParameters(params);
        String url = SERVER_PATH + SEARCH +"?"+ super.urlEncodeUTF8(params);

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



    private static Foursquare singleton = null;

    public static Foursquare getInstance(Context context) {
        if (singleton == null) {
            singleton = new Foursquare(context);
        }
        return singleton;
    }

    public static String TAG = Foursquare.class.getSimpleName();

    public Foursquare(Context context) {
        setContext(context);
    }
}
