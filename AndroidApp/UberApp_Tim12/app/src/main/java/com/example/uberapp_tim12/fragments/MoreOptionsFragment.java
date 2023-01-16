package com.example.uberapp_tim12.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.PassengerMainActivity;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.model.VehicleCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreOptionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreOptionsFragment extends Fragment {

    private PassengerMainActivity activity;
    private CreateRideDTO ride;
    private List<String> passengers;

    public MoreOptionsFragment(CreateRideDTO ride, List<String> passengers) {
        this.ride=ride;
        this.passengers=passengers;
    }


    public static MoreOptionsFragment newInstance(CreateRideDTO ride, List<String> passengers) {

        MoreOptionsFragment fragment = new MoreOptionsFragment(ride, passengers);
        return fragment;
    }

    public MoreOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=(PassengerMainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_more_options, container, false);
        Button next=(Button) view.findViewById(R.id.next_button_options);
        CheckBox pets=view.findViewById(R.id.pets_checkbox);
        CheckBox babies=view.findViewById(R.id.baby_checkbox);
        Spinner category=view.findViewById(R.id.spinner);
        setSpinner(category);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ride.setBabyTransport(babies.isChecked());
                ride.setPetTransport(pets.isChecked());
                int categoryPosition=category.getSelectedItemPosition();
                if(categoryPosition!=0)
                {
                    categoryPosition--;
                    ride.setVehicleType(VehicleCategory.values()[categoryPosition]);
                }
                activity.changeFragment(4, ride, passengers);
            }
        });
        return view;
    }

    private void setSpinner(Spinner spinner) {
        ArrayList<String> categories = new ArrayList<>();
        categories.add("");
        categories.add("STANDARD");
        categories.add("LUXURY");
        categories.add("VAN");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }
}