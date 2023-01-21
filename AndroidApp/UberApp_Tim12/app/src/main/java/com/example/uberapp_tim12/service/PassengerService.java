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
import com.example.uberapp_tim12.dto.NewPasswordDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;
import com.example.uberapp_tim12.model.Ride;
import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
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
                } else if(method.equals("checkPassengerByEmail"))
                {
                    UserEmailDTO emailDTO=new UserEmailDTO(intent.getStringExtra("email"));
                    Intent ints = new Intent ("checkByEmail");

                    final PassengerDetailsDTO[] passengerDTOS=new PassengerDetailsDTO[1];
                    Log.d("PASSS", intent.getStringExtra("email"));
                    Call<PassengerDetailsDTO> call = ControllerUtils.passengerController.checkPassengerByEmail(intent.getStringExtra("email"));
                    call.enqueue(new Callback<PassengerDetailsDTO>() {
                        @Override
                        public void onResponse(Call<PassengerDetailsDTO> call, Response<PassengerDetailsDTO> response) {
                            Log.d("PASSS", "gde");
                            if (response.code() == 200) {
                                ints.putExtra("found", "true");
                                Log.d("PASSS", "usao");
                                passengerDTOS[0] = response.body();
                                ints.putExtra("passengerDTO", passengerDTOS[0]);
                            } else if (response.code() == 404){
                                ints.putExtra("found", "false");
                            }else {
                                ints.putExtra("responseMessage", response.message());
                                Log.d("PASSS", "Meesage recieved: " + response.code());
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }

                        @Override
                        public void onFailure(Call<PassengerDetailsDTO> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
                        }
                    });
                }else if(method.equals("registerPassenger"))
                {
                    Intent ints = new Intent ("registration");
                    PassengerRegistrationDTO passengerRegistrationDTO = (PassengerRegistrationDTO) intent.getSerializableExtra("passenger");

                    Call<PassengerDetailsDTO> call = ControllerUtils.passengerController.registerPassenger(passengerRegistrationDTO);
                    call.enqueue(new Callback<PassengerDetailsDTO>() {
                        @Override
                        public void onResponse(Call<PassengerDetailsDTO> call, Response<PassengerDetailsDTO> response) {
                            if (response.code() == 200) {
                                ints.putExtra("register", "true");
                            } else if (response.code() == 400){
                                ints.putExtra("register", "emailExists");
                            } else {
                                ints.putExtra("register", "false");
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }
                        @Override
                        public void onFailure(Call<PassengerDetailsDTO> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
                        }
                    });
                } else if(method.equals("getPastRides"))
                {
                    Intent ints = new Intent ("pastRides");
                    final RidePageList[] ridesListDTOS=new RidePageList[1];
                    Call<RidePageList> call = ControllerUtils.passengerController.getPassengerRides(LoggedUser.getUserId(),
                            0, 100, "startTime,asc", LocalDateTime.now().minusYears(1)+"", LocalDateTime.now()+"", "Bearer "+ LoggedUser.getToken() );
                    call.enqueue(new Callback<RidePageList>() {
                        @Override
                        public void onResponse(Call<RidePageList> call, Response<RidePageList> response) {
                            if (response.code() == 200) {
                                ints.putExtra("found", "true");
                                ridesListDTOS[0]=response.body();
                                ints.putExtra("ridesListDTOS", ridesListDTOS[0]);
                            } else if (response.code() == 404){
                                ints.putExtra("found", "false");
                            } else {
                                ints.putExtra("found", "false");
                            }
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                        }
                        @Override
                        public void onFailure(Call<RidePageList> call, Throwable t) {
                            Log.d("PASSS", t.getMessage() != null ? t.getMessage() : "error");
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
