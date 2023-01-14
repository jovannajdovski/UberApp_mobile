package com.example.uberapp_tim12.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model_mock.ChatItem;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    Context context;
    ArrayList<ChatItem> items;
    public ChatListAdapter(Context context, ArrayList<ChatItem> items){
        this.context=context;
        this.items=items;
    }
    @Override
    public int getCount() {return items.size();}

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
            view=inflater.inflate(R.layout.chat_list_item, null);
        }
        else{
            view=convertView;
        }
        TextView routeView = view.findViewById(R.id.route);
        TextView dateTimeView = view.findViewById(R.id.date_time);
        ImageView iconView = view.findViewById(R.id.icon);
        RelativeLayout chatItemView=view.findViewById(R.id.chat_item);
        routeView.setText( items.get(position).getRoute());
        dateTimeView.setText(items.get(position).getDateTime());
        iconView.setImageResource(items.get(position).getIcon());
        chatItemView.setBackgroundColor(items.get(position).getColor());

        return view;
    }
}
