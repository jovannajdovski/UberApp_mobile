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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.LoginUserDTO;
import com.example.uberapp_tim12.dto.UserTokenDTO;
import com.example.uberapp_tim12.fragments.DriverCurrRideFragment;
import com.example.uberapp_tim12.security.JwtUtil;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.service.CurrentRideService;
import com.example.uberapp_tim12.service.UserService;

import java.sql.Driver;
import java.util.Properties;

public class UserLoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private JwtUtil jwtUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jwtUtil = new JwtUtil();
        setContentView(R.layout.activity_user_login);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black, this.getTheme()));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setTitle("Login");

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(UserLoginActivity.this, UserService.class);
                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                LoginUserDTO loginUserDTO = new LoginUserDTO(email, password);
                intentLogin.putExtra("endpoint", "loginUser");
                intentLogin.putExtra("user", loginUserDTO);
                UserLoginActivity.this.startService(intentLogin);
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(loginReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(loginReceiver, new IntentFilter("logged"));
    }

    public BroadcastReceiver loginReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String found = intent.getStringExtra("found");
            if (found.equals("true")) {
                UserTokenDTO token = (UserTokenDTO) intent.getSerializableExtra("userLoginDTO");
                //zapoceti smenu
                LoggedUser.setUsername(jwtUtil.getUsername(token.getAccessToken()));
                LoggedUser.setUserId(jwtUtil.getId(token.getAccessToken()));
                String role = jwtUtil.getRole(token.getAccessToken());
                LoggedUser.setRole(role);
                LoggedUser.setToken(token.getAccessToken());
                Intent intentLogin;
                if (role.equals("ROLE_PASSENGER")){
                    intentLogin = new Intent(UserLoginActivity.this, PassengerMainActivity.class);
                } else {
                    intentLogin = new Intent(UserLoginActivity.this, DriverMainActivity.class);
                }
                startActivity(intentLogin);
            } else {
                emailEditText.setText("");
                passwordEditText.setText("");
                Toast.makeText(UserLoginActivity.this, "Wrong credentials", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}