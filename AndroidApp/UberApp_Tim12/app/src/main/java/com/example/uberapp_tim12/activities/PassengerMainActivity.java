package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.uberapp_tim12.R;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.NavDrawerListAdapter;
import com.example.uberapp_tim12.fragments.ConfirmationFragment;
import com.example.uberapp_tim12.fragments.InviteFriendsFragment;
import com.example.uberapp_tim12.fragments.MapFragment;
import com.example.uberapp_tim12.fragments.MoreOptionsFragment;
import com.example.uberapp_tim12.fragments.OverviewFragment;
import com.example.uberapp_tim12.fragments.RouteFragment;
import com.example.uberapp_tim12.model.NavDrawerItem;
import com.example.uberapp_tim12.model.User;
import com.example.uberapp_tim12.tools.UserMockup;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class PassengerMainActivity extends AppCompatActivity {

    private DrawerLayout navDrawerLayout;
    private ListView navDrawerList;
    private ActionBarDrawerToggle navDrawerToggle;
    private RelativeLayout navDrawerPane;
    private ArrayList<NavDrawerItem> navDrawerItems=new ArrayList();
    private MapFragment mapFragment;
    private RouteFragment routeFragment;
    private InviteFriendsFragment inviteFriendsFragment;
    private MoreOptionsFragment moreOptionsFragment;
    private ConfirmationFragment confirmationFragment;
    private OverviewFragment overviewFragment;
    private FragmentManager manager;
    public StepView stepView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));

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

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setTitle(R.string.app_name);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        actionBar.setHomeButtonEnabled(true);

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
//            selectItemFromDrawer(4);
//        }
//        RelativeLayout pictureView=findViewById(R.id.picture_view);
//        pictureView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(PassengerMainActivity.this, PassengerAccountActivity.class);
//                User user=UserMockup.getUser();
//                intent.putExtra("user",user);
//                startActivity(intent);
//            }
//        });

        stepView=findViewById(R.id.step_view);
        stepView.getState().animationType(StepView.ANIMATION_ALL)
                .stepsNumber(6)
                .animationDuration(300).commit();
        stepView.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                int currentStep=stepView.getCurrentStep();

                if(step-currentStep<=1) {
                    changeFragment(step);
                }
            }
        });

        mapFragment=new MapFragment();
        routeFragment=new RouteFragment();
        inviteFriendsFragment=new InviteFriendsFragment();
        moreOptionsFragment=new MoreOptionsFragment();
        confirmationFragment=new ConfirmationFragment();
        overviewFragment=new OverviewFragment();
        manager=getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.content_fragment,mapFragment,mapFragment.getTag()).commit();

    }

    public void changeFragment(int step) {
        stepView.go(step,true);
        switch (step)
        {
            case 0:
                manager.beginTransaction().replace(R.id.content_fragment,mapFragment,mapFragment.getTag()).commit();
                break;
            case 1:
                manager.beginTransaction().replace(R.id.content_fragment, routeFragment,routeFragment.getTag()).commit();
                break;
            case 2:
                manager.beginTransaction().replace(R.id.content_fragment,inviteFriendsFragment,inviteFriendsFragment.getTag()).commit();
                break;
            case 3:
                manager.beginTransaction().replace(R.id.content_fragment, moreOptionsFragment,moreOptionsFragment.getTag()).commit();
                break;
            case 4:
                manager.beginTransaction().replace(R.id.content_fragment,confirmationFragment,confirmationFragment.getTag()).commit();
                break;
            case 5:
                manager.beginTransaction().replace(R.id.content_fragment, overviewFragment,overviewFragment.getTag()).commit();
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
            intent = new Intent(PassengerMainActivity.this,PassengerRideHistoryActivity.class);
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
    private void prepareNavigationDrawerList(){
        navDrawerItems.add(new NavDrawerItem(getString(R.string.payments), R.drawable.ic_baseline_payments));
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

        }else if(position == 1){
            intent=new Intent(PassengerMainActivity.this,PassengerInboxActivity.class);
            intent.putExtra("tab",1);
            startActivity(intent);
        }else if(position == 2){
            intent = new Intent(PassengerMainActivity.this, PassengerSettingsActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        }else if(position == 3){
            //..
        }

        navDrawerList.setItemChecked(position, true);

        navDrawerLayout.closeDrawer(navDrawerPane);

    }
    public void openInbox(View view)
    {
        Intent intent = new Intent(PassengerMainActivity.this, PassengerInboxActivity.class);
        intent.putExtra("tab",0);
        startActivity(intent);
    }
}