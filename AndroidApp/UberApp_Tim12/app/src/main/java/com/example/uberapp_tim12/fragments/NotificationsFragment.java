package com.example.uberapp_tim12.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.ChatActivity;
import com.example.uberapp_tim12.adapters.NotificationsListAdapter;
import com.example.uberapp_tim12.dto.ErrorMessageDTO;
import com.example.uberapp_tim12.dto.MultipleSendingMessageDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.dto.UserRideDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.model_mock.Message;
import com.example.uberapp_tim12.model_mock.NotificationItem;
import com.example.uberapp_tim12.service.RideService;
import com.example.uberapp_tim12.service.UserService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NotificationsFragment extends ListFragment {

    public NotificationsFragment() {

    }

    List<Button> acceptButtons=new ArrayList<>();
    List<Button> rejectButtons=new ArrayList<>();
    List<RelativeLayout> layouts= new ArrayList<>();
    Integer remainedPendingRides;
    FrameLayout frameLayout;


    private ArrayList<NotificationItem> notificationItems=new ArrayList<>();
    private View view;
    private View blackBackground;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareNotificationsList();
        Intent intent=new Intent(getActivity(), RideService.class);
        intent.putExtra("endpoint", "getPendingRidesForDriver");
        Log.d("PASSSSSS", "STARTSERVICE\t\tPASSS");
        getActivity().startService(intent);
        NotificationsListAdapter adapter=new NotificationsListAdapter(getActivity(),notificationItems);
        setListAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_notifications, container, false);
        blackBackground=view.findViewById(R.id.black_background);

