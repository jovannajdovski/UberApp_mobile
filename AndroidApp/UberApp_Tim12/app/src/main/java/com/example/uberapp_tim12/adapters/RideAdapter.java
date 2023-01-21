package com.example.uberapp_tim12.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.model_mock.Ride;
import com.example.uberapp_tim12.tools.MockupData;

import java.util.List;

public class RideAdapter extends BaseAdapter {

    private Activity activity;
    private List<RideNoStatusDTO> rides;

    public RideAdapter(Activity activity, List<RideNoStatusDTO> rides) {
        this.activity = activity;
        this.rides = rides;
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

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.ride_list, null);

        TextView rate = (TextView)vi.findViewById(R.id.rate);
        TextView rideDateTime = (TextView)vi.findViewById(R.id.ride_date_time);
        TextView startPlace = (TextView)vi.findViewById(R.id.start_place);
        TextView endPlace = (TextView)vi.findViewById(R.id.end_place);

        rate.setText("5.00"); //TODO: change dynamicly
        String[] startTime = ride.getStartTime().split("T");

        rideDateTime.setText(startTime[0] + " " + startTime[1]);
        PathDTO path = (PathDTO) ride.getLocations().toArray()[0];
        startPlace.setText(path.getDeparture().getAddress());
        endPlace.setText(path.getDestination().getAddress());

        return  vi;
    }
}
