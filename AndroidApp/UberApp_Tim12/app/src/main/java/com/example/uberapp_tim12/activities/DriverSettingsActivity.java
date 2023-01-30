package com.example.uberapp_tim12.activities;

import static com.example.uberapp_tim12.activities.DriverMainActivity.ongoingWorkHours;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.tools.ImageConverter;
import com.example.uberapp_tim12.tools.SnackbarUtil;
import com.example.uberapp_tim12.service.DriverService;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverSettingsActivity extends AppCompatActivity {
    TextView profileName;
    ImageView profileImage;
    DriverDetailsDTO driverDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_settings);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");

        profileName = findViewById(R.id.profile_name);
        profileImage = findViewById(R.id.profile_image);


        MaterialCardView profileLayout = findViewById(R.id.edit_profile);
        profileLayout.setOnClickListener(view -> {
            Intent intent = new Intent(DriverSettingsActivity.this, DriverAccountActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        MaterialCardView statisticLayout = findViewById(R.id.statistics);
        statisticLayout.setOnClickListener(view -> {
            Intent intent = new Intent(DriverSettingsActivity.this, DriverStatisticsActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        MaterialCardView reportLayout = findViewById(R.id.report);
        reportLayout.setOnClickListener(view -> {
            Intent intent = new Intent(DriverSettingsActivity.this, DriverReportActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        Button logOutButton = findViewById(R.id.logoutBtn);
        logOutButton.setOnClickListener(view -> {
            Intent intent = new Intent(DriverSettingsActivity.this, DriverService.class);
            if(DriverMainActivity.switchOffWhenIsNotPossible || ongoingWorkHours==null)
                intent.putExtra("workHourId", 0);
            else
                intent.putExtra("workHourId", ongoingWorkHours.getId());
            intent.putExtra("endpoint", "endShift");
            startService(intent);
        });

        updateUI();
    }

    private void updateUI() {
        Retrofit retrofit = ControllerUtils.retrofit;
        DriverController controller = retrofit.create(DriverController.class);

        Call<DriverDetailsDTO> call = controller.getDriverDetails(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<DriverDetailsDTO>() {
            @Override
            public void onResponse(Call<DriverDetailsDTO> call, Response<DriverDetailsDTO> response) {
                if (response.code() == 200) {
                    driverDetails = response.body();
                    profileName.setText(String.format("%s %s", driverDetails.getName(),
                            driverDetails.getSurname()));
                    if (driverDetails.getProfilePicture() != null)
                        profileImage.setImageBitmap(ImageConverter.decodeToImage(driverDetails.getProfilePicture()));

                } else {
                    SnackbarUtil.show(findViewById(R.id.driver_settings),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<DriverDetailsDTO> call, Throwable t) {
                SnackbarUtil.show(findViewById(R.id.driver_settings),
                        "Something went wrong!");
        }});
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(endShiftReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(endShiftReceiver, new IntentFilter("endShift"));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public BroadcastReceiver endShiftReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            ongoingWorkHours=null;
            Intent intentAct = new Intent(DriverSettingsActivity.this, UserLoginActivity.class);
            startActivity(intentAct);
            finish();
        }
    };
}