//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(pendingRidesReceiver, new IntentFilter("pendingRides"));
        return view;
    }



    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(pendingRidesReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(acceptRideReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rejectRideReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PASSSSSS", "RESUME\t\tPASSS");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(pendingRidesReceiver, new IntentFilter("pendingRides"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(acceptRideReceiver, new IntentFilter("acceptRide"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rejectRideReceiver, new IntentFilter("rejectRide"));
    }
    private void prepareNotificationsList()
    {
        //ako je ulogovan vozac
            //ako ima voznja koja je pending moze da se klikne na tu notifikaciju i onda se otvara dialog sa informacijama i dugmadima
            //ako je voznja accepted i trenutno vreme je vece od vremena pocekta voznje, onda se pojavljuje dugme start
        //ako je ulogovan putnik
            //ako ima voznja na koju je pozvan stize mu obavestenje o njoj
        notificationItems.add(new NotificationItem(
                "Kralja Aleksandra 7 - Preradoviceva 40",
                "13.11.2022. 14:00",
                NotificationItem.NotificationType.REMINDER,
                "Reminder",
                "Your ride is in 5 minutes",
                "13:55"));
        notificationItems.add(new NotificationItem(
                "Kralja Aleksandra 7 - Preradoviceva 40",
                "13.11.2022. 14:00",
                NotificationItem.NotificationType.REMINDER,
                "Ride cancellation",
                "Driver is sick",
                "13:00"));
        notificationItems.add(new NotificationItem(
                "Kralja Aleksandra 7 - Preradoviceva 40",
                "13.11.2022. 14:00",
                NotificationItem.NotificationType.REMINDER,
                "Reminder",
                "Your ride is in 5 minutes",
                "13:55"));
        notificationItems.add(new NotificationItem(
                "Kralja Aleksandra 7 - Preradoviceva 40",
                "13.11.2022. 14:00",
                NotificationItem.NotificationType.REMINDER,
                "Reminder",
                "Your ride is in 5 minutes",
                "13:55"));
        notificationItems.add(new NotificationItem(
                "Kralja Aleksandra 7 - Preradoviceva 40",
                "13.11.2022. 14:00",
                NotificationItem.NotificationType.REMINDER,
                "Reminder",
                "Your ride is in 5 minutes",
                "13:55"));
    }
    private RelativeLayout createAcceptanceRideDialog(Ride ride)
    {
        View blackBackground=view.findViewById(R.id.black_background);
        blackBackground.setVisibility(View.VISIBLE);

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout=(RelativeLayout) vi.inflate(R.layout.acceptance_ride_layout,null);
        TextView departure=layout.findViewById(R.id.new_ride_departure);
        TextView destination=layout.findViewById(R.id.new_ride_destination);
        TextView cost=layout.findViewById(R.id.new_ride_cost);
        TextView time=layout.findViewById(R.id.new_ride_time);
        departure.setText(ride.getLocations().iterator().next().getDeparture().getAddress());
        destination.setText(ride.getLocations().iterator().next().getDestination().getAddress());
        cost.setText(String.valueOf(ride.getTotalCost()));
        String dateTime=ride.getStartTime();
        time.setText(dateTime.substring(dateTime.indexOf('T')+1,dateTime.indexOf('T')+6));



        return layout;
    }
    private void setListeners(RelativeLayout layout, Ride ride)
    {
        Intent intent=new Intent(getActivity(), RideService.class);

        Button acceptButton=layout.findViewById(R.id.new_ride_accept_button);
        Button rejectButton=layout.findViewById(R.id.new_ride_reject_button);

        EditText rejectReason=layout.findViewById(R.id.new_ride_rejection_reason);

        acceptButtons.add(acceptButton);
        rejectButtons.add(rejectButton);
        Log.d("PASSS", "nasao dugmad");
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PASSS","Kliknut ".concat(ride.getId().toString()));
                acceptButtons.remove(0);
                rejectButtons.remove(0);
                Log.d("PASSS", "acceptClick");
                intent.putExtra("rideId", ride.getId());
                intent.putExtra("endpoint", "acceptRide");
                getActivity().startService(intent);
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PASSS","Kliknut ".concat(ride.getId().toString()));
                acceptButtons.remove(0);
                rejectButtons.remove(0);
                Log.d("PASSS", "rejectClick");
                ReasonDTO reasonDTO=new ReasonDTO(rejectReason.getText().toString());
                intent.putExtra("reasonDTO", reasonDTO);
                intent.putExtra("rideId", ride.getId());
                intent.putExtra("endpoint", "rejectRide");
                getActivity().startService(intent);
            }
        });
    }

    public BroadcastReceiver pendingRidesReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","lalalalaalla");
            RidesListDTO ridesListDTO=intent.getParcelableExtra("pendingRidesDTO");
            Log.d("PASSSSSSSSS",ridesListDTO.toString());
            frameLayout=view.findViewById(R.id.acceptance_ride_layout);
            remainedPendingRides=ridesListDTO.getTotalCount();
            //RelativeLayout layout;
            Ride ride;
            for(int i=ridesListDTO.getTotalCount()-1; i>=0;i--)
            {
                ride=ridesListDTO.getRides().get(i);
                RelativeLayout layout=createAcceptanceRideDialog(ride);
                layouts.add(layout);
                frameLayout.addView(layout);
                Log.d("PASSS","Napravljen ".concat(ride.getId().toString()));
                setListeners(layout, ride);
            }
        }
    };
    public BroadcastReceiver acceptRideReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSS", "ACCEPTED");
            Ride ride= (Ride) intent.getSerializableExtra("ride");
            Log.d("PASSSid", String.valueOf(ride.getId()));
            RelativeLayout layout1=layouts.remove(layouts.size()-1);
            Log.d("PASSS layout", String.valueOf(layouts.size()));
            layout1.setVisibility(View.GONE);
            //frameLayout.removeView(layout1);
            if(layouts.size()==0)
                blackBackground.setVisibility(View.GONE);
            Intent createChatIntent=new Intent(getActivity(), UserService.class);
            createChatIntent.putExtra("endpoint", "multipleSendMessage");
            SendingMessageDTO sendingMessageDTO=new SendingMessageDTO("Ride is accepted",ride.getId());
            MultipleSendingMessageDTO multipleSendingMessageDTO=new MultipleSendingMessageDTO(sendingMessageDTO, new ArrayList<>());
            for(UserRideDTO passenger: ride.getPassengers())
            {
                multipleSendingMessageDTO.getUserIds().add(passenger.getId());
            }
            Gson gson = new Gson();
            String obj=gson.toJson(multipleSendingMessageDTO);
            Log.d("PASSS", obj);
            createChatIntent.putExtra("multipleMessageDTO", multipleSendingMessageDTO);
            getActivity().startService(createChatIntent);
        }
    };
    public BroadcastReceiver rejectRideReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSS", "REJECTED");
            Ride ride= (Ride) intent.getSerializableExtra("ride");
            Log.d("PASSSid", String.valueOf(ride.getId()));
            Log.d("PASSS layout", String.valueOf(layouts.size()));
            RelativeLayout layout1=layouts.remove(layouts.size()-1);

            layout1.setVisibility(View.GONE);
            //frameLayout.removeView(layout1);
            if(layouts.size()==0)
                blackBackground.setVisibility(View.GONE);

        }
    };
}