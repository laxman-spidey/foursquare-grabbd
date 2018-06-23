package com.grabbd.foursquare.foursquare;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grabbd.foursquare.foursquare.RESTModels.FoursquareAPI;
import com.grabbd.foursquare.foursquare.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsFragment extends Fragment {


    private static final String ARG_ACTION = "action";
    public static final int ACTION_EXPLORE = 1;
    public static final int ACTION_SEARCH = 2;
    private int action;
    private List<Restaurant> restaurants = new ArrayList<>();
    private RestaurantsRecyclerViewAdapter adapter;


    public RestaurantsFragment() {
    }
    public static RestaurantsFragment newInstance(int action) {
        RestaurantsFragment fragment = new RestaurantsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            action = getArguments().getInt(ARG_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            adapter = new RestaurantsRecyclerViewAdapter(getContext(), restaurants);
            recyclerView.setAdapter(adapter);
            filter("Chicago, IL");
        }
        return view;
    }

    public void filter(String location) {
        FoursquareAPI.getInstance(getContext()).explore(location, response -> {
            if (response.isOkay) {
                if (response.data != null) {
                    List<Restaurant> data = (List<Restaurant>) response.data;
                    restaurants.addAll(data);
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }
    public void filter(double lat, double lng) {

    }



}
