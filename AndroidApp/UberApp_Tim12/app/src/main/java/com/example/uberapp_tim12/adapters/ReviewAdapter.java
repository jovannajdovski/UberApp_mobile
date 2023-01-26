package com.example.uberapp_tim12.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.ReviewRideDetailActivity;
import com.example.uberapp_tim12.activities.RideDetailForPassengerActivity;
import com.example.uberapp_tim12.dto.FullReviewDTO;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private static final String TAG = "ReviewAdapter";

    private List<FullReviewDTO> mDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView reviewPerson;
        private List<ImageView> driverStars;
        private List<ImageView> carStars;
        private EditText driverComment;
        private EditText carComment;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            reviewPerson = (TextView) v.findViewById(R.id.review_person);
            driverComment = (EditText) v.findViewById(R.id.editTextDriver);
            carComment = (EditText) v.findViewById(R.id.editTextCar);
            driverStars = new ArrayList<>();
            driverStars.add((ImageView) v.findViewById(R.id.star1driver));
            driverStars.add((ImageView) v.findViewById(R.id.star2driver));
            driverStars.add((ImageView) v.findViewById(R.id.star3driver));
            driverStars.add((ImageView) v.findViewById(R.id.star4driver));
            driverStars.add((ImageView) v.findViewById(R.id.star5driver));
            carStars = new ArrayList<>();
            carStars.add((ImageView) v.findViewById(R.id.star1car));
            carStars.add((ImageView) v.findViewById(R.id.star2car));
            carStars.add((ImageView) v.findViewById(R.id.star3car));
            carStars.add((ImageView) v.findViewById(R.id.star4car));
            carStars.add((ImageView) v.findViewById(R.id.star5car));
        }

        public TextView getReviewPerson() {
            return reviewPerson;
        }

        public List<ImageView> getDriverStars() {
            return driverStars;
        }

        public List<ImageView> getCarStars() {
            return carStars;
        }

        public EditText getDriverComment() {
            return driverComment;
        }

        public EditText getCarComment() {
            return carComment;
        }
    }


    public ReviewAdapter(List<FullReviewDTO> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_ride_raw_item, viewGroup, false);

        context = viewGroup.getContext();
        return new ReviewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getReviewPerson().setText(mDataSet.get(position).getVehicleReview().getPassenger().getEmail());
        if (mDataSet.get(position).getDriverReview().getComment()==null){
            viewHolder.getDriverComment().setText("NOT RATED!");
        } else {
            viewHolder.getDriverComment().setText(mDataSet.get(position).getDriverReview().getComment());
        }
        viewHolder.getDriverComment().setKeyListener(null);
        if (mDataSet.get(position).getVehicleReview().getComment()==null){
            viewHolder.getCarComment().setText("NOT RATED!");
        } else {
            viewHolder.getCarComment().setText(mDataSet.get(position).getVehicleReview().getComment());
        }
        viewHolder.getCarComment().setKeyListener(null);

        if (mDataSet.get(position).getDriverReview().getRating() == null) {
            viewHolder.getDriverStars().get(0).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getDriverStars().get(1).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getDriverStars().get(2).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getDriverStars().get(3).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getDriverStars().get(4).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
        } else {
            switch (mDataSet.get(position).getDriverReview().getRating()) {
                case 0:
                    viewHolder.getDriverStars().get(0).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 1:
                    viewHolder.getDriverStars().get(1).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 2:
                    viewHolder.getDriverStars().get(2).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 3:
                    viewHolder.getDriverStars().get(3).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 4:
                    viewHolder.getDriverStars().get(4).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                default:
                    break;
            }
        }
        if (mDataSet.get(position).getVehicleReview().getRating() == null) {
            viewHolder.getCarStars().get(0).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getCarStars().get(1).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getCarStars().get(2).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getCarStars().get(3).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
            viewHolder.getCarStars().get(4).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
        } else {
            switch (mDataSet.get(position).getVehicleReview().getRating()) {
                case 0:
                    viewHolder.getCarStars().get(0).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 1:
                    viewHolder.getCarStars().get(1).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 2:
                    viewHolder.getCarStars().get(2).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 3:
                    viewHolder.getCarStars().get(3).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                case 4:
                    viewHolder.getCarStars().get(4).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star));
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
