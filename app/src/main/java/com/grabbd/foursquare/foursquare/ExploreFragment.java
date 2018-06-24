package com.grabbd.foursquare.foursquare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class ExploreFragment extends Fragment {

    public static final int REQUEST_CODE_AUTOCOMPLETE = 201;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 202;


    private SearchBar searchBar;
    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        setupSearchBar(view);
        setupSectionList(view);
        addRestaurantListFragment();
        return view;
    }

    private ViewGroup sectionList;
    private String selectedSection = null;
    private void setupSectionList(View view) {

        sectionList = view.findViewById(R.id.sectionsList);
        sectionList.setVisibility(View.INVISIBLE);
        int count = sectionList.getChildCount();
        SectionView v = null;
        for(int i=0; i<count; i++) {
            v = (SectionView) sectionList.getChildAt(i);
            v.setOnSelectedListener(selectedText -> {
                selectedSection = selectedText;
                switch (searchBar.selectedLocationType) {
                    case -1: break;
                    case SearchBar.LOCATION_TYPE_PLACE: {
                        restaurantListFragment.filter(searchBar.getSelectedPlace(), selectedText);
                        break;
                    }
                    case SearchBar.LOCATION_TYPE_LAT_LNG: {
                        restaurantListFragment.filter(searchBar.lat, searchBar.lng, selectedText);
                        break;
                    }
                }
            });
        }

    }
    private void setupSearchBar(View view) {
        RelativeLayout layoutContainer = view.findViewById(R.id.searchBarContainer);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        searchBar = new SearchBar(getActivity());
        layoutContainer.addView(searchBar, layoutParams);

        searchBar.setRequestCodes(REQUEST_CODE_AUTOCOMPLETE, REQUEST_CODE_LOCATION_PERMISSION);
        searchBar.setOnLocationSelectedListener(new SearchBar.OnLocationSelectedListener() {
            @Override
            public void onLocationSelected(String location) {
                restaurantListFragment.filter(location, selectedSection);
                sectionList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLocationSelected(double lat, double lng) {
                restaurantListFragment.filter(lat, lng, selectedSection);
                sectionList.setVisibility(View.VISIBLE);
            }
        });

    }

    RestaurantsFragment restaurantListFragment;
    private void addRestaurantListFragment() {
        restaurantListFragment = RestaurantsFragment.newInstance(RestaurantsFragment.ACTION_EXPLORE);
        getFragmentManager().beginTransaction().add(R.id.restaurentListFragementContainer, restaurantListFragment).commit();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        searchBar.handleOnActivityCompleted(requestCode, resultCode, data);
    }
    public void onRequestPermissionsResult(int requestCode, int grantresult) {
        searchBar.onRequestPermissionsResult(requestCode, grantresult);
    }
}
