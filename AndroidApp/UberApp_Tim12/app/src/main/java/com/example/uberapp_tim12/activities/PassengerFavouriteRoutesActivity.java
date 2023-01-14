package com.example.uberapp_tim12.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.FavouriteRidesAdapter;
import com.example.uberapp_tim12.model.FavouriteRide;

import java.util.ArrayList;
import java.util.List;

public class PassengerFavouriteRoutesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_favourite_routes);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Favourite rides");

        List<FavouriteRide> items = new ArrayList<>();
        items.add(new FavouriteRide(
                "111 Elliot Mountain Suite 763",
                "81 Christiansen Point Suite 129",
                "Pere perica 1",
                new ArrayList<>()));
        items.add(new FavouriteRide(
                "111 Elliot Mountain Suite 763",
                "81 Christiansen Point Suite 129",
                "Pere perica 1",
                new ArrayList<>()));
        items.add(new FavouriteRide(
                "111 Elliot Mountain Suite 763",
                "81 Christiansen Point Suite 129",
                "Pere perica 1",
                new ArrayList<>()));
        items.add(new FavouriteRide(
                "111 Elliot Mountain Suite 763",
                "81 Christiansen Point Suite 129",
                "Pere perica 1",
                new ArrayList<>()));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new FavouriteRidesAdapter(PassengerFavouriteRoutesActivity.this, items));
    }
}
