package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model.ChatItem;
import com.example.uberapp_tim12.model.Message;

import java.util.List;

public class PassengerChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_chat);

        Intent intent=getIntent();
        ChatItem user= intent.getParcelableExtra("user");
        List<Message> messages= (List<Message>) intent.getSerializableExtra("messages");

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(user.getIcon());
        actionBar.setTitle(user.getRoute());

        LinearLayout main_layout = findViewById(R.id.messages_stack);
        for(Message message:messages)
        {
            main_layout.addView(create(message));
        }
        ScrollView scroll=findViewById(R.id.scroll);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private RelativeLayout create(Message message)
    {
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout;
        TextView textView;
        if(message.getSender()== Message.Sender.MYSELF) {
            layout= (RelativeLayout) vi.inflate(R.layout.user_message_layout, null);
            textView = (TextView) layout.findViewById(R.id.message);
        }
        else {
            layout= (RelativeLayout) vi.inflate(R.layout.other_message_layout, null);
            textView = (TextView) layout.findViewById(R.id.message);
        }
        textView.setText(message.getText());
        return layout;
    }
}