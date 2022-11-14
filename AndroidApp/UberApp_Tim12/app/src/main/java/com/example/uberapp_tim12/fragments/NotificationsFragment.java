package com.example.uberapp_tim12.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

    }
    private void prepareNotificationsList()
    {
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