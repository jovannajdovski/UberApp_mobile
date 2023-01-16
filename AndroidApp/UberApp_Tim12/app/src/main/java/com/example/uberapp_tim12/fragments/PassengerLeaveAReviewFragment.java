package com.example.uberapp_tim12.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.fragment.app.Fragment;

import com.example.uberapp_tim12.R;

public class PassengerLeaveAReviewFragment extends Fragment {
    RatingBar driverRating, vehicleRating;
    EditText description;
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_leave_a_review, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driverRating = view.findViewById(R.id.driver_rating);
        vehicleRating = view.findViewById(R.id.vehicle_rating);
        submit = view.findViewById(R.id.submit);
        description = view.findViewById(R.id.review_message);

        submit.setEnabled(false);

        driverRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (vehicleRating.getRating() != 0)
                submit.setEnabled(true);

        });

        vehicleRating.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (driverRating.getRating() != 0)
                submit.setEnabled(true);

        });

        submit.setOnClickListener(view1 -> {
            Float driverRatingVal = driverRating.getRating();
            Float vehicleRatingVal = vehicleRating.getRating();
            String message = String.valueOf(description.getText());
        });
    }
}
