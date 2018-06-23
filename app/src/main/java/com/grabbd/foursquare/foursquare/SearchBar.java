package com.grabbd.foursquare.foursquare;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * TODO: document your custom view class.
 */
public class SearchBar extends LinearLayout {

    public SearchBar(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.addView(LayoutInflater.from(getContext()).inflate(R.layout.search_bar, null));
    }

    public View getSearchBar() {
        return LayoutInflater.from(getContext()).inflate(R.layout.search_bar, this, true);
    }
}
