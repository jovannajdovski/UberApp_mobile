package com.example.uberapp_tim12.activities;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.DriverController;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.UserDTO;
import com.example.uberapp_tim12.model.User;
import com.example.uberapp_tim12.security.LoggedUser;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverAccountActivity extends AppCompatActivity {
    private enum ValidationType {
        allLetters,
        phoneNumber,
        address
    }
    TextView profileName, profileSurname, profileEmail, profileAddress, profilePhone;
    DriverDetailsDTO driverDetails;

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

    protected void initializeGUI() {
        profileName = findViewById(R.id.profile_name_text);
        profileSurname = findViewById(R.id.profile_surname_text);
        profileEmail = findViewById(R.id.profile_email_text);
        profileAddress = findViewById(R.id.profile_address_text);
        profilePhone = findViewById(R.id.profile_phone_text);

        updateUI();
    }

    private void updateUI() {
        Retrofit retrofit = ControllerUtils.retrofit;
        DriverController controller = retrofit.create(DriverController.class);

        Call<DriverDetailsDTO> call = controller.getDriverDetails(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<DriverDetailsDTO>() {
            @Override
            public void onResponse(Call<DriverDetailsDTO> call, Response<DriverDetailsDTO> response) {
                if (response.code() == 200) {
                    driverDetails = response.body();
                    profileName.setText(driverDetails.getName());
                    profileSurname.setText(driverDetails.getSurname());
                    profileEmail.setText(driverDetails.getEmail());
                    profileAddress.setText(driverDetails.getAddress());
                    profilePhone.setText(driverDetails.getTelephoneNumber());
                } else {
                    showMessage(findViewById(android.R.id.content).getRootView(),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<DriverDetailsDTO> call, Throwable t) {
                showMessage(findViewById(android.R.id.content).getRootView(),
                        "Something went wrong!");
            }
        });
    }

    protected void addActionListeners() {
        MaterialCardView profileNameLayout = findViewById(R.id.profile_name);
        profileNameLayout.setOnClickListener(view ->
                createDialog("Name", profileName.getText().toString(), ValidationType.allLetters));

        MaterialCardView profileSurnameLayout = findViewById(R.id.profile_surname);
        profileSurnameLayout.setOnClickListener(view ->
                createDialog("Surname", profileSurname.getText().toString(), ValidationType.allLetters));

        MaterialCardView profileAddressLayout = findViewById(R.id.profile_address);
        profileAddressLayout.setOnClickListener(view ->
                createDialog("Address", profileAddress.getText().toString(), ValidationType.address));

        MaterialCardView profilePhoneLayout = findViewById(R.id.profile_phone);
        profilePhoneLayout.setOnClickListener(view ->
                createDialog("Phone", profilePhone.getText().toString(), ValidationType.phoneNumber));
    }

    private void updateDriver() {
        UserDTO user = new UserDTO(driverDetails.getName(),
                driverDetails.getSurname(),
                driverDetails.getProfilePicture(),
                driverDetails.getTelephoneNumber(),
                driverDetails.getEmail(),
                driverDetails.getAddress());
        Retrofit retrofit = ControllerUtils.retrofit;
        DriverController controller = retrofit.create(DriverController.class);

        Call<User> call = controller.updateDriver(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    updateUI();
                    showMessage(findViewById(android.R.id.content).getRootView(),
                            "Account updated successfully.");
                } else {
                    showMessage(findViewById(android.R.id.content).getRootView(),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showMessage(findViewById(android.R.id.content).getRootView(),
                        "Something went wrong!");
            }
        });
    }

    protected void createDialog(String title, String fieldName, ValidationType validationType) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        EditText editText = view.findViewById(R.id.edit_text);
        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(this.getResources().getString(R.string.confirm),
                        (dialogInterface, i) -> {
                String input = editText.getText().toString();
                    switch (title) {
                        case "Name":
                            driverDetails.setName(input);
                            break;
                        case "Surname":
                            driverDetails.setSurname(input);
                            break;
                        case "Address":
                            driverDetails.setAddress(input);
                            break;
                        case "Phone":
                            driverDetails.setTelephoneNumber(input);
                            break;
                    }
                    updateDriver();
                })
                .setNegativeButton(this.getResources().getString(R.string.cancel),
                        (dialogInterface, i) -> {
                })
                .show();

        final Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);

        editText.setText(fieldName);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setEnabled(validateField(editText.getText().toString(), validationType));
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

    private void showMessage(View view, String message) {
        Toast toast = new Toast(view.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }
}