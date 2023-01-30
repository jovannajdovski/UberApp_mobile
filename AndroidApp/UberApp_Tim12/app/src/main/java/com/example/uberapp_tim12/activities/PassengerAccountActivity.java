package com.example.uberapp_tim12.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uberapp_tim12.R;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.controller.PassengerController;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.UserDTO;
import com.example.uberapp_tim12.model.User;
import com.example.uberapp_tim12.security.LoggedUser;
import com.example.uberapp_tim12.tools.ImageConverter;
import com.example.uberapp_tim12.tools.SnackbarUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PassengerAccountActivity extends AppCompatActivity {
    private enum ValidationType {
        allLetters,
        phoneNumber,
        address
    }
    TextView profileName, profileSurname, profileEmail, profileAddress, profilePhone;
    ImageView profileImage;
    PassengerDetailsDTO passengerDetails;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);

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
        profileImage = findViewById(R.id.profile_image);

        updateUI();
    }

    private void updateUI() {
        Retrofit retrofit = ControllerUtils.retrofit;
        PassengerController controller = retrofit.create(PassengerController.class);

        Call<PassengerDetailsDTO> call = controller.getPassengerDetails(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer());
        call.enqueue(new Callback<PassengerDetailsDTO>() {
            @Override
            public void onResponse(Call<PassengerDetailsDTO> call, Response<PassengerDetailsDTO> response) {
                if (response.code() == 200) {
                    passengerDetails = response.body();
                    profileName.setText(passengerDetails.getName());
                    profileSurname.setText(passengerDetails.getSurname());
                    profileEmail.setText(passengerDetails.getEmail());
                    profileAddress.setText(passengerDetails.getAddress());
                    profilePhone.setText(passengerDetails.getTelephoneNumber());
                    if (passengerDetails.getProfilePicture() != null)
                        profileImage.setImageBitmap(ImageConverter.decodeToImage(passengerDetails.getProfilePicture()));
                } else {
                    SnackbarUtil.show(findViewById(R.id.passenger_account),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<PassengerDetailsDTO> call, Throwable t) {
                SnackbarUtil.show(findViewById(R.id.passenger_account),
                        "Something went wrong!");
            }
        });
    }

    protected void addActionListeners() {
        MaterialCardView profilePictureLayout = findViewById(R.id.profile_picture_change);
        profilePictureLayout.setOnClickListener(view ->
                showImagePicker());

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

    private void showImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                passengerDetails.setProfilePicture(ImageConverter.encodeToString(bitmap));
                updatePassenger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePassenger() {
        UserDTO user = new UserDTO(passengerDetails.getName(),
                passengerDetails.getSurname(),
                passengerDetails.getProfilePicture(),
                passengerDetails.getTelephoneNumber(),
                passengerDetails.getEmail(),
                passengerDetails.getAddress());
        Retrofit retrofit = ControllerUtils.retrofit;
        PassengerController controller = retrofit.create(PassengerController.class);

        Call<User> call = controller.updatePassenger(LoggedUser.getUserId(),
                LoggedUser.getTokenWithBearer(), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    updateUI();
                    SnackbarUtil.show(findViewById(R.id.passenger_account),
                            "Account updated successfully.");
                } else {
                    SnackbarUtil.show(findViewById(R.id.passenger_account),
                            "Something went wrong!");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                SnackbarUtil.show(findViewById(R.id.passenger_account),
                        "Something went wrong!");
            }
        });

        MaterialCardView profilePasswordLayout = findViewById(R.id.password_change);
        profilePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PassengerAccountActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
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
                                    passengerDetails.setName(input);
                                    break;
                                case "Surname":
                                    passengerDetails.setSurname(input);
                                    break;
                                case "Address":
                                    passengerDetails.setAddress(input);
                                    break;
                                case "Phone":
                                    passengerDetails.setTelephoneNumber(input);
                                    break;
                            }
                            updatePassenger();
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
}