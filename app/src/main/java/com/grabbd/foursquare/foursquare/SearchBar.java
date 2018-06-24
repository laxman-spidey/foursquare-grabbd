package com.grabbd.foursquare.foursquare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.List;

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

        // Fetch latitude and longitude from the current location.
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            Location location = task.getResult();
                            selectedLocationType = LOCATION_TYPE_LAT_LNG;
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            listener.onLocationSelected(location.getLatitude(), location.getLongitude());

                            // Try to get address from the co-ordinates. If not possible just put coordinates into the searchEditText
                            List<Address> list = null;
                            try {
                                list = new Geocoder(activity).getFromLocation(location
                                        .getLatitude(), location.getLongitude(), 1);

                                String result = "";
                                if (list != null & list.size() > 0) {
                                    Address address = list.get(0);
                                    result += address.getAddressLine(0);
                                    searchEditText.setText(result);
                                } else {
                                    searchEditText.setText(location.getLatitude() + ", " + location.getLongitude());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                searchEditText.setText(location.getLatitude() + ", " + location.getLongitude());
                            }
                        } else {
                            Toast.makeText(activity, "Unable to locate you. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        task.getException().printStackTrace();
                        Toast.makeText(activity, "Exception Unable to locate you. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void handleOnActivityCompleted(int requestCode, int resultCode, Intent data) {

        if (requestCode == placesAutoCompleteRequestCode) {
            handleAutoCompleteData(requestCode, resultCode, data);
        }
    }

    public void onRequestPermissionsResult(int requestCode, int grantresult) {
        if (requestCode == locationPermissionRequestCode) {
            if (grantresult == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    public void handleAutoCompleteData(int requestCode, int resultCode, Intent data) {
        String result = googlePlacesAutoCompleteHandler.getResultString(requestCode, resultCode, data, activity);
        searchEditText.setText(result);
        selectedLocationType = LOCATION_TYPE_PLACE;
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
