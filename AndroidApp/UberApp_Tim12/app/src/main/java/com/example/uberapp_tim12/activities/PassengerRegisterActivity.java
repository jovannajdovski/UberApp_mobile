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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
import com.example.uberapp_tim12.service.PassengerService;

public class PassengerRegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_launcher_foreground);
        actionBar.setTitle("Registration");

        nameEditText = (EditText) findViewById(R.id.editTextName);
        surnameEditText = (EditText) findViewById(R.id.editTextSurname);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        phoneNumberEditText = (EditText) findViewById(R.id.editTextPhone);
        addressEditText = (EditText) findViewById(R.id.editTextAddress);
        passwordEditText = (EditText) findViewById(R.id.editTextPasswordRegister);
        confirmPasswordEditText = (EditText) findViewById(R.id.editTextPasswordRegisterConfirm);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = String.valueOf(nameEditText.getText());
                String surname = String.valueOf(surnameEditText.getText());
                String email = String.valueOf(emailEditText.getText());
                String phone = String.valueOf(phoneNumberEditText.getText());
                String address = String.valueOf(addressEditText.getText());
                String password = String.valueOf(passwordEditText.getText());
                String confirmPassword = String.valueOf(confirmPasswordEditText.getText());

                if (name.equals("") || surname.equals("") || email.equals("") || phone.equals("") || address.equals("")){
                    Toast.makeText(PassengerRegisterActivity.this,"Fields can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6 || confirmPassword.length()<6){
                    Toast.makeText(PassengerRegisterActivity.this,"Password must be min 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)){
                    Toast.makeText(PassengerRegisterActivity.this,"Confirm password not correct", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intentRegistration = new Intent(PassengerRegisterActivity.this, PassengerService.class);
                intentRegistration.putExtra("endpoint", "registerPassenger");
                intentRegistration.putExtra("passenger", new PassengerRegistrationDTO(name,surname,null,phone,email,address,password));
                PassengerRegisterActivity.this.startService(intentRegistration);

            }
        });

        TextView loginLink = findViewById(R.id.loginLinkButton);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerRegisterActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registerReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(registerReceiver, new IntentFilter("registration"));
    }

    public BroadcastReceiver registerReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String register = intent.getStringExtra("register");
            if (register.equals("true")) {
                Toast.makeText(PassengerRegisterActivity.this, "Activation link sent to "+emailEditText.getText()+" !", Toast.LENGTH_SHORT).show();
                Intent loginIntent = new Intent(PassengerRegisterActivity.this, UserLoginActivity.class);
                startActivity(loginIntent);
            } else if (register.equals("emailExists")){
                emailEditText.setText("");
                Toast.makeText(PassengerRegisterActivity.this, "Email already exists!", Toast.LENGTH_SHORT).show();
            } else {
                nameEditText.setText("");
                surnameEditText.setText("");
                emailEditText.setText("");
                phoneNumberEditText.setText("");
                addressEditText.setText("");
                passwordEditText.setText("");
                confirmPasswordEditText.setText("");
                Toast.makeText(PassengerRegisterActivity.this, "Not valid fields!", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
    protected void onRestart() {
        super.onRestart();
    }
}