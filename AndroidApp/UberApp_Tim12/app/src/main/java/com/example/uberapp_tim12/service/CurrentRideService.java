package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CurrentRideService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final RideFullDTO[] currentRidePassengerDTO = new RideFullDTO[1];
                final RideFullDTO[] currentRideDriverDTO=new RideFullDTO[1];
                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("getActiveRideForDriver"))
                {
                    Log.d("PASSS", "ista metoda");
                    Call<RideFullDTO> call = ControllerUtils.rideController.getActiveRideForDriver(LoggedUser.getUserId(),
                            "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<RideFullDTO>() {
                        @Override
                        public void onResponse(Call<RideFullDTO> call, Response<RideFullDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                currentRideDriverDTO[0]=response.body();
                                Log.d("PASSS", currentRideDriverDTO[0].toString());
                                Intent ints = new Intent ("activeRideDriver");
                                Log.d("PASSS", currentRideDriverDTO[0].toString());
                                ints.putExtra("activeRideDTO", currentRideDriverDTO[0]);
                                ints.putExtra("found", "true");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 404){
                                Intent ints = new Intent ("activeRideDriver");
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 401){
                                Intent ints = new Intent ("activeRideDriver");
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<RideFullDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("getActiveRideForPassenger")){
                    Log.d("PASSS","odma");
                    Call<RideFullDTO> call = ControllerUtils.rideController.getActiveRideForPassenger(LoggedUser.getUserId(),
                            "Bearer "+ LoggedUser.getToken());
                    Log.d("PASSS","posle");
                    call.enqueue(new Callback<RideFullDTO>() {
                        @Override
                        public void onResponse(Call<RideFullDTO> call, Response<RideFullDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                currentRidePassengerDTO[0]=response.body();
                                Log.d("PASSS", currentRidePassengerDTO[0].toString());
                                Intent ints = new Intent ("activeRidePassenger");
                                ints.putExtra("found", "true");
                                Log.d("PASSS", currentRidePassengerDTO[0].toString());
                                ints.putExtra("activeRideDTO", currentRidePassengerDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 404){
                                Intent ints = new Intent ("activeRidePassenger");
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else if (response.code() == 401){
                                Intent ints = new Intent ("activeRidePassenger");
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            } else{
                                Log.d("PASSS","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<RideFullDTO> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null?t.getMessage():"error");
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
