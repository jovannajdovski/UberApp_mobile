package com.example.uberapp_tim12.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.RideDetailForPassengerActivity;
import com.example.uberapp_tim12.adapters.RideAdapter;
import com.example.uberapp_tim12.dto.FullReviewList;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.model_mock.Ride;
import com.example.uberapp_tim12.tools.MockupData;

import java.util.List;


public class PassengerRideHistoryFragment extends ListFragment {

    private View view;
    private List<RideNoStatusDTO> rides;
    private List<ReviewsForRideDTO> fullReviewList;

    public static PassengerRideHistoryFragment newInstance(List<RideNoStatusDTO> rides, List<ReviewsForRideDTO> fullReviewList) {
        return new PassengerRideHistoryFragment(rides, fullReviewList);
    }

    public PassengerRideHistoryFragment(List<RideNoStatusDTO> rides, List<ReviewsForRideDTO> fullReviewList){
        this.rides = rides;
        this.fullReviewList = fullReviewList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_passenger_ride_history, vg, false);
        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        ListView listView = getListView();
        listView.setDivider(null);
        listView.setDividerHeight(0);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        RideNoStatusDTO ride = rides.get(position);
        ReviewsForRideDTO reviews = fullReviewList.get(position);

        Intent intent = new Intent(getActivity(), RideDetailForPassengerActivity.class);
        intent.putExtra("ride", ride);
        intent.putExtra("reviews", reviews);

        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RideAdapter adapter = new RideAdapter(getActivity(), rides, fullReviewList);
        setListAdapter(adapter);
    }
}