package com.example.uberapp_tim12.controller;

import com.example.uberapp_tim12.dto.ActiveDriverListDTO;
import com.example.uberapp_tim12.dto.RidesListDTO;
import com.example.uberapp_tim12.dto.DriverDetailsDTO;
import com.example.uberapp_tim12.dto.TimeDTO;
import com.example.uberapp_tim12.dto.WorkHoursDTO;

import java.sql.Time;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DriverController {

    @GET("driver/{id}")
    Call<DriverDetailsDTO> getDriverDetails(@Path("id") Integer id, @Header("Authorization") String token);

    @GET("driver/active-drivers")
    Call<ActiveDriverListDTO> getActiveDrivers(@Header("Authorization") String token);

    @POST("driver/{driver-id}/working-hour")
    Call<WorkHoursDTO> startShift(@Path("driver-id") Integer id, @Body TimeDTO time, @Header("Authorization") String token);

    @PUT("driver/working-hour/{work-hour-id}")
    Call<WorkHoursDTO> endShift(@Path("work-hour-id") Integer id, @Body TimeDTO time, @Header("Authorization") String token);
}
