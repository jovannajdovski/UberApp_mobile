package com.example.uberapp_tim12.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.BuildConfig;
import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.service.PanicService;
import com.example.uberapp_tim12.service.RideService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PassengerCurrRideFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private LatLng start;
    private LatLng end;
    private CameraUpdate cu;
    private View view;
    private Integer driverId;

    boolean routeDraw;
    private int rideId;

    private TextView timer;
    private Timer timerReal;

    public PassengerCurrRideFragment() {
    }

    public static PassengerCurrRideFragment newInstance() {
        PassengerCurrRideFragment mpf = new PassengerCurrRideFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routeDraw = false;

        Intent intentRide=new Intent(getActivity(), CurrentRideService.class);
        intentRide.putExtra("endpoint", "getActiveRideForPassenger");
        getActivity().startService(intentRide);

    }

    public BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            RideFullDTO activeRide= (RideFullDTO) intent.getSerializableExtra("activeRideDTO");

            start = new LatLng(activeRide.getLocations().iterator().next().getDeparture().getLatitude(),
                    activeRide.getLocations().iterator().next().getDeparture().getLongitude());
            end = new LatLng(activeRide.getLocations().iterator().next().getDestination().getLatitude(),
                    activeRide.getLocations().iterator().next().getDestination().getLongitude());

            rideId = activeRide.getId();

            String startAddress = activeRide.getLocations().iterator().next().getDeparture().getAddress();
            String endAddress = activeRide.getLocations().iterator().next().getDestination().getAddress();

            NumberFormat nf= NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);

            String estimatedTime = nf.format(activeRide.getEstimatedTimeInMinutes())+"min";
            String totalCost = nf.format(activeRide.getTotalCost())+" RSD";

            driverId = activeRide.getDriver().getId();

            TextView estimatedTimeLbl =view.findViewById(R.id.estimated_time_value);
            TextView totalCostLbl =view.findViewById(R.id.total_cost_value);
            TextView routeLbl =view.findViewById(R.id.route);

            routeLbl.setText(startAddress+" - "+endAddress);
            totalCostLbl.setText(totalCost);
            estimatedTimeLbl.setText(estimatedTime);

            Intent intentDriver=new Intent(getActivity(), DriverService.class);
            intentDriver.putExtra("endpoint", "getDriverDetails");
            intentDriver.putExtra("driverId", driverId);
            getActivity().startService(intentDriver);

            Log.d("PASSS",getActivity().toString());
            if (!routeDraw){
                drawRoute();
            }
        }
    };

    public BroadcastReceiver driverReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            DriverDetailsDTO driver= (DriverDetailsDTO) intent.getSerializableExtra("driverDetailsDTO");

            String name = driver.getName();
            String surname = driver.getSurname();

            TextView driverLbl =view.findViewById(R.id.driver_value);

            driverLbl.setText(name+ " "+ surname);
        }
    };

    public BroadcastReceiver panicReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            PanicDTO panicDTO= (PanicDTO) intent.getSerializableExtra("panicDTO");

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

        // can get location before map
        mMapFragment.getMapAsync(this);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bReceiver, new IntentFilter("activeRidePassenger"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(driverReceiver, new IntentFilter("driverDetails"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(panicReceiver, new IntentFilter("panicRideDetails"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(driverReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(panicReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_passenger_curr_ride, vg, false);

        Button panicButton = (Button) view.findViewById(R.id.panic_button);

        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPanicDialog();
            }
        });

        timer = (TextView) view.findViewById(R.id.timer_value);
        setTimer();

        return view;
    }

    public void updateDistance(String distance){
        TextView distanceLbl =view.findViewById(R.id.distance_value);
        distanceLbl.setText(distance);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.style_1_json));
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.style_2_json));

        if (start!=null && end!=null){
            drawRoute();
        }
    }

    public void drawRoute(){
        mMap.addMarker(new MarkerOptions().position(start).title("Start point").icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker)));
        mMap.addMarker(new MarkerOptions().position(end).title("End point").icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker)));

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY)
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, start.latitude + "," + start.longitude,
                end.latitude + "," + end.longitude);

        try {
            DirectionsResult res = req.await();
            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        updateDistance(leg.distance.toString());
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("Map direction", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).clickable(true).endCap(new RoundCap()).color(Color.YELLOW).width(10);
            mMap.addPolyline(opts);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start);
        builder.include(end);

        int padding = 200;
        LatLngBounds bounds = builder.build();

        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cu);
            }
        });

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnPolylineClickListener(this);
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 6));

        routeDraw = true;
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        mMap.animateCamera(cu);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    private void showPanicDialog(){
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getActivity());
        alert.setTitle("Panic");
        alert.setMessage("Enter reason for panic (optional)");

        final TextInputEditText input = new TextInputEditText(getActivity());
        final TextInputLayout inputLayout = new TextInputLayout(getActivity());

        LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editTextParams.setMargins(100,0,100,0);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textInputLayoutParams.setMargins(100,0,100,0);

        inputLayout.setLayoutParams(textInputLayoutParams);
        inputLayout.addView(input, editTextParams);
        inputLayout.setHint("Reason");
        alert.setView(inputLayout);

        alert.setPositiveButton("PANIC", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.d("Panic", "Panic message : " + value);

                Intent intentPanic=new Intent(getActivity(), PanicService.class);
                intentPanic.putExtra("endpoint", "panicRide");
                intentPanic.putExtra("idRide", rideId);
                intentPanic.putExtra("reason", value);
                getActivity().startService(intentPanic);
            }
        });

        alert.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
        alert.show();
    }

    public void setTimer() {
        timerReal = new Timer();
        TimerTask task = new TimerUpdateTask();

        timerReal.schedule(task, 1000, 1000);

    }

    private class TimerUpdateTask extends TimerTask {
        int tick = 0;

        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timer.setText("" + String.format("%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(tick),
                            TimeUnit.SECONDS.toSeconds(tick) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(tick))));
                }
            });
            tick++;
        }

    }
}