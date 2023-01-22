package com.example.uberapp_tim12.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.model_mock.Ride;
import com.example.uberapp_tim12.tools.MockupData;

import java.util.List;

public class RideAdapter extends BaseAdapter {

    private Activity activity;
    private List<RideNoStatusDTO> rides;
    private List<ReviewsForRideDTO> fullReviewList;

    public RideAdapter(Activity activity, List<RideNoStatusDTO> rides, List<ReviewsForRideDTO> fullReviewList) {
        this.activity = activity;
        this.rides = rides;
        this.fullReviewList = fullReviewList;
    }

    @Override
    public int getCount() {
        return rides.size();
    }

    @Override
    public Object getItem(int i) {
        return rides.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        RideNoStatusDTO ride = rides.get(position);
        ReviewsForRideDTO reviews = fullReviewList.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.ride_list, null);

        TextView rate = (TextView)vi.findViewById(R.id.rate);
        TextView rideDateTime = (TextView)vi.findViewById(R.id.ride_date_time);
        TextView startPlace = (TextView)vi.findViewById(R.id.start_place);
        TextView endPlace = (TextView)vi.findViewById(R.id.end_place);

        rate.setText(getAverage(reviews));

        String[] startDateTime = ride.getStartTime().split("T");

        String[] datePoints = startDateTime[0].split("-");
        String startDate = datePoints[2]+"."+datePoints[1]+"."+datePoints[0]+".";
        String[] timePoints = startDateTime[1].split(":");
        String startTime = timePoints[0]+":"+timePoints[1];

        rideDateTime.setText(startDate + " " + startTime);
        PathDTO path = (PathDTO) ride.getLocations().toArray()[0];
        startPlace.setText(path.getDeparture().getAddress());
        endPlace.setText(path.getDestination().getAddress());

        return  vi;
    }

    private String getAverage(ReviewsForRideDTO reviews) {
        if (reviews.getReviews().size()==0){
            return "Not rated";
        }
        Double s = 0.0;
        for (FullReviewDTO review: reviews.getReviews()){
            s+=review.getDriverReview().getRating();
            s+=review.getVehicleReview().getRating();
        }
        return String.valueOf(s/(reviews.getReviews().size()*2));
    }
}
