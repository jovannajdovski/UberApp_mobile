package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.PassengerDTO;
import com.example.uberapp_tim12.dto.UserEmailDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PassengerController {
    @GET("passenger/email/{email}")
    Call<PassengerDTO> getPassengerByEmail(@Path("email") String email, @Header("Authorization") String token);

}
