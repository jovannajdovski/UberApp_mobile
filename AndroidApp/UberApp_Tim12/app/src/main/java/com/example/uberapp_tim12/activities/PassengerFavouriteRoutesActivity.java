package com.example.uberapp_tim12.activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.FavouriteRidesAdapter;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.RideController;
import com.example.uberapp_tim12.model.FavouriteRide;
import com.example.uberapp_tim12.security.LoggedUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PassengerFavouriteRoutesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_favourite_routes);

        initializeToolbar();
        initializeActionBar();
        initializeRecyclerView();
        getFavouriteRides();
    }

    private void initializeToolbar() {
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initializeActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Favourite rides");
    }

    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getFavouriteRides() {
        Retrofit retrofit = ControllerUtils.retrofit;
        RideController controller = retrofit.create(RideController.class);

        Call<List<FavouriteRide>> call = controller.getFavoritesForPassenger(LoggedUser.getUserId(), LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<List<FavouriteRide>>() {
            @Override
            public void onResponse(Call<List<FavouriteRide>> call, Response<List<FavouriteRide>> response) {
                if (response.code() == 200) {
                    List<FavouriteRide> favouriteRides = response.body();
                    showFavouriteRides(favouriteRides);
                }
            }

            @Override
            public void onFailure(Call<List<FavouriteRide>> call, Throwable t) {
            }
        });
    }

    private void showFavouriteRides(List<FavouriteRide> favouriteRides) {
        FavouriteRidesAdapter recyclerAdapter = new FavouriteRidesAdapter(PassengerFavouriteRoutesActivity.this, favouriteRides);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
