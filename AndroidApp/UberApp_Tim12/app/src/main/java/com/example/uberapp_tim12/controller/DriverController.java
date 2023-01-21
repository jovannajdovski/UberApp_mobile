package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.RidePageList;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverController {

    @GET("driver/{id}")
    Call<DriverDetailsDTO> getDriverDetails(@Path("id") Integer id, @Header("Authorization") String token);

    @GET("driver/{id}/ride")
    Call<RidePageList> getDriverRides(@Path("id") Integer id, @Query("page") Integer page, @Query("size") Integer size, @Query("sort") String sort, @Query("from") String from, @Query("to") String to, @Header("Authorization") String token);
}
