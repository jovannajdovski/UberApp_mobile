package com.example.uberapp_tim12.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.uberapp_tim12.controller.ControllerUtils;
import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.FullReviewList;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.ReviewDTO;
import com.example.uberapp_tim12.dto.ReviewRequestDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;
import com.example.uberapp_tim12.security.LoggedUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewService extends Service {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        //dobavljanje podataka iz intenta
        String method=intent.getStringExtra("endpoint");

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("PASSS", "Metoda ".concat(method));
                if(method.equals("getReviewsForRide"))
                {
                    final List<FullReviewDTO>[] fullReviewDTOs = new List[]{new ArrayList<FullReviewDTO>()};
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("reviewsForRide");
                    Integer idRide=intent.getIntExtra("idRide",0);
                    Call<List<FullReviewDTO>> call = ControllerUtils.reviewController.getReviewsForRide(idRide,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<List<FullReviewDTO>>() {
                        @Override
                        public void onResponse(Call<List<FullReviewDTO>> call, Response<List<FullReviewDTO>> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                fullReviewDTOs[0] =response.body();
                                ints.putExtra("found", "true");
                                ints.putExtra("reviews", (Serializable) fullReviewDTOs[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<FullReviewDTO>> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                } else if(method.equals("getReviewsForMultipleRide"))
                {
                    final List<ReviewsForRideDTO>[] fullReviewDTOs = new List[]{new ArrayList<ReviewsForRideDTO>()};
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("reviewsForMultipleRide");
                    List<Integer> idRides=intent.getIntegerArrayListExtra("idRides");
                    Call<List<ReviewsForRideDTO>> call = ControllerUtils.reviewController.getReviewsForMultipleRide(idRides,"Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<List<ReviewsForRideDTO>>() {
                        @Override
                        public void onResponse(Call<List<ReviewsForRideDTO>> call, Response<List<ReviewsForRideDTO>> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                fullReviewDTOs[0] =response.body();
                                ints.putExtra("found", "true");
                                ints.putExtra("reviews", (Serializable) fullReviewDTOs[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ReviewsForRideDTO>> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                } else if(method.equals("leaveReviewForVehicle"))
                {
                    final ReviewDTO[] reviewDTOS = new ReviewDTO[1];
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("reviewForVehicleLeave");
                    ReviewRequestDTO reviewRequestDTO = (ReviewRequestDTO) intent.getSerializableExtra("reviewRequest");
                    Integer idRide=intent.getIntExtra("idRide",0);
                    Call<ReviewDTO> call = ControllerUtils.reviewController.leaveReviewForVehicle(idRide,reviewRequestDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<ReviewDTO>() {
                        @Override
                        public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                reviewDTOS[0] =response.body();
                                ints.putExtra("found", "true");
                                ints.putExtra("review", reviewDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ReviewDTO> call, Throwable t) {
                            Log.d("REZZ", t.getMessage() != null?t.getMessage():"error");
                        }
                    });
                } else if(method.equals("leaveReviewForDriver"))
                {
                    final ReviewDTO[] reviewDTOS = new ReviewDTO[1];
                    Log.d("PASSS", "pocetak");
                    Intent ints = new Intent ("reviewForDriverLeave");
                    ReviewRequestDTO reviewRequestDTO = (ReviewRequestDTO) intent.getSerializableExtra("reviewRequest");
                    Integer idRide=intent.getIntExtra("idRide",0);
                    Call<ReviewDTO> call = ControllerUtils.reviewController.leaveReviewForDriver(idRide,reviewRequestDTO, "Bearer "+ LoggedUser.getToken());
                    call.enqueue(new Callback<ReviewDTO>() {
                        @Override
                        public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                            if (response.code() == 200){
                                Log.d("PASSS", "200");
                                reviewDTOS[0] =response.body();
                                ints.putExtra("found", "true");
                                ints.putExtra("review", reviewDTOS[0]);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                            }else{
                                ints.putExtra("found", "false");
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(ints);
                                Log.d("REZZZZZ","Meesage recieved: "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ReviewDTO> call, Throwable t) {
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
