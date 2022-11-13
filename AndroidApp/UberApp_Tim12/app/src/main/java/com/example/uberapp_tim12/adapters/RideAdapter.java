package com.example.uberapp_tim12.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.tools.MockupData;

public class RideAdapter extends BaseAdapter {

    private Activity activity;

    public RideAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return MockupData.getRides().size();
    }

    @Override
    public Object getItem(int i) {
        return MockupData.getRides().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Ride ride = MockupData.getRides().get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.ride_list, null);

        TextView rate = (TextView)vi.findViewById(R.id.rate);
        TextView rideDateTime = (TextView)vi.findViewById(R.id.ride_date_time);
        TextView startPlace = (TextView)vi.findViewById(R.id.start_place);
        TextView endPlace = (TextView)vi.findViewById(R.id.end_place);

        rate.setText("5.00"); //TODO: change dynamicly
        rideDateTime.setText(ride.getStartDate() + " " + ride.getStartTime());
        startPlace.setText(ride.getStartPlace());
        endPlace.setText(ride.getEndPlace());

        return  vi;
    }
}
