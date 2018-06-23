package com.grabbd.foursquare.foursquare;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.grabbd.foursquare.foursquare.models.Restaurant;

import java.util.List;


public class RestaurantsRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RestaurantsRecyclerViewAdapter.class.getSimpleName();

    private final List<Restaurant> mValues;
    private Context context;
    private int currentPosition = -1;


    public RestaurantsRecyclerViewAdapter(Context context, List<Restaurant> items) {
        mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurants_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mNameView.setText(mValues.get(position));


        //hide all the detail views.
        holder.mDetailsView.setVisibility(View.GONE);

        //get the current position and reload the list when a restaurant is clicked
        holder.mTitleView.setOnClickListener(v -> {
            currentPosition = position;
            notifyDataSetChanged();
        });


        //animate and expand currently clicked item
        if (currentPosition == position) {
            Animation slideDown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
            holder.mDetailsView.setVisibility(View.VISIBLE);
            holder.mDetailsView.startAnimation(slideDown);
        }


    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "restaurant count: " + mValues.size());
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final View mTitleView;
        public final View mDetailsView;
        public final ImageView mImageView;
        public final TextView mNameView;
        public final TextView mCity;
        public final TextView mState;

        public Restaurant mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.headerContainer);
            mDetailsView = view.findViewById(R.id.restaurantDetailsContainer);
            mImageView = view.findViewById(R.id.restaurantImage);
            mNameView = view.findViewById(R.id.restaurantName);
            mCity = view.findViewById(R.id.city);
            mState = view.findViewById(R.id.state);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }

    private Context getContext() {
        return context;
    }
}
