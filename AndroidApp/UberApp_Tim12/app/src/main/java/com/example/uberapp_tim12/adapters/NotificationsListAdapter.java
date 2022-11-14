package com.example.uberapp_tim12.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model.ChatItem;
import com.example.uberapp_tim12.model.NotificationItem;

import java.util.ArrayList;

public class NotificationsListAdapter extends BaseAdapter {
    Context context;
    ArrayList<NotificationItem> items;
    public NotificationsListAdapter(Context context, ArrayList<NotificationItem> items){
        this.context=context;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {return items.get(position);}

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.notifications_list_item, null);
        }
        else{
            view=convertView;
        }
        TextView routeView = view.findViewById(R.id.route);
        TextView dateTimeView = view.findViewById(R.id.date_time);
        RelativeLayout iconView = view.findViewById(R.id.icon);
        TextView infoView = view.findViewById(R.id.info);
        TextView reasonView = view.findViewById(R.id.reason);
        TextView notificationDateTimeView = view.findViewById(R.id.notification_date_time);

        routeView.setText( items.get(position).getRoute());
        dateTimeView.setText(items.get(position).getDateTime());
        infoView.setText(items.get(position).getInformation());
        reasonView.setText(items.get(position).getReason());
        notificationDateTimeView.setText(items.get(position).getNotificationDateTime());

        if(items.get(position).getNotificationType()== NotificationItem.NotificationType.REMINDER)
            iconView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_reminder));
        else
            iconView.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_baseline_cancel_24));


        return view;
    }
}
