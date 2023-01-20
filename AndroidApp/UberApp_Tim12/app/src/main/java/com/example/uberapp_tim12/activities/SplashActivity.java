package com.example.uberapp_tim12.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dialogs.LocationDialog;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.service.PanicService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends Activity implements LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager locationManager;
    private ConnectivityManager connectivityManager;
    private String provider;
    private AlertDialog dialog;
    private boolean wifiNotify;
    private boolean gpsNotify;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

        wifiNotify = false;
        gpsNotify = false;

        MaterialAlertDialogBuilder alertWIFI = new MaterialAlertDialogBuilder(this);
        alertWIFI.setTitle("Network disabled");
        alertWIFI.setMessage("Your NETWORK seems to be disabled, you have to enable it to use the app");

        alertWIFI.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                wifiNotify = true;
                Intent ints = new Intent ("permission");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
            }
        });

        alertWIFI.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        wifiNotify = true;
                        Intent ints = new Intent ("permission");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                    }
                });


        MaterialAlertDialogBuilder alertGPS = new MaterialAlertDialogBuilder(this);
        alertGPS.setTitle("Locations disabled");
        alertGPS.setMessage("Your Locations seems to be disabled, you have to enable it to use the app");

        alertGPS.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                gpsNotify = true;
                Intent ints = new Intent ("permission");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
            }
        });

        alertGPS.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        gpsNotify = true;
                        Intent ints = new Intent ("permission");
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                    }
                });

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        NetworkCapabilities networkCapabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean wifi = networkCapabilities != null;

        if (!wifi){
            alertWIFI.show();
        } else {
            wifiNotify = true;
        }
        if(!gps){
            alertGPS.show();
        } else {
            gpsNotify = true;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public BroadcastReceiver permissionReceiver = new BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (wifiNotify && gpsNotify){
                boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                NetworkCapabilities networkCapabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                boolean wifi = networkCapabilities != null;

                Log.d("PASS",""+gps+wifi);

                if (!wifi || !gps){
                    //new MaterialAlertDialogBuilder(SplashActivity.this).setTitle("You can't use the app until you turn on the network and GPS. Closing the app...").show();
                    Toast.makeText(SplashActivity.this,"You can't use the app until you turn on the network and GPS. Closing the app...",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    int SPLASH_TIME_OUT = 5000;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                            finish(); // da nebi mogao da ode back na splash
                        }
                    }, SPLASH_TIME_OUT);
                }
            }
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        NetworkCapabilities networkCapabilities =  connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean wifi = networkCapabilities != null;

        LocalBroadcastManager.getInstance(this).registerReceiver(permissionReceiver, new IntentFilter("permission"));

        if(wifi && gps){
            int SPLASH_TIME_OUT = 5000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                    finish(); // da nebi mogao da ode back na splash
                }
            }, SPLASH_TIME_OUT);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    private void showLocatonDialog() {
        if (dialog == null) {
            dialog = new LocationDialog(this).prepareDialog();
        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your locations....Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SplashActivity.this,
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}