package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.AcceptedRideAdapter;
import com.example.uberapp_tim12.adapters.ReviewAdapter;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.ReviewService;
import com.example.uberapp_tim12.service.RideService;

import java.util.ArrayList;
import java.util.List;

public class DriverAcceptedRidesActivity extends AppCompatActivity {

    private List<RideNoStatusDTO> rides;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected RideDetailForPassengerActivity.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected AcceptedRideAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<RideNoStatusDTO> mDataset;
    private int DATASET_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_accepted_rides);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_check_circle_24);
        actionBar.setTitle("Accepted rides");

        Intent intentRide = new Intent(this, RideService.class);
        intentRide.putExtra("endpoint", "getAcceptedRidesForDriver");
        this.startService(intentRide);
    }

    public BroadcastReceiver acceptedRidesReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                RidePageList ridePageList = (RidePageList) intent.getSerializableExtra("ridesList");

                if (ridePageList.getTotalCount() != 0) {
                    rides = ridePageList.getResults();
                    DATASET_COUNT = rides.size();

                    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

                    mLayoutManager = new LinearLayoutManager(DriverAcceptedRidesActivity.this);

                    mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

                    setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

                    initRides();
                } else {
                    rides = new ArrayList<>();
                }

            }else {
                Toast.makeText(DriverAcceptedRidesActivity.this,"No accepted rides",Toast.LENGTH_SHORT).show();
                rides = new ArrayList<>();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(acceptedRidesReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(acceptedRidesReceiver, new IntentFilter("acceptedRides"));
    }

    private void initRides() {
        mDataset = rides;

        mAdapter = new AcceptedRideAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setRecyclerViewLayoutManager(RideDetailForPassengerActivity.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, 2);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

}