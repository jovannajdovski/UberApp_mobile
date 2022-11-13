package com.example.uberapp_tim12.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model.Passenger;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.tools.MockupData;

import java.util.ArrayList;
import java.util.List;

public class PassengerAdapter extends BaseAdapter {

    private Activity activity;
    private List<Passenger> passengers;

    public PassengerAdapter(Activity activity, List<Passenger> passengers) {
        this.activity = activity;
        this.passengers = passengers;
    }

    @Override
    public int getCount() {
        return this.passengers.size();
    }

    @Override
    public Object getItem(int i) {
        return this.passengers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Passenger passenger = this.passengers.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.passenger_list, null);

        TextView name = (TextView)vi.findViewById(R.id.name);
        TextView phone = (TextView)vi.findViewById(R.id.phone_number);

        name.setText(passenger.getName()+" "+passenger.getSurname());
        phone.setText(passenger.getPhoneNumber());

        return  vi;
    }
}
