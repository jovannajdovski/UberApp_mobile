package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.CreateFavoriteDTO;
import com.example.uberapp_tim12.dto.FavoriteFullDTO;
import com.example.uberapp_tim12.dto.FavoriteRouteForPassengerDTO;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("isFavorite"))
                {
                    final FavoriteRouteForPassengerDTO[] favoriteRouteForPassengerDTOS = new FavoriteRouteForPassengerDTO[1];
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("setFavorite");
                    String from=intent.getStringExtra("from");
                    String to=intent.getStringExtra("to");
                    Call<FavoriteRouteForPassengerDTO> call = ControllerUtils.favoriteController.isFavorite(from, to,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<FavoriteRouteForPassengerDTO>() {
                        @Override
                        public void onResponse(Call<FavoriteRouteForPassengerDTO> call, Response<FavoriteRouteForPassengerDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                favoriteRouteForPassengerDTOS[0] =response.body();
                                ints.putExtra("found", "true");
                                ints.putExtra("favoriteRoute", (Serializable) favoriteRouteForPassengerDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<FavoriteRouteForPassengerDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                } else if(method.equals("createFavorite"))
                {
                    final FavoriteFullDTO[] favoriteFullDTOS = new FavoriteFullDTO[1];
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("createFavorite");
                    CreateFavoriteDTO createFavoriteDTO=(CreateFavoriteDTO) intent.getSerializableExtra("favorite");
                    Call<FavoriteFullDTO> call = ControllerUtils.favoriteController.createFavorite(createFavoriteDTO,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<FavoriteFullDTO>() {
                        @Override
                        public void onResponse(Call<FavoriteFullDTO> call, Response<FavoriteFullDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                favoriteFullDTOS[0] =response.body();
                                ints.putExtra("created", "true");
                                ints.putExtra("favoriteRoute", (Serializable) favoriteFullDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("created", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<FavoriteFullDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                } else if(method.equals("deleteFavorite"))
                {
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("deleteFavorite");
                    Integer favoriteId= intent.getIntExtra("favoriteId",0);
                    Call<ResponseBody> call = ControllerUtils.favoriteController.deleteFavorite(favoriteId,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 204){
                                Log.d("PASSS", "200");
                                ints.putExtra("deleted", "true");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("deleted", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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
