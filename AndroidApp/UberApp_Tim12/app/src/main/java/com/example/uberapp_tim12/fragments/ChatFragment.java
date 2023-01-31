package com.example.uberapp_tim12.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.ListFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.activities.ChatActivity;
import com.example.uberapp_tim12.adapters.ChatListAdapter;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.RideIdListDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.model_mock.ChatItem;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.RideService;
import com.example.uberapp_tim12.service.UserService;

import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends ListFragment {

    public ChatFragment() {

    }

    private ArrayList<ChatItem> chatItems=new ArrayList<>();
    private MessageListDTO messageListDTO;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(getActivity(), UserService.class);
        intent.putExtra("endpoint", "getMessages");
        getActivity().startService(intent);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    private RideIdListDTO prepareChatListWithoutRideDetails(MessageListDTO messageListDTO)
    {
        RideIdListDTO rideIdListDTO=new RideIdListDTO(new ArrayList<>()); //proveriti sa velikim brojem poruka
        if(messageListDTO.getTotalCount()>0) {
            int firstIndex=0, currentOtherUserId, lastOtherUserId=(messageListDTO.getMessages().get(0).getReceiverId())+(messageListDTO.getMessages().get(0).getSenderId())-LoggedUser.getUserId();
            MessageDTO firstMessageOfChat;
            int otherPersonId;
            for(int i=0;i<messageListDTO.getTotalCount();i++)
            {
                currentOtherUserId=(messageListDTO.getMessages().get(i).getReceiverId())+(messageListDTO.getMessages().get(i).getSenderId())-LoggedUser.getUserId();
                firstMessageOfChat=messageListDTO.getMessages().get(firstIndex);
                if(!Objects.equals(messageListDTO.getMessages().get(i).getRideId(), firstMessageOfChat.getRideId())
                    || currentOtherUserId!=lastOtherUserId)
                {
                    rideIdListDTO.getIds().add(firstMessageOfChat.getRideId());
                    //otherPersonId=firstMessageOfChat.getReceiverId()+ firstMessageOfChat.getSenderId()- LoggedUser.getUserId();
                    if(firstMessageOfChat.getRideId()==0)
                        chatItems.add(new ChatItem(R.drawable.ic_baseline_support, firstMessageOfChat.getRideId(), messageListDTO.getMessages().subList(firstIndex,i),lastOtherUserId));
                    else
                        chatItems.add(new ChatItem(R.drawable.ic_profile, firstMessageOfChat.getRideId(), messageListDTO.getMessages().subList(firstIndex,i),lastOtherUserId));
                    firstIndex=i;
                    lastOtherUserId=currentOtherUserId;
                }
            }
            //otherPersonId=messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getReceiverId()+ messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getSenderId()- LoggedUser.getUserId();

                rideIdListDTO.getIds().add(messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getRideId());
                if(messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getRideId()==0)
                    chatItems.add(new ChatItem(R.drawable.ic_baseline_support, messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getRideId(), messageListDTO.getMessages().subList(firstIndex, messageListDTO.getTotalCount()),lastOtherUserId));
                else
                    chatItems.add(new ChatItem(R.drawable.ic_profile, messageListDTO.getMessages().get(messageListDTO.getTotalCount() - 1).getRideId(), messageListDTO.getMessages().subList(firstIndex, messageListDTO.getTotalCount()),lastOtherUserId));

        }
        return rideIdListDTO;
    }
    private void prepareChatList(RidesListDTO ridesListDTO)
    {
        int j=0;
        for(int i=0;i<chatItems.size();i++)
        {
            if(chatItems.get(i).getRideId()==0) {
                chatItems.get(i).setRoute("LIVE SUPPORT");
                chatItems.get(i).setDateTime("");
                j--;
            }
            else{
                chatItems.get(i).setRoute(ridesListDTO.getRides().get(j).getLocations().iterator().next().getDeparture().getAddress()+" - "
                        +ridesListDTO.getRides().get(j).getLocations().iterator().next().getDestination().getAddress());
                chatItems.get(i).setDateTime(ridesListDTO.getRides().get(j).getStartTime());
            }

            j++;
        }
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent;
        intent=new Intent(getActivity(), ChatActivity.class);
        Log.d("PASSSS", "POZICIJA "+position);
        Log.d("PASSS", String.valueOf(chatItems.get(position).getMessages().size()));
        ChatItem chatItem=chatItems.get(position);
        intent.putExtra("chat", chatItem);
        //intent.putExtra("messages", (CharSequence) chatItem.getMessages());
        startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messagesReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(rideDetailsReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messagesReceiver, new IntentFilter("userMessages"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(rideDetailsReceiver, new IntentFilter("ridesDetails"));
    }
    public BroadcastReceiver messagesReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","on receive message receiver");
            messageListDTO=intent.getParcelableExtra("messageListDTO");
            if(messageListDTO.getTotalCount()>0) {
                RideIdListDTO rideIdListDTO = prepareChatListWithoutRideDetails(messageListDTO);
                Log.d("PASSSSS", "Voznje broj id-eva: "+rideIdListDTO.getIds().size());
                Intent intentSer=new Intent(getActivity(), RideService.class);
                intentSer.putExtra("rideIdList", rideIdListDTO);
                intentSer.putExtra("endpoint", "getRidesDetails");
                getActivity().startService(intentSer);
            }

        }
    };
    public BroadcastReceiver rideDetailsReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PASSSSSSSSS","on receive ride details receiver");
            RidesListDTO ridesListDTO=intent.getParcelableExtra("ridesDetailsDTO");
            if(ridesListDTO.getTotalCount()>0)
                prepareChatList(ridesListDTO);

            ChatListAdapter adapter=new ChatListAdapter(getActivity(),chatItems);
            setListAdapter(adapter);
        }
    };
}