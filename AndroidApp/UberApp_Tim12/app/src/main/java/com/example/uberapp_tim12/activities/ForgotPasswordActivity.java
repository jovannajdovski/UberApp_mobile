package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.UserService;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = findViewById(R.id.editTextEmail);
        Button resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckEmail = new Intent(ForgotPasswordActivity.this, PassengerService.class);
                String email = String.valueOf(emailEditText.getText());
                intentCheckEmail.putExtra("endpoint", "checkPassengerByEmail");
                intentCheckEmail.putExtra("email", email);
                ForgotPasswordActivity.this.startService(intentCheckEmail);
            }
        });

        TextView registerLink = findViewById(R.id.loginLinkButton);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(forgotPasswordReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sentEmailReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(forgotPasswordReceiver, new IntentFilter("checkByEmail"));
        LocalBroadcastManager.getInstance(this).registerReceiver(sentEmailReceiver, new IntentFilter("resetPassword"));
    }

    public BroadcastReceiver forgotPasswordReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                PassengerDetailsDTO passengerDTO = (PassengerDetailsDTO) intent.getSerializableExtra("passengerDTO");

                Intent intentForgotPassword = new Intent(ForgotPasswordActivity.this, UserService.class);
                Integer id = passengerDTO.getId();
                intentForgotPassword.putExtra("endpoint", "sendResetCodeToEmail");
                intentForgotPassword.putExtra("id", id);
                ForgotPasswordActivity.this.startService(intentForgotPassword);
            } else {
                emailEditText.setText("");
                Toast.makeText(ForgotPasswordActivity.this, "Username does not exists!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public BroadcastReceiver sentEmailReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String sent = intent.getStringExtra("sent");
            if (sent.equals("true")) {
                Toast.makeText(ForgotPasswordActivity.this, "Reset code sent to email!", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(ForgotPasswordActivity.this, UserLoginActivity.class);
                startActivity(loginIntent);
            } else {
                emailEditText.setText("");
                Toast.makeText(ForgotPasswordActivity.this, "Username does not exists!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}