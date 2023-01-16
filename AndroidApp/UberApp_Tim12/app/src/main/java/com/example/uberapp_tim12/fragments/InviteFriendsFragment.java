package com.example.uberapp_tim12.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.PassengerMainActivity;
import com.example.uberapp_tim12.adapters.CustomAdapter;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.dto.LocationDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.RideService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFriendsFragment extends Fragment {
    private PassengerMainActivity activity;
    private CreateRideDTO ride;
    private View view;
    private Set<UserRideDTO> passengers=new HashSet<>();
    public InviteFriendsFragment(CreateRideDTO ride) {
        this.ride=ride;
    }

    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected InviteFriendsFragment.LayoutManagerType mCurrentLayoutManagerType;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<String> dataSet=new ArrayList<>();

    public static InviteFriendsFragment newInstance(CreateRideDTO ride) {

        InviteFriendsFragment fragment = new InviteFriendsFragment(ride);
        return fragment;
    }

    public InviteFriendsFragment() {
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
        view=inflater.inflate(R.layout.fragment_invite_friends, container, false);
        Button next=(Button) view.findViewById(R.id.to_more_options);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passengers.add(new UserRideDTO(LoggedUser.getUserId(), LoggedUser.getUsername()));
                ride.setPassengers(passengers);
                activity.changeFragment(3,ride, dataSet);
            }
        });
        Button addFriend=view.findViewById(R.id.add_friend_button);
        EditText friendEmail=view.findViewById(R.id.friend_email_input);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PassengerService.class);
                intent.putExtra("email",String.valueOf(friendEmail.getText()));
                intent.putExtra("endpoint", "getPassengerByEmail");
                getActivity().startService(intent);
            }
        });


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = InviteFriendsFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (InviteFriendsFragment.LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);




        return view;
    }
    public void setRecyclerViewLayoutManager(InviteFriendsFragment.LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                mCurrentLayoutManagerType = InviteFriendsFragment.LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = InviteFriendsFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = InviteFriendsFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(userByEmailReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userByEmailReceiver, new IntentFilter("userByEmail"));
    }

    public BroadcastReceiver userByEmailReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","lalalalaalla");
            PassengerDTO passengerDTO=intent.getParcelableExtra("passengerDTO");
            if(passengerDTO==null)
            {
                String responseMessage=intent.getStringExtra("responseMessage");
                Toast.makeText(getActivity(),responseMessage,Toast.LENGTH_SHORT).show();
            }
            else{
                dataSet.add(passengerDTO.getName().concat(" ").concat(passengerDTO.getSurname()));

                String[] dataSetArray = new String[dataSet.size()];
                dataSet.toArray(dataSetArray);
                mAdapter = new CustomAdapter(dataSetArray);
                mRecyclerView.setAdapter(mAdapter);
                passengers.add(new UserRideDTO(passengerDTO.getId(),passengerDTO.getEmail()));
            }
        }
    };
}