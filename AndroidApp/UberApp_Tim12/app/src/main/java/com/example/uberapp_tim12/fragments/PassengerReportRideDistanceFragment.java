package com.example.uberapp_tim12.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.MenuRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim12.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassengerReportRideDistanceFragment extends Fragment {
    LineChart lineGraph;
    List<String> xAxisValues;
    Button dropDownButton;
    TextView selectedDate, distance, average_distance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_report_ride_distance, parent, false);
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

        distance = view.findViewById(R.id.distance_text);
        average_distance = view.findViewById(R.id.average_distance_text);

        lineGraph = view.findViewById(R.id.line_chart);
        customizeLineChart();
//        showEmptyChart();

        selectedDate = view.findViewById(R.id.selected_date);
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        selectedDate.setText(materialDatePicker.getHeaderText());
                        populateGraphWithDayData();
                    }
                });

    }

    private void populateGraphWithDayData() {
        xAxisValues = new ArrayList<>(Arrays.asList("12", "13", "14", "15", "16"));

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 25));
        entries.add(new Entry(3, 10));
        entries.add(new Entry(4, 35));

        ArrayList<ILineDataSet> dataSets;
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(entries, "Income");
        set1.setColor(Color.rgb(56, 186, 247));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(56, 186, 247));
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSets.add(set1);

        lineGraph.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));

        LineData data = new LineData(dataSets);
        lineGraph.setData(data);
        lineGraph.animateX(1000);
        lineGraph.invalidate();
        lineGraph.getLegend().setEnabled(false);
        lineGraph.getDescription().setEnabled(false);

        distance.setText("100 km");
        average_distance.setText("50km");
    }

    private void showEmptyChart() {
        YAxis yAxis = lineGraph.getAxisLeft();
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(0f);

        xAxisValues = new ArrayList<>(Arrays.asList("1", "15", "30"));
        LineData data = new LineData();
        lineGraph.setData(data);
        lineGraph.invalidate();
    }

    private void customizeLineChart() {
        //customization
        lineGraph.setTouchEnabled(true);
        lineGraph.setDragEnabled(true);
        lineGraph.setScaleEnabled(false);
        lineGraph.setPinchZoom(false);
        lineGraph.setDrawGridBackground(false);


        lineGraph.getAxisLeft().setDrawGridLines(true);
        lineGraph.getAxisLeft().setAxisLineColor(Color.GRAY);
        lineGraph.getAxisLeft().setAxisLineWidth(1);
        lineGraph.getAxisLeft().setGridColor(Color.GRAY);
        lineGraph.getAxisLeft().setGridLineWidth(1.2f);
        lineGraph.getAxisLeft().setTextColor(Color.rgb(92, 92, 92));
        lineGraph.getAxisLeft().setTextSize(12);

        lineGraph.getAxisRight().setEnabled(false);
        lineGraph.getAxisRight().setDrawGridLines(false);
        lineGraph.getAxisRight().setDrawLabels(false);

        XAxis xAxis = lineGraph.getXAxis();
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setGranularity(1f);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(92, 92, 92));
        xAxis.setTextSize(12);
        xAxis.setDrawGridLines(false);
    }
}
