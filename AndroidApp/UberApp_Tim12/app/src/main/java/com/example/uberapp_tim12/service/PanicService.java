package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanicService extends Service {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final PanicDTO[] panicDTO = new PanicDTO[1];
                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("panicRide"))
                {
                    Log.d("PASSS", "pocetak");
                    Integer idRide=intent.getIntExtra("idRide",0);
                    String reason=intent.getStringExtra("reason");
                    ReasonDTO reasonDTO = new ReasonDTO(reason);
                    Log.d("PASSS", "ista metoda");
                    Call<PanicDTO> call = ControllerUtils.panicController.panicRide(idRide, reasonDTO ,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaWtpQGVtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlIjoiUk9MRV9QQVNTRU5HRVIiLCJpYXQiOjE2NzM4MDgxODQsImV4cCI6MTY3Mzg5NDU4NH0.4BCYwvuRwTSJveUu7EWNYfKeaAaLIuq0dmIjwjl5ITs");
                    call.enqueue(new Callback<PanicDTO>() {
                        @Override
                        public void onResponse(Call<PanicDTO> call, Response<PanicDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                panicDTO[0]=response.body();
                                Log.d("PASSS", panicDTO[0].toString());
                                Intent ints = new Intent ("panicRideDetails");
                                Log.d("PASSS", panicDTO[0].toString());
                                ints.putExtra("panicDTO", panicDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<PanicDTO> call, Throwable t) {
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
