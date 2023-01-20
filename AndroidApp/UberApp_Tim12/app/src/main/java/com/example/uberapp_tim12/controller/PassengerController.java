package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.NewPasswordDTO;
import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.PassengerRegistrationDTO;
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

public interface PassengerController {

    @GET("passenger/email/{email}")
    Call<PassengerDTO> getPassengerByEmail(@Path("email") String email, @Header("Authorization") String token);

    @GET("passenger/email/check/{email}")
    Call<PassengerDetailsDTO> checkPassengerByEmail(@Path("email") String email);

    @GET("passenger/{id}")
    Call<PassengerDetailsDTO> getPassengerDetails(@Path("id") Integer id, @Header("Authorization") String token);

    @POST("passenger")
    Call<PassengerDetailsDTO> registerPassenger(@Body PassengerRegistrationDTO passengerRegistrationDTO);
}
