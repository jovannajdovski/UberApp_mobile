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
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.PassengerService;
import com.example.uberapp_tim12.service.UserService;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordEditText = findViewById(R.id.editTextOldPassword);
        newPasswordEditText = findViewById(R.id.editTextNewPassword);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        Button changeButton = findViewById(R.id.changeButton);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = String.valueOf(oldPasswordEditText.getText());
                String newPassword = String.valueOf(newPasswordEditText.getText());
                String confirmPassword = String.valueOf(confirmPasswordEditText.getText());

                if(oldPassword.length()<6 || newPassword.length()<6 || confirmPassword.length()<6){
                    Toast.makeText(ChangePasswordActivity.this,"Password must be min 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)){
                    Toast.makeText(ChangePasswordActivity.this,"Confirm password not correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intentPasswordChange = new Intent(ChangePasswordActivity.this, UserService.class);
                intentPasswordChange.putExtra("endpoint", "changePassword");
                intentPasswordChange.putExtra("oldPassword", oldPassword);
                intentPasswordChange.putExtra("newPassword", newPassword);
                ChangePasswordActivity.this.startService(intentPasswordChange);
            }
        });

        TextView accountLink = findViewById(R.id.accountLinkButton);
        accountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToAccount();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(changePasswordReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(changePasswordReceiver, new IntentFilter("passwordChanged"));
    }

    public BroadcastReceiver changePasswordReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String changed = intent.getStringExtra("changed");
            if (changed.equals("true")) {
                Toast.makeText(ChangePasswordActivity.this, "Password changed!", Toast.LENGTH_SHORT).show();
                backToAccount();
            } else {
                oldPasswordEditText.setText("");
                newPasswordEditText.setText("");
                confirmPasswordEditText.setText("");
                Toast.makeText(ChangePasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void backToAccount(){
        Intent intent;
        String role = LoggedUser.getRole();
        if (role.equals("ROLE_PASSENGER")){
            intent = new Intent(ChangePasswordActivity.this, PassengerAccountActivity.class);
        } else {
            intent = new Intent(ChangePasswordActivity.this, DriverAccountActivity.class);
        }
        startActivity(intent);
    }
}