package com.grabbd.foursquare.foursquare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.SearchView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * TODO: document your custom view class.
 */
public class SearchBar extends LinearLayout {

    GooglePlacesAutoCompleteHandler googlePlacesAutoCompleteHandler;
    Activity activity;
    EditText searchEditText;
    ImageButton myLocationButton;
    private String selectedPlace;
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
        this.addView(view);
    }

    private void openGooglePlacesView() {
        googlePlacesAutoCompleteHandler = new GooglePlacesAutoCompleteHandler();
        googlePlacesAutoCompleteHandler.openAutocompleteActivity(activity);
    }

    private void getCurrentLocation() {

    }

    public void handleAutoCompleteData(int requestCode, int resultCode, Intent data) {
        String result = googlePlacesAutoCompleteHandler.getResultString(requestCode, resultCode, data,activity);
        searchEditText.setText(result);
        selectedPlace = googlePlacesAutoCompleteHandler.getOnlyPlace(requestCode, resultCode, data, activity);
    }

    public String getSelectedPlace() {
        return selectedPlace;
    }

}
