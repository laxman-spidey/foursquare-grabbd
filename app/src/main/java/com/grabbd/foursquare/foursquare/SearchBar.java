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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * TODO: document your custom view class.
 */
public class SearchBar extends LinearLayout {
    public static final int REQUEST_CODE_AUTOCOMPLETE = 101;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 102;

    public int selectedLocationType = -1;
    public static final int LOCATION_TYPE_PLACE = 1;
    public static final int LOCATION_TYPE_LAT_LNG = 2;


    private int placesAutoCompleteRequestCode = REQUEST_CODE_AUTOCOMPLETE;
    private int locationPermissionRequestCode = REQUEST_CODE_LOCATION_PERMISSION;

    private Activity activity;
    private EditText searchEditText;
    private ImageButton myLocationButton;
    private String selectedPlace;
    public double lat;
    public double lng;


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
        googlePlacesAutoCompleteHandler.openAutocompleteActivity(placesAutoCompleteRequestCode, activity);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        mFusedLocationClient.getLastLocation()
//                .addOnSuccessListener(activity, location -> {
//
//                    // Got last known location. In some rare situations this can be null.
//                    if (location == null) {
//                        Toast.makeText(activity, "Unable to locate you. Please try again.", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    searchEditText.setText(location.getLatitude() +", "+ location.getLongitude());
//                    listener.onLocationSelected(location.getLatitude(), location.getLongitude());
//                })
//                .addOnFailureListener(e -> {
//                    e.printStackTrace();
//                })
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        if(task.getResult() != null) {
                            Location location = task.getResult();
                            lat =location.getLatitude();
                            lng = location.getLongitude();
                            searchEditText.setText(location.getLatitude() +", "+ location.getLongitude());
                            listener.onLocationSelected(location.getLatitude(), location.getLongitude());
                        }
                        else {
                            Toast.makeText(activity, "Unable to locate you. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        task.getException().printStackTrace();
                        Toast.makeText(activity, "Exception Unable to locate you. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void handleOnActivityCompleted(int requestCode, int resultCode, Intent data) {
        if (requestCode == locationPermissionRequestCode) {
            getCurrentLocation();
        }
        else if( requestCode == placesAutoCompleteRequestCode) {
            handleAutoCompleteData(requestCode, resultCode, data);
        }

    }

    public void handleAutoCompleteData(int requestCode, int resultCode, Intent data) {
        String result = googlePlacesAutoCompleteHandler.getResultString(requestCode, resultCode, data, activity);
        searchEditText.setText(result);
        selectedLocationType = LOCATION_TYPE_PLACE;
//        selectedPlace = googlePlacesAutoCompleteHandler.getOnlyPlace(requestCode, resultCode, data, activity);
        selectedPlace = result;
        listener.onLocationSelected(result);
    }


    public String getSelectedPlace() {
        return selectedPlace;
    }


    public boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, locationPermissionRequestCode);
        }
        return false;
    }

    private OnLocationSelectedListener listener;

    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }

    public void setRequestCodes(int placesAutoCompleteRequestCode, int locationPermissionRequestCode) {
        this.placesAutoCompleteRequestCode = placesAutoCompleteRequestCode;
        this.locationPermissionRequestCode = locationPermissionRequestCode;
    }
    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
        void onLocationSelected(double lat, double lng);
    }
}
