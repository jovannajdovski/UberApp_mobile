package com.example.uberapp_tim12.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.fragments.PassengerReportRideCostFragment;
import com.example.uberapp_tim12.fragments.PassengerReportRideCountFragment;
import com.example.uberapp_tim12.fragments.PassengerReportRideDistanceFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PassengerReportActivity extends AppCompatActivity {
    BottomNavigationView statisticNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_report);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Report");


        statisticNavigation = findViewById(R.id.bottom_statistic_navigation);
        statisticNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Rides":
                        changeToCountStatistic();
                        return true;
                    case "Distance":
                        changeToDistanceStatistic();
                        return true;
                    case "Money":
                        changeToMoneySpentStatistic();
                        return true;
                }
                return false;
            }
        });

        changeToCountStatistic();
    }

    private void changeToCountStatistic() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.statistic_fragment, new PassengerReportRideCountFragment());
        ft.commit();
    }

    private void changeToDistanceStatistic() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.statistic_fragment, new PassengerReportRideDistanceFragment());
        ft.commit();
    }

    private void changeToMoneySpentStatistic() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.statistic_fragment, new PassengerReportRideCostFragment());
        ft.commit();
    }

}
