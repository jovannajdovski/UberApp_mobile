package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface PassengerController {
    @GET("passenger/{id}")
    Call<PassengerDetailsDTO> getPassengerDetails(@Path("id") Integer id, @Header("Authorization") String token);
}
