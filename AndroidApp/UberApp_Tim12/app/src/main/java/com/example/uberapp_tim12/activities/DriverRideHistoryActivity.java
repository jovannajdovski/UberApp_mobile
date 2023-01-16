package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.fragments.DriverRideHistoryFragment;
import com.example.uberapp_tim12.tools.FragmentTransition;

public class DriverRideHistoryActivity extends AppCompatActivity {

    private FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_history);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_history_24);
        actionBar.setTitle("Ride history");

        manager = getSupportFragmentManager();

        DriverRideHistoryFragment driverRideHistoryFragment = new DriverRideHistoryFragment();
        manager.beginTransaction().replace(R.id.mainContent, driverRideHistoryFragment,driverRideHistoryFragment.getTag()).commit();

    }


}