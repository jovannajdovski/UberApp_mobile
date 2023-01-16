package com.example.uberapp_tim12.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.PassengerMainActivity;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.service.RideService;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmationFragment extends Fragment {

    private PassengerMainActivity activity;
    private CreateRideDTO ride;
    private List<String> passengers;

    public ConfirmationFragment(CreateRideDTO ride, List<String> passengers) {
        this.ride = ride;
        this.passengers = passengers;
    }

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected ConfirmationFragment.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public static ConfirmationFragment newInstance(CreateRideDTO ride, List<String> passengers) {

        ConfirmationFragment fragment = new ConfirmationFragment(ride, passengers);
        return fragment;
    }

    public ConfirmationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent=new Intent(getActivity(), RideService.class);
        //getActivity().startService(intent);
        //isprobavanje servisa
        activity = (PassengerMainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmation, container, false);
        Button confirm = (Button) view.findViewById(R.id.confirm_button);
        TextView new_ride_departure = view.findViewById(R.id.new_ride_departure);
        TextView new_ride_destination = view.findViewById(R.id.new_ride_destination);
        TextView new_ride_child = view.findViewById(R.id.new_ride_child);
        TextView new_ride_pets = view.findViewById(R.id.new_ride_pets);
        TextView new_ride_type = view.findViewById(R.id.vehicle_type);
        TextView new_ride_time = view.findViewById(R.id.new_ride_time);

        new_ride_departure.setText(ride.getLocations().iterator().next().getDeparture().getAddress());
        new_ride_destination.setText(ride.getLocations().iterator().next().getDestination().getAddress());
        new_ride_child.setText(String.valueOf(ride.isBabyTransport()));
        new_ride_pets.setText(String.valueOf(ride.isPetTransport()));
        new_ride_type.setText(String.valueOf(ride.getVehicleType()));
        String dateTime=ride.getScheduledTime();
        new_ride_time.setText(dateTime.substring(dateTime.indexOf('T')+1,dateTime.indexOf('T')+6));


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(5, ride, passengers);
            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = ConfirmationFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (ConfirmationFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        String[] dataSetArray = new String[passengers.size()];
        passengers.toArray(dataSetArray);
        mAdapter = new CustomAdapter(dataSetArray);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

    public void setRecyclerViewLayoutManager(ConfirmationFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = ConfirmationFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ConfirmationFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = ConfirmationFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }
}
//    public BroadcastReceiver bReceiver = new BroadcastReceiver(){
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String dobijeno=intent.getStringExtra("pas");
//            Log.d("PASSSss", dobijeno);
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bReceiver, new IntentFilter("ihor"));
//    }
//}