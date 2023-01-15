package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.DriverDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface DriverController {
    @GET("driver/{id}")
    Call<DriverDetailsDTO> getDriverDetails(@Path("id") Integer id, @Header("Authorization") String token);
}
