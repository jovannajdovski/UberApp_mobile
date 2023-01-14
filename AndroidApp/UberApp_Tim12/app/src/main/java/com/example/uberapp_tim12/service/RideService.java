package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
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

        final String[] s = {intent.getStringExtra("pas")};
        //dobavljanje podataka iz intenta
        executor.execute(new Runnable() {
            @Override
            public void run() {
                s[0] = s[0].concat("ss");
                Log.d("PASSSSSS", s[0]);
                final DriverDetailsDTO[] driverDetailsDTO = new DriverDetailsDTO[1];

                Call<DriverDetailsDTO> call = ControllerUtils.rideController.getDriverDetails(4, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2tpQGVtYWlsLmNvbSIsImp0aSI6IjYiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3MzY2MzAwNywiZXhwIjoxNjczNzQ5NDA3fQ.zFUFtHeLucGjGVoMOYMSD9W7iVWTUuntlZN6y7ETqgI");
                call.enqueue(new Callback<DriverDetailsDTO>() {
                    @Override
                    public void onResponse(Call<DriverDetailsDTO> call, Response<DriverDetailsDTO> response) {
                        if (response.code() == 200){
                            driverDetailsDTO[0]=response.body();
                            /*JsonObject responseJson = new JsonObject().get(response.body().toString()).getAsJsonObject();
                            Gson gson = new Gson();
                            driverDetailsDTO[0] = gson.fromJson(responseJson, DriverDetailsDTO.class);*/
                            Log.d("REZZZ",driverDetailsDTO[0].toString());
                            sendToReceiver(s[0]);
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
        });

        //slanje podataka iz odgovora

        stopSelf();
        return START_NOT_STICKY;
    }

    private void sendToReceiver (String s){
        Intent intent = new Intent ("ihor"); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra("pas", s);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
