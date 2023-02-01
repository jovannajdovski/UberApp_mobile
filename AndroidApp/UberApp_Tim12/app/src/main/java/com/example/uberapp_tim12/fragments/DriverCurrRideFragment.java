package com.example.uberapp_tim12.fragments;

import android.annotation.SuppressLint;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
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
import com.example.uberapp_tim12.activities.ChatActivity;
import com.example.uberapp_tim12.adapters.ChatListAdapter;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.dto.ChatMessageDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.LocationDTO;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RideIdListDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.model_mock.ChatItem;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.service.PanicService;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.RideService;
import com.example.uberapp_tim12.service.UserService;
import com.example.uberapp_tim12.web_socket.STOMPUtils;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class DriverCurrRideFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private LatLng start;
    private LatLng end;
    private LatLng currentLocation=null;
    private CountDownTimer countDownTimer;
    private int stepPassed;
    private CameraUpdate cu;
    private Gson mGson = new GsonBuilder().create();

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
    private Timer timerSimulation;

    private List<LatLng> path;
    private Marker rideMarker;

    private RideFullDTO activeRide;

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
            activeRide = (RideFullDTO) intent.getSerializableExtra("activeRideDTO");

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

    public BroadcastReceiver finishRideReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Ride ride = (Ride) intent.getSerializableExtra("ride");
            sendSocketForFinishRide(ride.getId());

        }
    };

    @SuppressLint("CheckResult")
    private void sendSocketForFinishRide(Integer rideId){
        STOMPUtils stompUtils = new STOMPUtils();
        stompUtils.connectStomp();

        stompUtils.stompClient.send("api/socket-subscriber/finish-ride/"+rideId)
                .compose(stompUtils.applySchedulers())
                .subscribe(() -> {
                    Log.d("RIDECHAT", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RIDECHAT", "Error send STOMP echo", throwable);
                });

        stompUtils.disconnectStomp();

        FragmentManager manager = getParentFragmentManager();
        DriverMapFragment mapFragment = new DriverMapFragment();
        manager.beginTransaction().replace(R.id.driverMainContent,
                mapFragment,
                mapFragment.getTag()).commit();
    }

    public BroadcastReceiver panicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            PanicDTO panicDTO = (PanicDTO) intent.getSerializableExtra("panicDTO");
            sendSocketForPanic(panicDTO.getReason());

        }
    };

    @SuppressLint("CheckResult")
    private void sendSocketForPanic(String reason){
        STOMPUtils stompUtils = new STOMPUtils();
        stompUtils.connectStomp();

        Integer fromId = LoggedUser.getUserId();
        stompUtils.stompClient.send("api/socket-subscriber/send/panic/"+fromId+"/"+rideId, reason)
                .compose(stompUtils.applySchedulers())
                .subscribe(() -> {
                    Log.d("RIDECHAT", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RIDECHAT", "Error send STOMP echo", throwable);
                });

        stompUtils.disconnectStomp();
    }

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
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(finishRideReceiver, new IntentFilter("endRide"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(passengerReceiver, new IntentFilter("passengerDetails"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(panicReceiver, new IntentFilter("panicRideDetails"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(chatReceiver, new IntentFilter("rideChat"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(passengerReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(panicReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(chatReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(finishRideReceiver);
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

        Button finishButton = (Button) view.findViewById(R.id.routeButton);

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentFinish = new Intent(getActivity(), RideService.class);
                intentFinish.putExtra("endpoint", "endRide");
                intentFinish.putExtra("rideId", activeRide.getId());
                Log.d("PASSSS", "poslao");
                getActivity().startService(intentFinish);
            }
        });

        LinearLayout chatButton=(LinearLayout) view.findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMessages = new Intent(getActivity(), UserService.class);
                intentMessages.putExtra("endpoint", "getRideMessages");
                intentMessages.putExtra("rideId", activeRide.getId());
                Log.d("PASSSS", "poslao");
                getActivity().startService(intentMessages);
            }
        });

        timer = (TextView) view.findViewById(R.id.timer_value);
        setTimer();

        return view;
    }
    public BroadcastReceiver chatReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSS", "primio u on receive");
            Intent intentSer;
            intentSer=new Intent(getActivity(), ChatActivity.class);
            MessageListDTO messageListDTO=intent.getParcelableExtra("messageListDTO");
            Log.d("PASSSS", String.valueOf(messageListDTO.getMessages().size()));
            ChatItem chatItem=new ChatItem(activeRide.getLocations().iterator().next().getDeparture().getAddress()+" - "
                    +activeRide.getLocations().iterator().next().getDestination().getAddress(),
                    activeRide.getStartTime(),R.drawable.ic_profile,activeRide.getId(), messageListDTO.getMessages(),activeRide.getPassengers().iterator().next().getId());
            intentSer.putExtra("chat", chatItem);
            startActivity(intentSer);
        }
    };

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
        mMap.addMarker(new MarkerOptions().position(end).title("End point").icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_marker)));

        //Define list to get all latlng for the route
        path = new ArrayList();

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
//        if(currentLocation==null) currentLocation=start;
        rideMarker = mMap.addMarker(new MarkerOptions().position(start).title("My vehicle").icon(BitmapDescriptorFactory.fromResource(R.drawable.reserved_car_pin)));

        registerSocketForCurrentLocation(rideId);
    }

    @SuppressLint("CheckResult")
    private void registerSocketForCurrentLocation(Integer rideId) {
        STOMPUtils stompUtils = new STOMPUtils();
        stompUtils.connectStomp();

        stompUtils.stompClient.topic("api/socket-publisher/" + "vehicle/current-location/" + rideId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("RIDECHAT", "Received " + topicMessage.getPayload());
                    LocationDTO locationDTO = mGson.fromJson(topicMessage.getPayload(), LocationDTO.class);
                    setCurrentLocation(locationDTO);

                }, throwable -> {
                    Log.e("RIDECHAT", "Error on subscribe topic", throwable);
                });
        stompUtils.stompClient.send("api/socket-subscriber/vehicle/" + rideId + "/current-location")
                .compose(stompUtils.applySchedulers())
                .subscribe(() -> {
                    Log.d("RIDECHAT", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RIDECHAT", "Error send STOMP echo", throwable);
                });

    }

    public void setCurrentLocation(LocationDTO locationDTO)
    {
        if(locationDTO.getAddress().equals("finish"))
            rideMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.active_car_pin));
        else
            rideMarker.setPosition(new LatLng(locationDTO.getLatitude(),locationDTO.getLongitude()));

    }

    public void simulateRide(){
        timerSimulation = new Timer();
        TimerTask task = new TimerSimulationTask();

        rideMarker = mMap.addMarker(new MarkerOptions().position(start).title("My vehicle").icon(BitmapDescriptorFactory.fromResource(R.drawable.reserved_car_pin)));

        timerSimulation.schedule(task, 1000, 1000);
//
//
//        int interval = 2000;
//
//        countDownTimer= new CountDownTimer((long) (path.size()) * interval, interval) {
//            int tick = 0;
//
//            public void onTick(long millisUntilFinished) {
//                rideMarker.setPosition(path.get(tick));
//                tick++;
//            }
//
//            public void onFinish() {
//                Toast.makeText(getActivity(), "You are at your destination", Toast.LENGTH_SHORT).show();
//                rideMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.active_car_pin));
//                timerReal.cancel();
//                timerReal.purge();
//            }
//
//        }.start();

    }
    private class TimerSimulationTask extends TimerTask {
        int tick = 0;

        public void run() {
            if(tick>=path.size())
            {
                timerReal.cancel();
                timerReal.purge();
                timerSimulation.cancel();
                timerSimulation.purge();
                return;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAJMER", String.valueOf(tick));
                    Log.d("PATH SIZE", String.valueOf(path.size()));
                    if(tick>=path.size()) {
                        Toast.makeText(getActivity(), "You are at your destination", Toast.LENGTH_SHORT).show();
                        rideMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.active_car_pin));
                        return;
                    }
                    rideMarker.setPosition(path.get(tick));
                }
            });
            tick++;
        }

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
        Log.d("SET TIMER", "SET TIMER");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerReal.cancel();
        timerReal.purge();
    }
}

