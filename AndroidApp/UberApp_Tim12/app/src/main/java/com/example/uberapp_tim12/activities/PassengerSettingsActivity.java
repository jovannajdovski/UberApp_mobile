package com.example.uberapp_tim12.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.controller.PassengerController;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.model_mock.User;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.tools.ImageConverter;
import com.example.uberapp_tim12.tools.SnackbarUtil;
import com.example.uberapp_tim12.tools.UserMockup;
import com.google.android.material.card.MaterialCardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PassengerSettingsActivity extends AppCompatActivity {
    TextView profileName;
    ImageView profileImage;
    PassengerDetailsDTO passengerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_settings);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
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
            Intent intent = new Intent(PassengerSettingsActivity.this, PassengerAccountActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        MaterialCardView favouriteRoutesLayout = findViewById(R.id.favourite_routes);
        favouriteRoutesLayout.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerSettingsActivity.this, PassengerFavouriteRoutesActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        MaterialCardView reportLayout = findViewById(R.id.report);
        reportLayout.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerSettingsActivity.this, PassengerReportActivity.class);
            User user = UserMockup.getUser();
            intent.putExtra("user", user);
            startActivity(intent);
        });

        Button logOutButton = findViewById(R.id.logoutBtn);
        logOutButton.setOnClickListener(view -> {
            Intent intent = new Intent(PassengerSettingsActivity.this, UserLoginActivity.class);
            startActivity(intent);
            finish();
        });

        updateUI();
    }

    private void updateUI() {
        Retrofit retrofit = ControllerUtils.retrofit;
        PassengerController controller = retrofit.create(PassengerController.class);

        Call<PassengerDetailsDTO> call = controller.getPassengerDetails(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<PassengerDetailsDTO>() {
            @Override
            public void onResponse(Call<PassengerDetailsDTO> call, Response<PassengerDetailsDTO> response) {
                if (response.code() == 200) {
                    passengerDetails = response.body();
                    profileName.setText(String.format("%s %s", passengerDetails.getName(),
                            passengerDetails.getSurname()));
                    if (passengerDetails.getProfilePicture() != null)
                        profileImage.setImageBitmap(ImageConverter.decodeToImage(passengerDetails.getProfilePicture()));

                } else {
                    SnackbarUtil.show(findViewById(R.id.passenger_settings),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<PassengerDetailsDTO> call, Throwable t) {
                SnackbarUtil.show(findViewById(R.id.passenger_settings),
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
}
