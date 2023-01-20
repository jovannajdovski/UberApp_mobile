package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.ActiveDriverListDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.EndTimeDTO;
import com.example.uberapp_tim12.dto.StartTimeDTO;
import com.example.uberapp_tim12.dto.WorkHoursDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverService extends Service {
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("getDriverDetails"))
                {
                    final DriverDetailsDTO[] driverDetailsDTO = new DriverDetailsDTO[1];
                    Integer driverId=intent.getIntExtra("driverId",0);
                    Log.d("PASSS", "ista metoda");
                    Call<DriverDetailsDTO> call = ControllerUtils.driverController.getDriverDetails(driverId, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<DriverDetailsDTO>() {
                        @Override
                        public void onResponse(Call<DriverDetailsDTO> call, Response<DriverDetailsDTO> response) {
                            Intent ints = new Intent ("driverDetails");
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                driverDetailsDTO[0]=response.body();
                                Log.d("PASSS", driverDetailsDTO[0].toString());

                                Log.d("PASSS", driverDetailsDTO[0].toString());
                                ints.putExtra("driverDetailsDTO", driverDetailsDTO[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<DriverDetailsDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("getActiveDrivers"))
                {
                    final ActiveDriverListDTO[] activeDriverListDTOS = new ActiveDriverListDTO[1];
                    Log.d("PASSS", "ista metoda");
                    Call<ActiveDriverListDTO> call = ControllerUtils.driverController.getActiveDrivers("Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<ActiveDriverListDTO>() {
                        @Override
                        public void onResponse(Call<ActiveDriverListDTO> call, Response<ActiveDriverListDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                activeDriverListDTOS[0]=response.body();
                                Log.d("PASSSSSSSSSSSS", activeDriverListDTOS[0].getTotalCount().toString());
                                Intent ints = new Intent ("activeDrivers");
                                ints.putExtra("activeDriversDTO", activeDriverListDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ActiveDriverListDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("startShift"))
                {
                    final WorkHoursDTO[] workHoursDTOS = new WorkHoursDTO[1];
                    Log.d("PASSSSStart", "ista metoda");
                    StartTimeDTO timeDTO=new StartTimeDTO(LocalDateTime.now().toString());
                    Log.d("PASSSSStart",timeDTO.getDateTime());
                    Call<WorkHoursDTO> call = ControllerUtils.driverController.startShift(LoggedUser.getUserId(),timeDTO,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<WorkHoursDTO>() {
                        @Override
                        public void onResponse(Call<WorkHoursDTO> call, Response<WorkHoursDTO> response) {
                            Intent ints = new Intent ("startShift");
                            if (response.code() == 200){
                                Log.d("PASSSSStart", "200");
                                workHoursDTOS[0]=response.body();
                                Log.d("PASSSSStart", workHoursDTOS[0].toString());
                                Log.d("PASSSSStart", workHoursDTOS[0].getId().toString());

                                ints.putExtra("workHoursDTO", workHoursDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else if(response.code()==400){
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<WorkHoursDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                }
                else if(method.equals("endShift"))
                {
                    final WorkHoursDTO[] workHoursDTOS = new WorkHoursDTO[1];
                    Log.d("PASSS", "ista metoda");
                    Integer workHourId=intent.getIntExtra("workHourId",0);
                    EndTimeDTO timeDTO=new EndTimeDTO(LocalDateTime.now().toString());
                    Call<WorkHoursDTO> call = ControllerUtils.driverController.endShift(workHourId,timeDTO,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<WorkHoursDTO>() {
                        @Override
                        public void onResponse(Call<WorkHoursDTO> call, Response<WorkHoursDTO> response) {
                            Intent ints = new Intent ("endShift");
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                workHoursDTOS[0]=response.body();
                                Log.d("PASSS", workHoursDTOS[0].toString());

                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else if(response.code()==404){
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }
                            else{
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<WorkHoursDTO> call, Throwable t) {
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
