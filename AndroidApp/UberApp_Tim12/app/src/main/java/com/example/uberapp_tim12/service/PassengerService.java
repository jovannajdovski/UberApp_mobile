package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.uberapp_tim12.Constant;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerService extends Service {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        Log.d("PASSS", "Metoda ".concat(method));
        executor.execute(new Runnable() {
            @Override
            public void run() {

                if(method.equals("getPassengerByEmail"))
                {
                    Log.d("PASSS",intent.getStringExtra("email"));
                    Intent ints = new Intent ("userByEmail");
                    UserEmailDTO emailDTO=new UserEmailDTO(intent.getStringExtra("email"));

                    final PassengerDTO[] passengerDTOS=new PassengerDTO[1];
                    Log.d("PASSS", "zove bek ");
                    Call<PassengerDTO> call = ControllerUtils.passengerController.getPassengerByEmail(intent.getStringExtra("email"), "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<PassengerDTO>() {
                        @Override
                        public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                            if (response.code() == 200) {
                                Log.d("PASSS", "usao");
                                passengerDTOS[0] = response.body();
                                ints.putExtra("passengerDTO", passengerDTOS[0]);
                            } else {
                                ints.putExtra("responseMessage", response.message());
                                Log.d("PASSS", "Meesage recieved: " + response.code());
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }

                        @Override
                        public void onFailure(Call<PassengerDTO> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
                        }
                    });
                }
                else if(method.equals("getPassengerDetails"))
                {
                    Integer passengerId=intent.getIntExtra("passengerId",0);
                    Log.d("PASSS", "ista metoda");
                    PassengerDetailsDTO[] passengerDetailsDTO=new PassengerDetailsDTO[1];
                    Call<PassengerDetailsDTO> call = ControllerUtils.passengerController.getPassengerDetails(passengerId, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<PassengerDetailsDTO>() {
                        @Override
                        public void onResponse(Call<PassengerDetailsDTO> call, Response<PassengerDetailsDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                passengerDetailsDTO[0]=response.body();
                                Log.d("PASSS", passengerDetailsDTO[0].toString());
                                Intent ints = new Intent ("passengerDetails");
                                Log.d("PASSS", passengerDetailsDTO[0].toString());
                                ints.putExtra("passengerDetailsDTO", passengerDetailsDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<PassengerDetailsDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else{
                    stopSelf();
                }
            }
        });

        stopSelf();
        return START_NOT_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
