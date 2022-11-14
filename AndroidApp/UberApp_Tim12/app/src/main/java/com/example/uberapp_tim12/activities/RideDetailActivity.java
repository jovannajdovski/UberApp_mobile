package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.PassengerAdapter;
import com.example.uberapp_tim12.model.Passenger;
import com.example.uberapp_tim12.model.Ride;

import java.util.ArrayList;
import java.util.List;

public class RideDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView rate = findViewById(R.id.rate);
        TextView date = findViewById(R.id.startDate);
        TextView startTime = findViewById(R.id.start_time);
        TextView startPlace = findViewById(R.id.start_place);
        TextView endTime = findViewById(R.id.end_time);
        TextView endPlace = findViewById(R.id.end_place);

        TextView distance = findViewById(R.id.distance);
        TextView price = findViewById(R.id.price);
        TextView passengersNum = findViewById(R.id.passengers);

        Ride ride = (Ride)getIntent().getSerializableExtra("ride");

        if (ride.getRate() != -1){
            rate.setText(""+ride.getRate());
        } else {
            rate.setText("Not rated");
        }
        date.setText(ride.getStartDate());
        startTime.setText(ride.getStartTime());
        startPlace.setText(ride.getStartPlace());
        endTime.setText(ride.getEndTime());
        endPlace.setText(ride.getEndPlace());
        distance.setText("Distance "+ride.getDistance()+" km");
        price.setText("Price "+ride.getPrice()+" RSD");
        passengersNum.setText("Passengers: "+ride.getPassangers().size());

        List<Passenger> passengers = ride.getPassangers();
        PassengerAdapter adapter = new PassengerAdapter(this, passengers);
        ListView listView = (ListView) findViewById(R.id.passenger_list);
        listView.setAdapter(adapter);
    }
}