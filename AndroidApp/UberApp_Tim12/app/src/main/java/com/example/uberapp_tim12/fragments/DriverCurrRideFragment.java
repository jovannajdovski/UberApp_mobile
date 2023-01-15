package com.example.uberapp_tim12.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.BuildConfig;
import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.service.PanicService;
import com.example.uberapp_tim12.service.PassengerService;
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


public class DriverCurrRideFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private LatLng start;
    private LatLng end;
    private CameraUpdate cu;

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;
    private List<PassengerDetailsDTO> passengers;
    private int DATASET_COUNT;

    private View view;
    private boolean routeDraw;
    private int rideId;
    private TextView timer;
    private Timer timerReal;

    public DriverCurrRideFragment() {
    }

    public static DriverCurrRideFragment newInstance() {

        DriverCurrRideFragment mpf = new DriverCurrRideFragment();

        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        routeDraw = false;

        Intent intentRide = new Intent(getActivity(), CurrentRideService.class);
        intentRide.putExtra("endpoint", "getActiveRideForDriver");
        getActivity().startService(intentRide);

    }

    public BroadcastReceiver bReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            RideFullDTO activeRide = (RideFullDTO) intent.getSerializableExtra("activeRideDTO");

            start = new LatLng(activeRide.getLocations().iterator().next().getDeparture().getLatitude(),
                    activeRide.getLocations().iterator().next().getDeparture().getLongitude());
            end = new LatLng(activeRide.getLocations().iterator().next().getDestination().getLatitude(),
                    activeRide.getLocations().iterator().next().getDestination().getLongitude());

            rideId = activeRide.getId();
            String startAddress = activeRide.getLocations().iterator().next().getDeparture().getAddress();
            String endAddress = activeRide.getLocations().iterator().next().getDestination().getAddress();

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);

            String estimatedTime = nf.format(activeRide.getEstimatedTimeInMinutes()) + "min";
            String totalCost = nf.format(activeRide.getTotalCost()) + " RSD";

            List<Integer> passengersId = new ArrayList<>();
            for (UserRideDTO passenger : activeRide.getPassengers()) {
                passengersId.add(passenger.getId());
            }
            DATASET_COUNT = passengersId.size();

            TextView estimatedTimeLbl = view.findViewById(R.id.estimated_time_value);
            TextView totalCostLbl = view.findViewById(R.id.total_cost_value);
            TextView routeLbl = view.findViewById(R.id.route);

            routeLbl.setText(startAddress + " - " + endAddress);
            totalCostLbl.setText(totalCost);
            estimatedTimeLbl.setText(estimatedTime);

            passengers = new ArrayList<>();
            for (Integer passengerId : passengersId) {
                Intent intentDriver = new Intent(getActivity(), PassengerService.class);
                intentDriver.putExtra("endpoint", "getPassengerDetails");
                intentDriver.putExtra("passengerId", passengerId);
                getActivity().startService(intentDriver);
            }

            if (!routeDraw) {
                drawRoute();
            }
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

    public BroadcastReceiver panicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            PanicDTO panicDTO = (PanicDTO) intent.getSerializableExtra("panicDTO");

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
        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

        // can get location before map
        mMapFragment.getMapAsync(this);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bReceiver, new IntentFilter("activeRideDriver"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(passengerReceiver, new IntentFilter("passengerDetails"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(panicReceiver, new IntentFilter("panicRideDetails"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(passengerReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(panicReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_driver_curr_ride, vg, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (data != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) data
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

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

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    public void updateDistance(String distance) {
        TextView distanceLbl = view.findViewById(R.id.distance_value);
        distanceLbl.setText(distance);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.style_1_json));
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.style_2_json));

        if (start != null && end != null) {
            drawRoute();
        }
    }

    public void drawRoute() {
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

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        updateDistance(leg.distance.toString());
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
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
        } catch (Exception ex) {
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

    private void showPanicDialog() {
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getActivity());
        alert.setTitle("Panic");
        alert.setMessage("Enter reason for panic (optional)");

        final TextInputEditText input = new TextInputEditText(getActivity());
        final TextInputLayout inputLayout = new TextInputLayout(getActivity());

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

        alert.setPositiveButton("PANIC", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                Log.d("Panic", "Panic message : " + value);

                Intent intentPanic = new Intent(getActivity(), PanicService.class);
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

