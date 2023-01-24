package com.example.uberapp_tim12.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.controller.RideController;
import com.example.uberapp_tim12.dto.TimeSpanDTO;
import com.example.uberapp_tim12.model.DriverStatistics;
import com.example.uberapp_tim12.model.FavouriteRide;
import com.example.uberapp_tim12.security.LoggedUser;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverStatisticsActivity extends AppCompatActivity {
    DriverStatistics statistics = new DriverStatistics(0, 0, 0, 0);
    Button dropDownButton;
    TextView acceptedRides, rejectedRides, workHours, totalEarned;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_statistic);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Statistics");

        dropDownButton = findViewById(R.id.menu_button_ride);
        dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, R.menu.time_span_menu);
            }
        });

        acceptedRides = findViewById(R.id.accepted_rides);
        rejectedRides = findViewById(R.id.declined_rides);
        workHours = findViewById(R.id.work_hours);
        totalEarned = findViewById(R.id.total_earned);
        updateUI();
    }

    private void showMenu(View v, @MenuRes Integer menuRes) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                LocalDateTime from, to;
                from = LocalDateTime.now();
                to = LocalDateTime.now();

                switch (menuItem.getTitle().toString()) {
                    case "Today":
                        dropDownButton.setText("Today");
                        from = LocalDateTime.now().toLocalDate().atTime(LocalTime.MIN);
                        break;
                    case "This week":
                        dropDownButton.setText("This week");
                        from = to.with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
                                .toLocalDate().atTime(LocalTime.MIN);
                        break;
                    case "This month":
                        dropDownButton.setText("This month");
                        from = to.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atTime(LocalTime.MIN);;
                        break;

                }
                getStatistics(v, from, to);

                return false;
            }
        });

        popup.show();
    }

    private void getStatistics(View view, LocalDateTime from, LocalDateTime to) {
        Retrofit retrofit = ControllerUtils.retrofit;
        DriverController controller = retrofit.create(DriverController.class);

        Call<DriverStatistics> call = controller.getDriverStatistics(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer(), from, to);
        call.enqueue(new Callback<DriverStatistics>() {
                         @Override
                         public void onResponse(Call<DriverStatistics> call, Response<DriverStatistics> response) {
                             if (response.code() == 200) {
                                 statistics = response.body();
                                 updateUI();
                             } else {
                                 showMessage(view, "Something went wrong!");
                             }
                         }

                         @Override
                         public void onFailure(Call<DriverStatistics> call, Throwable t) {
                             showMessage(view, "Something went wrong!");
                         }
                     }
        );
    }

    private void updateUI() {
        acceptedRides.setText(String.format(statistics.getNumberOfAcceptedRides().toString()));
        rejectedRides.setText(String.format(statistics.getNumberOfRejectedRides().toString()));
        workHours.setText(String.format("%s h", statistics.getTotalWorkHours().toString()));
        totalEarned.setText(String.format("%s $", statistics.getTotalIncome().toString()));
    }

    private void showMessage(View view, String message) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }
}
