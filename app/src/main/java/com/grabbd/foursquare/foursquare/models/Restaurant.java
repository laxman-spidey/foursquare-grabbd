package com.grabbd.foursquare.foursquare.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    public String id;
    public String name;
    public String city;
    public String state;
    public String country;
    public List<String> categories;
    public List<String> photos;

    public Restaurant(String venueJson) throws JSONException {
        JSONObject object = new JSONObject(venueJson);
        id = object.getString("id");
        name = object.getString("name");
        JSONObject locationObject = object.getJSONObject("location");
        city = locationObject.getString("city");
        state = locationObject.getString("state");
        country = locationObject.getString("country");
        JSONArray categories = object.getJSONArray("categories");
        this.categories = new ArrayList<>();
        for (int i = 0; i < categories.length(); i++) {
            this.categories.add(categories.getJSONObject(i).getString("name"));
        }

    }

    public static List<Restaurant> getFromVenues(JSONArray venues) throws JSONException {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < venues.length(); i++) {
            restaurants.add(new Restaurant(venues.getJSONObject(i).getJSONObject("venue").toString()));
        }
        return restaurants;
    }
}
