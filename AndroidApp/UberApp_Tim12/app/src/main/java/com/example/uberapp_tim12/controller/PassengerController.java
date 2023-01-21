package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.NewPasswordDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.UserEmailDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @POST("passenger")
    Call<PassengerDetailsDTO> registerPassenger(@Body PassengerRegistrationDTO passengerRegistrationDTO);

    @GET("passenger/{id}/ride")
    Call<RidePageList> getPassengerRides(@Path("id") Integer id, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") String sort, @Query("from") String from, @Query("to") String to, @Header("Authorization") String token);
}
