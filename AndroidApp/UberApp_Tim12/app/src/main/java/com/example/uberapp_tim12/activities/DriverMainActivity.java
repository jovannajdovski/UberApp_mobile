package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.NavDrawerListAdapter;
import com.example.uberapp_tim12.model.NavDrawerItem;

import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity {
    SwitchCompat sw;
    private DrawerLayout navDrawerLayout;
    private ListView navDrawerList;
    private ActionBarDrawerToggle navDrawerToggle;
    private RelativeLayout navDrawerPane;
    private ArrayList<NavDrawerItem> navDrawerItems=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        prepareNavigationDrawerList();

        navDrawerLayout=findViewById(R.id.drawerLayout);
        navDrawerList=findViewById(R.id.navList);
        navDrawerPane=findViewById(R.id.drawerPane);

        NavDrawerListAdapter adapter=new NavDrawerListAdapter(this,navDrawerItems);

        navDrawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);
        navDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        navDrawerList.setAdapter(adapter);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setTitle(R.string.app_name);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        actionBar.setHomeButtonEnabled(true);

        navDrawerToggle=new ActionBarDrawerToggle(
                this,
                navDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View view){
                invalidateOptionsMenu();
            }
            public void onDrawerOpened(View drawerView)
            {
                invalidateOptionsMenu();
            }
        };
        if(savedInstanceState==null)
        {
            selectItemFromDrawer(3);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.driver_actionbar,menu);

        MenuItem itemSwitch=menu.findItem(R.id.isOnlineButton);
        itemSwitch.setActionView(R.layout.driver_activity_switch);
        sw=menu.findItem(R.id.isOnlineButton).getActionView().findViewById(R.id.driver_switch);
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
        if (id == R.id.profile)
            Toast.makeText(this,"Profile",Toast.LENGTH_LONG).show();
        else if (id == R.id.history){
            Intent intent = new Intent(DriverMainActivity.this,DriverRideHistoryActivity.class);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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
    private void prepareNavigationDrawerList(){
        navDrawerItems.add(new NavDrawerItem(getString(R.string.notifications), R.drawable.ic_baseline_notifications));
        navDrawerItems.add(new NavDrawerItem(getString(R.string.settings), R.drawable.ic_baseline_settings));
        navDrawerItems.add(new NavDrawerItem(getString(R.string.about), R.drawable.ic_baseline_about));
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }
    private void selectItemFromDrawer(int position) {
        Intent intent=null;
        if(position == 0){
            intent=new Intent(DriverMainActivity.this,DriverInboxActivity.class);
            intent.putExtra("tab",1);
            startActivity(intent);
        }else if(position == 1){

        }else if(position == 2){
            //..
        }else if(position == 3){
            //..
        }

        navDrawerList.setItemChecked(position, true);
        navDrawerLayout.closeDrawer(navDrawerPane);

    }
    public void openInbox(View view)
    {
        Intent intent = new Intent(DriverMainActivity.this, DriverInboxActivity.class);
        intent.putExtra("tab",0);
        startActivity(intent);
    }

}