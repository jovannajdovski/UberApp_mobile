package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.NewPasswordDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.UserEmailDTO;

import retrofit2.Call;

import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.UserDTO;
import com.example.uberapp_tim12.model.RideCostStatistics;
import com.example.uberapp_tim12.model.RideCountStatistics;
import com.example.uberapp_tim12.model.RideDistanceStatistics;
import com.example.uberapp_tim12.model.User;

import java.time.LocalDateTime;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PassengerController {

    @GET("passenger/email/{email}")
    Call<PassengerDTO> getPassengerByEmail(@Path("email") String email, @Header("Authorization") String token);

    @GET("passenger/email/check/{email}")
    Call<PassengerDetailsDTO> checkPassengerByEmail(@Path("email") String email);

    @GET("passenger/{id}")
    Call<PassengerDetailsDTO> getPassengerDetails(@Path("id") Integer id, @Header("Authorization") String token);

    @PUT("passenger/{id}")
    Call<User> updatePassenger(@Path("id") Integer id,
                               @Header("Authorization") String token,
                               @Body UserDTO userDTO);

    @GET("passenger/{id}/ride-count")
    Call<RideCountStatistics> getRideCountStatistics(@Path("id") Integer id,
                                                     @Header("Authorization") String token,
                                                     @Query("from") LocalDateTime from,
                                                     @Query("to") LocalDateTime to);

    @GET("passenger/{id}/distance")
    Call<RideDistanceStatistics> getRideDistanceStatistics(@Path("id") Integer id,
                                                           @Header("Authorization") String token,
                                                           @Query("from") LocalDateTime from,
                                                           @Query("to") LocalDateTime to);

    @GET("passenger/{id}/money-spent")
    Call<RideCostStatistics> getRideCostStatistics(@Path("id") Integer id,
                                                   @Header("Authorization") String token,
                                                   @Query("from") LocalDateTime from,
                                                   @Query("to") LocalDateTime to);
    @POST("passenger")
    Call<PassengerDetailsDTO> registerPassenger(@Body PassengerRegistrationDTO passengerRegistrationDTO);

    @GET("passenger/{id}/ride")
    Call<RidePageList> getPassengerRides(@Path("id") Integer id, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") String sort, @Query("from") String from, @Query("to") String to, @Header("Authorization") String token);
}
