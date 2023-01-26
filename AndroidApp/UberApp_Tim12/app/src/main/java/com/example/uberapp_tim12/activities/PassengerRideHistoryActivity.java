package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.FullReviewList;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.fragments.DriverRideHistoryFragment;
import com.example.uberapp_tim12.fragments.PassengerRideHistoryFragment;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.ReviewService;
import com.example.uberapp_tim12.tools.FragmentTransition;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

public class PassengerRideHistoryActivity extends AppCompatActivity {

    private FragmentManager manager;
    private RidePageList ridePageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_history);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_history_24);
        actionBar.setTitle("Ride history");

        manager = getSupportFragmentManager();

        Intent intentRides = new Intent(this, PassengerService.class);
        intentRides.putExtra("endpoint", "getPastRides");
        this.startService(intentRides);
    }

    public BroadcastReceiver ridesReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                ridePageList = (RidePageList) intent.getSerializableExtra("ridesListDTOS");

                if (ridePageList.getTotalCount() != 0) {
                    ArrayList<Integer> idRides = new ArrayList<>();
                    for (RideNoStatusDTO ride : ridePageList.getResults()){
                        idRides.add(ride.getId());
                    }

                    Intent intentReviews = new Intent(PassengerRideHistoryActivity.this, ReviewService.class);
                    intentReviews.putExtra("endpoint", "getReviewsForMultipleRide");
                    intentReviews.putIntegerArrayListExtra("idRides", idRides );
                    PassengerRideHistoryActivity.this.startService(intentReviews);
                }

            }

        }
    };

    public BroadcastReceiver reviewsReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                List<ReviewsForRideDTO> fullReviewList = (List<ReviewsForRideDTO>) intent.getSerializableExtra("reviews");

                PassengerRideHistoryFragment passengerRideHistoryFragment = new PassengerRideHistoryFragment(ridePageList.getResults(), fullReviewList);
                manager.beginTransaction().replace(R.id.mainContent, passengerRideHistoryFragment, passengerRideHistoryFragment.getTag()).commit();

            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(ridesReceiver, new IntentFilter("pastRides"));
        LocalBroadcastManager.getInstance(this).registerReceiver(reviewsReceiver, new IntentFilter("reviewsForMultipleRide"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(ridesReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reviewsReceiver);
    }
}