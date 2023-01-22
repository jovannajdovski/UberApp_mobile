package com.example.uberapp_tim12.holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;

public class FavouriteRideHolder extends RecyclerView.ViewHolder {
    public TextView title, departure, destination, passengers;
    public Button dropDownButton;

    public FavouriteRideHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.favourite_title);
        departure = itemView.findViewById(R.id.departure_text);
        destination = itemView.findViewById(R.id.destination_text);
        passengers = itemView.findViewById(R.id.friends);

        dropDownButton = itemView.findViewById(R.id.favourite_route_options);
    }

}
