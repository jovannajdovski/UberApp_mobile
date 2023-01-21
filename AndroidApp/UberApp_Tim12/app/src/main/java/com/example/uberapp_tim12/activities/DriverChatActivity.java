package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.adapters.ChatListAdapter;
import com.example.uberapp_tim12.dto.MessageDTO;
import com.example.uberapp_tim12.dto.MessageListDTO;
import com.example.uberapp_tim12.dto.SendingMessageDTO;
import com.example.uberapp_tim12.model_mock.ChatItem;
import com.example.uberapp_tim12.model_mock.Message;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.UserService;

import java.util.List;
import java.util.Objects;

public class DriverChatActivity extends AppCompatActivity {
    private LinearLayout main_layout;
    private ChatItem chatItem;
    private ScrollView scroll;
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
        EditText newMessage=findViewById(R.id.message_text);
        ImageButton sendButton=findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DriverChatActivity.this, UserService.class);
                intent.putExtra("endpoint", "sendMessage");
                SendingMessageDTO sendingMessageDTO=new SendingMessageDTO(newMessage.getText().toString(),chatItem.getRideId());
                intent.putExtra("messageDTO", sendingMessageDTO); //testirati
                DriverChatActivity.this.startService(intent);
            }
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
            Log.d("PASSSSSSSSS","lalalalaalla");
            MessageDTO messageDTO= (MessageDTO) intent.getSerializableExtra("messageDTO");
            chatItem.getMessages().add(messageDTO);
            main_layout.addView(create(messageDTO));
            scroll.fullScroll(View.FOCUS_DOWN);
        }
    };
}