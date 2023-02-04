package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.ChatMessageDTO;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.model_mock.ChatItem;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.UserService;
import com.example.uberapp_tim12.web_socket.STOMPUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity {
    private LinearLayout main_layout;
    private ChatItem chatItem;
    private ScrollView scroll;

    private STOMPUtils stompUtils;
    private Gson mGson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_chat);

        Intent intent=getIntent();
        chatItem= (ChatItem) intent.getParcelableExtra("chat");
        //List<MessageDTO> messages= (List<MessageDTO>) intent.getCharSequenceExtra("messages");
//        Log.d("PASSSS", String.valueOf(messages));
//        chatItem.setMessages(messages);
        Log.d("PASSSS", chatItem.toString());
        Log.d("PASSSS", chatItem.getRoute());
        Log.d("PASSSS", String.valueOf(chatItem.getMessages().size()));
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(chatItem.getIcon());
        actionBar.setTitle(chatItem.getRoute());

        main_layout = findViewById(R.id.messages_stack);
        for(MessageDTO message:chatItem.getMessages())
        {
            main_layout.addView(create(message));
        }
        scroll=findViewById(R.id.scroll);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });

        registerSocketForRideChat(LoggedUser.getUserId());

        EditText newMessage=findViewById(R.id.message_text);
        ImageButton sendButton=findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this, UserService.class);
                intent.putExtra("endpoint", "sendMessage");
                SendingMessageDTO sendingMessageDTO=new SendingMessageDTO(newMessage.getText().toString(),chatItem.getRideId());
                newMessage.setText("");
                intent.putExtra("messageDTO", sendingMessageDTO);
                Log.d("PASSSS", "other person id "+chatItem.getOtherPersonId());
                intent.putExtra("receiverId", chatItem.getOtherPersonId());
                ChatActivity.this.startService(intent);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void registerSocketForRideChat(Integer userId){
        stompUtils = new STOMPUtils();
        stompUtils.connectStomp();

        stompUtils.stompClient.topic("api/socket-publisher/user-chat/"+userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("RIDECHAT", "Received " + topicMessage.getPayload());
                    ChatMessageDTO chatMessage = mGson.fromJson(topicMessage.getPayload(), ChatMessageDTO.class);
                    if (chatMessage.getFromId()!=userId){
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setMessage(chatMessage.getMessage());
                        messageDTO.setRideId(chatMessage.getRideId());
                        messageDTO.setSenderId(chatMessage.getFromId());
                        main_layout.addView(create(messageDTO));
                        scroll.post(new Runnable() {
                            @Override
                            public void run() {
                                scroll.fullScroll(View.FOCUS_DOWN);
                            }
                        });
                    }
                }, throwable -> {
                    Log.e("RIDECHAT", "Error on subscribe topic", throwable);
                });
    }

    private RelativeLayout create(MessageDTO message)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout;
        TextView textView;
        if(Objects.equals(message.getSenderId(), LoggedUser.getUserId())) {
            layout= (RelativeLayout) vi.inflate(R.layout.user_message_layout, null);
            textView = (TextView) layout.findViewById(R.id.message);
        }
        else {
            layout= (RelativeLayout) vi.inflate(R.layout.other_message_layout, null);
            textView = (TextView) layout.findViewById(R.id.message);
        }
        textView.setText(message.getMessage());
        return layout;
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sendMessageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(sendMessageReceiver, new IntentFilter("sendMessage"));
    }
    public BroadcastReceiver sendMessageReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getSerializableExtra("messageDTO") == null)
                return;

            MessageDTO messageDTO= (MessageDTO) intent.getSerializableExtra("messageDTO");
            chatItem.getMessages().add(messageDTO);

            if (Objects.equals(messageDTO.getSenderId(), LoggedUser.getUserId())){
                main_layout.addView(create(messageDTO));
                scroll.fullScroll(View.FOCUS_DOWN);
            }

            sendMessageSocket(messageDTO.getRideId(),messageDTO.getSenderId(),messageDTO.getReceiverId(), messageDTO.getMessage());
        }
    };

    @SuppressLint("CheckResult")
    public void sendMessageSocket(Integer rideId, Integer fromId,Integer toId, String message){
        stompUtils.stompClient.send("api/socket-subscriber/send/message/"+rideId+"/"+fromId+"/"+toId, message)
                .compose(stompUtils.applySchedulers())
                .subscribe(() -> {
                    Log.d("RIDECHAT", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("RIDECHAT", "Error send STOMP echo", throwable);
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stompUtils.disconnectStomp();
    }
}