package com.grabbd.foursquare.foursquare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * TODO: document your custom view class.
 */
public class SearchBar extends LinearLayout {
    private static final int REQUEST_ACCESS_LOCATION = 100;

    private Activity activity;
    private EditText searchEditText;
    private ImageButton myLocationButton;
    private String selectedPlace;

    private FusedLocationProviderClient mFusedLocationClient;
    GooglePlacesAutoCompleteHandler googlePlacesAutoCompleteHandler;

    public SearchBar(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.search_bar, null);
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchEditText.setOnClickListener(v -> openGooglePlacesView());
        myLocationButton = view.findViewById(R.id.locationButton);
        myLocationButton.setOnClickListener(v -> getCurrentLocation());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        this.addView(view);
    }

    private void openGooglePlacesView() {
        googlePlacesAutoCompleteHandler = new GooglePlacesAutoCompleteHandler();
        googlePlacesAutoCompleteHandler.openAutocompleteActivity(activity);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                        }
                    }
                });
    }

    public void handleAutoCompleteData(int requestCode, int resultCode, Intent data) {
        String result = googlePlacesAutoCompleteHandler.getResultString(requestCode, resultCode, data,activity);
        searchEditText.setText(result);
        selectedPlace = googlePlacesAutoCompleteHandler.getOnlyPlace(requestCode, resultCode, data, activity);
        listener.onLocationSelected(result);
    }

    public String getSelectedPlace() {
        return selectedPlace;
    }


    public boolean requestLocationPermission()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        else
        {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION);
        }
        return false;
    }

    private OnLocationSelectedListener listener;
    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }
    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
        void onLocationSelected(double lat, double lng);
    }
}
