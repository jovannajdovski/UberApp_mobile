package com.example.uberapp_tim12.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.RideDetailActivity;
import com.example.uberapp_tim12.adapters.RideAdapter;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.tools.MockupData;

public class DriverRideHistoryFragment extends ListFragment {

    public static DriverRideHistoryFragment newInstance() {
        return new DriverRideHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.driver_ride_history, vg, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Ride ride = MockupData.getRides().get(position);

        Intent intent = new Intent(getActivity(), RideDetailActivity.class);
        intent.putExtra("ride", ride);

        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RideAdapter adapter = new RideAdapter(getActivity());
        setListAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        //inflater.inflate(R.menu.activity_itemdetail, menu); //TODO: ubaci od Savica
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

//        if(id == R.id.action_refresh){
//            Toast.makeText(getActivity(), "Refresh App", Toast.LENGTH_SHORT).show();
//        }
//        if(id == R.id.action_new){
//            Toast.makeText(getActivity(), "Create Text", Toast.LENGTH_SHORT).show();          //TODO: od Savica
//        }
        return super.onOptionsItemSelected(item);
    }
}