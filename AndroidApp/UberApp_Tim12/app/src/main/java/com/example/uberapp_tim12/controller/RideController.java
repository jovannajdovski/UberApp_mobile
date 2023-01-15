package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.RideFullDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RideController {
    @GET("driver/{id}")
    Call<DriverDetailsDTO> getDriverDetails(@Path("id") Integer id,
                                            @Header("Authorization") String token);
    @GET("driver/{id}/ride/pending")
    Call<RidesListDTO> getPendingRidesForDriver(@Path("id") Integer id, @Header("Authorization") String token);

    @GET("ride/driver/{driverId}/active")
    Call<RideFullDTO> getActiveRideForDriver(@Path("driverId") Integer driverId, @Header("Authorization") String token);

    @GET("ride/passenger/{passengerId}/active")
    Call<RideFullDTO> getActiveRideForPassenger(@Path("passengerId") Integer passengerId, @Header("Authorization") String token);
}
