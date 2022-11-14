package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberapp_tim12.R;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setTitle("Login");

        EditText emailEditText=findViewById(R.id.editTextEmail);
        EditText passwordEditText=findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(emailEditText.getText().toString().equals("driver"))
                    intent = new Intent(UserLoginActivity.this, DriverMainActivity.class);
                else
                    intent = new Intent(UserLoginActivity.this, PassengerMainActivity.class);

                startActivity(intent);
            }
        });

        TextView registerLink = findViewById(R.id.registerLinkButton);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, PassengerRegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}