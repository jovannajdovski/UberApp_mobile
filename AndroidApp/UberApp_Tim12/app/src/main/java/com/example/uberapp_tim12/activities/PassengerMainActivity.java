package com.example.uberapp_tim12.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.fragments.ConfirmationFragment;
import com.example.uberapp_tim12.fragments.DrawRouteFragment;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.fragments.InviteFriendsFragment;
import com.example.uberapp_tim12.fragments.MapFragment;
import com.example.uberapp_tim12.fragments.MoreOptionsFragment;
import com.example.uberapp_tim12.fragments.OverviewFragment;
import com.example.uberapp_tim12.model.Ride;

import com.example.uberapp_tim12.fragments.PassengerCurrRideFragment;

import com.example.uberapp_tim12.model_mock.NavDrawerItem;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.tools.FragmentTransition;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class PassengerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ActionBarDrawerToggle navDrawerToggle;
    private RelativeLayout navDrawerPane;
    private ArrayList<NavDrawerItem> navDrawerItems=new ArrayList();
    private MapFragment mapFragment;
    private DrawRouteFragment drawRouteFragment;
    private InviteFriendsFragment inviteFriendsFragment;
    private MoreOptionsFragment moreOptionsFragment;
    private ConfirmationFragment confirmationFragment;
    private OverviewFragment overviewFragment;
    private FragmentManager manager;
    public StepView stepView;
    private CreateRideDTO ride;
    private List<String> passengers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));

       // FragmentTransition.passengerTo(MapFragment.newInstance(),this,false);
       // FragmentTransition.passengerTo(DrawRouteFragment.newInstance(new LatLng(41.385064,2.173403), new LatLng(40.416775,-3.70379)), this, false);
        //FragmentTransition.passengerTo(PassengerCurrRideFragment.newInstance(new LatLng(41.385064,2.173403), new LatLng(40.416775,-3.70379)), this, false);


        stepView=findViewById(R.id.step_view);
        stepView.getState().animationType(StepView.ANIMATION_ALL)
                .stepsNumber(6)
                .animationDuration(300).commit();
        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                int currentStep=stepView.getCurrentStep();

                if(step<currentStep && currentStep!=5) {
                    changeFragment(step,ride, passengers);
                }
            }
        });

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

//        navDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);


//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setIcon(R.drawable.ic_launcher_foreground);
//        actionBar.setTitle(R.string.app_name);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
//        actionBar.setHomeButtonEnabled(true);

        String offer = getIntent().getStringExtra("offer");
        if (offer==null){
            mapFragment=new MapFragment(null,null, null, null);
            manager=getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.passengerMainContent,mapFragment,mapFragment.getTag()).commit();
        } else {
            String start = getIntent().getStringExtra("start");
            String end = getIntent().getStringExtra("end");
            LatLng startPoint = new LatLng(getIntent().getDoubleExtra("startLat",0),getIntent().getDoubleExtra("startLon",0));
            LatLng endPoint = new LatLng(getIntent().getDoubleExtra("endLat",0),getIntent().getDoubleExtra("endLon",0));
            mapFragment=new MapFragment(start, end, startPoint, endPoint);
            manager=getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.passengerMainContent,mapFragment,mapFragment.getTag()).commit();
        }



    }

    public void changeFragment(int step, CreateRideDTO ride, List<String> passengers) {
        this.ride=ride;
        this.passengers=passengers;
        stepView.go(step,true);
        switch (step)
        {
            case 0:
                manager.beginTransaction().replace(R.id.passengerMainContent,mapFragment,mapFragment.getTag()).commit();
                break;
            case 1:
                drawRouteFragment=DrawRouteFragment.newInstance(ride);
                manager.beginTransaction().replace(R.id.passengerMainContent, drawRouteFragment,drawRouteFragment.getTag()).commit();
                break;
            case 2:
                inviteFriendsFragment=InviteFriendsFragment.newInstance(ride);
                manager.beginTransaction().replace(R.id.passengerMainContent,inviteFriendsFragment,inviteFriendsFragment.getTag()).commit();
                break;
            case 3:
                moreOptionsFragment=MoreOptionsFragment.newInstance(ride, passengers);
                manager.beginTransaction().replace(R.id.passengerMainContent, moreOptionsFragment,moreOptionsFragment.getTag()).commit();
                break;
            case 4:
                confirmationFragment=ConfirmationFragment.newInstance(ride, passengers);
                manager.beginTransaction().replace(R.id.passengerMainContent,confirmationFragment,confirmationFragment.getTag()).commit();
                break;
            case 5:
                overviewFragment=OverviewFragment.newInstance(ride, passengers);
                manager.beginTransaction().replace(R.id.passengerMainContent, overviewFragment,overviewFragment.getTag()).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.driver_actionbar,menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.profile)
        {
            intent = new Intent(PassengerMainActivity.this, PassengerAccountActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        }
        else if (id == R.id.history){
            intent = new Intent(PassengerMainActivity.this, PassengerRideHistoryActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.message){
            intent = new Intent(PassengerMainActivity.this, PassengerInboxActivity.class);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activeRideReceiver, new IntentFilter("activeRidePassenger"));
    }

    public BroadcastReceiver activeRideReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")){
                PassengerCurrRideFragment passengerCurrRideFragment = new PassengerCurrRideFragment();
                RelativeLayout content = (RelativeLayout) findViewById(R.id.passengerCurrRideContent);
                manager.beginTransaction().replace(R.id.passengerCurrRideContent, passengerCurrRideFragment,passengerCurrRideFragment.getTag()).commit();
                content.removeAllViews();
                drawerLayout.close();
                LocalBroadcastManager.getInstance(PassengerMainActivity.this).unregisterReceiver(activeRideReceiver);
            }else {
                Toast.makeText(PassengerMainActivity.this,"No active ride",Toast.LENGTH_SHORT).show();
                drawerLayout.close();
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getTitle().toString()) {
            case "Settings":
                intent = new Intent(PassengerMainActivity.this, PassengerSettingsActivity.class);
                intent.putExtra("tab",0);
                startActivity(intent);
                break;
            case "Notification":
                intent = new Intent(PassengerMainActivity.this, PassengerInboxActivity.class);
                intent.putExtra("tab",1);
                startActivity(intent);
                break;
            case "Current ride":
                Intent intentRide = new Intent(this, CurrentRideService.class);
                intentRide.putExtra("endpoint", "getActiveRideForPassenger");
                this.startService(intentRide);

                break;
        }

        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}