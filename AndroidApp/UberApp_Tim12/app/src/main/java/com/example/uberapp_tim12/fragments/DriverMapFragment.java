package com.example.uberapp_tim12.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dialogs.LocationDialog;
import com.example.uberapp_tim12.dto.ActiveDriverDTO;
import com.example.uberapp_tim12.dto.ActiveDriverListDTO;
import com.example.uberapp_tim12.service.DriverService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class DriverMapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mMapFragment;
    private AlertDialog dialog;
    private Marker home;

    private GoogleMap map;


    public static DriverMapFragment newInstance() {
        DriverMapFragment mpf = new DriverMapFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_driver_map, vg, false);

        return view;
    }


    @Override
    public void onLocationChanged(Location location) {
//        if (map != null) {
//            addMarker(location);
//        }
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

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(activeDriversReceivers);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(activeDriversReceivers, new IntentFilter("activeDrivers"));
        createMapFragmentAndInflate();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps && !wifi) {
            showLocatonDialog();
        } else {
            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Request location updates:
                    locationManager.requestLocationUpdates(provider, 2000, 0, this);
//                    Toast.makeText(getContext(), "ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
        } else {
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

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();

                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle()+"\n"+marker.getSnippet(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(45.2396, 19.8227)).zoom(13).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



//        if (location != null) {
//            addMarker(location);
//        }

    }
    public void findActiveDrivers(){
        Intent intent=new Intent(getActivity(), DriverService.class);
        intent.putExtra("endpoint", "getActiveDrivers");
        getActivity().startService(intent);
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

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(14).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void addVehicleMarkers(List<ActiveDriverDTO> driverDTOS) {
        for (ActiveDriverDTO driver : driverDTOS){
            if (driver.isFree()){
                addFreeVehicle(driver);
            } else {
                addReservedVehicle(driver);
            }
        }
    }
    private void addFreeVehicle(ActiveDriverDTO driverDTO) {
        Marker marker = map.addMarker(new MarkerOptions()
                .title("Model: "+driverDTO.getVehicle().getModel())
                .snippet("Address: "+driverDTO.getLocation().getAddress())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.active_car_pin))
                .draggable(false)
                .position(new LatLng(driverDTO.getLocation().getLatitude(),driverDTO.getLocation().getLongitude())));
    }

    private void addReservedVehicle(ActiveDriverDTO driverDTO) {
        Marker marker = map.addMarker(new MarkerOptions()
                .title("Model: "+driverDTO.getVehicle().getModel())
                .snippet("Address: "+driverDTO.getLocation().getAddress())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.reserved_car_pin))
                .draggable(false)
                .position(new LatLng(driverDTO.getLocation().getLatitude(),driverDTO.getLocation().getLongitude())));
    }

//    private void addFreeVehicle(VehicleForMarker vehicle) {
//        Marker marker = map.addMarker(new MarkerOptions()
//                .title("Model: "+vehicle.getModel())
//                .snippet("Address: "+vehicle.getAddress())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.active_car_pin))
//                .draggable(false)
//                .position(new LatLng(vehicle.getLatitude(),vehicle.getLongitude())));
//    }
//
//    private void addReservedVehicle(VehicleForMarker vehicle) {
//        Marker marker = map.addMarker(new MarkerOptions()
//                .title("Model: "+vehicle.getModel())
//                .snippet("Address: "+vehicle.getAddress())
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.reserved_car_pin))
//                .draggable(false)
//                .position(new LatLng(vehicle.getLatitude(),vehicle.getLongitude())));
//    }


    public BroadcastReceiver activeDriversReceivers = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            ActiveDriverListDTO activeDriverDTOS= intent.getParcelableExtra("activeDriversDTO");
            if(activeDriverDTOS.getTotalCount()>0)
                addVehicleMarkers(activeDriverDTOS.getDrivers());
        }
    };
}