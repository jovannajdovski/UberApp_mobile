package com.example.uberapp_tim12.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverSettingsActivity extends AppCompatActivity {
    TextView profileName;
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
            Intent intent = new Intent(DriverSettingsActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
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

                } else {
                    showMessage(findViewById(android.R.id.content).getRootView(),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<DriverDetailsDTO> call, Throwable t) {
                showMessage(findViewById(android.R.id.content).getRootView(),
                        "Something went wrong!");
            }
        });
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

    private void showMessage(View view, String message) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }
}
