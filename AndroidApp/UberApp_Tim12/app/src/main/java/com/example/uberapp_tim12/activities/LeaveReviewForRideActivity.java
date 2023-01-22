package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.ReviewRequestDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.fragments.PassengerRideHistoryFragment;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.ReviewService;

import java.util.List;

public class LeaveReviewForRideActivity extends AppCompatActivity {

    private RideNoStatusDTO ride;
    private ReviewsForRideDTO reviews;

    RatingBar driverRating, vehicleRating;
    EditText descriptionDriver, descriptionCar;
    Button submit,skip;

    private boolean driverSent, vehicleSent;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_review_for_ride);

        ride = (RideNoStatusDTO) getIntent().getSerializableExtra("ride");
        reviews = (ReviewsForRideDTO) getIntent().getSerializableExtra("reviews");

        driverSent = false;
        vehicleSent = false;

        driverRating =  findViewById(R.id.driver_rating);
        vehicleRating = findViewById(R.id.vehicle_rating);
        submit = findViewById(R.id.submit);
        descriptionDriver = findViewById(R.id.review_message_driver);
        descriptionCar = findViewById(R.id.review_message_car);
        skip = findViewById(R.id.skip);

        submit.setEnabled(false);

        driverRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (vehicleRating.getRating() != 0)
                submit.setEnabled(true);

        });

        vehicleRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (driverRating.getRating() != 0)
                submit.setEnabled(true);

        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Float driverRatingVal = driverRating.getRating();
                Float vehicleRatingVal = vehicleRating.getRating();
                String commentDriver = String.valueOf(descriptionDriver.getText());
                String commentCar = String.valueOf(descriptionCar.getText());

                Intent intentDriverReview = new Intent(LeaveReviewForRideActivity.this, ReviewService.class);
                intentDriverReview.putExtra("endpoint", "leaveReviewForDriver");
                intentDriverReview.putExtra("idRide", ride.getId());
                intentDriverReview.putExtra("reviewRequest", new ReviewRequestDTO(driverRatingVal.doubleValue(),commentDriver));
                LeaveReviewForRideActivity.this.startService(intentDriverReview);

                Intent intentVehicleReview = new Intent(LeaveReviewForRideActivity.this, ReviewService.class);
                intentVehicleReview.putExtra("endpoint", "leaveReviewForVehicle");
                intentVehicleReview.putExtra("idRide", ride.getId());
                intentVehicleReview.putExtra("reviewRequest", new ReviewRequestDTO(vehicleRatingVal.doubleValue(),commentCar));
                LeaveReviewForRideActivity.this.startService(intentVehicleReview);
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public BroadcastReceiver reviewsDriverReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                driverSent = true;
                if (vehicleSent){
                    Toast.makeText(LeaveReviewForRideActivity.this, "Review sent, thank you", Toast.LENGTH_SHORT).show();
                    Intent intentHistory = new Intent(LeaveReviewForRideActivity.this, PassengerRideHistoryActivity.class);
                    startActivity(intentHistory);
                }

            } else {
                Toast.makeText(LeaveReviewForRideActivity.this, "You can't leave driver review, try again", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public BroadcastReceiver reviewsVehicleReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                vehicleSent = true;
                if (driverSent){
                    Toast.makeText(LeaveReviewForRideActivity.this, "Review sent, thank you", Toast.LENGTH_SHORT).show();
                    Intent intentHistory = new Intent(LeaveReviewForRideActivity.this, PassengerRideHistoryActivity.class);
                    startActivity(intentHistory);
                }

            } else {
                Toast.makeText(LeaveReviewForRideActivity.this, "You can't leave driver review, try again", Toast.LENGTH_SHORT).show();
            }

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(reviewsVehicleReceiver, new IntentFilter("reviewForVehicleLeave"));
        LocalBroadcastManager.getInstance(this).registerReceiver(reviewsDriverReceiver, new IntentFilter("reviewForDriverLeave"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reviewsVehicleReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(reviewsDriverReceiver);
    }
}