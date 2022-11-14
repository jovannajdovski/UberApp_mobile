package com.example.uberapp_tim12.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.DriverChatActivity;
import com.example.uberapp_tim12.activities.DriverInboxActivity;
import com.example.uberapp_tim12.adapters.ChatListAdapter;
import com.example.uberapp_tim12.model.ChatItem;
import com.example.uberapp_tim12.model.Message;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends ListFragment {

    public ChatFragment() {

    }

    private ArrayList<ChatItem> chatItems=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareChatList();
        ChatListAdapter adapter=new ChatListAdapter(getActivity(),chatItems);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    private void prepareChatList()
    {
        chatItems.add(new ChatItem("SUPPORT", "", R.drawable.ic_baseline_support, ChatItem.MessageType.SUPPORT));
        chatItems.add(new ChatItem("Kralja Aleksandra 7 - Preradoviceva 40", "13.11.2022. 14:00", R.drawable.ic_profile,ChatItem.MessageType.DRIVER));
        chatItems.add(new ChatItem("Kralja Aleksandra 7 - Preradoviceva 40", "13.11.2022. 14:00", R.drawable.ic_profile,ChatItem.MessageType.DRIVER));
        chatItems.add(new ChatItem("PANIC", "13.11.2022. 14:00", R.drawable.ic_baseline_alert,ChatItem.MessageType.PANIC));
        chatItems.add(new ChatItem("Kralja Aleksandra 7 - Preradoviceva 40", "13.11.2022. 14:00", R.drawable.ic_profile,ChatItem.MessageType.DRIVER));
        chatItems.add(new ChatItem("PANIC", "13.11.2022. 14:00", R.drawable.ic_baseline_alert,ChatItem.MessageType.PANIC));
        chatItems.add(new ChatItem("Kralja Aleksandra 7 - Preradoviceva 40", "13.11.2022. 14:00", R.drawable.ic_profile,ChatItem.MessageType.DRIVER));
        chatItems.add(new ChatItem("Kralja Aleksandra 7 - Preradoviceva 40", "13.11.2022. 14:00", R.drawable.ic_profile,ChatItem.MessageType.DRIVER));
        chatItems.add(new ChatItem("PANIC", "13.11.2022. 14:00", R.drawable.ic_baseline_alert,ChatItem.MessageType.PANIC));
    }
    private List<Message> prepareMessageList()
    {
        List<Message> messages=new ArrayList<>();
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.OTHERS));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.OTHERS));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.OTHERS));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.OTHERS));
        messages.add(new Message("AAAAAAAAAAAAAAAAAAAAAAAA", Message.Sender.MYSELF));
        return messages;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;
        intent=new Intent(getActivity(), DriverChatActivity.class);
        intent.putExtra("user", chatItems.get(position));
        intent.putExtra("messages", (Serializable) prepareMessageList());
        startActivity(intent);
    }

}