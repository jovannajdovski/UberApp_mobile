package com.example.uberapp_tim12.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.ListFragment;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.NotificationsListAdapter;
import com.example.uberapp_tim12.model.NotificationItem;

import java.util.ArrayList;

public class NotificationsFragment extends ListFragment {

    public NotificationsFragment() {

    }

    private ArrayList<NotificationItem> notificationItems=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareNotificationsList();
        NotificationsListAdapter adapter=new NotificationsListAdapter(getActivity(),notificationItems);
        setListAdapter(adapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        RelativeLayout listView= (RelativeLayout) view.findViewById(R.id.list_view);
        View blackBackground=view.findViewById(R.id.black_background);
        RelativeLayout newRideView=(RelativeLayout) view.findViewById(R.id.new_ride_view);
        boolean newRide=true; //dobijace se od drugde
        if(newRide)
        {
            newRideView.setVisibility(View.VISIBLE);
            blackBackground.setVisibility(View.VISIBLE);
            //listView.setVisibility(View.GONE);
        }
        else{
            newRideView.setVisibility(View.GONE);
            blackBackground.setVisibility(View.GONE);
        }
        return view;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        v.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.black));
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
}