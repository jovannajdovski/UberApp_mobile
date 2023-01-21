package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.Constant;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.CreateRideDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RideIdListDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.security.LoggedUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");
        Log.d("PASSS", "Metoda ".concat(method));
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("getPendingRidesForDriver"))
                {
                    final RidesListDTO[] ridesListDTOS=new RidesListDTO[1];
                    Call<RidesListDTO> call = ControllerUtils.rideController.getPendingRidesForDriver(LoggedUser.getUserId(), "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<RidesListDTO>() {
                        @Override
                        public void onResponse(Call<RidesListDTO> call, Response<RidesListDTO> response) {
                            if (response.code() == 200){
                                Log.d("REZZ", "usao");
                                ridesListDTOS[0]=response.body();
                                Intent ints = new Intent ("pendingRides");
                                ints.putExtra("pendingRidesDTO", ridesListDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<RidesListDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("acceptRide")){
                    final Ride[] ride = new Ride[1];
                    Integer rideId=intent.getIntExtra("rideId",-1);
                    Call<Ride> call = ControllerUtils.rideController.acceptRide(rideId, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<Ride>() {
                        @Override
                        public void onResponse(Call<Ride> call, Response<Ride> response) {
                            if (response.code() == 200){
                                ride[0] =response.body();
                                Intent ints = new Intent ("acceptRide");
                                ints.putExtra("ride", ride[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Ride> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("rejectRide")){
                    final Ride[] ride = new Ride[1];
                    ReasonDTO reasonDTO=intent.getParcelableExtra("reasonDTO");
                    Integer rideId=intent.getIntExtra("rideId",-1);
                    Call<Ride> call = ControllerUtils.rideController.rejectRide(rideId, reasonDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<Ride>() {
                        @Override
                        public void onResponse(Call<Ride> call, Response<Ride> response) {
                            if (response.code() == 200){
                                ride[0] =response.body();
                                Log.d("PASSS",ride[0].toString());
                                Intent ints = new Intent ("rejectRide");
                                ints.putExtra("ride", ride[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Ride> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("createRide")){
                    final RideFullDTO[] ride = new RideFullDTO[1];
                    CreateRideDTO createRideDTO= (CreateRideDTO) intent.getSerializableExtra("ride");
                    Log.d("PASSS", createRideDTO.getScheduledTime().toString());
                    Call<RideFullDTO> call = ControllerUtils.rideController.createRide(createRideDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<RideFullDTO>() {
                        Intent ints = new Intent ("createRide");

                        @Override
                        public void onResponse(Call<RideFullDTO> call, Response<RideFullDTO> response) {
                            if (response.code() == 200){
                                ride[0] =response.body();
                                ints.putExtra("ride", ride[0]);
                                }
                            else{
                                ints.putExtra("responseMessage", response.message());
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);


                        }

                        @Override
                        public void onFailure(Call<RideFullDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("getRidesDetails")){
                    Log.d("PASSS", "Usao u else if");
                    final RidesListDTO[] rides = new RidesListDTO[1];
                    RideIdListDTO rideIdListDTO= (RideIdListDTO) intent.getSerializableExtra("rideIdList");
                    Call<RidesListDTO> call = ControllerUtils.rideController.getRidesDetailsFromIdList(rideIdListDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<RidesListDTO>() {
                        Intent ints = new Intent ("ridesDetails");
                        @Override
                        public void onResponse(Call<RidesListDTO> call, Response<RidesListDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "odgovor 200");
                                rides[0] =response.body();
                                ints.putExtra("ridesDetailsDTO", rides[0]);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }

                        @Override
                        public void onFailure(Call<RidesListDTO> call, Throwable t) {
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
