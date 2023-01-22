package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.adapters.PassengerAdapter;
import com.example.uberapp_tim12.dto.CreateFavoriteDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.FavoriteFullDTO;
import com.example.uberapp_tim12.dto.FavoriteRouteForPassengerDTO;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.fragments.PassengerCurrRideFragment;
import com.example.uberapp_tim12.fragments.RideMapRouteFragment;
import com.example.uberapp_tim12.model_mock.Passenger;
import com.example.uberapp_tim12.model_mock.Ride;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.service.FavoriteService;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.tools.UserMockup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class RideDetailForPassengerActivity extends AppCompatActivity {

    private RideNoStatusDTO ride;
    private ReviewsForRideDTO reviews;
    private FragmentManager manager;

    private TextView driverName;
    private TextView driverPhone;
    private TextView distanceTxt;

    private boolean isFavorite;
    private Integer favoriteId;
    private ImageView favoriteIcon;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected RideDetailForPassengerActivity.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private List<PassengerDetailsDTO> passengers;
    private int DATASET_COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_detail_passenger);

        ride = (RideNoStatusDTO) getIntent().getSerializableExtra("ride");
        reviews = (ReviewsForRideDTO) getIntent().getSerializableExtra("reviews");

        Log.d("FFFF", ride.getPassengers().size()+"");

        manager = getSupportFragmentManager();


        RideMapRouteFragment rideMapRouteFragment = new RideMapRouteFragment(ride);
        manager.beginTransaction().replace(R.id.mapContent, rideMapRouteFragment, rideMapRouteFragment.getTag()).commit();

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_about);
        actionBar.setTitle("Ride details");

        TextView rate = findViewById(R.id.rate);
        TextView date = findViewById(R.id.startDate);
        TextView startTimes = findViewById(R.id.start_time);
        TextView startPlace = findViewById(R.id.start_place);
        TextView endTimes = findViewById(R.id.end_time);
        TextView endPlace = findViewById(R.id.end_place);

        distanceTxt = findViewById(R.id.distance);
        TextView price = findViewById(R.id.price);

        driverName = findViewById(R.id.driver_name);
        driverPhone = findViewById(R.id.phone_number);


        rate.setText(getAverage());

        String[] startDateTime = ride.getStartTime().split("T");
        String[] datePoints = startDateTime[0].split("-");
        String startDate = datePoints[2] + "." + datePoints[1] + "." + datePoints[0] + ".";
        String[] timePoints = startDateTime[1].split(":");
        String startTime = timePoints[0] + ":" + timePoints[1];

        String[] endDateTime = ride.getEndTime().split("T");
        timePoints = endDateTime[1].split(":");
        String endTime = timePoints[0] + ":" + timePoints[1];

        PathDTO path = (PathDTO) ride.getLocations().toArray()[0];


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RideDetailForPassengerActivity.this, ReviewRideDetailActivity.class);
                intent.putExtra("ride", ride);
                intent.putExtra("reviews", reviews);

                startActivity(intent);
            }
        });

        date.setText(startDate);
        startTimes.setText(startTime);
        startPlace.setText(path.getDeparture().getAddress());
        endTimes.setText(endTime);
        endPlace.setText(path.getDestination().getAddress());

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);

        String estimatedTime = nf.format(ride.getEstimatedTimeInMinutes()) + "min";
        String totalCost = nf.format(ride.getTotalCost()) + " RSD";

        price.setText("Price " + totalCost);

        Intent intentDriver = new Intent(this, DriverService.class);
        intentDriver.putExtra("endpoint", "getDriverDetails");
        intentDriver.putExtra("driverId", ride.getDriver().getId());
        this.startService(intentDriver);


        List<Integer> passengersId = new ArrayList<>();
        for (UserRideDTO passenger : ride.getPassengers()) {
            passengersId.add(passenger.getId());
        }
        DATASET_COUNT = passengersId.size();

        passengers = new ArrayList<>();
        for (Integer passengerId : passengersId) {
            Intent intentPassengers = new Intent(this, PassengerService.class);
            intentPassengers.putExtra("endpoint", "getPassengerDetails");
            intentPassengers.putExtra("passengerId", passengerId);
            this.startService(intentPassengers);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


        setUpFavorite();

        View inboxView = findViewById(R.id.inbox);

        inboxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //:TODO
                Toast.makeText(RideDetailForPassengerActivity.this, "Inbox for this ride", Toast.LENGTH_SHORT).show();
            }
        });


        View offersView = findViewById(R.id.offers_for_route);

        offersView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent offerIntent = new Intent(RideDetailForPassengerActivity.this, PassengerMainActivity.class);
                offerIntent.putExtra("offer", "true");
                offerIntent.putExtra("start", ride.getLocations().iterator().next().getDeparture().getAddress());
                offerIntent.putExtra("end", ride.getLocations().iterator().next().getDestination().getAddress());

                offerIntent.putExtra("startLat", ride.getLocations().iterator().next().getDeparture().getLatitude());
                offerIntent.putExtra("endLat", ride.getLocations().iterator().next().getDestination().getLatitude());
                offerIntent.putExtra("startLon", ride.getLocations().iterator().next().getDeparture().getLongitude());
                offerIntent.putExtra("endLon", ride.getLocations().iterator().next().getDestination().getLongitude());
                startActivity(offerIntent);
            }
        });

    }

    private void setUpFavorite() {
        Intent intentFav = new Intent(RideDetailForPassengerActivity.this, FavoriteService.class);
        intentFav.putExtra("endpoint", "isFavorite");
        intentFav.putExtra("from", ride.getLocations().iterator().next().getDeparture().getAddress());
        intentFav.putExtra("to", ride.getLocations().iterator().next().getDestination().getAddress());
        this.startService(intentFav);

        favoriteIcon = findViewById(R.id.favorite_icon);
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFavorite = new Intent(RideDetailForPassengerActivity.this, FavoriteService.class);
                if (isFavorite){
                    intentFavorite.putExtra("endpoint", "deleteFavorite");
                    intentFavorite.putExtra("favoriteId", favoriteId);
                } else{
                    intentFavorite.putExtra("endpoint", "createFavorite");
                    CreateFavoriteDTO createFavoriteDTO = new CreateFavoriteDTO(ride.getLocations().iterator().next().getDeparture().getAddress() + " - " +
                            ride.getLocations().iterator().next().getDestination().getAddress(),
                            ride.getLocations(),ride.getPassengers(), ride.getVehicleType(),ride.isBabyTransport(), ride.isPetTransport());

                    intentFavorite.putExtra("favorite", createFavoriteDTO);
                }
                RideDetailForPassengerActivity.this.startService(intentFavorite);
            }
        });
    }

    public BroadcastReceiver setFavoriteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                FavoriteRouteForPassengerDTO favoriteRouteForPassengerDTO = (FavoriteRouteForPassengerDTO) intent.getSerializableExtra("favoriteRoute");
                if (favoriteRouteForPassengerDTO.getFavoriteId()>0){
                    favoriteId = favoriteRouteForPassengerDTO.getFavoriteId();
                    isFavorite = true;
                    favoriteIcon.setImageDrawable(ContextCompat.getDrawable(RideDetailForPassengerActivity.this, R.drawable.ic_baseline_favorite_24));
                } else {
                    favoriteId = 0;
                    isFavorite = false;
                }
            } else {
                favoriteId = 0;
                isFavorite = false;
            }

        }
    };

    public BroadcastReceiver createFavoriteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("created");
            if (found.equals("true")){
                FavoriteFullDTO favoriteFullDTO = (FavoriteFullDTO) intent.getSerializableExtra("favoriteRoute");
                    favoriteId = favoriteFullDTO.getId();
                    isFavorite = true;
                    favoriteIcon.setImageDrawable(ContextCompat.getDrawable(RideDetailForPassengerActivity.this, R.drawable.ic_baseline_favorite_24));
                    Toast.makeText(RideDetailForPassengerActivity.this, "Favorite route marked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RideDetailForPassengerActivity.this, "You can't mark this as favorite route", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public BroadcastReceiver deleteFavoriteReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("deleted");
            if (found.equals("true")){
                favoriteId = 0;
                isFavorite = false;
                favoriteIcon.setImageDrawable(ContextCompat.getDrawable(RideDetailForPassengerActivity.this, R.drawable.ic_baseline_favorite_border_24));
                Toast.makeText(RideDetailForPassengerActivity.this, "Favorite route removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RideDetailForPassengerActivity.this, "You can't remove favorite route", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public BroadcastReceiver driverReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DriverDetailsDTO driver = (DriverDetailsDTO) intent.getSerializableExtra("driverDetailsDTO");

            String name = driver.getName();
            String surname = driver.getSurname();

            driverName.setText(name + " " + surname);
            driverPhone.setText(driver.getTelephoneNumber());

        }
    };

    public BroadcastReceiver passengerReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            PassengerDetailsDTO passenger = (PassengerDetailsDTO) intent.getSerializableExtra("passengerDetailsDTO");

            passengers.add(passenger);

            if (passengers.size() == DATASET_COUNT) {
                initPassengers();
            }

        }

    };

    public BroadcastReceiver distanceReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String distance = intent.getStringExtra("distance");
            distanceTxt.setText("Distance " + distance);
        }

    };

    private void initPassengers() {
        mDataset = new String[passengers.size()];
        for (int i = 0; i < passengers.size(); i++) {
            mDataset[i] = passengers.get(i).getName() + " " + passengers.get(i).getSurname();
        }
        mAdapter = new CustomAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(driverReceiver, new IntentFilter("driverDetails"));
        LocalBroadcastManager.getInstance(this).registerReceiver(passengerReceiver, new IntentFilter("passengerDetails"));
        LocalBroadcastManager.getInstance(this).registerReceiver(distanceReceiver, new IntentFilter("updateDistance"));
        LocalBroadcastManager.getInstance(this).registerReceiver(setFavoriteReceiver, new IntentFilter("setFavorite"));
        LocalBroadcastManager.getInstance(this).registerReceiver(createFavoriteReceiver, new IntentFilter("createFavorite"));
        LocalBroadcastManager.getInstance(this).registerReceiver(deleteFavoriteReceiver, new IntentFilter("deleteFavorite"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(driverReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(passengerReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(distanceReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(setFavoriteReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(createFavoriteReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(deleteFavoriteReceiver);
    }



    private String getAverage() {
        if (reviews.getReviews().size() == 0) {
            return "Not rated";
        }
        Double s = 0.0;
        for (FullReviewDTO review : reviews.getReviews()) {
            s += review.getDriverReview().getRating();
            s += review.getVehicleReview().getRating();
        }
        return String.valueOf(s / (reviews.getReviews().size() * 2));
    }

    public void setRecyclerViewLayoutManager(RideDetailForPassengerActivity.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(this, 2);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this);
                mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

}