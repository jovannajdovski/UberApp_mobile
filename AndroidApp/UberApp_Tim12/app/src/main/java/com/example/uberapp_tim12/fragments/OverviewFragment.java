package com.example.uberapp_tim12.fragments;

import android.annotation.SuppressLint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.PassengerMainActivity;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.RideService;
import com.example.uberapp_tim12.web_socket.STOMPUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    private PassengerMainActivity activity;
    private CreateRideDTO ride;
    private List<String> passengers;
    private TextView title;
    private TextView new_ride_departure;
    private TextView new_ride_destination;
    private TextView cost;
    private TextView time;
    private TextView driver;
    private TextView friendsTitle;
    private RelativeLayout grid;

    public OverviewFragment(CreateRideDTO ride,List<String> passengers) {
        this.ride=ride;
        this.passengers=passengers;
    }

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected OverviewFragment.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public static OverviewFragment newInstance(CreateRideDTO ride, List<String> passengers) {

        OverviewFragment fragment = new OverviewFragment(ride,passengers);
        return fragment;
    }

    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=(PassengerMainActivity) getActivity();
        Intent intent=new Intent(getActivity(), RideService.class);
        intent.putExtra("ride",ride);
        intent.putExtra("endpoint", "createRide");
        getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_overview, container, false);

        title=view.findViewById(R.id.new_ride_title);
        new_ride_departure= view.findViewById(R.id.new_ride_departure);
        new_ride_destination=view.findViewById(R.id.new_ride_destination);
        cost=view.findViewById(R.id.cost);
        time=view.findViewById(R.id.time);
        driver=view.findViewById(R.id.driver);
        grid=view.findViewById(R.id.grid);
        friendsTitle=view.findViewById(R.id.friends_title);
        Button back=(Button) view.findViewById(R.id.back_to_home);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(0,ride,null);
            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = OverviewFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (OverviewFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        String[] dataSetArray = new String[passengers.size()];
        passengers.toArray(dataSetArray);
        mAdapter = new CustomAdapter(dataSetArray);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
    public void setRecyclerViewLayoutManager(OverviewFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = OverviewFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = OverviewFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = OverviewFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(createRideReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(createRideReceiver, new IntentFilter("createRide"));
    }

    public BroadcastReceiver createRideReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","lalalalaalla");
            RideFullDTO ride= (RideFullDTO) intent.getSerializableExtra("ride");
            if(ride==null)
            {
                title.setText("The ride is not possible");
                grid.setVisibility(View.GONE);
                friendsTitle.setText("");
                Toast.makeText(getActivity(),intent.getStringExtra("responseMessage"),Toast.LENGTH_SHORT).show();
            }
            else{

                title.setText("The ride is scheduled");
                new_ride_departure.setText(ride.getLocations().iterator().next().getDeparture().getAddress());
                new_ride_destination.setText(ride.getLocations().iterator().next().getDestination().getAddress());
                cost.setText(String.valueOf(Math.round(ride.getTotalCost())));
                String dateTime=ride.getStartTime();
                time.setText(dateTime.substring(dateTime.indexOf('T')+1,dateTime.indexOf('T')+6));
                //time.setText(String.valueOf(ride.getStartTime().getHour()).concat(" : ").concat(String.valueOf(ride.getStartTime().getMinute())));

                driver.setText(String.valueOf(ride.getDriver()));

                createRideSocket(LoggedUser.getUserId(),ride.getId());
            }
        }
    };
    private STOMPUtils stompUtils;
    @SuppressLint("CheckResult")
    public void createRideSocket(Integer userId, Integer rideId){
        stompUtils = new STOMPUtils();
        stompUtils.connectStomp();
        stompUtils.stompClient.send("api/socket-subscriber/"+userId+"/new-ride/"+rideId)
                .compose(stompUtils.applySchedulers())
                .subscribe(() -> {
                    Log.d("RIDECHAT", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RIDECHAT", "Error send STOMP echo", throwable);
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stompUtils.disconnectStomp();
    }
}