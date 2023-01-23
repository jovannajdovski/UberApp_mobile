package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.adapters.ReviewAdapter;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ReviewRideDetailActivity extends AppCompatActivity {

    private RideNoStatusDTO ride;
    private ReviewsForRideDTO reviews;
    private boolean canRate;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected RideDetailForPassengerActivity.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected ReviewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<FullReviewDTO> mDataset;
    private List<PassengerDetailsDTO> passengers;
    private int DATASET_COUNT;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_ride_detail);

        ride = (RideNoStatusDTO) getIntent().getSerializableExtra("ride");
        reviews = (ReviewsForRideDTO) getIntent().getSerializableExtra("reviews");

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_baseline_rate_review_24);
        actionBar.setTitle("Ride reviews");

        String canRateExtra = getIntent().getStringExtra("canRate");
        if (canRateExtra.equals("true")) canRate = true; else canRate = false;

        DATASET_COUNT = reviews.getReviews().size();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(this);

        mCurrentLayoutManagerType = RideDetailForPassengerActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        initReviews();

        Button leaveReview = findViewById(R.id.leave_review_fab);
        if (canRate){
            leaveReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm");
//                LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
                    ZonedDateTime zdt = ZonedDateTime.parse(ride.getStartTime());
                    LocalDateTime ldt = zdt.toLocalDateTime();
                    if (ldt.plusDays(3).isBefore(LocalDateTime.now())){
                        Toast.makeText(ReviewRideDetailActivity.this, "Your time for leaving review has expired", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (FullReviewDTO review: reviews.getReviews()){
                        if (Objects.equals(review.getVehicleReview().getPassenger().getId(), LoggedUser.getUserId())){
                            Toast.makeText(ReviewRideDetailActivity.this, "You already leave review and rating", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    Intent intent = new Intent(ReviewRideDetailActivity.this, LeaveReviewForRideActivity.class);
                    intent.putExtra("ride", ride);
                    intent.putExtra("reviews", reviews);
                    startActivity(intent);
                }
            });
        } else {
            leaveReview.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initReviews() {
        mDataset = reviews.getReviews();

        mAdapter = new ReviewAdapter(mDataset);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
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