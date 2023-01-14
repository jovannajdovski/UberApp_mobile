package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
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
                final DriverDetailsDTO[] driverDetailsDTO = new DriverDetailsDTO[1];
                final RidesListDTO[] ridesListDTOS=new RidesListDTO[1];
                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("getPendingRidesForDriver"))
                {
                    Log.d("PASSS", "ista metoda");
                    Call<RidesListDTO> call = ControllerUtils.rideController.getPendingRidesForDriver(3, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6b2tpQGVtYWlsLmNvbSIsImp0aSI6IjMiLCJyb2xlIjoiUk9MRV9EUklWRVIiLCJpYXQiOjE2NzM3MTgxMDMsImV4cCI6MTY3MzgwNDUwM30.id8Qf364-g0BAS83oDhRQz0EBxleyV6IRLxn7ZBQp9Q");
                    call.enqueue(new Callback<RidesListDTO>() {
                        @Override
                        public void onResponse(Call<RidesListDTO> call, Response<RidesListDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                ridesListDTOS[0]=response.body();
                                Log.d("PASSS", ridesListDTOS[0].getTotalCount().toString());
                                Intent ints = new Intent ("pendingRides");
                                Log.d("PASSS", ridesListDTOS[0].getTotalCount().toString());
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
                else if(method.equals("getDriverDetails")){
                    Call<DriverDetailsDTO> call = ControllerUtils.rideController.getDriverDetails(4, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb2tpQGVtYWlsLmNvbSIsImp0aSI6IjYiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3MzcwMjc5NSwiZXhwIjoxNjczNzg5MTk1fQ.LK0gdiNRMCswl6w5Wks99DR5k39FRlUwDk34PBdsIc8");
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
