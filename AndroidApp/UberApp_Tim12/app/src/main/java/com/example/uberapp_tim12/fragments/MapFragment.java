package com.example.uberapp_tim12.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.uberapp_tim12.BuildConfig;
import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dialogs.LocationDialog;
import com.example.uberapp_tim12.tools.FragmentTransition;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Arrays;
import java.util.Calendar;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mMapFragment;
    private AlertDialog dialog;
    private Marker home;
    private GoogleMap map;
    private int hour;
    private int minute;
    private LatLng startPoint;
    private LatLng endPoint;
    private String startAddress;
    private String endAddress;

    private AutocompleteSupportFragment autocompleteStart;
    private AutocompleteSupportFragment autocompleteEnd;

    public static MapFragment newInstance() {
        MapFragment mpf = new MapFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        hour=-1;
        minute=-1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_map, vg, false);

        LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        View reverseLocation = view.findViewById(R.id.reverse_location);
        reverseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompleteStart.setText(endAddress);
                autocompleteEnd.setText(startAddress);
                LatLng t = startPoint;
                startPoint = endPoint;
                endPoint = t;
                String temp = startAddress;
                startAddress = endAddress;
                endAddress = temp;
            }
        });

        TextInputLayout chooseTime = (TextInputLayout) view.findViewById(R.id.time_field_box);
        chooseTime.getEditText().setShowSoftInputOnFocus(false);
        chooseTime.getEditText().setClickable(false);
        chooseTime.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("fds", "sdffs");
                Calendar calendar = Calendar.getInstance();
                int currHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currMin = calendar.get(Calendar.MINUTE);
                MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(currHour)
                        .setMinute(currMin)
                        .setTitleText("Select ride start time")
                        .build();

                picker.showNow(getParentFragmentManager(), "Time");

                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hour = picker.getHour();
                        minute = picker.getMinute();
                        chooseTime.getEditText().setText(String.format("%02d:%02d", hour, minute));
                    }
                });
            }
        });

        Button findRoute = (Button) view.findViewById(R.id.routeButton);
        findRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startPoint != null && endPoint != null) {
                    FragmentTransition.to(DrawRouteFragment.newInstance(startPoint, endPoint), getActivity(), false);
                }
            }
        });

        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        PlacesClient placesClient = Places.createClient(getActivity());

        autocompleteStart =
                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_start);

        autocompleteEnd =
                (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_end);

        setUpAutocompleteFragment(autocompleteStart);
        setUpAutocompleteFragment(autocompleteEnd);

        autocompleteStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPoint = place.getLatLng();
                startAddress = place.getName();
                Log.i("PlaceTAG", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Log.i("PlaceTAG", "An error occurred: " + status);
            }
        });

        autocompleteEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endAddress = place.getName();
                endPoint = place.getLatLng();
                Log.i("PlaceTAG", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                Log.i("PlaceTAG", "An error occurred: " + status);
            }
        });

        return view;
    }

    private void setUpAutocompleteFragment(AutocompleteSupportFragment autocompleteSupportFragment){
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(42.263, 17.654),
                new LatLng(46.3538, 23.567)
        ));
        autocompleteSupportFragment.setCountries("RS");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

    }

    @Override
    public void onLocationChanged(Location location) {
        if (map != null) {
            addMarker(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void createMapFragmentAndInflate() {

        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

//        can get location before map
        mMapFragment.getMapAsync(this);
    }

    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(getActivity()).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        createMapFragmentAndInflate();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !wifi) {
            showLocatonDialog();
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 2000, 0, this);
//                    Toast.makeText(getContext(), "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                }else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 2000, 0, this);
//                    Toast.makeText(getContext(), "ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations....Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.setMyLocationEnabled(true);
        Location location = null;

        if (provider == null) {
            showLocatonDialog();
        }else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    location = locationManager.getLastKnownLocation(provider);
                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.addMarker(new MarkerOptions()
                        .title("YOUR_POSITON")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_marker))
                        .position(latLng));
                home.setFlat(true);

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("ASD", "ASDASDASDSA");
            }
        });

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(getActivity(), "Drag started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Toast.makeText(getActivity(), "Dragging", Toast.LENGTH_SHORT).show();
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(getActivity(), "Drag ended", Toast.LENGTH_SHORT).show();
            }
        });

        if (location != null) {
            addMarker(location);
        }

    }

    private void addMarker(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if (home != null) {
            home.remove();
        }

        home = map.addMarker(new MarkerOptions()
                .title("YOUR_POSITON")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(loc));
        home.setFlat(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(14).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

}