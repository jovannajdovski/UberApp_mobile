package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.uberapp_tim12.R;

public class DriverAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.driver_toolbar);
        setSupportActionBar(toolbar);

        fillTheForm();
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

    protected void fillTheForm() {
        Intent intent=getIntent();

        TextView profileName = findViewById(R.id.profile_name_text);
        profileName.setText(intent.getStringExtra("name"));
        TextView profileEmail = findViewById(R.id.profile_email_text);
        profileEmail.setText(intent.getStringExtra("email"));
        TextView profile_gender = findViewById(R.id.profile_gender_text);
        profile_gender.setText(intent.getStringExtra("gender"));
        TextView profileBirthday = findViewById(R.id.profile_birthday_text);
        profileBirthday.setText(intent.getStringExtra("birthday"));
        TextView profilePhone = findViewById(R.id.profile_phone_text);
        profilePhone.setText(intent.getStringExtra("phone"));
    }
}