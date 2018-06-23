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
    public String address;
    public List<String> categories;
    public List<String> photos;

    /*
     *  If there is incomplete data returned by the API, return null.
     */
    public static Restaurant fromJson(String venueJson) {
        Restaurant restaurant = new Restaurant();
        try {
            JSONObject object = new JSONObject(venueJson);
            restaurant.id = object.getString("id");
            restaurant.name = object.getString("name");
            JSONObject locationObject = object.getJSONObject("location");
            restaurant.address = locationObject.getString("address");
            restaurant.city = locationObject.getString("city");
            restaurant.state = locationObject.getString("state");
            restaurant.country = locationObject.getString("country");
            JSONArray categories = object.getJSONArray("categories");
            restaurant.categories = new ArrayList<>();
            for (int i = 0; i < categories.length(); i++) {
                restaurant.categories.add(categories.getJSONObject(i).getString("name"));
            }
            return restaurant;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * To avoid runtime crashes due to incomplete data, it skips such objects.
     */
    public static List<Restaurant> getFromVenues(JSONArray venues)  {
        List<Restaurant> restaurants = new ArrayList<>();
        for (int i = 0; i < venues.length(); i++) {
            try {
                Restaurant restaurant =  Restaurant.fromJson(venues.getJSONObject(i).getJSONObject("venue").toString());
                if (restaurant != null) {
                    //skip the object if there is an exception occurred.
                    restaurants.add(restaurant);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return restaurants;
    }
}
