package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.FullReviewDTO;
import com.example.uberapp_tim12.dto.FullReviewList;
import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;
import com.example.uberapp_tim12.dto.ReviewDTO;
import com.example.uberapp_tim12.dto.ReviewRequestDTO;
import com.example.uberapp_tim12.dto.ReviewsForRideDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReviewController {

    @GET("review/{rideId}")
    Call<List<FullReviewDTO>> getReviewsForRide(@Path("rideId") Integer rideId, @Header("Authorization") String token);

    @POST("review/rides")
    Call<List<ReviewsForRideDTO>> getReviewsForMultipleRide(@Body List<Integer> listIdRides, @Header("Authorization") String token);

    @POST("review/{rideId}/vehicle")
    Call<ReviewDTO> leaveReviewForVehicle(@Path("rideId") Integer rideId, @Body ReviewRequestDTO reviewRequestDTO, @Header("Authorization") String token);

    @POST("review/{rideId}/driver")
    Call<ReviewDTO> leaveReviewForDriver(@Path("rideId") Integer rideId, @Body ReviewRequestDTO reviewRequestDTO, @Header("Authorization") String token);
}
