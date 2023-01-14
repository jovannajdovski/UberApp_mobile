package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.PassengerAdapter;
import com.example.uberapp_tim12.model_mock.Passenger;
import com.example.uberapp_tim12.model_mock.Ride;

import java.util.List;

public class RideDetailForPassengerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail_passenger);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_about);
        actionBar.setTitle("Ride details");

        TextView rate = findViewById(R.id.rate);
        TextView date = findViewById(R.id.startDate);
        TextView startTime = findViewById(R.id.start_time);
        TextView startPlace = findViewById(R.id.start_place);
        TextView endTime = findViewById(R.id.end_time);
        TextView endPlace = findViewById(R.id.end_place);

        TextView distance = findViewById(R.id.distance);
        TextView price = findViewById(R.id.price);
        TextView passengersNum = findViewById(R.id.passengers);

        TextView driverName = findViewById(R.id.name);
        TextView driverPhone = findViewById(R.id.phone_number);

        Ride ride = (Ride)getIntent().getSerializableExtra("ride");

        if (ride.getRate() != -1){
            rate.setText(""+ride.getRate());
        } else {
            rate.setText("Rate your ride");
            rate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRateDialog(rate);
                }
            });
        }
        date.setText(ride.getStartDate());
        startTime.setText(ride.getStartTime());
        startPlace.setText(ride.getStartPlace());
        endTime.setText(ride.getEndTime());
        endPlace.setText(ride.getEndPlace());
        distance.setText("Distance "+ride.getDistance()+" km");
        price.setText("Price "+ride.getPrice()+" RSD");
        passengersNum.setText("Passengers: "+ride.getPassangers().size());
        driverName.setText(ride.getDriver().getName()+" "+ride.getDriver().getSurname());
        driverPhone.setText(ride.getDriver().getPhoneNumber());

        List<Passenger> passengers = ride.getPassangers();
        PassengerAdapter adapter = new PassengerAdapter(this, passengers);
        ListView listView = (ListView) findViewById(R.id.passenger_list);


//        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_ride_detail, null, false);
//        listView.addFooterView(footerView);

        listView.setAdapter(adapter);

        Button offersButton = findViewById(R.id.offers_for_route);
        Button favRouteButton = findViewById(R.id.favorite_route);

        offersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RideDetailForPassengerActivity.this,"There is no offers for this route",Toast.LENGTH_SHORT).show();
            }
        });

        favRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RideDetailForPassengerActivity.this,"Route marked as favorite",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showRateDialog(TextView view){
        AlertDialog.Builder popDialog = new AlertDialog.Builder(this);

        popDialog.setIcon(R.drawable.ic_baseline_star);
        popDialog.setTitle("Rate your drive");

//        RatingBar rating = new RatingBar(this);
//        rating.setNumStars(5);
//        rating.setMax(5);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        popDialog.setView(rating);

        popDialog.setView(R.layout.rating_dialog);
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        FrameLayout layout= (FrameLayout) vi.inflate(R.layout.rating_dialog, null);

        RatingBar rating = (RatingBar) layout.findViewById(R.id.rating_bar);

        popDialog.setPositiveButton("Rate",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        view.setText(String.valueOf(rating.getProgress()));
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
        popDialog.create();
        popDialog.show();
    }
}