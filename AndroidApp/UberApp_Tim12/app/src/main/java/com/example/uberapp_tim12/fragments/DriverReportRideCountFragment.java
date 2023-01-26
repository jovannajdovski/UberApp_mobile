package com.example.uberapp_tim12.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.model.DailyRideCount;
import com.example.uberapp_tim12.model.RideCountStatistics;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.tools.SnackbarUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverReportRideCountFragment extends Fragment {
    LineChart lineChart;
    List<String> xAxisValues;
    RideCountStatistics statistics = new RideCountStatistics(new ArrayList<>(), 0, 0d);
    Button dropDownButton;
    TextView selectedDate, ride_count, ride_average_count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_report_ride_count, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();


        dropDownButton = view.findViewById(R.id.menu_button_ride);
        dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        ride_count = view.findViewById(R.id.ride_count_text);
        ride_average_count = view.findViewById(R.id.ride_average_count_text);


        lineChart = view.findViewById(R.id.line_chart);
        customizeLineChart();

        selectedDate = view.findViewById(R.id.selected_date);
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            LocalDateTime from =
                    Instant.ofEpochMilli(selection.first).atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime to =
                    Instant.ofEpochMilli(selection.second).atZone(ZoneId.systemDefault()).toLocalDateTime();
            getStatistics(view, from, to);
            selectedDate.setText(materialDatePicker.getHeaderText());
        });

    }

    private void getStatistics(View view, LocalDateTime from, LocalDateTime to) {
        Retrofit retrofit = ControllerUtils.retrofit;
        DriverController controller = retrofit.create(DriverController.class);

        Call<RideCountStatistics> call = controller.getRideCountStatistics(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer(), from, to);
        call.enqueue(new Callback<RideCountStatistics>() {
            @Override
            public void onResponse(Call<RideCountStatistics> call, Response<RideCountStatistics> response) {
                if (response.code() == 200) {
                    statistics = response.body();
                    updateUI();
                    updateGraph();
                } else {
                    SnackbarUtil.show(view, "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<RideCountStatistics> call, Throwable t) {
                SnackbarUtil.show(view, "Something went wrong!");
            }
        });
    }

    private void updateGraph() {
        xAxisValues = new ArrayList<>();

        ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < statistics.getCountPerDay().size(); i++) {
            DailyRideCount count = statistics.getCountPerDay().get(i);
            xAxisValues.add(String.valueOf(LocalDate.parse(count.getDay()).getDayOfMonth()));
            entries.add(new Entry(i, count.getCount()));
        }

        ArrayList<ILineDataSet> dataSets;
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(entries, "Ride Count");
        set1.setColor(Color.rgb(255, 120, 33));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(255, 120, 33));
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSets.add(set1);

        lineChart.getXAxis().setValueFormatter(
                new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));


        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.animateX(1000);
        lineChart.invalidate();
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
    }

    private void updateUI() {
        ride_count.setText(String.format("%s rides", statistics.getTotalCount().toString()));
        ride_average_count.setText(String.format("%s rides", statistics.getAverageCount().toString()));
    }

    private void customizeLineChart() {
        //customization
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);


        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setAxisLineColor(Color.GRAY);
        lineChart.getAxisLeft().setAxisLineWidth(1);
        lineChart.getAxisLeft().setGridColor(Color.GRAY);
        lineChart.getAxisLeft().setGridLineWidth(1.2f);
        lineChart.getAxisLeft().setTextColor(Color.rgb(92, 92, 92));
        lineChart.getAxisLeft().setTextSize(12);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setGranularity(1f);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(92, 92, 92));
        xAxis.setTextSize(12);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(5);
    }
}
