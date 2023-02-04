package com.example.uberapp_tim12.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.WorkHoursDTO;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.fragments.DriverMapFragment;
import com.example.uberapp_tim12.fragments.PassengerCurrRideFragment;
import com.example.uberapp_tim12.model_mock.NavDrawerItem;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.tools.FragmentTransition;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SwitchCompat sw;
    public static boolean switchOffWhenIsNotPossible=false;
    
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private DriverMapFragment driverMapFragment;
    private ListView navDrawerList;
    private ActionBarDrawerToggle navDrawerToggle;
    private RelativeLayout navDrawerPane;
    private ArrayList<NavDrawerItem> navDrawerItems=new ArrayList();
    private FragmentManager manager;

    public static WorkHoursDTO ongoingWorkHours=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);

        navDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close){
        };
        drawerLayout.addDrawerListener(navDrawerToggle);
        navDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        manager=getSupportFragmentManager();
        driverMapFragment=DriverMapFragment.newInstance();
        FragmentTransition.driverTo(driverMapFragment,this,false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.driver_actionbar,menu);

        MenuItem itemSwitch=menu.findItem(R.id.isOnlineButton);
        itemSwitch.setActionView(R.layout.driver_activity_switch);
        sw=menu.findItem(R.id.isOnlineButton).getActionView().findViewById(R.id.driver_switch);
        //sw.setChecked(true);
        if(ongoingWorkHours!=null) {
            Intent intent = new Intent(DriverMainActivity.this, DriverService.class);
            intent.putExtra("endpoint", "startShift");

            startService(intent);

        }
        else
        {
            sw.setChecked(false);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Intent intent = new Intent(DriverMainActivity.this, DriverService.class);
                    intent.putExtra("endpoint", "startShift");
                    startService(intent);
                }
                else {
                    if (!switchOffWhenIsNotPossible){
                            Intent intent = new Intent(DriverMainActivity.this, DriverService.class);
                        intent.putExtra("workHourId", ongoingWorkHours.getId());
                        intent.putExtra("endpoint", "endShift");
                        startService(intent);
                    }

                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent=null;
        if (id == R.id.profile) {
            intent = new Intent(DriverMainActivity.this, DriverAccountActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        }
        else if (id == R.id.history){
            intent = new Intent(DriverMainActivity.this,DriverRideHistoryActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.message){
            intent = new Intent(DriverMainActivity.this, DriverInboxActivity.class);
            intent.putExtra("tab",0);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(activeRideReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(startShiftReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(endShiftReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activeRideReceiver, new IntentFilter("activeRideDriver"));
        LocalBroadcastManager.getInstance(this).registerReceiver(startShiftReceiver, new IntentFilter("startShift"));
        LocalBroadcastManager.getInstance(this).registerReceiver(endShiftReceiver, new IntentFilter("endShift"));
    }

    public BroadcastReceiver activeRideReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                DriverCurrRideFragment driverCurrRideFragment = new DriverCurrRideFragment();
                manager.beginTransaction().replace(R.id.driverMainContent, driverCurrRideFragment,driverCurrRideFragment.getTag()).commit();
                drawerLayout.close();
                LocalBroadcastManager.getInstance(DriverMainActivity.this).unregisterReceiver(activeRideReceiver);
            }else {
                Toast.makeText(DriverMainActivity.this,"No active ride",Toast.LENGTH_SHORT).show();
                drawerLayout.close();
            }
        }
    };
    public BroadcastReceiver startShiftReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            WorkHoursDTO ongoingWorkHoursTemp=intent.getParcelableExtra("workHoursDTO");
            if(ongoingWorkHoursTemp==null) {
                if(intent.getStringExtra("message").equals("Work time exceeded")) {
                    switchOffWhenIsNotPossible = true;
                    sw.setChecked(false);
                }
            }
            else{
                Toast.makeText(DriverMainActivity.this, "ONLINE", Toast.LENGTH_SHORT).show();
                ongoingWorkHours=ongoingWorkHoursTemp;
            }
        }
    };
    public BroadcastReceiver endShiftReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            ongoingWorkHours=null;
            Toast.makeText(DriverMainActivity.this, "OFFLINE", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getTitle().toString()) {
            case "Settings":
                intent = new Intent(DriverMainActivity.this, DriverSettingsActivity.class);
                intent.putExtra("tab",0);
                startActivity(intent);
                break;
            case "Notification":
                intent = new Intent(DriverMainActivity.this, DriverInboxActivity.class);
                intent.putExtra("tab",1);
                startActivity(intent);
                break;
            case "Current ride":
                Intent intentRide = new Intent(this, CurrentRideService.class);
                intentRide.putExtra("endpoint", "getActiveRideForDriver");
                this.startService(intentRide);
                break;
            case "Accepted rides":
                intent = new Intent(DriverMainActivity.this, DriverAcceptedRidesActivity.class);
                intent.putExtra("tab",3);
                startActivity(intent);
                break;
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

}