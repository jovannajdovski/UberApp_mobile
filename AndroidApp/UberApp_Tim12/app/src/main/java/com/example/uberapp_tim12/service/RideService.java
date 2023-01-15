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
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.model.Ride;
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
                    Call<RidesListDTO> call = ControllerUtils.rideController.getPendingRidesForDriver(3, Constant.jwt);
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
                    Call<Ride> call = ControllerUtils.rideController.acceptRide(rideId, Constant.jwt);
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
                    Call<Ride> call = ControllerUtils.rideController.rejectRide(rideId, reasonDTO, Constant.jwt);
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
                else if(method.equals("getDriverDetails")){
                    final DriverDetailsDTO[] driverDetailsDTO = new DriverDetailsDTO[1];
                    Call<DriverDetailsDTO> call = ControllerUtils.rideController.getDriverDetails(4, Constant.jwt);
                    call.enqueue(new Callback<DriverDetailsDTO>() {
                        @Override
                        public void onResponse(Call<DriverDetailsDTO> call, Response<DriverDetailsDTO> response) {
                            if (response.code() == 200){
                                driverDetailsDTO[0]=response.body();
                            /*JsonObject responseJson = new JsonObject().get(response.body().toString()).getAsJsonObject();
                            Gson gson = new Gson();
                            driverDetailsDTO[0] = gson.fromJson(responseJson, DriverDetailsDTO.class);*/
                                Log.d("REZZZ",driverDetailsDTO[0].toString());
                                sendToReceiver();
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<DriverDetailsDTO> call, Throwable t) {
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

    private void sendToReceiver (){
        Intent intent = new Intent ("ihor"); //put the same message as in the filter you used in the activity when registering the receiver
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
