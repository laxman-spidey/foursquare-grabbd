package com.grabbd.foursquare.foursquare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;


public class SearchFragment extends Fragment {

    public static final int REQUEST_CODE_AUTOCOMPLETE = 301;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 302;

    private SearchBar searchBar;
    private EditText queryBox;


    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setupSearchBar(view);
        setupQueryBox(view);
        addRestaurantListFragment();
        return view;
    }

    private String searchString;
    private void setupQueryBox(View view) {
        queryBox = view.findViewById(R.id.queryBox);
        queryBox.setEnabled(false);
        queryBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (restaurantListFragment != null) {
                    searchString = s != null ? s.toString() : "";
                    if (!searchString.equals("")) {

                        switch (searchBar.selectedLocationType) {
                            case -1: break;
                            case SearchBar.LOCATION_TYPE_PLACE: {
                                restaurantListFragment.filter(searchBar.getSelectedPlace(), searchString);
                            }
                            case SearchBar.LOCATION_TYPE_LAT_LNG: {
                                restaurantListFragment.filter(searchBar.lat, searchBar.lng, searchString);
                            }
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    RestaurantsFragment restaurantListFragment;
    private void addRestaurantListFragment() {
        restaurantListFragment = RestaurantsFragment.newInstance(RestaurantsFragment.ACTION_SEARCH);
        getFragmentManager().beginTransaction().add(R.id.restaurantSearchListFragmentContainer, restaurantListFragment).commit();

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
                restaurantListFragment.filter(location, searchString);
                queryBox.setEnabled(true);
            }

            @Override
            public void onLocationSelected(double lat, double lng) {
                restaurantListFragment.filter(lat, lng, searchString);
                queryBox.setEnabled(true);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        searchBar.handleOnActivityCompleted(requestCode, resultCode, data);
    }
    public void onRequestPermissionsResult(int requestCode, int grantresult) {
        searchBar.onRequestPermissionsResult(requestCode, grantresult);
    }

}
