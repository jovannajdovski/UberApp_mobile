package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.PanicDTO;
import com.example.uberapp_tim12.dto.PassengerDetailsDTO;
import com.example.uberapp_tim12.dto.ReasonDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PanicController {
    @PUT("ride/{id}/panic")
    Call<PanicDTO> panicRide(@Path("id") Integer id, @Body ReasonDTO reason, @Header("Authorization") String token);
}
