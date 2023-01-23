package com.example.uberapp_tim12.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.DriverAcceptedRidesActivity;
import com.example.uberapp_tim12.activities.RideDetailForPassengerActivity;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.PathDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RideNoStatusDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.service.PanicService;
import com.example.uberapp_tim12.service.RideService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AcceptedRideAdapter extends RecyclerView.Adapter<AcceptedRideAdapter.ViewHolder>{
    private static final String TAG = "ReviewAdapter";

    private List<RideNoStatusDTO> mDataSet;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView rideDateTime;
        private TextView startPlace;
        private TextView endPlace;

        private Button playButton;
        private Button cancelButton;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                }
            });
            rideDateTime = (TextView) v.findViewById(R.id.ride_date_time);
            startPlace = (TextView) v.findViewById(R.id.start_place);
            endPlace = (TextView) v.findViewById(R.id.end_place);
            playButton = (Button) v.findViewById(R.id.playButton);
            cancelButton = (Button) v.findViewById(R.id.cancelButton);

        }

        public TextView getRideDateTime() {
            return rideDateTime;
        }

        public TextView getStartPlace() {
            return startPlace;
        }

        public TextView getEndPlace() {
            return endPlace;
        }

        public Button getPlayButton() {
            return playButton;
        }

        public Button getCancelButton() {
            return cancelButton;
        }
    }


    public AcceptedRideAdapter(List<RideNoStatusDTO> dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public AcceptedRideAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.accepted_ride_raw_item, viewGroup, false);

        context = viewGroup.getContext();
        return new AcceptedRideAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AcceptedRideAdapter.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        RideNoStatusDTO ride = mDataSet.get(position);
        String[] startDateTime = ride.getStartTime().split("T");

        String[] datePoints = startDateTime[0].split("-");
        String startDate = datePoints[2]+"."+datePoints[1]+"."+datePoints[0]+".";
        String[] timePoints = startDateTime[1].split(":");
        String startTime = timePoints[0]+":"+timePoints[1];

        viewHolder.getRideDateTime().setText(startDate + " " + startTime);
        PathDTO path = (PathDTO) ride.getLocations().toArray()[0];
        viewHolder.getStartPlace().setText(path.getDeparture().getAddress());
        viewHolder.getEndPlace().setText(path.getDestination().getAddress());

        viewHolder.getPlayButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStartRide = new Intent(context, RideService.class);
                intentStartRide.putExtra("endpoint", "startRide");
                intentStartRide.putExtra("position", viewHolder.getAbsoluteAdapterPosition());
                intentStartRide.putExtra("rideId", ride.getId());

                LocalBroadcastManager.getInstance(context).registerReceiver(startedRidesReceiver, new IntentFilter("rideStarted"));

                context.startService(intentStartRide);
            }
        });

        viewHolder.getCancelButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReasonDialog(ride, viewHolder.getAbsoluteAdapterPosition());
            }
        });

    }

    public BroadcastReceiver startedRidesReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                Toast.makeText(context,"Your ride started, check your current rides",Toast.LENGTH_SHORT).show();
                int pos = intent.getIntExtra("position",-1);
                Log.d("DDDD",pos+"");
                if (pos!=-1){
                    removeAt(pos);
                }
            }else {
                Toast.makeText(context,"You can't start that ride",Toast.LENGTH_SHORT).show();
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(startedRidesReceiver);
        }
    };

    public BroadcastReceiver rejectedRidesReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                Toast.makeText(context,"Your ride rejected",Toast.LENGTH_SHORT).show();
                int pos = intent.getIntExtra("position",-1);
                Log.d("DDDD",pos+"");
                if (pos!=-1){
                    removeAt(pos);
                }
            }else {
                Toast.makeText(context,"You can't reject that ride",Toast.LENGTH_SHORT).show();
            }
            LocalBroadcastManager.getInstance(context).unregisterReceiver(rejectedRidesReceiver);
        }
    };

    public void removeAt(int position) {
        Log.d("DDDD",position+"");
        mDataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataSet.size());
    }


    private void showReasonDialog(RideNoStatusDTO ride, int position) {
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(context);
        alert.setTitle("Ride cancellation");
        alert.setMessage("Enter reason for cancellation (optional)");

        final TextInputEditText input = new TextInputEditText(context);
        final TextInputLayout inputLayout = new TextInputLayout(context);

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editTextParams.setMargins(100, 0, 100, 0);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayoutParams.setMargins(100, 0, 100, 0);

        inputLayout.setLayoutParams(textInputLayoutParams);
        inputLayout.addView(input, editTextParams);
        inputLayout.setHint("Reason");
        alert.setView(inputLayout);

        alert.setPositiveButton("CANCEL RIDE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                Intent intentRejectRide = new Intent(context, RideService.class);
                intentRejectRide.putExtra("endpoint", "rejectRide");
                intentRejectRide.putExtra("rideId", ride.getId());
                intentRejectRide.putExtra("position", position);
                intentRejectRide.putExtra("reasonDTO", new ReasonDTO(value));

                LocalBroadcastManager.getInstance(context).registerReceiver(rejectedRidesReceiver, new IntentFilter("rejectRide"));

                context.startService(intentRejectRide);
            }
        });

        alert.setNeutralButton("GIVE UP", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
