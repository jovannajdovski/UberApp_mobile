package com.example.uberapp_tim12.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.fragments.DriverMapFragment;
import com.example.uberapp_tim12.fragments.PassengerCurrRideFragment;
import com.example.uberapp_tim12.model_mock.NavDrawerItem;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.tools.FragmentTransition;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SwitchCompat sw;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private ListView navDrawerList;
    private ActionBarDrawerToggle navDrawerToggle;
    private RelativeLayout navDrawerPane;
    private ArrayList<NavDrawerItem> navDrawerItems=new ArrayList();
    private FragmentManager manager;

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
        FragmentTransition.driverTo(DriverMapFragment.newInstance(),this,false);
        //FragmentTransition.driverTo(DriverCurrRideFragment.newInstance(new LatLng(41.385064,2.173403), new LatLng(40.416775,-3.70379)), this, false);
    }
//        prepareNavigationDrawerList();
//
//        navDrawerLayout=findViewById(R.id.drawerLayout);
//        navDrawerList=findViewById(R.id.navList);
//        navDrawerPane=findViewById(R.id.drawerPane);
//
//        NavDrawerListAdapter adapter=new NavDrawerListAdapter(this,navDrawerItems);
//
//        navDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
//        navDrawerList.setOnItemClickListener(new DrawerItemClickListener());
//        navDrawerList.setAdapter(adapter);
//
//
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setIcon(R.drawable.ic_launcher_foreground);
//        actionBar.setTitle(R.string.app_name);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
//        actionBar.setHomeButtonEnabled(true);
//
//        navDrawerToggle=new ActionBarDrawerToggle(
//                this,
//                navDrawerLayout,
//                toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close){
//            public void onDrawerClosed(View view){
//                invalidateOptionsMenu();
//            }
//            public void onDrawerOpened(View drawerView)
//            {
//                invalidateOptionsMenu();
//            }
//        };
//        if(savedInstanceState==null)
//        {
//            selectItemFromDrawer(3);
//        }
//        RelativeLayout pictureView=findViewById(R.id.picture_view);
//        pictureView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(DriverMainActivity.this, DriverAccountActivity.class);
//                User user=UserMockup.getUser();
//                intent.putExtra("user",user);
//                startActivity(intent);
//            }
//        });
//
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.driver_actionbar,menu);

        MenuItem itemSwitch=menu.findItem(R.id.isOnlineButton);
        itemSwitch.setActionView(R.layout.driver_activity_switch);
        sw=menu.findItem(R.id.isOnlineButton).getActionView().findViewById(R.id.driver_switch);
        sw.setChecked(true);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Toast.makeText(DriverMainActivity.this,"ONLINE",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(DriverMainActivity.this,"OFFLINE",Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(activeRideReceiver, new IntentFilter("activeRideDriver"));
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


//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        navDrawerToggle.syncState();
//    }
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        navDrawerToggle.onConfigurationChanged(newConfig);
//    }
//    private void prepareNavigationDrawerList(){
//        navDrawerItems.add(new NavDrawerItem(getString(R.string.notifications), R.drawable.ic_baseline_notifications));
//        navDrawerItems.add(new NavDrawerItem(getString(R.string.settings), R.drawable.ic_baseline_settings));
//        navDrawerItems.add(new NavDrawerItem(getString(R.string.about), R.drawable.ic_baseline_about));
//    }
//
//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItemFromDrawer(position);
//        }
//    }
//    private void selectItemFromDrawer(int position) {
//        Intent intent=null;
//        if(position == 0){
//            intent=new Intent(DriverMainActivity.this,DriverInboxActivity.class);
//            intent.putExtra("tab",1);
//            startActivity(intent);
//        }else if(position == 1){
//            intent = new Intent(DriverMainActivity.this, DriverSettingsActivity.class);
//            User user = UserMockup.getUser();
//            intent.putExtra("user", user);
//            startActivity(intent);
//        }else if(position == 2){
//            //..
//        }
//
//        navDrawerList.setItemChecked(position, true);
//        navDrawerLayout.closeDrawer(navDrawerPane);
//
//    }
}