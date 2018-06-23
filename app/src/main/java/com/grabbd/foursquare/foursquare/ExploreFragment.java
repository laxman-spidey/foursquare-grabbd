package com.grabbd.foursquare.foursquare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    public static final int REQUEST_CODE_AUTOCOMPLETE = 201;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 202;

    private OnFragmentInteractionListener mListener;

    private SearchBar searchBar;
    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        setupSearchBar(view);
        addRestaurantListFragment();
        return view;
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
                restaurantListFragment.filter(location);
            }

            @Override
            public void onLocationSelected(double lat, double lng) {

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
