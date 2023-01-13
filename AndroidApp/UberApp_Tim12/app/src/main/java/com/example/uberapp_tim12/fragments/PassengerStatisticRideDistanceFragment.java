package com.example.uberapp_tim12.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;

import com.example.uberapp_tim12.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PassengerStatisticRideDistanceFragment extends Fragment {
    LineChart lineGraph;
    List<String> xAxisValues;
    Button dropDownButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_statistic_ride_distance, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dropDownButton = view.findViewById(R.id.menu_button_ride);
        dropDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, R.menu.popup_menu);
            }
        });

        lineGraph = view.findViewById(R.id.line_chart);
        customizeLineChart();
        populateGraphWithYearData();
    }

    private void showMenu(View v, @MenuRes Integer menuRes) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getTitle().toString()) {
                    case "Day":
                        dropDownButton.setText("Day");
                        populateGraphWithDayData();
                        break;
                    case "Week":
                        dropDownButton.setText("Week");
                        populateGraphWithWeekData();
                        break;
                    case "Month":
                        dropDownButton.setText("Month");
                        populateGraphWithMonthData();
                        break;
                    case "Year":
                        dropDownButton.setText("Year");
                        populateGraphWithYearData();
                        break;
                }
                return false;
            }
        });

        popup.show();
    }

    private void populateGraphWithDayData() {
        xAxisValues = new ArrayList<>(Arrays.asList("00", "06", "12", "18", "24"));

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
        set1.setColor(Color.rgb(246, 190, 0));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(246, 190, 0));
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
    }

    private void populateGraphWithWeekData() {
        xAxisValues = new ArrayList<>(Arrays.asList("M", "T", "W", "T", "F", "S", "S"));

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 5));
        entries.add(new Entry(1, 6));
        entries.add(new Entry(2, 4));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 6));
        entries.add(new Entry(5, 3));
        entries.add(new Entry(6, 2));

        ArrayList<ILineDataSet> dataSets;
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(entries, "Income");
        set1.setColor(Color.rgb(246, 190, 0));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(246, 190, 0));
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
    }

    private void populateGraphWithMonthData() {
        xAxisValues = new ArrayList<>(Arrays.asList("2", "9", "16", "23", "30"));

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 5));
        entries.add(new Entry(1, 6));
        entries.add(new Entry(2, 4));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 6));

        ArrayList<ILineDataSet> dataSets;
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(entries, "Income");
        set1.setColor(Color.rgb(246, 190, 0));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(246, 190, 0));
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
    }

    private void populateGraphWithYearData() {
        xAxisValues = new ArrayList<>(Arrays.asList("Jan", "Mar", "Jun", "Sep", "Dec"));

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 5));
        entries.add(new Entry(1, 6));
        entries.add(new Entry(2, 4));
        entries.add(new Entry(3, 3));
        entries.add(new Entry(4, 30));

        ArrayList<ILineDataSet> dataSets;
        dataSets = new ArrayList<>();
        LineDataSet set1;

        set1 = new LineDataSet(entries, "Income");
        set1.setColor(Color.rgb(246, 190, 0));
        set1.setLineWidth(2);
        set1.setValueTextColor(Color.rgb(246, 190, 0));
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
    }

    private void customizeLineChart() {
        //customization
        lineGraph.setTouchEnabled(true);
        lineGraph.setDragEnabled(true);
        lineGraph.setScaleEnabled(false);
        lineGraph.setPinchZoom(false);
        lineGraph.setDrawGridBackground(false);
//        lineGraph.setExtraLeftOffset(15);
//        lineGraph.setExtraRightOffset(15);

        //to hide background lines
        lineGraph.getXAxis().setDrawGridLines(false);
        lineGraph.getXAxis().setAxisLineColor(Color.GRAY);
        lineGraph.getXAxis().setAxisLineWidth(1);


        lineGraph.getAxisLeft().setDrawGridLines(true);
        lineGraph.getAxisLeft().setGridColor(Color.GRAY);
        lineGraph.getAxisLeft().setAxisLineColor(Color.GRAY);
        lineGraph.getAxisLeft().setAxisLineWidth(1);
        lineGraph.getAxisLeft().setTextColor(Color.rgb(92, 92, 92));
        lineGraph.getAxisLeft().setTextSize(12);

        lineGraph.getAxisRight().setEnabled(false);
        lineGraph.getAxisRight().setDrawGridLines(false);
        lineGraph.getAxisRight().setDrawLabels(false);

        //to hide right Y and top X border
//        YAxis rightYAxis = lineGraph.getAxisRight();
//        rightYAxis.setEnabled(false);
//        YAxis leftYAxis = lineGraph.getAxisLeft();
//        leftYAxis.setEnabled(false);

        XAxis xAxis = lineGraph.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.rgb(92, 92, 92));
        xAxis.setTextSize(12);

    }
}
