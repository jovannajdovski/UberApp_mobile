package com.example.uberapp_tim12.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.model.User;

import java.util.Locale;

public class DriverAccountActivity extends AppCompatActivity {
    private enum ValidationType {
        allLetters,
        phoneNumber,
        address
    }

    TextView profileName;
    TextView profileSurname;
    TextView profileEmail;
    TextView profileAddress;
    TextView profilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black,this.getTheme()));

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.ic_profile);
        actionBar.setTitle("");

        initializeGUI();
        addActionListeners();
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

    protected void initializeGUI() {
        profileName = findViewById(R.id.profile_name_text);
        profileSurname = findViewById(R.id.profile_surname_text);
        profileEmail = findViewById(R.id.profile_email_text);
        profileAddress = findViewById(R.id.profile_address_text);
        profilePhone = findViewById(R.id.profile_phone_text);

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        profileName.setText(user.getName());
        profileSurname.setText(user.getSurname());
        profileEmail.setText(user.getEmail());
        profileAddress.setText(user.getAddress());
        profilePhone.setText(user.getPhoneNumber());
    }

    protected void addActionListeners() {
        ConstraintLayout profileNameLayout = findViewById(R.id.profile_name);
        profileNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Name", profileName, ValidationType.allLetters);
            }
        });

        ConstraintLayout profileSurnameLayout = findViewById(R.id.profile_surname);
        profileSurnameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Surname", profileSurname, ValidationType.allLetters);
            }
        });

        ConstraintLayout profileAddressLayout = findViewById(R.id.profile_address);
        profileAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Address", profileAddress, ValidationType.address);
            }
        });

        ConstraintLayout profilePhoneLayout = findViewById(R.id.profile_phone);
        profilePhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Phone", profilePhone, ValidationType.phoneNumber);
            }
        });
    }

    protected void createDialog(String title, TextView view, ValidationType validationType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverAccountActivity.this);

        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customLayout);
        builder.setTitle(title);
        EditText editText = customLayout.findViewById(R.id.edit_text);
        editText.setText(view.getText().toString());

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Context context = getApplicationContext();
                CharSequence text = "Request for " + title.toLowerCase(Locale.ROOT) + " change has been made.";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(validateField(editText.getText().toString(), validationType));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    protected boolean validateField(String text, ValidationType validationType) {
        if (validationType == ValidationType.allLetters)
            return isAllText(text);
        else if (validationType == ValidationType.phoneNumber)
            return isPhoneNumber(text);
        else if (validationType == ValidationType.address)
            return isAddress(text);
        return false;
    }

    protected boolean isAllText(String text) {
        return text.length() > 0 && text.matches("[a-zA-Z]+");
    }

    protected boolean isPhoneNumber(String text) {
        return PhoneNumberUtils.isGlobalPhoneNumber(text);
    }

    protected boolean isAddress(String text) {
        return text.length() > 0 && text.matches("^[#.0-9a-zA-Z\\s,-]+$");
    }
}