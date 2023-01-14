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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.NotificationsListAdapter;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.model_mock.Message;
import com.example.uberapp_tim12.model_mock.NotificationItem;
import com.example.uberapp_tim12.service.RideService;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment {

    public NotificationsFragment() {

    }

    private ArrayList<NotificationItem> notificationItems=new ArrayList<>();
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareNotificationsList();
        Intent intent=new Intent(getActivity(), RideService.class);
        intent.putExtra("endpoint", "getPendingRidesForDriver");
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
        return view;
    }

    public BroadcastReceiver bReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","lalalalaalla");
            RidesListDTO ridesListDTO=intent.getParcelableExtra("pendingRidesDTO");
            Log.d("PASSSSSSSSS",ridesListDTO.toString());
            FrameLayout frameLayout=view.findViewById(R.id.acceptance_ride_layout);

            for(int i=ridesListDTO.getTotalCount()-1; i>=0;i--)
            {
                frameLayout.addView(createAcceptanceRideDialog(ridesListDTO.getRides().get(i)));
            }
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(bReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(bReceiver, new IntentFilter("pendingRides"));
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
                NotificationItem.NotificationType.CANCELLATION,
                "Ride cancellation",
                "The driver buries the dog",
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
}