package com.grabbd.foursquare.foursquare;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseFragment extends Fragment {
    public static String TAG = BaseFragment.class.getSimpleName();
    RelativeLayout layoutContainer;
    RelativeLayout.LayoutParams layoutParams;
    ProgressBar progressBar;
    RelativeLayout errorView;
    RelativeLayout noDataView;
    RelativeLayout initView;
    View successView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TAG = this.getClass().getName();
        View view = inflater.inflate(R.layout.base_fragment, container, false);
        layoutContainer = view.findViewById(R.id.layoutContainer);
        layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        progressBar = view.findViewById(R.id.inbound_progressbar);
        errorView = view.findViewById(R.id.errorView);
        noDataView = view.findViewById(R.id.noDataLayout);
        initView = view.findViewById(R.id.initView);

        return view;
    }

    public void inProgress() {
        Log.i(TAG, "inProgress()");
        layoutContainer.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        initView.setVisibility(View.GONE);
    }

    public void setInitViewText(String text) {
        TextView initText = initView.findViewById(R.id.initText);
        initText.setText(text);
        initialize();
    }

    public void initialize() {
        layoutContainer.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        initView.setVisibility(View.VISIBLE);
    }
    public void setSuccessView(View view)
    {
        successView = view;
        layoutContainer.addView(successView);
    }

    public void onError() {
        Log.i(TAG, "onError()");
        layoutContainer.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        initView.setVisibility(View.GONE);
    }

    public void onSuccess() {
        Log.i(TAG, "onSuccess()");
        layoutContainer.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        initView.setVisibility(View.GONE);
    }

    public void onNoDataFound() {
        Log.i(TAG, "onNoDataFound()");
        layoutContainer.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        initView.setVisibility(View.GONE);
    }

}
